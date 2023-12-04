package utils;

public class Device {

    protected final String deviceName;
    protected final String osVersion;
    protected final String platform;
    protected final String udid;


    public Device(String deviceName, String osVersion, String platform, String udid) {
        this.deviceName = deviceName;
        this.osVersion = osVersion;
        this.platform = platform;
        this.udid = udid;

    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public String getPlatform() {
        return platform;
    }

    public String getUdid() {
        return udid;
    }

}

