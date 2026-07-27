---
name: lending-delete-test-api
description: 'Generic consolidated skill for generating JUnit5 integration test classes for any LoanIQ Delete API. Driven by a Business Object name and a Requirement Spreadsheet path. Covers ALL attributes from the spreadsheet Delete sheet with 100% test coverage using real DB round-trips.'
---

# Generic Delete Integration Test — Consolidated Skill

> **Purpose**: Generate a complete `LiqAPIDelete{BusinessObject}IntegrationTest extends BaseTestLoanIQ` class that achieves 100% test coverage for any LoanIQ REST API Delete operation, exclusively using real DB-backed integration patterns.

### Constraint Categories (in priority order)

**Primary — Test Constraints** (applies to generated Java test code only — these activate ONLY after all prerequisites pass):
- Integration tests ONLY — NO Mockito, NO mocks, NO stubs, NO faked responses
- Every test exercises a real DB round-trip via `invokeApiInterface()` or `callBasicExecute()`
- Halting on missing prerequisites (e.g., no Delete sheet) is NOT a conflict — tests are generated only when all inputs are valid

**Secondary — Spreadsheet Processing Constraints** (applies to how the agent reads the .xlsx input — unrelated to test methodology):
- Use the provided PowerShell scripts to extract and parse spreadsheet contents
- The scripts handle .xlsx internal XML parsing and column layout detection automatically
- If the primary parser fails (zero attributes, format mismatch, or script error), automatically invoke the fallback scripts in sequence before asking the user to intervene
- Fallback order: `parse-delete-attributes.ps1` → `parse-delete-attributes-fallback.ps1` → `parse-via-excel-com.ps1`
- Only ask the user to manually provide attribute data if ALL fallback scripts fail

**Secondary — Codebase Constraints** (applies to class/method references in generated code):
- All referenced classes, methods, and enum constants MUST be verified to exist before use

**Secondary — Coverage Constraints** (applies to test completeness):
- Every spreadsheet attribute must have at least one test case
- **MANDATORY:** For every "ATTRIBUTE_FIELD_NAME" column, generate an additional test case based on the information in "ATTRIBUTE_DESCRIPTION" column
- Mandatory fields get both positive and negative tests
- Optional fields get positive tests plus boundary/validation tests where applicable
- Description-based tests must validate business rules, constraints, or scenarios mentioned in the ATTRIBUTE_DESCRIPTION
- Primitive field mappings, non-primitive field mappings, and non-primitive collection mapping attributes MUST all be covered

> **Note on halting**: If the Delete sheet is missing from the spreadsheet, execution stops and the user is prompted. This is a prerequisite check — it does not conflict with test methodology constraints. Tests are only generated once all prerequisites are met.

---

## When to Use

Use `lending-delete-test-api` when:
- Generating **Delete integration test classes** (JUnit5) for any LoanIQ business object
- The entity has a Delete operation defined in the requirement spreadsheet
- You need 100% test coverage for all attributes in the Delete sheet
- You need real DB-backed integration tests (no mocks)

DO NOT use this skill for:
- Generating the Delete API implementation class itself (use `lending-delete-api`)
- Create test classes (use `lending-create-test-api`)
- Update test classes (use `lending-update-test-api`)
- Query/Get test classes (use `lending-query-test-api`)

---

## How to Run

Invoke via VS Code Copilot Chat with the skill prefix:

```text
/lending-delete-test-api Business Object is '{BusinessObject}'. Generate the JUnit5 Delete Integration Test Class using the requirement spreadsheet at path: '{SpreadsheetPath}'.
```

Or invoked automatically by the `lending-api-developer` agent during Step 5.

---

## Output Format

The skill produces a JUnit5 test class at:

```text
FLIQ-liqjava/LoanIQ/test/com/misys/liq/api/rest/executable/{domain}/LiqAPIDelete{BusinessObject}IntegrationTest.java
```

The test class:
- Extends `BaseTestLoanIQ`
- Uses `@TestMethodOrder(OrderAnnotation.class)`
- Contains integration tests with `@Order` annotations
- Achieves 100% attribute coverage from the spreadsheet

---

## User Prompt Template

To invoke this skill, you **must** prefix your prompt with `/lending-delete-test-api` in VS Code Copilot Chat. This tells Copilot to use this skill's instructions and patterns.

Copy and paste the prompt below into the chat, replacing the placeholders with your actual values:

---

### Prompt (copy this):

```
/lending-delete-test-api Business Object is '{BusinessObject}'. Generate the JUnit5 Delete Integration Test Class using the requirement spreadsheet at path: '{SpreadsheetPath}'.
```

### Full Prompt (with explicit requirements — optional, for more control):

```
/lending-delete-test-api Business Object is '{BusinessObject}'. Generate the JUnit5 Delete Integration Test Class
using the requirement spreadsheet at path: '{SpreadsheetPath}'.

Requirements:
- Test class name: LiqAPIDelete{BusinessObject}IntegrationTest
- Must extend BaseTestLoanIQ
- Integration tests ONLY (NO Mockito, NO mocks, NO stubs)
- Parse the "Delete" sheet from the spreadsheet for all attributes
- Generate negative tests for every mandatory field (null/missing → failure)
- Generate positive tests for every attribute (mandatory + optional)
- Generate invalid value tests for fields with code table or validation constraints
- Generate boundary/edge case tests for numeric and date fields
- Include class-mapping coverage tests (nonPrimitiveFieldMappings, primitiveFieldMappings, securityAccessSymbol)
- Include response assertion tests verifying the return value object
- Achieve 100% test coverage across all spreadsheet attributes
- Ensure ZERO compilation errors — verify all classes, methods, and enum constants exist in the codebase before referencing them
- Cover primitive, non-primitive, and non-primitive collection mapping attributes

Execution order (sequential stages — complete each before starting the next):
Stage 1 — Validation: Validate inputs (Business Object + Spreadsheet Path) — halt if missing
Stage 2 — Data Extraction: Extract spreadsheet → find Delete sheet → parse attributes (with automatic fallback if format is non-standard) — halt only if ALL parsers fail
Stage 3 — Discovery: Discover codebase classes, enums, and security symbols
Stage 4 — Template: Read JSON delete request payload template — generate default from spreadsheet if missing
Stage 5 — Generation: Generate test class with all phases
Stage 6 — Run & Fix: Run test cases, fix failures, iterate until all pass
Stage 7 — Reporting: Generate lending-delete-test-api.md report with coverage comparison
```

### Example Prompts:

**UpfrontFee:**
```
/lending-delete-test-api Business Object is 'UpfrontFee'. Generate the JUnit5 Delete Integration Test Class using the requirement spreadsheet at path: 'C:\Auto\API\Upfront Fee v2.1.xlsx'.
```

**Deal:**
```
/lending-delete-test-api Business Object is 'Deal'. Generate the JUnit5 Delete Integration Test Class using the requirement spreadsheet at path: 'C:\Auto\API\Deal REST API_V1.xlsx'.
```

**Facility:**
```
/lending-delete-test-api Business Object is 'Facility'. Generate the JUnit5 Delete Integration Test Class using the requirement spreadsheet at path: 'C:\Auto\API\Facility v9.xlsx'.
```

**LoanDrawdown:**
```
/lending-delete-test-api Business Object is 'LoanDrawdown'. Generate the JUnit5 Delete Integration Test Class using the requirement spreadsheet at path: 'C:\Auto\API\Loan InitialDrawndown v1.17.xlsx'.
```

**LoanRepricing:**
```
/lending-delete-test-api Business Object is 'LoanRepricing'. Generate the JUnit5 Delete Integration Test Class using the requirement spreadsheet at path: 'C:\Auto\API\Comprehensive Repricing V1.xlsx'.
```

**LoanInterestPayment:**
```
/lending-delete-test-api Business Object is 'LoanInterestPayment'. Generate the JUnit5 Delete Integration Test Class using the requirement spreadsheet at path: 'C:\Auto\API\Interest Payment API v1.7.xlsx'.
```

**LoanPrincipalPayment:**
```
/lending-delete-test-api Business Object is 'LoanPrincipalPayment'. Generate the JUnit5 Delete Integration Test Class using the requirement spreadsheet at path: 'C:\Auto\API\Principal Payment API v1.8.xlsx'.
```

**ProductGuarantee:**
```
/lending-delete-test-api Business Object is 'ProductGuarantee'. Generate the JUnit5 Delete Integration Test Class using the requirement spreadsheet at path: 'C:\Auto\API\Facility v9.xlsx'.
```

**MISCode:**
```
/lending-delete-test-api Business Object is 'MISCode'. Generate the JUnit5 Delete Integration Test Class using the requirement spreadsheet at path: 'C:\Auto\API\MIS Codes API v1.xlsx'.
```

---

## Required Inputs (MUST be present in the prompt)

Before generating any test class, **verify** that the user's prompt contains BOTH of these:

| # | Input | Description | Example |
|---|---|---|---|
| 1 | **Business Object** | The Pascal-case name of the entity | `UpfrontFee`, `Deal`, `Facility`, `LoanDrawdown`, `LoanRepricing`, `LoanInterestPayment`, `LoanPrincipalPayment`, `ProductGuarantee`, `MISCode` |
| 2 | **Requirement Spreadsheet Path** | Full path to the `.xlsx` file containing the API specification | `C:\Auto\API\Upfront Fee v2.1.xlsx` |

### If either input is missing — ASK the user:

```
I need two pieces of information to generate the Delete test class:

1. **Business Object Name** — The Pascal-case name of the entity (e.g., `UpfrontFee`, `Deal`, `LoanDrawdown`, `ProductGuarantee`).
2. **Requirement Spreadsheet Path** — The full file path to the Excel (.xlsx) file containing the API specification (e.g., `C:\Auto\API\Upfront Fee v2.1.xlsx`).

Please provide both.
```

**Wait for both inputs** before continuing to the next stage.

---

## Test File Location Strategy

Before generating the test class, follow these steps to determine where to write the file:

### Step 1: Check Temp Folder First

Look for an existing test file for this business object in the temp generation folder:

```
IntegrationAPITool/artifacts/temp_generated_class/
```

- If found → **Update this file** with the new JUnit test methods.
- After all JUnits are generated → **Move the file** to the correct FLIQ-liqjava repo location (Step 3).

### Step 2: If Not in Temp Folder → Check FLIQ-liqjava Repo

If no test file exists in the temp folder, go directly to:

```
C:\Users\asrivas3\git\7740_3\FLIQ-liqjava\LoanIQ\test\com\misys\liq\api\rest\executable\{domain}\
```

Where `{domain}` is derived from the business object (e.g., `deal`, `upfrontfee`, `outstanding/drawdown`, `guarantor`, `miscode`).

### Step 3: File Handling Logic

| Scenario | Action |
|---|---|
| File exists in temp folder | Update test class in temp folder, then move to FLIQ-liqjava repo test path |
| File NOT in temp folder, EXISTS in FLIQ-liqjava repo | Append new test methods after existing ones |
| File NOT in temp folder, NOT in FLIQ-liqjava repo | Generate a brand new test class at the FLIQ-liqjava repo path |

### Final Destination Path:

```
C:\Users\asrivas3\git\7740_3\FLIQ-liqjava\LoanIQ\test\com\misys\liq\api\rest\executable\{domain}\LiqAPIDelete{BusinessObject}IntegrationTest.java
```

---

## Spreadsheet Processing Rules

> **NOTE**: The scripts below handle .xlsx internal XML extraction and column detection. This constraint is about how the agent reads spreadsheet data — it is unrelated to test methodology constraints (no mocks, etc.).

### Script 1: Extract the Spreadsheet

Run `extract-spreadsheet.ps1` to convert the `.xlsx` to parseable XML:

```powershell
& "$PSScriptRoot\..\lending-delete-test-api\scripts\extract-spreadsheet.ps1" -SpreadsheetPath "{SpreadsheetPath}"
```

This produces an extracted folder (e.g., `C:\Auto\API\{BusinessObject}_extracted`) containing `xl/workbook.xml`, `xl/sharedStrings.xml`, and `xl/worksheets/*.xml`.

### Script 2: Find the Delete Sheet

Run `find-delete-sheet.ps1` to locate the exact worksheet XML file for the "Delete" sheet:

```powershell
& "$PSScriptRoot\..\lending-delete-test-api\scripts\find-delete-sheet.ps1" -ExtractedPath "{ExtractedPath}"
```

This returns the sheet filename (e.g., `sheet5.xml`). If no "Delete" sheet exists, stop execution and ask the user for a valid spreadsheet. Wait for user response before continuing.

### Script 3: Parse All Delete Attributes

Run `parse-delete-attributes.ps1` to extract all attribute rows with auto-detected column layout:

```powershell
& "$PSScriptRoot\..\lending-delete-test-api\scripts\parse-delete-attributes.ps1" -ExtractedPath "{ExtractedPath}" -SheetFile "{SheetFile}"
```

This returns structured attribute data: Category, FieldName, DataType, Required, Description, DefaultValue.

### Script 3a (Fallback): Parse Attributes with Dynamic Header Detection

If Script 3 fails (exits with error, returns 0 attributes, or reports "Could not auto-detect layout"), run the fallback parser:

```powershell
& "$PSScriptRoot\..\lending-delete-test-api\scripts\parse-delete-attributes-fallback.ps1" -ExtractedPath "{ExtractedPath}" -SheetFile "{SheetFile}"
```

This fallback script:
- Scans ALL cells in the first 30 rows looking for header keywords ("Field Name", "Attribute", "Data Type", "Required", etc.)
- Maps columns dynamically based on header content rather than fixed positions
- Handles spreadsheets with non-standard column orderings, extra columns, or different header naming
- Normalizes Required values ("Yes"/"Mandatory"/"M" → "Y", "No"/"Optional" → "N", etc.)
- Falls back to positional heuristics if keyword matching partially succeeds

### Script 3b (Last Resort): Parse via Excel COM Automation

If Script 3a also fails, use the COM-based parser:

```powershell
& "$PSScriptRoot\..\lending-delete-test-api\scripts\parse-via-excel-com.ps1" -SpreadsheetPath "{SpreadsheetPath}" -SheetName "Delete"
```

This script:
- Opens the .xlsx directly using Excel COM automation (requires Excel installed)
- Reads cell values including those in merged ranges
- Uses the same dynamic header detection as the fallback parser
- Reports a clear error if Excel is not installed, suggesting the user provide attribute data manually

### Fallback Decision Logic

```
parse-delete-attributes.ps1 (primary)
  ├─ SUCCESS (attributes.Count > 0) → Continue to Stage 3
  └─ FAILURE (exit code != 0, 0 attributes, or layout detection failed)
       │
       ▼
parse-delete-attributes-fallback.ps1 (fallback #1)
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
   - `FieldName` — the JSON/Java field name (maps to `ATTRIBUTE_FIELD_NAME` column)
   - `DataType` — String, Integer, Number, Boolean, Date, Enum, List, Object
   - `Required` — Y (mandatory), N (optional), CR (conditionally required)
   - `Description` — business rules, defaults, constraints, validation info (maps to `ATTRIBUTE_DESCRIPTION` column)
   - `Category` — grouping (e.g., OwnerIdentifier, TransactionIdentifier, FeeIdentifier)
   - `DefaultValue` — default value if field is omitted
   - `MappingType` — Primitive, NonPrimitive, NonPrimitiveCollection

**CRITICAL REQUIREMENT — Spreadsheet Column Reading:**

For every row in the Delete sheet of the requirement spreadsheet:
- Read **ATTRIBUTE_FIELD_NAME** column → Use as the field name for test generation
- Read **ATTRIBUTE_DESCRIPTION** column → Use to generate description-based test cases

**Conditional Processing:** These columns are ONLY processed if:
- The "Delete" sheet exists in the requirement spreadsheet
- The operation type "Delete" is valid for this business object
- If the Delete sheet does not exist, STOP and prompt user for valid spreadsheet

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
| Integration (delete) class | `LiqAPIDelete{BusinessObject}Integration` | `LiqAPIDeleteUpfrontFeeIntegration` |
| Test class | `LiqAPIDelete{BusinessObject}IntegrationTest` | `LiqAPIDeleteUpfrontFeeIntegrationTest` |
| Identifier class | `LiqAPI{BusinessObject}Identifier` | `LiqAPIUpfrontFeeIdentifier` |
| Response (return value) class | `LiqAPI{BusinessObject}IntegrationAsReturnValue` | `LiqAPIUpfrontFeeIntegrationAsReturnValue` |
| Create class (for bootstrap seed) | `LiqAPICreate{BusinessObject}Integration` | `LiqAPICreateUpfrontFeeIntegration` |
| Query class (for post-delete verify) | `LiqAPIQuery{BusinessObject}Integration` | `LiqAPIQueryUpfrontFeeIntegration` |
| Owner Identifier (if applicable) | `LiqAPIOwnerIdentifier` | `LiqAPIOwnerIdentifier` |
| Transaction Identifier (if applicable) | `LiqAPIOutstandingTransactionIdentifier` | `LiqAPIOutstandingTransactionIdentifier` |
| Java package | `com.misys.liq.api.rest.executable.{domain}` | `com.misys.liq.api.rest.executable.upfrontfee` |
| `GeneralIntegrationMapping` delete prefix | `DELETE_{SCREAMING_SNAKE_CASE}_*` | `DELETE_UPFRONTFEE_TRANSACTION` |

### Special Cases — Cancel-as-Delete Pattern

Some business objects use a **Cancel** operation instead of a **Delete** class:

| Business Object | Delete Class Name | Base Class |
|---|---|---|
| LoanRepricing | `LiqAPICancelLoanRepricing` | `LiqAPICancelTransaction` |
| LoanInterestPayment | `LiqAPICancelInterestPayment` | `LiqAPICancelTransaction` |

For Cancel-as-Delete objects:
- Test class → `LiqAPICancel{BusinessObject}IntegrationTest`
- Use `basicNew()` to instantiate instead of JSON loading
- Set `setIdempotencyKey(...)` on the cancel DTO
- Wire `outstandingTransactionIdentifier` from CREATE response

**Naming Rules:**
- Use Pascal-case for `{BusinessObject}` **exactly** as provided in the prompt.
- The domain (package segment) is the lowercased form of the business object name.
- For the `GeneralIntegrationMapping` enum prefix, convert to `SCREAMING_SNAKE_CASE`.
- Annotations `@TestMethodOrder`, `@BeforeEach`, and `extends BaseTestLoanIQ` are always present.

---

## Codebase Discovery Steps (mandatory before code generation)

Before writing the test class, the agent MUST perform codebase discovery to avoid compilation errors.

> **IMPORTANT**: Run the `discover-codebase.ps1` script first, then supplement with targeted searches.

### Run the Discovery Script

```powershell
& "$PSScriptRoot\..\lending-delete-test-api\scripts\discover-codebase.ps1" -BusinessObject "{BusinessObject}" -CodebasePath "C:\Users\asrivas3\git\7740_3\FLIQ-liqjava\LoanIQ"
```

This script automatically searches for:
- Delete integration class: `LiqAPIDelete{BusinessObject}Integration.java` or `LiqAPICancel{BusinessObject}.java`
- Create integration class: `LiqAPICreate{BusinessObject}Integration.java`
- Query integration class: `LiqAPIQuery{BusinessObject}Integration.java`
- Response class: `LiqAPI{BusinessObject}IntegrationAsReturnValue.java` (with getter methods)
- Identifier class: `LiqAPI{BusinessObject}Identifier.java` or `LiqAPIOutstandingTransactionIdentifier.java`
- `GeneralIntegrationMapping` enum constants matching the business object (DELETE_, CREATE_, QUERY_)
- Security access symbol from `APICommonConstants`
- Existing test class (if already present)

### Post-Script Verification Steps

After running the discovery script, verify and supplement with these targeted searches:

1. **Locate the Delete Integration Class** — Confirm the class name, package, and methods (especially `basicValidate()`, `basicExecute()`, field mappers).
2. **Identify the Identifier Pattern** — Is it `LiqAPIOutstandingTransactionIdentifier` (for outstanding transactions) or a domain-specific `LiqAPI{BusinessObject}Identifier` or `LiqAPIOwnerIdentifier`?
3. **Find GeneralIntegrationMapping Enum Constants** — Confirm DELETE, CREATE, and QUERY enum constants.
4. **Verify Security Access Symbol** — Confirm from the integration class's `securityAccessSymbol()` method.
5. **Determine If-Match Pattern** — Check if the delete requires `setMatchUpdatedTimestamp()` (i.e., concurrency control).
6. **Check for Additional Required Imports** — Look at `@LiqAPIFieldMapper` annotations.

---

## JSON Delete Request Payload Templates

> **IMPORTANT**: Before generating test methods, you MUST understand the JSON delete request payload structure for the business object.

### Template Location

The generic JSON delete request payload template is available in the skill's `templates/` subfolder:

```
.github/skills/lending-delete-test-api/templates/json-delete-request.md
```

### Sample JSON for a Business Object

The actual sample JSON delete request payload for a given business object should be checked at:

1. **Temp folder** (for newly generated/scaffolded objects):
   ```
   FLIQ-liqjava/IntegrationAPITool/artifacts/temp_generated_class/
   ```

2. **Test resources folder** (for established objects):
   ```
   LoanIQ/test-resources/json/{domain}/Delete{BusinessObject}Integration.json
   ```

### How JSON Relates to Test Methods

Each test method in the Delete test class loads the delete DTO via:

```java
// Pattern A: JSON-based loading (Deal, UpfrontFee, ProductGuarantee, MISCode)
liqAPiDataDelete = getMainObjectFromJsonDelete(
    GeneralIntegrationMapping.DELETE_{ENUM_CONSTANT}.toString(),
    LiqAPIDelete{BusinessObject}Integration.class);

// Pattern B: Direct instantiation (LoanDrawdown, cancel-based operations)
liqAPIDataDelete = new LiqAPIDelete{BusinessObject}Integration();
// OR for cancel-as-delete:
liqAPIDataCancel = (LiqAPICancel{BusinessObject}) LiqAPICancel{BusinessObject}.clazz.basicNew();
```

---

## Allowed APIs (whitelist)

Only these helpers may appear in generated tests:

| Helper | Purpose |
|---|---|
| `getMainObjectFromJsonCreate(enum, Class)` | Bootstrap entity to delete |
| `getMainObjectFromJsonQuery(enum, Class)` | Re-fetch for `updateTimeStamp` (If-Match) |
| `getMainObjectFromJsonDelete(enum, Class)` | Build delete DTO from JSON template |
| `invokeApiInterface(liqAPIData)` | Single-commit DB round trip |
| `LiqApiDataUtil.getObjectFromJson(enum, Class)` | Load any integration DTO from JSON |
| `LiqApiDataUtil.callBasicValidate(liqAPIData)` / `basicValidate()` | Trigger input validation |
| `LiqApiDataUtil.callBasicExecute(liqAPIData)` / `basicExecute()` | Execute and return response |
| `LiqApiDataUtil.generateIdempotencyKey()` + `setIdempotencyKey(...)` | Seed POST call |
| `setMatchUpdatedTimestamp(date)` | If-Match concurrency header |
| `getAPIMessages()` / `getSuccess()` / `getResult()` | Response assertions |
| `DateUtility.getDateAsFormattedString(date, "yyyy-MM-dd HH:mm:ss.S")` | Date formatting |
| `LiqAPIDelete{BusinessObject}Integration.clazz.nonPrimitiveFieldMappings()` | Non-primitive mapping coverage |
| `LiqAPIDelete{BusinessObject}Integration.clazz.primitiveFieldMappings()` | Primitive mapping coverage |
| `liqAPiDataDelete.securityAccessSymbol()` | Verify security symbol |
| `securityFunctionParent()` / `supportsAdditionalFields()` | Security and feature flags |
| `getOperationSummary()` / `getOperationDescription()` | API metadata |

**NEVER use:**
- `Mockito.mock(...)`, `@Mock`, `mockStatic`, `spy`, `@InjectMocks`
- `PowerMock`, `byte-buddy`
- Manually instantiated `LiqAPIResponse`
- Stubbed or faked response objects

---

## Mandatory 3-Step Bootstrap (MUST appear in every integration test)

> **STRICT RULE**: Every `@Test` method in the delete test class MUST execute all three steps in the following order before performing any mutation or assertion. No step may be skipped or reordered.

### Pattern A: `invokeApiInterface` Pattern (Deal, UpfrontFee, ProductGuarantee, MISCode)

```java
// ── STEP 1: CREATE ──────────────────────────────────────────────────────
liqAPIData = getMainObjectFromJsonCreate(
    GeneralIntegrationMapping.{CREATE_ENUM}.toString(),
    LiqAPICreate{BusinessObject}Integration.class);
liqAPIData.setIdempotencyKey(LiqApiDataUtil.generateIdempotencyKey());
basicExecuteOutput = (LiqAPIResponse) this.invokeApiInterface(liqAPIData);
assertEquals("true", basicExecuteOutput.getSuccess());

// ── STEP 2: QUERY ───────────────────────────────────────────────────────
liqAPiDataQuery = getMainObjectFromJsonQuery(
    GeneralIntegrationMapping.{QUERY_ENUM}.toString(),
    LiqAPIQuery{BusinessObject}Integration.class);
liqAPiDataQuery.get{Identifier}().setIdentifierValue(
    ((LiqAPI{BusinessObject}IntegrationAsReturnValue) basicExecuteOutput.getResult()).get{IdField}());
basicExecuteQuery = (LiqAPIResponse) this.invokeApiInterface(liqAPiDataQuery);

// ── STEP 3: DELETE ──────────────────────────────────────────────────────
liqAPiDataDelete = getMainObjectFromJsonDelete(
    GeneralIntegrationMapping.{DELETE_ENUM}.toString(),
    LiqAPIDelete{BusinessObject}Integration.class);
liqAPiDataDelete.get{Identifier}().setIdentifierType("id");
liqAPiDataDelete.get{Identifier}().setIdentifierValue(
    ((LiqAPI{BusinessObject}IntegrationAsReturnValue) basicExecuteOutput.getResult()).get{IdField}());
// Set If-Match timestamp from QUERY result
((List<LiqAPI{BusinessObject}IntegrationAsReturnValue>) basicExecuteQuery.getResult()).stream().forEach(p -> {
    String dateAsFormattedString = DateUtility.getDateAsFormattedString(
        p.getUpdateTimeStamp(), "yyyy-MM-dd HH:mm:ss.S");
    liqAPiDataDelete.setMatchUpdatedTimestamp(dateAsFormattedString);
});
// ─── Now mutate the specific field under test and invoke ──────────
basicExecuteDelete = (LiqAPIResponse) this.invokeApiInterface(liqAPiDataDelete);
```

### Pattern B: `callBasicExecute` Pattern (LoanDrawdown, LoanPrincipalPayment)

```java
// ── STEP 1: CREATE ──────────────────────────────────────────────────────
liqAPIDataCreate = LiqApiDataUtil.getObjectFromJson(
    GeneralIntegrationMapping.{CREATE_ENUM}.toString(),
    LiqAPICreate{BusinessObject}Integration.class);
liqAPIDataCreate.setIdempotencyKey(LiqApiDataUtil.generateIdempotencyKey());
liqAPIDataCreate.basicValidate();
outputCreate = (LiqAPI{BusinessObject}IntegrationAsReturnValue) liqAPIDataCreate.basicExecute();
Assertions.assertNotNull(outputCreate.getLoanTransactionId());

// ── STEP 2: QUERY ───────────────────────────────────────────────────────
liqAPIDataQuery = LiqApiDataUtil.getObjectFromJson(
    GeneralIntegrationMapping.{QUERY_ENUM}.toString(),
    LiqAPIQuery{BusinessObject}Integration.class);
liqAPIDataQuery.getOutstandingTransactionIdentifier().setIdentifierValue(outputCreate.getLoanTransactionId());
LiqApiDataUtil.callBasicValidate(liqAPIDataQuery);
queryOutput = (List<LiqAPI{BusinessObject}IntegrationAsReturnValue>) LiqApiDataUtil.callBasicExecute(liqAPIDataQuery);
Assertions.assertFalse(queryOutput.isEmpty());

// ── STEP 3: DELETE ──────────────────────────────────────────────────────
liqAPIDataDelete = new LiqAPIDelete{BusinessObject}Integration();
liqAPIDataDelete.setOutstandingTransactionIdentifier(new LiqAPIOutstandingTransactionIdentifier());
liqAPIDataDelete.getOutstandingTransactionIdentifier().setIdentifierType(
    LiqAPIOutstandingTransactionIdentifier.OutstandingTransactionIdentifierType.transactionId.name());
liqAPIDataDelete.getOutstandingTransactionIdentifier().setIdentifierValue(outputCreate.getLoanTransactionId());
// ─── Now mutate the specific field under test and invoke ──────────
```

### Pattern C: Cancel-as-Delete Pattern (LoanRepricing, LoanInterestPayment)

```java
// ── STEP 1: CREATE ──────────────────────────────────────────────────────
liqAPIDataCreate = LiqApiDataUtil.getObjectFromJson(
    GeneralIntegrationMapping.{CREATE_ENUM}.toString(),
    LiqAPICreate{BusinessObject}Integration.class);
liqAPIDataCreate.setIdempotencyKey(LiqApiDataUtil.generateIdempotencyKey());
liqAPIDataCreate.basicValidate();
outputCreate = (LiqAPI{BusinessObject}IntegrationAsReturnValue) liqAPIDataCreate.basicExecute();
Assertions.assertNotNull(outputCreate.getTransactionId());

// ── STEP 2: QUERY ───────────────────────────────────────────────────────
LiqAPIQuery{BusinessObject}Integration liqAPIDataQuery = LiqApiDataUtil.getObjectFromJson(
    GeneralIntegrationMapping.{QUERY_ENUM}.toString(),
    LiqAPIQuery{BusinessObject}Integration.class);
liqAPIDataQuery.getOutstandingTransactionIdentifier().setIdentifierValue(outputCreate.getTransactionId());
LiqApiDataUtil.callBasicValidate(liqAPIDataQuery);
List<LiqAPI{BusinessObject}IntegrationAsReturnValue> queryResults =
    (List<LiqAPI{BusinessObject}IntegrationAsReturnValue>) LiqApiDataUtil.callBasicExecute(liqAPIDataQuery);
Assertions.assertFalse(queryResults.isEmpty());

// ── STEP 3: CANCEL/DELETE ────────────────────────────────────────────────
liqAPIDataCancel = (LiqAPICancel{BusinessObject}) LiqAPICancel{BusinessObject}.clazz.basicNew();
liqAPIDataCancel.setIdempotencyKey(LiqApiDataUtil.generateIdempotencyKey());
LiqAPIOutstandingTransactionIdentifier identifier = new LiqAPIOutstandingTransactionIdentifier();
identifier.setIdentifierType(
    LiqAPIOutstandingTransactionIdentifier.OutstandingTransactionIdentifierType.transactionId.name());
identifier.setIdentifierValue(outputCreate.getTransactionId());
liqAPIDataCancel.setOutstandingTransactionIdentifier(identifier);
// ─── Now mutate the specific field under test and invoke ──────────
```

### Why all three steps are mandatory

| Step | Reason |
|---|---|
| CREATE | Produces a real, owned entity in the DB. Tests must own the data they delete. |
| QUERY | Verifies the created entity exists before deletion. Provides the live `updateTimeStamp` required for If-Match header (where applicable). |
| DELETE | The operation under test. The identifier value always comes from the CREATE result, never hardcoded. |

---

## Attribute-Driven Test Generation Rules

### For Every ATTRIBUTE_FIELD_NAME in the Spreadsheet:

1. **Mandatory Primitive Fields** (Required=Y, DataType=String/Integer/Boolean/Date):
   - `testDeleteWith{FieldName}Null` — set field to null, expect failure
   - `testDeleteWith{FieldName}Empty` — set field to empty string, expect failure
   - `testDeleteWith{FieldName}Invalid` — set field to invalid value, expect failure
   - `testDeleteWith{FieldName}Valid` — set valid value, assert success

2. **Mandatory Non-Primitive Fields** (Required=Y, DataType=Object):
   - `testDeleteWithout{FieldName}` — set entire object to null, expect failure
   - `testDeleteWith{FieldName}MissingSubFields` — set object but leave required sub-fields null, expect failure
   - `testDeleteWith{FieldName}Valid` — set all sub-fields, assert success

3. **Mandatory Non-Primitive Collection Fields** (Required=Y, DataType=List):
   - `testDeleteWith{FieldName}EmptyList` — set empty list, expect failure
   - `testDeleteWith{FieldName}NullList` — set null list, expect failure
   - `testDeleteWith{FieldName}InvalidItems` — populate with invalid items, expect failure
   - `testDeleteWith{FieldName}ValidItems` — populate with valid items, assert success

4. **Optional Fields** (Required=N):
   - `testDeleteWith{FieldName}Set` — set valid value, assert success (delete proceeds)
   - `testDeleteWith{FieldName}NotSet` — omit field, assert success (field is optional)
   - `testDeleteWith{FieldName}Invalid` — set invalid value if validation exists, expect failure

5. **Code Table / Enum Fields**:
   - `testDeleteWith{FieldName}ValidCode` — set valid code table value, assert success
   - `testDeleteWith{FieldName}InvalidCode` — set code not in table, expect failure

6. **If-Match / updateTimeStamp Fields**:
   - `testDeleteWithoutIfMatch` — omit timestamp, expect concurrency failure
   - `testDeleteWithStaleIfMatch` — use stale timestamp, expect concurrency failure
   - `testDeleteWithCurrentIfMatch` — use fresh timestamp from QUERY, assert success

7. **Based on ATTRIBUTE_DESCRIPTION** — for every attribute, also create a test case that exercises the specific business rule described in the ATTRIBUTE_DESCRIPTION column.

---

## Class-Mapping Coverage Tests (mandatory in every delete test class)

```java
@Test public void testNonPrimitiveFieldMappings() {
    assertNotNull(LiqAPIDelete{BusinessObject}Integration.clazz.nonPrimitiveFieldMappings());
}
@Test public void testPrimitiveFieldMappings() {
    assertNotNull(LiqAPIDelete{BusinessObject}Integration.clazz.primitiveFieldMappings());
}
@Test public void testSecurityAccessSymbol() throws JsonProcessingException {
    LiqAPIDelete{BusinessObject}Integration data = getMainObjectFromJsonDelete(
        GeneralIntegrationMapping.{DELETE_ENUM}.toString(),
        LiqAPIDelete{BusinessObject}Integration.class);
    assertEquals("Delete{BusinessObject}Integration", data.securityAccessSymbol());
}
@Test public void testIsRest() {
    assertEquals(true, LiqAPIDelete{BusinessObject}Integration.clazz.isRest());
}
@Test public void testSecurityFunctionParent() {
    // Value depends on the business object (e.g., "Deal", "Loan", "Facility")
    assertNotNull(LiqAPIDelete{BusinessObject}Integration.clazz.securityFunctionParent());
}
@Test public void testSupportsAdditionalFields() {
    assertNotNull(LiqAPIDelete{BusinessObject}Integration.clazz.supportsAdditionalFields());
}
@Test public void testBasicNew() {
    assertNotNull(LiqAPIDelete{BusinessObject}Integration.clazz.basicNew());
}
@Test public void testGetStClass() throws JsonProcessingException {
    LiqAPIDelete{BusinessObject}Integration data = getMainObjectFromJsonDelete(
        GeneralIntegrationMapping.{DELETE_ENUM}.toString(),
        LiqAPIDelete{BusinessObject}Integration.class);
    assertNotNull(data.getStClass());
}
```

---

## Post-Delete Verification (mandatory)

Every delete test class must include post-delete verification tests:

```java
@Test
public void testDeleted{BusinessObject}IsNotQueryable() throws JsonProcessingException {
    // Full 3-step bootstrap: CREATE → QUERY → DELETE
    // ... (complete bootstrap) ...
    
    // After successful delete, attempt to query again
    // Assert: entity is no longer retrievable or returns failure
}
```

---

## Test Execution and Fix Loop

After generating all test methods:

1. **Run all tests** using the configured JUnit runner.
2. **If any test fails**, analyze the failure:
   - Compilation error → fix import, class name, or method reference
   - Runtime assertion failure → fix test logic or expected value
   - Missing resource → create JSON file or enum constant
3. **Re-run tests** after fixing.
4. **Repeat** steps 2-3 until ALL tests pass (zero failures).

---

## Report Generation

Once all tests pass, generate a report file:

**File**: `lending-delete-test-api.md`  
**Location**: Same directory as the generated test class.

Report structure:
```markdown
# Delete Test Report — {BusinessObject}

## Summary
- Business Object: {BusinessObject}
- Spreadsheet: {SpreadsheetPath}
- Test Class: LiqAPIDelete{BusinessObject}IntegrationTest
- Total Tests: {count}
- Passed: {count}
- Failed: 0

## Attribute Coverage Matrix

| Spreadsheet Attribute | Field Name | Required | Data Type | Mapping Type | Test Method(s) | Status |
|---|---|---|---|---|---|---|
| {from spreadsheet} | {fieldName} | Y/N | {type} | Primitive/NonPrimitive/Collection | testDeleteWith{Field}... | ✅ Covered |
...

## Coverage: 100%
All attributes from the Delete sheet are covered by at least one test case.
```

---

## Don'ts

- **Never skip any of the 3 steps** — every `@Test` MUST call CREATE, then QUERY, then DELETE in that exact order.
- **Never reorder the 3 steps** — do not DELETE before QUERYing, do not QUERY before CREATEing.
- **Never use a hardcoded transaction ID / identifier value** — always derive from CREATE result.
- **Never use a hardcoded `updateTimeStamp`** — always derive from QUERY result.
- No `Mockito.mock(...)`, `@Mock`, `mockStatic`, `spy`, `@InjectMocks`.
- No standalone getter/setter unit tests — each field must be exercised through a full `invokeApiInterface()` or `basicExecute()` round-trip.
- Never hand-construct response objects; always obtain from the execution call.
- Never omit `setIdempotencyKey(...)` where required (CREATE and some DELETE operations).
- Never delete data that the test does not own (i.e., was not created in the same test method).
- Never skip attributes from the spreadsheet — 100% coverage is mandatory.
- Do not generate tests that produce compilation errors — always verify class/method existence first.
