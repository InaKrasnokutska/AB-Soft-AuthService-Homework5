package net.absoft;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;


public class BaseTest {
    @BeforeMethod
    public void sutUp(){
        System.out.println("Base setup");
    }

    @AfterMethod()
    public void tearDown(){
        System.out.println("Base teardown");
    }
}