import Core.BaseTestClassWebdriver;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(TestListenerAdapter.class)
@Epic("TestTask")
@Feature("Rozetka")
public class TestSuite extends BaseTestClassWebdriver {

    @Test(priority = 1, description = "Сценарий 1")
    @Description("Сценарий 1")
    public void test01() {
        logger.start(TestSuite.class, "Сценарий 1");
        homePage.userSearchAndGetResult();
    }

    @AfterMethod
    public void after(ITestResult result) {
        afterTest(driver, result, logger);
    }

}
