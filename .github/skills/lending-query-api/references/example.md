# LoanIQ Query API — Unified Reference Examples

> This file contains complete source code examples from the repository for multiple Query API business objects.
> Use these as reference when generating or modifying Query API classes for any supported business object.

---

## Example 1: Standard Entity Query (Deal)

**Source:** `LoanIQ/srcgen/com/misys/liq/api/rest/executable/deal/LiqAPIQueryDealIntegration.java`

```java
package com.misys.liq.api.rest.executable.deal;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.misys.liq.api.rest.data.deal.LiqAPIDealIdentifier;
import com.misys.liq.api.rest.data.deal.LiqAPIDealIntegrationAsReturnValue;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.finastra.liq.api.annotation.LiqAPIFieldMapper;
import com.misys.liq.Messages;
import com.misys.liq.api.constants.APICommonConstants;
import com.misys.liq.api.data.LiqAPINonPrimitiveFieldMapping;
import com.misys.liq.api.executable.LiqAPIExecutableData;
import com.misys.liq.api.rest.constants.ErrorMessageConstants;
import com.misys.liq.bm.desktopcore.main.cdt.deal.Deal;
import com.misys.liq.infrastructure.exceptions.LiqError;
import com.sxsy.smtj.StClass;
import com.sxsy.smtj.StClassRegistry;
import com.sxsy.smtj.StObject;
import com.sxsy.smtj.exceptions.ExceptionUtility;
import com.sxsy.smtj.utilities.StringUtility;
import org.apache.logging.log4j.Logger;

/**
 * Integration class for querying Deal.
 */
public class LiqAPIQueryDealIntegration extends LiqAPIExecutableData implements StObject {
    private static final Logger LOG = org.apache.logging.log4j.LogManager.getLogger(LiqAPIQueryDealIntegration.class);
    public static final Class clazz = new Class();

    static {
        StClassRegistry.register(clazz);
    }

    public StClass getStClass() {
        return clazz;
    }

    @LiqAPIFieldMapper(name = "DealIdentifier", className = "com.misys.liq.api.rest.data.deal.LiqAPIDealIdentifier")
    public LiqAPIDealIdentifier dealIdentifier;

    public LiqAPIDealIdentifier getDealIdentifier() {
        return dealIdentifier;
    }

    public void setDealIdentifier(LiqAPIDealIdentifier dealIdentifier) {
        this.dealIdentifier = dealIdentifier;
    }

    @Override
    public LiqAPIExecutableData validateLicense() {
        return this;
    }

    public void basicValidate() {
        if (Objects.nonNull(getDealIdentifier())) {
            getDealIdentifier().basicValidateQuery();
            if (StringUtils.isNotBlank(getDealIdentifier().getIdentifierValue()) && getDealIdentifier().getIdentifierValue().length() > APICommonConstants.ID_MAX_LENGTH) {
                ExceptionUtility.throwException(new LiqError(Messages.liqNlsExternalizedMessage(StringUtility.bindWith(ErrorMessageConstants.DEAL_IDENTIFIER_VALUE_EXCEEDS_LENGTH, getDealIdentifier().getIdentifierValue())), this));
            }
        } else {
            ExceptionUtility.throwException(new LiqError(Messages.liqNlsExternalizedMessage(ErrorMessageConstants.DEAL_IDENTIFIER_REQUIRED), this));
        }
    }

    public Object basicExecute() {
        return LiqAPIDealIntegrationAsReturnValue.clazz.forQuery(getDealNotebooks());
    }

    private List<Deal> getDealNotebooks() {
        List<Deal> dealNotebooks = null;
        try {
            dealNotebooks = List.of((Deal)Deal.clazz.getForId(getDealIdentifier().getIdentifierValue()));
        } catch (Exception exception) {
            LOG.error(ErrorMessageConstants.FAILURE_WHILE_FETCHING_DEAL, getDealIdentifier().getIdentifierValue(), exception.getMessage(), exception);
            getDealIdentifier().throwInvalidIdentifierException();
        }
        if (CollectionUtils.isNotEmpty(dealNotebooks) && Objects.nonNull(dealNotebooks.getFirst())) {
            setIds(dealNotebooks.stream().map(Deal::getId).collect(Collectors.toList()));
            return loadObjects(dealNotebooks);
        } else {
            this.getDealIdentifier().throwInvalidIdentifierException();
        }
        return dealNotebooks;
    }

    public String securityAccessSymbol() {
        return APICommonConstants.SECURITY_ACCESS_SYMBOL_DEAL_QUERY;
    }

    public static class Class extends LiqAPIExecutableData.Class implements StClass {
        protected Class() {}

        public StObject basicNew() {
            return new LiqAPIQueryDealIntegration();
        }

        public java.lang.Class getJavaClass() {
            return LiqAPIQueryDealIntegration.class;
        }

        public StClass getStSuperclass() {
            return LiqAPIExecutableData.clazz;
        }

        public String securityAccessSymbol() {
            return APICommonConstants.SECURITY_ACCESS_SYMBOL_DEAL_QUERY;
        }

        public List nonPrimitiveFieldMappings() {
            List mappings = super.nonPrimitiveFieldMappings();
            LiqAPINonPrimitiveFieldMapping dealIdentifierMapping = (LiqAPINonPrimitiveFieldMapping) LiqAPINonPrimitiveFieldMapping.clazz.newStObject();
            dealIdentifierMapping.setFieldName("dealIdentifier");
            dealIdentifierMapping.setFieldApiClass(LiqAPIDealIdentifier.clazz);
            mappings.add(dealIdentifierMapping);
            return mappings;
        }

        public List primitiveFieldMappings() {
            List t = super.primitiveFieldMappings();
            return t;
        }

        public boolean isRest() {
            return Boolean.TRUE;
        }
    }
}
```

---

## Example 2: Polymorphic Owner Query (MISCode)

**Source:** `LoanIQ/srcgen/com/misys/liq/api/rest/executable/miscode/LiqAPIQueryMISCodeIntegration.java`

```java
package com.misys.liq.api.rest.executable.miscode;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.finastra.liq.api.annotation.LiqAPIFieldMapper;
import com.misys.liq.Messages;
import com.misys.liq.api.constants.APICommonConstants;
import com.misys.liq.api.data.LiqAPINonPrimitiveFieldMapping;
import com.misys.liq.api.executable.LiqAPIExecutableData;
import com.misys.liq.api.rest.data.identifier.LiqAPIOwnerIdentifier;
import com.misys.liq.api.rest.data.miscode.LiqAPIMISCodeIntegrationAsReturnValue;
import com.misys.liq.infrastructure.bm.labstrct.KeyedDataObject;
import com.misys.liq.infrastructure.bm.labstrct.MISCode;
import com.misys.liq.infrastructure.exceptions.LiqError;
import com.sxsy.smtj.StClass;
import com.sxsy.smtj.StClassRegistry;
import com.sxsy.smtj.StObject;
import com.sxsy.smtj.exceptions.ExceptionUtility;

public class LiqAPIQueryMISCodeIntegration extends LiqAPIExecutableData implements StObject {

    public static final Class clazz = new Class();

    static {
        StClassRegistry.register(clazz);
    }

    public StClass getStClass() {
        return clazz;
    }

    @LiqAPIFieldMapper(name = "OwnerIdentifier",
        className = "com.misys.liq.api.rest.data.identifier.LiqAPIOwnerIdentifier")
    public LiqAPIOwnerIdentifier ownerIdentifier;

    KeyedDataObject bo;

    public LiqAPIOwnerIdentifier getOwnerIdentifier() {
        return ownerIdentifier;
    }

    public void setOwnerIdentifier(LiqAPIOwnerIdentifier ownerIdentifier) {
        this.ownerIdentifier = ownerIdentifier;
    }

    @Override
    public LiqAPIExecutableData validateLicense() {
        return this;
    }

    @Override
    public void basicValidate() {
        if (ownerIdentifier != null) {
            ownerIdentifier.basicValidate();
        } else {
            ExceptionUtility.throwException(new LiqError(
                Messages.liqNlsExternalizedMessage("Owner Identifier is required."), this));
        }
    }

    @Override
    public Object basicExecute() {
        return LiqAPIMISCodeIntegrationAsReturnValue.clazz.forQuery(getMisCodes(), ownerIdentifier);
    }

    private List<MISCode> getMisCodes() {
        List<MISCode> misCodes = null;
        bo = getBusinessObject();

        if (Objects.nonNull(bo)) {
            misCodes = bo.getMISCodeList();
        }

        try {
            if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(misCodes)) {
                misCodes = loadObjects(misCodes);
            }
        } catch (Exception ex) {
            ExceptionUtility.throwException(new LiqError(
                Messages.liqNlsExternalizedMessage("Unable to fetch MIS Codes. ")
                    .concat(ex.getMessage()), ex));
        }

        setIds(misCodes.stream().map(tran -> tran.getId()).collect(Collectors.toList()));
        return misCodes;
    }

    public String securityAccessSymbol() {
        return APICommonConstants.SECURITY_ACCESS_SYMBOL_QUERY_MIS_CODE;
    }

    public KeyedDataObject getBusinessObject() {
        if (ownerIdentifier != null) {
            if (ownerIdentifier.getOwnerType().equalsIgnoreCase(APICommonConstants.OWNER_TYPE_DEA)) {
                return ownerIdentifier.getDeal();
            } else if (ownerIdentifier.getOwnerType().equalsIgnoreCase(APICommonConstants.OWNER_TYPE_FAC)) {
                return ownerIdentifier.getFacility();
            }
        }
        return bo;
    }

    public static class Class extends LiqAPIExecutableData.Class implements StClass {
        protected Class() {}

        public StObject basicNew() {
            return new LiqAPIQueryMISCodeIntegration();
        }

        public java.lang.Class getJavaClass() {
            return LiqAPIQueryMISCodeIntegration.class;
        }

        public StClass getStSuperclass() {
            return LiqAPIExecutableData.clazz;
        }

        public List nonPrimitiveFieldMappings() {
            List mappings = super.nonPrimitiveFieldMappings();
            LiqAPINonPrimitiveFieldMapping t1 = (LiqAPINonPrimitiveFieldMapping)
                LiqAPINonPrimitiveFieldMapping.clazz.newStObject();
            t1.setFieldName("ownerIdentifier");
            t1.setFieldApiClass(LiqAPIOwnerIdentifier.clazz);
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
}
```

---

## Example 3: Transaction Query (LoanDrawdown)

**Source:** `LoanIQ/srcgen/com/misys/liq/api/rest/executable/outstanding/drawdown/LiqAPIQueryLoanDrawdownIntegration.java`

```java
package com.misys.liq.api.rest.executable.outstanding.drawdown;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;

import com.finastra.liq.api.annotation.LiqAPIFieldMapper;
import com.misys.liq.Messages;
import com.misys.liq.api.data.LiqAPINonPrimitiveFieldMapping;
import com.misys.liq.api.data.LiqAPIReturnData;
import com.misys.liq.api.rest.data.outstanding.drawdown.LiqAPILoanDrawdownIntegrationAsReturnValue;
import com.misys.liq.api.data.outstanding.LiqAPIOutstandingTransactionIdentifier;
import com.misys.liq.api.executable.LiqAPIExecutableData;
import com.misys.liq.bm.desktopcore.main.cdt.loan.LoanInitialDrawdown;
import com.misys.liq.infrastructure.exceptions.LiqError;
import com.sxsy.smtj.StClass;
import com.sxsy.smtj.StClassRegistry;
import com.sxsy.smtj.StObject;
import com.sxsy.smtj.exceptions.ExceptionUtility;
import com.sxsy.smtj.utilities.CollectionUtility;

public class LiqAPIQueryLoanDrawdownIntegration extends LiqAPIExecutableData implements StObject {

    public static final Class clazz = new Class();

    static {
        StClassRegistry.register(clazz);
    }

    public StClass getStClass() {
        return clazz;
    }

    @LiqAPIFieldMapper(name = "OutstandingTransactionIdentifier",
        className = "com.misys.liq.api.data.outstanding.LiqAPIOutstandingTransactionIdentifier")
    public LiqAPIOutstandingTransactionIdentifier outstandingTransactionIdentifier;

    public LiqAPIOutstandingTransactionIdentifier getOutstandingTransactionIdentifier() {
        return outstandingTransactionIdentifier;
    }

    public void setOutstandingTransactionIdentifier(
            LiqAPIOutstandingTransactionIdentifier outstandingTransactionIdentifier) {
        this.outstandingTransactionIdentifier = outstandingTransactionIdentifier;
    }

    @Override
    public LiqAPIExecutableData validateLicense() {
        return this;
    }

    public Object basicExecute() {
        return LiqAPILoanDrawdownIntegrationAsReturnValue.clazz.forQuery(getTransaction());
    }

    private List<LoanInitialDrawdown> getTransaction() {
        List<LoanInitialDrawdown> transactions = null;
        try {
            transactions = List.of((LoanInitialDrawdown) LoanInitialDrawdown.clazz.getForId(
                outstandingTransactionIdentifier.getLoanTransactionId()));
        } catch (NullPointerException e) {
            outstandingTransactionIdentifier.throwInvalidTxnException();
        } catch (Exception ex) {
            ExceptionUtility.throwException(new LiqError(
                Messages.liqNlsExternalizedMessage("Unable to fetch Outstanding Transaction. ")
                    .concat(ex.getMessage()), ex));
        }

        if (CollectionUtils.isNotEmpty(transactions) && Objects.nonNull(transactions.get(0))) {
            setIds(transactions.stream().map(tran -> tran.getId()).collect(Collectors.toList()));
            return loadObjects(transactions);
        }
        return transactions;
    }

    public void basicValidate() {
        if (getOutstandingTransactionIdentifier() != null) {
            outstandingTransactionIdentifier.basicValidate();
        } else {
            ExceptionUtility.throwException(new LiqError(
                Messages.liqNlsExternalizedMessage("Outstanding Transaction Identifier is required."), this));
        }
    }

    public String securityAccessSymbol() {
        return "QueryLoanDrawdownIntegration";
    }

    public static class Class extends LiqAPIExecutableData.Class implements StClass {
        protected Class() {}

        public StObject basicNew() {
            return new LiqAPIQueryLoanDrawdownIntegration();
        }

        public java.lang.Class getJavaClass() {
            return LiqAPIQueryLoanDrawdownIntegration.class;
        }

        public StClass getStSuperclass() {
            return LiqAPIExecutableData.clazz;
        }

        public String securityAccessSymbol() {
            return "QueryLoanDrawdownIntegration";
        }

        public List nonPrimitiveFieldMappings() {
            List mappings = super.nonPrimitiveFieldMappings();
            LiqAPINonPrimitiveFieldMapping t1 = (LiqAPINonPrimitiveFieldMapping)
                LiqAPINonPrimitiveFieldMapping.clazz.newStObject();
            t1.setFieldName("outstandingTransactionIdentifier");
            t1.setFieldApiClass(LiqAPIOutstandingTransactionIdentifier.clazz);
            mappings.add(t1);
            return mappings;
        }

        public List primitiveFieldMappings() {
            List t = super.primitiveFieldMappings();
            return t;
        }

        public String securityFunctionParent() {
            return "Loan";
        }

        public Boolean supportsAdditionalFields() {
            return false;
        }

        public List<LiqAPIReturnData> getReturnType() {
            List<LiqAPIReturnData> list = new ArrayList<LiqAPIReturnData>();
            LiqAPIReturnData liqAPIReturnData = LiqAPIReturnData.getInstance(
                    LiqAPILoanDrawdownIntegrationAsReturnValue.clazz, false);
            list.add(liqAPIReturnData);
            return list;
        }

        public List documentedReturnValues() {
            return CollectionUtility.listWith(LiqAPILoanDrawdownIntegrationAsReturnValue.clazz);
        }

        public boolean isRest() {
            return true;
        }
    }
}
```

---

## Example 4: Lazy Loading Pattern (Facility)

**Source:** `LoanIQ/srcgen/com/misys/liq/api/rest/executable/facility/LiqAPIQueryFacilityIntegration.java`

```java
package com.misys.liq.api.rest.executable.facility;

import com.finastra.liq.api.annotation.LiqAPIFieldMapper;
import com.misys.liq.Messages;
import com.misys.liq.api.data.LiqAPIData;
import com.misys.liq.api.data.LiqAPINonPrimitiveFieldMapping;
import com.misys.liq.api.data.facility.LiqAPIFacilityIdentifier;
import com.misys.liq.api.executable.LiqAPIExecutableData;
import com.misys.liq.api.rest.data.facility.LiqAPIFacilityIntegrationAsReturnValue;
import com.misys.liq.bm.desktopcore.main.cdt.facility.Facility;
import com.misys.liq.infrastructure.exceptions.LiqError;
import com.sxsy.smtj.StClass;
import com.sxsy.smtj.StClassRegistry;
import com.sxsy.smtj.StObject;
import com.sxsy.smtj.exceptions.ExceptionUtility;
import com.sxsy.smtj.utilities.StringUtility;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class LiqAPIQueryFacilityIntegration extends LiqAPIExecutableData implements StObject {

    private static final Logger LOG = org.apache.logging.log4j.LogManager.getLogger(LiqAPIQueryFacilityIntegration.class);
    public static final Class clazz = new Class();

    @LiqAPIFieldMapper(name = "FacilityIdentifier", className = "com.misys.liq.api.data.facility.LiqAPIFacilityIdentifier")
    public LiqAPIFacilityIdentifier facilityIdentifier;

    public Facility facility;

    static {
        StClassRegistry.register(clazz);
    }

    @Override
    public void basicValidate() {
        if (Objects.nonNull(getFacilityIdentifier())) {
            facilityIdentifier.basicValidate();
        } else {
            ExceptionUtility.throwException(new LiqError(Messages.liqNlsExternalizedMessage("Facility Identifier is required."), this));
        }
    }

    public StClass getStClass() {
        return clazz;
    }

    public LiqAPIFacilityIdentifier getFacilityIdentifier() {
        return facilityIdentifier;
    }

    public void setFacilityIdentifier(LiqAPIFacilityIdentifier facilityIdentifier) {
        this.facilityIdentifier = facilityIdentifier;
    }

    public Facility getFacility() {
        if (facility == null && facilityIdentifier != null) {
            try {
                facility = facilityIdentifier.getFacility();
            } catch (Exception exception) {
                ExceptionUtility.throwException(new LiqError(StringUtility.bindWith(
                        Messages.liqNlsExternalizedMessage(
                                "Facility %1 '%2' does not refer to a valid Facility."),
                        facilityIdentifier.getIdentifierType(), facilityIdentifier.getIdentifierValue()), this));
            }
        }
        return facility;
    }

    @Override
    public LiqAPIData basicExecute() {
        return (LiqAPIData) LiqAPIFacilityIntegrationAsReturnValue.clazz.forQuery(facilityIdentifier.getFacility());
    }

    public String securityAccessSymbol() {
        return "QueryFacilityIntegration";
    }

    @Override
    public LiqAPIExecutableData validateLicense() {
        return this;
    }

    public static class Class extends LiqAPIExecutableData.Class implements StClass {
        protected Class() {}

        public StObject basicNew() {
            return new LiqAPIQueryFacilityIntegration();
        }

        public java.lang.Class getJavaClass() {
            return LiqAPIQueryFacilityIntegration.class;
        }

        public StClass getStSuperclass() {
            return LiqAPIExecutableData.clazz;
        }

        public List nonPrimitiveFieldMappings() {
            List mappings = super.nonPrimitiveFieldMappings();
            LiqAPINonPrimitiveFieldMapping t1 = (LiqAPINonPrimitiveFieldMapping) LiqAPINonPrimitiveFieldMapping.clazz.newStObject();
            t1.setFieldName("facilityIdentifier");
            t1.setFieldApiClass(LiqAPIFacilityIdentifier.clazz);
            mappings.add(t1);
            return mappings;
        }

        @Override
        public List primitiveFieldMappings() {
            List t = super.primitiveFieldMappings();
            return t;
        }

        @Override
        public boolean isRest() {
            return true;
        }
    }
}
```

---

## Example 5: Cashflow Message Query (OutgoingDDAMessage)

**Source:** `LoanIQ/srcgen/com/misys/liq/api/rest/executable/cashflow/LiqAPIQueryOutgoingDDAMessageIntegration.java`

```java
package com.misys.liq.api.rest.executable.cashflow;

import com.misys.liq.Messages;
import com.misys.liq.api.data.LiqAPIReturnData;
import com.misys.liq.api.data.LiqAPIViewPrimitiveFieldMapping;
import com.misys.liq.api.executable.LiqAPIExecutableData;
import com.misys.liq.api.rest.constants.ErrorMessageConstants;
import com.misys.liq.bm.accttran.Cashflow;
import com.misys.liq.api.rest.data.cashflow.LiqAPIOutgoingDDAMessageIntegrationAsReturnValue;
import com.misys.liq.bm.desktopcore.main.cdt.remittnc.OutgoingDemandDeposit;
import com.misys.liq.infrastructure.exceptions.LiqError;
import com.sxsy.smtj.StClass;
import com.sxsy.smtj.StClassRegistry;
import com.sxsy.smtj.StObject;
import com.sxsy.smtj.exceptions.ExceptionUtility;
import com.sxsy.smtj.utilities.CollectionUtility;
import com.sxsy.smtj.utilities.StringUtility;

import java.util.List;

import static com.misys.liq.api.constants.APICommonConstants.SECURITY_ACCESS_SYMBOL_CASHFLOW;
import static com.misys.liq.api.constants.APICommonConstants.SECURITY_ACCESS_SYMBOL_QUERY_OUTGOING_DDA_MESSAGE;

public class LiqAPIQueryOutgoingDDAMessageIntegration extends LiqAPIExecutableData implements StObject {
    public static final Class clazz = new Class();

    static {
        StClassRegistry.register(clazz);
    }

    public StClass getStClass() {
        return clazz;
    }

    @Override
    public LiqAPIExecutableData validateLicense() {
        return this;
    }

    public String ddaOutId;
    public OutgoingDemandDeposit outgoingDDAMessage;

    public String getDdaOutId() {
        return ddaOutId;
    }

    public void setDdaOutId(String ddaOutId) {
        this.ddaOutId = ddaOutId;
    }

    public OutgoingDemandDeposit getOutgoingDDAMessage() {
        return outgoingDDAMessage;
    }

    public void setOutgoingDDAMessage(OutgoingDemandDeposit outgoingDDAMessage) {
        this.outgoingDDAMessage = outgoingDDAMessage;
    }

    public Object basicExecute() {
        return LiqAPIOutgoingDDAMessageIntegrationAsReturnValue.clazz.forQuery(loadObject(this.getOutgoingDDAMessage()));
    }

    public void basicValidate() {
        validateOutgoingDDAMessage();
    }

    private void validateOutgoingDDAMessage() {
        if (!StringUtility.isNilOrBlank(ddaOutId)) {
            this.setOutgoingDDAMessage((OutgoingDemandDeposit) OutgoingDemandDeposit.clazz.getForId(ddaOutId));
            if (null == this.getOutgoingDDAMessage()) {
                ExceptionUtility.throwException(new LiqError(Messages.liqNlsExternalizedMessage(
                        String.format(ErrorMessageConstants.INVALID_OUTGOING_MESSAGE_DDA, ddaOutId)), this));
            }
            if (null == Cashflow.clazz.getForId(this.getOutgoingDDAMessage().getCashflowId())) {
                ExceptionUtility.throwException(new LiqError(Messages.liqNlsExternalizedMessage(
                        String.format(ErrorMessageConstants.INVALID_CASHFLOW_FOR_OUTGOING_MESSAGE_DDA, ddaOutId)), this));
            }
        }
    }

    public static class Class extends LiqAPIExecutableData.Class implements StClass {
        protected Class() {}

        public StObject basicNew() {
            return new LiqAPIQueryOutgoingDDAMessageIntegration();
        }

        public java.lang.Class getJavaClass() {
            return LiqAPIQueryOutgoingDDAMessageIntegration.class;
        }

        public boolean getResultMixedType() {
            return true;
        }

        public StClass getStSuperclass() {
            return LiqAPIExecutableData.clazz;
        }

        public List primitiveFieldMappings() {
            List<LiqAPIViewPrimitiveFieldMapping> primitiveFieldMappings = super.primitiveFieldMappings();
            primitiveFieldMappings.add(createViewMapping("ddaOutId", "id", true));
            return primitiveFieldMappings;
        }

        public List<LiqAPIReturnData> getReturnType() {
            return List.of(LiqAPIReturnData.getInstance(LiqAPIOutgoingDDAMessageIntegrationAsReturnValue.clazz, false));
        }

        public List documentedReturnValues() {
            return CollectionUtility.listWith(LiqAPIOutgoingDDAMessageIntegrationAsReturnValue.clazz);
        }

        public String securityFunctionParent() {
            return SECURITY_ACCESS_SYMBOL_CASHFLOW;
        }

        public String securityAccessSymbol() {
            return SECURITY_ACCESS_SYMBOL_QUERY_OUTGOING_DDA_MESSAGE;
        }

        public boolean isRest() {
            return true;
        }
    }
}
```

---

## Example 6: Circle Query (Custom Identifier)

**Source:** `LoanIQ/srcgen/com/misys/liq/api/rest/executable/circle/LiqAPIQueryCircleIntegration.java`

```java
package com.misys.liq.api.rest.executable.circle;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.finastra.liq.api.annotation.LiqAPIFieldMapper;
import com.misys.liq.Messages;
import com.misys.liq.api.constants.APICommonConstants;
import com.misys.liq.api.data.LiqAPINonPrimitiveFieldMapping;
import com.misys.liq.api.executable.LiqAPIExecutableData;
import com.misys.liq.api.rest.constants.ErrorMessageConstants;
import com.misys.liq.bm.desktopcore.main.cdt.invest.OriginationDealPrimary;
import com.misys.liq.infrastructure.exceptions.LiqError;
import com.sxsy.smtj.StClass;
import com.sxsy.smtj.StClassRegistry;
import com.sxsy.smtj.StObject;
import com.sxsy.smtj.exceptions.ExceptionUtility;

public class LiqAPIQueryCircleIntegration extends LiqAPIExecutableData implements StObject {

    public static final Class clazz = new Class();

    static {
        StClassRegistry.register(clazz);
    }

    public StClass getStClass() {
        return clazz;
    }

    @LiqAPIFieldMapper(name = "CircleIdentifier", className = "com.misys.liq.api.rest.executable.circle.LiqAPICircleIdentifier")
    public LiqAPICircleIdentifier circleIdentifier;

    public LiqAPICircleIdentifier getCircleIdentifier() {
        return circleIdentifier;
    }

    public void setCircleIdentifier(LiqAPICircleIdentifier circleIdentifier) {
        this.circleIdentifier = circleIdentifier;
    }

    @Override
    public LiqAPIExecutableData validateLicense() {
        return this;
    }

    @Override
    public boolean isIntegrationAPI() {
        return true;
    }

    @Override
    public void basicValidate() {
        if (Objects.nonNull(getCircleIdentifier())) {
            getCircleIdentifier().basicValidate();
            if (Objects.isNull(getCircleIdentifier().getIdentifierValue()) || StringUtils.isEmpty(getCircleIdentifier().getIdentifierValue())) {
                getCircleIdentifier().throwInvalidIdentifierValueException();
            }
        } else {
            ExceptionUtility.throwException(new LiqError(Messages.liqNlsExternalizedMessage(ErrorMessageConstants.CIRCLE_IDENTIFIER_REQUIRED), this));
        }
    }

    @Override
    public Object basicExecute() {
        return LiqAPICircleIntegrationAsReturnValue.clazz.forQuery(getCircles());
    }

    private List<OriginationDealPrimary> getCircles() {
        List<OriginationDealPrimary> circles = null;
        try {
            circles = List.of((OriginationDealPrimary) OriginationDealPrimary.clazz.getForId(getCircleIdentifier().getCircleId()));
        } catch (NullPointerException e) {
            getCircleIdentifier().throwInvalidIdentifierValueException();
        } catch (Exception ex) {
            ExceptionUtility.throwException(new LiqError(Messages.liqNlsExternalizedMessage("Failure while fetching Circle data: ").concat(ex.getMessage()), ex));
        }
        if (Objects.nonNull(circles) && CollectionUtils.isNotEmpty(circles) && Objects.nonNull(circles.get(0))) {
            return circles.stream().map(this::loadObject).collect(Collectors.toList());
        }
        return circles;
    }

    public String securityAccessSymbol() {
        return APICommonConstants.SECURITY_ACCESS_SYMBOL_QUERY_CIRCLE;
    }

    public static class Class extends LiqAPIExecutableData.Class implements StClass {
        protected Class() {}

        public StObject basicNew() {
            return new LiqAPIQueryCircleIntegration();
        }

        public java.lang.Class getJavaClass() {
            return LiqAPIQueryCircleIntegration.class;
        }

        public StClass getStSuperclass() {
            return LiqAPIExecutableData.clazz;
        }

        public List nonPrimitiveFieldMappings() {
            List mappings = super.nonPrimitiveFieldMappings();
            LiqAPINonPrimitiveFieldMapping t1 = (LiqAPINonPrimitiveFieldMapping) LiqAPINonPrimitiveFieldMapping.clazz.newStObject();
            t1.setFieldName("circleIdentifier");
            t1.setFieldApiClass(LiqAPICircleIdentifier.clazz);
            mappings.add(t1);
            return mappings;
        }

        public boolean isRest() {
            return true;
        }
    }
}
```

---

## Example 7: AdditionalFields Query (Polymorphic with Complex Logic)

**Source:** `LoanIQ/srcgen/com/misys/liq/api/rest/executable/additionalfields/LiqAPIQueryAdditionalFieldsIntegration.java`

```java
package com.misys.liq.api.rest.executable.additionalfields;

import com.finastra.liq.api.annotation.LiqAPIFieldMapper;
import com.misys.liq.Messages;
import com.misys.liq.api.data.LiqAPINonPrimitiveFieldMapping;
import com.misys.liq.api.data.additionalfields.LiqAPIAdditionalField;
import com.misys.liq.api.executable.LiqAPIExecutableData;
import com.misys.liq.api.rest.data.additionalfields.LiqAPIAdditionalFieldsIntegrationAsReturnValue;
import com.misys.liq.api.rest.data.identifier.LiqAPIOwnerIdentifier;
import com.misys.liq.bm.desktopcore.extender.domain.LiqObjectExtension;
import com.misys.liq.bm.desktopcore.extender.domain.LiqObjectExtensionAttribute;
import com.misys.liq.bm.desktopcore.extender.domain.LiqObjectExtensionComposite;
import com.misys.liq.bm.desktopcore.main.cdt.deal.Deal;
import com.misys.liq.bm.desktopcore.main.cdt.facility.Facility;
import com.misys.liq.infrastructure.bm.labstrct.KeyedDataObject;
import com.misys.liq.infrastructure.exceptions.LiqError;
import com.sxsy.smtj.StClass;
import com.sxsy.smtj.StClassRegistry;
import com.sxsy.smtj.StObject;
import com.sxsy.smtj.exceptions.ExceptionUtility;
import org.apache.commons.collections4.CollectionUtils;
import com.misys.liq.infrastructure.LiqDate;
import com.misys.liq.infrastructure.LiqTime;
import org.apache.logging.log4j.util.Strings;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class LiqAPIQueryAdditionalFieldsIntegration extends LiqAPIExecutableData implements StObject {

    public static final LiqAPIQueryAdditionalFieldsIntegration.Class clazz = new LiqAPIQueryAdditionalFieldsIntegration.Class();

    static {
        StClassRegistry.register(clazz);
    }

    @Override
    public StClass getStClass() {
        return clazz;
    }

    @LiqAPIFieldMapper(name = "OwnerIdentifier", className = "com.misys.liq.api.rest.data.identifier.LiqAPIOwnerIdentifier")
    public LiqAPIOwnerIdentifier ownerIdentifier;

    KeyedDataObject bo;

    public LiqAPIOwnerIdentifier getOwnerIdentifier() {
        return ownerIdentifier;
    }

    public void setOwnerIdentifier(LiqAPIOwnerIdentifier ownerIdentifier) {
        this.ownerIdentifier = ownerIdentifier;
    }

    @Override
    public void basicValidate() {
        if (ownerIdentifier != null) {
            ownerIdentifier.basicValidate();
        } else {
            ExceptionUtility.throwException(new LiqError(Messages.liqNlsExternalizedMessage("Additional Field Identifier is required."), this));
        }
    }

    public Object basicExecute() {
        return LiqAPIAdditionalFieldsIntegrationAsReturnValue.clazz.forQuery(getTransaction(), ownerIdentifier);
    }

    private List<LiqAPIAdditionalField> getTransaction() {
        List<LiqAPIAdditionalField> additionalFieldList = null;
        bo = getBusinessObject();
        try {
            if (ownerIdentifier != null) {
                switch (ownerIdentifier.getOwnerType().toUpperCase()) {
                    case "DEA":
                        additionalFieldList = additionalFields(bo);
                        break;
                    case "FAC":
                        additionalFieldList = additionalFields(bo);
                        break;
                }
            }
        } catch (Exception ex) {
            ExceptionUtility.throwException(new LiqError(Messages.liqNlsExternalizedMessage("Additional Fields not found")));
        }
        return additionalFieldList;
    }

    public KeyedDataObject getBusinessObject() {
        if (ownerIdentifier != null) {
            switch (ownerIdentifier.getOwnerType().toUpperCase()) {
                case "DEA":
                    bo = ownerIdentifier.getDeal();
                    setIds(List.of((Deal) bo).stream().map(Deal::getId).collect(Collectors.toList()));
                    break;
                case "FAC":
                    bo = ownerIdentifier.getFacility();
                    setIds(List.of((Facility) bo).stream().map(Facility::getId).collect(Collectors.toList()));
                    break;
            }
            if (Objects.nonNull(bo)) {
                return loadObject(bo);
            }
        }
        return bo;
    }

    private List<LiqAPIAdditionalField> additionalFields(KeyedDataObject bo) {
        LiqObjectExtension objectExtension = null;
        if (ownerIdentifier.getOwnerType().equals("DEA")) {
            if (bo == null || ((Deal) bo).getDeal() == null || ((Deal) bo).getDeal().objectExtensionBehavior() == null) {
                return new ArrayList<>();
            } else {
                objectExtension = ((Deal) bo).getDeal().objectExtensionBehavior().loadDomainObject();
            }
        } else if (ownerIdentifier.getOwnerType().equals("FAC")) {
            if (bo == null || ((Facility) bo).getFacility() == null || ((Facility) bo).getFacility().objectExtensionBehavior() == null) {
                return new ArrayList<>();
            } else {
                objectExtension = ((Facility) bo).getFacility().objectExtensionBehavior().loadDomainObject();
            }
        }

        if (objectExtension == null || CollectionUtils.isEmpty(objectExtension.getAttributes())) {
            return new ArrayList<>();
        }

        List<LiqAPIAdditionalField> result = new ArrayList<>();
        for (Object attrObj : objectExtension.getAttributes()) {
            if (!(attrObj instanceof LiqObjectExtensionAttribute)) {
                continue;
            }
            LiqObjectExtensionAttribute attribute = (LiqObjectExtensionAttribute) attrObj;
            createAdditionalField(attribute, result);
        }
        return result;
    }

    private void createAdditionalField(LiqObjectExtensionAttribute attribute, List<LiqAPIAdditionalField> result) {
        if (Objects.nonNull(attribute.getTypedValue()) && Objects.nonNull(attribute.getTypedValue().getValue())) {
            Object value = (attribute.typedValue != null) ? attribute.typedValue.getValue() : null;
            String fieldValue = "";
            switch (value.getClass().getSimpleName()) {
                case "LiqDate":
                    fieldValue = ((LiqDate) value).asString();
                    break;
                case "LiqTime":
                    fieldValue = ((LiqTime) value).asISOString();
                    break;
                case "ArrayList":
                    fieldValue = Strings.join((ArrayList) value, ' ');
                    break;
                case "BigDecimal":
                case "Integer":
                case "Long":
                case "String":
                    fieldValue = String.valueOf(value);
                    break;
                case "Boolean":
                    fieldValue = Boolean.TRUE.equals(((Boolean) value)) ? "Y" : "N";
                    break;
                case "LiqObjectExtensionComposite":
                    if (CollectionUtils.isNotEmpty(((LiqObjectExtensionComposite) value).getAttributes())) {
                        for (LiqObjectExtensionAttribute objExtension : ((List<LiqObjectExtensionAttribute>) ((LiqObjectExtensionComposite) value).getAttributes())) {
                            createAdditionalField(objExtension, result);
                        }
                    }
                    return;
                default:
                    fieldValue = value.toString();
                    break;
            }
            LiqAPIAdditionalField apiField = (LiqAPIAdditionalField) LiqAPIAdditionalField.clazz.basicNew();
            apiField.setFieldType(attribute.getName());
            apiField.setFieldValue(fieldValue);
            result.add(apiField);
        }
    }

    public static class Class extends LiqAPIExecutableData.Class implements StClass {
        protected Class() {}

        @Override
        public StObject basicNew() {
            return new LiqAPIQueryAdditionalFieldsIntegration();
        }

        @Override
        public java.lang.Class getJavaClass() {
            return LiqAPIQueryAdditionalFieldsIntegration.class;
        }

        @Override
        public StClass getStSuperclass() {
            return LiqAPIExecutableData.clazz;
        }

        @Override
        public List primitiveFieldMappings() {
            List mappings = super.primitiveFieldMappings();
            return mappings;
        }

        @Override
        public List nonPrimitiveFieldMappings() {
            List<LiqAPINonPrimitiveFieldMapping> mappings = super.nonPrimitiveFieldMappings();
            LiqAPINonPrimitiveFieldMapping t1 = (LiqAPINonPrimitiveFieldMapping) LiqAPINonPrimitiveFieldMapping.clazz.newStObject();
            t1.setFieldName("ownerIdentifier");
            t1.setFieldApiClass(LiqAPIOwnerIdentifier.clazz);
            mappings.add(t1);
            return mappings;
        }

        public boolean isRest() {
            return true;
        }
    }
}
```

---

## Key Patterns Summary

| Pattern | Identifier Type | basicExecute() Return | Entity Fetch Method |
|---------|----------------|----------------------|---------------------|
| Standard Entity | `@LiqAPIFieldMapper` identifier class | `ReturnValue.clazz.forQuery(getTransaction())` | `{Entity}.clazz.getForId(id)` |
| Polymorphic Owner | `LiqAPIOwnerIdentifier` | `ReturnValue.clazz.forQuery(getTransaction(), ownerIdentifier)` | `getBusinessObject().getList()` |
| Transaction | `LiqAPIOutstandingTransactionIdentifier` | `ReturnValue.clazz.forQuery(getTransaction())` | `{Type}.clazz.getForId(txnId)` |
| Cashflow Message | Plain `String` ID field | `ReturnValue.clazz.forQuery(loadObject(entity))` | `{Entity}.clazz.getForId(id)` |
| Lazy Loading | `@LiqAPIFieldMapper` identifier class | `ReturnValue.clazz.forQuery(identifier.get{Entity}())` | `identifier.get{Entity}()` |
| Fee/Payment | Custom identifier class | `ReturnValue.clazz.forQuery(getTransaction())` | `identifier.get{Entity}()` |

---

## Common Inner Class Methods

All Query API inner classes MUST implement:

1. `basicNew()` — Returns new instance of outer class
2. `getJavaClass()` — Returns `.class` of outer class
3. `getStSuperclass()` — Returns `LiqAPIExecutableData.clazz`
4. `nonPrimitiveFieldMappings()` — Maps identifier field
5. `primitiveFieldMappings()` — Maps primitive fields (often delegates to super)
6. `isRest()` — Returns `true`
7. `securityAccessSymbol()` — Returns security constant

Optional (for transaction/fee entities):
- `securityFunctionParent()` — Hierarchical security parent
- `supportsAdditionalFields()` — Usually `false`
- `getReturnType()` — Return type configuration
- `documentedReturnValues()` — Documentation metadata
- `getResultMixedType()` — For cashflow messages
