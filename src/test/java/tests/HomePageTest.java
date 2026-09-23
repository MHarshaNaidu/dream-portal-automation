package tests;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.HomePage;
import utils.DriverFactory;
import java.util.Set;
import org.testng.ITestResult;
import utils.ScreenshotUtil;

public class HomePageTest {
    @Test
    public void verifyLoadingMessageDisappears() {

        homePage.open();

        Assert.assertTrue(
                homePage.isLoadingMessageVisible(),
                "Loading message should be visible initially."
        );

        homePage.waitForLoadingToDisappear();

        Assert.assertFalse(
                homePage.isLoadingMessageVisible(),
                "Loading message should disappear after loading."
        );

        System.out.println("Loading message appeared and disappeared successfully.");
    }

    private WebDriver driver;
    private HomePage homePage;

    @BeforeMethod
    public void setUp() {
        driver = DriverFactory.createDriver();
        homePage = new HomePage(driver);
    }

    @Test
    public void verifyHomePageLoadingAndContent() {

        homePage.open();

        // Loading screen should initially appear
        Assert.assertTrue(
                homePage.isLoadingMessageVisible(),
                "Loading message should be visible initially"
        );

        // Wait for loading screen to disappear
        homePage.waitForLoadingToDisappear();

        // My Dreams button should be visible after loading
        Assert.assertTrue(
                homePage.isMyDreamsButtonVisible(),
                "My Dreams button should be visible after loading"
        );

        System.out.println("Home page loaded successfully.");
        System.out.println("My Dreams button is visible.");
    }
    @Test
    public void verifyMyDreamsOpensRequiredPages() {

        homePage.open();

        homePage.waitForLoadingToDisappear();

        String originalWindow = driver.getWindowHandle();

        homePage.clickMyDreamsAndWaitForNewWindows();

        Set<String> windows = driver.getWindowHandles();

        System.out.println("Number of windows/tabs: " + windows.size());

        Assert.assertEquals(
                windows.size(),
                3,
                "My Dreams should open two new tabs/windows"
        );

        boolean diaryFound = false;
        boolean totalFound = false;

        for (String window : windows) {

            driver.switchTo().window(window);

            String url = driver.getCurrentUrl();

            System.out.println("Opened URL: " + url);

            if (url.contains("dreams-diary.html")) {
                diaryFound = true;
            }

            if (url.contains("dreams-total.html")) {
                totalFound = true;
            }
        }

        Assert.assertTrue(
                diaryFound,
                "Dreams Diary page should be opened"
        );

        Assert.assertTrue(
                totalFound,
                "Dreams Total page should be opened"
        );

        driver.switchTo().window(originalWindow);
    }

    @AfterMethod
    public void tearDown(ITestResult result) {

        if (!result.isSuccess() && driver != null) {

            ScreenshotUtil.takeScreenshot(
                    driver,
                    result.getMethod().getMethodName()
            );
        }

        if (driver != null) {
            driver.quit();
        }
    }
}