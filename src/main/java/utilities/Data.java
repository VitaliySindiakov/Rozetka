package utilities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Data {
    public static Date date = new Date();
    public static DateFormat dateFormat = new SimpleDateFormat("MMddHHmmSS");
    public static String date1 = dateFormat.format(date);

    public  static String home = "https://rozetka.com.ua";


//    public static final String pathToReport = "C:\\Program Files (x86)\\Jenkins\\workspace\\GoDateNow\\target\\";
    public static final  String pathToReport = "C:\\Rozetka\\target\\";
}