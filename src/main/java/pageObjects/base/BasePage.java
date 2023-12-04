package pageObjects.base;

import actions.ActionBot;

public class BasePage {

    public ActionBot actionBot;
    protected final String BASE_URL = "https://www.shutterfly.com?test=true";

    public BasePage(ActionBot actionBot) {
        this.actionBot = actionBot;
    }

    protected void log(String message) {
        actionBot.log(message);
    }
}


