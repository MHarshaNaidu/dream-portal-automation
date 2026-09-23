package tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import pages.DreamsDiaryPage;
import utils.DriverFactory;
import utils.DreamAIClassifier;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DreamAIClassificationTest {

    private WebDriver driver;

    @Test
    public void verifyDreamsUsingAI() throws Exception {

        driver = DriverFactory.createDriver();

        DreamsDiaryPage diaryPage =
                new DreamsDiaryPage(driver);

        DreamAIClassifier aiClassifier =
                new DreamAIClassifier();

        diaryPage.open();

        List<WebElement> rows =
                diaryPage.getDreamRows();

        Assert.assertEquals(
                rows.size(),
                10,
                "Expected exactly 10 dream entries."
        );

        // Get unique dream names from the diary
        Set<String> uniqueDreams =
                new HashSet<>();

        for (WebElement row : rows) {

            List<WebElement> cells =
                    diaryPage.getCells(row);

            String dreamName =
                    cells.get(0).getText().trim();

            uniqueDreams.add(dreamName);
        }

        List<String> dreamNames =
                new ArrayList<>(uniqueDreams);

        System.out.println(
                "Unique dreams sent to AI: "
                        + dreamNames.size()
        );

        // One Gemini API request for all unique dreams
        Map<String, String> aiResults =
                aiClassifier.classifyDreams(dreamNames);

        // Compare AI results with website data
        for (WebElement row : rows) {

            List<WebElement> cells =
                    diaryPage.getCells(row);

            String dreamName =
                    cells.get(0).getText().trim();

            String actualType =
                    cells.get(2).getText().trim();

            String aiPrediction =
                    aiResults.get(dreamName);

            System.out.println(
                    "Dream: " + dreamName
                            + " | Actual: " + actualType
                            + " | AI: " + aiPrediction
            );

            Assert.assertNotNull(
                    aiPrediction,
                    "AI did not classify: " + dreamName
            );

            Assert.assertEquals(
                    aiPrediction,
                    actualType,
                    "AI classification does not match actual type for: "
                            + dreamName
            );
        }

        System.out.println(
                "AI classification completed successfully."
        );
    }

    @AfterMethod
    public void tearDown() {

        if (driver != null) {
            driver.quit();
        }
    }
}