package pageObjects;

import actions.ActionBot;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.*;
import org.testng.Assert;
import pageObjects.base.BasePage;


public class MainPage extends BasePage {


    private final By accountIcon = AppiumBy.xpath("//*[@aria-label='User Menu']");
    public final By mobileHamburgerHeader = By.cssSelector("button#navigationMenu");
    private final By sighUpMobile = By.xpath("//a[normalize-space()='Sign Up']");
    private final By passwordLocator = By.xpath("//*[@id='password']");

    public MainPage(ActionBot actionBot) {
        super(actionBot);
    }

    public MainPage openStartupPage() {
        actionBot.openURL(BASE_URL);
        new MainPage(actionBot).closeNeverBanner();
        actionBot.waitVisibilityOfElement(mobileHamburgerHeader, 40);
        return this;
    }

    public MainPage handleStartupFlow() {
        openStartupPage();
        deleteAllCookies();
        actionBot.refresh();
        closeNeverBanner();
        return this;
    }

    private void deleteAllCookies() {
        log("Delete all cookies");
        actionBot.getDriver().manage().deleteAllCookies();
        actionBot.storageClear();
    }

    public void openSighUpPage() {
        log("Open Sigh Up form");
        clickAccountIcon();
        actionBot.waitVisibilityOfElement(sighUpMobile, 15);
        actionBot.clickSelenium(sighUpMobile);
        actionBot.waitVisibilityOfElement(passwordLocator, 30);
    }

    public void clickAccountIcon() {
        log("Clicking on Account icon");
        closeInsideBanner();
        closeExtraBanner();
        actionBot.waitVisibilityOfElement(accountIcon, 20);
        //  actionBot.clickOnElementUsingJS(accountIcon);
        actionBot.clickSelenium(accountIcon);
    }

    public void closeNeverBanner() {
        if (actionBot.isElementVisible(By.cssSelector("div.Modal "), 10)) {
            log("Closing banner");
            actionBot.clickSelenium(By.xpath("//button[@data-dismiss='modal']"));
            actionBot.waitInVisibilityOfElement(By.xpath("//button[@data-dismiss='modal']"), 5);
        }
    }

    public void closeInsideBanner() {
        if (actionBot.isElementPresent(By.cssSelector("#attentive_creative"), 8)) {
            log("Closing banner");
            JavascriptExecutor js = actionBot.getDriver();
            js.executeScript("arguments[0].remove();", actionBot.findElement(By.cssSelector("#attentive_creative")));
            Assert.assertFalse(actionBot.isElementPresent(By.cssSelector("#attentive_creative"), 3), "The banner was not closed");
            log("The banner is closed");
        }
    }

    public void closeExtraBanner() {
        if (actionBot.isElementVisible(By.xpath("//div[@class='popup-content']//div[@data-dismiss='modal']"), 10)) {
            actionBot.clickSelenium(By.xpath("//div[@class='popup-content']//div[@data-dismiss='modal']"));
            actionBot.waitInVisibilityOfElement(By.xpath("//div[@class='popup-content']//div[@data-dismiss='modal']"), 5);
        }
        if (actionBot.isElementVisible(By.xpath("//div[contains(@class, 'minimized-popup-modal fadeInUp')]//button[@data-dismiss='modal']"), 5)) {
            actionBot.clickOnElementUsingJS(By.xpath("//div[contains(@class, 'minimized-popup-modal fadeInUp')]//button[@data-dismiss='modal']"));
            actionBot.waitInVisibilityOfElement(By.xpath("//div[contains(@class, 'minimized-popup-modal fadeInUp')]//button[@data-dismiss='modal']"), 5);
        }
    }
}

