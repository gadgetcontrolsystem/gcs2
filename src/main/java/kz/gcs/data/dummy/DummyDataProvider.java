package kz.gcs.data.dummy;

import com.google.common.base.Predicate;
import com.google.common.collect.*;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vaadin.server.VaadinSession;
import kz.gcs.data.DataProvider;
import kz.gcs.data.service.LocationService;
import kz.gcs.data.service.UserService;
import kz.gcs.domain.*;
import org.apache.log4j.Logger;

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
    private static Map<Long, Location> locations = new HashMap<>();

    private static Random rand = new Random();

    private final Collection<DashboardNotification> notifications = DummyDataGenerator
            .randomNotifications();


    private LocationService locationService;
    private UserService userService;


    /**
     * Initialize the data for this application.
     */
    public DummyDataProvider(LocationService locationService, UserService userService) {
        this.locationService = locationService;
        this.userService = userService;

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
        List<Location> newLocations = locationService.getLocations(user.getGadgetId());
        logger.info("LOCATIONS SIZE FROM DB " + newLocations.size());
        for (Location loc : newLocations) {
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
        return userService.getUser(userName, password);
    }

    @Override
    public Collection<Location> getRecentLocations(int count) {
        refreshStaticData();

        List<Location> orderedLocations = Lists.newArrayList(locations
                .values());

        if (orderedLocations.size() == 0) {
            orderedLocations.add(new Location());
        }


        for (Location location : orderedLocations) {
            location.setRead(true);
        }
        Collections.sort(orderedLocations, new Comparator<Location>() {
            @Override
            public int compare(Location o1, Location o2) {
                return o2.getTime().compareTo(o1.getTime());
            }
        });
        return orderedLocations;
    }

    @Override
    public Location getLastLocation(long gadgetId) {

        User user = (User) VaadinSession.getCurrent().getAttribute(
                User.class.getName());

        return locationService.getLastLocation(user.getGadgetId());
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
        Predicate<Location> unreadPredicate = new Predicate<Location>() {
            @Override
            public boolean apply(Location input) {
                return !input.isRead();
            }
        };
        return Collections2.filter(locations.values(), unreadPredicate).size();
    }


    @Override
    public Collection<Location> getLocationsBetween(final Date startDate,
                                                    final Date endDate) {
        return Collections2.filter(locations.values(),
                new Predicate<Location>() {
                    @Override
                    public boolean apply(Location input) {
                        return !input.getTime().before(startDate)
                                && !input.getTime().after(endDate);
                    }
                });
    }

}
