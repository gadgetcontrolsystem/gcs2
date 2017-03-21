/*
 * Copyright 2015 - 2016 Anton Tananaev (anton@traccar.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package kz.gcs.domain;

public enum Command {

    TYPE_CUSTOM("custom"),

    TYPE_POSITION_SINGLE("positionSingle"),
    TYPE_POSITION_PERIODIC("positionPeriodic"),
    TYPE_SOS_NUMBER("sosNumber"),
    TYPE_ALARM_SOS("alarmSos"),
    TYPE_REBOOT_DEVICE("rebootDevice"),
    TYPE_ALARM_BATTERY("alarmBattery"),
    TYPE_ALARM_REMOVE("alarmRemove"),
    TYPE_ALARM_CLOCK("alarmClock"),
    TYPE_SILENCE_TIME("silenceTime"),
    TYPE_SET_PHONEBOOK("setPhonebook"),
    TYPE_VOICE_MESSAGE("voiceMessage"),
    TYPE_SET_TIMEZONE("setTimezone"),
    TYPE_SET_INDICATOR("setIndicator"),
    TYPE_CENTER_NUMBER("setCenterNumber"),
    TYPE_ASSIST_NUMBER("setAssistNumber"),
    TYPE_CONTROL_PASSWORD("setPassword"),
    TYPE_MONITOR("monitorDevice"),
    TYPE_REMOTE_UPGRADE("remoteUpgrade"),
    TYPE_IP_PORT("setIpPort"),
    TYPE_FACTORY_RESET("factoryReset"),
    TYPE_LANG_TIME_AREA("setLangTimeArea"),
    TYPE_QUERY_GOOGLE_LINK("queryGoogleLink"),
    TYPE_APN_SETTINGS("setApnSettings"),
    TYPE_SMS_ACCESS_SETTINGS("setSmsAccessSettings"),
    TYPE_PARAMETER_QUERY("parameterQuery"),
    TYPE_VERSION_QUERY("versionQuery"),
    TYPE_LOCATION_ORDER("locationOrder"),
    TYPE_BLUETOOTH_CONTROL_ORDER("bluetoothControlOrder"),
    TYPE_WORKING_TIME_AREA_DIRECTIVE("workingTimeAreaDirective"),
    TYPE_WORKTIME("setWorkTime"),
    TYPE_SHUTDOWN("shutDown"),
    TYPE_PULSE_QUERY("pulseQuery"),
    TYPE_PEDOMETER("setPedometer"),
    TYPE_PEDOMETER_WALKTIME("setPedometerWalkTime"),
    TYPE_SLEEPTIME("setSleepTime"),
    TYPE_LOCATE("locate"),
    TYPE_SHOW_MESSAGE("showMessage"),
    TYPE_SMS_ON_OFF("setSmsOnOrOff"),
    TYPE_AUTOMATIC_PICKUP("setAutomaticPickUp"),
    TYPE_WHITELIST1("setWhiteList1"),
    TYPE_WHITELIST2("setWhiteList2"),
    TYPE_CALL_PHONE("phoneCall"),
    TYPE_SET_PHONEBOOK2("setPhonebook2");

    private String commandString;

    Command(String commandString) {
        this.commandString = commandString;
    }

    public String getCommandString() {
        return commandString;
    }

}
