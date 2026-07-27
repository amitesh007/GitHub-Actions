---
name: lending-update-api
description: 'Unified skill for generating LoanIQ Update API Integration classes for ALL business objects. Covers patterns, field mappings, class hierarchy, basicExecute flows, locking, security, timestamp validation, and return values across all 33 entity domains.'
---

# LoanIQ Update API — Unified Skill

> **Purpose:** Generate **Update API Integration classes** for any LoanIQ business object.
> This unified skill consolidates all entity-specific patterns (Deal, Facility,
> outstanding transactions, SBLC, fee payments, cashflow messages, user admin, etc.) into a single reference.

---

## When to Use This Skill

Use the `lending-update-api` skill when:
- Generating **Update** operation API classes for ANY LoanIQ entity
- Implementing PATCH/PUT endpoints that modify existing entities
- Updating existing entities with optimistic locking (timestamp validation)
- Performing partial field updates on entities
- Working with any of the 33 supported business objects listed below

DO NOT use this skill for:
- Create operations (use `lending-create-api`)
- Query/Read operations (use `lending-query-api`)
- Delete operations (out of scope)

---

## Supported Business Objects

| Business Object | Integration Class | Package (under `executable.`) | Pattern | Extends |
|---|---|---|---|---|
| AdditionalFields | `LiqAPIUpdateAdditionalFieldsIntegration` | `additionalfields` | A | `LiqAPIExecutableData` |
| Deal | `LiqAPIUpdateDealIntegration` | `deal` | B | `LiqAPIUpdateDeal` |
| Facility | `LiqAPIUpdateFacilityIntegration` | `facility` | B | `LiqAPIUpdateFacility` |
| FacilityInterestPricing | `LiqAPIUpdateFacilityInterestPricingIntegration` | `facility` | B | `LiqAPIUpdateFacilityInterestPricing` |
| FacilityOngoingFee | `LiqAPIUpdateFacilityOngoingFeeIntegration` | `slmb` | B | `LiqAPIFacilityOngoingFeeIntegration` |
| FacilityOngoingFeePayment | `LiqAPIUpdateFacilityOngoingFeePaymentIntegration` | `slmb` | B | `LiqAPIUpdateOngoingFeePayment` |
| FlexUnscheduledTransaction | `LiqAPIUpdateFlexUnscheduledTransactionIntegration` | `outstanding` | A | `LiqAPIExecutableData` |
| FundingRate | `LiqAPIUpdateFundingRateIntegration` | `fundingrate` | A | `LiqAPIExecutableData` |
| HolidayCalendarCode | `LiqAPIUpdateHolidayCalendarCodeIntegration` | `holidaycalendar` | A | `LiqAPIExecutableData` |
| HolidayCalendarDate | `LiqAPIUpdateHolidayCalendarDateIntegration` | `holidaycalendar` | A | `LiqAPIExecutableData` |
| LoanDrawdown | `LiqAPIUpdateLoanDrawdownIntegration` | `outstanding.drawdown` | B | `LiqAPIUpdateLoanDrawdown` |
| LoanIncrease | `LiqAPIUpdateLoanIncreaseIntegration` | `outstanding.increase` | B | `LiqAPIUpdateLoanIncrease` |
| LoanInterestPayment | `LiqAPIUpdateLoanInterestPaymentIntegration` | `outstanding.interest` | B | `LiqAPIUpdateLoanInterestPayment` |
| LoanPrincipalPayment | `LiqAPIUpdateLoanPrincipalPaymentIntegration` | `outstanding.principal` | B | `LiqAPIUpdateLoanPrincipalPayment` |
| MISCode | `LiqAPIUpdateMISCodeIntegration` | `miscode` | A | `LiqAPIExecutableData` |
| OutgoingACHMessage | `LiqAPIUpdateOutgoingACHMessageIntegration` | `cashflow` | A | `LiqAPIExecutableData` |
| OutgoingBOJMessage | `LiqAPIUpdateOutgoingBOJMessageIntegration` | `cashflow` | A | `LiqAPIExecutableData` |
| OutgoingDDAMessage | `LiqAPIUpdateOutgoingDDAMessageIntegration` | `cashflow` | A | `LiqAPIExecutableData` |
| OutgoingIMTMessage | `LiqAPIUpdateOutgoingIMTMessageIntegration` | `cashflow` | A | `LiqAPIExecutableData` |
| OutgoingISOMessage | `LiqAPIUpdateOutgoingISOMessageIntegration` | `cashflow` | A | `LiqAPIExecutableData` |
| OutgoingMTMessage | `LiqAPIUpdateOutgoingMTMessageIntegration` | `cashflow` | A | `LiqAPIExecutableData` |
| OutgoingZenginMessage | `LiqAPIUpdateOutgoingZenginMessageIntegration` | `cashflow` | A | `LiqAPIExecutableData` |
| ProductGuarantee | `LiqAPIUpdateProductGuaranteeIntegration` | `guarantor` | A | `LiqAPIExecutableData` |
| QuickLoanRepricing | `LiqAPIUpdateQuickLoanRepricingIntegration` | `outstanding.qlr` | B | `LiqAPIUpdateQuickRepricing` |
| SBLCDecrease | `LiqAPIUpdateSBLCDecreaseIntegration` | `outstanding.sblc` | B | `LiqAPIUpdateSBLCIncreaseDecrease` |
| SBLCFacingFeePayment | `LiqAPIUpdateSBLCFacingFeePaymentIntegration` | `sblcfeepayment` | B | `LiqAPIAbstractExecutableSBLCFeePayment` |
| SBLCIncrease | `LiqAPIUpdateSBLCIncreaseIntegration` | `outstanding.sblc` | B | `LiqAPIUpdateSBLCIncreaseDecrease` |
| SBLCIssuance | `LiqAPIUpdateSBLCIssuanceIntegration` | `sblc` | B | `LiqAPIUpdateSBLCIssuance` |
| SBLCIssuanceFeePayment | `LiqAPIUpdateSBLCIssuanceFeePaymentIntegration` | `sblcfeepayment` | B | `LiqAPIUpdateSBLCFeePayment` |
| UnscheduledLoanPrincipalPayment | `LiqAPIUpdateUnscheduledLoanPrincipalPaymentIntegration` | `outstanding` | B | `LiqAPIUpdateUnscheduledLoanPrincipalPayment` |
| UpfrontFee | `LiqAPIUpdateUpfrontFeeIntegration` | `upfrontfee` | B | `LiqAPIAbstractUpfrontFeeIntegration` |
| UserProfile | `LiqAPIUpdateUserProfileIntegration` | `user` | B | `LiqAPIUpdateUserProfile` |
| UserSecurityProfile | `LiqAPIUpdateUserSecurityProfileIntegration` | `user` | B | `LiqAPIUpdateUserSecurityProfile` |

---

## Common Coding Instructions

**⚠️ IMPORTANT:** Before implementing Update API classes, review the common coding standards:

📖 **[LoanIQ REST API Common Instructions](.github/instructions/lending-api-instructions.md)**

The patterns in this SKILL.md are **Update-specific additions** to the common instructions above.

---

## How to Run

This skill is invoked by the `lending-api-developer` agent during Step 4 (Modify Generated Classes Per Skill). It is not run as a standalone script.

```text
@lending-api-developer generate Update API for {EntityName} using file path {ExcelFilePath}
```

The agent loads this skill and applies its patterns to the baseline classes generated by `run-excel-reader.ps1`.

---

## Output Format

The skill produces a production-ready Java class at:

```text
FLIQ-liqjava/LoanIQ/srcgen/main/java/com/misys/liq/api/rest/executable/{domain}/LiqAPIUpdate{EntityName}Integration.java
```

With supporting return value class:

```text
FLIQ-liqjava/LoanIQ/srcgen/main/java/com/misys/liq/api/rest/data/{domain}/LiqAPI{EntityName}IntegrationAsReturnValue.java
```

---

## Prerequisites

- Access to PowerShell script `run-excel-reader.ps1` at `.github/agents/scripts/run-excel-reader.ps1`
- JAR file available at `.github/agents/scripts/artifacts/executable/IntegrationAPITool-1.0-exec.jar`
- Requirement spreadsheet with Update operation definitions
- Active LoanIQ repository branch with write access
- Access to skill reference files: `references/example.md`

---

## Class Inheritance Patterns

### Pattern A: Direct Extension from LiqAPIExecutableData

Use when **no intermediate base class** exists. The Integration class directly extends `LiqAPIExecutableData`.

```java
public class LiqAPIUpdate{EntityName}Integration extends LiqAPIExecutableData 
    implements IAPIRestIntegration, StObject {
    
    public static final Class clazz = new Class();
    
    // @LiqAPIFieldMapper annotations for non-primitive fields
    @LiqAPIFieldMapper(name = "OwnerIdentifier", className = "...")
    public LiqAPIOwnerIdentifier ownerIdentifier;
    
    // Primitive fields
    public String field1;
    public LiqDate field2;
    
    static { StClassRegistry.register(clazz); }
    public StClass getStClass() { return clazz; }
}
```

**Pattern A Entities:** AdditionalFields, FlexUnscheduledTransaction, FundingRate, HolidayCalendarCode, HolidayCalendarDate, MISCode, OutgoingACHMessage, OutgoingBOJMessage, OutgoingDDAMessage, OutgoingIMTMessage, OutgoingISOMessage, OutgoingMTMessage, OutgoingZenginMessage, ProductGuarantee

### Pattern B: Extension from Intermediate Base Class

Use when an **intermediate base class** exists (`LiqAPIUpdate{Entity}`, `LiqAPIAbstract{Entity}Integration`).

```java
public class LiqAPIUpdate{EntityName}Integration extends LiqAPIUpdate{EntityName} 
    implements IAPIRestIntegration {
    
    public static final Class clazz = new Class();
    
    // Integration-specific fields on top of inherited ones
    @LiqAPIFieldMapper(name = "EntityIdentifier", className = "...")
    public LiqAPIEntityIdentifier entityIdentifier;
    
    static { StClassRegistry.register(clazz); }
    public StClass getStClass() { return clazz; }
}
```

**Pattern B Entities:** Deal, Facility, FacilityInterestPricing, FacilityOngoingFee, FacilityOngoingFeePayment, LoanDrawdown, LoanIncrease, LoanInterestPayment, LoanPrincipalPayment, QuickLoanRepricing, SBLCDecrease, SBLCFacingFeePayment, SBLCIncrease, SBLCIssuance, SBLCIssuanceFeePayment, UnscheduledLoanPrincipalPayment, UpfrontFee, UserProfile, UserSecurityProfile

---

## Entity-Specific Field Mappings

### Deal
| @LiqAPIFieldMapper Field | Mapper Class |
|---|---|
| `MISCodeIntegration` | `LiqAPIMISCodeIntegration` |
| `DealAdminAgent` | `LiqAPIDealAdminAgentIntegration` |
| `DealIdentifier` | `LiqAPIDealIdentifier` |
| `ProjectDetails` | `LiqAPIProjectDetailsIntegration` |
| `AdditionalFields` | `LiqAPIAdditionalFieldIntegration` |

**Primitive:** `productType` (String)

### Facility
| @LiqAPIFieldMapper Field | Mapper Class |
|---|---|
| `SublimitsIntegration` | `LiqAPIFacilitySublimitIntegration` |
| `affiliateMappingDetails` | `LiqAPIAffiliateMappingDetailsIntegration` |
| `feePricing` | `LiqAPIFeePricingIntegration` |
| `FacilityHostBankPortfolioShare` | `LiqAPIFacilityPortfolioShareIntegration` |

### FacilityInterestPricing
| @LiqAPIFieldMapper Field | Mapper Class |
|---|---|
| `FacilityIdentifier` | `LiqAPIFacilityIdentifier` |
| `InterestPricing` | `LiqAPIInterestPricingIntegration` |

### LoanDrawdown
| @LiqAPIFieldMapper Field | Mapper Class |
|---|---|
| `OutstandingTransactionIdentifier` | `LiqAPIOutstandingTransactionIdentifier` |
| `FacilityIdentifier` | `LiqAPIFacilityIdentifier` |
| `OutstandingIdentifier` | `LiqAPIOutstandingIdentifier` |
| `CustomerIdentifier` | `LiqAPICustomerIdentifier` |
| `SpreadAdjustmentComponentOverrideIntegration` | `LiqAPISpreadAdjustmentComponentOverrideIntegration` |

**Primitive:** `interestRateIsFloating` (Boolean), `sourceRefNum` (String), `systemSourceId` (String), `repricingFrequencyApplies` (Boolean), `racRate` (BigDecimal)

### QuickLoanRepricing
| @LiqAPIFieldMapper Field | Mapper Class |
|---|---|
| `SpreadAdjustmentComponentOverrideIntegration` | `LiqAPISpreadAdjustmentComponentOverrideIntegration` |
| `cofPricingFormula` | `LiqAPICOFPricingFormulaIntegration` |
| `OutstandingTransactionIdentifier` | `LiqAPIOutstandingTransactionIdentifier` |
| `FacilityIdentifier` | `LiqAPIFacilityIdentifier` |
| `OutstandingIdentifier` | `LiqAPIOutstandingIdentifier` |
| `CustomerIdentifier` | `LiqAPICustomerIdentifier` |
| `InterestPayment` | `LiqAPIUpdateQuickRepricingInterestPayment` |

**Primitive:** `statusCode` (String, "PEND"), `interestRateIsFloating` (Boolean), `repricingFrequencyApplies` (Boolean)

### MISCode
| @LiqAPIFieldMapper Field | Mapper Class |
|---|---|
| `OwnerIdentifier` | `LiqAPIOwnerIdentifier` |
| `MISCodeIntegration` | `LiqAPIMISCodeIntegration` |

### SBLCIssuance
| @LiqAPIFieldMapper Field | Mapper Class |
|---|---|
| `FacilityIdentifier` | `LiqAPIFacilityIdentifier` |
| `BorrowerIdentifier` | `LiqAPIBorrowerIdentifier` |
| `SBLCIdentifier` | `LiqAPISBLCIdentifier` |
| `OutstandingOngoingFeePricingIntegration` | `LiqAPIOutstandingOngoingFeePricingIntegration` |
| `SBLCTransactionIdentifier` | `LiqAPISBLCTransactionIdentifier` |

**Primitive:** `sourceRefNum` (String), `systemSourceId` (String), `noteDate` (LiqDate), `requestedAmount` (BigDecimal), `eventComment` (String), `issuingFeeExistsIndicator` (Boolean), `facingFeeExistsIndicator` (Boolean)

### SBLCDecrease / SBLCIncrease
| @LiqAPIFieldMapper Field | Mapper Class |
|---|---|
| `SBLCIdentifier` | `LiqAPISBLCIdentifier` |
| `SBLCTransactionIdentifier` | `LiqAPISBLCTransactionIdentifier` |

**Primitive:** `eventComment` (String), `transactionDescription` (String), `systemSourceId` (String), `sourceRefNum` (String)

### SBLCFacingFeePayment
| @LiqAPIFieldMapper Field | Mapper Class |
|---|---|
| `sblcIdentifier` | `LiqAPISBLCIdentifier` |
| `FeeTransactionIdentifier` | `LiqAPIFeeTransactionIdentifier` |

**Primitive:** `transactionDescription` (String), `eventComment` (String), `borrowerRemittanceInstruction` (String)

### SBLCIssuanceFeePayment
| @LiqAPIFieldMapper Field | Mapper Class |
|---|---|
| `sblcIdentifier` | `LiqAPISBLCIdentifier` |
| `FeeTransactionIdentifier` | `LiqAPIFeeTransactionIdentifier` |

**Primitive:** `transactionDescription` (String), `eventComment` (String)

### FacilityOngoingFee
| @LiqAPIFieldMapper Field | Mapper Class |
|---|---|
| `FeeIdentifier` | `LiqAPIFeeIdentifier` |

### FacilityOngoingFeePayment
| @LiqAPIFieldMapper Field | Mapper Class |
|---|---|
| `FeeTransactionIdentifier` | `LiqAPIFeeTransactionIdentifier` |
| `FacilityIdentifier` | `LiqAPIFacilityIdentifier` |
| `FeeIdentifier` | `LiqAPIFeeIdentifier` |

**Primitive:** `transactionDescription` (String), `comment` (String)

### UpfrontFee
| @LiqAPIFieldMapper Field | Mapper Class |
|---|---|
| `OwnerIdentifier` | `LiqAPIOwnerIdentifier` |
| `UpfrontFeeIdentifier` | `LiqAPIUpfrontFeeIdentifier` |

### UserProfile
| @LiqAPIFieldMapper Field | Mapper Class |
|---|---|
| `UserDepartments` | `LiqAPIUserDepartmentsIntegration` |
| `UserWorkflowTemplateGroups` | `LiqAPIUserWorkflowTemplateGroupsIntegration` |
| `UserSecondaryProcessingAreas` | `LiqAPIUserSecondaryProcessingAreasIntegration` |
| `UserSalesProducts` | `LiqAPIUserSalesProductsIntegration` |
| `UserRiskBooks` | `LiqAPIUserRiskBooksIntegration` |

**Primitive:** `riskBooksViewAllInd` (boolean)

### Outgoing Messages (ACH, BOJ, DDA, IMT, ISO, MT, Zengin)
All share the same pattern — no @LiqAPIFieldMapper annotations.

**Common Primitive Fields:** `cashflowId` (String), `{network}OutId` (String), `narrative` (String), `queueStatus` (String), `sequenceNumber` (Integer), `timestampSent` (Date)

### HolidayCalendarCode / HolidayCalendarDate
No @LiqAPIFieldMapper. **Primitive:** `description` (String), `activeIndicator` (Boolean), `holidayCalendarCode` (String), `holidayDate` (LiqDate), `isUpdate` (Boolean)

### FlexUnscheduledTransaction
No @LiqAPIFieldMapper. **Primitive:** `transactionId` (String), `transactionDescription` (String), `reason` (String), `transactionType` (String)

---

## basicExecute() Patterns by Entity Category

### Standard Lock-Execute-Commit (Most Entities)

```java
public Object basicExecute() {
    try {
        checkDealSecurity();
        checkCustomerSecurity();
        this.lockAPIData();
        super.basicExecute();  // or custom update method
        this.singleCommit();
    } finally {
        this.unLockAPIData();
    }
    return response();
}
```

**Used by:** Deal, Facility, LoanIncrease, LoanInterestPayment, SBLCDecrease, SBLCIncrease, SBLCIssuance, SBLCIssuanceFeePayment, UnscheduledLoanPrincipalPayment, MISCode, AdditionalFields

### Spread Component Pattern (Drawdown/QLR)

```java
public Object basicExecute() {
    try {
        checkDealSecurity();
        checkCustomerSecurity();
        this.lockAPIData();
        super.basicExecute();
        this.updateSpreadComponent();
        this.performUpdate();  // or this.singleCommit() directly
        this.singleCommit();
    } finally {
        if (this.getOutstandingTran() != null)
            this.getOutstandingTran().getPrivateState().remove("RefreshRates");
        this.unLockAPIData();
    }
    return response();
}
```

**Used by:** LoanDrawdown, QuickLoanRepricing

### Principal Payment Pattern (with TransactionDate/AutoReduce)

```java
public Object basicExecute() {
    try {
        checkDealSecurity();
        checkCustomerSecurity();
        this.lockAPIData();
        updateTransactionDate();
        updateAutoReduceFacility();
        super.basicExecute();
        this.singleCommit();
    } finally {
        this.unlockData();
    }
    return response();
}
```

**Used by:** LoanPrincipalPayment

### Custom Update Method (No super.basicExecute)

```java
public Object basicExecute() {
    try {
        checkDealSecurity();
        checkCustomerSecurity();
        this.lockAPIData();
        this.updateSBLCFacingFeePayment();  // or updateFacilityOngoingFee(), etc.
        this.singleCommit();
    } finally {
        this.unLockAPIData();
    }
    return response();
}
```

**Used by:** SBLCFacingFeePayment, FacilityOngoingFee, FacilityOngoingFeePayment, UpfrontFee, FlexUnscheduledTransaction, OutgoingMessages

### Admin Pattern (No Security Checks)

```java
public Object basicExecute() {
    try {
        this.lockAPIData();
        // mapping methods...
        super.basicExecute();
        this.singleCommit();
    } finally {
        this.unLockAPIData();
    }
    return response();
}
```

**Used by:** UserProfile, UserSecurityProfile

### Code Table Pattern (HolidayCalendar)

```java
public Object basicExecute() {
    if (isUpdate) {
        recordUpdateEvent();
        try { updateCode(); } catch (ConcurrentUpdateException e) { ... }
    }
    save();
    updateCollection();
    singleCommit();
    refreshAllCodeTables();
    return LiqAPIHolidayCalendar{Type}IntegrationAsReturnValue.clazz.forUpdate(this);
}
```

**Used by:** HolidayCalendarCode, HolidayCalendarDate

---

## basicValidate() Patterns

### Standard Pattern

```java
public void basicValidate() {
    validateIdentifiers();
    super.basicValidate();
    validateTimeStamp(this.businessObject, this.getMatchUpdatedTimestamp());
}
```

### Extended Validation (with field-specific checks)

```java
public void basicValidate() {
    validateIdentifiers();
    super.basicValidate();
    validateTimeStamp(...);
    validateRacRate();
    validateSpreadValue();
}
```

### Multi-Owner Validation (MISCode)

```java
public void basicValidate() {
    super.basicValidate();
    validateIdentifiers();
    validateMisCodes();
    validateTimeStamp(getBusinessObject(), this.getMatchUpdatedTimestamp());
}
```

---

## Specialized Patterns

### Pattern: Multiple Owner Type Support (MISCode)

For entities that can belong to different owner types (Deal, Facility, Loan). Uses polymorphic update methods based on `ownerIdentifier.getOwnerType()`.

### Pattern: Additional Fields Support (Deal/Facility)

For entities supporting custom/additional fields via Object Extension (`LiqPersistentObjectExtensionBehavior`).

### Pattern: Complex Nested Collections (Facility)

For entities with multiple nested collections: sublimits, affiliate mappings, fee pricing, portfolio shares.

### Pattern: Conditional Updates (Deal ProductType)

For fields that can only be updated under specific conditions (license checks, state transitions).

---

## Mandatory Methods for isDelete Feature (Update/Delete Support)

> **Every Update API Integration class MUST implement the following four methods** to enable the isDelete feature for Update APIs.
> These methods provide the structural metadata required by the framework to support partial-delete (field-level removal) operations through the Update endpoint.

### Required Import

```java
import com.misys.liq.api.rest.executable.update.helper.Node;
```

### 1. `isEnabledForUpdateDelete()`

/**
 * Enables the isDelete feature for this Update API.
 * When this method returns Boolean.TRUE, the framework allows field-level
 * delete operations to be performed through the Update endpoint.
 *
 * @return Boolean.TRUE to enable isDelete feature for this Update API
 */

```java
public Boolean isEnabledForUpdateDelete() {
    return Boolean.TRUE;
}
```

Always returns `Boolean.TRUE` for all Update APIs that support the isDelete feature.

### 2. `fetchMandatoryAttributesForQuery()`

/**
 * Returns the set of mandatory attribute names required to identify the entity
 * when performing a query as part of the isDelete feature for Update APIs.
 * These attributes must be present in the request payload to locate the target entity.
 *
 * @return Set of mandatory attribute names for entity identification
 */

```java
public Set<String> fetchMandatoryAttributesForQuery() {
    return Set.of("entityIdentifierFieldName");
}
```

Returns a `Set<String>` of the mandatory identifier field names required to locate the entity for query operations. These correspond to the identifier fields declared in the class.

**Examples by entity:**
| Entity | Mandatory Attributes |
|---|---|
| UpfrontFee | `"upforntFeeIdentifier"` |
| UserProfile | `"id", "loginId"` |
| SBLCIssuance | `"sblcTransactionIdentifier", "version"` |

### 3. `updateStructure()`

/**
 * Defines the hierarchical node structure for the Update payload,
 * used by the isDelete feature framework to understand which non-primitive
 * collection fields can be targeted for field-level delete operations
 * through the Update API.
 *
 * @return Node representing the update payload tree structure
 */

Builds a `Node` tree representing the update payload structure. Each non-primitive collection field that supports delete operations must be represented as a child node with its primary key(s).

```java
public Node updateStructure() {

    // Create a child node for each non-primitive collection that supports delete
    Node childCollection = Node.NodeBuilder.getInstance()
            .setAttributeName("collectionFieldName")     // field name in the Integration class
            .addPrimaryKeys("primaryKeyFieldName")       // key that uniquely identifies items in the collection
            .setIsNonPrimitiveCollection(Boolean.TRUE)   // marks as a collection node
            .build();

    // For nested collections, add grandchild nodes
    Node nestedChild = Node.NodeBuilder.getInstance()
            .setAttributeName("nestedFieldName")
            .addPrimaryKeys("nestedPrimaryKey")
            .setIsNonPrimitiveCollection(Boolean.TRUE)
            .build();

    Node parentWithNesting = Node.NodeBuilder.getInstance()
            .setAttributeName("parentCollectionFieldName")
            .addPrimaryKeys("parentPrimaryKey")
            .setIsNonPrimitiveCollection(Boolean.TRUE)
            .addChildren(nestedChild)
            .build();

    // Create root node named "Update{EntityName}Integration" with all children
    Node root = Node.NodeBuilder.getInstance()
            .setAttributeName("Update{EntityName}Integration")
            .addChildren(childCollection)
            .addChildren(parentWithNesting)
            .build();

    return root;
}
```

**Key rules:**
- Root node `attributeName` = `"Update{EntityName}Integration"` (without `LiqAPI` prefix)
- Each child represents a non-primitive collection field from the class
- `addPrimaryKeys()` specifies the field(s) that uniquely identify items within the collection
- Use `addChildren()` for nested sub-collections within a parent collection
- Only include collections that are modifiable through the Update endpoint

### 4. `queryStructure()`

/**
 * Defines the hierarchical node structure for the Query payload used by
 * the isDelete feature framework. This structure mirrors the updateStructure
 * but with query-mode enabled, linking each query node to its corresponding
 * attribute in the Update payload for field-level delete operations
 * through the Update API.
 *
 * @return Node representing the query payload tree structure
 */

Mirrors `updateStructure()` but with query-mode settings that link query nodes back to their corresponding update payload attributes.

```java
public Node queryStructure() {

    // Create a child node - same as updateStructure but with query-mode settings
    Node childCollection = Node.NodeBuilder.getInstance()
            .setAttributeName("collectionFieldName")
            .addPrimaryKeys("primaryKeyFieldName")
            .setQueryMode(Boolean.TRUE)                              // enables query mode
            .setUpdatePayloadAssociatedAttribute("collectionFieldName")  // links to update attribute
            .setIsNonPrimitiveCollection(Boolean.TRUE)
            .build();

    // For nested collections in query mode
    Node nestedChild = Node.NodeBuilder.getInstance()
            .setAttributeName("nestedFieldName")
            .addPrimaryKeys("nestedPrimaryKey")
            .setQueryMode(Boolean.TRUE)
            .setUpdatePayloadAssociatedAttribute("nestedFieldName")
            .setIsNonPrimitiveCollection(Boolean.TRUE)
            .build();

    Node parentWithNesting = Node.NodeBuilder.getInstance()
            .setAttributeName("parentCollectionFieldName")
            .addPrimaryKeys("parentPrimaryKey")
            .setQueryMode(Boolean.TRUE)
            .setUpdatePayloadAssociatedAttribute("parentCollectionFieldName")
            .setIsNonPrimitiveCollection(Boolean.TRUE)
            .addChildren(nestedChild)
            .build();

    // Create root node with query mode enabled
    Node root = Node.NodeBuilder.getInstance()
            .setAttributeName("Update{EntityName}Integration")
            .addChildren(childCollection)
            .addChildren(parentWithNesting)
            .setQueryMode(Boolean.TRUE)   // optional: set on root when all children are query-mode
            .build();

    return root;
}
```

**Key rules:**
- Structure mirrors `updateStructure()` exactly (same nodes, same hierarchy)
- Each node adds `.setQueryMode(Boolean.TRUE)`
- Each node adds `.setUpdatePayloadAssociatedAttribute("fieldName")` — the value must match the `attributeName` of the corresponding node in `updateStructure()`
- Root node `attributeName` = `"Update{EntityName}Integration"` (same as `updateStructure()`)

### Complete Example (Simple — Single Collection)

```java
/**
 * Enables the isDelete feature for this Update API.
 * @return Boolean.TRUE to enable isDelete feature for this Update API
 */
public Boolean isEnabledForUpdateDelete() {
    return Boolean.TRUE;
}

/**
 * Returns the mandatory attributes required to identify the entity
 * for the isDelete feature of this Update API.
 * @return Set of mandatory attribute names for entity identification
 */
public Set<String> fetchMandatoryAttributesForQuery() {
    return Set.of("entityIdentifier");
}

/**
 * Defines the update payload structure for the isDelete feature of this Update API.
 * @return Node representing the update payload tree structure
 */
public Node updateStructure() {
    Node feeDetails = Node.NodeBuilder.getInstance()
            .setAttributeName("feeDetails")
            .addPrimaryKeys("feeType")
            .setIsNonPrimitiveCollection(Boolean.TRUE)
            .build();

    Node root = Node.NodeBuilder.getInstance()
            .setAttributeName("UpdateUpfrontFeeIntegration")
            .addChildren(feeDetails)
            .build();

    return root;
}

/**
 * Defines the query payload structure for the isDelete feature of this Update API.
 * @return Node representing the query payload tree structure
 */
public Node queryStructure() {
    Node feeDetails = Node.NodeBuilder.getInstance()
            .setAttributeName("feeDetails")
            .addPrimaryKeys("feeType")
            .setQueryMode(Boolean.TRUE)
            .setUpdatePayloadAssociatedAttribute("feeDetails")
            .setIsNonPrimitiveCollection(Boolean.TRUE)
            .build();

    Node root = Node.NodeBuilder.getInstance()
            .setAttributeName("UpdateUpfrontFeeIntegration")
            .addChildren(feeDetails)
            .build();

    return root;
}
```

### Complete Example (Nested Collections)

```java
/**
 * Enables the isDelete feature for this Update API.
 * @return Boolean.TRUE to enable isDelete feature for this Update API
 */
public Boolean isEnabledForUpdateDelete() {
    return Boolean.TRUE;
}

/**
 * Returns the mandatory attributes required to identify the entity
 * for the isDelete feature of this Update API.
 * @return Set of mandatory attribute names for entity identification
 */
public Set<String> fetchMandatoryAttributesForQuery() {
    return Set.of("id", "loginId");
}

/**
 * Defines the update payload structure for the isDelete feature of this Update API.
 * @return Node representing the update payload tree structure
 */
public Node updateStructure() {
    Node userDepartment = Node.NodeBuilder.getInstance()
            .setAttributeName("userDepartment")
            .addPrimaryKeys("departmentCode")
            .setIsNonPrimitiveCollection(Boolean.TRUE)
            .build();

    Node userDepartments = Node.NodeBuilder.getInstance()
            .setAttributeName("userDepartments")
            .addPrimaryKeys("userDepartment")
            .setIsNonPrimitiveCollection(Boolean.TRUE)
            .addChildren(userDepartment)
            .build();

    Node root = Node.NodeBuilder.getInstance()
            .setAttributeName("UpdateUserProfileIntegration")
            .addChildren(userDepartments)
            .build();

    return root;
}

/**
 * Defines the query payload structure for the isDelete feature of this Update API.
 * @return Node representing the query payload tree structure
 */
public Node queryStructure() {
    Node userDepartment = Node.NodeBuilder.getInstance()
            .setAttributeName("userDepartment")
            .addPrimaryKeys("departmentCode")
            .setQueryMode(Boolean.TRUE)
            .setUpdatePayloadAssociatedAttribute("userDepartment")
            .setIsNonPrimitiveCollection(Boolean.TRUE)
            .build();

    Node userDepartments = Node.NodeBuilder.getInstance()
            .setAttributeName("userDepartments")
            .addPrimaryKeys("userDepartment")
            .setQueryMode(Boolean.TRUE)
            .setUpdatePayloadAssociatedAttribute("userDepartments")
            .setIsNonPrimitiveCollection(Boolean.TRUE)
            .addChildren(userDepartment)
            .build();

    Node root = Node.NodeBuilder.getInstance()
            .setAttributeName("UpdateUserProfileIntegration")
            .addChildren(userDepartments)
            .setQueryMode(Boolean.TRUE)
            .build();

    return root;
}
```

---

## Return Value Class

If `LiqAPI{EntityName}IntegrationAsReturnValue` is generated:

- Implement `forUpdate()` static method
- Map entity fields from business object to return value
- Set entity identifier and updated timestamp
- Implement inner `Class` field mapping methods:
  - `primitiveFieldMappings()` — String, BigDecimal, Date, Boolean
  - `nonPrimitiveFieldMappings()` — single complex objects
  - `nonPrimitiveFieldCollectionMappings()` — List<> collections

---

## Scripts

This skill relies on centralized script execution via the `lending-api-developer` agent:

- **Script Location:** `.github/agents/scripts/run-excel-reader.ps1`
- **JAR Dependency:** `.github/agents/scripts/artifacts/executable/IntegrationAPITool-1.0-exec.jar`
- **Output Path:** `FLIQ-liqjava/IntegrationAPITool/artifacts/temp-generated_class/`
- **Validation Script:** `.github/skills/lending-update-api/scripts/generate-update-api.ps1`

The validation script extracts metadata (class hierarchy, @LiqAPIFieldMapper annotations, primitive fields, update methods) from any existing Update Integration source file.

**Usage:**
```powershell
.github\skills\lending-update-api\scripts\generate-update-api.ps1 -EntityName "Deal" -PackagePath "deal"
.github\skills\lending-update-api\scripts\generate-update-api.ps1 -EntityName "LoanDrawdown" -PackagePath "outstanding/drawdown"
.github\skills\lending-update-api\scripts\generate-update-api.ps1 -EntityName "UserProfile" -PackagePath "user"
```

---

## How to Use This Skill

### Step 1: Check for Existing Generated Class

First, check if a `LiqAPIUpdate{EntityName}Integration.java` file already exists at the output path:

```
FLIQ-liqjava/IntegrationAPITool/artifacts/temp-generated_class/
```

- **If the file EXISTS** → Proceed to **Step 3** (use it as the base context).
- **If the file DOES NOT EXIST** → Proceed to **Step 2**.

### Step 2: Generate Base Class (Prerequisites Check)

If no `LiqAPIUpdate{EntityName}Integration.java` exists at the output path, check for the following prerequisites:

1. **Business Object name** — the entity to generate the Update API for
2. **Requirement spreadsheet path** — the Excel file defining the Update operation

**If prerequisites are available:**
- Run the generation script to produce the base class at the output path:
  ```powershell
  .github\agents\scripts\run-excel-reader.ps1 -SpreadsheetPath "<path-to-spreadsheet>" -Operation "Update" -EntityName "<EntityName>"
  ```
- The generated file will be placed at:
  ```
  FLIQ-liqjava/IntegrationAPITool/artifacts/temp-generated_class/LiqAPIUpdate{EntityName}Integration.java
  ```
- Proceed to **Step 3**.

**If prerequisites are NOT available:**
- **STOP processing.** Inform the user that the business object name and requirement spreadsheet path are required to generate the Update API Integration class.
- Do NOT attempt to create the class manually without the script output.

### Step 3: Apply Update Skill to Base Class

Take the existing `LiqAPIUpdate{EntityName}Integration.java` from the output path as the base context and apply the rules in this SKILL.md to add any missing code:

1. **Verify and add missing interface implementation** — ensure `implements IAPIRestIntegration` is present.

2. **Verify and add missing methods** — check that all required methods are implemented:
   - `basicValidate()` — with identifier validation and timestamp check
   - `basicExecute()` — with lock/execute/commit pattern
   - `lockAPIData()` / `unLockAPIData()` — locking methods
   - `checkDealSecurity()` / `checkCustomerSecurity()` — security methods
   - `response()` — return value generation
   - `addIds()` — ID extraction
   - `securityAccessSymbol()` — security function name
   - `isEnabledForUpdateDelete()` — isDelete feature enablement
   - `fetchMandatoryAttributesForQuery()` — mandatory identifier attributes
   - `updateStructure()` — Node tree for update payload
   - `queryStructure()` — Node tree for query payload (mirrors updateStructure with query-mode)

3. **Verify and add missing field mappings** — ensure `@LiqAPIFieldMapper` annotations, non-primitive field collection mappings, and primitive field mappings are complete.

4. **Verify inner `Class`** — ensure `basicNew()`, `getJavaClass()`, `getStSuperclass()`, `nonPrimitiveFieldMappings()`, `nonPrimitiveFieldCollectionMappings()`, `primitiveFieldMappings()`, and `isRest()` are present.

5. **Modify `LiqAPI{EntityName}IntegrationAsReturnValue`** if generated. Only modify the `forUpdate` method following patterns in `references/example.md`.

6. **Refer to `references/example.md`** for complete implementation examples covering all patterns (UpfrontFee, Deal, MISCode, OutgoingMessage, etc.).

### Step 4: Copy to Repository

1. **Copy modified classes** to the repository under the correct package path and delete from temp path.
2. **Do not change existing classes** — only add newly generated ones.

**Package paths:**
```
FLIQ-liqjava/LoanIQ/srcgen/com/misys/liq/api/rest/executable/{domain}/LiqAPIUpdate{EntityName}Integration.java
FLIQ-liqjava/LoanIQ/srcgen/com/misys/liq/api/rest/data/{domain}/LiqAPI{EntityName}IntegrationAsReturnValue.java
FLIQ-liqjava/LoanIQ/test/com/misys/liq/api/rest/executable/{domain}/LiqAPIUpdate{EntityName}IntegrationTest.java
```

---

## Key Differences Between Update and Other APIs

| Aspect | Update API | Create API | Query API | Delete API |
|--------|-----------|------------|-----------|-----------|
| **Identifier Type** | List of identifiers | N/A (creates new) | Single identifier | Entity/Owner identifier |
| **Timestamp** | matchUpdatedTimestamp required | N/A | Not required | matchUpdatedTimestamp required |
| **Operation** | Write (modification) | Write (creation) | Read-only | Write (destructive) |
| **Validation** | Identifier + field + timestamp | Full field validation | Identifier only | Identifier + state checks |
| **Return Type** | Updated entity + timestamp | Created entity ID | Entity data as-is | Deleted entity confirmation |
| **Security** | Update{Entity}Integration | Create{Entity}Integration | Query{Entity}Integration | Delete{Entity}Integration |
| **Base Class** | May extend intermediate (Pattern B) | LiqAPIExecutableData | Always LiqAPIExecutableData | LiqAPIExecutableData |
| **Locking** | Exclusive lock (try-finally) | Exclusive lock (try-finally) | No locking | Exclusive lock (try-finally) |
| **Idempotency** | Not applicable | Required | Not applicable | Not applicable |
| **isDelete Feature** | Supports field-level deletion | N/A | N/A | Full entity deletion |

---

## Best Practices

### Locking
- Always use try-finally for lock/unlock patterns
- Call `lockAPIData()` before any business logic
- Call `unLockAPIData()` in the finally block (ensures cleanup on failure)
- For LoanPrincipalPayment, use `unlockData()` instead of `unLockAPIData()`
- Clean up private state in finally (e.g., remove `"RefreshRates"` for Drawdown/QLR)

### Timestamp Validation
- Always validate timestamp using `validateTimeStamp(businessObject, getMatchUpdatedTimestamp())`
- Timestamp format must be `yyyy-MM-dd HH:mm:ss.S`
- Perform timestamp validation after identifier validation in `basicValidate()`
- Timestamp prevents concurrent update conflicts (optimistic locking)

### Security
- Define `securityAccessSymbol()` in both class and inner Class
- Use consistent naming: `"Update{Entity}Integration"`
- Always call `checkDealSecurity()` before `checkCustomerSecurity()`
- Admin entities (UserProfile, UserSecurityProfile) skip security checks
- Add `securityFunctionParent()` for hierarchical security

### Error Handling
- Use specific exception types for validation failures
- Call identifier's `throwInvalidIdentifierException()` for invalid identifiers
- Log errors with context before throwing
- Use externalized messages via `Messages.liqNlsExternalizedMessage()`
- Catch `ConcurrentUpdateException` for HolidayCalendar patterns

### Field Mappings
- Use `@LiqAPIFieldMapper` for all non-primitive field annotations
- Separate fields into primitives and non-primitives in inner Class mappings
- Implement all three mapping methods: `primitiveFieldMappings()`, `nonPrimitiveFieldMappings()`, `nonPrimitiveFieldCollectionMappings()`
- Inherit parent mappings via `super.*FieldMappings()` before adding new ones

### isDelete Feature
- Always return `Boolean.TRUE` (boxed, not primitive `true`) from `isEnabledForUpdateDelete()`
- Root node `attributeName` in `updateStructure()` must be `"Update{EntityName}Integration"` (without `LiqAPI` prefix)
- `queryStructure()` must mirror `updateStructure()` exactly with `.setQueryMode(Boolean.TRUE)` on every node
- `.setUpdatePayloadAssociatedAttribute()` value must match the corresponding `attributeName` in `updateStructure()`

### General
1. Always extend from the appropriate base class (Pattern A or B)
2. Implement `IAPIRestIntegration` interface
3. Validate identifiers early in `basicValidate()`
4. Lock/unlock data in try-finally blocks
5. Commit in single transaction using `singleCommit()`
6. Return formatted response using return value class `forUpdate()` method
7. Register static inner `Class` with `StClassRegistry.register(clazz)`
8. Implement `isRest()` returning `true` in inner Class
9. Refer to `references/example.md` for complete implementation patterns

---

## Troubleshooting

| Issue | Resolution |
|---|---|
| Script did not generate Update class | Entity does not support Update — check Supported Business Objects table |
| Compilation error: Cannot find symbol | Verify base class exists (Pattern A: `LiqAPIExecutableData`, Pattern B: intermediate class); check imports |
| `lockAPIData()` throws exception | Entity already locked by another transaction — check for concurrent updates |
| Timestamp validation fails | Ensure `matchUpdatedTimestamp` format is `yyyy-MM-dd HH:mm:ss.S` and matches current entity timestamp |
| `ConcurrentUpdateException` on commit | Entity was modified between read and write — retry with fresh timestamp |
| `updateStructure()` / `queryStructure()` not working | Verify root node `attributeName` is `"Update{EntityName}Integration"` (without `LiqAPI` prefix) |
| `isEnabledForUpdateDelete()` not invoked | Ensure method returns `Boolean.TRUE` (not `true`) — framework checks for boxed Boolean |
| Test class not generated | Create test class manually following test patterns |
| Missing `forUpdate()` method | Implement static method in ReturnValue class per Step 3.5 |
| Security check fails (`checkDealSecurity`) | Verify user has appropriate deal-level security permissions for the entity |
