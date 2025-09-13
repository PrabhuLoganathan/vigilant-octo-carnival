package com.demo.config;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class DriverFactory {
    private static final ThreadLocal<WebDriver> TL_DRIVER = new ThreadLocal<>();

    public static void initDriver() {
        if (TL_DRIVER.get() == null) {
            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--start-maximized");
            // Allow running in CI containers:
            if (Boolean.parseBoolean(System.getProperty("headless","false"))) {
                options.addArguments("--headless=new");
            }
            TL_DRIVER.set(new ChromeDriver(options));
        }
    }

    public static WebDriver getDriver() {
        return TL_DRIVER.get();
    }

    public static void quitDriver() {
        WebDriver driver = TL_DRIVER.get();
        if (driver != null) {
            driver.quit();
            TL_DRIVER.remove();
        }
    }
}
