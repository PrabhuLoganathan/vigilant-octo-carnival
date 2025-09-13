# Selenium + Cucumber (TestNG) + PageFactory Demo

This mini framework automates actions on **https://seleniumbase.io/demo_page** using
PageFactory locators with `@FindBy`, `@FindBys` (AND), and `@FindAll` (OR).

## Tech
- Java 17, Maven
- Selenium 4, Cucumber 7 (with TestNG), WebDriverManager

## Project layout
```
src
 └─ test
    ├─ java
    │  ├─ com.demo.config      # DriverFactory + Hooks
    │  ├─ com.demo.pages       # Page Objects (PageFactory)
    │  ├─ com.demo.steps       # Step definitions
    │  └─ com.demo.runners     # TestNG Cucumber runner
    └─ resources
       └─ features             # Feature file(s)
```

## Run
1) Install JDK 17+ and Maven.
2) In project root:
```bash
mvn -q test -DbaseUrl="https://seleniumbase.io/demo_page" -Dcucumber.filter.tags="@demopage"
```
Or with TestNG suite:
```bash
mvn -q -Dtest=CucumberTestRunner test
```
Headless:
```bash
mvn -q test -Dheadless=true
```

> If any locator differs on your environment, update `DemoPage.java`.
> `@FindAll` is used to provide fallbacks (OR). `@FindBys` demonstrates AND chaining.


## Parallel Execution

This project enables **parallel scenario execution** via TestNG DataProvider.

- The Cucumber runner overrides `scenarios()` with `@DataProvider(parallel = true)`.
- Concurrency is controlled by TestNG's *data-provider thread count*.

### Run N threads from CLI
```bash
# 4 parallel scenarios
mvn -q test -Ddataproviderthreadcount=4 -Dcucumber.filter.tags="@demopage"
# headless + 6 threads
mvn -q test -Dheadless=true -Ddataproviderthreadcount=6
```

### Or via `testng.xml`
The suite sets `data-provider-thread-count="4"`. Adjust as needed:
```xml
<suite name="Cucumber Suite" data-provider-thread-count="8">
```

> The framework uses `ThreadLocal<WebDriver>` so each scenario runs with an isolated browser instance. `@Before`/`@After` hooks create/quit a driver per scenario.
