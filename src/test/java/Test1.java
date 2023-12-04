import base.BaseTest;
import org.testng.annotations.Test;
import pageObjects.MainPage;

public class Test1 extends BaseTest {

    @Test
    public void test1() {
        MainPage mainPage = new MainPage(actionBot);
        mainPage.handleStartupFlow();
        mainPage.openSighUpPage();
    }
}

