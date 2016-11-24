package kz.gcs.data;

import kz.gcs.data.service.LocationService;
import kz.gcs.domain.*;

import java.util.Collection;
import java.util.Date;

/**
 * QuickTickets Dashboard backend API.
 */
public interface DataProvider {
    /**
     * @param count
     *            Number of transactions to fetch.
     * @return A Collection of most recent transactions.
     */
    Collection<Location> getRecentLocations(int count);


    Location getLastLocation(long gadgetId);


    /**
     * @param userName
     * @param password
     * @return Authenticated used.
     */
    User authenticate(String userName, String password);

    /**
     * @return The number of unread notifications for the current user.
     */
    int getUnreadLocationCount();


    /**
     * @param startDate
     * @param endDate
     * @return A Collection of Transactions between the given start and end
     *         dates.
     */
    Collection<Location> getLocationsBetween(Date startDate, Date endDate);

    void setService(LocationService locationService);
}
