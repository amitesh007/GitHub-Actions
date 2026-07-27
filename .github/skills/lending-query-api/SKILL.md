---
name: lending-query-api
description: 'Generate LoanIQ Query API classes for any supported business object and modify methods based on repository skill rules.'
---

# LoanIQ Query API — Unified Skill

> **Note:** For workflow instructions on how to generate and modify Query API classes, refer to the `lending-api-developer` agent's Workflow section. This document focuses on technical patterns, class structures, and implementation rules.

---

## When to Use This Skill

Use the `lending-query-api` skill when:
- Generating **Query** operation API classes for ANY supported LoanIQ entity
- The entity supports Query operations (validated by script generation)
- Implementing GET endpoints that retrieve existing entities
- Performing read-only operations without data modification
- Querying entities by identifier (id, name, alias, etc.)

DO NOT use this skill for:
- Create operations (use `lending-create-api`)
- Update operations (use `lending-update-api`)
- Delete operations (use `lending-delete-api`)

---

## Supported Business Objects

This skill covers Query API generation for ALL of the following business objects:

| # | Business Object | Domain Package | Source Class |
|---|----------------|----------------|--------------|
| 1 | AdditionalFields | additionalfields | `LiqAPIQueryAdditionalFieldsIntegration.java` |
| 2 | OutgoingDDAMessage | cashflow | `LiqAPIQueryOutgoingDDAMessageIntegration.java` |
| 3 | Deal | deal | `LiqAPIQueryDealIntegration.java` |
| 4 | Facility | facility | `LiqAPIQueryFacilityIntegration.java` |
| 5 | FacilityInterestPricing | facility | `LiqAPIQueryFacilityInterestPricingIntegration.java` |
| 6 | ProductGuarantee | guarantee | *(to be generated)* |
| 7 | HolidayCalendarCode | holidaycalendar | `LiqAPIQueryHolidayCalendarCodeIntegration.java` |
| 8 | HolidayCalendarDate | holidaycalendar | `LiqAPIQueryHolidayCalendarDateIntegration.java` |
| 9 | MISCode | miscode | `LiqAPIQueryMISCodeIntegration.java` |
| 10 | OutgoingACHMessage | cashflow | `LiqAPIQueryOutgoingACHMessageIntegration.java` |
| 11 | OutgoingBOJMessage | cashflow | `LiqAPIQueryOutgoingBOJMessageIntegration.java` |
| 12 | OutgoingIMTMessage | cashflow | `LiqAPIQueryOutgoingIMTMessageIntegration.java` |
| 13 | OutgoingISOMessage | cashflow | `LiqAPIQueryOutgoingISOMessageIntegration.java` |
| 14 | OutgoingMTMessage | cashflow | `LiqAPIQueryOutgoingMTMessageIntegration.java` |
| 15 | OutgoingZenginMessage | cashflow | `LiqAPIQueryOutgoingZenginMessageIntegration.java` |
| 16 | Circle | circle | `LiqAPIQueryCircleIntegration.java` |
| 17 | FXRate | fxrate | *(to be generated)* |
| 18 | FlexUnscheduledTransaction | outstanding | *(to be generated)* |
| 19 | LoanDrawdown | outstanding/drawdown | `LiqAPIQueryLoanDrawdownIntegration.java` |
| 20 | LoanIncrease | outstanding/increase | `LiqAPIQueryLoanIncreaseIntegration.java` |
| 21 | LoanInterestPayment | outstanding/interest | `LiqAPIQueryLoanInterestPaymentIntegration.java` |
| 22 | DiscountLoanDrawdown | outstanding/discountloandrawdown | `LiqAPIQueryDiscountLoanDrawdownIntegration.java` |
| 23 | LoanPrincipalPayment | outstanding/principal | `LiqAPIQueryLoanPrincipalPaymentIntegration.java` |
| 24 | QuickLoanRepricing | outstanding/qlr | `LiqAPIQueryQuickLoanRepricingIntegration.java` |
| 25 | SBLCDecrease | outstanding/sblc | `LiqAPIQuerySBLCDecreaseIntegration.java` |
| 26 | SBLCIncrease | outstanding/sblc | `LiqAPIQuerySBLCIncreaseIntegration.java` |
| 27 | UnscheduledLoanPrincipalPayment | outstanding | *(to be generated)* |
| 28 | SBLCIssuance | sblc | `LiqAPIQuerySBLCIssuanceIntegration.java` |
| 29 | SBLCFacingFeePayment | sblcfeepayment | `LiqAPIQuerySBLCFacingFeePaymentIntegration.java` |
| 30 | SBLCIssuanceFeePayment | sblcfeepayment | `LiqAPIQuerySBLCIssuanceFeePaymentIntegration.java` |
| 31 | FacilityOngoingFee | slmb | `LiqAPIQueryFacilityOngoingFeeIntegration.java` |
| 32 | FacilityOngoingFeePayment | slmb | `LiqAPIQueryFacilityOngoingFeePaymentIntegration.java` |
| 33 | UpfrontFee | upfrontfee | `LiqAPIQueryUpfrontFeeIntegration.java` |
| 34 | UserProfile | user | `LiqAPIQueryUserProfileIntegration.java` |
| 35 | UserSecurityProfile | user | `LiqAPIQueryUserSecurityProfileIntegration.java` |

All source classes reside under: `LoanIQ/srcgen/com/misys/liq/api/rest/executable/{domain}/`

---

## Common Coding Instructions

**⚠️ IMPORTANT:** Before implementing Query API classes, review the common coding standards:

📖 **[LoanIQ REST API Common Instructions](.github/instructions/lending-api-instructions.md)**

This document covers:
- Package structure
- Class structure (Integration, ReturnValue, Inner Class)
- Field mapping methods (mandatory 3-method pattern)
- Common methods (basicValidate, basicExecute, response, securityAccessSymbol)
- Annotation usage (@LiqAPIFieldMapper)
- Import standards
- Error handling patterns
- Javadoc standards
- JSON structure patterns
- Testing standards

The patterns in this SKILL.md are **Query-specific additions** to the common instructions above.

---

## How to Run

This skill is invoked by the `lending-api-developer` agent during Step 4 (Modify Generated Classes Per Skill). It is not run as a standalone script.

```text
@lending-api-developer generate Query API for {EntityName} using file path {ExcelFilePath}
```

The agent loads this skill and applies its patterns to the baseline classes generated by `run-excel-reader.ps1`.

---

## Output Format

The skill produces a production-ready Java class at:

```text
FLIQ-liqjava/LoanIQ/srcgen/main/java/com/misys/liq/api/rest/executable/{domain}/LiqAPIQuery{EntityName}Integration.java
```

With supporting return value class:

```text
FLIQ-liqjava/LoanIQ/srcgen/main/java/com/misys/liq/api/rest/data/{domain}/LiqAPI{EntityName}IntegrationAsReturnValue.java
```

---

## Prerequisites

- Access to PowerShell script `run-excel-reader.ps1` at `.github/agents/scripts/run-excel-reader.ps1`
- JAR file available at `.github/agents/scripts/artifacts/executable/IntegrationAPITool-1.0-exec.jar`
- Requirement spreadsheet with Query operation definitions
- Active LoanIQ repository branch with write access
- Access to skill reference files: `references/example.md` and `scripts/generate-query-api.ps1`

---

## Workflow

Follow these numbered steps in order to generate and implement LoanIQ Query API classes for **any** supported business object:

### 1. Check for Existing Generated Class

First, check if `LiqAPIQuery{EntityName}Integration.java` already exists at the output path:

```
FLIQ-liqjava/IntegrationAPITool/artifacts/temp-generated_class/
```

**Decision tree:**

```
┌─ Does LiqAPIQuery{EntityName}Integration.java exist at output path?
│
├── YES → Use it as the base context. Proceed to Step 2.
│         Apply the Query skill rules on the existing base code to add
│         any missing methods, annotations, Javadoc, or field mappings.
│         Do NOT regenerate the class — only augment what is missing.
│
└── NO  → Check prerequisites:
          ┌─ Is the Business Object name known AND is a requirement
          │  spreadsheet path available?
          │
          ├── YES → Generate the base class by executing:
          │         .github\agents\scripts\run-excel-reader.ps1 "<spreadsheet-path>"
          │         Then verify the class appeared at output path.
          │         If generated successfully, proceed to Step 2.
          │         If NOT generated (entity does not support Query), STOP.
          │
          └── NO  → **STOP processing.**
                    Cannot proceed without either an existing generated class
                    or the prerequisite inputs (business object + spreadsheet).
                    Inform the user that the following are required:
                      • Business Object name (from the Supported Business Objects table)
                      • Path to the requirement spreadsheet (.xlsx)
```

**Summary of Step 1 outcomes:**
| Condition | Action |
|-----------|--------|
| Class exists at output path | Use as base context, apply skill to augment |
| Class absent + BO name + spreadsheet available | Run script to generate, then apply skill |
| Class absent + prerequisites missing | STOP — request missing inputs from user |

### 2. Identify Query Pattern

Determine which pattern applies to the entity being generated (see **Specialized Query Patterns** section below):

| Pattern | When to Use | Example Entities |
|---------|------------|------------------|
| **Standard Entity** | Top-level entities with direct ID lookup | Deal, Facility, Circle, HolidayCalendarCode, HolidayCalendarDate, UserProfile, UserSecurityProfile |
| **Polymorphic Owner** | Entities belonging to different owner types | MISCode, AdditionalFields |
| **Transaction** | Outstanding transaction types | LoanDrawdown, LoanIncrease, LoanInterestPayment, DiscountLoanDrawdown, LoanPrincipalPayment, QuickLoanRepricing, SBLCDecrease, SBLCIncrease, UnscheduledLoanPrincipalPayment |
| **Cashflow Message** | Outgoing message types | OutgoingDDAMessage, OutgoingACHMessage, OutgoingBOJMessage, OutgoingIMTMessage, OutgoingISOMessage, OutgoingMTMessage, OutgoingZenginMessage |
| **Fee/Payment** | Fee and payment transactions | UpfrontFee, SBLCIssuance, SBLCFacingFeePayment, SBLCIssuanceFeePayment, FacilityOngoingFee, FacilityOngoingFeePayment |
| **Lazy Loading** | Entities resolved through identifier | Facility, FacilityInterestPricing |

### 3. Apply Method Implementations

**Standardized Method Names:** All Query API classes MUST use the following exact method names regardless of entity type. Do NOT rename these methods to match the entity (e.g., do NOT use `getMisCodes()`, `getUpfrontFee()`, `getDeals()`, etc.):

| Method | Purpose |
|--------|---------|
| `basicValidate()` | Identifier validation |
| `validateLicense()` | License validation (typically returns `this`) |
| `basicExecute()` | Entity retrieval — calls `getTransaction()` and passes result to ReturnValue |
| `getTransaction()` | Fetches the entity object or list of entity objects |
| `securityAccessSymbol()` | Returns the security access constant |

Implement the following methods with complete logic (no stubs):

- **`basicValidate()`** — Identifier validation
  - Validate identifier is not null
  - Call `identifier.basicValidate()`
  - Throw appropriate exception if validation fails
  - No field validation needed (read-only operation)

- **`validateLicense()`** — License validation
  - Typically returns `this` (no license validation for Query)

- **`basicExecute()`** — Entity retrieval logic
  - Call `getTransaction()` method to retrieve entity
  - Return `LiqAPI{EntityName}IntegrationAsReturnValue.clazz.forQuery(getTransaction())`
  - For polymorphic entities, pass owner context: `forQuery(getTransaction(), ownerIdentifier)`
  - No locking required (read-only operation)

- **`getTransaction()`** — Entity fetching (standardized name for ALL entity types)
  - **Always name this method `getTransaction()`** — never `get{Entity}()`, `getMisCodes()`, etc.
  - Fetch entity using the appropriate identifier's getter (e.g., `identifier.getUpfrontFee()`, `{Entity}.clazz.getForId(identifier.getIdentifierValue())`)
  - Handle `NullPointerException` → call `identifier.throwInvalidIdentifierException()` or `identifier.throwInvalidTxnException()`
  - Call `setIds()` with entity IDs
  - Call `loadObjects()` to fully populate entity
  - Return entity object or list of entity objects

- **`securityAccessSymbol()`** — Access control
  - Return security constant: `"Query{EntityName}Integration"`
  - Implement in both main class and inner Class

### 4. Modify Return Value Class

If `LiqAPI{EntityName}IntegrationAsReturnValue` was generated:

- **Implement `forQuery()` static method**:
  - Signature: `public static LiqAPI{EntityName}IntegrationAsReturnValue forQuery(List<{Entity}> entities)`
  - Or for polymorphic: `public static LiqAPI{EntityName}IntegrationAsReturnValue forQuery(List<{Entity}> entities, KeyedDataObject owner)`
  - Map entity fields from business object to return value object
  - Call `queryMessage()` to populate fields
  - Return populated return value object

- **Implement `queryMessage()` method**:
  - Signature: `public LiqAPI{EntityName}IntegrationAsReturnValue queryMessage(LiqAPI{EntityName}IntegrationAsReturnValue t, {EntityType} bo)`
  - Or for polymorphic: `public LiqAPI{EntityName}IntegrationAsReturnValue queryMessage(LiqAPI{EntityName}IntegrationAsReturnValue t, {EntityType} bo, LiqAPIOwnerIdentifier ownerIdentifier)`
  - This method MUST set values from the passed entity business object onto ALL instance variables of the ReturnValue class — primitives, non-primitives, and non-primitive collections.
  - Follow patterns in `references/example.md`

  **Field mapping rules for `queryMessage()`:**

  **1. Primitive fields** — For each primitive/simple-type instance variable (`String`, `BigDecimal`, `Date`, `LiqDate`, `Boolean`, etc.) in the ReturnValue class:

  ```java
  public LiqAPI{EntityName}IntegrationAsReturnValue queryMessage(
          LiqAPI{EntityName}IntegrationAsReturnValue t, {EntityType} bo) {
      t.setEffectiveDate((({EntityClass})bo).zz_effectiveDate() != null ? (({EntityClass})bo).zz_effectiveDate() : null);
      t.setBranchCode((({EntityClass})bo).getBranchCode() != null ? (({EntityClass})bo).getBranchCode() : null);
      t.setAmount((({EntityClass})bo).zz_actualAmount() != null ? (({EntityClass})bo).zz_actualAmount().getAmount() : null);
      t.setCurrencyCode((({EntityClass})bo).getCurrencyCode());
      t.setUpdateTimeStamp((({EntityClass})bo).getUpdateTimeStamp());

      // Conditional primitive mappings
      if (SomeLicenseEnhancement.clazz.isLicensed()) {
          t.setFxRate((({EntityClass})bo).getFXRate());
      }
  ```

  **2. Non-primitive collection fields** — For each `List<SomeApiClass>` field annotated with `@LiqAPIFieldMapper`:

  ```java
      t.setBorrowerIdentifiers(borrowerIdentifiers(bo));
      t.setOwnerIdentifiers(ownerIdentifiers(bo));
  ```

  Each helper method:
  ```java
  private List<LiqAPI{Collection}Identifier> {collectionName}({EntityType} bo) {
      List<LiqAPI{Collection}Identifier> result = new ArrayList<>();
      // Iterate, map, add
      return result;
  }
  ```

  **3. Non-primitive single fields** — For each single non-primitive field:

  ```java
      t.setServicingGroup(servicingGroup(bo));
      return t;
  }
  ```

  **Summary — `queryMessage()` must map ALL ReturnValue attributes:**
  | Attribute Type | How to Map | Example |
  |---------------|------------|--------|
  | Primitive (`String`, `BigDecimal`, `Date`, `LiqDate`) | Direct getter with null checks | `t.setAmount(...)` |
  | Non-primitive collection (`List<SomeApiClass>`) | Private helper method | `t.setBorrowerIdentifiers(borrowerIdentifiers(bo))` |
  | Non-primitive single (`SomeApiClass`) | Private helper method | `t.setServicingGroup(servicingGroup(bo))` |

- **Implement inner `Class` field mapping methods**:

  #### `primitiveFieldMappings()`
  Maps primitive/simple-type instance variables. Each field gets a `LiqAPIViewPrimitiveFieldMapping` entry:

  ```java
  public List primitiveFieldMappings() {
      List<LiqAPIViewPrimitiveFieldMapping> list = super.primitiveFieldMappings();
      LiqAPIViewPrimitiveFieldMapping t1 = (LiqAPIViewPrimitiveFieldMapping) LiqAPIViewPrimitiveFieldMapping.clazz.newStObject();
      t1.setFieldName("amount");
      t1.setLogicalFieldName("amount");
      list.add(t1);
      return list;
  }
  ```

  **Logical field name mapping rules:**
  | Java Type | Logical Field Name |
  |-----------|-------------------|
  | BigDecimal (monetary) | `"amount"` |
  | BigDecimal (rate) | `"fxRate"` |
  | Date / LiqDate | `"date"` |
  | String (ID) | `"id"` |
  | String (code) | field name itself (e.g., `"branchCode"`, `"currencyCode"`) |
  | String (text/comment) | `"description"` |

  #### `nonPrimitiveFieldMappings()`
  Maps single non-primitive instance variables:

  ```java
  public List nonPrimitiveFieldMappings() {
      List t = super.nonPrimitiveFieldMappings();
      LiqAPINonPrimitiveFieldMapping t1 = (LiqAPINonPrimitiveFieldMapping) LiqAPINonPrimitiveFieldMapping.clazz.newStObject();
      t1.setFieldName("servicingGroup");
      t1.setFieldApiClass(LiqAPIServicingGroupIntegration.clazz);
      t.add(t1);
      return t;
  }
  ```

  #### `nonPrimitiveFieldCollectionMappings()`
  Maps List/Collection non-primitive instance variables:

  ```java
  public List nonPrimitiveFieldCollectionMappings() {
      List t = super.nonPrimitiveFieldCollectionMappings();
      LiqAPINonPrimitiveFieldMapping t1 = (LiqAPINonPrimitiveFieldMapping) LiqAPINonPrimitiveFieldMapping.clazz.newStObject();
      t1.setFieldName("feeDetails");
      t1.setFieldApiClass(LiqAPIData.clazz);
      t.add(t1);
      return t;
  }
  ```

  **How to determine which method a field belongs to:**
  | Field Declaration | Mapping Method |
  |-------------------|---------------|
  | `public String fieldName;` | `primitiveFieldMappings()` |
  | `public BigDecimal fieldName;` | `primitiveFieldMappings()` |
  | `public Date fieldName;` | `primitiveFieldMappings()` |
  | `public LiqDate fieldName;` | `primitiveFieldMappings()` |
  | `public Boolean fieldName;` | `primitiveFieldMappings()` |
  | `public SomeApiClass fieldName;` (single object) | `nonPrimitiveFieldMappings()` |
  | `public List<SomeApiClass> fieldName;` (collection) | `nonPrimitiveFieldCollectionMappings()` |

- **⚠️ MANDATORY IMPLEMENTATION** — The three field mapping methods MUST be implemented in BOTH:
  1. **Integration API classes** — In the static inner `Class` of `LiqAPIQuery{EntityName}Integration`
  2. **Return Value classes** — In the static inner `Class` of `LiqAPI{EntityName}IntegrationAsReturnValue`

- **Repository Class References** — When implementing field mappings, refer to existing classes as examples:
  - `LiqAPIQueryDealIntegration.java` — Full example with nested collections
  - `LiqAPIQueryFacilityIntegration.java` — Complex field mappings with identifiers
  - `LiqAPIQueryUpfrontFeeIntegration.java` — Primitive and non-primitive field patterns
  - `LiqAPIQueryMISCodeIntegration.java` — Polymorphic owner pattern

  **⚠️ CAVEAT:** Do NOT reference existing repository classes with the SAME name as the entity you are generating. Reference OTHER entity classes instead.

### 5. Generate Test Class

Create comprehensive test coverage (see **Test Implementation Patterns** below):

- **Validation tests** (Order 1-10)
- **Successful query tests** (Order 11-25)
- **Getter/setter tests** (Order 21-30)
- **Method coverage tests** (Order 31-45)
- **Edge cases** (Order 46-55+)

### 6. Generate JSON Examples

- **Request**: Include entity identifier — save as `LiqAPIQuery{EntityName}IntegrationRequestExample.json`
- **Response**: Include complete entity data — save as `LiqAPIQuery{EntityName}IntegrationResponseExample.json`

### 7. Generate Javadoc

Add comprehensive Javadoc to all generated classes (excluding test classes):
- Class-level with `@see` and `@since`
- Method-level with `@param`, `@return`, `@throws`
- Field-level for `@LiqAPIFieldMapper` annotated fields
- Inner Class Javadoc

---

## Scripts

This skill relies on centralized script execution via the `lending-api-developer` agent:

- **Script Location:** `.github/agents/scripts/run-excel-reader.ps1`
- **JAR Dependency:** `.github/agents/scripts/artifacts/executable/IntegrationAPITool-1.0-exec.jar`
- **Purpose:** Generates baseline API classes for **all three operations** (Create, Update, Query) from Excel requirement spreadsheets
- **Output Path:** `FLIQ-liqjava/IntegrationAPITool/artifacts/temp-generated_class/`

**Manual Execution (for testing):**
```powershell
.github\agents\scripts\run-excel-reader.ps1 "C:\path\to\requirement.xlsx"
```

**Skill-specific script:** `scripts/generate-query-api.ps1` — validates source file presence and resolves the correct domain package for a given business object.

---

## How to Use This Skill

1. **Check output path first** — Look for `LiqAPIQuery{Entity}Integration.java` at `FLIQ-liqjava/IntegrationAPITool/artifacts/temp-generated_class/`. If present, use it as the base context and apply the skill rules to augment any missing code (methods, annotations, Javadoc, field mappings). Do NOT recreate from scratch.

2. **Generate if not present** — If no class exists at the output path, verify that the Business Object name and requirement spreadsheet path are available. If both are available, execute `.github\agents\scripts\run-excel-reader.ps1` with the spreadsheet path to generate the base class. If prerequisites are missing, **STOP** and request them from the user.

3. **Do not generate `LiqAPIQuery{Entity}Integration.java` manually** — If the script does not produce a Query class for the entity, it means the entity does not support Query operation. Do not create one manually.

4. **Modify `LiqAPI{EntityName}IntegrationAsReturnValue`** if generated by the script. Implement `forQuery()` and `queryMessage()` per patterns.

5. **Modify generated test classes** per the Test Implementation Patterns section below.

6. **Copy modified classes to the repository** under the correct package path:
```
FLIQ-liqjava/LoanIQ/
├── srcgen/
│   └── main/java/com/misys/liq/api/rest/executable/{domain}/
│       └── LiqAPIQuery{EntityName}Integration.java
│   └── main/java/com/misys/liq/api/rest/data/{domain}/
│       └── LiqAPI{EntityName}IntegrationAsReturnValue.java
└── test/com/misys/liq/api/rest/executable/{domain}/
    └── LiqAPIQuery{EntityName}IntegrationTest.java
```

7. **Delete from** `FLIQ-liqjava\IntegrationAPITool\artifacts\temp-generated_class\` after copy.

8. **Do not change existing classes** — only add newly generated classes.

---

## Specialized Query Patterns

### Pattern 1: Standard Entity (Deal, Circle, HolidayCalendar, UserProfile)

For top-level entities identified by a single identifier:

```java
public class LiqAPIQuery{EntityName}Integration extends LiqAPIExecutableData implements StObject {

    @LiqAPIFieldMapper(name = "{Entity}Identifier",
        className = "com.misys.liq.api.rest.data.{domain}.LiqAPI{Entity}Identifier")
    public LiqAPI{Entity}Identifier {entity}Identifier;

    public void basicValidate() {
        if (Objects.nonNull(get{Entity}Identifier())) {
            get{Entity}Identifier().basicValidate();
        } else {
            ExceptionUtility.throwException(new LiqError(
                Messages.liqNlsExternalizedMessage("{Entity} Identifier is required."), this));
        }
    }

    public Object basicExecute() {
        return LiqAPI{Entity}IntegrationAsReturnValue.clazz.forQuery(getTransaction());
    }

    private List<{Entity}> getTransaction() {
        List<{Entity}> entities = null;
        try {
            entities = List.of(({Entity}) {Entity}.clazz.getForId(
                get{Entity}Identifier().getIdentifierValue()));
        } catch (NullPointerException e) {
            get{Entity}Identifier().throwInvalidIdentifierException();
        } catch (Exception ex) {
            LOG.error("Unable to fetch {Entity}: {}",
                get{Entity}Identifier().getIdentifierValue(), ex.getMessage(), ex);
            ExceptionUtility.throwException(new LiqError(
                Messages.liqNlsExternalizedMessage("Unable to fetch {Entity}. ")
                    .concat(ex.getMessage()), ex));
        }
        if (CollectionUtils.isNotEmpty(entities) && Objects.nonNull(entities.get(0))) {
            setIds(entities.stream().map({Entity}::getId).collect(Collectors.toList()));
            return loadObjects(entities);
        } else {
            get{Entity}Identifier().throwInvalidIdentifierException();
        }
        return entities;
    }

    public String securityAccessSymbol() {
        return "Query{Entity}Integration";
    }
}
```

**Applies to:** Deal, Circle, HolidayCalendarCode, HolidayCalendarDate, UserProfile, UserSecurityProfile, FXRate

---

### Pattern 2: Polymorphic Owner (MISCode, AdditionalFields)

For entities belonging to different owner types:

```java
public class LiqAPIQuery{EntityName}Integration extends LiqAPIExecutableData implements StObject {

    @LiqAPIFieldMapper(name = "OwnerIdentifier",
        className = "com.misys.liq.api.rest.data.identifier.LiqAPIOwnerIdentifier")
    public LiqAPIOwnerIdentifier ownerIdentifier;

    KeyedDataObject bo;

    public void basicValidate() {
        if (ownerIdentifier != null) {
            ownerIdentifier.basicValidate();
        } else {
            ExceptionUtility.throwException(new LiqError(
                Messages.liqNlsExternalizedMessage("Owner Identifier is required."), this));
        }
    }

    public Object basicExecute() {
        return LiqAPI{Entity}IntegrationAsReturnValue.clazz.forQuery(getTransaction(), ownerIdentifier);
    }

    private List<{Entity}> getTransaction() {
        List<{Entity}> entities = null;
        bo = getBusinessObject();
        if (Objects.nonNull(bo)) {
            entities = bo.get{Entity}List();
        }
        try {
            if (CollectionUtils.isNotEmpty(entities)) {
                entities = loadObjects(entities);
            }
        } catch (Exception ex) {
            ExceptionUtility.throwException(new LiqError(
                Messages.liqNlsExternalizedMessage("Unable to fetch entities. ")
                    .concat(ex.getMessage()), ex));
        }
        setIds(entities.stream().map(e -> e.getId()).collect(Collectors.toList()));
        return entities;
    }

    public KeyedDataObject getBusinessObject() {
        if (ownerIdentifier != null) {
            if (ownerIdentifier.getOwnerType().equalsIgnoreCase("DEA")) {
                return ownerIdentifier.getDeal();
            } else if (ownerIdentifier.getOwnerType().equalsIgnoreCase("FAC")) {
                return ownerIdentifier.getFacility();
            }
        }
        return bo;
    }
}
```

**Applies to:** MISCode, AdditionalFields

---

### Pattern 3: Transaction Identifier (Loan Outstanding Transactions)

For outstanding transactions:

```java
public class LiqAPIQuery{EntityName}Integration extends LiqAPIExecutableData implements StObject {

    @LiqAPIFieldMapper(name = "OutstandingTransactionIdentifier",
        className = "com.misys.liq.api.data.outstanding.LiqAPIOutstandingTransactionIdentifier")
    public LiqAPIOutstandingTransactionIdentifier outstandingTransactionIdentifier;

    public void basicValidate() {
        if (getOutstandingTransactionIdentifier() != null) {
            outstandingTransactionIdentifier.basicValidate();
        } else {
            ExceptionUtility.throwException(new LiqError(
                Messages.liqNlsExternalizedMessage("Outstanding Transaction Identifier is required."), this));
        }
    }

    public Object basicExecute() {
        return LiqAPI{Entity}IntegrationAsReturnValue.clazz.forQuery(getTransaction());
    }

    private List<{TransactionType}> getTransaction() {
        List<{TransactionType}> transactions = null;
        try {
            transactions = List.of(({TransactionType}) {TransactionType}.clazz.getForId(
                outstandingTransactionIdentifier.getLoanTransactionId()));
        } catch (NullPointerException e) {
            outstandingTransactionIdentifier.throwInvalidTxnException();
        } catch (Exception ex) {
            ExceptionUtility.throwException(new LiqError(
                Messages.liqNlsExternalizedMessage("Unable to fetch Outstanding Transaction. ")
                    .concat(ex.getMessage()), ex));
        }
        if (CollectionUtils.isNotEmpty(transactions) && Objects.nonNull(transactions.get(0))) {
            setIds(transactions.stream().map(t -> t.getId()).collect(Collectors.toList()));
            return loadObjects(transactions);
        }
        return transactions;
    }

    public String securityAccessSymbol() {
        return "Query{Entity}Integration";
    }

    public static class Class extends LiqAPIExecutableData.Class implements StClass {
        // ... standard inner class methods ...

        public String securityFunctionParent() {
            return "Loan";
        }

        public Boolean supportsAdditionalFields() {
            return false;
        }

        public List<LiqAPIReturnData> getReturnType() {
            List<LiqAPIReturnData> list = new ArrayList<>();
            list.add(LiqAPIReturnData.getInstance(LiqAPI{Entity}IntegrationAsReturnValue.clazz, false));
            return list;
        }

        public List documentedReturnValues() {
            return CollectionUtility.listWith(LiqAPI{Entity}IntegrationAsReturnValue.clazz);
        }
    }
}
```

**Applies to:** LoanDrawdown, LoanIncrease, LoanInterestPayment, DiscountLoanDrawdown, LoanPrincipalPayment, QuickLoanRepricing, SBLCDecrease, SBLCIncrease, UnscheduledLoanPrincipalPayment

---

### Pattern 4: Cashflow Message (Outgoing Messages)

For outgoing message types with direct ID field (no annotated identifier):

```java
public class LiqAPIQuery{MessageType}Integration extends LiqAPIExecutableData implements StObject {

    public String {messageId}OutId;
    public {MessageEntity} outgoing{MessageType};

    public void basicValidate() {
        validate{MessageType}();
    }

    private void validate{MessageType}() {
        if (!StringUtility.isNilOrBlank({messageId}OutId)) {
            this.setOutgoing{MessageType}(({MessageEntity}) {MessageEntity}.clazz.getForId({messageId}OutId));
            if (null == this.getOutgoing{MessageType}()) {
                ExceptionUtility.throwException(new LiqError(
                    Messages.liqNlsExternalizedMessage(
                        String.format(ErrorMessageConstants.INVALID_OUTGOING_MESSAGE, {messageId}OutId)), this));
            }
            // Validate associated cashflow
            if (null == Cashflow.clazz.getForId(this.getOutgoing{MessageType}().getCashflowId())) {
                ExceptionUtility.throwException(new LiqError(
                    Messages.liqNlsExternalizedMessage(
                        String.format(ErrorMessageConstants.INVALID_CASHFLOW, {messageId}OutId)), this));
            }
        }
    }

    public Object basicExecute() {
        return LiqAPI{MessageType}IntegrationAsReturnValue.clazz.forQuery(loadObject(this.getOutgoing{MessageType}()));
    }

    public static class Class extends LiqAPIExecutableData.Class implements StClass {
        public List primitiveFieldMappings() {
            List<LiqAPIViewPrimitiveFieldMapping> list = super.primitiveFieldMappings();
            list.add(createViewMapping("{messageId}OutId", "id", true));
            return list;
        }

        public String securityFunctionParent() {
            return SECURITY_ACCESS_SYMBOL_CASHFLOW;
        }
    }
}
```

**Applies to:** OutgoingDDAMessage, OutgoingACHMessage, OutgoingBOJMessage, OutgoingIMTMessage, OutgoingISOMessage, OutgoingMTMessage, OutgoingZenginMessage

---

### Pattern 5: Lazy Loading (Facility, FacilityInterestPricing)

For entities where the identifier itself resolves the entity:

```java
public class LiqAPIQuery{EntityName}Integration extends LiqAPIExecutableData implements StObject {

    @LiqAPIFieldMapper(name = "{Entity}Identifier",
        className = "com.misys.liq.api.data.{domain}.LiqAPI{Entity}Identifier")
    public LiqAPI{Entity}Identifier {entity}Identifier;

    public {Entity} {entity};

    public {Entity} get{Entity}() {
        if ({entity} == null && {entity}Identifier != null) {
            try {
                {entity} = {entity}Identifier.get{Entity}();
            } catch (Exception exception) {
                LOG.error("Unable to fetch {Entity}: {}",
                    {entity}Identifier.getIdentifierValue(), exception);
                {entity}Identifier.throwInvalidIdentifierException();
            }
        }
        return {entity};
    }

    public Object basicExecute() {
        return (LiqAPIData) LiqAPI{Entity}IntegrationAsReturnValue.clazz.forQuery(
            {entity}Identifier.get{Entity}());
    }
}
```

**Applies to:** Facility, FacilityInterestPricing

---

### Pattern 6: Fee/Payment (UpfrontFee, SBLC Fees, Ongoing Fees)

For fee and payment entities with custom identifier resolution:

```java
public class LiqAPIQuery{EntityName}Integration extends LiqAPIExecutableData implements StObject {

    @LiqAPIFieldMapper(name = "{Entity}Identifier",
        className = "com.misys.liq.api.rest.data.{domain}.LiqAPI{Entity}Identifier")
    public LiqAPI{Entity}Identifier {entity}Identifier;

    public Object basicExecute() {
        return LiqAPI{Entity}IntegrationAsReturnValue.clazz.forQuery(getTransaction());
    }

    public List<Transaction> getTransaction() {
        List<Transaction> transactions = null;
        try {
            {EntityType} entity = get{Entity}Identifier().get{Entity}();
            transactions = List.of(entity);
        } catch (NullPointerException e) {
            {entity}Identifier.throwInvalidTxnException();
        } catch (Exception ex) {
            ExceptionUtility.throwException(new LiqError(
                Messages.liqNlsExternalizedMessage("Unable to fetch {Entity}. ")
                    .concat(ex.getMessage()), ex));
        }
        if (Objects.nonNull(transactions) && !transactions.isEmpty()) {
            transactions = loadObjects(transactions);
        }
        if (transactions != null) {
            setIds(transactions.stream().map(Transaction::getId).collect(Collectors.toList()));
        }
        return transactions;
    }
}
```

**Applies to:** UpfrontFee, SBLCIssuance, SBLCFacingFeePayment, SBLCIssuanceFeePayment, FacilityOngoingFee, FacilityOngoingFeePayment, ProductGuarantee, FlexUnscheduledTransaction

---

## Standard Class Structure

### Class Inheritance

All Query APIs extend directly from `LiqAPIExecutableData`:

```java
public class LiqAPIQuery{EntityName}Integration extends LiqAPIExecutableData implements StObject {

    private static final Logger LOG = org.apache.logging.log4j.LogManager.getLogger(
        LiqAPIQuery{EntityName}Integration.class);

    public static final Class clazz = new Class();

    static {
        StClassRegistry.register(clazz);
    }

    public StClass getStClass() {
        return clazz;
    }

    // ... fields, methods, inner class
}
```

### Inner Class Pattern

```java
public static class Class extends LiqAPIExecutableData.Class implements StClass {
    protected Class() {}

    public StObject basicNew() {
        return new LiqAPIQuery{EntityName}Integration();
    }

    public java.lang.Class getJavaClass() {
        return LiqAPIQuery{EntityName}Integration.class;
    }

    public StClass getStSuperclass() {
        return LiqAPIExecutableData.clazz;
    }

    public String securityAccessSymbol() {
        return "Query{Entity}Integration";
    }

    public List nonPrimitiveFieldMappings() {
        List mappings = super.nonPrimitiveFieldMappings();
        LiqAPINonPrimitiveFieldMapping t1 = (LiqAPINonPrimitiveFieldMapping)
            LiqAPINonPrimitiveFieldMapping.clazz.newStObject();
        t1.setFieldName("{entity}Identifier");
        t1.setFieldApiClass(LiqAPI{Entity}Identifier.clazz);
        mappings.add(t1);
        return mappings;
    }

    public List primitiveFieldMappings() {
        return super.primitiveFieldMappings();
    }

    public boolean isRest() {
        return true;
    }
}
```

### Extended Inner Class (Transaction APIs)

```java
public static class Class extends LiqAPIExecutableData.Class implements StClass {
    // ... standard methods ...

    public String securityFunctionParent() {
        return "Loan"; // or "Deal", "Facility", "Cashflow"
    }

    public Boolean supportsAdditionalFields() {
        return false;
    }

    public List<LiqAPIReturnData> getReturnType() {
        List<LiqAPIReturnData> list = new ArrayList<>();
        list.add(LiqAPIReturnData.getInstance(LiqAPI{Entity}IntegrationAsReturnValue.clazz, false));
        return list;
    }

    public List documentedReturnValues() {
        return CollectionUtility.listWith(LiqAPI{Entity}IntegrationAsReturnValue.clazz);
    }
}
```

---

## Test Implementation Patterns

### Test Class Organization

| Category | Order Range | Purpose | Key Tests |
|----------|-------------|---------|-----------|
| **Validation Tests** | 1-10 | Input validation, null checks | testQueryWithoutIdentifier, testQueryWithInvalidIdentifierValue, testQueryNonExistentEntity |
| **Successful Query Tests** | 11-25 | Happy path scenarios | testSuccessfulQueryById, testQueryReturnsCorrectEntityData |
| **Getter/Setter Tests** | 21-30 | Field accessors | testIdentifierGetterSetter, testSetIdentifierWithNull |
| **Method Coverage Tests** | 31-45 | Inner class methods | testSecurityAccessSymbol, testBasicNew, testNonPrimitiveFieldMappings |
| **Edge Cases** | 46-55+ | Special scenarios | testQueryWithSpecialCharacters, testCaseInsensitiveIdentifierType |

### Test Class Template

```java
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LiqAPIQuery{Entity}IntegrationTest extends BaseTestLoanIQ {

    private static final Logger LOG = LoggerFactory.getLogger(
        LiqAPIQuery{Entity}IntegrationTest.class);

    private LiqAPIQuery{Entity}Integration liqAPIDataQuery;
    private LiqAPIResponse basicExecuteQuery;

    @BeforeEach
    public void setUp() {
        LOG.debug("Setting up test for LiqAPIQuery{Entity}Integration");
        Properties props = System.getProperties();
        props.setProperty("RestServices", "Y");
        liqAPIDataQuery = new LiqAPIQuery{Entity}Integration();
    }
}
```

### Key Test Requirements

✅ **Must Have**:
- `@TestMethodOrder(OrderAnnotation.class)` at class level
- `@Order(n)` on each test method
- Extends `BaseTestLoanIQ`
- Uses `invokeApiInterface()` for API calls
- Uses `getMainObjectFromJsonQuery()` for JSON loading
- SLF4J Logger with debug statements
- Tests both positive and negative scenarios
- Tests all identifier types (ID, NAME if applicable)
- Tests getter/setter methods
- Tests inner class methods

❌ **Must Not**:
- Hardcode entity IDs
- Skip negative test scenarios
- Omit logging statements
- Test methods without @Order annotation

---

## Key Differences Between Query and Other APIs

| Aspect | Query API | Update API | Create API |
|--------|-----------|------------|------------|
| **Identifier Type** | Single identifier | List of identifiers | N/A |
| **Timestamp** | Not required | matchUpdatedTimestamp required | N/A |
| **Operation** | Read-only | Write operation | Write operation |
| **Validation** | Identifier only | Identifier + field validation | Full field validation |
| **Return Type** | Entity data as-is | Updated entity + timestamp | Created entity ID |
| **Security** | Query{Entity}Integration | Update{Entity}Integration | Create{Entity}Integration |
| **Base Class** | Always LiqAPIExecutableData | May extend intermediate | LiqAPIExecutableData |

---

## Best Practices

### Error Handling
- Use specific exception types (NullPointerException for invalid ID)
- Call identifier's `throwInvalidIdentifierException()`
- Log errors with context before throwing
- Use externalized messages

### Validation
- Always validate identifier is not null first
- Delegate to identifier's validation method
- Add business-specific validations when needed

### Entity Loading
- Always call `loadObjects()` after fetching
- Call `setIds()` before `loadObjects()`
- Check for null/empty results

### Security
- Define `securityAccessSymbol()` in both class and inner Class
- Use consistent naming: `"Query{Entity}Integration"`
- Add `securityFunctionParent()` for hierarchical security

### Logging
- Define static Logger field
- Log errors before throwing exceptions
- Include identifier value in log messages

---

## Troubleshooting

| Issue | Resolution |
|---|---|
| Script did not generate Query class | Entity does not support Query — check Supported Business Objects table |
| Compilation error: Cannot find symbol | Verify identifier class imports and base class `LiqAPIExecutableData` is on classpath |
| `getTransaction()` returns null | Verify identifier value exists in database; check `getForId()` or `getLoanTransactionId()` returns valid entity |
| `NullPointerException` in `getTransaction()` | Identifier is valid but entity not found — ensure `throwInvalidIdentifierException()` is called in catch block |
| `loadObjects()` throws exception | Entity may have lazy-loaded collections that fail — check database connectivity and entity state |
| Test class not generated | Create test class manually using the Test Class Template above |
| Missing `forQuery()` method | Implement static method in ReturnValue class per Step 4 |
| Polymorphic owner returns wrong entity | Verify `ownerIdentifier.getOwnerType()` matches expected values (DEA, FAC) in `getBusinessObject()` |
| Security access denied | Ensure `securityAccessSymbol()` returns correct value in both main class and inner Class |
