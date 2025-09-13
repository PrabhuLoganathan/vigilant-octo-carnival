package com.demo.practise;



import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class WebTableHelper {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final By tableLocator;

    public WebTableHelper(WebDriver driver, By tableLocator, long timeoutSec) {
        this.driver = driver;
        this.tableLocator = tableLocator;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSec));
    }

    private WebElement table() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(tableLocator));
    }

    /** Returns header names in order (th text, normalized). */
    public List<String> getHeaders() {
        List<String> headers = new ArrayList<>();
        for (WebElement th : table().findElements(By.xpath(".//thead//th"))) {
            headers.add(th.getText().trim());
        }
        return headers;
    }

    /** Returns 1-based column index for a given header (case-insensitive match). */
    public int getColumnIndex(String headerName) {
        String xpath = String.format(
                ".//thead//th[normalize-space(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'))='%s']",
                headerName.trim().toLowerCase()
        );
        WebElement th = table().findElement(By.xpath(xpath));
        // index = preceding th count + 1
        String indexXpath = "count(preceding-sibling::th) + 1";
        Long idx = (Long) ((JavascriptExecutor) driver)
                .executeScript("return " + indexXpath + ";", th);
        return idx.intValue();
    }

    /** Number of data rows (tbody tr). */
    public int getRowCount() {
        return table().findElements(By.xpath(".//tbody/tr")).size();
    }

    /** Get cell text by (rowIndex 1-based, header name). */
    public String getCellText(int rowIndex1Based, String headerName) {
        int colIdx = getColumnIndex(headerName);
        String cellXpath = String.format(".//tbody/tr[%d]/td[%d]", rowIndex1Based, colIdx);
        return table().findElement(By.xpath(cellXpath)).getText().trim();
    }

    /** Find the 1-based row index where the cell under header == value (exact, normalized). */
    public int findRowIndex(String headerName, String cellExactValue) {
        int colIdx = getColumnIndex(headerName);
        String rowXpath = String.format(
                ".//tbody/tr[normalize-space(td[%d])=%s]",
                colIdx, toXPathLiteral(cellExactValue.trim())
        );
        List<WebElement> rows = table().findElements(By.xpath(rowXpath));
        if (rows.isEmpty()) return -1;

        // compute index: preceding-sibling count + 1 on the first match
        WebElement row = rows.get(0);
        String indexJs = "return (arguments[0].previousElementSibling ? arguments[0].previousElementSibling.parentElement.querySelectorAll('tr').length : 0);";
        // The above can be brittle if <thead> exists; safer to count via XPath:
        Long index = (Long) ((JavascriptExecutor) driver)
                .executeScript("return document.evaluate('count(preceding-sibling::tr)+1', arguments[0], null, XPathResult.NUMBER_TYPE, null).numberValue;", row);
        return index.intValue();
    }

    /** Click a button/link within the matched row. Provide a locator relative to the row (dynamic XPath). */
    public void clickInRow(String headerName, String cellExactValue, String relativeActionXPath) {
        int colIdx = getColumnIndex(headerName);
        String rowXpath = String.format(
                ".//tbody/tr[normalize-space(td[%d])=%s]%s",
                colIdx, toXPathLiteral(cellExactValue.trim()), relativeActionXPath.startsWith("/") ? relativeActionXPath : "//" + relativeActionXPath
        );
        WebElement actionEl = wait.until(ExpectedConditions.elementToBeClickable(table().findElement(By.xpath(rowXpath))));
        actionEl.click();
    }

    /** Edit field in the matched row (example: click edit icon, type into an input in that same row, save). */
    public void setInputInRow(String matchHeader, String matchValue, String inputHeader, String newValue) {
        int matchCol = getColumnIndex(matchHeader);
        int inputCol = getColumnIndex(inputHeader);

        // Row that matches
        String baseRow = String.format(".//tbody/tr[normalize-space(td[%d])=%s]", matchCol, toXPathLiteral(matchValue.trim()));

        // Example: click an "Edit" within the row if present
        List<WebElement> edits = table().findElements(By.xpath(baseRow + "//button[normalize-space()='Edit' or contains(@class,'edit') or .//i[contains(@class,'edit')]]"));
        if (!edits.isEmpty()) {
            wait.until(ExpectedConditions.elementToBeClickable(edits.get(0))).click();
        }

        // Type into the input located in the cell under input header
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath(baseRow + String.format("/td[%d]//input|%s/td[%d]//textarea", inputCol, baseRow, inputCol))
        ));
        input.clear();
        input.sendKeys(newValue);

        // Example: click "Save" in the same row (adjust as per actual DOM)
        List<WebElement> saves = table().findElements(By.xpath(baseRow + "//button[normalize-space()='Save' or contains(@class,'save') or .//i[contains(@class,'save')]]"));
        if (!saves.isEmpty()) {
            saves.get(0).click();
        }
    }

    /** Safe XPath literal for values containing quotes. */
    private static String toXPathLiteral(String s) {
        if (!s.contains("'")) return "'" + s + "'";
        if (!s.contains("\"")) return "\"" + s + "\"";
        // contains both ' and "
        String[] parts = s.split("'");
        StringBuilder sb = new StringBuilder("concat(");
        for (int i = 0; i < parts.length; i++) {
            if (i > 0) sb.append(", \"'\", ");
            sb.append("'").append(parts[i]).append("'");
        }
        sb.append(")");
        return sb.toString();
    }
}
