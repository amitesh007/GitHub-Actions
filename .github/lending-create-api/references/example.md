# LoanIQ Create API — Unified Reference Examples

This document contains representative source code examples from multiple entity categories,
demonstrating the full range of Create API patterns in LoanIQ.

---

## 1. Pattern A — Direct Extension (HolidayCalendarDate)

**Source:** `LoanIQ/srcgen/com/misys/liq/api/rest/executable/holidaycalendar/LiqAPICreateHolidayCalendarDateIntegration.java`

```java
package com.misys.liq.api.rest.executable.holidaycalendar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.misys.liq.LoanIQ;
import com.misys.liq.Messages;
import com.misys.liq.api.data.LiqAPIReturnData;
import com.misys.liq.api.data.LiqAPIViewPrimitiveFieldMapping;
import com.misys.liq.api.executable.LiqAPIExecutableData;
import com.misys.liq.api.rest.constants.ErrorMessageConstants;
import com.misys.liq.bm.desktopcore.GTMEvents;
import com.misys.liq.bm.desktopcore.gtm.GTMCode;
import com.misys.liq.bm.desktopcore.gtm.GTMCodeTable;
import com.misys.liq.bm.desktopcore.gtm.GTMHolidayCalendarDateCode;
import com.misys.liq.bm.desktopcore.hlphelper.TableMaintenanceProcessingHelper;
import com.misys.liq.bm.desktopcore.main.cdt.Event;
import com.misys.liq.infrastructure.LiqDate;
import com.misys.liq.infrastructure.LiqObject;
import com.misys.liq.infrastructure.LiqUtilities;
import com.misys.liq.infrastructure.bm.handlers.BusinessDateHandler;
import com.misys.liq.infrastructure.exceptions.LiqError;
import com.sxsy.smtj.StClass;
import com.sxsy.smtj.StClassRegistry;
import com.sxsy.smtj.StObject;
import com.sxsy.smtj.blockinterfaces.IBoolean1Block;
import com.sxsy.smtj.exceptions.ExceptionUtility;
import com.sxsy.smtj.utilities.CompareUtility;
import com.sxsy.smtj.utilities.StringUtility;

/**
 * Creates a new entry of Holiday Calendar Date.
 * Pattern A — extends LiqAPIExecutableData directly.
 */
public class LiqAPICreateHolidayCalendarDateIntegration extends LiqAPIExecutableData
        implements StObject {

    public static final Class clazz = new Class();

    public String holidayCalendarCode;
    public LiqDate holidayDate;
    public String description;
    public Boolean activeIndicator;
    public GTMHolidayCalendarDateCode gTMHolidayCalendarDateCode;
    public GTMCode gtmCode;

    static {
        StClassRegistry.register(clazz);
    }

    // Getters/setters omitted for brevity

    public StClass getStClass() { return clazz; }

    public GTMCodeTable codeTable() {
        return ((LiqAPICreateHolidayCalendarDateIntegration.Class) this.getStClass()).codeTable();
    }

    public void basicValidate() {
        super.basicValidate();
        this.validateHolidayCode();
        this.validateHolidayDate();
        this.validateDateCodeCombination();
    }

    public Object basicExecute() {
        this.createNewHolidayCalendarDate();
        this.singleCommit();
        LoanIQ.currentSession().refreshAllCodeTables();
        return LiqAPIHolidayCalendarDateTableEntryIntegrationAsReturnValue.clazz.forCreate(this);
    }

    public void createNewHolidayCalendarDate() {
        GTMCodeTable table = GTMCodeTable.clazz.named("Holiday Calendar Dates");
        GTMCode t = GTMCode.clazz.forCodeTable(table);
        t.setDefaultValue();
        t.zz_description(this.getDescription());
        t.zz_code(this.getHolidayCalendarCode());
        t.zz_date(this.getHolidayDate());
        if (!StringUtility.isNilOrBlank(this.getActiveIndicator())) {
            t.zz_active(this.getActiveIndicator());
        }
        table.addCode(t);
        table.commit();
        TableMaintenanceProcessingHelper.clazz.recordAddEventFor(t, this);
        LiqUtilities.save(t);
        this.setGtmCode(t);
    }

    public static class Class extends LiqAPIExecutableData.Class implements StClass {
        protected Class() {}
        public StObject basicNew() { return new LiqAPICreateHolidayCalendarDateIntegration(); }
        public java.lang.Class getJavaClass() { return LiqAPICreateHolidayCalendarDateIntegration.class; }
        public StClass getStSuperclass() { return LiqAPIExecutableData.clazz; }
        public GTMCodeTable codeTable() { return GTMCodeTable.clazz.named("Holiday Calendar Dates"); }

        public List<LiqAPIReturnData> getReturnType() {
            List<LiqAPIReturnData> list = new ArrayList<>();
            list.add(LiqAPIReturnData.getInstance(LiqAPIHolidayCalendarDateTableEntryIntegrationAsReturnValue.clazz, false));
            return list;
        }

        public List primitiveFieldMappings() {
            LiqAPIViewPrimitiveFieldMapping t1 = (LiqAPIViewPrimitiveFieldMapping) LiqAPIViewPrimitiveFieldMapping.clazz.newStObject();
            t1.setFieldName("holidayCalendarCode");
            t1.setLogicalFieldName("holidayCalendar");
            t1.setIsRequired(true);

            LiqAPIViewPrimitiveFieldMapping t2 = (LiqAPIViewPrimitiveFieldMapping) LiqAPIViewPrimitiveFieldMapping.clazz.newStObject();
            t2.setFieldName("holidayDate");
            t2.setLogicalFieldName("date");
            t2.setIsRequired(true);

            LiqAPIViewPrimitiveFieldMapping t3 = (LiqAPIViewPrimitiveFieldMapping) LiqAPIViewPrimitiveFieldMapping.clazz.newStObject();
            t3.setFieldName("description");
            t3.setLogicalFieldName("description");
            t3.setMaxSize(100);
            t3.setIsRequired(false);

            List t10 = super.primitiveFieldMappings();
            t10.add(t1);
            t10.add(t2);
            t10.add(t3);
            return t10;
        }

        public String securityAccessSymbol() { return "AlterCodeTable"; }
        public boolean isRest() { return true; }
    }
}
```

---

## 2. Pattern B — Outstanding Transaction (SBLCDecrease)

**Source:** `LoanIQ/srcgen/com/misys/liq/api/rest/executable/outstanding/sblc/LiqAPICreateSBLCDecreaseIntegration.java`

```java
package com.misys.liq.api.rest.executable.outstanding.sblc;

import com.finastra.liq.api.annotation.LiqAPIFieldMapper;
import com.misys.liq.api.data.LiqAPIViewPrimitiveFieldMapping;
import com.misys.liq.api.executable.IAPIRestIntegration;
import com.misys.liq.api.rest.executable.sblc.LiqAPISBLCIdentifier;
import com.sxsy.smtj.StClass;
import com.sxsy.smtj.StClassRegistry;
import com.sxsy.smtj.StObject;

import java.util.List;

/**
 * SBLC Decrease — Pattern B with simple try/finally.
 * Standard outstanding transaction pattern with SBLC identifier.
 */
public class LiqAPICreateSBLCDecreaseIntegration extends LiqAPICreateSBLCDecrease
        implements IAPIRestIntegration {

    public static final Class clazz = new Class();

    @LiqAPIFieldMapper(name = "SBLCIdentifier", className = "com.misys.liq.api.rest.executable.sblc.LiqAPISBLCIdentifier")
    public LiqAPISBLCIdentifier sblcIdentifier;

    public String eventComment;
    public String transactionDescription;
    public String systemSourceId;
    public String sourceRefNum;

    static { StClassRegistry.register(clazz); }

    public StClass getStClass() { return clazz; }

    @Override
    public Object basicExecute() {
        try {
            checkDealSecurity();
            checkCustomerSecurity();
            lockAPIData();
            super.basicExecute();
            createIdempotency();
            this.singleCommit();
        } finally {
            this.unLockAPIData();
        }
        return response();
    }

    public static class Class extends LiqAPICreateSBLCDecrease.Class implements StClass {
        protected Class() {}
        public StObject basicNew() { return new LiqAPICreateSBLCDecreaseIntegration(); }
        public java.lang.Class getJavaClass() { return LiqAPICreateSBLCDecreaseIntegration.class; }
        public StClass getStSuperclass() { return LiqAPICreateSBLCDecrease.clazz; }
        public boolean isRest() { return true; }

        public List primitiveFieldMappings() {
            List list = super.primitiveFieldMappings();
            LiqAPIViewPrimitiveFieldMapping t1 = (LiqAPIViewPrimitiveFieldMapping) LiqAPIViewPrimitiveFieldMapping.clazz.newStObject();
            t1.setFieldName("eventComment");
            t1.setLogicalFieldName("description");
            t1.setMaxSize(254);
            t1.setIsRequired(false);
            list.add(t1);

            LiqAPIViewPrimitiveFieldMapping t2 = (LiqAPIViewPrimitiveFieldMapping) LiqAPIViewPrimitiveFieldMapping.clazz.newStObject();
            t2.setFieldName("transactionDescription");
            t2.setLogicalFieldName("description");
            t2.setMaxSize(256);
            t2.setIsRequired(false);
            list.add(t2);
            // systemSourceId and sourceRefNum also added
            return list;
        }
    }
}
```

---

## 3. Pattern B — Fee Payment with Custom Business Method (SBLCFacingFeePayment)

**Source:** `LoanIQ/srcgen/com/misys/liq/api/rest/executable/sblcfeepayment/LiqAPICreateSBLCFacingFeePaymentIntegration.java`

```java
package com.misys.liq.api.rest.executable.sblcfeepayment;

import com.finastra.liq.api.annotation.LiqAPIFieldMapper;
import com.misys.liq.api.executable.IAPICreateRestIntegration;
import com.misys.liq.api.rest.executable.sblc.LiqAPISBLCIdentifier;
import com.misys.liq.infrastructure.LiqDate;
import com.sxsy.smtj.StObject;

import java.math.BigDecimal;

/**
 * SBLC Facing Fee Payment — custom createSBLCFacingFeePayment() method.
 * Demonstrates the "custom business method" basicExecute variant.
 */
public class LiqAPICreateSBLCFacingFeePaymentIntegration extends LiqAPIAbstractExecutableSBLCFeePayment
        implements StObject, IAPICreateRestIntegration {

    public static final Class clazz = new Class();

    @LiqAPIFieldMapper(name = "SBLCIdentifier", className = "com.misys.liq.api.rest.executable.sblc.LiqAPISBLCIdentifier")
    public LiqAPISBLCIdentifier sblcIdentifier;

    public String systemSourceId;
    public String sourceRefNum;
    public String eventComment;
    public String transactionDescription;
    public LiqDate cycleStartDate;
    public LiqDate effectiveDate;
    public BigDecimal requestedAmount;

    public Object basicExecute() {
        try {
            checkDealSecurity();
            checkCustomerSecurity();
            this.lockAPIData();
            this.createSBLCFacingFeePayment();
            createIdempotency();
            this.singleCommit();
        } finally {
            this.unLockAPIData();
        }
        return response();
    }

    public Object createSBLCFacingFeePayment() {
        this.saveAndCommitNewTransaction();
        this.demoteFirstErrorToWarningAndReturnFrom(new IVoid0Block() {
            public void value() {
                LiqAPICreateSBLCFacingFeePaymentIntegration.this.sendToFinalStatus();
            };
        });
        this.createSLMInsertEvent();
        this.saveAndCommitData();
        return this.transactionAsReturnValue();
    }
}
```

---

## 4. Pattern B-Admin — User Profile (No Security Checks, Collection Mapping)

**Source:** `LoanIQ/srcgen/com/misys/liq/api/rest/executable/user/LiqAPICreateUserProfileIntegration.java`

```java
package com.misys.liq.api.rest.executable.user;

import com.finastra.liq.api.annotation.LiqAPIFieldMapper;
import com.misys.liq.api.executable.IAPICreateRestIntegration;
import com.sxsy.smtj.StClass;
import com.sxsy.smtj.StClassRegistry;

import java.util.List;

/**
 * User Profile — Pattern B-Admin.
 * No checkDealSecurity/checkCustomerSecurity. Multiple collection fields.
 */
public class LiqAPICreateUserProfileIntegration extends LiqAPICreateUserProfile
        implements IAPICreateRestIntegration {

    public static final Class clazz = new Class();

    @LiqAPIFieldMapper(name = "UserDepartments", className = "com.misys.liq.api.rest.executable.user.LiqAPIUserDepartmentsIntegration")
    public List<LiqAPIUserDepartmentsIntegration> userDepartments;

    @LiqAPIFieldMapper(name = "UserWorkflowTemplateGroups", className = "com.misys.liq.api.rest.executable.user.LiqAPIUserWorkflowTemplateGroupsIntegration")
    public List<LiqAPIUserWorkflowTemplateGroupsIntegration> userWorkflowTemplateGroups;

    @LiqAPIFieldMapper(name = "UserSecondaryProcessingAreas", className = "com.misys.liq.api.rest.executable.user.LiqAPIUserSecondaryProcessingAreasIntegration")
    public List<LiqAPIUserSecondaryProcessingAreasIntegration> userSecondaryProcessingAreas;

    @LiqAPIFieldMapper(name = "UserSalesProducts", className = "com.misys.liq.api.rest.executable.user.LiqAPIUserSalesProductsIntegration")
    public List<LiqAPIUserSalesProductsIntegration> userSalesProducts;

    @LiqAPIFieldMapper(name = "UserRiskBooks", className = "com.misys.liq.api.rest.executable.user.LiqAPIUserRiskBooksIntegration")
    public List<LiqAPIUserRiskBooksIntegration> userRiskBooks;

    public String languageCode;
    public String groupAddress;

    static { StClassRegistry.register(clazz); }

    public Object basicExecute() {
        try {
            this.lockAPIData();
            mapUserDepartments();
            mapUserWorkflowTemplateGroups();
            mapUserSecondaryProcessingAreas();
            mapUserSalesProducts();
            mapUserRiskBooks();
            super.basicExecute();
            createIdempotency();
            this.singleCommit();
        } finally {
            this.unLockAPIData();
        }
        return response();
    }
}
```

---

## 5. Pattern B — Rollback-on-Error (UnscheduledLoanPrincipalPayment)

**Source:** `LoanIQ/srcgen/com/misys/liq/api/rest/executable/outstanding/LiqAPICreateUnscheduledLoanPrincipalPaymentIntegration.java`

```java
package com.misys.liq.api.rest.executable.outstanding;

import com.finastra.liq.api.annotation.LiqAPIFieldMapper;
import com.misys.liq.LoanIQ;
import com.misys.liq.Messages;
import com.misys.liq.api.data.outstanding.LiqAPIOutstandingIdentifier;
import com.misys.liq.api.executable.IAPICreateRestIntegration;
import com.misys.liq.infrastructure.exceptions.LiqError;
import com.sxsy.smtj.StObject;
import com.sxsy.smtj.exceptions.ExceptionUtility;

/**
 * Unscheduled Loan Principal Payment — rollback-on-error pattern.
 */
public class LiqAPICreateUnscheduledLoanPrincipalPaymentIntegration extends LiqAPICreateUnscheduledPrincipalPayment
        implements StObject, IAPICreateRestIntegration {

    public static final Class clazz = new Class();

    @LiqAPIFieldMapper(name = "OutstandingIdentifier", className = "com.misys.liq.api.data.outstanding.LiqAPIOutstandingIdentifier")
    public LiqAPIOutstandingIdentifier outstandingIdentifier;

    public Boolean autoReduceFacility;
    public Boolean prepaymentIndicator;

    public Object basicExecute() {
        try {
            checkDealSecurity();
            checkCustomerSecurity();
            lockAPIData();
            super.basicExecute();
            createIdempotency();
            this.singleCommit();
        } catch(Exception ex) {
            LoanIQ.currentSession().setSingleCommitMode(false);
            LoanIQ.currentSession().rollback();
            LoanIQ.logError(Messages.liqNlsExternalizedMessage(
                "Exception occured during Unscheduled Loan Principal Payment Creation.").concat(ex.getMessage()), ex);
            ExceptionUtility.throwException(new LiqError(
                Messages.liqNlsExternalizedMessage(ex.getMessage()), this));
        } finally {
            this.unLockAPIData();
        }
        return response();
    }
}
```

---

## 6. Pattern B — Complex Error Recovery (QuickLoanRepricing)

**Source:** `LoanIQ/srcgen/com/misys/liq/api/rest/executable/outstanding/qlr/LiqAPICreateQuickLoanRepricingIntegration.java`

```java
package com.misys.liq.api.rest.executable.outstanding.qlr;

import com.finastra.liq.api.annotation.LiqAPIFieldMapper;
import com.misys.liq.api.data.outstanding.LiqAPIOutstandingIdentifier;
import com.misys.liq.api.executable.IAPICreateRestIntegration;
import com.misys.liq.infrastructure.exceptions.LiqError;
import com.sxsy.smtj.exceptions.ExceptionUtility;

/**
 * Quick Loan Repricing — complex error recovery with RefreshRates cleanup.
 */
public class LiqAPICreateQuickLoanRepricingIntegration extends LiqAPICreateQuickRepricing
        implements IAPICreateRestIntegration {

    public static final Class clazz = new Class();

    @LiqAPIFieldMapper(name = "OutstandingIdentifier", className = "com.misys.liq.api.data.outstanding.LiqAPIOutstandingIdentifier")
    public LiqAPIOutstandingIdentifier outstandingIdentifier;

    @LiqAPIFieldMapper(name = "COFPricingFormula", className = "com.misys.liq.api.rest.executable.outstanding.qlr.LiqAPICOFPricingFormulaIntegration")
    public LiqAPICOFPricingFormulaIntegration cofPricingFormula;

    @LiqAPIFieldMapper(name = "InterestPayment", className = "com.misys.liq.api.rest.executable.outstanding.interest.LiqAPICreateQuickRepricingInterestPaymentIntegration")
    public LiqAPICreateQuickRepricingInterestPaymentIntegration interestPayment;

    @LiqAPIFieldMapper(name = "SpreadAdjustmentComponents", className = "com.misys.liq.api.rest.data.outstanding.spread.LiqAPISpreadAdjustmentComponentOverrideIntegration")
    public List<LiqAPISpreadAdjustmentComponentOverrideIntegration> ostSpreadAdjustmentComponents;

    public Boolean interestRateIsFloating;
    public Boolean repricingFrequencyApplies;
    public String eventComment;

    public Object basicExecute() {
        try {
            checkDealSecurity();
            checkCustomerSecurity();
            this.lockAPIData();
            super.basicExecute();
            createIdempotency();
            this.updateSpreadComponent();
            this.singleCommit();
        } catch(Exception e) {
            if (this.getNewQuickRepricing() != null
                    && this.getNewQuickRepricing().getPrivateState().contains("RefreshRates")) {
                this.getNewQuickRepricing().delete();
                if (LiqMNIdempotencyKey.clazz.getIdempotencyKey(this.getIdempotencyKey()) != null) {
                    LiqMNIdempotencyKey.clazz.getIdempotencyKey(this.getIdempotencyKey()).delete();
                }
                this.singleCommit();
            }
            ExceptionUtility.throwException(new LiqError(e, this));
        } finally {
            if (this.getNewQuickRepricing() != null) {
                this.getNewQuickRepricing().getPrivateState().remove("RefreshRates");
            }
            this.unLockAPIData();
        }
        return response();
    }
}
```

---

## 7. JSON Request Examples

### SBLC Decrease Request
```json
{
  "sblcIdentifier": {
    "dealName": "DEAL001",
    "facilityName": "FAC001"
  },
  "eventComment": "SBLC decrease request",
  "transactionDescription": "Decrease SBLC by 50000",
  "systemSourceId": "EXT_SYS_001",
  "sourceRefNum": "REF12345"
}
```

### User Profile Request
```json
{
  "languageCode": "EN",
  "groupAddress": "Group A - Operations",
  "userDepartments": [
    { "departmentCode": "DEPT001" }
  ],
  "userWorkflowTemplateGroups": [
    { "groupName": "WF_APPROVAL" }
  ],
  "userSecondaryProcessingAreas": [
    { "processingAreaCode": "PA001" }
  ]
}
```

### User Security Profile Request
```json
{
  "userProfileRID": "USR00001",
  "databaseConnectionId": "DB_CONN_PRIMARY",
  "comment": "New security profile for operations user",
  "osUserId": "jsmith"
}
```

### Facility Ongoing Fee Payment Request
```json
{
  "facilityIdentifier": {
    "dealName": "DEAL001",
    "facilityName": "FAC001"
  },
  "feeIdentifier": {
    "feeDescription": "Commitment Fee"
  },
  "effectiveDate": "2024-06-15",
  "systemSourceId": "SRC001",
  "sourceRefNum": "FEEREF001",
  "transactionDescription": "Quarterly fee payment",
  "comment": "Q2 2024 payment"
}
```

### Holiday Calendar Date Request
```json
{
  "holidayCalendarCode": "USD",
  "holidayDate": "2024-12-25",
  "description": "Christmas Day",
  "activeIndicator": true
}
```

### Unscheduled Loan Principal Payment Request
```json
{
  "outstandingIdentifier": {
    "dealName": "DEAL001",
    "facilityName": "FAC001",
    "outstandingAlias": "OST001"
  },
  "autoReduceFacility": true,
  "prepaymentIndicator": false
}
```
