# Dream Portal Automation

Selenium-based functional test automation framework developed for the Wingify Dream Portal assignment.

## Project Overview

This project automates functional testing of the Dream Portal application:

https://arjitnigam.github.io/myDreams/

The automation framework validates:

- Home page loading behavior
- Loading message visibility and disappearance
- My Dreams navigation
- Dreams Diary data
- Dream type validation
- Recurring dreams
- Dreams summary totals
- Multiple browser tabs/windows
- AI-based dream classification

The framework is built using Selenium WebDriver with Java and TestNG and follows the Page Object Model (POM) design pattern.

---

## Tech Stack

- Java 17
- Selenium WebDriver 4.35.0
- TestNG 7.11.0
- Maven
- WebDriverManager 6.3.2
- Google Gemini API
- Page Object Model (POM)
- TestNG HTML Reports

---

## Project Structure

```text
dream-portal-automation
│
├── src
│   └── test
│       ├── java
│       │   ├── pages
│       │   │   ├── HomePage.java
│       │   │   ├── DreamsDiaryPage.java
│       │   │   └── DreamsTotalPage.java
│       │   │
│       │   ├── tests
│       │   │   ├── HomePageTest.java
│       │   │   ├── DreamsDiaryTest.java
│       │   │   ├── DreamsTotalTest.java
│       │   │   └── DreamAIClassificationTest.java
│       │   │
│       │   └── utils
│       │       ├── DriverFactory.java
│       │       ├── ScreenshotUtil.java
│       │       └── DreamAIClassifier.java
│       │
│       └── resources
│           └── testng.xml
│
├── screenshots
├── .gitignore
├── pom.xml
└── README.md
```

---

## Test Coverage

### 1. Home Page

The home page tests verify:

- Loading message is displayed initially.
- Loading message disappears after the page finishes loading.
- "My Dreams" button is visible.
- Clicking "My Dreams" opens the required pages.
- Dreams Diary opens in a new tab/window.
- Dreams Total opens in a new tab/window.
- Expected URLs are verified.

### 2. Dreams Diary

The Dreams Diary tests verify:

- Exactly 10 dream entries are displayed.
- Every row contains exactly 3 columns.
- Dream Name is not empty.
- Days Ago is not empty.
- Dream Type is either `Good` or `Bad`.
- Recurring dreams are identified from the diary data.
- Exactly 2 recurring dream names exist.
- `Flying over mountains` is recurring.
- `Lost in maze` is recurring.

### 3. Dreams Total

The Dreams Total page is validated against the expected summary:

| Metric | Expected Value |
|---|---:|
| Good | 6 |
| Bad | 4 |
| Total | 10 |
| Recurring | 2 |

---

## Test Results

### Core Automation

Latest complete Maven execution:

```text
Tests run: 6
Failures: 0
Errors: 0
Skipped: 0

BUILD SUCCESS
```

### AI Bonus Automation

Latest AI execution:

```text
Tests run: 1
Failures: 0
Errors: 0
Skipped: 0

BUILD SUCCESS
```

### Recurring Dreams Identified

- `Flying over mountains` → 3 occurrences
- `Lost in maze` → 2 occurrences

---

## Test Cases

| Test Class | Test | Description |
|---|---|---|
| HomePageTest | Home Page Loading | Verifies loading message and page content |
| HomePageTest | Loading Message | Verifies loading message disappears |
| HomePageTest | My Dreams Navigation | Verifies required pages open in new tabs |
| DreamsDiaryTest | Dream Entries | Verifies 10 valid dream entries |
| DreamsDiaryTest | Recurring Dreams | Verifies recurring dream logic |
| DreamsTotalTest | Dream Totals | Verifies Good, Bad, Total and Recurring counts |
| DreamAIClassificationTest | AI Classification | Classifies unique dream names using Gemini and compares predictions with actual types |

---

## Running the Tests

### Prerequisites

Install the following:

- Java 17 or later
- Maven
- Google Chrome

### Run the Complete Core Test Suite

Open a terminal in the project root and run:

```bash
mvn clean test
```

Maven will execute the TestNG suite configured in:

```text
src/test/resources/testng.xml
```

### Run the AI Bonus Test

The AI test is executed separately because it requires a Gemini API key:

```bash
mvn test "-Dtest=DreamAIClassificationTest"
```

---

## Gemini API Configuration

The Gemini API key is stored as an environment variable and is not included in the source code.

Set the API key in PowerShell:

```powershell
$env:GEMINI_API_KEY = Read-Host "Enter your Gemini API key"
```

Verify that the variable is available:

```powershell
if ($env:GEMINI_API_KEY) {
    "Gemini API key is set"
} else {
    "Gemini API key is NOT set"
}
```

**Important:** The API key must never be committed to GitHub.

---

## AI-Based Dream Classification

An AI-based dream classification test is included using the Google Gemini API.

The AI classifies dream names as:

- `Good`
- `Bad`

The AI predictions are then compared against the actual dream types displayed in the Dreams Diary.

### AI Implementation

The Dreams Diary contains repeated dream names.

Instead of making a separate API request for every diary row, the automation:

1. Extracts the dream names from the diary.
2. Removes duplicate dream names.
3. Sends the unique dream names to Gemini in a single API request.
4. Stores the AI classifications.
5. Compares the AI predictions against all 10 diary entries.

This avoids sending repeated requests for the same dream.

### AI Test Result

The AI successfully classified all 7 unique dream names and matched the classifications displayed in the website.

| Dream Name | Actual Type | AI Prediction |
|---|---|---|
| Flying over mountains | Good | Good |
| Lost in maze | Bad | Bad |
| Winning lottery | Good | Good |
| Being late to exam | Bad | Bad |
| Swimming with dolphins | Good | Good |
| Time travel adventure | Good | Good |
| Monster chase | Bad | Bad |

---

## TestNG Suite

The project uses a dedicated TestNG suite:

```text
src/test/resources/testng.xml
```

The core suite includes:

- `HomePageTest`
- `DreamsDiaryTest`
- `DreamsTotalTest`

The AI bonus test is executed separately using:

```text
DreamAIClassificationTest
```

This keeps the core UI automation independent of the external Gemini API.

---

## Page Object Model

The project follows the Page Object Model design pattern.

### Pages

**HomePage.java**

Contains locators and actions related to the Dream Portal home page.

**DreamsDiaryPage.java**

Contains functionality for accessing and reading dream diary entries.

**DreamsTotalPage.java**

Contains functionality for reading the dream summary values.

### Utilities

**DriverFactory.java**

Responsible for creating the Selenium WebDriver instance.

**ScreenshotUtil.java**

Captures screenshots when a test fails.

**DreamAIClassifier.java**

Handles communication with the Google Gemini API and parses AI dream classifications.

---

## Reporting

TestNG HTML reports are generated automatically after test execution.

Reports are available under:

```text
target/surefire-reports/
```

The generated reports include:

- Test execution summary
- Passed tests
- Failed tests
- Skipped tests
- Execution details
- TestNG suite information

---

## Failure Handling

The framework captures screenshots automatically when a test fails.

Screenshots are stored under:

```text
screenshots/
```

This helps with debugging failed UI tests.

---

## Logging

The automation framework logs important execution details such as:

- Number of dream entries
- Dream names
- Days ago values
- Dream types
- Recurring dream occurrences
- Summary counts
- Number of browser tabs/windows
- Opened URLs
- AI predictions

Example:

```text
Number of dream rows: 10

Dream: Flying over mountains | Days Ago: 1 day ago | Type: Good

Recurring dream: Flying over mountains (3 times)

Good: 6
Bad: 4
Total: 10
Recurring: 2
```

AI example:

```text
Dream: Flying over mountains | Actual: Good | AI: Good
Dream: Lost in maze | Actual: Bad | AI: Bad
Dream: Winning lottery | Actual: Good | AI: Good
```

---

## Design Principles

The project focuses on:

- Maintainable test structure
- Page Object Model
- Reusable WebDriver setup
- Explicit waits instead of unnecessary fixed delays
- Meaningful assertions
- Failure screenshots
- Test execution logging
- Maven-based execution
- TestNG suite management
- Secure handling of API credentials
- Efficient API usage through duplicate removal

---

## Assignment Validation Summary

| Requirement | Status |
|---|---|
| Home page validation | PASS |
| Loading animation validation | PASS |
| My Dreams navigation | PASS |
| Multiple tabs/windows | PASS |
| 10 diary entries | PASS |
| Good/Bad validation | PASS |
| Recurring dream validation | PASS |
| Summary counts | PASS |
| Page Object Model | PASS |
| TestNG | PASS |
| Failure screenshots | PASS |
| HTML reporting | PASS |
| AI classification bonus | PASS |
| API key security | PASS |

---

## Notes

- The Gemini API key is intentionally excluded from the repository.
- The core UI automation does not depend on the Gemini API.
- The AI bonus test requires a valid `GEMINI_API_KEY` environment variable.
- The AI test classifies unique dream names once and compares the results against all diary entries.

---

## Author

**Harsha Vardhan**