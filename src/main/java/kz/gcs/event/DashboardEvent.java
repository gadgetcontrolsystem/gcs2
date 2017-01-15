package kz.gcs.event;

import kz.gcs.domain.Position;
import kz.gcs.views.MenuViewType;

import java.util.Collection;

/*
 * Event bus events used in Dashboard are listed here as inner classes.
 */
public abstract class DashboardEvent {

    public static final class UserLoginRequestedEvent {
        private final String userName, password;

        public UserLoginRequestedEvent(final String userName,
                final String password) {
            this.userName = userName;
            this.password = password;
        }

        public String getUserName() {
            return userName;
        }

        public String getPassword() {
            return password;
        }
    }

    public static class BrowserResizeEvent {

    }

    public static class UserLoggedOutEvent {

    }

    public static class NotificationsCountUpdatedEvent {
    }

    public static final class ReportsCountUpdatedEvent {
        private final int count;

        public ReportsCountUpdatedEvent(final int count) {
            this.count = count;
        }

        public int getCount() {
            return count;
        }

    }

    public static final class TransactionReportEvent {
        private final Collection<Position> locations;

        public TransactionReportEvent(final Collection<Position> locations) {
            this.locations = locations;
        }

        public Collection<Position> getLocations() {
            return locations;
        }
    }

    public static final class PostViewChangeEvent {
        private final MenuViewType view;

        public PostViewChangeEvent(final MenuViewType view) {
            this.view = view;
        }

        public MenuViewType getView() {
            return view;
        }
    }

    public static class CloseOpenWindowsEvent {
    }

    public static class ProfileUpdatedEvent {
    }

}
