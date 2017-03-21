package kz.gcs.domain;

/**
 * Created by burs on 3/21/17.
 */

public enum CommandParameter {

    KEY_FREQUENCY("frequency"),
    KEY_TIMEZONE("timezone"),
    KEY_MESSAGE("message"),
    KEY_ENABLE("enable"),
    KEY_DATA("data"),
    KEY_INDEX("index"),
    KEY_PHONE("phone"),
    KEY_PORT("port"),
    KEY_LANGUAGE("language"),
    KEY_URL("url"),
    KEY_PASSWORD("password"),
    KEY_WORKTIME("worktime"),
    KEY_APN_NAME("apnName"),
    KEY_APN_USER_NAME("apnUserName"),
    KEY_APN_CODE("apnCode"),
    KEY_APN_USER_DATA("apnUserData");

    private String parameterString;

    CommandParameter(String parameterString) {
        this.parameterString = parameterString;
    }

    public String getParameterString() {
        return parameterString;
    }

}
