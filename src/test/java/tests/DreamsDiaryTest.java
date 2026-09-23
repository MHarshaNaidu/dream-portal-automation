package tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.DreamsDiaryPage;
import utils.DriverFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import org.testng.ITestResult;
import utils.ScreenshotUtil;

public class DreamsDiaryTest {

    private WebDriver driver;
    private DreamsDiaryPage diaryPage;

    @BeforeMethod
    public void setUp() {
        driver = DriverFactory.createDriver();
        diaryPage = new DreamsDiaryPage(driver);
    }

    @Test
    public void verifyDreamEntries() {

        diaryPage.open();

        List<WebElement> rows = diaryPage.getDreamRows();

        System.out.println("Number of dream rows: " + rows.size());

        // Requirement: exactly 10 dream entries
        Assert.assertEquals(
                rows.size(),
                10,
                "Diary should contain exactly 10 dream entries"
        );

        for (WebElement row : rows) {

            List<WebElement> cells = diaryPage.getCells(row);

            // Each row must contain:
            // Dream Name | Days Ago | Dream Type
            Assert.assertEquals(
                    cells.size(),
                    3,
                    "Each dream row should contain exactly 3 columns"
            );

            String dreamName = cells.get(0).getText().trim();
            String daysAgo = cells.get(1).getText().trim();
            String dreamType = cells.get(2).getText().trim();

            System.out.println(
                    "Dream: " + dreamName +
                            " | Days Ago: " + daysAgo +
                            " | Type: " + dreamType
            );

            // Dream Name must not be empty
            Assert.assertFalse(
                    dreamName.isEmpty(),
                    "Dream name should not be empty"
            );

            // Days Ago must not be empty
            Assert.assertFalse(
                    daysAgo.isEmpty(),
                    "Days Ago should not be empty"
            );

            // Dream Type must be Good or Bad
            Assert.assertTrue(
                    dreamType.equals("Good") || dreamType.equals("Bad"),
                    "Dream type must be Good or Bad"
            );
        }
    }
    @Test
    public void verifyRecurringDreams() {

        diaryPage.open();

        List<WebElement> rows = diaryPage.getDreamRows();

        Map<String, Integer> dreamCount = new HashMap<>();

        for (WebElement row : rows) {

            List<WebElement> cells = diaryPage.getCells(row);

            String dreamName = cells.get(0).getText().trim();

            dreamCount.put(
                    dreamName,
                    dreamCount.getOrDefault(dreamName, 0) + 1
            );
        }

        int recurringDreamCount = 0;

        for (Map.Entry<String, Integer> entry : dreamCount.entrySet()) {

            if (entry.getValue() > 1) {
                recurringDreamCount++;

                System.out.println(
                        "Recurring dream: " +
                                entry.getKey() +
                                " (" +
                                entry.getValue() +
                                " times)"
                );
            }
        }

        Assert.assertEquals(
                recurringDreamCount,
                2,
                "There should be exactly 2 recurring dream names"
        );

        Assert.assertTrue(
                dreamCount.get("Flying over mountains") > 1,
                "Flying over mountains should be recurring"
        );

        Assert.assertTrue(
                dreamCount.get("Lost in maze") > 1,
                "Lost in maze should be recurring"
        );
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