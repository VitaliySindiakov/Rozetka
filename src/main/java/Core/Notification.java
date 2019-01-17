package Core;

import io.qameta.allure.Step;

public class Notification {
    public static void main(String[] args) {


    }
    @Step("")public void message(){
        System.out.println(" ");
        System.out.println("@@@@@@@@@@________@@@@_________@@@@@@@@@____@@@@@@@@@@_____@@@@@@@@___@@@@@@@@@");
        System.out.println("@@________@______@____@_______@@___________@@______________@@_________@@_______@@");
        System.out.println("@@_________@____@______@______@@___________@@______________@@@@_______@@________@@");
        System.out.println("@@________@____@@@@@@@@@@______@@@@@@@@@_____@@@@@@@@@_____@@@@_______@@________@@");
        System.out.println("@@@@@@@@@@____@__________@_____________@@____________@@____@@_________@@________@@");
        System.out.println("@@___________@@__________@@____________@@____________@@____@@_________@@_______@@");
        System.out.println("@@__________@@@__________@@@___@@@@@@@@@_____@@@@@@@@@_____@@@@@@@@@__@@@@@@@@@");
    }
    @Step("")public void succesMessage(){
        System.out.println("<<< Test successfully Passed >>>");
    }
    @Step("")public void justEmpty(){
        System.out.println("________________________________");
    }
    @Step("")public void testFailed(){
        System.out.println("***** T E S T  F A I L E D *****");
    }

}
