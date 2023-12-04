package base;

import com.google.common.collect.ImmutableMap;
import io.appium.java_client.remote.options.BaseOptions;

import utils.AppData;
import utils.Device;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Optional;
import java.util.Properties;

public class Preconditions {

    private static final String BROWSERSTACK_URL = "https://%s:%s@hub-cloud.browserstack.com";
    private static final String LOCAL_PROVIDER = "http://127.0.0.1:4723";
    protected BaseOptions baseOptions = new BaseOptions();

    public BaseOptions iOSSafariDesiredCapabilities(Device device) {
        baseOptions.setCapability("platformName", "iOS");
        baseOptions.setCapability("platformVersion", device.getOsVersion());
        baseOptions.setCapability("deviceName", device.getDeviceName());
        baseOptions.setCapability("udid", device.getUdid());
        baseOptions.setCapability("automationName", "XCuiTest");
        baseOptions.setCapability("appium:browserName", "safari");
        baseOptions.setCapability("appium:newCommandTimeout", 1200);
        baseOptions.setCapability("safariInitialUrl", "https://www.shutterfly.com/");
        baseOptions.setCapability("–session-override", true);
        baseOptions.setCapability("disableWindowAnimation", true);
        return baseOptions;
    }

    public BaseOptions androidChromeDesiredCapabilities(Device device) {
        baseOptions.setCapability("platformName", "Android");
        baseOptions.setCapability("platformVersion", device.getOsVersion());
        baseOptions.setCapability("deviceName", device.getDeviceName());
        baseOptions.setCapability("udid", device.getUdid());
        baseOptions.setCapability("automationName", "Uiautomator2");
        baseOptions.setCapability("appium:chromeOptions", ImmutableMap.of("w3c", true));
        baseOptions.setCapability("appium:browserName", "chrome");
        baseOptions.setCapability("appium:newCommandTimeout", 1200);
        baseOptions.setCapability("enableWebviewDetailsCollection", true);
        baseOptions.setCapability("showChromedriverLog", true);
        baseOptions.setCapability("disableWindowAnimation", true);
        return baseOptions;
    }


    public BaseOptions getLocalCapabilities(Device device) {
        if (device.getPlatform().toLowerCase().equals("ios")) {
            return iOSSafariDesiredCapabilities(device);
        } else {
            return androidChromeDesiredCapabilities(device);
        }
    }

    public BaseOptions getBrowserStackCapabilities(Device device, String testName) {

        BaseOptions baseOptions = new BaseOptions();
        HashMap<String, Object> browserstackOptions = new HashMap<String, Object>();
        browserstackOptions.put("appiumVersion", "2.0.1");
        browserstackOptions.put("idleTimeout", "120");
        browserstackOptions.put("disableAnimations", "true");
        browserstackOptions.put("timezone", "Jerusalem");
        browserstackOptions.put("disableCorsRestrictions", "true");
        browserstackOptions.put("realMobile", "true");
        //   browserstackOptions.put("device", device.getDeviceName());
//        browserstackOptions.put("interactiveDebugging", "true");
        //   browserstackOptions.put("automationVersion", "2.29.2");

        String browserName = device.getPlatform().toLowerCase().equals("ios") ? "safari" : "chrome";
        baseOptions.setCapability("bstack:options", browserstackOptions);
        baseOptions.setCapability("browserName", browserName);
        baseOptions.setCapability("deviceName", device.getDeviceName());
        // capabilities.setCapability("device", device.getDeviceName());
        baseOptions.setCapability("platformVersion", device.getOsVersion());
        baseOptions.setCapability("platformName", device.getPlatform());
        baseOptions.setCapability("name", testName);

        //  baseOptions.setCapability("automationVersion", "v2.29.2");

        baseOptions.setCapability("automationName", "UiAutomator2");
        baseOptions.setCapability("appium:chromeOptions", ImmutableMap.of("w3c", true));
        return baseOptions;
    }

//    public DesiredCapabilities getBrowserStackCapabilities(Device device, String testName) {
//        String browserName = device.getPlatform().toLowerCase().equals("ios") ? "safari" : "chrome";
//        capabilities.setCapability("device", device.getDeviceName());
//        capabilities.setCapability("name", testName);
//        capabilities.setCapability("realMobile", true);
//
//
//       // capabilities.setCapability("appium:chromeOptions", ImmutableMap.of("w3c", false));
//        return capabilities;
//    }


    public BaseOptions getRemoteCapabilities(Device device) {
        if (device.getPlatform().toLowerCase().equals("ios")) {
            return iOSSafariDesiredCapabilities(device);
        } else {
            BaseOptions baseOptions = androidChromeDesiredCapabilities(device);
            return baseOptions;
        }
    }

    public BaseOptions getCapabilities(String provider, Device device, String testName) {
        if (provider.toLowerCase().equals(AppData.Provider.LOCAL.getProvider())) {
            return getLocalCapabilities(device);
        } else if (provider.toLowerCase().equals(AppData.Provider.BROWSERSTACK.getProvider())) {
            return getBrowserStackCapabilities(device, testName);
        } else if (provider.toLowerCase().equals(AppData.Provider.REMOTE.getProvider())) {
            return getRemoteCapabilities(device);
        } else {
            // TODO
        }
        return null;
    }

    private String getAppiumURL(String provider) {
        String appiumUrl = "";
        if (provider.toLowerCase().equals("local")) {
            appiumUrl = LOCAL_PROVIDER;
        } else if (provider.toLowerCase().equals("browserstack")) {
            appiumUrl = getBrowserStackUrl();
        } else if (provider.toLowerCase().equals("remote")) {
            appiumUrl = LOCAL_PROVIDER;
        } else {
            System.out.println("Unknown provider");
        }
        return appiumUrl;
    }

    public URL appiumUrl(String provider) {
        URL url = null;
        try {
            url = new URL(getAppiumURL(provider));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    private String getBrowserStackUrl() {
        String user = getPropertyFromPropertiesFile("userName", "browserStack");
        String key = getPropertyFromPropertiesFile("automateKey", "browserStack");
        return String.format(BROWSERSTACK_URL, user, key);
    }

    public Device getDevice(String provider, String platform) {
        if (provider.toLowerCase().equals(AppData.Provider.LOCAL.getProvider())) {
            return getDeviceFromPropertyFile("localRun");
        } else if (provider.toLowerCase().equals(AppData.Provider.BROWSERSTACK.getProvider())) {
            if (platform.toLowerCase().contains("ios")) {
                return getDeviceFromPropertyFile("browserStackiOSDevice");
            } else {

                return getDeviceFromPropertyFile("browserStackAndroidDevice");
            }
        } else if (provider.toLowerCase().equals(AppData.Provider.REMOTE.getProvider())) {
            if (platform.toLowerCase().contains("ios")) {
                return getDeviceFromPropertyFile("hariOSDevice");
            } else {
                return getDeviceFromPropertyFile("harAndroidDevice");
            }
        }
        return null;
    }

    public static String getPropertyFromPropertiesFile(String property, String propertyFileName) {
        Properties p = new Properties();
        try
                (Reader reader = new FileReader(System.getProperty("user.dir") + File.separator + "src/main/resources/properties" + File.separator + propertyFileName + ".properties")) {
            p.load(reader);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return p.getProperty(property);
    }


    private Device getDeviceFromPropertyFile(String propertyFileName) {
        String deviceName = Optional.ofNullable(System.getenv("DEVICE_NAME")).orElse(getPropertyFromPropertiesFile("deviceName", propertyFileName));
        String osVersion = Optional.ofNullable(System.getenv("OS_VERSION")).orElse(getPropertyFromPropertiesFile("osVersion", propertyFileName));
        String platform = Optional.ofNullable(System.getenv("PLATFORM")).orElse(getPropertyFromPropertiesFile("platform", propertyFileName));
        String udid = Optional.ofNullable(System.getenv("")).orElse(getPropertyFromPropertiesFile("udid", propertyFileName));
        return new Device(deviceName, osVersion, platform, udid);

    }
}


