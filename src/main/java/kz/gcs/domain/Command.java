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

    /**RG Sends command to locate, device sends back a Google link to phone*/
    TYPE_POSITION_SINGLE("positionSingle"),

    /**UPLOAD (int CommandParameter.KEY_FREQUENCY in minutes) Sends command to set reporting time interval*/
    TYPE_POSITION_PERIODIC("positionPeriodic"),

    /**SOS (int CommandParameter.KEY_INDEX, string CommandParameter.KEY_PHONE) Sends command to set SOS numbers*/
    TYPE_SOS_NUMBER("sosNumber"),

    /**SOSSMS (boolean CommandParameter.KEY_ENABLE) Set to send SMS to SOS number in case of emergency*/
    TYPE_ALARM_SOS("alarmSos"),

    /**RESET Sends command to reboot device remotely*/
    TYPE_REBOOT_DEVICE("rebootDevice"),

    /**LOWBAT (boolean CommandParameter.KEY_ENABLE) Set to send SMS to the center number when battery is low*/
    TYPE_ALARM_BATTERY("alarmBattery"),

    /**REMOVE (boolean CommandParameter.KEY_ENABLE) Set to send alarm when the watch is taken off*/
    TYPE_ALARM_REMOVE("alarmRemove"),

    /**REMIND (string CommandParameter.KEY_DATA example: 08:10-1-1,08:10-1-2, 08:10-1-3-0111110) Set alarm clock*/
    TYPE_ALARM_CLOCK("alarmClock"),

    /**SILENCETIME (string CommandParameter.KEY_DATA example: 21:10-7:30,21:10-7:30,21:10-7:30,21:10-7:30) Set silence mode*/
    TYPE_SILENCE_TIME("silenceTime"),

    /**PHB (string CommandParameter.KEY_DATA example: 597D003100320033,313131) Set phonebook contacts*/
    TYPE_SET_PHONEBOOK("setPhonebook"),
    TYPE_SET_PHONEBOOK2("setPhonebook2"),

    /**TK (string CommandParameter.KEY_DATA) Sends voice message in binary data form*/
    TYPE_VOICE_MESSAGE("voiceMessage"),

    /**FLOWER (string CommandParameter.KEY_DATA) Send an indicated amount of hearts to the watch*/
    TYPE_SET_INDICATOR("setIndicator"),

    /**CENTER (string CommandParameter.KEY_PHONE) Set center number*/
    TYPE_CENTER_NUMBER("setCenterNumber"),

    /**SLAVE (string CommandParameter.KEY_PHONE) Set slave number*/
    TYPE_ASSIST_NUMBER("setAssistNumber"),

    /**PW (string CommandParameter.KEY_PASSWORD) Set device password*/
    TYPE_CONTROL_PASSWORD("setPassword"),

    /**MONITOR Send command to call your phone and start listening*/
    TYPE_MONITOR("monitorDevice"),

    /**UPGRADE (string CommandParameter.KEY_URL) Upgrade the watch remotely via link*/
    TYPE_REMOTE_UPGRADE("remoteUpgrade"),

    /**IP (string CommandParameter.KEY_URL, int CommandParameter.KEY_PORT) Connect the device to a new server*/
    TYPE_IP_PORT("setIpPort"),

    /**FACTORY Reset the device to factory settings*/
    TYPE_FACTORY_RESET("factoryReset"),

    /**LZ (string CommandParameter.KEY_LANGUAGE, int CommandParameter.KEY_TIMEZONE) Set language and timezone*/
    TYPE_LANG_TIME_AREA("setLangTimeArea"),

    /**URL (string CommandParameter.KEY_URL) Query Google Link*/
    TYPE_QUERY_GOOGLE_LINK("queryGoogleLink"),

    /**
     * APN (string CommandParameter.KEY_APN_NAME, string CommandParameter.KEY_APN_USER_NAME,
     * string CommandParameter.KEY_APN_CODE, string CommandParameter.KEY_APN_USER_DATA)
     * Set APN settings
     * */
    TYPE_APN_SETTINGS("setApnSettings"),

    /**ANY (boolean CommandParameter.KEY_ENABLE) Set if any number can send settings via SMS*/
    TYPE_SMS_ACCESS_SETTINGS("setSmsAccessSettings"),

    /**TS Query tracker parameters*/
    TYPE_PARAMETER_QUERY("parameterQuery"),

    /**VERNO Query tracker firmware version*/
    TYPE_VERSION_QUERY("versionQuery"),

    /**CR GPS Receiver Immediate Wake-up Command*/
    TYPE_LOCATION_ORDER("locationOrder"),

    /**BT (boolean CommandParameter.KEY_ENABLE) Bluetooth access ON/Off Command*/
    TYPE_BLUETOOTH_CONTROL_ORDER("bluetoothControlOrder"),

    /**WORK (string CommandParameter.KEY_DATA example: 6-9,11-13,13-15,17-19) Set the working time area,and separate every area by comma.*/
    TYPE_WORKING_TIME_AREA_DIRECTIVE("workingTimeAreaDirective"),

    /**WORKTIME (int CommandParameter.KEY_WORKTIME) Set the terminal working time ,and the unit is minutes.*/
    TYPE_WORKTIME("setWorkTime"),

    /**POWEROFF Shutdown the device*/
    TYPE_SHUTDOWN("shutDown"),

    /**PULSE Query the current pulse of the person wearing the watch*/
    TYPE_PULSE_QUERY("pulseQuery"),

    /**PEDO (boolean Command)Toggle pedometer*/
    TYPE_PEDOMETER("setPedometer"),

    /**WALKTIME (string CommandParameter.KEY_DATA example: 8:10-9:30,10:10-11:30,12:10-13:30) Pedometer Working Time Plan setting*/
    TYPE_PEDOMETER_WALKTIME("setPedometerWalkTime"),

    /**SLEEPTIME (string CommandParameter.KEY_DATA example: 21:10-7:30) Set sleeptime*/
    TYPE_SLEEPTIME("setSleepTime"),

    /**FIND After tracker received this command, watch will ring for 1 minute*/
    TYPE_LOCATE("locate"),

    /**MESSAGE (string CommandParameter.KEY_MESSAGE) Send message to device*/
    TYPE_SHOW_MESSAGE("showMessage"),

    /**SMSONOFF (boolean CommandParameter.KEY_ENABLE) Tracker watch SMS functionality enable/Disable Setting. By this command, it On/Off all SMS intending operation.*/
    TYPE_SMS_ON_OFF("setSmsOnOrOff"),

    /**GSMANT (boolean CommandParameter.KEY_ENABLE) Phone call automatic Pickup ON/Off Setting*/
    TYPE_AUTOMATIC_PICKUP("setAutomaticPickUp"),

    /**WHITELIST1 (string CommandParameter.KEY_DATA) Phone Book White Contact List Command*/
    TYPE_WHITELIST1("setWhiteList1"),
    TYPE_WHITELIST2("setWhiteList2"),

    /**CALL (string CommandParameter.KEY_PHONE) Call phone immediately*/
    TYPE_CALL_PHONE("phoneCall");


    private String commandString;

    Command(String commandString) {
        this.commandString = commandString;
    }

    public String getCommandString() {
        return commandString;
    }

}
