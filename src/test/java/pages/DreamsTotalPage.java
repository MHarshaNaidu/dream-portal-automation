package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class DreamsTotalPage {

    private WebDriver driver;

    public DreamsTotalPage(WebDriver driver) {
        this.driver = driver;
    }

    public void open() {
        driver.get("https://arjitnigam.github.io/myDreams/dreams-total.html");
    }

    public WebElement getSummaryTable() {
        return driver.findElement(By.cssSelector("table"));
    }

    public String getGoodCount() {
        return driver.findElement(
                By.xpath("//td[contains(text(),'Good')]/following-sibling::td")
        ).getText().trim();
    }

    public String getBadCount() {
        return driver.findElement(
                By.xpath("//td[contains(text(),'Bad')]/following-sibling::td")
        ).getText().trim();
    }

    public String getTotalCount() {
        return driver.findElement(
                By.xpath("//td[contains(text(),'Total')]/following-sibling::td")
        ).getText().trim();
    }

    public String getRecurringCount() {
        return driver.findElement(
                By.xpath("//td[contains(text(),'Recurring')]/following-sibling::td")
        ).getText().trim();
    }
}