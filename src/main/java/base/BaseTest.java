package base;

import actions.ActionBot;
import base.Preconditions;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class BaseTest {

    protected String PROVIDER;
    protected ActionBot actionBot;
    private SoftAssert softAssert;
    private AndroidDriver androidDriver;
    private IOSDriver iosDriver;
    private Preconditions preconditions = new Preconditions();
    public static String PLATFORM;
    public static String AUTOMATION_KEY;

    public static final HashMap<String, ActionBot> ACTION_BOTS = new HashMap<>();

    @BeforeMethod(alwaysRun = true, timeOut = 300000)
    @Parameters({"provider", "platform"})
    public void setup(
            @Optional("") String provider,
            @Optional("") String platform,
            Method method, ITestResult result
    ) {
        String currentProvider = provider.length() > 0 ? provider : java.util.Optional.ofNullable(Preconditions.getPropertyFromPropertiesFile("provider", "localRun")).orElse("local");
        String currentPlatform = platform.length() > 0 ? platform : java.util.Optional.ofNullable(Preconditions.getPropertyFromPropertiesFile("platform", "localRun")).orElse("ios");
        PLATFORM = currentPlatform;
        System.out.println("Platform is " + currentPlatform);
        PROVIDER = currentProvider;
        softAssert = new SoftAssert();
        printWithTime("Starting to create driver for test " + result.getMethod().getMethodName());
        if (currentPlatform.equals("ios")) {
            iosDriver = new IOSDriver(preconditions.appiumUrl(currentProvider), preconditions.getCapabilities(currentProvider, preconditions.getDevice(currentProvider, currentPlatform), result.getMethod().getMethodName()));
            actionBot = new ActionBot(iosDriver, softAssert);
            iosDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        } else {
            androidDriver = new AndroidDriver(preconditions.appiumUrl(currentProvider), preconditions.getCapabilities(currentProvider, preconditions.getDevice(currentProvider, currentPlatform), result.getMethod().getMethodName()));
            actionBot = new ActionBot(androidDriver, softAssert);
            androidDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        }
        ACTION_BOTS.put(result.getMethod().getInstance().toString(), actionBot);
        printWithTime("Setup method finished for test " + result.getMethod().getMethodName());
    }

    @AfterMethod(alwaysRun = true, timeOut = 240000)
    public void runOnceAtEnd(ITestResult result) {
        if (PLATFORM.equals("ios")) {
            if (iosDriver != null) {
                iosDriver.quit();
            }
        } else {
            if (androidDriver != null) {
                androidDriver.quit();
            }
        }
        String testName = result.getMethod().getInstance().toString();
        ACTION_BOTS.remove(testName);
        System.out.println("After method finished ");
    }

    private void printWithTime(String message) {
        String currentTime = new SimpleDateFormat("HH:mm:ss").format(new Date());
        System.out.println(currentTime + " " + message);
    }
}
