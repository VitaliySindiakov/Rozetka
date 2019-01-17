package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import utilities.BaseClass;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.List;

import static utilities.Data.pathToReport;

public class ResultPage extends BaseClass {
    public ResultPage(WebDriver driver) {
        super(driver);
    }
@Step("Записать результат в файл")
    public void getResultAndWriteToFile() {
        List<WebElement> name = driver.findElements(By.cssSelector("#catalog_goods_block .g-i-tile.g-i-tile-catalog"));
        WebElement[] name2;
        name2 = driver.findElements(By.cssSelector("#catalog_goods_block .g-i-tile.g-i-tile-catalog")).toArray(new WebElement[0]);
        FileWriter writer = null;
        try {
            writer = new FileWriter(pathToReport+"Note.txt", false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < name2.length; i++) {
            name2[i] = name.get(i);
            System.out.println(name2[i].getText());
            try {
                String text = "\n"+name2[i].getText()+"\n";
                writer.write(text);
                writer.append('\n');
                writer.flush();
            } catch (
                    IOException e) {
                System.out.println("Error: " + e);
                e.printStackTrace();
            }
        }
    }
}
