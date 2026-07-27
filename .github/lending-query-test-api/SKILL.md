---
name: lending-query-test-api
description: 'Generate JUnit5 integration test classes for any LoanIQ Query (GET) API business object. Consolidates patterns from deal-get-test, facility-get-test, loandrawdown-get-test, loaninterestpayment-get-test, loanprincipalpayment-get-test, loanrepricing-get-test, primary-get-test, upfrontfee-get-test, miscode-get-test, and additionalfields-get-test skills. Covers all spreadsheet attributes including mandatory/optional, primitive/non-primitive, and non-primitive collection mappings. NO mock-based tests.'
---

# LoanIQ Query (GET) Test — Generic Skill

> **Sample Prompt:** `/lending-query-test-api Generate query test cases for the Deal business object using the requirement spreadsheet at C:\Auto\API\Deal REST API_V1.xlsx`

---

## Required Inputs (MUST be present in prompt)

Before executing this skill, validate that the following two inputs are present in the user's prompt:

1. **Business Object** — The name of the business object (e.g., `Deal`, `Facility`, `LoanDrawdown`, `LoanInterestPayment`, `LoanPrincipalPayment`, `LoanRepricing`, `Primary`, `UpfrontFee`, `MISCode`, `AdditionalFields`)
2. **Requirement Spreadsheet Path** — The full file path to the requirement spreadsheet for the business object (e.g., `C:\Auto\API\Deal REST API_V1.xlsx`)

**If either input is missing, ASK the user to provide it before proceeding.**

Example prompt to ask the user:
```
I need the following information to generate the query test cases:
1. Business Object name (e.g., Deal, Facility, LoanDrawdown)
2. Full path to the Requirement Spreadsheet for this business object (e.g., C:\Auto\API\Deal REST API_V1.xlsx)

Please provide both.
```

---

## When to Use

Use `lending-query-test-api` when:
- Generating **Query/GET integration test classes** (JUnit5) for any LoanIQ business object
- The entity has a Query/GetByID operation defined in the requirement spreadsheet
- You need 100% test coverage for all output attributes in the Query sheet
- You need real DB-backed integration tests (no mocks)

DO NOT use this skill for:
- Generating the Query API implementation class itself (use `lending-query-api`)
- Create test classes (use `lending-create-test-api`)
- Update test classes (use `lending-update-test-api`)
- Delete test classes (use `lending-delete-test-api`)

---

## How to Run

Invoke via VS Code Copilot Chat with the skill prefix:

```text
/lending-query-test-api Generate query test cases for the {BusinessObject} business object using the requirement spreadsheet at {SpreadsheetPath}
```

Or invoked automatically by the `lending-api-developer` agent during Step 5.

---

## Output Format

The skill produces a JUnit5 test class at:

```text
FLIQ-liqjava/LoanIQ/test/com/misys/liq/api/rest/executable/{domain}/LiqAPIQuery{BusinessObject}IntegrationTest.java
```

The test class:
- Extends `BaseTestLoanIQ`
- Uses `@TestMethodOrder(OrderAnnotation.class)`
- Contains integration tests with `@Order` annotations
- Achieves 100% attribute coverage from the spreadsheet
- Generates a coverage report at `lending-query-test-api.md`

---

## Workflow Overview

This skill generates **JUnit5 integration test classes** for LoanIQ Query (GET) APIs. The generated tests:
- Extend `BaseTestLoanIQ`
- Use real DB-backed integration patterns (NO Mockito/mocks)
- Cover all attributes from the requirement spreadsheet (mandatory + optional, primitive + non-primitive + collection mappings)
- Target ~100% test coverage of the Query API class

---

## Step-by-Step Execution

### Step 1: Parse Requirement Spreadsheet

Run the spreadsheet extraction script to get attribute details:

```powershell
# Primary extraction method
.github/skills/lending-query-test-api/scripts/extract-query-attributes.ps1 -ExcelFilePath "<SpreadsheetPath>" -SheetName "GetByID"
```

**If the primary script fails** (e.g., spreadsheet is not in expected format), use the fallback script:

```powershell
# Fallback extraction — handles non-standard spreadsheet formats
.github/skills/lending-query-test-api/scripts/extract-query-attributes-fallback.ps1 -ExcelFilePath "<SpreadsheetPath>"
```

**If the fallback also fails**, use the raw reader script to dump all sheet contents and manually parse:

```powershell
# Raw reader — dumps all data for manual parsing
.github/skills/lending-query-test-api/scripts/read-spreadsheet-raw.ps1 -ExcelFilePath "<SpreadsheetPath>"
```

> **IMPORTANT**: Do NOT stop processing if the spreadsheet fails to parse initially. Try all scripts sequentially until attribute details are extracted. Continue executing the task to generate test cases.

**CRITICAL REQUIREMENT — Spreadsheet Column Reading:**

For every row in the Query/GetByID/GetById sheet of the requirement spreadsheet:
- Read **ATTRIBUTE_FIELD_NAME** column → Use as the output field name for test generation
- Read **ATTRIBUTE_DESCRIPTION** column → Use to generate description-based test cases

**Conditional Processing:** These columns are ONLY processed if:
- A "Query", "GetByID", or "GetById" sheet exists in the requirement spreadsheet
- The operation type "Query" or "Get" is valid for this business object
- If no Query sheet exists, skip Query test generation or prompt user

**Test Generation from ATTRIBUTE_DESCRIPTION:**

**CONDITIONAL LOGIC:**

1. **If ATTRIBUTE_DESCRIPTION is BLANK or EMPTY:**
   - Generate ONLY the basic JUnit test method for the attribute (assertNotNull or getter accessibility test)
   - Do NOT generate description-based test
   - Move forward to next attribute

2. **If ATTRIBUTE_DESCRIPTION has CONTENT:**
   - Generate the basic JUnit test method for the attribute
   - ADDITIONALLY generate description-based JUnit test method(s) validating:
     - Computed/calculated field logic
     - Format constraints
     - Conditional display rules
     - Code table value validation
     - Relational field consistency
     - Derived status correctness

---

### Step 2: Check for Existing Test File in Temp Folder

Check if a test file already exists in the temp folder of the current repo:

```
IntegrationAPITool/artifacts/temp_generated_class/
```

Look for: `LiqAPIQuery{BusinessObject}IntegrationTest.java`

---

### Step 3: File Location Decision

**IF the test file EXISTS in `IntegrationAPITool/artifacts/temp_generated_class/`:**
1. First update the test class with JUnit tests at this temp location.
2. Once all JUnits are generated, **move** the file to the correct FLIQ-liqjava repo location:
   ```
   C:\Users\asrivas3\git\7740_3\FLIQ-liqjava\LoanIQ\test\com\misys\liq\api\rest\executable\{domain}\
   ```
   Where `{domain}` is the lowercased business object package segment.

**IF the test file is NOT in the temp folder:**
1. Go directly to the FLIQ-liqjava repo test folder:
   ```
   C:\Users\asrivas3\git\7740_3\FLIQ-liqjava\LoanIQ\test\com\misys\liq\api\rest\executable\{domain}\
   ```
2. Check if `LiqAPIQuery{BusinessObject}IntegrationTest.java` already exists there.

**IF it exists in the FLIQ-liqjava repo test folder:**
- Generate NEW test cases **after** the existing ones (append, do not overwrite).

**IF it does NOT exist in the FLIQ-liqjava repo test folder:**
- Generate a complete new test class with all test cases.

---

### Step 4: Load Query Request Payload Template

Load the sample query request payload JSON from:
```
FLIQ-liqjava\IntegrationAPITool\artifacts\temp_generated_class\
```

Also reference templates under this skill:
```
.github/skills/lending-query-test-api/templates/
```

Use the generic query request payload template for each JUnit method.

---

### Step 5: Generate Test Cases

Generate test cases covering **ALL attributes** from the requirement spreadsheet:

#### A. Input Validation Tests (Orders 1-10)
- Test with null identifier
- Test with invalid identifier type
- Test with invalid identifier value
- Test with empty identifier value
- Test with non-existent entity ID
- Test `basicValidate()` calls

#### B. Successful Query Tests (Orders 11-25)
- Test successful query by each supported identifier type
- Test response contains all mandatory primitive fields
- Test response contains all optional primitive fields (if populated)
- Test response contains non-primitive single fields
- Test response contains non-primitive collection fields

#### C. Getter/Setter Tests (Orders 21-30)
- Test identifier getter/setter
- Test identifier persistence across mutations
- Test null handling on setter

#### D. Method Coverage Tests (Orders 31-45)
- `testSecurityAccessSymbol()`
- `testValidateLicense()`
- `testBasicNew()`
- `testGetJavaClass()`
- `testGetStClass()`
- `testGetStSuperclass()`
- `testIsRest()`
- `testNonPrimitiveFieldMappings()`
- `testPrimitiveFieldMappings()`
- `testNonPrimitiveFieldCollectionMappings()`
- `testReturnType()`
- `testDocumentedReturnValues()`
- `testSecurityFunctionParent()`
- `testSupportsAdditionalFields()`
- `testResponseClassMappings()`

#### E. Edge Cases & Error Handling (Orders 46-55+)
- Test with special characters in identifier value
- Test with very long identifier value
- Test multiple consecutive queries
- Test case-insensitive identifier type

#### F. Attribute Coverage Tests (from spreadsheet)

For EACH output attribute documented in the spreadsheet's GetByID/GetById sheet:

| Attribute Type | Test Pattern |
|---|---|
| **Mandatory Primitive** (`String`, `BigDecimal`, `Date`, `LiqDate`, `Boolean`) | `assertNotNull(output.get{AttributeName}())` |
| **Optional Primitive** | `output.get{AttributeName}()` — verify accessible (may be null) |
| **Non-Primitive Single** (nested object) | `assertNotNull(output.get{NestedObject}())` + verify sub-fields |
| **Non-Primitive Collection** (`List<SomeApiClass>`) | `assertNotNull(output.get{Collection}())` + `assertFalse(output.get{Collection}().isEmpty())` + verify elements have expected sub-fields |

#### G. ATTRIBUTE_DESCRIPTION-Based Tests (MANDATORY)

**CRITICAL REQUIREMENT:** For EACH output attribute with content in the ATTRIBUTE_DESCRIPTION column, generate an additional test case validating the rules/constraints mentioned:

| Description Type | Test Pattern |
|---|---|
| **Computed/Calculated Field** | Verify calculation logic (e.g., "Field X = Field Y + Field Z" → assert calculation is correct) |
| **Format Constraint** | Verify format (e.g., "Field must be YYYY-MM-DD" → assert date format) |
| **Conditional Display** | Verify conditional logic (e.g., "Field shown only if X=Y" → assert presence/absence based on condition) |
| **Code Table Reference** | Verify value is from valid code table (e.g., "Field uses table CODE_XXX" → assert value in valid set) |
| **Relational Field** | Verify relationship consistency (e.g., "Field references parent entity" → assert relationship exists) |
| **Derived Status** | Verify status derivation logic (e.g., "Status is 'Active' if X>0" → assert status correctness) |

---

### Step 6: Run Test Cases

After generating all test cases, run them:

```powershell
.github/skills/lending-query-test-api/scripts/run-query-tests.ps1 -TestClass "LiqAPIQuery{BusinessObject}IntegrationTest"
```

Or use the workspace JUnit Ant task with:
```
ant unittest -Ddbconfig=C:/Server7651_226/dbconfig_junit_135.ini -Djsql=... -Dls2=...
```

---

### Step 7: Fix Failing Tests

If any test cases fail:
1. Analyze the failure (compilation error, assertion failure, runtime exception).
2. Fix the failing test case.
3. Re-run the tests.
4. **Repeat Steps 7.1–7.3 until ALL test cases pass.**

Do NOT stop until all tests are green.

---

### Step 8: Generate Report

Once all test cases pass, generate the report at:
```
C:\Users\asrivas3\git\7740_3\FLIQ-liqjava\LoanIQ\test\com\misys\liq\api\rest\executable\{domain}\lending-query-test-api.md
```

The report must include:

| Column | Description |
|---|---|
| Test Name | Full method name |
| Status | PASS / FAIL |
| Spreadsheet Attribute | The attribute being tested |
| Attribute Type | Mandatory/Optional |
| Mapping Type | Primitive / Non-Primitive / Non-Primitive Collection |
| Covered | YES / NO |

Include a summary section showing:
- Total attributes in spreadsheet
- Attributes covered by tests
- Coverage percentage (target: 100%)
- Any uncovered attributes with reasons

---

## Naming Conventions

Given a business object name `{BusinessObject}`:

| Role | Naming Pattern | Example (`Deal`) |
|---|---|---|
| Query class (under test) | `LiqAPIQuery{BusinessObject}Integration` | `LiqAPIQueryDealIntegration` |
| Test class | `LiqAPIQuery{BusinessObject}IntegrationTest` | `LiqAPIQueryDealIntegrationTest` |
| Identifier class | `LiqAPI{BusinessObject}Identifier` | `LiqAPIDealIdentifier` |
| Response class | `LiqAPI{BusinessObject}IntegrationAsReturnValue` | `LiqAPIDealIntegrationAsReturnValue` |
| Create class (seed) | `LiqAPICreate{BusinessObject}Integration` | `LiqAPICreateDealIntegration` |
| Java package | `com.misys.liq.api.rest.executable.{domain}` | `com.misys.liq.api.rest.executable.deal` |
| `GeneralIntegrationMapping` prefix | `QUERY_{BUSINESS_OBJECT_UPPER}_*` | `QUERY_DEAL_INTEGRATION` |
| Security symbol | `"Query{BusinessObject}Integration"` | `"QueryDealIntegration"` |

**Rules:**
- Pascal-case for `{BusinessObject}` exactly as provided in the prompt.
- Domain (package segment) = lowercased business object name.
- `@TestMethodOrder(OrderAnnotation.class)`, `@BeforeEach`, `extends BaseTestLoanIQ` always present.

---

## Allowed APIs (Whitelist)

ONLY these helpers may appear in generated tests:

| Helper | Purpose |
|---|---|
| `getMainObjectFromJsonCreate(enum, Class)` | Bootstrap entity to query |
| `getMainObjectFromJsonQuery(enum, Class)` | Build query DTO |
| `LiqApiDataUtil.getObjectFromJson(enum, Class)` | Load DTO from JSON |
| `invokeApiInterface(liqAPIData)` | Single-commit DB round trip |
| `LiqApiDataUtil.callBasicValidate(liqAPIData)` | Trigger input validation |
| `LiqApiDataUtil.callBasicExecute(liqAPIData)` | Execute and return response |
| `LiqApiDataUtil.generateIdempotencyKey()` + `setIdempotencyKey(...)` | Seed POST |
| `setIdentifierValue(...)` / `setIdentifierType(...)` | Target identifier |
| `getAPIMessages()` / `getSuccess()` / `getResult()` | Response assertions |
| `.clazz.nonPrimitiveFieldMappings()` / `.clazz.primitiveFieldMappings()` | Mapping coverage |
| `.clazz.nonPrimitiveFieldCollectionMappings()` | Collection mapping coverage |
| `securityAccessSymbol()` | Verify security symbol |
| `basicValidate()` / `basicExecute()` | Direct method calls |
| `StringUtility.bindWith(...)` | Format error messages |
| `ErrorMessageConstants.*` | Expected error messages |
| `TestDataConstants.*` | Test data constants |

**NEVER** use: Mockito, PowerMock, byte-buddy spies, stubbed responses, `@Mock`, `@InjectMocks`.

---

## Don'ts

- No `Mockito.mock(...)`, `@Mock`, `mockStatic`, `spy`, `@InjectMocks`.
- No standalone getter/setter unit tests — each field must be exercised through a full `invokeApiInterface()` or `callBasicExecute()` round-trip.
- Never hand-construct response objects; always obtain from execution.
- Never hardcode entity identifiers — always derive from CREATE step or use `TestDataConstants`.
- Never suppress exceptions without asserting on the error message.
- Never skip the CREATE → QUERY bootstrap for positive tests (unless entity is pre-existing like AdditionalFields).
- Never omit `setIdempotencyKey(...)` when seeding data via CREATE.

---

## Scripts (under `scripts/` folder)

This skill uses the following scripts during query test generation:

| Script | Purpose |
|---|---|
| `extract-query-attributes.ps1` | Primary script to extract input/output attributes from GetByID sheet |
| `extract-query-attributes-fallback.ps1` | Fallback script for non-standard spreadsheet formats |
| `read-spreadsheet-raw.ps1` | Raw reader for manual parsing when other scripts fail |
| `run-query-tests.ps1` | Runs the generated query test class |
| `move-test-file.ps1` | Moves test file from temp folder to correct FLIQ-liqjava location |

---

## Templates (under `templates/` folder)

The `templates/` folder contains generic query request payload JSON templates used by each JUnit method. The sample query request payload JSON is also available under the temp folder:

```
FLIQ-liqjava\IntegrationAPITool\artifacts\temp_generated_class\
```

Reference these templates when generating the query request payloads for test methods.

---

## Spreadsheet Attribute Coverage Rules

The generated test class MUST cover all attributes from the requirement spreadsheet:

### CRITICAL REQUIREMENT: ATTRIBUTE_DESCRIPTION-Based Test Generation

**MANDATORY:** For every "ATTRIBUTE_FIELD_NAME" in the spreadsheet, generate an additional test case based on the "ATTRIBUTE_DESCRIPTION" column content. If the description mentions:
- Business rules → Generate a test validating that rule
- Constraints (min/max, format, pattern) → Generate a boundary/validation test
- Conditional behavior ("if X then Y") → Generate a conditional logic test
- Code table values → Generate valid/invalid value tests
- Relationships to other fields → Generate a relational validation test
- Special scenarios → Generate a scenario-specific test
- Data format requirements → Generate format validation tests

### Mandatory Attributes
- Must have at least one test asserting `assertNotNull(output.get{Attribute}())`
- Must verify the value matches what was set during CREATE (if applicable)
- Must have description-based test if ATTRIBUTE_DESCRIPTION provides additional validation rules

### Optional Attributes
- Must have at least one test verifying the getter is accessible
- If the attribute can be populated via CREATE, verify it returns the set value
- Must have description-based test if ATTRIBUTE_DESCRIPTION specifies constraints or scenarios

### Primitive Mapping (`String`, `BigDecimal`, `Date`, `LiqDate`, `Boolean`, `Integer`)
- Direct `assertNotNull` or `assertEquals` on the getter
- Verify type correctness

### Non-Primitive Mapping (single nested object)
- `assertNotNull` on the nested object getter
- Verify sub-fields of the nested object are accessible

### Non-Primitive Collection Mapping (`List<SomeApiClass>`)
- `assertNotNull` on the collection getter
- `assertFalse(collection.isEmpty())` when data exists
- Verify elements have expected sub-fields populated

---

## Query Patterns (by business object type)

### Pattern 1: Standard Entity Query (Deal, Facility, UpfrontFee)
- Uses `getMainObjectFromJsonCreate()` + `invokeApiInterface()` for CREATE seed
- Uses `getMainObjectFromJsonQuery()` + `invokeApiInterface()` for QUERY
- Response is `LiqAPIResponse` with `.getSuccess()` and `.getResult()`

### Pattern 2: Transaction Query (LoanDrawdown, LoanInterestPayment, LoanPrincipalPayment, LoanRepricing)
- Uses `LiqApiDataUtil.getObjectFromJson()` for loading
- Uses `LiqApiDataUtil.callBasicValidate()` + `LiqApiDataUtil.callBasicExecute()` or direct `basicValidate()` + `basicExecute()`
- Response is typically `List<ReturnValue>`

### Pattern 3: Polymorphic Owner Query (MISCode, AdditionalFields)
- Uses `ownerIdentifier` with `ownerType`, `ownerIdentifierType`, `ownerIdentifierValue`
- Supports multiple owner types (Deal, Facility, Outstanding)
- Response contains owner-specific data

---

## Error Handling During Spreadsheet Parsing

If the spreadsheet cannot be parsed by the primary script:

1. **Try fallback extraction script** — handles alternate column layouts
2. **Try raw reader** — dumps all content for manual inspection
3. **If all scripts fail** — manually inspect the spreadsheet structure and extract attributes from the available data
4. **NEVER stop the workflow** — always continue to generate test cases with whatever attribute information is available

---

## Test Coverage Target

The target test coverage is **~100%**. This means:
- Every public method in the Query Integration class must be tested
- Every attribute in the spreadsheet GetByID output section must have a corresponding assertion
- **MANDATORY:** Every attribute with ATTRIBUTE_DESCRIPTION must have an additional description-based test validating mentioned rules/constraints/calculations
- Every identifier type supported must have a positive test
- Every validation path must have a negative test
- All inner class methods (`basicNew`, `getJavaClass`, `getStSuperclass`, `isRest`, etc.) must be tested
- All field mapping methods must be tested
- Description-based tests validate computed fields, format constraints, conditional display logic, and relational consistency
