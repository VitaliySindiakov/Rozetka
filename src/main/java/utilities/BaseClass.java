package utilities;

import Core.Notification;
import Core.PropertiesCache;
import com.relevantcodes.extentreports.LogStatus;
import io.qameta.allure.Step;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.testng.AssertJUnit.fail;
import static utilities.Data.pathToReport;


public abstract class BaseClass {
    @FindBy(className = "popup_plugin_info-accept-cookies__link")
    WebElement cookies;
    int time = 25;
    int implicit = 4;
    private static long time2 = 25;

    public long implicitWait = Long.parseLong(PropertiesCache.getProperty("wait.implicit"));
    public long pageWait = Long.parseLong(PropertiesCache.getProperty("wait.page"));
    public long scriptWait = Long.parseLong(PropertiesCache.getProperty("wait.script"));

    public static Notification nt = new Notification();

    public String homeUrl;
    protected WebDriver driver;
    protected WebDriverWait wait;


    public BaseClass(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
        wait = new WebDriverWait(driver, time);
    }

     public void driverWait() {
        waitForLoad();
        driver.manage().timeouts().pageLoadTimeout(pageWait, TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(scriptWait, TimeUnit.SECONDS);
    }

    @Step("Try to close Cookies")
     public void closeCookies() {
        driverWait();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int count = 1;
        do {
            System.out.println("Try close cookies");
            System.out.println("Count is: " + count);
            count++;
            switchTabs(driver, 0);
            if (driver.findElements(By.className("popup_plugin_info-accept-cookies__link")).size() == 0) {
                System.out.println("Cookies not found");
                break;
            } else if (cookies.isDisplayed()) {
                System.out.println("Cookies still visible");
                cookies.click();
                switchTabs(driver, 0);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                closeTabs(driver, 1);
            }
        } while (count <= 5);
    }

    @Step("Try to switch Tabs")
     public void switchTabs(WebDriver driver, int tabNumber) {
        try {
            ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
            driver.switchTo().window(tabs.get(tabNumber));
        } catch (Exception e) {
            driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL + "t");
        }
    }

    @Step("Try to close Tabs")
     public void closeTabs(WebDriver driver, int tabNumber) {
        try {
            ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
            driverWait();
            driver.switchTo().window(tabs.get(tabNumber));
            driver.close();
        } catch (Exception e) {
            driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL + "t");
        }
    }

     public void makeScreenshotAndAddItToTheReport(WebDriver driver, String screenName, Report logger) {
        driverWait();
        captureScreenshot(driver, screenName);
        logger.addScreen(screenName);
    }
    @Step("Try to assert One Of Array Element Present")
    public boolean assertOneOfArrayElementPresent(Report logger, List<WebElement> elementsList) {
        driverWait();
        String messageForSuccess = "Element present";
        String messageForFail = "No Such Element";
        String step = "assert Element present at the page";
        logger.info(step);
        for (int second = 0; ; second++) {
            if (second >= 11) {
                logger.getLogger().log(LogStatus.FAIL, "ACTUAL RESULT: " + messageForFail);
                fail(messageForFail);
                return false;
            } else {
                for (WebElement element : elementsList) {
                    try {
                        if (element.isDisplayed()) {
                            logger.getLogger().log(LogStatus.PASS, "ACTUAL RESULT: " + messageForSuccess);
                            System.out.println(messageForSuccess + " - '" + element.getText() + "'");
                            return true;
                        }
                    } catch (Exception e) {
                    }
                }
            }
            try {
                Thread.sleep(2500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    @Step("Try to wait Clickability")
     public void waitClickability(WebElement element, int period) {
        driverWait();
        new WebDriverWait(driver, period).
                until(ExpectedConditions.elementToBeClickable(element));
    }
    @Step("Try to wait Visibility")
     public void waitVisibility(WebElement element, int period) {
        driverWait();
        new WebDriverWait(driver, period).
                until(ExpectedConditions.visibilityOf(element));
    }

    public boolean waitVisibilityBolean(WebElement element, int period) {
        driverWait();
        new WebDriverWait(driver, period).
                until(ExpectedConditions.visibilityOf(element));
        return true;
    }

    public boolean waitVisibilityNoBolean(WebElement element, int period) {
        driverWait();
        new WebDriverWait(driver, period).
                until(ExpectedConditions.not(ExpectedConditions.visibilityOf(element)));
        return true;
    }
    @Step("Try assert -  is ElementAbsent")
    public boolean isElementAbsent(By locatorKey, Report logger) {
        driverWait();
        String messageForFail = "FAILED! Element still present !";
        String succesMessage = "Success - Element Absent";
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            driver.findElement(locatorKey);
            logger.getLogger().log(LogStatus.FAIL, "ACTUAL RESULT: " + messageForFail);
            fail(messageForFail);
            return false;
        } catch (org.openqa.selenium.NoSuchElementException e) {
            logger.getLogger().log(LogStatus.PASS, "ACTUAL RESULT: " + succesMessage);
            System.out.println("ACTUAL RESULT: " + succesMessage);
            System.out.println(succesMessage);
            return true;
        }
    }

    @Step("Try to wait Not Visibility")
    public boolean waitNotVisibility(WebElement element, int period, Report logger) {
        String messageForFail = "FAILED! Element still visible !";
        String succesMessage = "Succes - element not visible";
        for (int second = 0; ; second++) {
            try {
                if (second >= 5) {
                    logger.getLogger().log(LogStatus.FAIL, "ACTUAL RESULT: " + messageForFail);
                    fail(messageForFail);
                    return false;
                } else {
                    new WebDriverWait(driver, period).until(ExpectedConditions.not(ExpectedConditions.visibilityOf(element)));
                    logger.getLogger().log(LogStatus.PASS, "ACTUAL RESULT: " + succesMessage);
                    System.out.println("ACTUAL RESULT: " + succesMessage);
                    return true;
                }
            } catch (Exception e) {
            }
        }
    }


    @Step("Try to wait And Click")
     public void waitAndClick(String elementName, WebElement element, Report logger) {
        String step = "Click '" + elementName + "'";
        try {
            logger.info(step);
            driverWait();
            System.out.println("Try : " + step);
            waitClickability(element, time);
            waitVisibility(element, time);
            element.click();
            driver.manage().timeouts().pageLoadTimeout(pageWait, TimeUnit.SECONDS);
        } catch (StaleElementReferenceException ex) {
            element.click();
        } catch (WebDriverException wb) {
            element.click();
        }
    }

    @Step("Try to click If Value Is True")
     public void clickIfValueIsTrue(String step, WebElement element, Report logger) {
        driverWait();
        String value = element.getAttribute("value");
        try {
            if (value.contains("1")) {
                logger.info(step);
                driverWait();
                waitClickability(element, time);
                waitVisibility(element, time);
                element.click();
            } else {
                System.out.println("CheckBox are clear");
            }
        } catch (Exception e) {
        }
    }

    @Step("Try to accept Alert")
     public void acceptAlert(Report logger) {
        for (int i = 0; ; i++) {
            if (i >= 5) {
                logger.info("Accept Alert");
                logger.getLogger().log(LogStatus.PASS, "");
            } else {
                try {
                    {
                        logger.info("Accept Alert");
                        Thread.sleep(2500);
                        Alert alert = driver.switchTo().alert();
                        String text = alert.getText();
                        alert.accept();
                        logger.getLogger().log(LogStatus.PASS, "'" + text + "' - Alert present");
                        System.out.println("'" + text + "' - Alert present");
                        break;
                    }
                } catch (Exception e) {
                    logger.getLogger().log(LogStatus.FAIL, " Allert not found");
                    break;
                }
            }
        }
    }

    @Step("Try to click If Value Is False")
    public void clickIfValueIsFalse(String step, WebElement element, Report logger) {
        driverWait();
        System.out.println("try select checkBox");
        String value = element.getAttribute("value");
        try {
            if (value.contains("1")) {
                System.out.println("CheckBox already selected");
            } else {
                logger.info(step);
                driverWait();
                waitClickability(element, time);
                waitVisibility(element, time);
                element.click();
                logger.getLogger().log(LogStatus.PASS, "checkBox - successfully selected");
                System.out.println("checkBox - successfully selected");
            }
        } catch (Exception e) {
        }
    }

    @Step("Try to enter Data")
   public void enterData(String elemenName, WebElement element, String data, Report logger) {
        String step = "Type '" + elemenName + "'";
        driverWait();
        logger.info(step);
        element.clear();
        System.out.println("Try : " + step);
        element.sendKeys(data);
    }

    @Step("Try to wait And Move And Click")
     public void waitAndMoveAndClick(String elementName, WebElement element, Report logger) {
        String step = "Click '" + elementName + "'";
        driverWait();
        try {
            logger.info(step);
            driverWait();
            Actions actions = new Actions(driver);
            actions.moveToElement(element);
            actions.perform();
            waitVisibility(element, time);
            waitClickability(element, time);
            actions.moveToElement(element);
            actions.perform();
            driver.manage().timeouts().implicitlyWait(implicitWait, TimeUnit.SECONDS);
            System.out.println("Try : " + step);
            element.click();
        } catch (StaleElementReferenceException ex) {
            Actions actions = new Actions(driver);
            actions.moveToElement(element);
            actions.perform();
            element.click();
        }
    }

    @Step("Try to move To Element")
     public void moveToElement(WebElement element, Report logger) {
        driverWait();
        Actions actions = new Actions(driver);
        actions.moveToElement(element);
        actions.perform();
    }
    @Step("Try to move mouse")
    public void mouseHover(WebElement element, Report logger,String elName) {
        System.out.println("Try: mouseHover -'" + elName+"'");
        driverWait();
        Actions actions = new Actions(driver);
        actions.moveToElement(element).build().perform();

    }
    @Step("Try to Assert Text Present At Page")
    public static void assertTextPresentAtPage(String text, WebDriver driver, Report logger) {
        String messageForFail = "'" + text + " ' - NOT FOUND";
        String messageForSuccess = "'" + text + " '  - present";
        driver.manage().timeouts().pageLoadTimeout(time2, TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(time2, TimeUnit.SECONDS);
        for (int second = 0; ; second++) {
            if (second >= 30) {
                logger.getLogger().log(LogStatus.FAIL, "ACTUAL RESULT: " + messageForFail);
                fail(messageForFail);
            } else {
                try {
                    driver.manage().timeouts().pageLoadTimeout(time2, TimeUnit.SECONDS);
                    driver.manage().timeouts().setScriptTimeout(time2, TimeUnit.SECONDS);
                    if (driver.getPageSource().contains(text)) {
                        logger.getLogger().log(LogStatus.PASS, "ACTUAL RESULT: " + messageForSuccess);
                        System.out.println(messageForSuccess);
                        break;
                    }
                } catch (Exception e) {
                }
            }
        }
    }

    public static void assertTextAbsentAtPage(WebElement element, String locator, String text, String messageForSuccess, String messageForFail, WebDriver driver, Report logger) {
        driver.manage().timeouts().pageLoadTimeout(time2, TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(time2, TimeUnit.SECONDS);
        if (getText(driver, element, locator).toString().contains(text)) {
            System.out.println(getText(driver, element, locator).toString());
            logger.getLogger().log(LogStatus.FAIL, "ACTUAL RESULT: " + messageForFail);
            System.out.println(messageForFail);
        } else {
            logger.getLogger().log(LogStatus.PASS, "ACTUAL RESULT: " + messageForSuccess);
            System.out.println(messageForSuccess);
        }
    }

    @Step("Try to assert Element Present")
     public void assertElementPresent(String elementName, WebElement element, Report logger) {
        logger.info("assert element present at page");
        String messageForFail = "'" + elementName + "' - Not Found";
        String messageForSuccess = "'" + elementName + "' - Present";
        driverWait();
        System.out.println("Try to assert presents of element '" + elementName + "' ");
        for (int second = 0; ; second++) {
            if (second >= 30) {
                logger.getLogger().log(LogStatus.FAIL, "ACTUAL RESULT: " + messageForFail);
                fail(messageForFail);
            } else {
                try {
                    driverWait();
                    if (element.isDisplayed()) {
                        logger.getLogger().log(LogStatus.PASS, "ACTUAL RESULT: " + messageForSuccess);
                        System.out.println(messageForSuccess);
                        break;
                    }
                } catch (Exception e) {
                }
            }
        }
    }

    public static void assertPageTitle(String text, WebDriver driver, Report logger) {
        driver.manage().timeouts().pageLoadTimeout(time2, TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(time2, TimeUnit.SECONDS);
        System.out.println(driver.getTitle());
        for (int second = 0; ; second++) {
            if (second >= 10) {
                logger.getLogger().log(LogStatus.FAIL, "ACTUAL RESULT:  Title doesn't match to text");
                fail("Title doesn't match to text");
            } else {
                try {
                    driver.manage().timeouts().pageLoadTimeout(time2, TimeUnit.SECONDS);
                    driver.manage().timeouts().setScriptTimeout(time2, TimeUnit.SECONDS);
                    if (driver.getTitle().contains(text)) {
                        logger.getLogger().log(LogStatus.PASS, "ACTUAL RESULT:  Title matches to text");
                        System.out.println("Title matches to text");
                        break;
                    }
                } catch (Exception e) {
                }
            }
        }
    }

    @Step("Try to assert URL")
    public static void assertURL(WebDriver driver, Report logger, String expectedURL) {
        driver.manage().timeouts().implicitlyWait(time2, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(time2, TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(time2, TimeUnit.SECONDS);
        String actualURL = driver.getCurrentUrl();
        System.out.println("current URL is: " + driver.getCurrentUrl());
        for (int second = 0; ; second++) {
            if (second >= 30) {
                logger.getLogger().log(LogStatus.FAIL, "ACTUAL RESULT: Url does not match");
                fail("Url does not match");
            } else {
                try {
                    driver.manage().timeouts().implicitlyWait(time2, TimeUnit.SECONDS);
                    driver.manage().timeouts().pageLoadTimeout(time2, TimeUnit.SECONDS);
                    driver.manage().timeouts().setScriptTimeout(time2, TimeUnit.SECONDS);
                    if (actualURL.equals(expectedURL)) {
                        logger.getLogger().log(LogStatus.PASS, "ACTUAL RESULT: Url matches");
                        System.out.println("Url matches");
                        break;
                    }
                } catch (Exception e) {
                }
            }
        }
    }
    @Step("Try to contains URL")
    public static void containsURL(WebDriver driver, Report logger, String expectedURL) {
        driver.manage().timeouts().pageLoadTimeout(time2, TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(time2, TimeUnit.SECONDS);
        String actualURL = driver.getCurrentUrl();
        System.out.println("current URL is : " + actualURL);
        for (int second = 0; ; second++) {
            if (second >= 30) {
                driver.manage().timeouts().pageLoadTimeout(time2, TimeUnit.SECONDS);
                driver.manage().timeouts().setScriptTimeout(time2, TimeUnit.SECONDS);
                logger.getLogger().log(LogStatus.FAIL, "ACTUAL RESULT: Url does not match");
                fail("Url does not match");
            } else {
                try {
                    if (actualURL.contains(expectedURL)) {
                        logger.getLogger().log(LogStatus.PASS, "ACTUAL RESULT: Url matches");
                        System.out.println("Url matches");
                        break;
                    }
                } catch (Exception e) {
                }
            }
        }
    }

    public static void clickIfContains(WebDriver driver, Report logger, WebElement element, String expectedText) {
        for (int second = 0; ; second++) {
            if (second >= 15) {
                logger.getLogger().log(LogStatus.FAIL, "ACTUAL RESULT: Text '" + expectedText + "' does not match");
                fail("Text '" + expectedText + "' does not match");
            } else {
                if (element.getText().contains(expectedText)
                ) {
                    element.click();
                    logger.getLogger().log(LogStatus.FAIL, "ACTUAL RESULT: Text '" + expectedText + "' present in the button");
                    System.out.println("button found");
                    break;
                }
            }
        }
    }

     public void handleWindow() {
        homeUrl = driver.getWindowHandle();
    }

    public static void openNewWindow(WebDriver driver, String homePage) {

        Set<String> window = driver.getWindowHandles();
        Iterator iterator = window.iterator();
        String currentWindowId = "WIndow Not found";
        while (iterator.hasNext()) {
            currentWindowId = iterator.next().toString();

            if (!currentWindowId.equals(homePage)) driver.switchTo().window(currentWindowId);
        }
    }

     public void backToPreviousWindow() {
        driver.switchTo().window(homeUrl);
    }

     public void selectEmptyItemAtDropList(String step, WebElement listName, Report logger) {
        logger.info(step);
        Select emptyItem = new Select(listName);
        emptyItem.selectByIndex(0);
    }

     public void selectItemByValue(String elementName, WebElement element, String value, Report logger) {
        String step = "Try to select : '" + elementName + "' ";
        logger.info(step);
        Select dropdown = new Select(element);
        dropdown.selectByValue(value);
    }

     public void selectByIndex(WebElement element, int index) {
        Select input = new Select(element);
        input.selectByIndex(index);
    }

     public void mouseOver(WebElement element) {
        Actions builder = new Actions(driver);
        builder.moveToElement(element).build().perform();
    }

    public static void choosePhotoForUpload(String filePath) throws AWTException {
        StringSelection stringSelection = new StringSelection(filePath);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
        Robot robot = new Robot();
        robot.delay(1000);
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.delay(300);
        robot.keyPress(KeyEvent.VK_V);
        robot.delay(300);
        robot.keyRelease(KeyEvent.VK_V);
        robot.delay(300);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.delay(300);
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.delay(300);
        robot.keyRelease(KeyEvent.VK_ENTER);
        robot.delay(300);
    }


    public static String captureScreenshot(WebDriver driver, String screenshotName) {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        try {
            File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(scrFile,
                    new File(pathToReport + dateFormat.format(date) + screenshotName + ".png"));
        } catch (Exception e) {
            System.out.println("Screenshot was not taken" + e.getMessage());
        }
        return screenshotName;
    }

    @Step("get Text")
    public static WebElement getText(WebDriver driver, WebElement element, String locator) {
        element = driver.findElement(By.cssSelector(locator));
        return element;
    }

    @Step("get Random")
    public static String random(int min, int max) {
        int randomInt = (int) ((Math.random() * ((max - min) + 1)) + min);
        String numberString = Integer.toString(randomInt);
        return numberString;
    }

    @Step("Try to assert text from element")
    public static void containTextFromElement(Report logger, WebElement element, String expectedText, WebDriver driver) {
        driver.manage().timeouts().pageLoadTimeout(time2, TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(time2, TimeUnit.SECONDS);
        System.out.println("Try to assert text from element - '" + expectedText + "'");
        for (int second = 0; ; second++) {
            driver.manage().timeouts().pageLoadTimeout(time2, TimeUnit.SECONDS);
            driver.manage().timeouts().setScriptTimeout(time2, TimeUnit.SECONDS);
            if (second >= 130) {
                logger.getLogger().log(LogStatus.FAIL, "ACTUAL RESULT: Text '" + expectedText + "' -  not found");
                fail("Text '" + expectedText + "' -  not found");
            } else {
                try {
                    driver.manage().timeouts().pageLoadTimeout(time2, TimeUnit.SECONDS);
                    element.getText();
                    if (element.getText().contains(expectedText)) {
                        logger.getLogger().log(LogStatus.PASS, "ACTUAL RESULT: Text '" + expectedText + "' -  present");
                        System.out.println("Text '" + expectedText + "' -  present");
                        break;
                    }
                } catch (Exception e) {
                }
            }
        }
    }

     public void waitForLoad() {
        ExpectedCondition<Boolean> pageLoadCondition = new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
            }
        };
        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(pageLoadCondition);
    }
}

