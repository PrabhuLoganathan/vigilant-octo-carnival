package com.demo.practise;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class StaleElementDemo {

    public static void main(String[] args) {
        // Set up driver (make sure ChromeDriver is in your PATH)
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();

        try {
            // Open a sample site
            driver.get("https://the-internet.herokuapp.com/dynamic_controls");

            // Call method that causes stale element exception
            causeStaleElement(driver);



        } finally {
            driver.quit();
        }
    }

    // ❌ This will throw StaleElementReferenceException
    public static void causeStaleElement(WebDriver driver) {
        System.out.println("---- Trying stale element ----");
        WebElement checkbox = driver.findElement(By.id("checkbox")); // locate element

        // Click remove button (this removes checkbox from DOM)
        driver.findElement(By.xpath("//button[text()='Remove']")).click();
        driver.navigate().refresh();

        try {
            // Try to use old reference (now DOM changed → stale element)
            System.out.println("Checkbox displayed? " + checkbox.isDisplayed());
        } catch (Exception e) {
            System.out.println("Caught Exception: " + e);

            handleStaleElementWithRefreshed(driver);
        }
    }



    public static void handleStaleElementWithRefreshed(WebDriver driver) {
        System.out.println("---- Handling stale element (refreshed wrapper) ----");
        By checkboxBy = By.id("checkbox");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // This wraps the condition so Selenium re-finds the element if the old one turns stale
        WebElement checkbox = wait.until(
                ExpectedConditions.refreshed(ExpectedConditions.visibilityOfElementLocated(checkboxBy))
        );

        System.out.println("Checkbox displayed? " + checkbox.isDisplayed());
    }



    // ✅ Correct way: Re-locate element after DOM change
    public static void handleStaleElement(WebDriver driver) {
        System.out.println("---- Handling stale element ----");

        // Click Add button (to add checkbox again)
        driver.findElement(By.xpath("//button[text()='Add']")).click();

        // Re-locate the checkbox fresh
        WebElement checkbox = driver.findElement(By.id("checkbox"));

        // Now this works fine
        System.out.println("Checkbox displayed? " + checkbox.isDisplayed());
    }

}
