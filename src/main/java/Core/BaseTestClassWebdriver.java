package Core;

import com.relevantcodes.extentreports.LogStatus;
import io.github.bonigarcia.wdm.ChromeDriverManager;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import org.apache.jasper.tagplugins.jstl.core.Url;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import pageObject.HomePage;
import utilities.BaseClass;
import utilities.Report;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import static org.openqa.selenium.remote.BrowserType.CHROME;
import static utilities.BaseClass.nt;
import static utilities.Data.home;

public class BaseTestClassWebdriver {

    private String browser = System.getProperty("browser", CHROME);
    private long implicitWait = Long.parseLong(PropertiesCache.getProperty("wait.implicit"));
    private long pageWait = Long.parseLong(PropertiesCache.getProperty("wait.page"));
    private long scriptWait = Long.parseLong(PropertiesCache.getProperty("wait.script"));
    protected WebDriver driver;
    public Report logger;
    public HomePage homePage;

    Url url = new Url();

    private DesiredCapabilities setDesiredCapabilities(String platform, String remoteBrowser) throws MalformedURLException {
        DesiredCapabilities caps = new DesiredCapabilities();
        if (platform.equalsIgnoreCase(Platform.WIN10.name())) {
            caps.setPlatform(Platform.WIN10);
            caps.setBrowserName(remoteBrowser);
        }
        return caps;
    }

    @Parameters({"platform", "remoteBrowser"})
    @BeforeMethod
     @Step("setUp")public void setUp() {
        switch (browser.toLowerCase()) {
            case CHROME:
                ChromeDriverManager.getInstance().setup();
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--headless");
                options.addArguments("--window-size=1920,1080");
                options.addArguments("--disable-popup-blocking");
                options.addArguments("--ignore-certificate-errors");
                driver = new ChromeDriver(options);
                goToWebPage(driver);
                driver.manage().window().maximize();
                driver.manage().timeouts().implicitlyWait(implicitWait, TimeUnit.SECONDS);
                driver.manage().timeouts().pageLoadTimeout(pageWait, TimeUnit.SECONDS);
                driver.manage().timeouts().setScriptTimeout(scriptWait, TimeUnit.SECONDS);
                break;
        }

        logger = Report.getInstance();
        homePage = new HomePage(driver);
        driverWait();
    }
    @Step("")public void driverWait() {
        driver.manage().timeouts().pageLoadTimeout(pageWait, TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(scriptWait, TimeUnit.SECONDS);
    }

    public static void makeScreenshotAndAddItToTheReport(WebDriver driver, String screenName, Report logger) {
        BaseClass.captureScreenshot(driver, screenName);
        logger.addScreen(screenName);
    }

    @Attachment(value = "Page screenshot", type = "image/png")
    public static byte[] saveScreenshotPNG(WebDriver driver){
        return ((TakesScreenshot)driver).getScreenshotAs(OutputType.BYTES);
    }

    public static void afterTest(WebDriver driver, ITestResult result, Report logger) {
        String className = String.valueOf(result.getTestClass()).replaceAll("TestClass name=class ", "");
        String testName = result.getName();
        String screenName = className + testName;

        if (ITestResult.SUCCESS == result.getStatus()) {
            logger.getLogger().log(LogStatus.PASS, "SUCCESS");
            makeScreenshotAndAddItToTheReport(driver, screenName, logger);
            saveScreenshotPNG(driver);
            nt.succesMessage();
            nt.justEmpty();
        }

        if (ITestResult.FAILURE == result.getStatus()) {
            logger.getLogger().log(LogStatus.FAIL, "FAILURE");
            makeScreenshotAndAddItToTheReport(driver, screenName, logger);
            saveScreenshotPNG(driver);
            nt.testFailed();
            nt.justEmpty();
        }
        logger.end();
        driver.quit();
    }

    @Step("")public void wait(WebElement element, int period) {
        new WebDriverWait(driver, period).
                until(ExpectedConditions.visibilityOf(element));
    }

    @Step("")public void waitCkickabilityOfElement(WebElement element, int period) {
        new WebDriverWait(driver, period).until(ExpectedConditions.elementToBeClickable(element));
    }

    @Parameters("browser")
    @Step("")public void crossbrowserTesting(String browserName) {
        if (browserName.equalsIgnoreCase("chrome")) {
            driver = new FirefoxDriver();
        } else if (browserName.equalsIgnoreCase("firefox")) {
            ChromeDriverManager.getInstance().setup();
            driver = new ChromeDriver();
        }
    }

    public boolean assertElementNotPresent(Report logger, String messageForSuccess, String messageForFail, WebElement element) throws InterruptedException {
        try {
            if (element.isDisplayed()) {
                logger.getLogger().log(LogStatus.FAIL, messageForFail);
                return false;
            } else {
                logger.getLogger().log(LogStatus.PASS, messageForSuccess);
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    @Step("")public void maximizeWindow(WebDriver driver) {
        driver.manage().window().maximize();
    }


    @Step("")public void goToWebPage(WebDriver driver) {
        driver.get(home);
        driver.manage().timeouts().implicitlyWait(implicitWait, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(pageWait, TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(scriptWait, TimeUnit.SECONDS);
        driver.manage().deleteAllCookies();
    }


}









