# LoanIQ Delete API — Unified Example Reference

This file contains representative Java source examples for all Delete API patterns.
Each example demonstrates the complete class structure from the actual repository source.

---

## 1. Pattern A: Owner-Based Single Entity — DealAdministrator

**Source**: `LiqAPIDeleteDealAdministratorIntegration.java`  
**Package**: `com.misys.liq.api.rest.executable.dealadministrator`

```java
package com.misys.liq.api.rest.executable.dealadministrator;

import com.finastra.liq.api.annotation.LiqAPIFieldMapper;
import com.misys.liq.Messages;
import com.misys.liq.api.constants.APICommonConstants;
import com.misys.liq.api.data.LiqAPINonPrimitiveFieldMapping;
import com.misys.liq.api.executable.IAPIRestIntegration;
import com.misys.liq.api.executable.LiqAPIExecutableData;
import com.misys.liq.api.rest.constants.ErrorMessageConstants;
import com.misys.liq.api.rest.data.dealadministrator.LiqAPIDealAdministratorIntegrationAsReturnValue;
import com.misys.liq.api.rest.data.identifier.LiqAPIOwnerIdentifier;
import com.misys.liq.bm.desktopcore.main.cdt.deal.Deal;
import com.misys.liq.bm.desktopcore.main.cdt.deal.DealAdministrator;
import com.misys.liq.bm.desktopcore.main.cdt.deal.DealBorrower;
import com.misys.liq.infrastructure.LiqUtilities;
import com.misys.liq.infrastructure.bm.labstrct.KeyedDataObject;
import com.misys.liq.infrastructure.bm.labstrct.LS2UpdateableData;
import com.misys.liq.infrastructure.enfinbasesupport.LiqBusinessObject;
import com.misys.liq.infrastructure.exceptions.LiqError;
import com.misys.liq.infrastructure.exceptions.LiqMessageException;
import com.sxsy.smtj.StClass;
import com.sxsy.smtj.StClassRegistry;
import com.sxsy.smtj.StObject;
import com.sxsy.smtj.exceptions.ExceptionUtility;
import com.sxsy.smtj.utilities.CollectionUtility;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Integration API class for deleting Deal Administrator in LoanIQ.
 */
public class LiqAPIDeleteDealAdministratorIntegration extends LiqAPIExecutableData implements StObject, IAPIRestIntegration {

    @LiqAPIFieldMapper(name = "OwnerIdentifier", className = "com.misys.liq.api.rest.data.identifier.LiqAPIOwnerIdentifier")
    public LiqAPIOwnerIdentifier ownerIdentifier;

    KeyedDataObject bo;
    private boolean deleted = false;
    public static final Class clazz = new Class();
    static { StClassRegistry.register(clazz); }

    public StClass getStClass() { return clazz; }
    public String securityAccessSymbol() { return APICommonConstants.SECURITY_ACCESS_SYMBOL_DELETE_DEAL_ADMINISTRATOR; }

    public LiqAPIOwnerIdentifier getOwnerIdentifier() { return ownerIdentifier; }
    public void setOwnerIdentifier(LiqAPIOwnerIdentifier ownerIdentifier) { this.ownerIdentifier = ownerIdentifier; }

    public KeyedDataObject getBusinessObject() {
        if (ownerIdentifier != null && ownerIdentifier.getOwnerType().toUpperCase().equals("DEA")) {
            bo = ownerIdentifier.getDeal();
        }
        return bo;
    }

    @Override
    public void basicValidate() {
        super.basicValidate();
        validateIdentifiers();
        validateDealAdministratorExists();
        validateTimeStamp(getBusinessObject(), this.getMatchUpdatedTimestamp());
    }

    public void validateDealAdministratorExists() {
        Deal deal = getDealFromOwnerIdentifier();
        if (deal == null) {
            ExceptionUtility.throwException(new LiqError(
                Messages.liqNlsExternalizedMessage("Deal not found for the given owner identifier."), this));
        }
        DealAdministrator dealAdmin = deal.getDealAdministrator();
        if (dealAdmin == null || dealAdmin.getCustomerId() == null) {
            ExceptionUtility.throwException(new LiqError(
                Messages.liqNlsExternalizedMessage("Deal Administrator does not exist for the given Deal."), this));
        }
    }

    public void validateIdentifiers() {
        if (ownerIdentifier != null) { ownerIdentifier.basicValidate(); }
        else { ExceptionUtility.throwException(new LiqError(ErrorMessageConstants.OWNER_IDENTIFIER_REQUIRED)); }
        ownerIdentifier.validateForOwnerObject();
        if (!ownerIdentifier.getOwnerType().toUpperCase().equals("DEA")) {
            ExceptionUtility.throwException(new LiqError(
                Messages.liqNlsExternalizedMessage("Unsupported owner type: " + ownerIdentifier.getOwnerType()), this));
        }
    }

    private Deal getDealFromOwnerIdentifier() {
        if (ownerIdentifier != null && ownerIdentifier.getOwnerType().toUpperCase().equals("DEA")) {
            return ownerIdentifier.getDeal();
        }
        return null;
    }

    public Object basicExecute() {
        try {
            checkDealSecurity();
            checkCustomerSecurity();
            this.lockAPIData();
            this.deleteDealAdministrator();
            this.singleCommit();
        } finally {
            this.unLockAPIData();
        }
        return response();
    }

    @Override
    public void checkDealSecurity() {
        dealSecurity(ownerIdentifier.getDeal().getDealId());
    }

    @Override
    public void checkCustomerSecurity() {
        Iterator iterator = ((Deal) getBusinessObject()).getBorrowers().iterator();
        while (iterator.hasNext()) {
            DealBorrower borrower = (DealBorrower) iterator.next();
            customerSecurity(borrower.getCustomerId());
        }
    }

    @Override
    public Object response() {
        Object object = LiqAPIDealAdministratorIntegrationAsReturnValue.clazz.forDelete(ownerIdentifier);
        this.addIds(List.of(this.getBusinessObject()));
        return this.deleted ? object : "";
    }

    @Override
    public void addIds(List<LS2UpdateableData> objects) {
        if (null == objects || objects.isEmpty()) return;
        setIds(objects.stream().map(tran -> tran.getId()).collect(Collectors.toList()));
    }

    @Override
    public void lockAPIData() {
        LiqBusinessObject anObject = (LiqBusinessObject) getBusinessObject();
        if (anObject != null) {
            try { anObject.exclusiveUpdateLockFor(this); }
            catch (final LiqMessageException ex) { ExceptionUtility.throwException(new LiqError(ex, this)); }
        }
    }

    @Override
    public void unLockAPIData() {
        LiqBusinessObject liqObject = (LiqBusinessObject) getBusinessObject();
        if (liqObject != null) {
            try { liqObject.exclusiveUpdateUnlockFor(this); }
            catch (final LiqMessageException ex) { ExceptionUtility.throwException(new LiqError(ex, this)); }
        }
    }

    private void deleteDealAdministrator() {
        try {
            if (ownerIdentifier.getOwnerType().toUpperCase().equals("DEA")) {
                deleteDealAdministratorForDeal();
            }
            deleted = true;
        } catch (Exception e) {
            ExceptionUtility.throwException(new LiqError(Messages.liqNlsExternalizedMessage(e.getMessage()), this));
        }
    }

    private void deleteDealAdministratorForDeal() {
        Deal deal = this.ownerIdentifier.getDeal();
        if (deal != null) {
            DealAdministrator dealAdmin = deal.getDealAdministrator();
            if (dealAdmin != null && dealAdmin.getCustomerId() != null) {
                LiqUtilities.liqTriggerRegistryEvent(this, "validateSimplifiedDealForDelete", CollectionUtility.listWith(this));
                dealAdmin.lockConstructionBudgetsForDelete(true);
                dealAdmin.updateLockedBudgets();
                dealAdmin.updateAndSaveSalesTarget();
                this.saveLockedBudgetsForAPI(dealAdmin);
                dealAdmin.delete();
                deal.save();
            }
        }
    }

    public void saveLockedBudgetsForAPI(DealAdministrator dealAdmin) {
        if (dealAdmin.lockedConstructionBudgets != null) {
            Iterator iterator = dealAdmin.lockedConstructionBudgets.iterator();
            while (iterator.hasNext()) { LiqUtilities.save(iterator.next()); }
        }
    }

    public static class Class extends LiqAPIExecutableData.Class implements StClass {
        protected Class() {}
        public StObject basicNew() { return new LiqAPIDeleteDealAdministratorIntegration(); }
        public java.lang.Class getJavaClass() { return LiqAPIDeleteDealAdministratorIntegration.class; }
        public StClass getStSuperclass() { return LiqAPIExecutableData.clazz; }
        public List nonPrimitiveFieldMappings() {
            List mappings = super.nonPrimitiveFieldMappings();
            LiqAPINonPrimitiveFieldMapping m = (LiqAPINonPrimitiveFieldMapping) LiqAPINonPrimitiveFieldMapping.clazz.newStObject();
            m.setFieldName("ownerIdentifier");
            m.setFieldApiClass(LiqAPIOwnerIdentifier.clazz);
            mappings.add(m);
            return mappings;
        }
        public List primitiveFieldMappings() { return super.primitiveFieldMappings(); }
        @Override public boolean isRest() { return true; }
        public String securityFunctionParent() { return "Deal"; }
        public String getOperationSummary() { return "Delete Deal Administrator from Deal"; }
    }
}
```

---

## 2. Pattern C: Direct Entity Deletion — Deal

**Source**: `LiqAPIDeleteDealIntegration.java`  
**Package**: `com.misys.liq.api.rest.executable.deal`

```java
package com.misys.liq.api.rest.executable.deal;

import com.finastra.liq.api.annotation.LiqAPIFieldMapper;
import com.misys.liq.LoanIQ;
import com.misys.liq.Messages;
import com.misys.liq.api.constants.APICommonConstants;
import com.misys.liq.api.data.LiqAPINonPrimitiveFieldMapping;
import com.misys.liq.api.executable.IAPIRestIntegration;
import com.misys.liq.api.executable.LiqAPIExecutableData;
import com.misys.liq.api.rest.constants.ErrorMessageConstants;
import com.misys.liq.api.rest.data.deal.LiqAPIDealIdentifier;
import com.misys.liq.api.rest.data.deal.LiqAPIDealIntegrationAsReturnValue;
import com.misys.liq.api.rest.data.workflow.WorkflowStatusIntegration;
import com.misys.liq.bm.desktopcore.main.cdt.codetabl.TimeRegionCode;
import com.misys.liq.bm.desktopcore.main.cdt.deal.Deal;
import com.misys.liq.bm.desktopcore.main.cdt.deal.DealBorrower;
import com.misys.liq.infrastructure.bm.labstrct.LS2UpdateableData;
import com.misys.liq.infrastructure.exceptions.LiqError;
import com.misys.liq.infrastructure.exceptions.LiqMessageException;
import com.sxsy.smtj.StClass;
import com.sxsy.smtj.StClassRegistry;
import com.sxsy.smtj.StObject;
import com.sxsy.smtj.exceptions.ExceptionUtility;
import com.sxsy.smtj.utilities.StringUtility;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Integration class for deleting Deal with workflow status validation.
 */
public class LiqAPIDeleteDealIntegration extends LiqAPIExecutableData implements StObject, IAPIRestIntegration {
    private static final Logger LOG = org.apache.logging.log4j.LogManager.getLogger(LiqAPIDeleteDealIntegration.class);
    public static final Class clazz = new Class();
    static { StClassRegistry.register(clazz); }
    public StClass getStClass() { return clazz; }

    @LiqAPIFieldMapper(name = "DealIdentifier", className = "com.misys.liq.api.rest.data.deal.LiqAPIDealIdentifier")
    public LiqAPIDealIdentifier dealIdentifier;
    public Deal deal;

    @Override public LiqAPIExecutableData validateLicense() { return this; }

    public Deal getDeal() {
        if (Objects.isNull(this.deal)) {
            validateDealIdentifier();
            this.deal = getDealIdentifier().getDeal();
        }
        return this.deal;
    }

    public void basicValidate() {
        validateDealIdentifier();
        validateDealWorkflowStatus();
    }

    private void validateDealWorkflowStatus() {
        if (Objects.nonNull(this.getDeal()) &&
            !(((LiqAPIDeleteDealIntegration.Class) this.getStClass()).validStatusCodes().contains(this.getDeal().getObjectStateCode()))) {
            ExceptionUtility.throwException(new LiqError(Messages.liqNlsExternalizedMessage(
                StringUtility.bindWith(ErrorMessageConstants.INVALID_DELETE_WORKFLOW_STATUS,
                    WorkflowStatusIntegration.DEAL_APRVD.getDisplayName())), this));
        }
    }

    private void validateDealIdentifier() {
        if (Objects.nonNull(getDealIdentifier())) {
            getDealIdentifier().basicValidateDelete();
            if (StringUtils.isNotBlank(getDealIdentifier().getIdentifierValue()) &&
                getDealIdentifier().getIdentifierValue().length() > APICommonConstants.ID_MAX_LENGTH) {
                ExceptionUtility.throwException(new LiqError(Messages.liqNlsExternalizedMessage(
                    StringUtility.bindWith(ErrorMessageConstants.DEAL_IDENTIFIER_VALUE_EXCEEDS_LENGTH,
                        getDealIdentifier().getIdentifierValue())), this));
            }
        } else {
            ExceptionUtility.throwException(new LiqError(Messages.liqNlsExternalizedMessage(
                ErrorMessageConstants.DEAL_IDENTIFIER_REQUIRED), this));
        }
    }

    public Object basicExecute() {
        try {
            checkDealSecurity();
            checkCustomerSecurity();
            this.lockAPIData();
            deleteDealNotebook();
            this.singleCommit();
        } finally { this.unLockAPIData(); }
        return response();
    }

    private void deleteDealNotebook() {
        try { this.getDeal().delete(); }
        catch (Exception exception) {
            LOG.error("Failure while deleting Deal: {}", getDealIdentifier().getIdentifierValue(), exception);
            ExceptionUtility.throwException(new LiqError(Messages.liqNlsExternalizedMessage(
                StringUtility.bindWith(ErrorMessageConstants.DEAL_DELETION_ERROR,
                    getDealIdentifier().getIdentifierType(), getDealIdentifier().getIdentifierValue())), this));
        }
    }

    @Override public void checkDealSecurity() { dealSecurity(this.getDeal().getDealId()); }

    @Override
    public void checkCustomerSecurity() {
        if (Objects.nonNull(this.getDeal()) && CollectionUtils.isNotEmpty(this.getDeal().getBorrowers())) {
            for (Object borrower : this.getDeal().getBorrowers()) {
                if (StringUtils.isNotBlank(((DealBorrower) borrower).getId()))
                    customerSecurity(((DealBorrower) borrower).getId());
            }
        }
    }

    @Override
    public Object response() {
        Object object = LiqAPIDealIntegrationAsReturnValue.clazz.forDelete();
        this.addIds(List.of(this.getDeal()));
        return this.getDeal().isDeleted() ? object : "";
    }

    @Override
    public void lockAPIData() {
        if (Objects.nonNull(this.getDeal()) && !this.getDeal().isLockedForUpdate() && !this.getDeal().isLockedForUpdateBy(this)) {
            this.waitMessageBoxText(Messages.messageNumber(200189));
            try { this.getDeal().workstationLockForUpdateBy(this); }
            catch (final LiqMessageException ex) { ExceptionUtility.throwException(new LiqError(ex, this)); }
        }
    }

    @Override
    public void unLockAPIData() {
        this.unlockTimeRegion();
        if (Objects.nonNull(this.getDeal()) && this.getDeal().isLockedForUpdateBy(this)) {
            this.waitMessageBoxText(Messages.messageNumber(200203));
            try { this.getDeal().workstationUnlockForUpdateBy(this); }
            catch (final LiqMessageException ex) { ExceptionUtility.throwException(new LiqError(ex, this)); }
        }
        try { this.getDeal().unlockFacilitiesForDealFlowdown(); }
        catch (Exception e) { ExceptionUtility.throwException(new LiqError(Messages.liqNlsExternalizedMessage(e.toString()), this)); }
    }

    public void unlockTimeRegion() {
        TimeRegionCode timeRegionObject = LoanIQ.currentSession().getSecurityHandler().getUserProfile().getBranchObject().timeRegionObject();
        timeRegionObject.workstationUpdateUnlockFor(this);
    }

    @Override
    public void addIds(List<LS2UpdateableData> objects) {
        if (null == objects || objects.isEmpty()) return;
        setIds(objects.stream().map(tran -> tran.getId()).collect(Collectors.toList()));
    }

    public static class Class extends LiqAPIExecutableData.Class implements StClass {
        protected Class() {}
        public StObject basicNew() { return new LiqAPIDeleteDealIntegration(); }
        public java.lang.Class getJavaClass() { return LiqAPIDeleteDealIntegration.class; }
        public StClass getStSuperclass() { return LiqAPIExecutableData.clazz; }
        public String securityAccessSymbol() { return APICommonConstants.SECURITY_ACCESS_SYMBOL_DEAL_DELETE; }
        public List nonPrimitiveFieldMappings() {
            List mappings = super.nonPrimitiveFieldMappings();
            LiqAPINonPrimitiveFieldMapping m = (LiqAPINonPrimitiveFieldMapping) LiqAPINonPrimitiveFieldMapping.clazz.newStObject();
            m.setFieldName("dealIdentifier");
            m.setFieldApiClass(LiqAPIDealIdentifier.clazz);
            mappings.add(m);
            return mappings;
        }
        public List validStatusCodes() {
            return Arrays.asList(WorkflowStatusIntegration.PEND.getStatus(),
                WorkflowStatusIntegration.AWA.getStatus(),
                WorkflowStatusIntegration.AWSTA.getStatus(),
                WorkflowStatusIntegration.STA.getStatus());
        }
        @Override public boolean isRest() { return true; }
    }

    public LiqAPIDealIdentifier getDealIdentifier() { return dealIdentifier; }
    public void setDealIdentifier(LiqAPIDealIdentifier dealIdentifier) { this.dealIdentifier = dealIdentifier; }
    public String securityAccessSymbol() { return APICommonConstants.SECURITY_ACCESS_SYMBOL_DEAL_DELETE; }
}
```

---

## 3. Pattern C: Direct Entity — UpfrontFee (with validateForDelete)

**Source**: `LiqAPIDeleteUpfrontFeeIntegration.java`  
**Package**: `com.misys.liq.api.rest.executable.upfrontfee`

```java
package com.misys.liq.api.rest.executable.upfrontfee;

import com.finastra.liq.api.annotation.LiqAPIFieldMapper;
import com.misys.liq.Messages;
import com.misys.liq.api.data.LiqAPINonPrimitiveFieldMapping;
import com.misys.liq.api.executable.IAPIRestIntegration;
import com.misys.liq.api.executable.LiqAPIExecutableData;
import com.misys.liq.api.rest.data.upfrontfee.LiqAPIUpfrontFeeIdentifier;
import com.misys.liq.api.rest.data.upfrontfee.LiqAPIUpfrontFeeIntegrationAsReturnValue;
import com.misys.liq.bm.desktopcore.main.cdt.deal.UpfrontFeeFromCustomer;
import com.misys.liq.infrastructure.bm.labstrct.KeyedDataObject;
import com.misys.liq.infrastructure.bm.labstrct.LS2UpdateableData;
import com.misys.liq.infrastructure.enfinbasesupport.LiqBusinessObject;
import com.misys.liq.infrastructure.exceptions.LiqError;
import com.misys.liq.infrastructure.exceptions.LiqMessageException;
import com.sxsy.smtj.StClass;
import com.sxsy.smtj.StClassRegistry;
import com.sxsy.smtj.StObject;
import com.sxsy.smtj.exceptions.ExceptionUtility;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Integration API class for deleting an upfront fee in LoanIQ.
 * Uses validateForDelete() pre-check before deletion.
 */
public class LiqAPIDeleteUpfrontFeeIntegration extends LiqAPIExecutableData implements StObject, IAPIRestIntegration {
    protected KeyedDataObject deletedLiqBusinessObject;

    @LiqAPIFieldMapper(name = "UpfrontFeeIdentifier", className = "com.misys.liq.api.rest.data.upfrontfee.LiqAPIUpfrontFeeIdentifier")
    public LiqAPIUpfrontFeeIdentifier upfrontFeeIdentifier;

    private UpfrontFeeFromCustomer upfrontFeeTransaction;
    public static final Class clazz = new Class();
    static { StClassRegistry.register(clazz); }

    public StClass getStClass() { return clazz; }
    public String securityAccessSymbol() { return "DeleteUpfrontFeeIntegration"; }

    @Override
    public void basicValidate() {
        if (upfrontFeeIdentifier != null) { upfrontFeeIdentifier.basicValidate(); }
        this.validateTimeStamp(getUpfrontFeeTransaction(), getMatchUpdatedTimestamp());
    }

    public UpfrontFeeFromCustomer getUpfrontFeeTransaction() {
        upfrontFeeTransaction = this.getUpfrontFeeIdentifier().getUpfrontFee();
        if (Objects.nonNull(upfrontFeeTransaction)) { loadObjects(List.of(upfrontFeeTransaction)); }
        return upfrontFeeTransaction;
    }

    public Object basicExecute() {
        try {
            checkDealSecurity();
            checkCustomerSecurity();
            this.lockAPIData();
            this.deleteUpfrontFee();
            this.singleCommit();
        } finally { this.unLockAPIData(); }
        return response();
    }

    @Override public void checkDealSecurity() { if (getUpfrontFeeTransaction() != null) dealSecurity(getUpfrontFeeTransaction().getDealId()); }
    @Override public void checkCustomerSecurity() { if (getUpfrontFeeTransaction() != null) customerSecurity(getUpfrontFeeTransaction().getBorrowerId()); }

    @Override
    public Object response() {
        Object object = LiqAPIUpfrontFeeIntegrationAsReturnValue.clazz.forDelete();
        this.addIds(List.of(this.upfrontFeeTransaction));
        return this.upfrontFeeTransaction.isDeleted() ? object : "";
    }

    @Override
    public void lockAPIData() {
        if (getUpfrontFeeTransaction() != null) {
            try { ((LiqBusinessObject) getUpfrontFeeTransaction()).exclusiveUpdateLockFor(this); }
            catch (final LiqMessageException ex) { ExceptionUtility.throwException(new LiqError(ex, this)); }
        }
    }

    @Override
    public void unLockAPIData() {
        if (upfrontFeeTransaction != null) {
            try { ((LiqBusinessObject) upfrontFeeTransaction).exclusiveUpdateUnlockFor(this); }
            catch (final LiqMessageException ex) { ExceptionUtility.throwException(new LiqError(ex, this)); }
        }
    }

    public void deleteUpfrontFee() {
        try {
            UpfrontFeeFromCustomer transaction = getUpfrontFeeTransaction();
            if (transaction != null) {
                transaction.validateForDelete();
                transaction.delete();
            }
        } catch (Exception e) {
            ExceptionUtility.throwException(new LiqError(
                Messages.liqNlsExternalizedMessage("Failed to delete upfront fee: " + e.getMessage()), this));
        }
    }

    public static class Class extends LiqAPIExecutableData.Class implements StClass {
        protected Class() {}
        public StObject basicNew() { return new LiqAPIDeleteUpfrontFeeIntegration(); }
        public java.lang.Class getJavaClass() { return LiqAPIDeleteUpfrontFeeIntegration.class; }
        public StClass getStSuperclass() { return LiqAPIExecutableData.clazz; }
        public List nonPrimitiveFieldMappings() {
            List mappings = super.nonPrimitiveFieldMappings();
            LiqAPINonPrimitiveFieldMapping m = (LiqAPINonPrimitiveFieldMapping) LiqAPINonPrimitiveFieldMapping.clazz.newStObject();
            m.setFieldName("upfrontFeeIdentifier");
            m.setFieldApiClass(LiqAPIUpfrontFeeIdentifier.clazz);
            mappings.add(m);
            return mappings;
        }
        @Override public boolean isRest() { return true; }
    }

    public LiqAPIUpfrontFeeIdentifier getUpfrontFeeIdentifier() { return upfrontFeeIdentifier; }
    public void setUpfrontFeeIdentifier(LiqAPIUpfrontFeeIdentifier id) { this.upfrontFeeIdentifier = id; }
}
```

---

## 4. Pattern B: Polymorphic Owner — MISCode (DEA/FAC/LNID)

**Source**: `LiqAPIDeleteMISCodeIntegration.java`  
**Package**: `com.misys.liq.api.rest.executable.miscode`

```java
package com.misys.liq.api.rest.executable.miscode;

import com.finastra.liq.api.annotation.LiqAPIFieldMapper;
import com.misys.liq.Messages;
import com.misys.liq.api.constants.APICommonConstants;
import com.misys.liq.api.data.LiqAPINonPrimitiveFieldMapping;
import com.misys.liq.api.executable.IAPIRestIntegration;
import com.misys.liq.api.executable.LiqAPIExecutableData;
import com.misys.liq.api.rest.constants.ErrorMessageConstants;
import com.misys.liq.api.rest.data.deal.LiqAPIMISCodeIntegration;
import com.misys.liq.api.rest.data.identifier.LiqAPIOwnerIdentifier;
import com.misys.liq.api.rest.data.miscode.LiqAPIMISCodeIntegrationAsReturnValue;
import com.misys.liq.bm.desktopcore.main.cdt.deal.Deal;
import com.misys.liq.bm.desktopcore.main.cdt.deal.DealBorrower;
import com.misys.liq.bm.desktopcore.main.cdt.facility.Facility;
import com.misys.liq.bm.desktopcore.main.cdt.loan.OutstandingTransaction;
import com.misys.liq.infrastructure.bm.labstrct.KeyedDataObject;
import com.misys.liq.infrastructure.bm.labstrct.LS2UpdateableData;
import com.misys.liq.infrastructure.bm.labstrct.MISCode;
import com.misys.liq.infrastructure.enfinbasesupport.LiqBusinessObject;
import com.misys.liq.infrastructure.exceptions.LiqError;
import com.misys.liq.infrastructure.exceptions.LiqMessageException;
import com.sxsy.smtj.StClass;
import com.sxsy.smtj.StClassRegistry;
import com.sxsy.smtj.StObject;
import com.sxsy.smtj.exceptions.ExceptionUtility;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Integration API for deleting MIS codes from Deal, Facility, or Outstanding.
 * Demonstrates polymorphic owner resolution (DEA/FAC/LNID).
 */
public class LiqAPIDeleteMISCodeIntegration extends LiqAPIExecutableData implements StObject, IAPIRestIntegration {

    @LiqAPIFieldMapper(name = "OwnerIdentifier", className = "com.misys.liq.api.rest.data.identifier.LiqAPIOwnerIdentifier")
    public LiqAPIOwnerIdentifier ownerIdentifier;

    @LiqAPIFieldMapper(name = "MISCodeIntegration", className = "com.misys.liq.api.rest.data.deal.LiqAPIMISCodeIntegration")
    public List<LiqAPIMISCodeIntegration> misCodes;

    KeyedDataObject bo;
    private boolean deleted = false;
    public static final Class clazz = new Class();
    static { StClassRegistry.register(clazz); }

    public StClass getStClass() { return clazz; }
    public String securityAccessSymbol() { return APICommonConstants.SECURITY_ACCESS_SYMBOL_DELETE_MIS_CODE; }

    /**
     * Polymorphic owner resolution — supports Deal, Facility, Outstanding.
     */
    public KeyedDataObject getBusinessObject() {
        if (ownerIdentifier != null && ownerIdentifier.getOwnerType().toUpperCase().equals("DEA")) {
            bo = ownerIdentifier.getDeal();
        } else if (ownerIdentifier != null && ownerIdentifier.getOwnerType().toUpperCase().equals("FAC")) {
            bo = ownerIdentifier.getFacility();
        } else if (ownerIdentifier != null && ownerIdentifier.getOwnerType().toUpperCase().equals("LNID")) {
            bo = ownerIdentifier.getOutstandingTransaction();
        }
        return bo;
    }

    @Override
    public void basicValidate() {
        super.basicValidate();
        validateIdentifiers();
        validateMisCodes();
        validateTimeStamp(getBusinessObject(), this.getMatchUpdatedTimestamp());
    }

    public Object basicExecute() {
        try {
            checkDealSecurity();
            checkCustomerSecurity();
            this.lockAPIData();
            this.deleteMISCodes();
            this.singleCommit();
        } finally { this.unLockAPIData(); }
        return response();
    }

    // deleteMISCodes() iterates misCodes list, matches and removes from BO
    // Security and lock follow owner-type-specific logic
}
```

---

## 5. Pattern B: Polymorphic Owner — FacilityInterestPricing (Pricing Node Deletion)

**Source**: `LiqAPIDeleteFacilityInterestPricingIntegration.java`  
**Package**: `com.misys.liq.api.rest.executable.facility`

```java
package com.misys.liq.api.rest.executable.facility;

import com.finastra.liq.api.annotation.LiqAPIFieldMapper;
import com.misys.liq.api.executable.IAPIRestIntegration;
import com.misys.liq.api.executable.LiqAPIExecutableData;
import com.misys.liq.api.rest.data.facility.LiqAPIFacilityIntegrationAsReturnValue;
import com.misys.liq.api.rest.data.identifier.LiqAPIOwnerIdentifier;
import com.misys.liq.bm.desktopcore.main.cdt.pricing.IPricingItem;
import com.misys.liq.bm.desktopcore.main.cdt.pricing.shr.PricingNode;
import com.misys.liq.bm.desktopcore.main.cdt.pricing.shr.PricingType;
import com.misys.liq.infrastructure.enfinbasesupport.LiqBusinessObject;
import com.sxsy.smtj.StClass;
import com.sxsy.smtj.StObject;
import com.sxsy.smtj.utilities.CompareUtility;
import com.sxsy.smtj.utilities.CollectionUtility;

import java.util.*;

/**
 * Deletes specific interest pricing formula from facility based on pricing option name.
 * Demonstrates pricing node iteration and matrix handling.
 */
public class LiqAPIDeleteFacilityInterestPricingIntegration extends LiqAPIExecutableData
        implements StObject, IAPIRestIntegration {

    @LiqAPIFieldMapper(name = "OwnerIdentifier", className = "com.misys.liq.api.rest.data.identifier.LiqAPIOwnerIdentifier")
    public LiqAPIOwnerIdentifier ownerIdentifier;
    public String optionName;
    public Facility facility;

    public void deleteInterestPricing(PricingType pricingObj, String optionName) {
        Object object = null;
        Iterator iterator = pricingObj.getPricingNodeList().iterator();
        boolean found = false;
        while (iterator.hasNext()) {
            Object e = iterator.next();
            if (CompareUtility.equals(((LiqBusinessObject) e).getOptionName(), optionName)) {
                object = e; found = true; break;
            }
        }
        if (!found) { /* throw error */ }
        Object aPricingNode = object;
        if (((IPricingItem) aPricingNode).isMatrix())
            aPricingNode = ((PricingNode) aPricingNode).ultimateOwner();
        ((PricingNode) aPricingNode).markForDeletion();
        this.deletePricing(pricingObj);
    }

    public void deletePricing(PricingType pricingObj) {
        List deletedNodes = pricingObj.removeDeletedNodes();
        pricingObj.setDeleteNodeList((Collection) CollectionUtility.concat(pricingObj.getDeleteNodeList(), deletedNodes));
        pricingObj.saveNodes();
        pricingObj.validatePricingOrDraftPricingForSave();
        ((ObjectWithTIPs) this.getFacility()).savePricingRelatedDataFor(pricingObj);
    }
}
```

---

## 6. Pattern D: Inherited Delegation — FlexUnscheduledTransaction

**Source**: `LiqAPIDeleteFlexUnscheduledTransactionIntegration.java`  
**Package**: `com.misys.liq.api.executable.outstanding`

```java
package com.misys.liq.api.executable.outstanding;

import com.misys.liq.api.data.LiqAPIData;
import com.misys.liq.api.executable.IAPIRestIntegration;
import com.misys.liq.api.executable.LiqAPIExecutableData;
import com.misys.liq.infrastructure.bm.labstrct.LS2UpdateableData;
import com.sxsy.smtj.StClass;
import com.sxsy.smtj.StClassRegistry;
import com.sxsy.smtj.StObject;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST Integration class for deleting Flex Unscheduled Transactions.
 * Extends parent class — delegates core logic via super.basicExecute().
 */
public class LiqAPIDeleteFlexUnscheduledTransactionIntegration
        extends LiqAPIDeleteFlexUnscheduledTransaction implements StObject, IAPIRestIntegration {

    private static final Logger LOG = org.apache.logging.log4j.LogManager.getLogger(
        LiqAPIDeleteFlexUnscheduledTransactionIntegration.class);
    public static final Class clazz = new Class();
    static { StClassRegistry.register(clazz); }
    public StClass getStClass() { return clazz; }

    public LiqAPIData basicExecute() {
        try {
            checkDealSecurity();
            checkCustomerSecurity();
            this.lockAPIData();
            super.basicExecute();  // Delegates to parent
            this.singleCommit();
        } catch (Exception e) {
            LOG.error("Error: ", e.getMessage(), e);
            throw e;
        } finally { this.unLockAPIData(); }
        return response();
    }

    @Override public LiqAPIExecutableData validateLicense() { return this; }
    @Override public void checkCustomerSecurity() { customerSecurity(this.outstanding().getBorrowerId()); }
    @Override public void checkDealSecurity() { dealSecurity(this.outstanding().getDealId()); }
    @Override public LiqAPIData response() { return this.returnValue(); }
    @Override public void lockAPIData() { /* parent handles */ }
    @Override public void unLockAPIData() { /* parent handles */ }
    @Override public void addIds(List<LS2UpdateableData> objects) {
        if (null == objects || objects.isEmpty()) return;
        setIds(objects.stream().map(tran -> tran.getId()).collect(Collectors.toList()));
    }
    public String securityAccessSymbol() { return "DeleteFlexUnscheduledTransaction"; }

    public static class Class extends LiqAPIDeleteFlexUnscheduledTransaction.Class implements StClass {
        protected Class() {}
        public StObject basicNew() { return new LiqAPIDeleteFlexUnscheduledTransactionIntegration(); }
        public java.lang.Class getJavaClass() { return LiqAPIDeleteFlexUnscheduledTransactionIntegration.class; }
        public StClass getStSuperclass() { return LiqAPIDeleteFlexUnscheduledTransaction.clazz; }
        public String securityAccessSymbol() { return "DeleteFlexUnscheduledTransaction"; }
        public boolean isRest() { return true; }
    }
}
```

---

## 7. Pattern C: Direct Entity — LoanPrincipalPayment (with UncaughtExceptionHandler)

**Source**: `LiqAPIDeleteLoanPrincipalPaymentIntegration.java`  
**Package**: `com.misys.liq.api.rest.executable.outstanding.principal`

```java
package com.misys.liq.api.rest.executable.outstanding.principal;

import com.finastra.liq.api.annotation.LiqAPIFieldMapper;
import com.misys.liq.APIWorkerUncaughtExceptionHandler;
import com.misys.liq.LoanIQ;
import com.misys.liq.Messages;
import com.misys.liq.api.data.outstanding.LiqAPIOutstandingTransactionIdentifier;
import com.misys.liq.api.executable.IAPIRestIntegration;
import com.misys.liq.api.executable.LiqAPIExecutableData;
import com.misys.liq.api.rest.data.outstanding.principal.LiqAPILoanPrincipalPaymentIntegrationAsReturnValue;
import com.misys.liq.bm.desktopcore.main.cdt.loan.LoanPrincipalPayment;
import com.misys.liq.infrastructure.bm.labstrct.LS2UpdateableData;
import com.misys.liq.infrastructure.exceptions.LiqError;
import com.misys.liq.infrastructure.exceptions.LiqMessageException;
import com.sxsy.smtj.StClass;
import com.sxsy.smtj.StClassRegistry;
import com.sxsy.smtj.StObject;
import com.sxsy.smtj.exceptions.ExceptionUtility;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Integration class for deleting a Loan Principal Payment.
 * Demonstrates UncaughtExceptionHandler on lock.
 */
public class LiqAPIDeleteLoanPrincipalPaymentIntegration extends LiqAPIExecutableData
        implements StObject, IAPIRestIntegration {
    public static final Class clazz = new Class();
    public LoanPrincipalPayment loanPrincipalPayment;
    static { StClassRegistry.register(clazz); }
    public StClass getStClass() { return clazz; }

    @LiqAPIFieldMapper(name = "OutstandingTransactionIdentifier", className = "com.misys.liq.api.data.outstanding.LiqAPIOutstandingTransactionIdentifier")
    public LiqAPIOutstandingTransactionIdentifier outstandingTransactionIdentifier;

    @Override public LiqAPIExecutableData validateLicense() { return this; }

    public void basicValidate() {
        if (outstandingTransactionIdentifier != null) { outstandingTransactionIdentifier.basicValidate(); }
        else { ExceptionUtility.throwException(new LiqError(
            Messages.liqNlsExternalizedMessage("Outstanding Transaction Identifier is required."), this)); }
        super.basicValidate();
    }

    public Object basicExecute() {
        try {
            checkDealSecurity();
            checkCustomerSecurity();
            this.lockAPIData();
            this.deleteTransaction();
            this.singleCommit();
        } finally { this.unLockAPIData(); }
        return response();
    }

    @Override public void checkCustomerSecurity() { customerSecurity(getLoanPrincipalPayment().getBorrowerId()); }
    @Override public void checkDealSecurity() { dealSecurity(getLoanPrincipalPayment().getDealId()); }

    @Override
    public Object response() {
        Object object = LiqAPILoanPrincipalPaymentIntegrationAsReturnValue.clazz.forDelete();
        this.addIds(List.of(this.loanPrincipalPayment));
        return this.loanPrincipalPayment.isDeleted() ? object : "";
    }

    public void lockAPIData() {
        try {
            this.getLoanPrincipalPayment().exclusiveUpdateLockFor(this);
            Thread.currentThread().setUncaughtExceptionHandler(new APIWorkerUncaughtExceptionHandler());
            LoanIQ.logInfo("FA_LOCK- User > " + LoanIQ.currentSession().userId() + "-InitStart");
        } catch (final LiqMessageException ex) { ExceptionUtility.throwException(new LiqError(ex, this)); }
    }

    public void unLockAPIData() {
        try {
            this.getLoanPrincipalPayment().exclusiveUpdateUnlockFor(this);
            LoanIQ.logInfo("FA_LOCK- User > " + LoanIQ.currentSession().userId() + "-Unlock");
        } catch (final LiqMessageException ex) { ExceptionUtility.throwException(new LiqError(ex, this)); }
    }

    @Override public void addIds(List<LS2UpdateableData> objects) {
        if (null == objects || objects.isEmpty()) return;
        setIds(objects.stream().map(tran -> tran.getId()).collect(Collectors.toList()));
    }
}
```
