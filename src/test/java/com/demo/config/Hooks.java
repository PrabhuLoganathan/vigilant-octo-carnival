package com.demo.config;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.openqa.selenium.WebDriver;

public class Hooks {

    @Before
    public void beforeScenario() {
        DriverFactory.initDriver();
        WebDriver driver = DriverFactory.getDriver();
        // Base URL can be overridden with -DbaseUrl=...
        String baseUrl = System.getProperty("baseUrl", "https://seleniumbase.io/demo_page");
        driver.get(baseUrl);
    }

    @After
    public void afterScenario() {
        DriverFactory.quitDriver();
    }
}
