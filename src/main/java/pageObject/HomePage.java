package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import utilities.BaseClass;
import utilities.Report;

public class HomePage extends BaseClass {
    public HomePage(WebDriver driver) {
        super(driver);
    }

    ResultPage resultPage = new ResultPage(driver);
    Report logger = Report.getInstance();
    @FindBy(xpath = "//a[@class='menu-categories__link' and contains(@href,'telefony-tv-i-ehlektronika')]")
    WebElement telefonyTvEhlektronikaLink;
    @FindBy(xpath = "//a[contains(text(),'Телефоны')]")
    WebElement telefonyLink;
    @FindBy(xpath = "//a[contains(text(),'Смартфоны')]")
    WebElement smartfonyLink;


    public void userSearchAndGetResult() {
        driverWait();
        selectTelefonyTvEhlektronikaLink();
        selectTelefonyLink();
        clickSmartPhones();
        resultPage.getResultAndWriteToFile();
    }

    @Step("перейти в раздел 'Телефоны, ТВ и электроника'")
    public void selectTelefonyTvEhlektronikaLink() {
        waitVisibility(telefonyTvEhlektronikaLink, 5);
        mouseHover(telefonyTvEhlektronikaLink, logger);
    }

    @Step("перейти в раздел 'Телефоны'")
    public void selectTelefonyLink() {
        waitVisibility(telefonyLink, 5);
        mouseHover(telefonyLink, logger);
    }
    @Step("перейти в раздел 'Смартфоны'")
    public void clickSmartPhones(){
        waitAndClick("Смартфоны", smartfonyLink, logger);
    }
}
