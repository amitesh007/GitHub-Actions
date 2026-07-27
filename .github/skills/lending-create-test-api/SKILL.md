---
name: lending-create-test-api
description: 'Generic consolidated skill for generating JUnit5 integration test classes for any LoanIQ Create API. Driven by a Business Object name and a Requirement Spreadsheet path. Covers ALL attributes from the spreadsheet Create sheet with 100% test coverage using real DB round-trips.'
---

# Generic Create Integration Test — Consolidated Skill

> **Purpose**: Generate a complete `LiqAPICreate{BusinessObject}IntegrationTest extends BaseTestLoanIQ` class that achieves 100% test coverage for any LoanIQ REST API Create operation, exclusively using real DB-backed integration patterns.

### Constraint Categories (in priority order)

**Primary — Test Constraints** (applies to generated Java test code only — these activate ONLY after all prerequisites pass):
- Integration tests ONLY — NO Mockito, NO mocks, NO stubs, NO faked responses
- Every test exercises a real DB round-trip via `invokeApiInterface()` or `callBasicExecute()`
- Halting on missing prerequisites (e.g., no Create sheet) is NOT a conflict — tests are generated only when all inputs are valid

**Secondary — Spreadsheet Processing Constraints** (applies to how the agent reads the .xlsx input — unrelated to test methodology):
- Use the provided PowerShell scripts to extract and parse spreadsheet contents
- The scripts handle .xlsx internal XML parsing and column layout detection automatically
- If the primary parser fails (zero attributes, format mismatch, or script error), automatically invoke the fallback scripts in sequence before asking the user to intervene
- Fallback order: `parse-create-attributes.ps1` → `parse-create-attributes-fallback.ps1` → `parse-via-excel-com.ps1`
- Only ask the user to manually provide attribute data if ALL fallback scripts fail

**Secondary — Codebase Constraints** (applies to class/method references in generated code):
- All referenced classes, methods, and enum constants MUST be verified to exist before use

**Secondary — Coverage Constraints** (applies to test completeness):
- Every spreadsheet attribute must have at least one test case
- **MANDATORY:** For every "ATTRIBUTE_FIELD_NAME" column, generate an additional test case based on the information in "ATTRIBUTE_DESCRIPTION" column
- Mandatory fields get both positive and negative tests
- Optional fields get positive tests plus boundary/validation tests where applicable
- Description-based tests must validate business rules, constraints, or scenarios mentioned in the ATTRIBUTE_DESCRIPTION

> **Note on halting**: If the Create sheet is missing from the spreadsheet, execution stops and the user is prompted. This is a prerequisite check — it does not conflict with test methodology constraints. Tests are only generated once all prerequisites are met.

---

## When to Use

Use `lending-create-test-api` when:
- Generating **Create integration test classes** (JUnit5) for any LoanIQ business object
- The entity has a Create operation defined in the requirement spreadsheet
- You need 100% test coverage for all attributes in the Create sheet
- You need real DB-backed integration tests (no mocks)

DO NOT use this skill for:
- Generating the Create API implementation class itself (use `lending-create-api`)
- Update test classes (use `lending-update-test-api`)
- Query/Get test classes (use `lending-query-test-api`)
- Delete test classes (use `lending-delete-test-api`)

---

## How to Run

Invoke via VS Code Copilot Chat with the skill prefix:

```text
#lending-create-test-api Business Object is '{BusinessObject}'. Generate the JUnit5 Create Integration Test Class using the requirement spreadsheet at path: '{SpreadsheetPath}'.
```

Or invoked automatically by the `lending-api-developer` agent during Step 5.

---

## Output Format

The skill produces a JUnit5 test class at:

```text
FLIQ-liqjava/LoanIQ/test/com/misys/liq/api/rest/executable/{domain}/LiqAPICreate{BusinessObject}IntegrationTest.java
```

The test class:
- Extends `BaseTestLoanIQ`
- Uses `@TestMethodOrder(OrderAnnotation.class)`
- Contains integration tests with `@Order` annotations
- Achieves 100% attribute coverage from the spreadsheet

---

## User Prompt Template

To invoke this skill, you **must** prefix your prompt with `#lending-create-test-api` in VS Code Copilot Chat. This tells Copilot to use this skill's instructions and patterns.

Copy and paste the prompt below into the chat, replacing the placeholders with your actual values:

---

### Prompt (copy this):

```
#lending-create-test-api Business Object is '{BusinessObject}'. Generate the JUnit5 Create Integration Test Class using the requirement spreadsheet at path: '{SpreadsheetPath}'.
```

### Full Prompt (with explicit requirements — optional, for more control):

```
#lending-create-test-api Business Object is '{BusinessObject}'. Generate the JUnit5 Create Integration Test Class
using the requirement spreadsheet at path: '{SpreadsheetPath}'.

Requirements:
- Test class name: LiqAPICreate{BusinessObject}IntegrationTest
- Must extend BaseTestLoanIQ
- Integration tests ONLY (NO Mockito, NO mocks, NO stubs)
- Parse the "Create" sheet from the spreadsheet for all attributes
- Generate negative tests for every mandatory field (null/missing → failure)
- Generate positive tests for every attribute (mandatory + optional)
- Generate invalid value tests for fields with code table or validation constraints
- Generate default value tests where defaults are documented
- Generate boundary/edge case tests for numeric and date fields
- Include idempotency key missing test
- Include class-mapping coverage tests (nonPrimitiveFieldMappings, primitiveFieldMappings, securityAccessSymbol)
- Include response assertion tests verifying the return value object
- Achieve 100% test coverage across all spreadsheet attributes
- Ensure ZERO compilation errors — verify all classes, methods, and enum constants exist in the codebase before referencing them

Execution order (sequential stages — complete each before starting the next):
Stage 1 — Validation: Validate inputs (Business Object + Spreadsheet Path) — halt if missing
Stage 2 — Data Extraction: Extract spreadsheet → find Create sheet → parse attributes (with automatic fallback if format is non-standard) — halt only if ALL parsers fail
Stage 3 — Discovery: Discover codebase classes, enums, and security symbols
Stage 4 — Template: Read JSON request payload template — generate default from spreadsheet if missing
Stage 5 — Generation: Generate test class with all phases
Stage 6 — Verification: Verify coverage checklist
```

### Example Prompts:

**UpfrontFee:**
```
#lending-create-test-api Business Object is 'UpfrontFee'. Generate the JUnit5 Create Integration Test Class using the requirement spreadsheet at path: 'C:\Auto\API\Upfront Fee v2.1.xlsx'.
```

**Deal:**
```
#lending-create-test-api Business Object is 'Deal'. Generate the JUnit5 Create Integration Test Class using the requirement spreadsheet at path: 'C:\Auto\API\Deal REST API_V1.xlsx'.
```

**Facility:**
```
#lending-create-test-api Business Object is 'Facility'. Generate the JUnit5 Create Integration Test Class using the requirement spreadsheet at path: 'C:\Auto\API\Facility v9.xlsx'.
```

**LoanDrawdown:**
```
#lending-create-test-api Business Object is 'LoanDrawdown'. Generate the JUnit5 Create Integration Test Class using the requirement spreadsheet at path: 'C:\Auto\API\Loan InitialDrawndown v1.17.xlsx'.
```

**LoanRepricing:**
```
#lending-create-test-api Business Object is 'LoanRepricing'. Generate the JUnit5 Create Integration Test Class using the requirement spreadsheet at path: 'C:\Auto\API\Comprehensive Repricing V1.xlsx'.
```

**LoanInterestPayment:**
```
#lending-create-test-api Business Object is 'LoanInterestPayment'. Generate the JUnit5 Create Integration Test Class using the requirement spreadsheet at path: 'C:\Auto\API\Interest Payment API v1.7.xlsx'.
```

**LoanPrincipalPayment:**
```
#lending-create-test-api Business Object is 'LoanPrincipalPayment'. Generate the JUnit5 Create Integration Test Class using the requirement spreadsheet at path: 'C:\Auto\API\Principal Payment API v1.8.xlsx'.
```

**Primary (Circle):**
```
#lending-create-test-api Business Object is 'Primary'. Generate the JUnit5 Create Integration Test Class using the requirement spreadsheet at path: 'C:\Auto\API\Circle API - V1.xlsx'.
```

**ProductGuarantee:**
```
#lending-create-test-api Business Object is 'ProductGuarantee'. Generate the JUnit5 Create Integration Test Class using the requirement spreadsheet at path: 'C:\Auto\API\Facility v9.xlsx'.
```

**UserProfile:**
```
#lending-create-test-api Business Object is 'UserProfile'. Generate the JUnit5 Create Integration Test Class using the requirement spreadsheet at path: 'C:\Auto\API\User Profile V2.xlsx'.
```

---

## Required Inputs (MUST be present in the prompt)

Before generating any test class, **verify** that the user's prompt contains BOTH of these:

| # | Input | Description | Example |
|---|---|---|---|
| 1 | **Business Object** | The Pascal-case name of the entity | `UpfrontFee`, `Deal`, `Facility`, `LoanDrawdown`, `LoanRepricing`, `LoanInterestPayment`, `LoanPrincipalPayment`, `Primary`, `ProductGuarantee` |
| 2 | **Requirement Spreadsheet Path** | Full path to the `.xlsx` file containing the API specification | `C:\Auto\API\Upfront Fee v2.1.xlsx` |

### If either input is missing — ASK the user:

```
I need two pieces of information to generate the Create test class:

1. **Business Object Name** — The Pascal-case name of the entity (e.g., `UpfrontFee`, `Deal`, `Facility`, `LoanDrawdown`).
2. **Requirement Spreadsheet Path** — The full file path to the Excel (.xlsx) file containing the API specification (e.g., `C:\Auto\API\Upfront Fee v2.1.xlsx`).

Please provide both.
```

**Wait for both inputs** before continuing to the next stage.

---

## Spreadsheet Processing Rules

> **NOTE**: The scripts below handle .xlsx internal XML extraction and column detection. This constraint is about how the agent reads spreadsheet data — it is unrelated to test methodology constraints (no mocks, etc.).

### Script 1: Extract the Spreadsheet

Run `extract-spreadsheet.ps1` to convert the `.xlsx` to parseable XML:

```powershell
.\scripts\extract-spreadsheet.ps1 -SpreadsheetPath "{SpreadsheetPath}"
```

This produces an extracted folder (e.g., `C:\Auto\API\{BusinessObject}_extracted`) containing `xl/workbook.xml`, `xl/sharedStrings.xml`, and `xl/worksheets/*.xml`.

### Script 2: Find the Create Sheet

Run `find-create-sheet.ps1` to locate the exact worksheet XML file for the "Create" sheet:

```powershell
.\scripts\find-create-sheet.ps1 -ExtractedPath "{ExtractedPath}"
```

This returns the sheet filename (e.g., `sheet4.xml`). If no "Create" sheet exists, stop execution and ask the user for a valid spreadsheet. Wait for user response before continuing.

### Script 3: Parse All Create Attributes

Run `parse-create-attributes.ps1` to extract all attribute rows with auto-detected column layout:

```powershell
.\scripts\parse-create-attributes.ps1 -ExtractedPath "{ExtractedPath}" -SheetFile "{SheetFile}"
```

This returns structured attribute data: Category, FieldName, DataType, Required, Description, DefaultValue. The script auto-detects column layout (Layout A or B) from header rows.

### Script 3a (Fallback): Parse Attributes with Dynamic Header Detection

If Script 3 fails (exits with error, returns 0 attributes, or reports "Could not auto-detect layout"), run the fallback parser:

```powershell
.\scripts\parse-create-attributes-fallback.ps1 -ExtractedPath "{ExtractedPath}" -SheetFile "{SheetFile}"
```

This fallback script:
- Scans ALL cells in the first 30 rows looking for header keywords ("Field Name", "Attribute", "Data Type", "Required", etc.)
- Maps columns dynamically based on header content rather than fixed positions
- Handles spreadsheets with non-standard column orderings, extra columns, or different header naming
- Normalizes Required values ("Yes"/"Mandatory"/"M" → "Y", "No"/"Optional" → "N", etc.)
- Falls back to positional heuristics if keyword matching partially succeeds

### Script 3b (Last Resort): Parse via Excel COM Automation

If Script 3a also fails (e.g., the .xlsx has non-standard XML internals, password-protected sheets, or heavily merged cells), use the COM-based parser:

```powershell
.\scripts\parse-via-excel-com.ps1 -SpreadsheetPath "{SpreadsheetPath}"
```

This script:
- Opens the .xlsx directly using Excel COM automation (requires Excel installed)
- Reads cell values including those in merged ranges
- Uses the same dynamic header detection as the fallback parser
- Reports a clear error if Excel is not installed, suggesting the user provide attribute data manually

### Fallback Decision Logic

```
parse-create-attributes.ps1 (primary)
  ├─ SUCCESS (attributes.Count > 0) → Continue to Stage 3
  └─ FAILURE (exit code != 0, 0 attributes, or layout detection failed)
       │
       ▼
parse-create-attributes-fallback.ps1 (fallback #1)
  ├─ SUCCESS (attributes.Count > 0) → Continue to Stage 3
  └─ FAILURE (header not found, 0 attributes)
       │
       ▼
parse-via-excel-com.ps1 (fallback #2 — last resort)
  ├─ SUCCESS (attributes.Count > 0) → Continue to Stage 3
  └─ FAILURE (Excel not available, or still no attributes)
       │
       ▼
ASK USER: Provide attribute data manually or fix spreadsheet
```

### Attribute Data Interpretation

The parsed output uses these column semantics:
   - `FieldName` — the JSON/Java field name
   - `DataType` — String, Integer, Number, Boolean, Date, Enum, List, Object
   - `Required` — Y (mandatory), N (optional), CR (conditionally required)
   - `Description` — business rules, defaults, constraints, validation info
   - `Category` — grouping (e.g., OwnerIdentifier, FeeDetails, RiskTypes, Department)
   - `DefaultValue` — default value if field is omitted

**CRITICAL REQUIREMENT — Spreadsheet Column Reading:**

For every row in the Create sheet of the requirement spreadsheet:
- Read **ATTRIBUTE_FIELD_NAME** column → Use as the field name for test generation
- Read **ATTRIBUTE_DESCRIPTION** column → Use to generate description-based test cases

**Conditional Processing:** These columns are ONLY processed if:
- The "Create" sheet exists in the requirement spreadsheet
- The operation type "Create" is valid for this business object
- If the Create sheet does not exist, STOP and prompt user for valid spreadsheet

**Test Generation from ATTRIBUTE_DESCRIPTION:**

**CONDITIONAL LOGIC:**

1. **If ATTRIBUTE_DESCRIPTION is BLANK or EMPTY:**
   - Generate ONLY the basic JUnit test method for the attribute
   - Do NOT generate description-based test
   - Move forward to next attribute

2. **If ATTRIBUTE_DESCRIPTION has CONTENT:**
   - Generate the basic JUnit test method for the attribute
   - ADDITIONALLY generate description-based JUnit test method(s) based on what the description mentions:
     - Business rules → Generate a test validating that rule
     - Constraints (min/max, format, pattern) → Generate a boundary/validation test
     - Conditional behavior ("if X then Y") → Generate a conditional logic test
     - Code table values → Generate valid/invalid value tests
     - Relationships to other fields → Generate a relational validation test
     - Special scenarios → Generate a scenario-specific test

**Coverage Rule:** Every attribute must have at least one basic test case. Mandatory attributes get both positive and negative basic tests; optional attributes get positive basic tests plus boundary/validation tests where the description specifies constraints.

---

## Class Name Convention

All class names are derived from the **Business Object** name provided in the prompt.

Given a business object name `{BusinessObject}` (e.g., `UpfrontFee`):

| Role | Naming Pattern | Example (`UpfrontFee`) |
|---|---|---|
| Integration (request) class | `LiqAPICreate{BusinessObject}Integration` | `LiqAPICreateUpfrontFeeIntegration` |
| Test class | `LiqAPICreate{BusinessObject}IntegrationTest` | `LiqAPICreateUpfrontFeeIntegrationTest` |
| Response (return value) class | `LiqAPI{BusinessObject}IntegrationAsReturnValue` | `LiqAPIUpfrontFeeIntegrationAsReturnValue` |
| Identifier class (if applicable) | `LiqAPI{BusinessObject}Identifier` | `LiqAPIUpfrontFeeIdentifier` |
| Java package | `com.misys.liq.api.rest.executable.{domain}` | `com.misys.liq.api.rest.executable.upfrontfee` |
| `GeneralIntegrationMapping` enum prefix | `CREATE_{SCREAMING_SNAKE_CASE}_*` | `CREATE_UPFRONTFEE_TRANSACTION_*` |

**Naming Rules:**
- Use Pascal-case for `{BusinessObject}` **exactly** as provided in the prompt.
- The domain (package segment) is the lowercased, no-separator form of the business object name (e.g., `UpfrontFee` → `upfrontfee`, `LoanDrawdown` → `outstanding.drawdown`).
- For the `GeneralIntegrationMapping` enum prefix, convert the business object name to `SCREAMING_SNAKE_CASE`.
- Annotations `@TestMethodOrder`, `@BeforeEach`, and `extends BaseTestLoanIQ` are always present.

---

## Codebase Discovery Steps (mandatory before code generation)

Before writing the test class, the agent MUST perform codebase discovery to avoid compilation errors.

> **IMPORTANT**: You MUST run the `discover-codebase.ps1` script first to get a comprehensive overview, then supplement with targeted searches as needed.

### Run the Discovery Script

```powershell
.\scripts\discover-codebase.ps1 -BusinessObject "{BusinessObject}" -CodebasePath "C:\Users\asrivas3\git\7740_3\FLIQ-liqjava\LoanIQ"
```

This script automatically searches for:
- Integration (request) class: `LiqAPICreate{BusinessObject}Integration.java`
- Response class: `LiqAPI{BusinessObject}IntegrationAsReturnValue.java` (with getter methods)
- `GeneralIntegrationMapping` enum constants matching the business object
- Security access symbol from `APICommonConstants`
- Superclass: `LiqAPICreate{BusinessObject}.java`
- Existing test class (if already present)

### Post-Script Verification Steps

After running the discovery script, verify and supplement with these targeted searches:

### Step 1: Locate the Integration (Request) Class

If the script didn't find it in the default location, search `LoanIQ/srcgen/` manually. For "Add" operations (e.g., ProductGuarantee), look for `LiqAPIUpdate{BusinessObject}Integration` instead.

### Step 2: Identify the Response Class

Review the getter methods listed by the discovery script (e.g., `getId()`, `getTransactionId()`, `getUpdateTimeStamp()`). These determine your response assertion pattern.

### Step 3: Find GeneralIntegrationMapping Enum Constants

Confirm the enum constants found by the script. These are the available JSON templates for `getMainObjectFromJsonCreate()`.

### Step 4: Verify Security Access Symbol

Confirm the security symbol string from the script output. If not found, search the integration class's `securityAccessSymbol()` method directly.

### Step 5: Identify the Package

Note the actual package path from the source file. Use it for the test class package declaration.

### Step 6: Check for Additional Required Imports

Look at the integration class's fields and `@LiqAPIFieldMapper` annotations to identify:
- Identifier classes (e.g., `LiqAPIOwnerIdentifier`, `LiqAPIDealIdentifier`, `LiqAPIFacilityIdentifier`)
- Nested data classes used in list fields
- Enum types used for identifier types

---

## JSON Request Payload Templates

> **IMPORTANT**: Before generating test methods, you MUST understand the JSON request payload structure for the business object. Use the resources below.

### Template Location

The generic JSON request payload template is available in the skill's `templates/` subfolder:

```
.github/skills/lending-create-test-api/templates/json-request.md
```

This file contains the universal JSON structure used by `getMainObjectFromJsonCreate()`, including:
- Header block (`appId`, `isB2B`)
- Business object name and className
- Primitive fields (`attribute`, `valueType`, `value`)
- List/collection fields (`valueType: "List"`, `valueList`)
- Nested objects with sub-groups

### Sample JSON for a Business Object

The actual sample JSON request payload for a given business object is located at:

```
LoanIQ/test-resources/json/{domain}/Create{BusinessObject}Integration.json
```

For example:
- UserProfile → `LoanIQ/test-resources/json/userprofile/CreateUserProfileIntegration.json`
- Deal → `LoanIQ/test-resources/json/deal/CreateDealIntegration.json`
- UpfrontFee → `LoanIQ/test-resources/json/upfrontfee/CreateUpfrontFeeIntegration.json`

### Generated Sample from IntegrationAPITool

If a new business object's JSON has been recently generated or scaffolded, check the temp output folder:

```
FLIQ-liqjava/IntegrationAPITool/artifacts/temp_generated_class/
```

This folder contains auto-generated Integration classes and may include sample JSON payloads for business objects being newly developed.

### How JSON Relates to Test Methods

Each test method in the Create test class calls:

```java
liqAPIData = getMainObjectFromJsonCreate(
    GeneralIntegrationMapping.{ENUM_CONSTANT}.toString(),
    LiqAPICreate{BusinessObject}Integration.class);
```

This loads the JSON file referenced by the `GeneralIntegrationMapping` enum constant's `getJsonFileName()` method, deserializes it into the integration class, and populates all fields. The agent MUST:

1. **Read the actual JSON file** at `LoanIQ/test-resources/json/{domain}/Create{BusinessObject}Integration.json` to understand which fields are pre-populated. If this JSON file does not exist, generate a default JSON template based on the spreadsheet attributes using the structure from `templates/json-request.md`.
2. **Reference the generic template** in `templates/json-request.md` for structural understanding
3. **Check `IntegrationAPITool/artifacts/temp_generated_class/`** for any newly generated sample payload
4. **Use the field names from the JSON** to match setter method names on the integration class (e.g., JSON `"attribute": "firstName"` → `setFirstName()`)
5. **Identify which fields exist in the JSON but need null/override** for negative test scenarios

---

## Allowed APIs (whitelist)

Only these helpers may appear in generated tests:

| Helper | Purpose |
|---|---|
| `getMainObjectFromJsonCreate(enum, Class)` | Build request DTO from JSON template |
| `LiqApiDataUtil.getObjectFromJson(enum, Class)` | Static variant for loading from JSON |
| `invokeApiInterface(liqAPIData)` | Single-commit DB round trip |
| `LiqApiDataUtil.callBasicValidate(liqAPIData)` | Trigger input validation (optional, some APIs use this) |
| `LiqApiDataUtil.callBasicExecute(liqAPIData)` | Execute and return response (alternative to invokeApiInterface) |
| `LiqApiDataUtil.callSetParents(liqAPIData)` | Set parent linkage if required by the integration class |
| `LiqApiDataUtil.generateIdempotencyKey()` + `setIdempotencyKey(...)` | Mandatory POST header |
| `basicExecuteOutput.getAPIMessages()` / `getSuccess()` / `getResult()` | Response assertions |
| `liqAPIData.securityAccessSymbol()` | Verify security symbol string |
| `LiqAPICreate{BusinessObject}Integration.clazz.nonPrimitiveFieldMappings()` | Non-primitive mapping coverage |
| `LiqAPICreate{BusinessObject}Integration.clazz.primitiveFieldMappings()` | Primitive mapping coverage |
| `DateUtility.getDateAsFormattedString(date, format)` | Date formatting |

**NEVER use:**
- `Mockito.mock(...)`, `@Mock`, `mockStatic`, `spy`, `@InjectMocks`
- `PowerMock`, `byte-buddy`
- Manually instantiated `LiqAPIResponse`
- Stubbed or faked response objects

---

## Required Class Skeleton (template)

```java
package com.misys.liq.api.rest.executable.{domain};

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.misys.liq.BaseTestLoanIQ;
import com.misys.liq.addon.desktopcomm.apicomm.apiexecutor.LiqAPIExceptionMessage;
import com.misys.liq.addon.desktopcomm.apicomm.apiexecutor.LiqAPIResponse;
import com.misys.liq.api.rest.constants.GeneralIntegrationMapping;
import com.misys.liq.api.rest.util.LiqApiDataUtil;
// ── Add additional imports based on codebase discovery ──
import java.util.Properties;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LiqAPICreate{BusinessObject}IntegrationTest extends BaseTestLoanIQ {

    private static final Logger LOG =
        org.apache.logging.log4j.LogManager.getLogger(LiqAPICreate{BusinessObject}IntegrationTest.class);

    private LiqAPICreate{BusinessObject}Integration liqAPIData;
    private LiqAPIResponse basicExecuteOutput;

    @BeforeEach
    public void setProperties() {
        Properties props = System.getProperties();
        props.setProperty("RestServices", "Y");
    }
}
```

**Notes:**
- Replace `{BusinessObject}` with the actual Pascal-case name.
- Replace `{domain}` with the actual package segment discovered in Step 5.
- Add additional instance variables if needed (e.g., separate JSON enum constants for different scenarios).

---

## Test Generation Strategy — Achieving ~100% Coverage

### Phase 1: Mandatory Field Validation Tests (negative cases, ordered)

For **every** attribute marked `Required=Y` in the spreadsheet:

```java
@Test
@Order(N)  // sequential ordering for validation tests
public void testCreate{BusinessObject}Without{FieldName}() throws JsonProcessingException {
    liqAPIData = getMainObjectFromJsonCreate(
        GeneralIntegrationMapping.{ENUM_CONSTANT}.toString(),
        LiqAPICreate{BusinessObject}Integration.class);
    liqAPIData.setIdempotencyKey(LiqApiDataUtil.generateIdempotencyKey());
    liqAPIData.set{FieldName}(null);  // or remove from list
    basicExecuteOutput = (LiqAPIResponse) this.invokeApiInterface(liqAPIData);
    assertEquals("false", basicExecuteOutput.getSuccess());
    basicExecuteOutput.getAPIMessages().forEach(message ->
        assertNotNull(((LiqAPIExceptionMessage) message).getMessage()));
}
```

### Phase 2: Invalid Value Tests (negative cases, ordered)

For attributes with **validation rules** described in the spreadsheet (code tables, length limits, format constraints, date ranges, enum values):

```java
@Test
@Order(N)
public void testCreate{BusinessObject}WithInvalid{FieldName}() throws JsonProcessingException {
    liqAPIData = getMainObjectFromJsonCreate(
        GeneralIntegrationMapping.{ENUM_CONSTANT}.toString(),
        LiqAPICreate{BusinessObject}Integration.class);
    liqAPIData.setIdempotencyKey(LiqApiDataUtil.generateIdempotencyKey());
    liqAPIData.set{FieldName}("INVALID_VALUE");  // or out-of-range value
    basicExecuteOutput = (LiqAPIResponse) this.invokeApiInterface(liqAPIData);
    assertEquals("false", basicExecuteOutput.getSuccess());
    basicExecuteOutput.getAPIMessages().forEach(message ->
        assertNotNull(((LiqAPIExceptionMessage) message).getMessage()));
}
```

### Phase 3: Positive / Happy-Path Tests (unordered)

For **every** attribute (mandatory AND optional):

```java
@Test
public void testCreate{BusinessObject}With{FieldName}() throws JsonProcessingException {
    liqAPIData = getMainObjectFromJsonCreate(
        GeneralIntegrationMapping.{ENUM_CONSTANT}.toString(),
        LiqAPICreate{BusinessObject}Integration.class);
    liqAPIData.setIdempotencyKey(LiqApiDataUtil.generateIdempotencyKey());
    // Set the field to a valid value (from spreadsheet description or test data)
    basicExecuteOutput = (LiqAPIResponse) this.invokeApiInterface(liqAPIData);
    assertEquals("true", basicExecuteOutput.getSuccess());
    assertNotNull(basicExecuteOutput.getResult());
}
```

### Phase 4: ATTRIBUTE_DESCRIPTION-Based Tests (MANDATORY)

**CRITICAL REQUIREMENT:** For **every** attribute with content in the ATTRIBUTE_DESCRIPTION column, generate an additional test case validating the rules/constraints/scenarios mentioned in the description.

If ATTRIBUTE_DESCRIPTION mentions:

**Business Rule:**
```java
@Test
public void testCreate{BusinessObject}{FieldName}BusinessRule() throws JsonProcessingException {
    // Example: "Field X must be greater than Field Y"
    liqAPIData = getMainObjectFromJsonCreate(
        GeneralIntegrationMapping.{ENUM_CONSTANT}.toString(),
        LiqAPICreate{BusinessObject}Integration.class);
    liqAPIData.setIdempotencyKey(LiqApiDataUtil.generateIdempotencyKey());
    liqAPIData.set{FieldX}(100);
    liqAPIData.set{FieldY}(200);  // Violates rule: X > Y
    basicExecuteOutput = (LiqAPIResponse) this.invokeApiInterface(liqAPIData);
    assertEquals("false", basicExecuteOutput.getSuccess());
}
```

**Format/Pattern Constraint:**
```java
@Test
public void testCreate{BusinessObject}{FieldName}FormatConstraint() throws JsonProcessingException {
    // Example: "Field must match pattern XXX-NNNN"
    liqAPIData = getMainObjectFromJsonCreate(
        GeneralIntegrationMapping.{ENUM_CONSTANT}.toString(),
        LiqAPICreate{BusinessObject}Integration.class);
    liqAPIData.setIdempotencyKey(LiqApiDataUtil.generateIdempotencyKey());
    liqAPIData.set{FieldName}("INVALID-FORMAT");
    basicExecuteOutput = (LiqAPIResponse) this.invokeApiInterface(liqAPIData);
    assertEquals("false", basicExecuteOutput.getSuccess());
}
```

**Conditional Behavior:**
```java
@Test
public void testCreate{BusinessObject}{FieldName}ConditionalBehavior() throws JsonProcessingException {
    // Example: "If Field X is set to 'Y', Field Z is required"
    liqAPIData = getMainObjectFromJsonCreate(
        GeneralIntegrationMapping.{ENUM_CONSTANT}.toString(),
        LiqAPICreate{BusinessObject}Integration.class);
    liqAPIData.setIdempotencyKey(LiqApiDataUtil.generateIdempotencyKey());
    liqAPIData.set{FieldX}("Y");
    liqAPIData.set{FieldZ}(null);  // Should fail because Z is conditionally required
    basicExecuteOutput = (LiqAPIResponse) this.invokeApiInterface(liqAPIData);
    assertEquals("false", basicExecuteOutput.getSuccess());
}
```

**Relational Validation:**
```java
@Test
public void testCreate{BusinessObject}{FieldName}RelationalValidation() throws JsonProcessingException {
    // Example: "Field X references Field Y; Y must exist"
    liqAPIData = getMainObjectFromJsonCreate(
        GeneralIntegrationMapping.{ENUM_CONSTANT}.toString(),
        LiqAPICreate{BusinessObject}Integration.class);
    liqAPIData.setIdempotencyKey(LiqApiDataUtil.generateIdempotencyKey());
    liqAPIData.set{FieldX}("NON_EXISTENT_REFERENCE");
    basicExecuteOutput = (LiqAPIResponse) this.invokeApiInterface(liqAPIData);
    assertEquals("false", basicExecuteOutput.getSuccess());
}
```

### Phase 5: Default Value Tests

For attributes with **default values** documented in the spreadsheet:

```java
@Test
public void testCreate{BusinessObject}{FieldName}DefaultsTo{DefaultValue}() throws JsonProcessingException {
    liqAPIData = getMainObjectFromJsonCreate(
        GeneralIntegrationMapping.{ENUM_CONSTANT}.toString(),
        LiqAPICreate{BusinessObject}Integration.class);
    liqAPIData.setIdempotencyKey(LiqApiDataUtil.generateIdempotencyKey());
    // DO NOT set the field — let default apply
    basicExecuteOutput = (LiqAPIResponse) this.invokeApiInterface(liqAPIData);
    assertEquals("true", basicExecuteOutput.getSuccess());
}
```

### Phase 6: Boundary / Edge Case Tests

For numeric/date fields with documented limits:

- Maximum value test
- Minimum value test
- Exact boundary value test
- Zero/empty for optional numerics

### Phase 7: Complex Object / List Tests

For attributes that are lists or nested objects (e.g., `FeePricing`, `MISCodes`, `RiskTypes`, `LenderDetails`):

- Test with single valid entry
- Test with multiple valid entries
- Test with empty list (if allowed)
- Test with invalid entry within list
- Test with duplicate entries (if uniqueness enforced)

### Phase 8: Identifier Variation Tests

For owner/parent identifiers that support multiple types (e.g., Deal identifier supports `id`, `name`, `alias`, etc.):

- One positive test per supported identifier type
- One test with invalid identifier type
- One test with non-existent identifier value

---

## Mandatory Idempotency Key Test

Every generated test class MUST include:

```java
@Test
@Order(1)
public void testCreate{BusinessObject}WithoutIdempotencyKey() throws JsonProcessingException {
    liqAPIData = getMainObjectFromJsonCreate(
        GeneralIntegrationMapping.{ENUM_CONSTANT}.toString(),
        LiqAPICreate{BusinessObject}Integration.class);
    // Deliberately do NOT call setIdempotencyKey
    basicExecuteOutput = (LiqAPIResponse) this.invokeApiInterface(liqAPIData);
    assertEquals("false", basicExecuteOutput.getSuccess());
}
```

---

## Mandatory Class-Mapping Coverage Tests

Every generated test class MUST include these three tests:

```java
@Test
public void testNonPrimitiveFieldMappings() {
    assertFalse(LiqAPICreate{BusinessObject}Integration.clazz.nonPrimitiveFieldMappings().isEmpty());
}

@Test
public void testPrimitiveFieldMappings() {
    assertFalse(LiqAPICreate{BusinessObject}Integration.clazz.primitiveFieldMappings().isEmpty());
}

@Test
public void testSecurityAccessSymbol() throws JsonProcessingException {
    LiqAPICreate{BusinessObject}Integration data = LiqApiDataUtil.getObjectFromJson(
        GeneralIntegrationMapping.{PRIMARY_ENUM_CONSTANT}.toString(),
        LiqAPICreate{BusinessObject}Integration.class);
    assertEquals("{ExpectedSecuritySymbol}", data.securityAccessSymbol());
}
```

---

## Response Assertions (Output Validation)

After every successful create, assert the response object fields:

```java
assertEquals("true", basicExecuteOutput.getSuccess());
Object result = basicExecuteOutput.getResult();
assertNotNull(result);
// Cast to the discovered response class:
LiqAPI{BusinessObject}IntegrationAsReturnValue response =
    (LiqAPI{BusinessObject}IntegrationAsReturnValue) result;
assertNotNull(response.getId());               // or getTransactionId() depending on class
assertNotNull(response.getUpdateTimeStamp());  // if present in response class
```

The specific getter method (`getId()`, `getTransactionId()`, `getProductGuaranteeIdentifierList()`) is determined during **Codebase Discovery Step 2**.

---

## Canonical Negative Test Pattern (reference)

```java
@Test
@Order(2)
public void testCreate{BusinessObject}Without{MandatoryField}() throws JsonProcessingException {
    liqAPIData = getMainObjectFromJsonCreate(
        GeneralIntegrationMapping.{ENUM_CONSTANT}.toString(),
        LiqAPICreate{BusinessObject}Integration.class);
    liqAPIData.setIdempotencyKey(LiqApiDataUtil.generateIdempotencyKey());
    liqAPIData.set{MandatoryField}(null);
    basicExecuteOutput = (LiqAPIResponse) this.invokeApiInterface(liqAPIData);
    assertEquals("false", basicExecuteOutput.getSuccess());
    assertFalse(basicExecuteOutput.getAPIMessages().isEmpty());
    basicExecuteOutput.getAPIMessages().forEach(message ->
        assertNotNull(((LiqAPIExceptionMessage) message).getMessage()));
}
```

---

## Canonical Positive Test Pattern (reference)

```java
@Test
public void testCreate{BusinessObject}WithAllMandatoryFields() throws JsonProcessingException {
    liqAPIData = getMainObjectFromJsonCreate(
        GeneralIntegrationMapping.{ENUM_CONSTANT}.toString(),
        LiqAPICreate{BusinessObject}Integration.class);
    liqAPIData.setIdempotencyKey(LiqApiDataUtil.generateIdempotencyKey());
    basicExecuteOutput = (LiqAPIResponse) this.invokeApiInterface(liqAPIData);
    assertEquals("true", basicExecuteOutput.getSuccess());
    LiqAPI{BusinessObject}IntegrationAsReturnValue response =
        (LiqAPI{BusinessObject}IntegrationAsReturnValue) basicExecuteOutput.getResult();
    assertNotNull(response);
    assertNotNull(response.getId());
}
```

---

## Attribute-to-Test Mapping Rules

For each attribute row in the spreadsheet, generate tests as follows:

| Attribute Characteristic | Tests to Generate |
|---|---|
| Required=Y | `testWithout{Field}` (null → failure) + `testWith{Field}` (valid → success) |
| Required=N (optional) | `testWith{Field}` (valid → success) + `testWithout{Field}` (omit → success with default) |
| Has validation rule (code table) | `testWithInvalid{Field}` (bogus value → failure) |
| Has default value | `testDefault{Field}` (omit → verify default applied) |
| Enum type | One positive test per allowed value + one invalid test |
| Date with range constraints | `testWithFuture{Field}`, `testWithPast{Field}`, boundary tests |
| Numeric with limits | `testWithZero{Field}`, `testWithNegative{Field}`, max value test |
| String with max length | `testWith{Field}ExceedsMaxLength` |
| Boolean with default | `testWith{Field}True`, `testWith{Field}False`, `testDefault{Field}` |
| List/Collection | `testWithSingle{Field}`, `testWithMultiple{Field}`, `testWithEmpty{Field}`, `testWithDuplicate{Field}` (if uniqueness) |
| Complex nested object | `testWith{Field}` (valid) + `testWithout{Subfield}` (for each required sub-field) |
| Identifier group (multiple types) | One test per identifier type + invalid type test + non-existent value test |

---

## Compilation Safety Rules

To ensure NO compilation errors in generated test classes:

1. **Never reference a class that doesn't exist** — Always verify via codebase discovery before using a class name.
2. **Never call a method that doesn't exist** — Verify getter/setter names from the actual source class.
3. **Use correct generics** — When casting `getResult()`, use the verified response class.
4. **Import all used classes** — Generate complete import statements based on actual packages.
5. **Match enum constant names exactly** — Copy from `GeneralIntegrationMapping.java`, do not guess.
6. **Use correct assertion methods** — `assertEquals`, `assertNotNull`, `assertFalse`, `assertTrue` from `org.junit.jupiter.api.Assertions`.
7. **Proper exception handling** — All test methods declare `throws JsonProcessingException`.
8. **Do not reference non-existent JSON templates** — Only use enum constants found in Step 3.
9. **String literals for field values** — Use actual valid values from test data or spreadsheet examples.
10. **Match actual setter signatures** — Some fields use `setX(String)`, others `setX(Boolean)`, etc.

---

## Coverage Checklist (must achieve ~100%)

Before finalizing the test class, verify:

- [ ] Every Required=Y attribute has a null/missing negative test
- [ ] Every Required=Y attribute has a valid positive test
- [ ] Every optional attribute has at least one positive test
- [ ] Every attribute with validation rules has an invalid-value negative test
- [ ] Every attribute with defaults has a default-verification test
- [ ] Every identifier group has per-type positive tests + invalid tests
- [ ] Every complex object/list attribute has structural tests
- [ ] Idempotency key test is present
- [ ] Class-mapping coverage tests (3) are present
- [ ] Security access symbol test is present
- [ ] All enum constant templates found in codebase have at least one associated test
- [ ] Response assertions verify all output fields documented in spreadsheet

---

## Don'ts

- No `Mockito.mock(...)`, `@Mock`, `mockStatic`, `spy`, or `@InjectMocks`.
- No instantiating `LiqAPIResponse` manually — must come from `invokeApiInterface(...)` or `callBasicExecute(...)`.
- No tests that bypass `BaseTestLoanIQ` initialization.
- No getter/setter-only tests — field exercise must occur through an `invokeApiInterface` round-trip.
- No hardcoded RIDs or identifiers — always use values from JSON templates or dynamic generation.
- No test methods without assertions.
- No duplicate test methods testing the same scenario.
- No tests that depend on execution order of other tests (each test is self-contained).

---

## Execution Workflow Summary

> **All scripts are located at**: `.github/skills/lending-create-test-api/scripts/`

```
1. Verify BOTH inputs (Business Object + Spreadsheet Path) are present
   └─ If missing → ASK user → STOP until provided

2. INVOKE: extract-spreadsheet.ps1
   ┌─────────────────────────────────────────────────────────────────────────────┐
   │ .\scripts\extract-spreadsheet.ps1 -SpreadsheetPath "{SpreadsheetPath}"      │
   └─────────────────────────────────────────────────────────────────────────────┘
   └─ Produces extracted XML folder at {ExtractedPath}

3. INVOKE: find-create-sheet.ps1
   ┌─────────────────────────────────────────────────────────────────────────────┐
   │ .\scripts\find-create-sheet.ps1 -ExtractedPath "{ExtractedPath}"            │
   └─────────────────────────────────────────────────────────────────────────────┘
   └─ Returns sheet filename (e.g., "sheet4.xml")
   └─ If no Create sheet found → inform user → STOP

4. INVOKE: parse-create-attributes.ps1 (with fallback chain)
   ┌─────────────────────────────────────────────────────────────────────────────┐
   │ .\scripts\parse-create-attributes.ps1 -ExtractedPath "{ExtractedPath}"      │
   │     -SheetFile "{SheetFile}"                                                │
   └─────────────────────────────────────────────────────────────────────────────┘
   └─ Returns structured attribute list (Category, Field, Type, Required, Desc)
   └─ Note all mandatory fields, optional fields, collections, identifiers
   └─ IF FAILED (exit code != 0, or 0 attributes returned):
       ┌─────────────────────────────────────────────────────────────────────────┐
       │ .\scripts\parse-create-attributes-fallback.ps1                          │
       │     -ExtractedPath "{ExtractedPath}" -SheetFile "{SheetFile}"           │
       └─────────────────────────────────────────────────────────────────────────┘
       └─ Uses dynamic header detection (scans for keywords in any column)
       └─ IF ALSO FAILED:
           ┌─────────────────────────────────────────────────────────────────────┐
           │ .\scripts\parse-via-excel-com.ps1                                   │
           │     -SpreadsheetPath "{SpreadsheetPath}"                            │
           └─────────────────────────────────────────────────────────────────────┘
           └─ Opens file directly via Excel COM automation
           └─ IF ALL THREE FAIL → Ask user to provide attributes manually

5. INVOKE: discover-codebase.ps1
   ┌─────────────────────────────────────────────────────────────────────────────┐
   │ .\scripts\discover-codebase.ps1 -BusinessObject "{BusinessObject}"          │
   │     -CodebasePath "C:\Users\asrivas3\git\7740_3\FLIQ-liqjava\LoanIQ"       │
   └─────────────────────────────────────────────────────────────────────────────┘
   └─ Returns: integration class, response class, enum constants, security symbol
   └─ Supplement with targeted file reads for setters, imports, field annotations

6. READ: JSON Request Payload
   ┌─────────────────────────────────────────────────────────────────────────────┐
   │ Read: LoanIQ/test-resources/json/{domain}/Create{BusinessObject}            │
   │       Integration.json                                                      │
   │ Read: .github/skills/lending-create-test-api/templates/json-request.md      │
   │ Check: IntegrationAPITool/artifacts/temp_generated_class/ (for new objects) │
   └─────────────────────────────────────────────────────────────────────────────┘
   └─ Understand pre-populated fields in the JSON payload
   └─ Map JSON attribute names to setter methods on the integration class
   └─ Identify which fields to null/override for negative test scenarios

7. CHECK: Test File in Temp Folder (IntegrationAPITool)
   ┌─────────────────────────────────────────────────────────────────────────────┐
   │ Check if test file already exists at:                                       │
   │   IntegrationAPITool/artifacts/temp_generated_class/                        │
   │   (Look for LiqAPICreate{BusinessObject}IntegrationTest.java)               │
   └─────────────────────────────────────────────────────────────────────────────┘
   └─ IF FOUND in temp folder:
       └─ Update the test class in this temp location with new JUnit tests
       └─ Once all JUnits are generated, MOVE the file to the correct
          FLIQ-liqjava repo location:
          C:\Users\asrivas3\git\7740_3\FLIQ-liqjava\LoanIQ\test\com\misys\liq\
          api\rest\executable\{domain}\
       └─ Continue to Step 10 (Run Tests)
   └─ IF NOT FOUND in temp folder → Continue to Step 8

8. CHECK: Test File in FLIQ-liqjava Repo
   ┌─────────────────────────────────────────────────────────────────────────────┐
   │ Check if test file already exists at:                                       │
   │   C:\Users\asrivas3\git\7740_3\FLIQ-liqjava\LoanIQ\test\com\misys\liq\    │
   │   api\rest\executable\{domain}\                                             │
   │   LiqAPICreate{BusinessObject}IntegrationTest.java                          │
   └─────────────────────────────────────────────────────────────────────────────┘
   └─ IF FOUND in FLIQ-liqjava repo:
       └─ Generate new test cases AFTER the existing ones (append, do not
          overwrite existing tests)
       └─ Continue to Step 10 (Run Tests)
   └─ IF NOT FOUND in FLIQ-liqjava repo → Continue to Step 9

9. Generate NEW test class following:
   └─ Class skeleton (package, imports, annotations, fields, @BeforeEach)
   └─ Phase 1: Mandatory field validation (negative, ordered)
   └─ Phase 2: Invalid value tests (negative, ordered)
   └─ Phase 3: Positive happy-path tests (unordered)
   └─ Phase 4: Default value tests
   └─ Phase 5: Boundary/edge case tests
   └─ Phase 6: Complex object/list tests
   └─ Phase 7: Identifier variation tests
   └─ Mandatory: Idempotency key test
   └─ Mandatory: Class-mapping coverage tests (3)
   └─ Mandatory: Response assertions
   └─ Place the generated test class at:
      C:\Users\asrivas3\git\7740_3\FLIQ-liqjava\LoanIQ\test\com\misys\liq\
      api\rest\executable\{domain}\
      LiqAPICreate{BusinessObject}IntegrationTest.java

10. RUN: Execute All Test Cases
   ┌─────────────────────────────────────────────────────────────────────────────┐
   │ Run the generated/updated test class using the configured test runner       │
   │ (JUnit Ant task or IDE test runner)                                         │
   └─────────────────────────────────────────────────────────────────────────────┘
   └─ Execute all test cases in the test class
   └─ Collect results: PASS / FAIL for each test method

11. FIX: Repair Failing Test Cases (iterative)
   ┌─────────────────────────────────────────────────────────────────────────────┐
   │ IF any test cases FAIL:                                                     │
   │   a. Analyze the failure reason (compilation error, assertion failure,      │
   │      runtime exception, missing dependency)                                 │
   │   b. Fix the failing test case(s) in the test class                         │
   │   c. Re-run ALL test cases to verify the fix                                │
   │   d. REPEAT steps (a)-(c) until ALL test cases PASS                         │
   │                                                                             │
   │ Keep iterating fix → run → verify until 100% pass rate is achieved.         │
   └─────────────────────────────────────────────────────────────────────────────┘

12. REPORT: Generate Test Coverage Report
   ┌─────────────────────────────────────────────────────────────────────────────┐
   │ Generate: lending-create-test-api.md                                        │
   │ Location: Same path as the test class:                                      │
   │   C:\Users\asrivas3\git\7740_3\FLIQ-liqjava\LoanIQ\test\com\misys\liq\    │
   │   api\rest\executable\{domain}\lending-create-test-api.md                   │
   └─────────────────────────────────────────────────────────────────────────────┘
   └─ Report Contents:
       ├─ Test Class Summary (class name, total tests, pass/fail counts)
       ├─ Individual Test Case Results Table:
       │   | # | Test Method Name | Status (PASS/FAIL) | Description |
       ├─ Spreadsheet Attribute Coverage Table:
       │   | Attribute Name | Required | Covered by Test(s) | Coverage Status |
       │   (Compare each attribute from the spreadsheet against generated tests
       │    to show whether the attribute is fully covered, partially covered,
       │    or not covered)
       └─ Summary: Total attributes, covered count, coverage percentage

13. Verify coverage checklist (~100%)

14. DONE — Return complete compilable Java test class + coverage report
```

### Script Location Reference

| Script | Path | Purpose |
|---|---|---|
| `extract-spreadsheet.ps1` | `.github/skills/lending-create-test-api/scripts/extract-spreadsheet.ps1` | Extract .xlsx to XML |
| `find-create-sheet.ps1` | `.github/skills/lending-create-test-api/scripts/find-create-sheet.ps1` | Find the Create worksheet |
| `parse-create-attributes.ps1` | `.github/skills/lending-create-test-api/scripts/parse-create-attributes.ps1` | Parse all attribute rows (primary) |
| `parse-create-attributes-fallback.ps1` | `.github/skills/lending-create-test-api/scripts/parse-create-attributes-fallback.ps1` | Parse attributes with dynamic header detection (fallback #1) |
| `parse-via-excel-com.ps1` | `.github/skills/lending-create-test-api/scripts/parse-via-excel-com.ps1` | Parse via Excel COM automation (fallback #2) |
| `discover-codebase.ps1` | `.github/skills/lending-create-test-api/scripts/discover-codebase.ps1` | Find classes, enums, symbols |

### Template & JSON Reference

| Resource | Path | Purpose |
|---|---|---|
| Generic JSON template | `.github/skills/lending-create-test-api/templates/json-request.md` | Understand payload structure |
| Actual JSON payloads | `LoanIQ/test-resources/json/{domain}/Create{BusinessObject}Integration.json` | Pre-populated test data |
| Generated samples | `IntegrationAPITool/artifacts/temp_generated_class/` | Newly scaffolded objects |
| Temp test file | `IntegrationAPITool/artifacts/temp_generated_class/LiqAPICreate{BusinessObject}IntegrationTest.java` | Check for pre-existing test class in temp folder |
| Final test location | `C:\Users\asrivas3\git\7740_3\FLIQ-liqjava\LoanIQ\test\com\misys\liq\api\rest\executable\{domain}\` | Target location for test class in FLIQ-liqjava repo |
| Coverage report | `lending-create-test-api.md` (same folder as test class) | Generated test coverage report |
