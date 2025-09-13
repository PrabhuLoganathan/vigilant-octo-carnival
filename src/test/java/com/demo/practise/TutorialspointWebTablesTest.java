package com.demo.practise;


import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.List;

public class TutorialspointWebTablesTest {
    private WebDriver driver;
    private WebTableHelper table;

    // Adjust this locator to the specific table on the page; often first visible table works:
    private static final By TABLE = By.xpath("(//table)[1]");

    @BeforeClass
    public void setup() {
        ChromeOptions opts = new ChromeOptions();
        opts.addArguments("--start-maximized");
        driver = new ChromeDriver(opts);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.get("https://www.tutorialspoint.com/selenium/practice/webtables.php");

        table = new WebTableHelper(driver, TABLE, 10);
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        if (driver != null) driver.quit();
    }

    @Test
    public void readHeadersAndCounts() {
        List<String> headers = table.getHeaders();
        System.out.println("Headers: " + headers);

        int rows = table.getRowCount();
        System.out.println("Row count: " + rows);

        Assert.assertTrue(headers.size() > 0, "No headers found");
        Assert.assertTrue(rows > 0, "No rows found");
    }

    @Test
    public void readCellByHeader() {
        // Example: get the “Email” value from row 1
        String email = table.getCellText(1, "Email");
        System.out.println("Row1 Email: " + email);
        Assert.assertNotNull(email);
    }

    @Test
    public void findRowByValueAndClickAction() {
        // Example: find the row where “First Name” == “Kierra”, then click a row-level “Edit” or “Delete”
        int rowIdx = table.findRowIndex("First Name", "Kierra");
        if (rowIdx == -1) {
            System.out.println("Target row not found — adjust test data as needed.");
            return;
        }

        // Click an action btn/link inside that row:
        // Adjust the relative locator depending on DOM: a button labeled 'Edit', or an icon with class 'edit', etc.
        table.clickInRow("First Name", "Kierra", "button[normalize-space()='Edit']");

        // ...perform edits/assertions as needed
    }

    @Test
    public void inlineEditInRowExample() {
        // Change the “Age” to 35 for the row where Email == some value
        String matchHeader = "Email";
        String matchValue  = "kierra@example.com"; // adjust to real value visible in the table
        String inputHeader = "Age";
        String newValue    = "35";

        table.setInputInRow(matchHeader, matchValue, inputHeader, newValue);

        // Optional: verify (re-read the cell or check success indicator)
        int row = table.findRowIndex(matchHeader, matchValue);
        if (row != -1) {
            String after = table.getCellText(row, inputHeader);
            Assert.assertEquals(after, newValue, "Age not updated");
        }
    }
}
