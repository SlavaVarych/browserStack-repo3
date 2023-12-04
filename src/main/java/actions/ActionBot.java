package actions;

import base.BaseTest;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import org.testng.asserts.SoftAssert;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;

import static com.epam.reportportal.service.ReportPortal.emitLog;

public class ActionBot {
    AndroidDriver androidDriver;
    IOSDriver iosDriver;

    private SoftAssert softAssert;

    public ActionBot(AndroidDriver driver, SoftAssert softAssert) {
        this.androidDriver = driver;
        this.softAssert = softAssert;
    }

    public ActionBot(IOSDriver driver, SoftAssert softAssert) {
        this.iosDriver = driver;
        this.softAssert = softAssert;
    }

    public void log(String message) {
        emitLog(message, "info", new Date());
        printWithTime(message);
    }

    public static void printWithTime(String message) {
        String currentTime = new SimpleDateFormat("HH:mm:ss").format(new Date());
        System.out.println(currentTime + " " + message);
    }

    public AppiumDriver getDriver() {
        if (BaseTest.PLATFORM.equals("ios")) {
            return this.iosDriver;
        } else {
            return this.androidDriver;
        }
    }

    public AndroidDriver getAndroidDriver() {
        return this.androidDriver;
    }

    public IOSDriver getiOSDriver() {
        return this.iosDriver;
    }


    /**
     * Method for navigate to url
     *
     * @param URL
     */
    public void openURL(String URL) {
        log("Opening URL " + URL);
        getDriver().get(URL);
    }

    /**
     * Method returns webElement from locator
     *
     * @param locator - locator of the element
     * @return
     */
    public WebElement findElement(By locator) {
        return getDriver().findElement(locator);
    }

    public By waitVisibilityOfElement(By locator, int waitTimeInSec) {
        WebDriverWait wait = (new WebDriverWait(getDriver(), Duration.ofSeconds(waitTimeInSec)));
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        return locator;
    }

    public void waitInVisibilityOfElement(By locator, int waitTimeInSec) {
        WebDriverWait wait = (new WebDriverWait(getDriver(), Duration.ofSeconds(waitTimeInSec)));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public void waitInVisibilityOfElement(WebElement element, int waitTimeInSec) {
        WebDriverWait wait = (new WebDriverWait(getDriver(), Duration.ofSeconds(waitTimeInSec)));
        wait.until(ExpectedConditions.invisibilityOf(element));
    }

    public By waitForElementToBeRefreshedAndClickable(By locator, int waitTimeInSec) {
        WebDriverWait wait = (new WebDriverWait(getDriver(), Duration.ofSeconds(waitTimeInSec)));
        wait.until(ExpectedConditions.refreshed(ExpectedConditions.elementToBeClickable(locator)));
        return locator;
    }

    public boolean isElementVisible(By locator, int waitTimeInSec) {
        WebDriverWait wait = (new WebDriverWait(getDriver(), Duration.ofSeconds(waitTimeInSec)));
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    public boolean isElementPresent(By locator, int waitTimeInSec) {
        try {
            WebDriverWait wait = (new WebDriverWait(getDriver(), Duration.ofSeconds(waitTimeInSec)));
            wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    public boolean isFrameReadyToSwitch(By locator, int waitTimeInSec) {
        WebDriverWait wait = (new WebDriverWait(getDriver(), Duration.ofSeconds(waitTimeInSec)));
        try {
            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(locator));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Performs click on element
     *
     * @param locator - locator of the element
     */
    public void clickOnElementUsingJS(By locator) {
        JavascriptExecutor executor = getDriver();
        executor.executeScript("arguments[0].click()", findElement(locator));
    }

    public void clickSelenium(By locator) {
        try {
            findElement(locator).click();
        } catch (ElementClickInterceptedException e) {
            closeSurveyFrame();
            findElement(locator).click();
        }
    }

    public void clickSelenium(WebElement element) {
        try {
            element.click();
        } catch (ElementClickInterceptedException e) {
            closeSurveyFrame();
            element.click();
        }
    }

    /**
     * Method performs waiting for element, clicks on it, and waiting for invisibility of the element
     *
     * @param locator
     * @param howMuchWaitForVisibility
     * @param howMuchWaitForInVisibility
     */
    public void clickElementAndWaitInvisibility(By locator, int howMuchWaitForVisibility, int howMuchWaitForInVisibility) {
        waitForElementToBeRefreshedAndClickable(locator, howMuchWaitForVisibility);
        WebElement clickElement = findElement(locator);
        clickSelenium(clickElement);
        waitInVisibilityOfElement(clickElement, howMuchWaitForInVisibility);
    }

    /**
     * Method for page refresh
     */
    public void refresh() {
        log("Refreshing page");
        getDriver().navigate().refresh();

    }

    public void storageClear() {
        getDriver().executeScript("localStorage.clear()");
    }

    public void closeSurveyFrame() {
        if (isFrameReadyToSwitch(By.cssSelector("iframe[title='Feedback Survey']"), 5)) {
            log("Closing the Survey frame");
            if (isElementVisible(By.cssSelector("button[name='close']"), 4)) {
                clickElementAndWaitInvisibility(By.cssSelector("button[name='close']"), 3, 5);
            }
            getDriver().switchTo().defaultContent();
        } else if (isElementVisible(By.cssSelector("iframe[title='Feedback Survey']"), 3)) {
            JavascriptExecutor jse = getDriver();
            jse.executeScript("arguments[0].remove()", findElement(By.cssSelector("iframe[title='Feedback Survey']")));
            waitInVisibilityOfElement(By.cssSelector("iframe[title='Feedback Survey']"), 3);
        }
    }
}

