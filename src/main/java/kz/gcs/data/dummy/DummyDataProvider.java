package kz.gcs.data.dummy;

import com.google.common.base.Predicate;
import com.google.common.collect.*;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vaadin.server.VaadinSession;
import kz.gcs.data.DataProvider;
import kz.gcs.data.service.PositionService;
import kz.gcs.data.service.UserService;
import kz.gcs.domain.*;
import kz.gcs.rest.CommandRestService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;

/**
 * A dummy implementation for the backend API.
 */
public class DummyDataProvider implements DataProvider, Serializable {

    // TODO: Get API key from http://developer.rottentomatoes.com
    private static final String ROTTEN_TOMATOES_API_KEY = null;
    private static final long serialVersionUID = -2435788440141620247L;

    private final static Logger logger = Logger.getLogger(DummyDataProvider.class);


    private static Date lastDataUpdate;
    private static Map<Long, Position> locations = new HashMap<>();

    private static Random rand = new Random();

    private final Collection<DashboardNotification> notifications = DummyDataGenerator
            .randomNotifications();


    private PositionService positionService;
    private UserService userService;

    private CommandRestService commandRestService;


    /**
     * Initialize the data for this application.
     */
    public DummyDataProvider(PositionService positionService, UserService userService, CommandRestService commandRestService) {
        this.positionService = positionService;
        this.userService = userService;
        this.commandRestService = commandRestService;

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -1);
        if (lastDataUpdate == null || lastDataUpdate.before(cal.getTime())) {
            refreshStaticData();
            lastDataUpdate = new Date();
        }
    }

    private void refreshStaticData() {
        User user = (User) VaadinSession.getCurrent().getAttribute(
                User.class.getName());
        if (user == null) return;
        List<Position> newLocations = positionService.getPositions(user.getDeviceId());
        logger.info("LOCATIONS SIZE FROM DB " + newLocations.size());
        for (Position loc : newLocations) {
            logger.info(loc);
            if (!locations.containsKey(loc.getId())) {
                locations.put(loc.getId(), loc);
            }
        }
    }

    /**
     * Get a list of movies currently playing in theaters.
     *
     * @return a list of Movie objects
     */

    /**
     * Initialize the list of movies playing in theaters currently. Uses the
     * Rotten Tomatoes API to get the list. The result is cached to a local file
     * for 24h (daily limit of API calls is 10,000).
     *
     * @return
     */

    /* JSON utility method */
    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    /* JSON utility method */
    private static JsonObject readJsonFromUrl(String url) throws IOException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is,
                    Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JsonElement jelement = new JsonParser().parse(jsonText);
            JsonObject jobject = jelement.getAsJsonObject();
            return jobject;
        } finally {
            is.close();
        }
    }

    /* JSON utility method */
    private static JsonObject readJsonFromFile(File path) throws IOException {
        BufferedReader rd = new BufferedReader(new FileReader(path));
        String jsonText = readAll(rd);
        JsonElement jelement = new JsonParser().parse(jsonText);
        JsonObject jobject = jelement.getAsJsonObject();
        return jobject;
    }


    @Override
    public User authenticate(String userName, String password) {
        User user = userService.getUserByLogin(userName);
        System.out.println("Logged in: "+commandRestService.login(userName, password));
        if(user!=null && user.isPasswordValid(password)) {
            return user;
        }
        return null;

        //return userService.getUser(userName, password);
    }

    @Override
    public Collection<Position> getRecentLocations(int count) {
        refreshStaticData();

        List<Position> orderedLocations = Lists.newArrayList(locations
                .values());

        if (orderedLocations.size() == 0) {
            orderedLocations.add(new Position());
        }


        for (Position location : orderedLocations) {
            location.setRead(true);
        }
        Collections.sort(orderedLocations, new Comparator<Position>() {
            @Override
            public int compare(Position o1, Position o2) {
                return o2.getDeviceTime().compareTo(o1.getDeviceTime());
            }
        });
        return orderedLocations;
    }

    @Override
    public Position getLastLocation() {

        User user = (User) VaadinSession.getCurrent().getAttribute(
                User.class.getName());
        if (user == null) return null;

        return positionService.getLastPosition(user.getDeviceId());
    }


    private Date getDay(Date time) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        return cal.getTime();
    }

    @Override
    public int getUnreadLocationCount() {
        refreshStaticData();
        Predicate<Position> unreadPredicate = new Predicate<Position>() {
            @Override
            public boolean apply(Position input) {
                return !input.isRead();
            }
        };
        return Collections2.filter(locations.values(), unreadPredicate).size();
    }


    @Override
    public Collection<Position> getLocationsBetween(final Date startDate,
                                                    final Date endDate) {
        return Collections2.filter(locations.values(),
                new Predicate<Position>() {
                    @Override
                    public boolean apply(Position input) {
                        return !input.getDeviceTime().before(startDate)
                                && !input.getDeviceTime().after(endDate);
                    }
                });
    }

    @Override
    public boolean sendCommand(String type, Map<String, Object> attrs) {
        return commandRestService.sendCommand(type, attrs);
    }

}
