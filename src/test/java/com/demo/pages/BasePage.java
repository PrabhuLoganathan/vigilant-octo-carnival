package com.demo.pages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected Actions actions;
    protected JavascriptExecutor js;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        this.actions = new Actions(driver);
        this.js = (JavascriptExecutor) driver;
        PageFactory.initElements(driver, this);
    }

    protected void click(WebElement el) {
        wait.until(ExpectedConditions.elementToBeClickable(el)).click();
    }

    protected void type(WebElement el, String text) {
        wait.until(ExpectedConditions.visibilityOf(el));
        el.clear();
        el.sendKeys(text);
    }

    protected void selectByVisibleText(WebElement selectEl, String txt) {
        wait.until(ExpectedConditions.visibilityOf(selectEl));
        new Select(selectEl).selectByVisibleText(txt);
    }

    protected String getText(WebElement el) {
        return wait.until(ExpectedConditions.visibilityOf(el)).getText();
    }

    protected boolean isDisplayed(WebElement el) {
        try {
            wait.until(ExpectedConditions.visibilityOf(el));
            return el.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    protected void setRangeInput(WebElement slider, int value) {
        // Use JS to set the value for stability across OSes
        js.executeScript("arguments[0].value=arguments[1]; arguments[0].dispatchEvent(new Event('input'));", slider, value);
    }

    protected void dragAndDrop(WebElement source, WebElement target) {
        actions.dragAndDrop(source, target).perform();
    }

    protected String getAttribute(WebElement el, String attr) {
        return wait.until(ExpectedConditions.visibilityOf(el)).getAttribute(attr);
    }
}
