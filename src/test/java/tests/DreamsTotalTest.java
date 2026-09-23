package tests;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.DreamsTotalPage;
import utils.DriverFactory;
import org.testng.ITestResult;
import utils.ScreenshotUtil;

public class DreamsTotalTest {

    private WebDriver driver;
    private DreamsTotalPage totalPage;

    @BeforeMethod
    public void setUp() {
        driver = DriverFactory.createDriver();
        totalPage = new DreamsTotalPage(driver);
    }

    @Test
    public void verifyDreamTotals() {

        totalPage.open();

        String good = totalPage.getGoodCount();
        String bad = totalPage.getBadCount();
        String total = totalPage.getTotalCount();
        String recurring = totalPage.getRecurringCount();

        System.out.println("Good: " + good);
        System.out.println("Bad: " + bad);
        System.out.println("Total: " + total);
        System.out.println("Recurring: " + recurring);

        Assert.assertEquals(good, "6");
        Assert.assertEquals(bad, "4");
        Assert.assertEquals(total, "10");
        Assert.assertEquals(recurring, "2");
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