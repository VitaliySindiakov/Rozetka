package utilities;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.LogStatus;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static utilities.Data.pathToReport;

public class Report {

    private static Report instance;
    public ExtentReports logger;
    private String reportFileName;
    private String reportFilePath;

    private Report() {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        reportFileName = "Report-" + dateFormat.format(date) + ".html";
        reportFilePath = pathToReport + reportFileName;
    }

     public void addScreen(String screenshotName) {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        logger.attachScreenshot(pathToReport + dateFormat.format(date) + screenshotName + ".png");//path to screanshot for Report
    }

    public static Report getInstance() {
        if (instance == null) {
            instance = new Report();
        }
        return instance;
    }

     public void info(String info) {
        getLogger().log(LogStatus.PASS, info);
    }

    public ExtentReports start(Class suite, String title) {
        String className = suite.getName().replaceAll("class", "");
        logger = ExtentReports.get(suite);
        logger.init(reportFilePath, false);
        logger.startTest(className + "_" + title);
        return logger;
    }

     public void end() {
        logger.endTest();
        logger = null;
    }

    public ExtentReports getLogger() {
        return logger;
    }

}
