package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HomePage {

    private WebDriver driver;

    private final String URL =
            "https://arjitnigam.github.io/myDreams/";

    private By loadingMessage =
            By.xpath("//*[contains(text(),'Welcome to the Dreams Section')]");

    private By myDreamsButton =
            By.xpath("//*[contains(translate(text(),'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ'),'MY DREAMS')]");

    public HomePage(WebDriver driver) {
        this.driver = driver;
    }
    public void clickMyDreamsAndWaitForNewWindows() {

        WebDriverWait wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(10)
        );

        clickMyDreams();

        wait.until(
                driver -> driver.getWindowHandles().size() >= 3
        );
    }

    public void open() {
        driver.get(URL);
    }

    public boolean isLoadingMessageVisible() {
        return !driver.findElements(loadingMessage).isEmpty()
                && driver.findElements(loadingMessage).get(0).isDisplayed();
    }

    public void waitForLoadingToDisappear() {
        WebDriverWait wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(10)
        );

        wait.until(
                ExpectedConditions.invisibilityOfElementLocated(
                        loadingMessage
                )
        );
    }

    public boolean isMyDreamsButtonVisible() {
        return driver.findElement(myDreamsButton).isDisplayed();
    }

    public void clickMyDreams() {
        driver.findElement(myDreamsButton).click();
    }
}