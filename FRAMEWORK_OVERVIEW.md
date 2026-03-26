# 📦 PulsePoint QA Automation Framework

## 📖 Overview
This is a Java-based QA Automation Framework for testing three PulsePoint SaaS advertising/healthcare platforms: LIFE, HCP365 (Signal), and Studio.

---

## 🏗️ Codebase Structure

```text
qa-automation/
├── pom.xml                          # Maven build + dependency management
├── README.md                        # Project documentation
└── src/
    ├── main/java/
    │   ├── api/                     # API request/response handling
    │   ├── factory/                 # DriverFactory – browser lifecycle (Playwright)
    │   ├── pages/                   # Page Object Model classes (37 classes)
    │   │   ├── admin/
    │   │   ├── hcp/                 # HCP365 pages
    │   │   ├── life/                # LIFE platform pages
    │   │   ├── studio/              # Studio platform pages
    │   │   └── Navigation.java      # Login & cross-app navigation
    │   └── utils/                   # Helper/utility modules
    └── test/
        ├── java/
        │   ├── hooks/               # Cucumber lifecycle hooks (@Before/@After)
        │   ├── stepdefinitions/     # Gherkin step implementations
        │   └── testrunner/          # TestRunner + FailedTestRunner (JUnit runners)
        └── resources/
            ├── config/
            │   └── config.properties  # URLs, credentials, browser settings
            ├── features/              # Gherkin .feature files (35 total)
            │   ├── api/               # API tests (NPI)
            │   ├── e2e/               # Cross-app end-to-end workflows (11 features)
            │   ├── hcp/               # HCP platform tests
            │   ├── life/              # LIFE platform tests (19 features)
            │   └── studio/            # Studio platform tests
            └── logback.xml            # Logging configuration
```

---

## 🔧 Key Technologies

| Technology | Version | Role |
| :--- | :--- | :--- |
| Java | JDK 21 | Primary language |
| Maven | — | Build tool & dependency management |
| Cucumber | 7.20.1 | BDD framework (Gherkin feature files) |
| Playwright | 1.50.0 | Browser automation (replaces Selenium) |
| JUnit | — | Test runner integration |
| Apache POI | 5.4.0 | Excel/CSV file handling |
| OpenCSV | 5.9 | CSV parsing |
| SQL Server JDBC | 12.8.1 | Database validation queries |
| SLF4J + Logback | 2.0.9 / 1.5.27 | Logging |

---

## 🎯 What's Being Tested
Three PulsePoint applications:
* LIFE – Ad campaign management platform (campaigns, line items, tactics, creatives, pixels, reports, NPI lists, domains, inventory)
* HCP365 / Signal – Healthcare professional targeting (smart actions, audiences, webhooks, physician lists)
* Studio – Data exploration workspace (Explorer/Expansion workspaces, NPI publishing, permission management)

---

## 📐 Architecture & Patterns

### 1. Page Object Model (POM)
Each application screen has a dedicated Java class encapsulating locators and actions:

```java
public class Campaigns {
    private final Page page;
    private final Locator CREATE_CAMPAIGN = page.locator("//button[text()='Create a Campaign']");

    public void clickCreateCampaign() { CREATE_CAMPAIGN.click(); }
}
```

### 2. BDD with Cucumber + Gherkin
Tests are written in human-readable Gherkin syntax:

```gherkin
Scenario Outline: Create a Campaign with a Tactic & a Line Item
  When User enters the campaign details as "<ADVERTISER>" "<CP_NAME>" "<CP_TYPE>" and saves
  Examples:
    | ADVERTISER     | CP_NAME | CP_TYPE |
    | 01- Advertiser | Auto    | Regular |
```

### 3. Data-Driven Testing
Extensive use of Scenario Outline + Examples: tables and Gherkin DataTables for parameterized runs across many data combinations.

### 4. Thread-Safe Driver Management
DriverFactory.java uses ThreadLocal<Page>, ThreadLocal<BrowserContext>, and ThreadLocal<Browser> — ready for parallel test execution.

### 5. Encrypted Credentials
Credentials and DB URLs in config.properties are AES-encrypted, decrypted at runtime by EncryptionDecryption.java.

### 6. Playwright Tracing & Screenshots
The @After hook automatically captures screenshots and Playwright trace .zip files on test failure for easy debugging.

---

## 🛠️ Utility Modules

| Utility | Purpose |
| :--- | :--- |
| CommonUtils.java | Timestamps, random strings, JSON reading, element helpers |
| ConfigReader.java | Reads & decrypts config.properties properties |
| EncryptionDecryption.java | AES encrypt/decrypt for credentials |
| WaitUtility.java | Explicit waits (spinner hidden, element visible/hidden) |
| DatabaseActions.java | SQL Server query execution for data validation |
| ExcelActions.java | CSV/Excel file reading for test data |
| FileActions.java | File I/O operations |

---

## ▶️ Running the Tests

```bash
# Run all @e2e scenarios
mvn test -Dtest=TestRunner

# Re-run only failed tests
mvn test -Dtest=FailedTestRunner
```

Or from an IDE (IntelliJ/Eclipse): right-click TestRunner.java → Run.

Test Reports are generated to:
* HTML: target/cucumber-reports/report.html
* Logs: target/logs/test-execution.log
* Playwright Traces: target/trace_*.zip (for failures)

---

## 📊 Scale
35 Feature Files | ~166 Scenarios | 37 Page Object Classes
Test Tags: @e2e (cross-app workflows), @regression (module-specific)
Environments: demo and preRelease (configurable in config.properties)
No CI/CD pipelines present — tests are run manually or via IDE

---

In summary, this is a mature, well-structured BDD automation framework using Cucumber + Playwright on Java 21, following the Page Object Model pattern to test three interconnected PulsePoint advertising platforms across UI, API, E2E, and database layers.
