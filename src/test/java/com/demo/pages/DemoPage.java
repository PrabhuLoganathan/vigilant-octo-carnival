package com.demo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;

import java.util.List;

public class DemoPage extends BasePage {

    public DemoPage(WebDriver driver) {
        super(driver);
    }

    // ===== Headings (via @FindBys for AND condition: h2 inside #myForm) =====
    @FindBys({
        @FindBy(id = "myForm"),
        @FindBy(tagName = "h2")
    })
    private WebElement formHeadingH2;

    // ===== Text input & textarea (simple @FindBy) =====
    // Known IDs on demo_page
    @FindBy(css = "#myTextInput")
    private WebElement textInput;

    @FindBy(css = "#myTextarea")
    private WebElement textArea;

    // ===== Button and result paragraph =====
    @FindBy(css = "#myButton")
    private WebElement clickMeButton;

    @FindBy(css = "#pText")
    private WebElement paragraphText;

    // ===== Select Dropdown with fallbacks (OR) via @FindAll =====
    @FindAll({
        @FindBy(id = "myDropdown"),
        @FindBy(css = "select#mySelect"),
        @FindBy(xpath = "//label[contains(.,'Select Dropdown')]/following::select[1]")
    })
    private WebElement selectDropdown;

    // ===== Slider (input[type=range]) with OR fallbacks =====
    @FindAll({
        @FindBy(id = "mySlider"),
        @FindBy(css = "input[type='range']"),
        @FindBy(xpath = "//label[contains(.,'Input Slider')]/following::input[@type='range'][1]")
    })
    private WebElement slider;

    // ===== Progress and Meter =====
    @FindAll({
        @FindBy(css = "progress#progressBar"),
        @FindBy(xpath = "//label[contains(.,'Progress Bar')]/following::progress[1]")
    })
    private WebElement progressBar;

    @FindAll({
        @FindBy(css = "meter#meter"),
        @FindBy(xpath = "//label[contains(.,'HTML Meter')]/following::meter[1]")
    })
    private WebElement htmlMeter;

    // ===== SVG rect =====
    @FindAll({
        @FindBy(css = "#svgRect"),
        @FindBy(xpath = "//*[name()='svg']//*[name()='rect']")
    })
    private WebElement svgRect;

    // ===== Radio buttons =====
    @FindAll({
        @FindBy(id = "radioButton1"),
        @FindBy(xpath = "(//input[@type='radio'])[1]")
    })
    private WebElement radio1;

    @FindAll({
        @FindBy(id = "radioButton2"),
        @FindBy(xpath = "(//input[@type='radio'])[2]")
    })
    private WebElement radio2;

    // ===== Checkboxes =====
    @FindAll({
        @FindBy(id = "checkBox1"),
        @FindBy(xpath = "(//input[@type='checkbox'])[1]")
    })
    private WebElement checkBox1;

    @FindAll({
        @FindBy(id = "checkBox2"),
        @FindBy(xpath = "(//input[@type='checkbox'])[2]")
    })
    private WebElement checkBox2;

    @FindAll({
        @FindBy(id = "preCheckBox"),
        @FindBy(xpath = "(//input[@type='checkbox'])[3]")
    })
    private WebElement preCheckedBox;

    // ===== Checkbox in iFrame =====
    @FindAll({
        @FindBy(css = "iframe#iframeCheckbox"),
        @FindBy(xpath = "//iframe[contains(@title,'iFrame') or contains(.,'iframe')]")
    })
    private WebElement checkboxIframe;

    @FindAll({
        @FindBy(id = "iframeCheckBox"),
        @FindBy(xpath = "//input[@type='checkbox']")
    })
    private WebElement iframeCheckbox;

    // ===== Drag and Drop =====
    @FindAll({
        @FindBy(id = "drop1"),
        @FindBy(xpath = "//*[contains(.,'Drag and Drop A')][last()]")
    })
    private WebElement dragA;

    @FindAll({
        @FindBy(id = "drop2"),
        @FindBy(xpath = "//*[contains(.,'Drag and Drop B')][last()]")
    })
    private WebElement dropB;

    // ===== Links =====
    @FindBy(partialLinkText = "seleniumbase.com")
    private WebElement seleniumbaseCom;

    @FindBy(partialLinkText = "SeleniumBase on GitHub")
    private WebElement seleniumbaseGithub;

    @FindBy(partialLinkText = "seleniumbase.io")
    private WebElement seleniumbaseDocs;

    // --- Actions ---
    public boolean isFormHeadingVisible() { return isDisplayed(formHeadingH2); }

    public void enterText(String text) { type(textInput, text); }

    public void enterNotes(String notes) { type(textArea, notes); }

    public void clickGreenButton() { click(clickMeButton); }

    public String paragraphColorText() { return getText(paragraphText); }

    public void selectPercent(String pctLabel) { selectByVisibleText(selectDropdown, pctLabel); }

    public void setSlider(int value) { setRangeInput(slider, value); }

    public String progressValue() {
        String val = getAttribute(progressBar, "value");
        if (val == null || val.isBlank()) {
            // some browsers expose innerText like "(50%)"
            try { return progressBar.getText(); } catch (Exception e) { return ""; }
        }
        return val;
    }

    public String meterValue() {
        String val = getAttribute(htmlMeter, "value");
        if (val == null || val.isBlank()) { return ""; }
        return val;
    }

    public void selectRadio(int idx) {
        if (idx == 1) click(radio1); else click(radio2);
    }

    public boolean isRadioSelected(int idx) {
        return (idx == 1 ? radio1 : radio2).isSelected();
    }

    public void setCheckBoxes(boolean c1, boolean c2, boolean pre) {
        setCheckbox(checkBox1, c1);
        setCheckbox(checkBox2, c2);
        setCheckbox(preCheckedBox, pre);
    }

    private void setCheckbox(WebElement el, boolean desired) {
        if (el == null) return;
        if (desired != el.isSelected()) click(el);
    }

    public void checkIframeBox() {
        driver.switchTo().frame(checkboxIframe);
        if (!iframeCheckbox.isSelected()) iframeCheckbox.click();
        driver.switchTo().defaultContent();
    }

    public void performDragAndDrop() { dragAndDrop(dragA, dropB); }

    public boolean isSvgRectVisible() { return isDisplayed(svgRect); }

    public boolean areCoreLinksVisible() {
        return isDisplayed(seleniumbaseCom) && isDisplayed(seleniumbaseGithub) && isDisplayed(seleniumbaseDocs);
    }
}
