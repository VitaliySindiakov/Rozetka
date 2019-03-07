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

    @Test(priority = 1, description = "User Search for Phone")
    @Description("User Search for Phone")
    public void test01() {
        logger.start(TestSuite.class, "User Search for Phone");
        homePage.userSearchAndGetResult();
    }

    @AfterMethod
    public void after(ITestResult result) {
        afterTest(driver, result, logger);
    }

}
