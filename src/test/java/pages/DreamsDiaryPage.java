package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class DreamsDiaryPage {

    private WebDriver driver;

    public DreamsDiaryPage(WebDriver driver) {
        this.driver = driver;
    }

    public void open() {
        driver.get("https://arjitnigam.github.io/myDreams/dreams-diary.html");
    }

    public List<WebElement> getDreamRows() {
        return driver.findElements(By.cssSelector("table tbody tr"));
    }

    public List<WebElement> getCells(WebElement row) {
        return row.findElements(By.tagName("td"));
    }
}