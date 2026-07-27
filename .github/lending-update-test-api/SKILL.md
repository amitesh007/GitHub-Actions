---
name: lending-update-test-api
description: 'Generate JUnit5 integration test class for any LoanIQ Update API business object. Consolidates all Update-test patterns into a single generic skill. Requires Business Object name and Requirement Spreadsheet path as inputs.'
---

# LoanIQ Generic Update Integration Test Skill

> **Sample Prompt:** `/lending-update-test-api Generate the Update integration test class for the UpfrontFee business object using the spreadsheet at C:\Auto\API\Upfront Fee v2.1.xlsx`

---

## Required Inputs (MANDATORY — Ask if missing)

Before proceeding with test generation, validate that BOTH of the following inputs are present in the user's prompt:

| # | Input | Description | Example |
|---|---|---|---|
| 1 | **Business Object** | The Pascal-case name of the entity | `Deal`, `Facility`, `UpfrontFee`, `LoanDrawdown`, `LoanRepricing`, `LoanInterestPayment`, `LoanPrincipalPayment`, `MISCode`, `AdditionalFields`, `Primary`, `ProductGuarantee` |
| 2 | **Requirement Spreadsheet Path** | Absolute path to the `.xlsx` file containing the Update sheet | `C:\Auto\API\Deal REST API_V1.xlsx` |

**⚠️ If either input is missing, STOP and ask the user:**

```
I need the following information to generate the Update test class:
1. Business Object name (e.g., Deal, Facility, UpfrontFee, LoanDrawdown)
2. Requirement Spreadsheet path (e.g., C:\Auto\API\Deal REST API_V1.xlsx)

Please provide both values.
```

---

## When to Use This Skill

Use `lending-update-test-api` when:
- Generating **Update integration test classes** (JUnit5) for any LoanIQ business object
- The entity has an Update operation defined in the requirement spreadsheet
- You need to cover ALL attributes (mandatory + optional, primitive + non-primitive + collections)
- You need ~100% test coverage for all attributes in the spreadsheet

DO NOT use this skill for:
- Generating the Update API implementation class itself (use `lending-update-api`)
- Create test classes (use `lending-create-test-api`)
- Query/Get test classes (use `lending-query-test-api`)
- Delete test classes (use `lending-delete-test-api`)

---

## How to Run

Invoke via VS Code Copilot Chat with the skill prefix:

```text
/lending-update-test-api Generate the Update integration test class for the {BusinessObject} business object using the spreadsheet at {SpreadsheetPath}
```

Or invoked automatically by the `lending-api-developer` agent during Step 5.

---

## Output Format

The skill produces a JUnit5 test class at:

```text
FLIQ-liqjava/LoanIQ/test/com/misys/liq/api/rest/executable/{domain}/LiqAPIUpdate{BusinessObject}IntegrationTest.java
```

The test class:
- Extends `BaseTestLoanIQ`
- Uses `@TestMethodOrder(OrderAnnotation.class)`
- Contains integration tests with `@Order` annotations
- Achieves 100% attribute coverage from the spreadsheet
- Generates a coverage report at `lending-update-test-api.md`

---

## Workflow

### Step 1: Parse Requirement Spreadsheet

Invoke the spreadsheet parsing script to extract attribute details:

```powershell
# Primary method — use run-excel-reader.ps1
.github/agents/scripts/run-excel-reader.ps1 "<SpreadsheetPath>"
```

**Output location:** `IntegrationAPITool/artifacts/temp_generated_class/`

If the script succeeds, the generated class file `LiqAPIUpdate{BusinessObject}Integration.java` will be available in the temp folder along with the Update request payload JSON template.

#### Fallback: If Spreadsheet Parsing Fails

If the primary script fails because the spreadsheet is not in the required format, **DO NOT STOP**. Use the alternative extraction scripts under the `scripts/` subfolder of this skill:

```powershell
# Fallback 1: Extract attributes using column-header detection
.github/skills/lending-update-test-api/scripts/extract-attributes-flexible.ps1 "<SpreadsheetPath>" "Update"

# Fallback 2: Extract attributes from a non-standard layout
.github/skills/lending-update-test-api/scripts/extract-attributes-alt-format.ps1 "<SpreadsheetPath>"

# Fallback 3: Manual CSV-based extraction
.github/skills/lending-update-test-api/scripts/extract-from-csv.ps1 "<SpreadsheetPath>"
```

These fallback scripts produce a normalized `attributes.json` file containing:
```json
{
  "businessObject": "EntityName",
  "attributes": [
    {
      "name": "fieldName",
      "type": "String|BigDecimal|Date|Boolean|List",
      "required": true|false,
      "updatable": true|false,
      "codeTable": "TableName or null",
      "description": "Field description",
      "isPrimitive": true|false,
      "isCollection": false|true
    }
  ]
}
```

Continue test generation using this normalized output regardless of which extraction method succeeded.

**CRITICAL REQUIREMENT — Spreadsheet Column Reading:**

For every row in the Update sheet of the requirement spreadsheet:
- Read **ATTRIBUTE_FIELD_NAME** column → Use as the field name for test generation
- Read **ATTRIBUTE_DESCRIPTION** column → Use to generate description-based test cases

**Conditional Processing:** These columns are ONLY processed if:
- The "Update" sheet exists in the requirement spreadsheet
- The operation type "Update" is valid for this business object
- If the Update sheet does not exist, skip Update test generation or prompt user

**Test Generation from ATTRIBUTE_DESCRIPTION:**

**CONDITIONAL LOGIC:**

1. **If ATTRIBUTE_DESCRIPTION is BLANK or EMPTY:**
   - Generate ONLY the basic JUnit test method for the attribute
   - Do NOT generate description-based test
   - Move forward to next attribute

2. **If ATTRIBUTE_DESCRIPTION has CONTENT:**
   - Generate the basic JUnit test method for the attribute
   - ADDITIONALLY generate description-based JUnit test method(s) validating:
     - Business rules mentioned in description
     - Constraints (min/max, format, pattern)
     - Conditional behavior ("if X then Y")
     - Code table values
     - Relationships to other fields
     - Special scenarios
     - Updatability restrictions

### Step 2: Locate or Create Test File

Follow this decision tree to determine where to generate the test class:

#### Step 2A: Check Temp Folder First

Check if a test file already exists for this business object in the temp folder:

```
IntegrationAPITool/artifacts/temp_generated_class/LiqAPIUpdate{BusinessObject}IntegrationTest.java
```

- **If the test file EXISTS in temp folder** → Proceed to Step 2B (update in temp, then move).
- **If the test file DOES NOT EXIST in temp folder** → Skip to Step 2C (check repo directly).

#### Step 2B: Update Test File in Temp Folder, Then Move

1. Open the existing test file at `IntegrationAPITool/artifacts/temp_generated_class/LiqAPIUpdate{BusinessObject}IntegrationTest.java`
2. Generate all JUnit test methods inside this file following the patterns in [example.md](references/example.md) and [Update-test-class-structure.md](references/Update-test-class-structure.md)
3. Once all JUnit tests are generated in the temp file, **move the file** to the correct FLIQ-liqjava repo location based on the business object's package:

```
C:\Users\asrivas3\git\7740_3\FLIQ-liqjava\LoanIQ\test\com\misys\liq\api\rest\executable\{domain}\LiqAPIUpdate{BusinessObject}IntegrationTest.java
```

Where `{domain}` is the lowercased business object name (e.g., `deal`, `facility`, `upfrontfee`, `loandrawdown`, etc.)

4. Proceed to Step 3.

#### Step 2C: Check Repo Test Folder Directly

If the test file was NOT found in the temp folder, check the FLIQ-liqjava repo directly:

```
C:\Users\asrivas3\git\7740_3\FLIQ-liqjava\LoanIQ\test\com\misys\liq\api\rest\executable\{domain}\LiqAPIUpdate{BusinessObject}IntegrationTest.java
```

- **If the test file EXISTS in the repo** → Open it and generate NEW test cases **after the existing ones**. Do NOT overwrite or remove existing test methods. Append new tests with `@Order` values higher than the last existing test.
- **If the test file DOES NOT EXIST in the repo** → Create a new test class file at the repo location with all test cases generated from scratch.

### Step 3: Identify Entity Type and Bootstrap Pattern

Determine which bootstrap pattern applies based on the business object:

| Entity Type | Pattern | Example Entities |
|---|---|---|
| **Standalone with Create** | CREATE → QUERY → UPDATE (3-step) | Deal, Facility, UpfrontFee, LoanDrawdown, LoanRepricing, LoanInterestPayment, LoanPrincipalPayment, Primary, ProductGuarantee |
| **Owner-based (no Create)** | QUERY → UPDATE (2-step) | MISCode, AdditionalFields |

### Step 4: Load Update Request Payload Template

Load the generic Update request payload JSON template from the `templates/` subfolder:

```
.github/skills/lending-update-test-api/templates/
```

This folder contains a generic template JSON for the Update request payload. The sample Update request payload JSONs are available at:

```
FLIQ-liqjava/IntegrationAPITool/artifacts/temp_generated_class/
```

Each JUnit test method uses this template as the base payload, then mutates specific fields for each test scenario.

### Step 5: Generate Test Class

Generate the test class following the patterns in:
- [example.md](references/example.md) — Complete code patterns for all test scenarios
- [Update-test-class-structure.md](references/Update-test-class-structure.md) — Generic class structure template

### Step 6: Validate Coverage

Ensure the generated test class covers:
- ✅ ALL mandatory attributes (positive + negative tests)
- ✅ ALL optional attributes (positive tests + boundary cases)
- ✅ **CRITICAL:** ALL attributes with ATTRIBUTE_DESCRIPTION have description-based tests validating mentioned rules/constraints/scenarios
- ✅ ALL primitive field mappings (String, BigDecimal, Date, Boolean)
- ✅ ALL non-primitive single object mappings
- ✅ ALL non-primitive collection mappings (List<> fields)
- ✅ Identifier validation tests
- ✅ If-Match timestamp validation tests
- ✅ Class-mapping coverage tests (nonPrimitiveFieldMappings, primitiveFieldMappings, securityAccessSymbol, isRest, basicNew, getJavaClass, getStSuperclass)
- ✅ Getter/Setter tests for all fields
- ✅ Invalid code table value tests
- ✅ Non-updatable field tests (verify they cannot be modified)
- ✅ Collection field tests (add, modify, remove, duplicates)
- ✅ ~100% attribute coverage from the spreadsheet

### Step 7: Run Test Cases

After all test cases are generated, **run the test class** to verify compilation and execution:

```powershell
# Run the generated test class
cd C:\Users\asrivas3\git\7740_3\FLIQ-liqjava\LoanIQ
ant unittest -Dtest.class=com.misys.liq.api.rest.executable.{domain}.LiqAPIUpdate{BusinessObject}IntegrationTest
```

Or use the VS Code JUnit test runner to execute the test class.

### Step 8: Fix Failing Test Cases

If any test cases fail:

1. **Read the error output** — Identify compilation errors or runtime assertion failures.
2. **Fix compilation issues** — Missing imports, incorrect method signatures, wrong class names, incorrect casting.
3. **Fix assertion failures** — Wrong expected values, incorrect field names, wrong enum constants.
4. **Fix logic errors** — Incorrect bootstrap pattern, missing timestamp binding, wrong identifier wiring.

**Common fixes:**
- Missing import → Add the correct import statement
- Method not found → Verify the method name against the actual Integration class
- ClassCastException → Fix the response casting (single object vs List)
- Enum constant not found → Verify `GeneralIntegrationMapping` has the referenced constant
- NullPointerException → Add null-checks or fix bootstrap wiring order

### Step 9: Re-Run and Iterate Until All Tests Pass

After fixing test cases:

1. **Re-run the test class** again.
2. **If tests still fail** → Go back to Step 8 and fix.
3. **Repeat this cycle** (fix → run → verify) until ALL test cases pass.
4. **Do NOT stop** until zero failures are reported.

> **STRICT RULE**: Keep iterating between fix and run until the test run shows 0 failures. There is no maximum iteration count — fix every failing test.

### Step 10: Generate Coverage Report

Once ALL test cases pass, generate a coverage report file:

**Report file location:** Same directory as the test class:
```
C:\Users\asrivas3\git\7740_3\FLIQ-liqjava\LoanIQ\test\com\misys\liq\api\rest\executable\{domain}\lending-update-test-api.md
```

**Report format:**

```markdown
# Update Test API Coverage Report — {BusinessObject}

## Test Execution Summary

| Metric | Value |
|---|---|
| Business Object | {BusinessObject} |
| Test Class | LiqAPIUpdate{BusinessObject}IntegrationTest |
| Total Tests | {count} |
| Passed | {count} |
| Failed | 0 |
| Spreadsheet | {SpreadsheetPath} |
| Generated Date | {date} |

## Test Case Results

| # | Test Method | Status | Category |
|---|---|---|---|
| 1 | testUpdateWithoutIdentifierValue | ✅ PASS | Identifier Validation |
| 2 | testUpdateWithNonExistentEntity | ✅ PASS | Identifier Validation |
| ... | ... | ... | ... |

## Attribute Coverage Matrix

| # | Attribute (from Spreadsheet) | Type | Required | Updatable | Test Coverage | Test Method(s) |
|---|---|---|---|---|---|---|
| 1 | {attributeName} | {type} | Y/N | Y/N | ✅ Covered / ❌ Not Covered | testMethod1, testMethod2 |
| ... | ... | ... | ... | ... | ... | ... |

## Coverage Summary

| Category | Total | Covered | Coverage % |
|---|---|---|---|
| Mandatory Attributes | {n} | {n} | {%} |
| Optional Attributes | {n} | {n} | {%} |
| Primitive Fields | {n} | {n} | {%} |
| Non-Primitive Fields | {n} | {n} | {%} |
| Collection Fields | {n} | {n} | {%} |
| Class Mapping Tests | 9 | {n} | {%} |
| **Overall** | **{n}** | **{n}** | **{%}** |

## Uncovered Attributes (if any)

| # | Attribute | Reason |
|---|---|---|
| — | — | All attributes covered |
```

The report MUST:
- List every test method with its pass/fail status
- Map every attribute from the spreadsheet to its corresponding test method(s)
- Clearly indicate which attributes are covered and which are not
- Show overall coverage percentage
- Be generated ONLY after all tests pass (Step 9 complete)

---

## Class Name Convention

Given a business object name `{BusinessObject}`:

| Role | Naming Pattern | Example (`UpfrontFee`) |
|---|---|---|
| Integration (request) class | `LiqAPIUpdate{BusinessObject}Integration` | `LiqAPIUpdateUpfrontFeeIntegration` |
| Test class | `LiqAPIUpdate{BusinessObject}IntegrationTest` | `LiqAPIUpdateUpfrontFeeIntegrationTest` |
| Identifier class | `LiqAPI{BusinessObject}Identifier` | `LiqAPIUpfrontFeeIdentifier` |
| Response (return value) class | `LiqAPI{BusinessObject}IntegrationAsReturnValue` | `LiqAPIUpfrontFeeIntegrationAsReturnValue` |
| Create class (bootstrap seed) | `LiqAPICreate{BusinessObject}Integration` | `LiqAPICreateUpfrontFeeIntegration` |
| Query class (bootstrap fetch) | `LiqAPIQuery{BusinessObject}Integration` | `LiqAPIQueryUpfrontFeeIntegration` |
| Java package | `com.misys.liq.api.rest.executable.{domain}` | `com.misys.liq.api.rest.executable.upfrontfee` |
| `GeneralIntegrationMapping` update prefix | `UPDATE_{BUSINESS_OBJECT_UPPER}_*` | `UPDATE_UPFRONTFEE_TRANSACTION_INTEGRATION` |

**Rules:**
- Use Pascal-case for `{BusinessObject}` exactly as provided in the prompt.
- The domain (package segment) is the lowercased, no-separator form of the business object name.
- For `GeneralIntegrationMapping` enum constants, convert to `SCREAMING_SNAKE_CASE`.
- The `@TestMethodOrder`, `@BeforeEach`, and `extends BaseTestLoanIQ` annotations are always present.

---

## Allowed APIs (whitelist)

Only these helpers may appear in generated tests:

| Helper | Purpose |
|---|---|
| `getMainObjectFromJsonCreate(enum, Class)` | Bootstrap entity to update |
| `getMainObjectFromJsonQuery(enum, Class)` | Re-fetch to read current `updateTimeStamp` for If-Match |
| `getMainObjectFromJsonUpdate(enum, Class)` | Build the update DTO |
| `LiqApiDataUtil.getObjectFromJson(enum, Class)` | Load any integration DTO from JSON |
| `invokeApiInterface(liqAPIData)` | Single-commit DB round trip |
| `LiqApiDataUtil.callBasicValidate(liqAPIData)` / `basicValidate()` | Trigger input validation |
| `LiqApiDataUtil.callBasicExecute(liqAPIData)` / `basicExecute()` | Execute and return response |
| `LiqApiDataUtil.generateIdempotencyKey()` + `setIdempotencyKey(...)` | POST seed call |
| `LiqApiDataUtil.getUpdatedTimestampFromQuery(list, getter)` | Extract updateTimeStamp from query result |
| `setMatchUpdatedTimestamp(date)` | If-Match concurrency header |
| `setIdentifierValue(...)` on identifier | Target the just-created entity |
| `getAPIMessages()` / `getSuccess()` / `getResult()` | Response assertions |
| `LiqAPIUpdate{Entity}Integration.clazz.nonPrimitiveFieldMappings()` / `primitiveFieldMappings()` / `nonPrimitiveFieldCollectionMappings()` | Mapping coverage |
| `securityAccessSymbol()` | Verify security symbol |
| `DateUtility.getDateAsFormattedString(date, format)` | Date formatting |
| `setParents()` | Required for parent linkage (LoanDrawdown etc.) |

**NEVER** use Mockito, PowerMock, byte-buddy spies, or stubbed `LiqAPIResponse` instances.

---

## Attribute Coverage Rules

### CRITICAL REQUIREMENT: ATTRIBUTE_DESCRIPTION-Based Test Generation

**MANDATORY:** For every "ATTRIBUTE_FIELD_NAME" in the spreadsheet, you MUST generate an additional test case based on the "ATTRIBUTE_DESCRIPTION" column content. If the description mentions:
- Business rules → Generate a test validating that rule
- Constraints (min/max, format, pattern) → Generate a boundary/validation test
- Conditional behavior ("if X then Y") → Generate a conditional logic test
- Code table values → Generate valid/invalid value tests
- Relationships to other fields → Generate a relational validation test
- Special scenarios → Generate a scenario-specific test
- Updatability restrictions → Generate a non-updatable field test

### Mandatory Attributes (Required=Y)

For every mandatory attribute in the spreadsheet:
1. **Positive test** — set a valid value, assert success
2. **Null test** — set `null`, assert failure with appropriate error message
3. **Invalid value test** — set an invalid value (for code table fields), assert failure
4. **Description-based test** — if ATTRIBUTE_DESCRIPTION specifies additional rules, generate corresponding validation test

### Optional Attributes (Required=N or CR)

For every optional attribute in the spreadsheet:
1. **Positive test** — set a valid value, assert success
2. **Boundary test** — test edge cases (empty string, max length, etc.)
3. **Description-based test** — if ATTRIBUTE_DESCRIPTION specifies constraints or scenarios, generate corresponding test

### Primitive Field Mapping

For fields mapped as primitive types (`String`, `BigDecimal`, `Date`, `Boolean`, `LiqDate`):
- Use `primitiveFieldMappings()` method on the inner Class
- Each field gets a `LiqAPIViewPrimitiveFieldMapping` entry with correct `logicalFieldName`

| Java Type | Logical Field Name |
|-----------|-------------------|
| BigDecimal (monetary) | `"amount"` |
| BigDecimal (rate) | `"fxRate"` |
| Date / LiqDate | `"date"` |
| String (ID) | `"id"` |
| String (code) | field name itself (e.g., `"branchCode"`, `"currencyCode"`) |
| String (text/comment) | `"description"` |

### Non-Primitive Single Object Mapping

For complex object fields (not collections):
- Use `nonPrimitiveFieldMappings()` method on the inner Class
- Each field gets a `LiqAPINonPrimitiveFieldMapping` entry with `setFieldName()` and `setFieldApiClass()`

### Non-Primitive Collection Mapping

For `List<>` fields annotated with `@LiqAPIFieldMapper`:
- Use `nonPrimitiveFieldCollectionMappings()` method on the inner Class
- Each field gets a `LiqAPINonPrimitiveFieldMapping` entry with `setFieldName()` matching the instance variable and `setFieldApiClass()` matching the element type's `.clazz`
- Tests MUST cover: add item, modify item, remove item, duplicate detection

---

## Compilation Safety Rules

To avoid compilation errors in generated test classes:

1. **Import all referenced classes** — Generate full import statements for every class used
2. **Use correct generics** — `List<LiqAPI{Entity}IntegrationAsReturnValue>` not raw `List`
3. **Handle checked exceptions** — All test methods declare `throws JsonProcessingException`
4. **Use correct assertion imports** — `import static org.junit.jupiter.api.Assertions.*`
5. **Match method signatures** — Verify getter/setter names match the actual Integration class
6. **Use correct enum constants** — Verify `GeneralIntegrationMapping` constants exist before referencing
7. **Cast response correctly** — Use proper casting for `getResult()` based on return type (single object vs List)
8. **Logger type** — Use either `LoggerFactory.getLogger()` (SLF4J) or `LogManager.getLogger()` (Log4j2) consistently

---

## Scripts

This skill uses scripts located in the `scripts/` subfolder to assist in test generation:

| Script | Purpose | When to Use |
|---|---|---|
| `extract-attributes-flexible.ps1` | Extract attributes with flexible column-header detection | When primary parser fails on non-standard format |
| `extract-attributes-alt-format.ps1` | Extract attributes from alternative spreadsheet layout | When spreadsheet uses different sheet naming or column layout |
| `extract-from-csv.ps1` | Extract attributes from CSV export of spreadsheet | Last resort when .xlsx parsing fails entirely |
| `generate-test-skeleton.ps1` | Generate test class skeleton from normalized attributes | After attributes are extracted successfully |
| `validate-coverage.ps1` | Validate that all spreadsheet attributes have test coverage | Final verification step |

### Script Invocation Order

1. Try primary parser: `.github/agents/scripts/run-excel-reader.ps1`
2. If fails → Try: `.github/skills/lending-update-test-api/scripts/extract-attributes-flexible.ps1`
3. If fails → Try: `.github/skills/lending-update-test-api/scripts/extract-attributes-alt-format.ps1`
4. If fails → Try: `.github/skills/lending-update-test-api/scripts/extract-from-csv.ps1`
5. Once attributes are extracted → Run: `.github/skills/lending-update-test-api/scripts/generate-test-skeleton.ps1`
6. After test class generated → Run: `.github/skills/lending-update-test-api/scripts/validate-coverage.ps1`

---

## Templates

The `templates/` subfolder contains:

| File | Purpose |
|---|---|
| `generic-update-request.json` | Generic template JSON for Update request payload |

The sample Update request payload JSON is available at:
```
FLIQ-liqjava/IntegrationAPITool/artifacts/temp_generated_class/
```

This template is used as the base JSON for each test method's `getMainObjectFromJsonUpdate()` call. Each test mutates specific fields from this base template.

---

## Test Coverage Target: ~100%

The generated test class MUST achieve approximately 100% attribute coverage:

- Every attribute listed in the Update sheet of the spreadsheet has at least one dedicated test
- **MANDATORY:** Every attribute with an ATTRIBUTE_DESCRIPTION has an additional test case validating the rules/constraints mentioned in the description
- Every mandatory attribute has both positive and negative (null/invalid) tests
- Every code-table backed attribute has an invalid-value test
- Every collection field has add/modify/remove/duplicate tests
- Class metadata tests are always included (mappings, security, basicNew, etc.)
- Getter/Setter unit tests for all fields on the Integration class
- Non-updatable fields verified to be unchanged after update attempt
- Description-based tests cover all business rules, constraints, and scenarios mentioned in ATTRIBUTE_DESCRIPTION column

---

## File Placement

```
LoanIQ/
  test/
    com/misys/liq/api/rest/
      executable/
        {domain}/
          LiqAPIUpdate{Entity}IntegrationTest.java    ← Update integration tests
      data/
        {domain}/
          LiqAPI{Entity}IntegrationAsReturnValueTest.java  ← Return value unit tests (if applicable)
```

---

## References

- Common coding instructions: `.github/instructions/lending-api-instructions.md`
- Update API implementation skill: `.github/skills/lending-update-api/SKILL.md`
- Example patterns: `.github/skills/lending-update-test-api/references/example.md`
- Class structure template: `.github/skills/lending-update-test-api/references/Update-test-class-structure.md`
- Scripts: `.github/skills/lending-update-test-api/scripts/`
- Templates: `.github/skills/lending-update-test-api/templates/`
