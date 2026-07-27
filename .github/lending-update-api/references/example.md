# LoanIQ Update API — Unified Reference Examples

This document contains representative source code examples from multiple entity categories,
demonstrating the full range of Update API patterns in LoanIQ.

---

## 1. Pattern B — Abstract Base Extension (UpfrontFee)

**Source:** `LoanIQ/srcgen/com/misys/liq/api/rest/executable/upfrontfee/LiqAPIUpdateUpfrontFeeIntegration.java`

```java
package com.misys.liq.api.rest.executable.upfrontfee;

import com.finastra.liq.api.annotation.LiqAPIFieldMapper;
import com.misys.liq.Messages;
import com.misys.liq.api.data.*;
import com.misys.liq.api.data.customer.LiqAPIBorrowerIdentifier;
import com.misys.liq.api.executable.IAPIRestIntegration;
import com.misys.liq.api.rest.data.deal.LiqAPIServicingGroupIntegration;
import com.misys.liq.api.rest.data.identifier.LiqAPIOwnerIdentifier;
import com.misys.liq.api.rest.data.upfrontfee.LiqAPIFeeDetails;
import com.misys.liq.api.rest.data.upfrontfee.LiqAPIUpfrontFeeIdentifier;
import com.misys.liq.api.rest.data.upfrontfee.LiqAPIUpfrontFeeIntegrationAsReturnValue;
import com.misys.liq.api.rest.executable.update.helper.Node;
import com.misys.liq.bm.desktopcore.main.cdt.codetabl.SystemParameter;
import com.misys.liq.bm.desktopcore.main.cdt.customer.Customer;
import com.misys.liq.bm.desktopcore.main.cdt.deal.UpfrontFeeFromCustomer;
import com.misys.liq.bm.desktopcore.main.cdt.faxgroup.ServicingGroup;
import com.misys.liq.bm.desktopcore.main.cdt.funding.Money;
import com.misys.liq.bm.desktopcore.main.upfrontfee.LiqMNUpfrontFeeDetailCollection;
import com.misys.liq.infrastructure.ChangeEventListeners;
import com.misys.liq.infrastructure.bm.labstrct.LS2UpdateableData;
import com.misys.liq.infrastructure.enfinbasesupport.LiqBusinessObject;
import com.misys.liq.infrastructure.exceptions.LiqError;
import com.misys.liq.infrastructure.exceptions.LiqMessageException;
import com.sxsy.smtj.StClass;
import com.sxsy.smtj.StClassRegistry;
import com.sxsy.smtj.StObject;
import com.sxsy.smtj.exceptions.ExceptionUtility;
import com.sxsy.smtj.utilities.StringUtility;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

/**
 * API class for updating upfront fee transactions.
 * <p>
 * This class provides functionality to update upfront fee transactions with various
 * attributes including fee details, borrower information, and transaction amounts.
 * It validates input parameters and handles the update process for upfront fees.
 */
public class LiqAPIUpdateUpfrontFeeIntegration extends LiqAPIAbstractUpfrontFeeIntegration implements IAPIRestIntegration {

	public static final Class clazz = new Class();

	// Non-primitive field collection mappings

	@LiqAPIFieldMapper(name = "OwnerIdentifier", className = "com.misys.liq.api.rest.data.identifier.LiqAPIOwnerIdentifier")
	public List<LiqAPIOwnerIdentifier> ownerIdentifiers;

	@LiqAPIFieldMapper(name = "UpfrontFeeIdentifier", className = "com.misys.liq.api.rest.data.upfrontfee.LiqAPIUpfrontFeeIdentifier")
	public List<LiqAPIUpfrontFeeIdentifier> upfrontFeeIdentifiers;

    @LiqAPIFieldMapper(name = "UpforntFeeIdentifier", className = "com.misys.liq.api.rest.data.upfrontfee.LiqAPIUpfrontFeeIdentifier")
    public LiqAPIUpfrontFeeIdentifier upforntFeeIdentifier;

	public LiqAPIUpfrontFeeIdentifier upfrontFeeIdebtifierById;

	private UpfrontFeeFromCustomer upfrontFeeTransaction;

	static {
		StClassRegistry.register(clazz);
	}

	public StClass getStClass() {
		return clazz;
	}

	// Getters and Setters for non-primitive fields

	@Override
	public List<LiqAPIFeeDetails> getFeeDetails() {
		if (Objects.isNull(this.feeDetails)) {
			this.feeDetails = new ArrayList<LiqAPIFeeDetails>();
		}
		return feeDetails;
	}

	public List<LiqAPIOwnerIdentifier> getOwnerIdentifiers() {
		return ownerIdentifiers;
	}

	public void setOwnerIdentifiers(List<LiqAPIOwnerIdentifier> ownerIdentifiers) {
		this.ownerIdentifiers = ownerIdentifiers;
	}

	public List<LiqAPIUpfrontFeeIdentifier> getUpfrontFeeIdentifiers() {
		return upfrontFeeIdentifiers;
	}

	public void setUpfrontFeeIdentifiers(List<LiqAPIUpfrontFeeIdentifier> upfrontFeeIdentifiers) {
        if(CollectionUtils.isNotEmpty(upfrontFeeIdentifiers)) {
            upforntFeeIdentifier = upfrontFeeIdentifiers.stream() .filter(identifier -> identifier.identifierType.equalsIgnoreCase(LiqAPIUpfrontFeeIdentifier.UpfrontFeeIdentifierType.id.toString())) .findFirst() .orElse(null); //Or you can use streams and get the "id" one
        }
        this.upfrontFeeIdentifiers = upfrontFeeIdentifiers;
	}

    public LiqAPIUpfrontFeeIdentifier getUpforntFeeIdentifier() {
        return upforntFeeIdentifier;
    }

    public void setUpforntFeeIdentifier(LiqAPIUpfrontFeeIdentifier upforntFeeIdentifier) {
        this.upforntFeeIdentifier = upforntFeeIdentifier;
    }

	public void setUpfrontFeeTransaction(UpfrontFeeFromCustomer upfrontFeeTransaction) {
		this.upfrontFeeTransaction = upfrontFeeTransaction;
	}

	/**
	 * Gets the upfront fee transaction.
	 * @return the upfront fee transaction
	 */
	public UpfrontFeeFromCustomer getUpfrontFeeTransaction() {
		upfrontFeeIdebtifierById = LiqAPIUpfrontFeeIdentifier.clazz.getUpfrontFeeIdIdentifier(getUpfrontFeeIdentifiers());
		upfrontFeeTransaction = upfrontFeeIdebtifierById.getUpfrontFee();
		if(Objects.nonNull(upfrontFeeTransaction)) {
			loadObjects(List.of(upfrontFeeTransaction));
		}
		return upfrontFeeTransaction;
	}

	/**
	 * Validates all input parameters.
	 */
	public void basicValidate() {
		LiqAPIUpfrontFeeIdentifier.clazz.validateIdIdentifier(getUpfrontFeeIdentifiers());
		this.validateTimeStamp(getUpfrontFeeTransaction(), getMatchUpdatedTimestamp());
	}

	/**
	 * Executes the basic update operation.
	 * @return the response object
	 */
	public Object basicExecute() {
		try {
			checkDealSecurity();
			checkCustomerSecurity();
			this.lockAPIData();
			this.performUpdate();
			this.singleCommit();
		} finally {
			this.unLockAPIData();
		}
		return response();
	}

	/**
	 * Performs the actual update operation.
	 */
	protected void performUpdate() {
		UpfrontFeeFromCustomer transaction = getUpfrontFeeTransaction();
		if (transaction != null) {
			updateFields(transaction);
			transaction.save();
		}
	}

	/**
	 * Updates the transaction fields with the provided values.
	 * @param upfrontFeeFromCustomer the transaction to update
	 */
	protected void updateFields(UpfrontFeeFromCustomer upfrontFeeFromCustomer) {
        /*moving this currencyCode set before amount comparison to avoid unnecessary actualAmount set.
        As mount decimal precision depends on currencyCode*/
        if (currencyCode != null) {
            validateCurrencyCode(this.getCurrencyCode(),upfrontFeeFromCustomer.getDeal());
            upfrontFeeFromCustomer.zz_currencyCode(this.getCurrencyCode());
        }

		if (amount != null && ((upfrontFeeFromCustomer.getAmount().getAmount()).compareTo(amount) != 0)) {
			Money actualAmount = Money.clazz.fromAmount(this.getAmount(),this.getCurrencyCode());
			upfrontFeeFromCustomer.zz_actualAmount(actualAmount);
		}
		if (effectiveDate != null) {
			upfrontFeeFromCustomer.zz_effectiveDate(this.getEffectiveDate());
		}

		if (commentText != null) {
            if(this.getCommentText() != null && this.getCommentText().toUpperCase().equals("NULL")){
                ExceptionUtility.throwException(new LiqError(
                        Messages.liqNlsExternalizedMessage("Comment text cannot be set to NULL."),
                        this));
            }
			upfrontFeeFromCustomer.commentText(this.getCommentText());
		}

		if (branchCode != null) {
			upfrontFeeFromCustomer.zz_branch(this.getBranchCode());
		}

        if (fxRate != null) {
            upfrontFeeFromCustomer.setFXRate(this.getFxRate());
            ChangeEventListeners.objChangedField(upfrontFeeFromCustomer, "fXRate");
            //upfrontFeeFromCustomer.setAttributeValue("fXRate",this.getFxRate());
        }

		Customer borrower = null;
		if (borrowerIdentifier != null) {
			borrower = borrowerIdentifier.getCustomer();
			upfrontFeeFromCustomer.zz_paidByCustomerId(borrower.getId());
		}

		if (servicingGroup != null) {
            //LIQ-165314
            if(!upfrontFeeFromCustomer.getDeal().isHostBankAgent()){
                ExceptionUtility.throwException(new LiqError(
                        Messages.liqNlsExternalizedMessage("Servicing group cannot be updated for a non-host bank agent deal."),
                        this));
            }
			if(borrower == null){
				borrower = (Customer) upfrontFeeFromCustomer.getFeePayer();
			}
			upfrontFeeFromCustomer.zz_paidByServicingGroupId(getServicingGroupForBorrower(borrower).getId());
		}else{

			ServicingGroup sg = upfrontFeeFromCustomer.getPaidByServicingGroup();
			if(borrower != null){
				List<ServicingGroup> sgList = borrower.getServicingGroups();

				// Check if the servicing group ID is available in the borrower's servicing groups
				boolean isServicingGroupValid = sg != null && sgList != null && !sgList.isEmpty() &&
						sgList.stream().anyMatch(servicingGroupInList ->
								servicingGroupInList != null && servicingGroupInList.getId().equals(sg.getId()));

				// If servicing group is not valid for this borrower, clear it
				if(!isServicingGroupValid && sg != null){
					ExceptionUtility.throwException(new LiqError(
							Messages.liqNlsExternalizedMessage(StringUtility.bindWith("The servicing group (%1) does not belong to the fee payer (%2)", new Object[]{sg.getAlias(), borrower.getFullname()})),
							this));
				}
			}
		}

		if (feeDetails != null) {
			setUpfrontFeeDetails(upfrontFeeFromCustomer);
		}

        upfrontFeeFromCustomer.save();
	}

	/**
	 * Sets the fee details for the upfront fee business object.
	 * @param upfrontFeeFromCustomer upfront fee business object
	 */
	public void setUpfrontFeeDetails(UpfrontFeeFromCustomer upfrontFeeFromCustomer) {
		if(!SystemParameter.clazz.upfrontFeeTypeBreakdownForBranch(upfrontFeeFromCustomer.getBranch()).booleanValue()){
			return;
		}
		if(upfrontFeeFromCustomer == null){
			return;
		}
		LiqMNUpfrontFeeDetailCollection feeDetailsObject = upfrontFeeFromCustomer.getFeeDetails();
        deleteFeeDetails(feeDetailsObject,getFeeDetails());
        updateFeeDetails(feeDetailsObject,upfrontFeeFromCustomer.getCurrencyCode());
        int feeDetailCount = feeDetailsObject.feeDetails().size();
        this.validateSum(feeDetailsObject,feeDetailCount);
		feeDetailsObject.save();
		upfrontFeeFromCustomer.setFeeDetails(feeDetailsObject);
	}

    /**
     * Validates the sum of fee details.
     * @param feeDetailsObject the fee details collection to validate
     */
    public void validateSum(LiqMNUpfrontFeeDetailCollection feeDetailsObject,int feeDetailCount){
        if(feeDetailsObject != null){
            if(feeDetailCount == 0){
                return;
            }
            feeDetailsObject.validateSum();
        }
    }

	/**
	 * Checks deal security.
	 */
	@Override
	public void checkDealSecurity() {
		if (getUpfrontFeeTransaction() != null) {
			dealSecurity(getUpfrontFeeTransaction().getDealId());
		}
	}

	/**
	 * Checks customer security.
	 */
	@Override
	public void checkCustomerSecurity() {
		if (getUpfrontFeeTransaction() != null) {
			customerSecurity(getUpfrontFeeTransaction().getBorrowerId());
		}
	}

	/**
	 * Generates the response object.
	 * @return the response object
	 */
	@Override
	public Object response() {
		Object object = com.misys.liq.api.rest.data.upfrontfee.LiqAPIUpfrontFeeIntegrationAsReturnValue.clazz.forUpdate(getUpfrontFeeTransaction());
		this.addIds(List.of(this.getUpfrontFeeTransaction()));
		return this.getUpfrontFeeTransaction() == null || !this.getUpfrontFeeTransaction().isSaved() ? new String() : nonNull(object) ? object : new String();
	}

	/**
	 * Adds IDs to the response.
	 * @param objects the objects to extract IDs from
	 */
	@Override
	public void addIds(List<LS2UpdateableData> objects) {
		if (null == objects || objects.isEmpty()) {
			return;
		}
		setIds(objects.stream().map(tran -> tran.getId()).collect(Collectors.toList()));
	}

	/**
	 * Locks the API data for the update operation.
	 */
	@Override
	public void lockAPIData() {
		if (getUpfrontFeeTransaction() != null) {
			try {
				((LiqBusinessObject) getUpfrontFeeTransaction()).exclusiveUpdateLockFor(this);
			}
			catch (final LiqMessageException ex) {
				ExceptionUtility.throwException(new LiqError(ex, this));
			}
		}
	}

	/**
	 * Unlocks the API data after the update operation.
	 */
	@Override
	public void unLockAPIData() {
		if (getUpfrontFeeTransaction() != null) {
			try {
				((LiqBusinessObject) getUpfrontFeeTransaction()).exclusiveUpdateUnlockFor(this);
			} catch (final LiqMessageException ex) {
				ExceptionUtility.throwException(new LiqError(ex, this));
			}
		}
	}

	/**
	 * Returns the security access symbol for this operation.
	 * @return the security access symbol
	 */
	public String securityAccessSymbol() {
		return "UpdateUpfrontFeeIntegration";
	}

    public Boolean isEnabledForUpdateDelete() {
        return Boolean.TRUE;
    }

    public Set<String> fetchMandatoryAttributesForQuery() {
        return Set.of("upforntFeeIdentifier");
    }

    public Node updateStructure() {

        // Create ongoingFeePricings node with attributes
        Node feeDetails = Node.NodeBuilder.getInstance()
                .setAttributeName("feeDetails")
                .addPrimaryKeys("feeType")
                .setIsNonPrimitiveCollection(Boolean.TRUE)
                .build();

        // Create root node with children
        Node root = Node.NodeBuilder.getInstance()
                .setAttributeName("UpdateUpfrontFeeIntegration")
                .addChildren(feeDetails)
                .build();

        return root;
    }

    public Node queryStructure() {

        // Create ongoingFeePricings node with attributes
        Node feeDetails = Node.NodeBuilder.getInstance()
                .setAttributeName("feeDetails")
                .addPrimaryKeys("feeType")
                .setQueryMode(Boolean.TRUE)
                .setUpdatePayloadAssociatedAttribute("feeDetails")  // Attribute name present in Update Class
                .setIsNonPrimitiveCollection(Boolean.TRUE).build();

        // Create root node with children
        Node root = Node.NodeBuilder.getInstance()
                .setAttributeName("UpdateUpfrontFeeIntegration")
                .addChildren(feeDetails)
                .build();

        return root;
    }

	/**
	 * Metadata class for LiqAPIUpdateUpfrontFeeIntegration.
	 */
	public static class Class extends LiqAPIAbstractUpfrontFeeIntegration.Class implements StClass {
		protected Class() {
		}

		public StObject basicNew() {
			return new LiqAPIUpdateUpfrontFeeIntegration();
		}

		public java.lang.Class getJavaClass() {
			return LiqAPIUpdateUpfrontFeeIntegration.class;
		}

		public StClass getStSuperclass() {
			return LiqAPIAbstractUpfrontFeeIntegration.clazz;
		}

		/**
		 * Defines non-primitive field mappings.
		 * @return list of non-primitive field mappings
		 */
		public List nonPrimitiveFieldMappings() {
			List mappings = super.nonPrimitiveFieldMappings();

			LiqAPINonPrimitiveFieldMapping servicingGroupMapping = (LiqAPINonPrimitiveFieldMapping) LiqAPINonPrimitiveFieldMapping.clazz.newStObject();
			servicingGroupMapping.setFieldName("servicingGroup");
			servicingGroupMapping.setFieldApiClass(LiqAPIServicingGroupIntegration.clazz);
			mappings.add(servicingGroupMapping);

			LiqAPINonPrimitiveFieldMapping borrowerMapping = (LiqAPINonPrimitiveFieldMapping) LiqAPINonPrimitiveFieldMapping.clazz.newStObject();
			borrowerMapping.setFieldName("borrowerIdentifier");
			borrowerMapping.setFieldApiClass(LiqAPIBorrowerIdentifier.clazz);
			mappings.add(borrowerMapping);

			return mappings;
		}

		/**
		 * Defines non-primitive field collection mappings.
		 * @return list of non-primitive field collection mappings
		 */
		public List nonPrimitiveFieldCollectionMappings() {
			List mappings = super.nonPrimitiveFieldCollectionMappings();

			LiqAPINonPrimitiveFieldMapping ownerMapping = (LiqAPINonPrimitiveFieldMapping) LiqAPINonPrimitiveFieldMapping.clazz.newStObject();
			ownerMapping.setFieldName("ownerIdentifiers");
			ownerMapping.setFieldApiClass(LiqAPIOwnerIdentifier.clazz);
			mappings.add(ownerMapping);

			LiqAPINonPrimitiveFieldMapping upfrontFeeMapping = (LiqAPINonPrimitiveFieldMapping) LiqAPINonPrimitiveFieldMapping.clazz.newStObject();
			upfrontFeeMapping.setFieldName("upfrontFeeIdentifiers");
			upfrontFeeMapping.setFieldApiClass(LiqAPIUpfrontFeeIdentifier.clazz);
			mappings.add(upfrontFeeMapping);

			return mappings;
		}

		/**
		 * Defines primitive field mappings.
		 * @return list of primitive field mappings
		 */
		public List primitiveFieldMappings() {
			return super.primitiveFieldMappings();
		}

		public String securityFunctionParent() {
			return "UpfrontFee";
		}

		public Boolean supportsAdditionalFields() {
			return false;
		}

		public List<LiqAPIReturnData> getReturnType() {
			List<LiqAPIReturnData> list = new ArrayList<LiqAPIReturnData>();
			LiqAPIReturnData liqAPIReturnData = LiqAPIReturnData.getInstance(LiqAPIUpfrontFeeIntegrationAsReturnValue.clazz, false);
			list.add(liqAPIReturnData);
			return list;
		}

		public List documentedReturnValues() {
			return List.of(com.misys.liq.api.rest.data.upfrontfee.LiqAPIUpfrontFeeIntegrationAsReturnValue.clazz);
		}

		public boolean isRest() {
			return true;
		}
	}
}
```

---

## 2. Pattern A — Direct Extension with Multi-Owner (MISCode)

**Source:** `LoanIQ/srcgen/com/misys/liq/api/rest/executable/miscode/LiqAPIUpdateMISCodeIntegration.java`

```java
package com.misys.liq.api.rest.executable.miscode;

import static java.util.Objects.nonNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.finastra.liq.api.annotation.LiqAPIFieldMapper;
import com.misys.liq.LoanIQ;
import com.misys.liq.Messages;
import com.misys.liq.api.constants.APICommonConstants;
import com.misys.liq.api.data.LiqAPINonPrimitiveFieldMapping;
import com.misys.liq.api.executable.IAPIRestIntegration;
import com.misys.liq.api.executable.LiqAPIExecutableData;
import com.misys.liq.api.rest.constants.ErrorMessageConstants;
import com.misys.liq.api.rest.data.deal.LiqAPIMISCodeIntegration;
import com.misys.liq.api.rest.data.identifier.LiqAPIOwnerIdentifier;
import com.misys.liq.api.rest.data.miscode.LiqAPIMISCodeIntegrationAsReturnValue;
import com.misys.liq.bm.desktopcore.main.cdt.codetabl.CodeTable;
import com.misys.liq.bm.desktopcore.main.cdt.codetabl.OnlineCodeTable;
import com.misys.liq.bm.desktopcore.main.cdt.deal.Deal;
import com.misys.liq.bm.desktopcore.main.cdt.deal.DealBorrower;
import com.misys.liq.bm.desktopcore.main.cdt.facility.Facility;
import com.misys.liq.bm.desktopcore.main.cdt.loan.Outstanding;
import com.misys.liq.bm.desktopcore.main.cdt.loan.OutstandingTransaction;
import com.misys.liq.bm.product.FacilityBorrower;
import com.misys.liq.bm.product.ProductMISCode;
import com.misys.liq.infrastructure.LiqDate;
import com.misys.liq.infrastructure.LiqUtilities;
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
import com.sxsy.smtj.utilities.CompareUtility;
import com.sxsy.smtj.utilities.StringUtility;

/**
 * REST API executable class for updating MIS (Management Information System) code integration data.
 * This class extends {@link LiqAPIExecutableData} and implements {@link IAPIRestIntegration}
 * to provide comprehensive MIS code update functionality for various owner types.
 *
 * <p>The class supports updating MIS codes for different owner types including:</p>
 * <ul>
 *   <li>DEA (Deal) - Deal-level MIS codes</li>
 *   <li>FAC (Facility) - Facility-level MIS codes</li>
 *   <li>LNID (LoanInitialDrawdown) - Outstanding transaction loan MIS codes</li>
 * </ul>
 *
 * <p>Key features include:</p>
 * <ul>
 *   <li>Validation of owner identifiers and MIS code data</li>
 *   <li>Security checks for deal and customer access</li>
 *   <li>Exclusive locking mechanism to prevent concurrent modifications</li>
 *   <li>Duplicate MIS code validation</li>
 *   <li>Timestamp-based optimistic locking</li>
 *   <li>Transaction management with commit/rollback capabilities</li>
 * </ul>
 *
 * <p>This class implements the StObject interface for Smalltalk-Java integration
 * and provides field mappings for REST API serialization/deserialization.</p>
 *
 * @author Generated
 * @version 1.0
 * @since LoanIQ API
 */
public class LiqAPIUpdateMISCodeIntegration extends LiqAPIExecutableData implements
		IAPIRestIntegration, StObject {

	/**
	 * Owner identifier that specifies which business object (Deal, Facility, Outstanding)
	 * the MIS codes should be updated for. Maps to the "OwnerIdentifier" field in REST API.
	 */
	@LiqAPIFieldMapper(name = "OwnerIdentifier", className =  "com.misys.liq.api.rest.data.identifier.LiqAPIOwnerIdentifier" )
	public LiqAPIOwnerIdentifier ownerIdentifier;

	/**
	 * List of MIS code integration objects to be updated. Maps to the "MISCodeIntegration"
	 * field in REST API. Contains the MIS codes with their types, values, and value types.
	 */
	@LiqAPIFieldMapper(name = "MISCodeIntegration", className = "com.misys.liq.api.rest.data.deal.LiqAPIMISCodeIntegration")
	public List<LiqAPIMISCodeIntegration> misCodes;

	/**
	 * The business object (Deal, Facility, or Outstanding) that owns the MIS codes being updated.
	 * This is populated based on the owner identifier during validation.
	 */
    KeyedDataObject bo;

	/**
	 * Static class instance used for Smalltalk-Java integration.
	 * This field holds the metadata class definition for this object type.
	 */
	public static final Class clazz = new Class();

	static {
		StClassRegistry.register(clazz);
	}

	/**
	 * Returns the Smalltalk class definition for this object.
	 *
	 * @return the StClass instance representing this object's class metadata
	 */
	public StClass getStClass() {
		return clazz;
	}

	/**
	 * Gets the owner identifier that specifies which business object to update.
	 *
	 * @return the owner identifier containing business object reference information
	 */
	public LiqAPIOwnerIdentifier getOwnerIdentifier() {
		return ownerIdentifier;
	}

	/**
	 * Sets the owner identifier that specifies which business object to update.
	 *
	 * @param ownerIdentifier the owner identifier containing business object reference information
	 */
	public void setOwnerIdentifier(LiqAPIOwnerIdentifier ownerIdentifier) {
		this.ownerIdentifier = ownerIdentifier;
	}

	/**
	 * Gets the list of MIS codes to be updated.
	 *
	 * @return list of MIS code integration objects, may be null if not set
	 */
	public List<LiqAPIMISCodeIntegration> getMisCodes() {
		return misCodes;
	}

	/**
	 * Sets the list of MIS codes to be updated.
	 *
	 * @param misCodes list of MIS code integration objects to update
	 */
	public void setMisCodes(List<LiqAPIMISCodeIntegration> misCodes) {
		this.misCodes = misCodes;
	}

	/**
	 * Validates the license for this operation.
	 * Currently returns the same instance without additional license validation.
	 *
	 * @return this instance after license validation
	 */
	@Override
	public LiqAPIExecutableData validateLicense() {
		return this;
	}

	/**
	 * Returns the security access symbol required for this operation.
	 *
	 * @return the security access symbol for updating MIS codes
	 */
	public String securityAccessSymbol() {
		return APICommonConstants.SECURITY_ACCESS_SYMBOL_UPDATE_MIS_CODE;
	}

	/**
	 * Performs comprehensive validation before executing the update operation.
	 * Validates identifiers, MIS codes, and timestamp for optimistic locking.
	 *
	 * @throws LiqError if validation fails
	 */
	@Override
	public void basicValidate() {
		super.basicValidate();
		validateIdentifiers();
		validateMisCodes();
        validateTimeStamp(getBusinessObject(),this.getMatchUpdatedTimestamp());
	}

	/**
	 * Gets the business object (Deal, Facility, or Outstanding) based on the owner identifier.
	 * The object type is determined by the owner type specified in the identifier.
	 *
	 * @return the business object to be updated, or null if owner identifier is invalid
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

	/**
	 * Validates the MIS codes for business rule compliance.
	 * Currently validates for duplicate MIS codes of the same type.
	 */
	public void validateMisCodes() {
		this.validateForDuplicateMISCodes();
        this.validateForInvalidMISCodes(this.getMisCodes());
        this.validateValue(this.getMisCodes());
        //this.validateForUserEntrable(this.getMisCodes());
	}

    private void validateValue(List<LiqAPIMISCodeIntegration> misCodes) {
        List<HashMap> misCodeList = ((OnlineCodeTable) LoanIQ.currentSession().codeTableNamed("MIS Code")).asDictionaries();
        for (LiqAPIMISCodeIntegration misCode : misCodes) {
            if(isValueTypeRequired(misCode,misCodeList)){
                if(misCode.getValue() != null && !misCode.getValue().trim().isEmpty()){
                    ExceptionUtility.throwException(new LiqError(
                            Messages.liqNlsExternalizedMessage(StringUtility.bindWith("MIS Code 'value' is not required for MIS Code Type: %1. Instead 'valueType' should be passed for this MIS Code: %1", misCode.getType())),
                            this
                    ));
                }
                if(misCode.getValueType() == null || misCode.getValueType().trim().isEmpty()){
                    ExceptionUtility.throwException(new LiqError(
                            Messages.liqNlsExternalizedMessage(StringUtility.bindWith("MIS Code 'valueType' is required for MIS Code Type: %1", misCode.getType())),
                            this
                    ));
                }
                this.validateValueType(misCode);
            }else{
                if(misCode.getValueType() != null && !misCode.getValueType().trim().isEmpty()){
                    ExceptionUtility.throwException(new LiqError(
                            Messages.liqNlsExternalizedMessage(StringUtility.bindWith("MIS Code 'valueType' is not required for MIS Code Type: %1. Instead 'value' should be passed for this MIS Code: %1", misCode.getType())),
                            this
                    ));
                }
                if(misCode.getValue() == null || misCode.getValue().trim().isEmpty()){
                    ExceptionUtility.throwException(new LiqError(
                            Messages.liqNlsExternalizedMessage(StringUtility.bindWith("MIS Code 'value' is required for MIS Code Type: %1", misCode.getType())),
                            this
                    ));
                }

            }
        }
    }

    private boolean isValueTypeRequired(LiqAPIMISCodeIntegration misCode, List<HashMap> misCodeList ) {
        List<String> misTypeList = new ArrayList<>();
        for(Map map:misCodeList){
            misTypeList.add((String) map.get("misType"));
        }
        if(misTypeList.contains(misCode.getType()) && !this.isTypeOfAmount(misCode.getType()) && !this.isTypeOfDate(misCode.getType())){
            return true;
        }
        return false;
    }

    private void validateValueType(LiqAPIMISCodeIntegration misCode) {
        String misTypeCode = ((OnlineCodeTable) LoanIQ.currentSession().codeTableNamed("MIS Code")).retrieveByCode(misCode.getValueType()).get("misType").toString();
        if(!(misCode.getType().equalsIgnoreCase(misTypeCode))){
            ExceptionUtility.throwException(new LiqError(
                    Messages.liqNlsExternalizedMessage(StringUtility.bindWith("MIS Code valueType: %1 is not valid for MIS Code Type: %2..", misCode.getValueType(), misCode.getType())),
                    this
            ));
        }
    }

    public void validateForUserEntrable(List<LiqAPIMISCodeIntegration> misCodes) {
        for (LiqAPIMISCodeIntegration misCode : misCodes) {
            if (CompareUtility.equals(((OnlineCodeTable) LoanIQ.currentSession().codeTableNamed("MIS Type")).retrieveByCode(misCode.getType()).get("userEnterable"), "N")) {
                if (misCode.getValueType() == null || misCode.getValueType().trim().isEmpty()) {
                    ExceptionUtility.throwException(new LiqError(
                            Messages.liqNlsExternalizedMessage(StringUtility.bindWith("MIS Code valueType is required for User Non Enterable MIS Code Type: %1", misCode.getType())),
                            this
                    ));
                } else {
                    String validMISCodeType = getValidMISCodeTypeForMISCodesValueType(misCode.getValueType());
                    if (validMISCodeType == null || validMISCodeType.isEmpty() || !validMISCodeType.equalsIgnoreCase(misCode.getType())) {
                        ExceptionUtility.throwException(new LiqError(
                                Messages.liqNlsExternalizedMessage(StringUtility.bindWith("MIS Code valueType: %1 is not valid for User Non Enterable MIS Code Type: %2..", misCode.getValueType(), misCode.getType())),
                                this
                        ));
                    }
                }
            }
        }
    }

    public String getValidMISCodeTypeForMISCodesValueType(String valueType) {
        return LoanIQ.currentSession().code(valueType, "MIS Code").get("misType").toString();
    }

    /**
     * Validates that all MIS codes have valid types that exist in the MIS Type code table.
     * Collects all invalid MIS code types and creates a consolidated error message.
     * Uses Set-based lookup for O(1) performance instead of List.contains() O(n).
     *
     * @param misCodes the list of MIS codes to validate
     * @throws LiqError if any MIS code has invalid type(s) or missing required fields
     */
    public void validateForInvalidMISCodes(List<LiqAPIMISCodeIntegration> misCodes) {
        // Early return for null or empty list
        if (misCodes == null || misCodes.isEmpty()) {
            return;
        }

        // Validate for null or empty types first
        misCodes.stream()
            .filter(misCode -> misCode.getType() == null || misCode.getType().trim().isEmpty())
            .findFirst()
            .ifPresent(misCode -> {
                ExceptionUtility.throwException(new LiqError(
                    Messages.liqNlsExternalizedMessage("MIS Code Type cannot be null or empty"),
                    this
                ));
            });

        // Convert to Set for O(1) lookup performance (vs O(n) for List.contains)
        Set<String> validMISTypeCodes = new HashSet<>(allMISTypeCodeValues());

        // Collect all invalid MIS code types (distinct values only)
        List<String> invalidMISTypes = misCodes.stream()
            .map(LiqAPIMISCodeIntegration::getType)
            .filter(type -> type != null && !type.trim().isEmpty())
            .filter(type -> !validMISTypeCodes.contains(type))
            .distinct()
            .collect(Collectors.toList());

        // If there are invalid types, throw error with consolidated message
        if (!invalidMISTypes.isEmpty()) {
            String ownerType = getOwnerTypeDisplayName();
            String invalidTypesCommaSeparated = String.join(", ", invalidMISTypes);
            String errorMessage = StringUtility.bindWith(
                "MIS Code type '%1' is not enabled for '%2'.",
                invalidTypesCommaSeparated,
                ownerType
            );
            ExceptionUtility.throwException(new LiqError(
                Messages.liqNlsExternalizedMessage(errorMessage),
                this
            ));
        }
    }

    /**
     * Gets the display name for the owner type.
     *
     * @return the owner type display name (e.g., "Deal", "Facility", "Outstanding")
     */
    private String getOwnerTypeDisplayName() {
        if (ownerIdentifier == null || ownerIdentifier.getOwnerType() == null) {
            return "Unknown";
        }

        String ownerType = ownerIdentifier.getOwnerType().toUpperCase();
        switch (ownerType) {
            case "DEA":
                return "Deal";
            case "FAC":
                return "Facility";
            case "OST":
                return "Outstanding";
            case "LNID":
            	return "Loan Initial Drawdown";
            default:
                return ownerType;
        }
    }

    /**
     * Retrieves all valid MIS type code values from the MIS Type code table.
     * Filters codes based on the indicator type flag being "Y" for the current owner type.
     * Uses modern Java streams for efficient collection processing.
     *
     * @return list of MIS type code values as strings that are enabled for the current owner type
     */
    @SuppressWarnings("unchecked")
    public List<String> allMISTypeCodeValues() {
        CodeTable codeTable = LoanIQ.currentSession().codeTableNamed("MIS Type");
        List<Map<String, Object>> dictionaries = (List<Map<String, Object>>) codeTable.asDictionaries();
        String indicatorType = getIndicatorTypes() != null? getIndicatorTypes().get(0):null;
        String requiredForAllIndicatorType = getIndicatorTypes() != null &&  getIndicatorTypes().size() > 1? getIndicatorTypes().get(1):null;

        return dictionaries.stream()
                .filter(dictionary -> {
                    // Filter by indicatorType equals "Y"
                    Object indicatorValue = dictionary.get(indicatorType);
                    Object requiredForAllIndicatorValue = "N";
                    if(requiredForAllIndicatorType != null){
                        requiredForAllIndicatorValue = "Y".equalsIgnoreCase(String.valueOf(requiredForAllIndicatorType))?"Y":"N";
                    }

                    return "Y".equals(indicatorValue) || "Y".equalsIgnoreCase(String.valueOf(indicatorValue)) || "Y".equalsIgnoreCase(String.valueOf(requiredForAllIndicatorValue));
                })
                .map(dictionary -> dictionary.get("code"))
                .filter(code -> code != null)
                .map(Object::toString)
                .filter(code -> !code.isEmpty())
                .collect(Collectors.toList());
    }

    public List<String >getIndicatorTypes() {
        if("DEA".equalsIgnoreCase(ownerIdentifier.getOwnerType())) {
            return List.of("dealIndicator");
        } else if("FAC".equalsIgnoreCase(ownerIdentifier.getOwnerType())) {
            return List.of("facilityIndicator","requiredForAllFacilitiesIndicator");
        } else {
            return List.of("loanIndicator");
        }
    }

    /**
	 * Validates that there are no duplicate MIS codes of the same type in the update request.
	 * Throws an error if duplicates are found.
	 *
	 * @throws LiqError if duplicate MIS codes of the same type are detected
	 */
	public void validateForDuplicateMISCodes() {
		if (this.getMisCodes() == null  || (this.getMisCodes() != null && this.getMisCodes().isEmpty()))
			return;
		List apiMisCodeTypes = new ArrayList();
		for (Object apiMisCodeType : this.getMisCodes()) {
			apiMisCodeTypes.add(((LiqAPIMISCodeIntegration) apiMisCodeType).getType());
		}
		if (LiqUtilities.containsDuplicates(apiMisCodeTypes)) {
			ExceptionUtility.throwException(new LiqError(
					Messages.liqNlsExternalizedMessage("Duplicate MIS Codes of same type are not allowed. Please remove the duplicates."),this));
			return;
		}
	}

	/**
	 * Validates the owner identifier to ensure it is present and properly formatted.
	 *
	 * @throws LiqError if owner identifier is missing or invalid
	 */
	private void validateIdentifiers() {
		if(ownerIdentifier!=null) {
			ownerIdentifier.basicValidate();
		}else {
			ExceptionUtility.throwException(new LiqError(ErrorMessageConstants.OWNER_IDENTIFIER_REQUIRED));
		}

	}

	/**
	 * Executes the MIS code update operation with proper transaction management.
	 * Performs security checks, locks the data, updates MIS codes, and commits the transaction.
	 *
	 * @return the response object containing the results of the update operation
	 * @throws LiqError if the operation fails at any step
	 */
	@Override
	public Object basicExecute() {
		try{
			checkDealSecurity();
			checkCustomerSecurity();
			this.lockAPIData();
			updateMisCodes();
			this.singleCommit();
		}finally {
			this.unLockAPIData();
		}
		return response();
	}

	/**
	 * Validates customer security access for all borrowers associated with the owner object.
	 * Checks security for Deal borrowers or Facility borrowers based on owner type.
	 *
	 * @throws LiqError if customer security validation fails
	 */
    public void checkCustomerSecurity() {
        if (ownerIdentifier.getOwnerType().toUpperCase().equals("DEA")) {
            Iterator iterator = ((Deal)getBusinessObject()).getBorrowers().iterator();
            while (iterator.hasNext()) {
                DealBorrower ea = (DealBorrower) iterator.next();
                customerSecurity(ea.getCustomerId());
            }
        }else if (ownerIdentifier.getOwnerType().toUpperCase().equals("FAC")) {
            Iterator iterator = ((Facility)getBusinessObject()).getBorrowers().iterator();
            while (iterator.hasNext()) {
                FacilityBorrower ea = (FacilityBorrower) iterator.next();
                customerSecurity(ea.getBorrowerId());
            }
        }else if (ownerIdentifier.getOwnerType().toUpperCase().equals("LNID")) {
        	OutstandingTransaction ost = (OutstandingTransaction) OutstandingTransaction.clazz.getBusinessObjectFor(ownerIdentifier.getOwnerIdentifierType(),
					ownerIdentifier.getOwnerIdentifierValue());
			Iterator iterator = ost.getDeal().getBorrowers().iterator();
			while (iterator.hasNext()) {
				DealBorrower ea = (DealBorrower) iterator.next();
				customerSecurity(ea.getCustomerId());
			}
        }
    }

	/**
	 * Locks the API data to prevent concurrent modifications.
	 * This method delegates to the lockData() method for actual locking implementation.
	 */
	public void lockAPIData() {
		this.lockData();
	}

	/**
	 * Locks the business object associated with the owner identifier.
	 * Uses the internal lockObject method to perform the actual locking.
	 */
	private void lockData() {
		LiqBusinessObject anObject = this.ownerIdentifier.owner;
		lockObject(anObject);
	}

	/**
	 * Applies an exclusive update lock on the specified business object.
	 *
	 * @param anObject the business object to lock
	 * @return true if the lock was successfully applied, false otherwise
	 * @throws LiqError if the locking operation fails
	 */
	public Boolean lockObject(Object anObject) {
		if (anObject == null)
			return false;
		try {
			((LiqBusinessObject) anObject).exclusiveUpdateLockFor(this);
		} catch (final LiqMessageException ex) {
			ExceptionUtility.throwException(new LiqError(ex, this));
			ExceptionUtility.exitWith(ex, null);
			return false;
		}
		return true;
	}

	/**
	 * Unlocks the API data to allow further modifications.
	 * This method delegates to the unlockData() method for actual unlocking implementation.
	 */
	public void unLockAPIData() {
		this.unlockData();
	}

	/**
	 * Adds IDs from the provided list of updateable data objects to the current context.
	 * Used for tracking which objects were modified during the operation.
	 *
	 * @param objects list of updateable data objects whose IDs should be tracked
	 */
	@Override
	public void addIds(List<LS2UpdateableData> objects) {
        if (null == objects || objects.isEmpty()) {
            return;
        }
        setIds(objects.stream().map(tran -> tran.getId()).collect(Collectors.toList()));
	}

	/**
	 * Unlocks the business object that was previously locked for the update operation.
	 * This implementation unlocks the business object associated with the owner identifier.
	 */
	@Override
	public void unlockData() {
		LiqBusinessObject liqObject = this.getBusinessObject();
		this.unlockObject(liqObject);
	}

	/**
	 * Removes the exclusive update lock from the specified business object.
	 *
	 * @param anObject the business object to unlock
	 * @return true if the unlock was successful, false otherwise
	 * @throws LiqError if the unlocking operation fails
	 */
	public Boolean unlockObject(Object anObject) {
		if (anObject == null)
			return false;
		try {
			((LiqBusinessObject) anObject).exclusiveUpdateUnlockFor(this);
		} catch (final LiqMessageException ex) {
			ExceptionUtility.throwException(new LiqError(ex, this));
			ExceptionUtility.exitWith(ex, null);
			return false;
		}
		return true;
	}

	/**
	 * Validates deal security for the current transaction.
	 * Retrieves the deal ID based on the owner type and performs security validation.
	 *
	 * @throws IllegalArgumentException if the owner type is not supported
	 * @throws LiqError if deal security validation fails
	 */
	public void checkDealSecurity() {
		String ownerType = ownerIdentifier.getOwnerType().toUpperCase();
		switch (ownerType) {
		case "DEA":
			dealSecurity(ownerIdentifier.getDeal().getDealId());
			break;
		case "FAC":
			dealSecurity(ownerIdentifier.getFacility().getDeal().getDealId());
			break;
		case "LNID":
			dealSecurity(ownerIdentifier.getOutstandingTransaction().getDeal().getDealId());
			break;
		default:
			throw new IllegalArgumentException("Unsupported owner type: " + ownerType);
		}
	}

	/**
	 * Creates and returns the response object for the update operation.
	 * The response contains the updated business object and transaction details.
	 *
	 * @return the response object, or an empty string if the operation was not successful
	 */
	@Override
	public Object response() {
        Object object = LiqAPIMISCodeIntegrationAsReturnValue.clazz.forUpdate(this.getBusinessObject());
        this.addIds(List.of(this.getBusinessObject()));
        return this.getBusinessObject() == null || !this.getBusinessObject().isSaved() ? new String() : nonNull(object) ? object : new String();
	}

	/**
	 * Updates MIS codes based on the owner type specified in the owner identifier.
	 * Delegates to specific update methods for Deal, Facility, or Outstanding objects.
	 *
	 * @throws IllegalArgumentException if the owner type is not supported
	 */
	private void updateMisCodes() {
		if (isMisCodesEmpty()) {
			return;
		}

		switch (ownerIdentifier.getOwnerType().toUpperCase()) {
		case "DEA":
			updateMisCodesForDeal();
			break;
		case "FAC":
			updateMisCodesForFacility();
			break;
		case "OST":
			updateMisCodesForOST();
			break;
		case "LNID":
			updateMisCodesForLNID();
			break;
		default:
			throw new IllegalArgumentException("Unsupported owner type: " + ownerIdentifier.getOwnerType());
		}
	}

	/**
	 * Checks if the MIS codes list is null or empty.
	 *
	 * @return true if MIS codes list is null or empty, false otherwise
	 */
	private boolean isMisCodesEmpty() {
		return this.getMisCodes() == null || this.getMisCodes().isEmpty();
	}

	/**
	 * Updates MIS codes for a Deal object.
	 * Compares existing MIS codes with the provided ones and performs add/update operations.
	 * Creates new MIS codes for types that don't exist and updates existing ones.
	 */
	private void updateMisCodesForDeal() {
		Deal deal = this.ownerIdentifier.getDeal();
        if(deal != null){
            // delete mis codes from facility where deleteIndicator is true in the request
            deletedMISCodesFor(deal);

            // update mis codes where deleteIndicator is false in the request
            updateMISCodeFor(deal);

            // insert mis codes
            insertMISCodeFor(deal);
        }
	}

	/**
	 * Updates MIS codes for a Facility object.
	 * Handles both update and delete operations based on the delete indicator.
	 * Creates new MIS codes for types that don't exist and updates or deletes existing ones.
	 */
	private void updateMisCodesForFacility() {
		Facility facility = this.ownerIdentifier.getFacility();
		if(facility != null){
            // delete mis codes from facility where deleteIndicator is true in the request
			deletedMISCodesFor(facility);

            // update mis codes where deleteIndicator is false in the request
            updateMISCodeFor(facility);

            // insert mis codes
            insertMISCodeFor(facility);

		}
	}

    private void insertMISCodeFor(KeyedDataObject bo) {
        if(bo == null){
            return;
        }
        List<MISCode> toBeInserted = new ArrayList<>();
        List<String> apiMisCodeTypes = this.getMisCodes().stream().filter(misCode -> misCode.getDeleteIndicator() == null || !misCode.getDeleteIndicator()).map(LiqAPIMISCodeIntegration::getType).collect(Collectors.toList());
        List<MISCode> existingMISCodes = bo.getMisCodes();
        List<String> existingMISCodeTypes = new ArrayList<>();
        for(MISCode miscode:existingMISCodes){
            existingMISCodeTypes.add((String) miscode.getType());
        }
        for(LiqAPIMISCodeIntegration e:this.getMisCodes()){
            //If the MIS Code from request payload is not associated to facility and deleteIndicator is false then only create the MIS Code, otherwise skip the record.
            if (!existingMISCodeTypes.contains(e.getType()) && (e.getDeleteIndicator() == null || !e.getDeleteIndicator())) {
                ProductMISCode t = ProductMISCode.clazz.createFor(bo);
                if(e.getType() != null){
                    t.type(e.getType());
                }else{
                    ExceptionUtility.throwException(new LiqError(
                            Messages.liqNlsExternalizedMessage(StringUtility.bindWith("MIS Code Type cannot be null for MIS Code Type: %1", e.getType())),
                            this
                    ));
                }
                if (e.getValue() != null && !e.getValue().trim().isEmpty())
                {
                    if (this.isTypeOfAmount(e.getType())) {
                        validateNumericValue(e.getValue(), e.getType());
                        BigDecimal r1 = new BigDecimal(e.getValue());
                        t.setValueAmount(r1);
                    }
                    if (this.isTypeOfDate(e.getType())) {
                        try {
                            t.setValueDate((LiqDate) LiqDate.clazz.fromStringOrAsIs(e.getValue()));
                        }catch(Exception ex){
                            ExceptionUtility.throwException(new LiqError(
                                    Messages.liqNlsExternalizedMessage(StringUtility.bindWith("MIS Code value: %1 is not valid date for MIS Code Type: %2..", e.getValue(), e.getType())),
                                    this
                            ));
                        }
                    }
                    if (this.isTypeOfValue(e.getType())) {
                        validateText(e.getValue(),e.getType());
                        t.zz_value(e.getValue());
                    }
                    if (this.isTypeOfMultiLineText(e.getType())) {
                        validateMText(e.getValue(),e.getType());
                        t.valueMultiLineText(e.getValue());
                    }
                } else {
                   if(e.getValueType() != null && !e.getValueType().trim().isEmpty()){
                       t.setValueType(e.getValueType());
                   }
                }
                t.save();
                ((LiqBusinessObject) bo).updateCollection("misCodes", t);
                toBeInserted.add(t);
            }
        }
        bo.updateCollectionWithAll(toBeInserted);
        bo.save();
    }

    private void updateMISCodeFor(KeyedDataObject bo) {
        if(bo == null){
            return;
        }
        List<MISCode> toBeUpdated = new ArrayList<>();
        List<String> apiMisCodeTypes = this.getMisCodes().stream().filter(misCode -> misCode.getDeleteIndicator() == null || !misCode.getDeleteIndicator()).map(LiqAPIMISCodeIntegration::getType).collect(Collectors.toList());
        if (!apiMisCodeTypes.isEmpty() && bo != null) {
            List<MISCode> existingMISCodes = bo.getMisCodes();
            for(MISCode miscode:existingMISCodes){
                if (apiMisCodeTypes.contains(miscode.getType())) {
                    for (LiqAPIMISCodeIntegration e : this.getMisCodes()) {
                        if (e.getType().equals(miscode.getType())) {
                            if(e.getValue() != null && !e.getValue().trim().isEmpty()){
                                if (this.isTypeOfAmount(e.getType())) {
                                    validateNumericValue(e.getValue(), e.getType());
                                    BigDecimal r1 = new BigDecimal(e.getValue());
                                    miscode.setValueAmount(r1);
                                }
                                if (this.isTypeOfDate(e.getType())) {
                                    try {
                                        miscode.setValueDate((LiqDate) LiqDate.clazz.fromStringOrAsIs(e.getValue()));
                                    }catch(Exception ex){
                                        ExceptionUtility.throwException(new LiqError(
                                                Messages.liqNlsExternalizedMessage(StringUtility.bindWith("MIS Code value: %1 is not valid date for MIS Code Type: %2..", e.getValue(), e.getType())),
                                                this
                                        ));
                                    }
                                }
                                if (this.isTypeOfValue(e.getType())) {
                                    validateText(e.getValue(), e.getType());
                                    miscode.zz_value(e.getValue());
                                }
                                if (this.isTypeOfMultiLineText(e.getType())) {
                                    validateMText(e.getValue(), e.getType());
                                    miscode.valueMultiLineText(e.getValue());
                                }
                            }else{
                                if(e.getValueType() != null && !e.getValueType().trim().isEmpty()){
                                    miscode.zz_valueType(e.getValueType());

                                }
                            }
                            miscode.save();
                            ((LiqBusinessObject) bo).updateCollection("misCodes", miscode);
                            toBeUpdated.add(miscode);
                        }
                    }
                }
            }
            if(!toBeUpdated.isEmpty()){
                bo.updateCollectionWithAll(toBeUpdated);
                bo.save();
            }
        }
    }

    private void validateText(String value, String type) {
        if (value == null || value.trim().isEmpty()) {
            return;
        }

        String trimmedValue = value.trim();

        // Check if the value exceeds 20 characters
        if (trimmedValue.length() > 20) {
            ExceptionUtility.throwException(new LiqError(
                    Messages.liqNlsExternalizedMessage(StringUtility.bindWith(
                            "MIS Code value of length %1 exceeds the maximum allowed length of 20 characters for MIS Code Type: %2",
                            String.valueOf(trimmedValue.length()), type)),
                    this
            ));
        }
    }

    private void validateMText(String value, String type) {
        if (value == null || value.trim().isEmpty()) {
            return;
        }

        String trimmedValue = value.trim();

        // Check if the value exceeds 256 characters
        if (trimmedValue.length() > 256) {
            ExceptionUtility.throwException(new LiqError(
                    Messages.liqNlsExternalizedMessage(StringUtility.bindWith(
                            "MIS Code value of length %1 exceeds the maximum allowed length of 256 characters for MIS Code Type: %2",
                            String.valueOf(trimmedValue.length()), type)),
                    this
            ));
        }
    }

    private void validateNumericValue(String value, String type) {
        if (value == null || value.trim().isEmpty()) {
            return;
        }

        String trimmedValue = value.trim();

        // Define the maximum and minimum allowed values
        BigDecimal maxValue = new BigDecimal("9999999999.999999");
        BigDecimal minValue = new BigDecimal("-9999999999.999999");

        try {
            // Parse the value as BigDecimal
            BigDecimal numericValue = new BigDecimal(trimmedValue);

            // Round to 6 decimal places if it has more than 6 decimal places
            numericValue = numericValue.setScale(6, BigDecimal.ROUND_HALF_UP);

            // Check if the value exceeds the maximum allowed value
            if (numericValue.compareTo(maxValue) > 0) {
                ExceptionUtility.throwException(new LiqError(
                        Messages.liqNlsExternalizedMessage(StringUtility.bindWith(
                                "MIS Code value: %1 exceeds the maximum allowed value for MIS Code Type: %2",
                                trimmedValue, type)),
                        this
                ));
            }

            // Check if the value is less than the minimum allowed value
            if (numericValue.compareTo(minValue) < 0) {
                ExceptionUtility.throwException(new LiqError(
                        Messages.liqNlsExternalizedMessage(StringUtility.bindWith(
                                "MIS Code value: %1 is less than the minimum allowed value for MIS Code Type: %2",
                                trimmedValue, type)),
                        this
                ));
            }

        } catch (NumberFormatException ex) {
            // Throw an exception if the value is not a valid number
            ExceptionUtility.throwException(new LiqError(
                    Messages.liqNlsExternalizedMessage(StringUtility.bindWith(
                            "MIS Code value: %1 is not a valid numeric value for MIS Code Type: %2",
                            trimmedValue, type)),
                    this
            ));
        }
    }

    private void deletedMISCodesFor(KeyedDataObject bo) {
        List<MISCode> toBedeleted = new ArrayList<>();

        // Get MIS codes where deleteIndicator is true
        List<String> deletedMISCodeTypesFromRequest = this.getMisCodes().stream()
                .filter(misCode -> misCode.getDeleteIndicator() != null && misCode.getDeleteIndicator())
                .map(LiqAPIMISCodeIntegration::getType)
                .collect(Collectors.toList());

        // Find matching MIS codes in facility
        if (!deletedMISCodeTypesFromRequest.isEmpty() && bo != null) {
            List<MISCode> existingMISCodes = bo.getMisCodes();
            for(MISCode miscode:existingMISCodes){
                if (deletedMISCodeTypesFromRequest.contains(miscode.getType())) {
                    toBedeleted.add(miscode);
                }
            }
            // remove mis codes from facility
            if(toBedeleted != null && !toBedeleted.isEmpty()){
                bo.removeFromCollectionAll(toBedeleted);
                bo.save();
            }
        }
    }

   	private void updateMisCodesForOST() {
   		Outstanding ost = (Outstanding) Outstanding.clazz.getBusinessObjectFor(ownerIdentifier.getOwnerIdentifierType(),
   				ownerIdentifier.getOwnerIdentifierValue());
   	}

	private void updateMisCodesForLNID() {
		OutstandingTransaction outstandingTransaction = this.ownerIdentifier
				.getOutstandingTransaction();
		if (outstandingTransaction != null) {

			// delete mis codes from loan where deleteIndicator is true in
			// the request
			deletedMISCodesFor(outstandingTransaction);

			// update mis codes where deleteIndicator is false in the request
			updateMISCodeFor(outstandingTransaction);

			// insert mis codes
			insertMISCodeFor(outstandingTransaction);

		}
	}

    public boolean isTypeOfAmount(String type) {
        return "NUM".equals(LoanIQ.currentSession().attribute("foreignKey1", type, "MIS Type"));
    }

    public boolean isTypeOfDate(String type) {
        return "DATE".equals(LoanIQ.currentSession().attribute("foreignKey1", type, "MIS Type"));
    }

    public boolean isTypeOfMultiLineText(String type) {
        return "MTEXT".equals(LoanIQ.currentSession().attribute("foreignKey1", type, "MIS Type"));
    }

    public boolean isTypeOfValue(String type) {
        return "TEXT".equals(LoanIQ.currentSession().attribute("foreignKey1", type, "MIS Type"));
    }

    public static class Class extends LiqAPIExecutableData.Class implements StClass {

		protected Class() {
		}

		public StObject basicNew() {
			return new LiqAPIUpdateMISCodeIntegration();
		}

		public java.lang.Class getJavaClass() {
			return LiqAPIUpdateMISCodeIntegration.class;
		}

		public StClass getStSuperclass() {
			return LiqAPIExecutableData.clazz;
		}

		public List nonPrimitiveFieldCollectionMappings() {
			List<LiqAPINonPrimitiveFieldMapping> mappings = super.nonPrimitiveFieldCollectionMappings();
			LiqAPINonPrimitiveFieldMapping t1 = (LiqAPINonPrimitiveFieldMapping) LiqAPINonPrimitiveFieldMapping.clazz.newStObject();
			t1.setFieldName("misCodes");
			t1.setFieldApiClass(LiqAPIMISCodeIntegration.clazz);
			mappings.add(t1);
			return mappings;
		}

		public List nonPrimitiveFieldMappings() {
			List mappings = super.nonPrimitiveFieldMappings();
			LiqAPINonPrimitiveFieldMapping t1 = (LiqAPINonPrimitiveFieldMapping) LiqAPINonPrimitiveFieldMapping.clazz.newStObject();
			t1.setFieldName("ownerIdentifier");
			t1.setFieldApiClass(LiqAPIOwnerIdentifier.clazz);
			mappings.add(t1);

			return mappings;
		}

		public List primitiveFieldMappings() {
			return super.primitiveFieldMappings();
		}

		@Override
		public boolean isRest() {
			return true;
		}

	}

}
```

---

## 3. Pattern B — Outstanding with Spread Component (LoanDrawdown)

**Source:** `LoanIQ/srcgen/com/misys/liq/api/rest/executable/outstanding/drawdown/LiqAPIUpdateLoanDrawdownIntegration.java`

```java
package com.misys.liq.api.rest.executable.outstanding.drawdown;

import com.finastra.liq.api.annotation.LiqAPIFieldMapper;
import com.misys.liq.Messages;
import com.misys.liq.api.data.*;
import com.misys.liq.api.data.comparator.MISCodeComparator;
import com.misys.liq.api.data.customer.LiqAPICustomerIdentifier;
import com.misys.liq.api.data.facility.LiqAPIFacilityIdentifier;
import com.misys.liq.api.data.miscode.LiqAPIMISCode;
import com.misys.liq.api.data.outstanding.LiqAPIOutstandingIdentifier;
import com.misys.liq.api.data.outstanding.LiqAPIOutstandingTransactionIdentifier;
import com.misys.liq.api.data.utils.DataUtils;
import com.misys.liq.api.executable.IAPIRestIntegration;
import com.misys.liq.api.executable.LiqAPIExecutableData;
import com.misys.liq.api.executable.outstanding.LiqAPIUpdateLoanDrawdown;
import com.misys.liq.api.rest.data.outstanding.drawdown.LiqAPILoanDrawdownIntegrationAsReturnValue;
import com.misys.liq.api.rest.data.outstanding.spread.LiqAPISpreadAdjustmentComponentOverrideIntegration;
import com.misys.liq.api.rest.data.outstanding.spread.LiqAPISpreadComponentIntegrationCommonUtil;
import com.misys.liq.bm.desktopcore.amortization.TransactionWithAmortizationBuckets;
import com.misys.liq.bm.desktopcore.main.cdt.customer.Customer;
import com.misys.liq.bm.desktopcore.main.cdt.facility.Facility;
import com.misys.liq.bm.desktopcore.main.cdt.funding.Money;
import com.misys.liq.bm.desktopcore.main.cdt.loan.AbstractLoanRates;
import com.misys.liq.bm.desktopcore.main.cdt.loan.Loan;
import com.misys.liq.bm.desktopcore.main.cdt.loan.LoanInitialDrawdown;
import com.misys.liq.bm.desktopcore.main.cdt.loan.LoanStructuringTransaction;
import com.misys.liq.bm.desktopcore.main.cdt.loan.LoanTransaction;
import com.misys.liq.bm.desktopcore.main.cdt.loan.Outstanding;
import com.misys.liq.bm.product.AccrualState;
import com.misys.liq.bm.desktopcore.main.cdt.loan.*;
import com.misys.liq.bm.main.cdt.loan.LiqMNAlternateReferenceRatesModelEnhancement;
import com.misys.liq.bm.product.LiqMNFacilityInterestPricingOption;
import com.misys.liq.bm.product.LiqMNPercentOfRateFormulaAndRoundingEnhancement;
import com.misys.liq.bm.product.NonAccrualState;
import com.misys.liq.bm.product.ProductMISCode;
import com.misys.liq.bm.spreadmatrix.LiqMNOutstandingSpreadComponentDetails;
import com.misys.liq.enhancements.Bill.LiqMNInterestScheduleForRepricingDateForOutstandingModelEnhancement;
import com.misys.liq.infrastructure.LiqDate;
import com.misys.liq.infrastructure.LiqObject;
import com.misys.liq.infrastructure.bm.labstrct.KeyedDataObject;
import com.misys.liq.infrastructure.bm.labstrct.LS2UpdateableData;
import com.misys.liq.infrastructure.bm.labstrct.MISCode;
import com.misys.liq.infrastructure.exceptions.LiqError;
import com.sxsy.smtj.StClass;
import com.sxsy.smtj.StClassRegistry;
import com.sxsy.smtj.StObject;
import com.sxsy.smtj.exceptions.ExceptionUtility;
import com.sxsy.smtj.utilities.CollectionUtility;
import com.sxsy.smtj.utilities.CompareUtility;
import com.sxsy.smtj.utilities.ReflectionUtility;
import com.sxsy.smtj.utilities.StringUtility;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

public class LiqAPIUpdateLoanDrawdownIntegration extends
		LiqAPIUpdateLoanDrawdown implements IAPIRestIntegration{

	public static final Class clazz = new Class();
	
	public Boolean interestRateIsFloating;
	
	@LiqAPIFieldMapper(name = "OutstandingTransactionIdentifier", className = "com.misys.liq.api.data.outstanding.LiqAPIOutstandingTransactionIdentifier")
	public LiqAPIOutstandingTransactionIdentifier outstandingTransactionIdentifier;
	
	//facilityIdentifier changed as list and plural name
	@LiqAPIFieldMapper(name = "FacilityIdentifier", className = "com.misys.liq.api.data.facility.LiqAPIFacilityIdentifier")
	public List<LiqAPIFacilityIdentifier> facilityIdentifiers;

	//outstandingIdentifier changed as list and plural name
	@LiqAPIFieldMapper(name = "OutstandingIdentifier", className = "com.misys.liq.api.data.outstanding.LiqAPIOutstandingIdentifier")
	public List<LiqAPIOutstandingIdentifier> outstandingIdentifiers;

	//borrowerIdentifier changed as list and plural name
	@LiqAPIFieldMapper(name = "CustomerIdentifier", className = "com.misys.liq.api.data.customer.LiqAPICustomerIdentifier")
	public List<LiqAPICustomerIdentifier> borrowerIdentifiers;

	@LiqAPIFieldMapper(name = "SpreadAdjustmentComponentOverrideIntegration", className = "com.misys.liq.api.rest.data.outstanding.spread.LiqAPISpreadAdjustmentComponentOverrideIntegration")
	public List<LiqAPISpreadAdjustmentComponentOverrideIntegration> ostSpreadAdjustmentComponents;

	public String sourceRefNum;
	public String systemSourceId;
	public Boolean repricingFrequencyApplies;
	public BigDecimal racRate;
	
	static {
		StClassRegistry.register(clazz);
	}
	
	public StClass getStClass() {
		return clazz;
	}

	public List<LiqAPISpreadAdjustmentComponentOverrideIntegration> getOstSpreadAdjustmentComponents() {
		return ostSpreadAdjustmentComponents;
	}

	public void setOstSpreadAdjustmentComponents(List<LiqAPISpreadAdjustmentComponentOverrideIntegration> ostSpreadAdjustmentComponents) {
		this.ostSpreadAdjustmentComponents = ostSpreadAdjustmentComponents;
	}

	public String getStatusCode() {
		return statusCode();
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode();
	}
	
	public String statusCode() {
		if(Objects.nonNull(getOutstandingTransactionIdentifier()) && Objects.nonNull(getOutstandingTran())) {
			return getOutstandingTran().getObjectStateCode();
		}
		return null;
	}

	/**
	 * Return actual value passed in API, if it is not null, if passed empty or interestRateIsFloating field not passed then get the value from existing outstanding transaction notebook
	 */
	public Boolean getInterestRateIsFloating() {
		return this.interestRateIsFloating == null ? this.getOutstandingTran().isBorrowerRateFloating() : this.interestRateIsFloating;
	}

	public void setInterestRateIsFloating(Boolean interestRateIsFloating) {
		this.interestRateIsFloating = interestRateIsFloating;
	}

	public LiqAPIOutstandingTransactionIdentifier getOutstandingTransactionIdentifier() {
		return outstandingTransactionIdentifier;
	}

	public void setOutstandingTransactionIdentifier(
			LiqAPIOutstandingTransactionIdentifier outstandingTransactionIdentifier) {
		this.outstandingTransactionIdentifier = outstandingTransactionIdentifier;
	}

	public List<LiqAPIFacilityIdentifier> getFacilityIdentifiers() {
		if(Objects.isNull(this.facilityIdentifiers)) {
			this.facilityIdentifiers = new ArrayList<LiqAPIFacilityIdentifier>();
		}
		return facilityIdentifiers;
	}

	public void setFacilityIdentifiers(List<LiqAPIFacilityIdentifier> facilityIdentifiers) {
		this.facilityIdentifiers = facilityIdentifiers;
	}
	
	public List<LiqAPIOutstandingIdentifier> getOutstandingIdentifiers() {
		if(Objects.isNull(this.outstandingIdentifiers)) {
			this.outstandingIdentifiers = new ArrayList<LiqAPIOutstandingIdentifier>();
		}
		return outstandingIdentifiers;
	}

	public void setOutstandingIdentifiers(List<LiqAPIOutstandingIdentifier> outstandingIdentifiers) {
		this.outstandingIdentifiers = outstandingIdentifiers;
	}

	public List<LiqAPICustomerIdentifier> getBorrowerIdentifiers() {
		if(Objects.isNull(this.borrowerIdentifiers)) {
			this.borrowerIdentifiers = new ArrayList<LiqAPICustomerIdentifier>();
		}
		return borrowerIdentifiers;
	}

	public void setBorrowerIdentifiers(List<LiqAPICustomerIdentifier> borrowerIdentifiers) {
		this.borrowerIdentifiers = borrowerIdentifiers;
	}

	public String getSourceRefNum() {
		return sourceRefNum;
	}

	public String getSystemSourceId() {
		return systemSourceId;
	}

	public void setSourceRefNum(String sourceRefNum) {
		this.sourceRefNum = sourceRefNum;
	}

	public void setSystemSourceId(String systemSourceId) {
		this.systemSourceId = systemSourceId;
	}
	
	public Boolean getRepricingFrequencyApplies() {
		return repricingFrequencyApplies;
	}

	public void setRepricingFrequencyApplies(Boolean repricingFrequencyApplies) {
		this.repricingFrequencyApplies = repricingFrequencyApplies;
	}
	
	public BigDecimal getRacRate() {
		return racRate;
	}

	public void setRacRate(BigDecimal racRate) {
		this.racRate = racRate;
	}

	/**
	 * Save racRate field added in update loan draw down rest api to align with online behavior
	 * throws error when racRate passed but racApplies indicator not set
	 * LIQ-152849 - Not null check added before calling set racRate
	 * LIQ-160610 - racRate is not getting updated in the LoanIQ via PUT call - used zz_ method 
	 */
	public void setRacRate() {
		if (this.getRacRate() != null && !this.outstandingTran().racApplies()) {
			ExceptionUtility.throwException(new LiqError(StringUtility.bindWith(
					Messages.liqNlsExternalizedMessage("racRate is invalid, \'RAC Applies\' indicator is not setup for currency \'%1\' on this facility"),
					this.getCurrency()), this));
		} else if (this.getRacRate() != null) {
			((AbstractLoanRates) this.outstandingTran().getRates()).zz_reserveAssetCostRate(this.getRacRate());
		}
	}
	
	@Override 
	public void updateOutstandingRepricingFreqencyAppplies() {
	    if (this.getRepricingFrequencyApplies() != null) {
	        ((Outstanding) this.outstandingTran().getOutstanding()).zz_hasRepricingDate(this.getRepricingFrequencyApplies());
	        if(!this.getRepricingFrequencyApplies()) {
	        	this.outstandingTran().zz_repricingFrequency(null);
	        	this.outstandingTran().zz_repricingDate(null);
	        }
	    }
	}

	@Override
	public void validateRoundingRulesDecimalAndFractionRounding(){
		if(!LiqMNPercentOfRateFormulaAndRoundingEnhancement.clazz.isLicensed())
			return;
		if(StringUtility.isNilOrBlank(this.getRoundingRulesDecimalRounding()) || StringUtility.isNilOrBlank(this.getRoundingRulesFractionRounding()))
			return;
		List allowed = LiqMNPercentOfRateFormulaAndRoundingEnhancement.clazz.allowedRoundingMethods();
		if(!allowed.contains(this.getRoundingRulesDecimalRounding().trim().toUpperCase()) || !allowed.contains(this.getRoundingRulesFractionRounding().trim().toUpperCase()))
			ExceptionUtility.throwException(new LiqError("Invalid rounding method. Allowed methods are UP, DOWN, NEAR and NONE"));
	}
	/**
	 * Overriding the method validateLicense of LiqAPIExecutableData class.
	 * This is temporary and would be changed at a later point of time.
	 */
	@Override
	public LiqAPIExecutableData validateLicense() {
		return this;
	}
	
	public Object basicExecute() {
		try {
			checkDealSecurity();
			checkCustomerSecurity();
			this.lockAPIData();
			super.basicExecute();
			this.updateSpreadComponent();
			this.performUpdate();
			this.singleCommit();
		}finally {
			if(this.getOutstandingTran()!=null) {
				this.getOutstandingTran().getPrivateState().remove("RefreshRates");		
			}
			this.unLockAPIData();
		}	
		return response();
	}
	
	/**
	 * Hook for subclasses to add extra update logic after the standard update
	 * and spread component processing, but before the final commit.
	 * Default implementation does nothing.
	 */
	protected void performUpdate() {
	}

    public void updateSpreadComponent() {
        if(LiqMNAlternateReferenceRatesModelEnhancement.clazz.isLicensed()) {
            validateSpreadAdjIndt();
            if (((this.getSpreadAdjApplies() != null && this.getSpreadAdjApplies())
                    || this.outstandingTran().getOutstanding().benchmarkAdjApplies())) {
                List<LiqAPISpreadAdjustmentComponentOverrideIntegration> components = this.getOstSpreadAdjustmentComponents();
                Outstanding outstanding = (Outstanding) this.outstandingTran().getOutstanding();

                // Condition 1:
                // If both spread adjustment rate and component overrides are absent,
                // derive and set rate from existing spread matrix components.
                if (LiqAPISpreadComponentIntegrationCommonUtil.isSpreadAdjComponentAndSpreadRateAbsent(this.getSpreadAdjRate(),
                        components)) {
                    this.updateSpreadAdjustmentRate(outstanding);
                    return;
                }

                // Condition 2:
                // If spread adjustment rate is provided but component overrides are missing,
                // prefer existing outstanding matrix components when present;
                // otherwise validate and use the passed spread adjustment rate.
                if (Objects.nonNull(this.getSpreadAdjRate()) && CollectionUtility.isEmpty(components)) {
                    if (nonNull(outstanding.getSpreadAdjMatrixList()) && !outstanding.getSpreadAdjMatrixList().isEmpty()) {
                        this.updateSpreadAdjustmentRate(outstanding);
                    } else {
                        LiqAPISpreadComponentIntegrationCommonUtil.validateAndCalculateTotal(outstanding.getSpreadAdjMatrixList());
                        ((AbstractLoanRates) outstanding.getRates()).setBenchmarkAdjRate(this.getSpreadAdjRate());
                        ((AbstractLoanRates) outstanding.getRates()).setBenchmarkAdjOverride(true);
                        outstanding.getRates().save();
                        ((LoanRates) this.outstandingTran.getOutstanding().getRates()).writeSetBenchmarkEvent();
                        this.saveObject();
                    }
                    return;
                }

                // Condition 3:
                // If component overrides are provided, perform intermediate commit
                // and recalculate spread adjustment rate from component totals.
                if (components != null && !components.isEmpty()) {
                    LiqAPISpreadComponentIntegrationCommonUtil liqAPISpreadComponentIntegrationCommonUtil =
                            (LiqAPISpreadComponentIntegrationCommonUtil) LiqAPISpreadComponentIntegrationCommonUtil.clazz.basicNew();
                    liqAPISpreadComponentIntegrationCommonUtil.validateAndProcessSpreadComponents(components, outstanding, false);
                    if (LiqMNAlternateReferenceRatesModelEnhancement.clazz.isLicensed()) {
                        this.updateSpreadAdjustmentRate(outstanding);
                    }
                }
            }
        }
    }

	@Override
	public void validateSpreadAdjIndt() {
		if (this.getSpreadAdjApplies() == null) {
			return;
		}
		LiqAPISpreadComponentIntegrationCommonUtil.validateSpreadAdjApplies(this.getSpreadAdjApplies(), (String) this.getPricingOption());
	}

	private void updateSpreadAdjustmentRate(Outstanding outstanding) {
		if (!(null != this.getSpreadAdjApplies() && !this.getSpreadAdjApplies() && null != this.getSpreadAdjRate())) {
            BigDecimal total = LiqAPISpreadComponentIntegrationCommonUtil.validateAndCalculateTotal(outstanding.getSpreadAdjMatrixList());
            if (!total.equals(((AbstractLoanRates) outstanding.getRates()).getBenchmarkAdjRate())) {
                ((AbstractLoanRates) outstanding.getRates()).setBenchmarkAdjRate(total);
                if (((AbstractLoanRates) outstanding.getRates()).getBenchmarkAdjOverride())
                    ((AbstractLoanRates) outstanding.getRates()).setBenchmarkAdjOverride(false);
            }
			outstanding.getRates().save();
			this.saveObject();
		}
	}
	
	public void validateOutstandingTransactionIdentifier() {
		if(outstandingTransactionIdentifier!=null) {
			outstandingTransactionIdentifier.basicValidate();
			setLoanTransactionId(outstandingTransactionIdentifier.getLoanTransactionId());
		}else {
			ExceptionUtility.throwException(new LiqError(Messages.liqNlsExternalizedMessage("Outstanding Transaction Identifier is required."),this));
		}
	}

	public void validateIdentifiers() {
		validateOutstandingTransactionIdentifier();
		//validate each facilityIdentifier and validate primary fee identifier
		if (facilityIdentifiers != null && !facilityIdentifiers.isEmpty()) {
		    facilityIdentifiers.forEach(LiqAPIFacilityIdentifier::basicValidate);
		    validatePrimaryFacilityIdentifier();
		}
		
		//validate each outstandingIdentifier and validate primary outstanding identifier
		if (outstandingIdentifiers != null && !outstandingIdentifiers.isEmpty()) {
		    outstandingIdentifiers.forEach(LiqAPIOutstandingIdentifier::basicValidateUpdate);
		    validatePrimaryOutstandingIdentifier();
		}
		
		//validate each borrowerIdentifier and validate primary customer identifier
		if (borrowerIdentifiers != null && !borrowerIdentifiers.isEmpty()) {
			borrowerIdentifiers.forEach(LiqAPICustomerIdentifier::basicValidate);
			validatePrimaryCustomerIdentifier();
		}		
	}
	
	LiqAPICustomerIdentifier primaryBorrowerIdentifier = null;
	public void validatePrimaryCustomerIdentifier() {
		primaryBorrowerIdentifier = borrowerIdentifiers.stream()
	        .filter(facIde -> facIde.getIdentifierType().equalsIgnoreCase(LiqAPICustomerIdentifier.CustomerIdentifierType.id.name()))
	        .findFirst()
	        .orElseThrow(() -> new LiqError(Messages.liqNlsExternalizedMessage(
	            "In the update loan drawdown transaction for the borrowerIdentifiers, the identifierType id must be passed."), this));

	    if (!(getOutstandingTran().getBorrower().getId().equalsIgnoreCase(primaryBorrowerIdentifier.getIdentifierValue()))) {
	        throw new LiqError(Messages.liqNlsExternalizedMessage(
	            "The borrower identifier for identifierType id does not match the borrower Id of the transaction."), this);
	    }
	}
	
    LiqAPIFacilityIdentifier primaryFacilityIdentifier = null;
	public void validatePrimaryFacilityIdentifier() {
	    primaryFacilityIdentifier = facilityIdentifiers.stream()
	        .filter(facIde -> facIde.getIdentifierType().equalsIgnoreCase(LiqAPIFacilityIdentifier.FacilityIdentifierType.id.name()))
	        .findFirst()
	        .orElseThrow(() -> new LiqError(Messages.liqNlsExternalizedMessage(
	            "In the update loan drawdown transaction for the facilityIdentifiers, the identifierType id must be passed."), this));

	    if (!getOutstandingTran().getFacilityId().equalsIgnoreCase(primaryFacilityIdentifier.getIdentifierValue())) {
	        throw new LiqError(Messages.liqNlsExternalizedMessage(
	            "The facility Identifier for identifierType id does not match the facility Id of the transaction."), this);
	    }
	}
	
	public void validatePrimaryOutstandingIdentifier() {
        LiqAPIOutstandingIdentifier primaryOutstandingIdentifier = outstandingIdentifiers.stream()
            .filter(os -> os.getIdentifierType().equalsIgnoreCase(LiqAPIOutstandingIdentifier.UpdateOutstandingIdentifierType.id.name()))
            .findFirst()
            .orElseThrow(() -> new LiqError(Messages.liqNlsExternalizedMessage(
                "In the update loan drawdown transaction for the outstandingIdentifiers, the identifierType id must be passed."), this));

        if (!getOutstandingTran().getOutstanding().getId().equalsIgnoreCase(primaryOutstandingIdentifier.getIdentifierValue())) {
            throw new LiqError(Messages.liqNlsExternalizedMessage(
                "The Outstanding Identifier for identifierType id does not match the outstanding Id of the transaction."), this);
        }
    }

	@Override
	public Customer getCustomer() {
		Customer result = null;
		if (primaryBorrowerIdentifier == null)
			result = ((LoanTransaction) this.outstandingTran()).getBorrower();
		else {
			result = primaryBorrowerIdentifier.getCustomer();
		}
		return result;
	}
	
	@Override
	public TransactionWithAmortizationBuckets getOutstandingTran() {
		validateOutstandingTransactionIdentifier();
		return outstandingTransactionIdentifier.getOutstandingTran();
	}
	
	@Override
	public void checkCustomerSecurity() {
		customerSecurity(getOutstandingTran().getBorrowerId());
	}

	@Override
	public void checkDealSecurity() {
		dealSecurity(getOutstandingTran().getDealId());
	}

	@Override
	public Object response() {
		Object object = LiqAPILoanDrawdownIntegrationAsReturnValue.clazz.forUpdate((LoanInitialDrawdown)this.outstandingTran);
		this.addIds(List.of(this.outstandingTran));
		return this.outstandingTran == null || !this.outstandingTran.isSaved() ? new String() : null != object ?object:new String();
	}
	
	@Override
	public void addIds(List<LS2UpdateableData> objects) {
		if(null == objects || objects.isEmpty()) {
			return;
		}
		setIds(objects.stream().map(tran -> tran.getId()).collect(Collectors.toList()));
		
	}
	
	@Override
	public void lockAPIData() {
		this.lockLoanDrawdownTransaction();
	}
	
	@Override
	public void unLockAPIData() {
		this.unlockLoanDrawdownTransaction();	
	}

	
	public void basicValidate() {
		validateIdentifiers();
		super.basicValidate();
		validateTimeStamp(this.outstandingTran,this.getMatchUpdatedTimestamp());
		validateRacRate();
		validateSpreadValue();
	}

	public void validateRacRate() {
		if(Objects.nonNull(this.getRacRate()) && this.getRacRate().compareTo(BigDecimal.ONE) > 0) {
			ExceptionUtility.throwException(new LiqError("Any RacRate value more than 1 would be exceeding 100% as per conversion. Hence, value more than 1 is not allowed to input."));
		}
	}

	public void validateSpreadValue() {
		if(Objects.nonNull(this.getSpread()) && this.getSpread().compareTo(BigDecimal.ONE) > 0) {
			ExceptionUtility.throwException(new LiqError("Any spread value more than 1 would be exceeding 100% as per conversion. Hence, value more than 1 is not allowed to input."));
		}		
	}

	@Override
	public void validateAmount() {
		TransactionWithAmortizationBuckets drawdown = getOutstandingTran();
		if(drawdown!=null && (drawdown).isAwaitingSendToRateApproval()) {
			Money reqAmt = Money.clazz.fromAmount(this.getRequestedAmount(), (drawdown).getCurrency());
			if(!drawdown.getRequestedAmount().equals(reqAmt)) {
				ExceptionUtility.throwException(new LiqError( StringUtility.bindWith(Messages.liqNlsExternalizedMessage("The status of initial drawdown transaction %1 is %2. Requested amount cannot be updated."), drawdown.getId(), drawdown.objectStateDescription()),this));
			}
		}
		super.validateAmount();
	}

	public void validateFacility() {
		if (this.getFacility() != null) {
			setRequiredParamsForSOAP();
			this.validatePurpose();
			this.validatePricing();
			this.validateEffectiveDate();
			if (this.customer() != null)
				this.valildateFacilityBorrower();
		}
		
	}
	
	@Override
	public void validateCofRate() {
		if (!this.isMatchFunded().booleanValue() && this.cofRate() != null)
			ExceptionUtility.throwException(new LiqError(Messages.apiMessageNumber(29), this));
		if(!this.isMatchFunded() && null!=this.useCOFFormula && this.useCOFFormula) 
		ExceptionUtility.throwException(new LiqError(Messages.liqNlsExternalizedMessage("Use COF Formula does not apply to a non-matchfunded loan."), this));
			
		if(null!=this.useCOFFormula && this.useCOFFormula) {
			if(null == this.getCofPricingFormula() || (null == this.getCofPricingFormula().getSpread() &&  StringUtility.isBlank(this.getCofPricingFormula().getRateCode()))) {
				ExceptionUtility.throwException(new LiqError(Messages.liqNlsExternalizedMessage("Either provide either the rate Code or spread"), this));
			}
		}

		if((null==this.useCOFFormula || !this.useCOFFormula) && null!= this.getCofPricingFormula())
			ExceptionUtility.throwException(new LiqError(Messages.liqNlsExternalizedMessage("COF Pricing Formula is not required when Use COF pricing formula is either passed as N or not passed"), this));
		if (this.cofRate() != null && null!=this.useCOFFormula && this.useCOFFormula)
			ExceptionUtility.throwException(new LiqError(Messages.liqNlsExternalizedMessage("Either provide Cof Rate or Use Cof Formula"), this));
	}
	
	@Override
	public void validateFxRate() {
		if (this.getFxRate() == null) {
			if (this.getUseFacilityCcyFxRate()!=null && this.getUseFacilityCcyFxRate() && this.getUseSpotCcyFxRate().booleanValue())
				ExceptionUtility.throwException(new LiqError(Messages.apiMessageNumber(51), this));
		} else {
			if ((this.getUseFacilityCcyFxRate()!=null && this.getUseFacilityCcyFxRate()) || this.getUseSpotCcyFxRate().booleanValue())
				ExceptionUtility.throwException(new LiqError(Messages.apiMessageNumber(51), this));
		}
	}

	private void setRequiredParamsForSOAP() {
		Customer borrower = getCustomer();
		if(Objects.nonNull(borrower)){
			this.setBorrowerExternalID(borrower.getExternalId());
		}
	}

	public boolean validateFacilityControlNumber() {
			if (this.retrieveFacility() == null) {
				return false;
			} 
			return true;
	}

	@Override
	public void saveOptionalFields() {
		super.saveOptionalFields();
		outstandingTran().baseRateSet();
		this.setInterestRateType();
		this.setRacRate();
	}
	
	@Override
	public void saveOptionalFieldsOnTransaction() {
		LS2UpdateableData tran = this.outstandingTran();
		if (this.getAccrualPeriod() != null)
			tran.setAttributeValue("accrualPeriod",(String) this.getUpdateValueFor(this.getAccrualPeriod()));
		if (this.purpose() != null)
			((LoanStructuringTransaction) tran).setLoanPurpose((String) this.getUpdateValueFor(this.purpose()));
		if (this.getNonAccrual() != null && this.getNonAccrual().booleanValue()) {
			if (CompareUtility.equals(this.getFacility().getPerformingStatus(), AccrualState.clazz.code()))
				((Outstanding) ((LoanTransaction) tran).getOutstanding()).setPerformingStatus(NonAccrualState.clazz.code());
		}
		if (this.repricingDate() != null && !this.getRepricingAnniversaryDateApplies())		
			((KeyedDataObject) tran).setRepricingDate((LiqDate) this.getUpdateValueFor(this.repricingDate()));
	}

	@Override
	public void updateSublimit() {
		if(Objects.nonNull(this.outstandingTran)) {
			String previousSublimitValue = StringUtils.isNotBlank(this.outstandingTran.getSublimitName()) ? this.outstandingTran.getSublimitName() : null;
			super.updateSublimit();
			if(null!=this.getSublimitName()) {
				String sublimitName = this.getSublimitName().isEmpty() ? null : this.getSublimitName();
				this.outstandingTran.sublimitChangeFrom(previousSublimitValue ,sublimitName);
			}
		}
	}

	@Override
	public void updateInterestScheduleForRepricingDates() {
		if(LiqMNInterestScheduleForRepricingDateForOutstandingModelEnhancement.clazz.isLicensed()){
			if (this.interestScheduleForRepricingDates != null){
				LiqMNFacilityInterestPricingOption option = ((LoanStructuringTransaction) this.outstandingTran()).getOutstanding().getFacility()
						.getInterestPricingOptionWithOptionCode(((LoanStructuringTransaction) this.outstandingTran()).getOutstanding().getPricingOption());
				if(option !=null && option.getInterestScheduleForRepricingDates()) {
					((LoanStructuringTransaction) this.outstandingTran()).setInterestScheduleForRepricingDates(this.getInterestScheduleForRepricingDates());
					((Loan)((LoanStructuringTransaction) this.outstandingTran()).getOutstanding()).validateInterestScheduleForRepricingDates();
				}else if(((LoanStructuringTransaction)this.outstandingTran()).getInterestScheduleForRepricingDates()!=this.getInterestScheduleForRepricingDates())
				{
					ExceptionUtility.throwException(new LiqError(
							Messages.liqNlsExternalizedMessage(
									"InterestScheduleForRepricingDates cannot be updated when \'interest schedule for repricing dates false at facility interest pricing option.\'"),
							this));
				}
			}
		}
	}
	
	@Override
	public Boolean isBorrowerIsFloating() {
		return this.getInterestRateIsFloating();
	}

	@Override
	public BigDecimal getBaseRate() {
		Object passedInValue = ReflectionUtility.call(this, "baseRate");
		if (this.interestRateIsFloating != null 
				&& this.interestRateIsFloating 
				&& !this.getOutstandingTran().isBorrowerRateFloating()
				&& passedInValue == null
				&& this.outstandingTran().getBaseRate() != null) {
					((AbstractLoanRates) this.outstandingTran().getRates()).zz_baseRate(null);

					if(Boolean.FALSE.equals(((LoanStructuringTransaction) this.outstandingTran()).autoRateFixIndicator())) {
						((LoanStructuringTransaction) this.outstandingTran()).autoRateFixIndicator(true);
						this.outstandingTran().recordAutoRateFixChangeEvent();
					}

					return null;
		} else if (this.getInterestRateIsFloating() && passedInValue == null ) {
			return null;
		} else
			return super.getBaseRate();
	}

	@Override
	public void validateBaseRate() {
		if (this.getInterestRateIsFloating() != null) {
			boolean isBorrowerRatesFloats = CompareUtility.equals(LiqObject.objGetAttributeValue(this.pricingOptionCodeTableRow(), "floatingRateInd"), "Y");
			boolean isBorrowerRatesFloatsOverridable = CompareUtility.equals(LiqObject.objGetAttributeValue(this.pricingOptionCodeTableRow(), "floatingRateOverrideInd"), "Y");

			if (Boolean.FALSE.equals(isBorrowerRatesFloats)
					&& Boolean.FALSE.equals(isBorrowerRatesFloatsOverridable)
					&& Boolean.TRUE.equals(this.getInterestRateIsFloating())) {
						ExceptionUtility.throwException(new LiqError(Messages.liqNlsExternalizedMessage("Interest rate type cannot be overriden as per the pricing option configuration"), this));
			} else if (Boolean.FALSE.equals(isBorrowerRatesFloats)
					&& Boolean.TRUE.equals(isBorrowerRatesFloatsOverridable)
					&& Boolean.TRUE.equals(this.getInterestRateIsFloating())
					&& this.getBaseRate() != null) {
						ExceptionUtility.throwException(new LiqError(Messages.apiMessageNumber(28), this));
			} else if (Boolean.TRUE.equals(isBorrowerRatesFloats)
					&& Boolean.FALSE.equals(isBorrowerRatesFloatsOverridable)
					&& Boolean.TRUE.equals(this.getInterestRateIsFloating())
					&& this.getBaseRate() != null) {
						ExceptionUtility.throwException(new LiqError(Messages.apiMessageNumber(28), this));
			} else if (Boolean.TRUE.equals(isBorrowerRatesFloats)
					&& Boolean.FALSE.equals(isBorrowerRatesFloatsOverridable)
					&& Boolean.FALSE.equals(this.getInterestRateIsFloating())) {
						ExceptionUtility.throwException(new LiqError(Messages.liqNlsExternalizedMessage("Interest rate type cannot be overriden as per the pricing option configuration"), this));
			} else if (Boolean.TRUE.equals(isBorrowerRatesFloats)
					&& Boolean.TRUE.equals(isBorrowerRatesFloatsOverridable)
					&& Boolean.TRUE.equals(this.getInterestRateIsFloating())
					&& this.getBaseRate() != null) {
						ExceptionUtility.throwException(new LiqError(Messages.apiMessageNumber(28), this));
			}
			} else {
				super.validateBaseRate();
			}
	}

	public void setInterestRateType() {
		if (this.getInterestRateIsFloating() != null) {
			((Outstanding) this.outstandingTran().getOutstanding())
					.zz_isBorrowerRateFloating(
							this.getInterestRateIsFloating());
		}
	}

	public String securityAccessSymbol() {
		return "UpdateLoanDrawdownIntegration";
	}

	@Override
	public void updateMISCodes() {
		List listDelete = null;
		List listAdd = null;
		List<MISCode> payLoadLst = new ArrayList<MISCode>();
		if (!this.getMisCodes().isEmpty()) {
			List deleteList = new ArrayList();
			List addList = new ArrayList();
			List updateList = new ArrayList();
			List outstandingTranMISCodes = this.getOutstandingTran().getMisCodes();
			for (Object obj : this.getMisCodes()) {
				LiqAPIMISCode misCodeElem = (LiqAPIMISCode) obj;
				MISCode misCode = (MISCode) MISCode.clazz.newStObject();
				misCode.type = misCodeElem.getType();
				payLoadLst.add(misCode);
			}
			MISCodeComparator comparator = new MISCodeComparator();
			listDelete = DataUtils.removeAll(outstandingTranMISCodes, payLoadLst,
					comparator);
			for (Object obj : this.getMisCodes()) {
				LiqAPIMISCode misCodeElement = (LiqAPIMISCode) obj;
				if (!StringUtility.isNilOrBlank(misCodeElement.getDeleteIndicator()) && misCodeElement.getDeleteIndicator().booleanValue()) {
					for (Object obj1 : outstandingTranMISCodes) {
						MISCode ostTranMisCode = (MISCode) obj1;
						if (misCodeElement.getType().equals(ostTranMisCode.getType())) {
							deleteList.add(ostTranMisCode);
						}
					}
				} else {
					boolean updated = false;
					List list = (List) CollectionUtility.copy(outstandingTranMISCodes);
					for (Object obj2 : list) {
						MISCode misCode = (MISCode) obj2;
						if (misCodeElement.getType()
								.equals(misCode.getType())) {
								if (misCode.isTypeOfAmount()) {
									BigDecimal r1 = new BigDecimal(misCodeElement.getValue());
									misCode.setValueAmount(r1);
								}
								if (misCode.isTypeOfDate()) {
									misCode.setValueDate((LiqDate) LiqDate.clazz.fromStringOrAsIs(misCodeElement.getValue()));
								}
								if (misCode.isTypeOfValue()) {
									misCode.zz_value(misCodeElement.getValue());
								}
								if (misCode.isTypeOfMultiLineText()) {
									misCode.valueMultiLineText(misCodeElement.getValue());
								}
							misCode.save();
							updateList.add(misCode);
							updated = true;
						}
					}
					if (!updated) {
						addList.add(misCodeElement);
					}
				}
			}
			deleteList = DataUtils.addAll(deleteList, listDelete, comparator);
			if (!deleteList.isEmpty())
				this.outstandingTran().removeAllFromCollection(deleteList);
			if (!updateList.isEmpty())
				this.outstandingTran().updateCollectionWithAll(updateList);
			if (!addList.isEmpty()) {
				this.createOutstandingTranMISCodesFor(addList);
			}
		}
	}
	
	 @Override
	public Facility retrieveFacility() {
		if(primaryFacilityIdentifier!=null && primaryFacilityIdentifier.getFacility()!=null) {
			return this.facility = primaryFacilityIdentifier.getFacility();
		}else {
			if (this.outstandingTran() == null) {
		        return null;
		    } else {
		        return this.outstandingTran().getFacility();
		    }
		}
		}

	
	public void createOutstandingTranMISCodesFor(List addList) {
		if(!addList.isEmpty()) {
			Iterator iterator = CollectionUtility.iterator(addList);
			while (iterator.hasNext()) {
				LiqAPIMISCode mc = (LiqAPIMISCode) iterator.next();
				ProductMISCode t = ProductMISCode.clazz.createFor(this.getOutstandingTran());
				t.type(mc.getType());
				if (t.isFreeFormCode()) {
					t.setValueByMISCodeValueTypeWith(mc.getValue());
				} else {
					t.setValueType(mc.getValueType());
				}
				this.getOutstandingTran().updateCollectionWith(t);
			}
		}
	}

	public static class Class extends LiqAPIUpdateLoanDrawdown.Class implements StClass {
		protected Class() {
		}

		public StObject basicNew() {
			return new LiqAPIUpdateLoanDrawdownIntegration();
		}

		public java.lang.Class getJavaClass() {
			return LiqAPIUpdateLoanDrawdownIntegration.class;
		}

		public StClass getStSuperclass() {
			return LiqAPIUpdateLoanDrawdown.clazz;
		}
		
		public String securityAccessSymbol() {
			return "UpdateLoanDrawdownIntegration";
		}
		
		public List nonPrimitiveFieldCollectionMappings() {
			List mappings = super.nonPrimitiveFieldCollectionMappings();
			
			LiqAPINonPrimitiveFieldMapping t1 = (LiqAPINonPrimitiveFieldMapping) LiqAPINonPrimitiveFieldMapping.clazz.newStObject();
			t1.setFieldName("facilityIdentifiers");
			t1.setFieldApiClass(LiqAPIFacilityIdentifier.clazz);
			mappings.add(t1);
			
			LiqAPINonPrimitiveFieldMapping t2 = (LiqAPINonPrimitiveFieldMapping) LiqAPINonPrimitiveFieldMapping.clazz.newStObject();
			t2.setFieldName("outstandingIdentifiers");
			t2.setFieldApiClass(LiqAPIOutstandingIdentifier.clazz);
			mappings.add(t2);
			
			LiqAPINonPrimitiveFieldMapping t3 = (LiqAPINonPrimitiveFieldMapping) LiqAPINonPrimitiveFieldMapping.clazz.newStObject();
			t3.setFieldName("borrowerIdentifiers");
			t3.setFieldApiClass(LiqAPICustomerIdentifier.clazz);
			mappings.add(t3);
			
			return mappings;
		}
		
		public List nonPrimitiveFieldMappings() {
			List mappings = super.nonPrimitiveFieldMappings();
			LiqAPINonPrimitiveFieldMapping t4 = (LiqAPINonPrimitiveFieldMapping) LiqAPINonPrimitiveFieldMapping.clazz.newStObject();
			t4.setFieldName("outstandingTransactionIdentifier");
			t4.setFieldApiClass(LiqAPIOutstandingTransactionIdentifier.clazz);
			mappings.add(t4);
			
			return mappings;
		}

		
		public List primitiveFieldMappings() {
			List<LiqAPIPrimitiveFieldMapping> list = super.primitiveFieldMappings();
			LiqAPIViewPrimitiveFieldMapping systemSourceIdFieldMapping = 
					(LiqAPIViewPrimitiveFieldMapping) LiqAPIData.clazz.createViewMapping("systemSourceId", "sourceSystemID", false);
			list.add(systemSourceIdFieldMapping);

			LiqAPIViewPrimitiveFieldMapping sourceRefNumFieldMapping = 
					(LiqAPIViewPrimitiveFieldMapping) LiqAPIData.clazz.createViewMapping("sourceRefNum", "sourceRefNum", false);
			list.add(sourceRefNumFieldMapping);
			list.removeIf(e -> e.getFieldName().equals("facilityControlNumber"));
			list.removeIf(e -> e.getFieldName().equals("alias"));
			list.removeIf(e -> e.getFieldName().equals("borrowerExternalID"));
			list.removeIf(e -> e.getFieldName().equals("loanTransactionId"));
			
			list.removeIf(e -> e.getFieldName().equals("spread"));
			
			LiqAPIViewPrimitiveFieldMapping spreadMapping =
					(LiqAPIViewPrimitiveFieldMapping) LiqAPIData.clazz.createViewMapping("spread", "spreadIntegration", false);
			list.add(spreadMapping);
			
			LiqAPIViewPrimitiveFieldMapping t1  = (LiqAPIViewPrimitiveFieldMapping) LiqAPIViewPrimitiveFieldMapping.clazz.newStObject();
			t1.setFieldName("interestRateIsFloating");
			t1.setLogicalFieldName("boolean");
			t1.setIsRequired(false);
			list.add(t1);
			
			LiqAPIViewPrimitiveFieldMapping t2  = (LiqAPIViewPrimitiveFieldMapping) LiqAPIViewPrimitiveFieldMapping.clazz.newStObject();
			t2.setFieldName("repricingFrequencyApplies");
			t2.setLogicalFieldName("boolean");
			t2.setIsRequired(false);
			list.add(t2);

			LiqAPIViewPrimitiveFieldMapping t3  = (LiqAPIViewPrimitiveFieldMapping) LiqAPIViewPrimitiveFieldMapping.clazz.newStObject();
			t3.setFieldName("racRate");
			t3.setLogicalFieldName("baseRate");
			t3.setIsRequired(false);
			list.add(t3);
			
			return list;
		}
		
		public String securityFunctionParent() {
			return "Loan";
		}
		
		public Boolean supportsAdditionalFields() {
			return false;
		}
		
		public List<LiqAPIReturnData> getReturnType() {
			List<LiqAPIReturnData> list = new ArrayList<LiqAPIReturnData>();
			LiqAPIReturnData liqAPIReturnData = LiqAPIReturnData.getInstance(LiqAPILoanDrawdownIntegrationAsReturnValue.clazz, false);
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

## 4. Pattern A — Cashflow Message Update (OutgoingACHMessage)

**Source:** `LoanIQ/srcgen/com/misys/liq/api/rest/executable/cashflow/LiqAPIUpdateOutgoingACHMessageIntegration.java`

```java
package com.misys.liq.api.rest.executable.cashflow;

import com.misys.liq.Messages;
import com.misys.liq.api.data.LiqAPIReturnData;
import com.misys.liq.api.data.LiqAPIViewPrimitiveFieldMapping;
import com.misys.liq.api.executable.LiqAPIExecutableData;
import com.misys.liq.api.rest.constants.ErrorMessageConstants;
import com.misys.liq.api.rest.data.cashflow.LiqAPIUpdateOutgoingACHMessageIntegrationAsReturnValue;
import com.misys.liq.bm.accttran.Cashflow;
import com.misys.liq.bm.main.cdt.remittance.LiqMNOutgoingAutomatedClearingHouse;
import com.misys.liq.infrastructure.exceptions.LiqError;
import com.sxsy.smtj.StClass;
import com.sxsy.smtj.StClassRegistry;
import com.sxsy.smtj.StObject;
import com.sxsy.smtj.exceptions.ExceptionUtility;
import com.sxsy.smtj.utilities.CollectionUtility;
import com.sxsy.smtj.utilities.StringUtility;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import com.misys.liq.api.constants.APICommonConstants;

import static com.misys.liq.api.constants.APICommonConstants.SECURITY_ACCESS_SYMBOL_CASHFLOW;
import static com.misys.liq.api.constants.APICommonConstants.SECURITY_ACCESS_SYMBOL_UPDATE_OUTGOING_ACH_MESSAGE;

/**
 * This class represents the integration logic for updating outgoing Automated Clearing House (ACH) messages.
 * It extends `LiqAPIExecutableData` and implements `StObject` to provide the necessary functionality for
 * validating, executing, and managing outgoing ACH messages.
 */
public class LiqAPIUpdateOutgoingACHMessageIntegration extends LiqAPIExecutableData implements StObject {
	public static final Class clazz = new Class();

	static {
		StClassRegistry.register(clazz);
	}

	public StClass getStClass() {
		return clazz;
	}

    /**
     * Validates the license for this executable data.
     *
     * @return the current instance of `LiqAPIExecutableData`.
     */
	@Override
    public LiqAPIExecutableData validateLicense() {
        return this;
    }

	@Override
	public boolean isIntegrationAPI() {
		return true;
	}

	public String cashflowId;

	public Cashflow cashflow;

	public String achOutId;

	public LiqMNOutgoingAutomatedClearingHouse outgoingACHMessage;

	public String narrative;

	public String queueStatus;

	public Integer sequenceNumber;

	public Date timestampSent;

	public String getCashflowId() {
		return cashflowId;
	}

	public void setCashflowId(String cashflowId) {
		this.cashflowId = cashflowId;
	}

	public Cashflow getCashflow() {
		return cashflow;
	}

	public void setCashflow(Cashflow cashflow) {
		this.cashflow = cashflow;
	}

	public String getAchOutId() {
		return achOutId;
	}

	public void setAchOutId(String achOutId) {
		this.achOutId = achOutId;
	}

	public LiqMNOutgoingAutomatedClearingHouse getOutgoingACHMessage() {
		return outgoingACHMessage;
	}

	public void setOutgoingACHMessage(LiqMNOutgoingAutomatedClearingHouse outgoingACHMessage) {
		this.outgoingACHMessage = outgoingACHMessage;
	}

	public String getNarrative() {
		return narrative;
	}

	public void setNarrative(String narrative) {
		this.narrative = narrative;
	}

	public String getQueueStatus() {
		return queueStatus;
	}

	public void setQueueStatus(String queueStatus) {
		this.queueStatus = queueStatus;
	}

	public Integer getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(Integer sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public Date getTimestampSent() {
		return timestampSent;
	}

	public void setTimestampSent(LocalDateTime timestampSent) {
		this.timestampSent = Timestamp.valueOf(timestampSent);
	}

    /**
     * Executes the update operation for the outgoing ACH message.
     *
     * @return the result of the update operation.
     */
	public Object basicExecute() {
		this.getOutgoingACHMessage().zz_queueStatus(getQueueStatus());

		if (null != getNarrative()) {
			this.getOutgoingACHMessage().zz_narrative(getNarrative());
		}
		if (null != getSequenceNumber()) {
			this.getOutgoingACHMessage().zz_sequenceNumber(getSequenceNumber());
		}
		if (null != getTimestampSent()) {
			this.getOutgoingACHMessage().zz_timeStampSent(getTimestampSent());
		}
		this.getOutgoingACHMessage().save();
		this.singleCommit();
		return LiqAPIUpdateOutgoingACHMessageIntegrationAsReturnValue.clazz.forUpdate(this.getOutgoingACHMessage());
	}

    /**
     * Validates the input data for the update operation.
     */
	public void basicValidate() {
		validateCashflow();
		validateOutgoingACHMessage();
		validateQueueStatus();
		validateTimeStamp(this.getOutgoingACHMessage().getUpdateTimeStamp(), this.getMatchUpdatedTimestamp());
	}


    /**
     * Validates the cashflow ID and its associated data.
     */
	private void validateCashflow() {
		if (!StringUtility.isNilOrBlank(cashflowId)) {
			this.setCashflow((Cashflow) Cashflow.clazz.getForId(cashflowId));

			if (null == this.getCashflow()) {
				ExceptionUtility.throwException(new LiqError(
						Messages.liqNlsExternalizedMessage(String.format(ErrorMessageConstants.INVALID_CASHFLOW_ID, cashflowId)),
						this));
			}

			if (!this.getCashflow().isReleased()) {
				ExceptionUtility.throwException(new LiqError(
						Messages.liqNlsExternalizedMessage(String.format(ErrorMessageConstants.CASHFLOW_NOT_RELEASED, cashflowId)),
						this));
			}

			if (LiqMNOutgoingAutomatedClearingHouse.clazz.getForCashflowId(cashflowId).isEmpty()) {
				ExceptionUtility.throwException(new LiqError(
						Messages.liqNlsExternalizedMessage(String.format(ErrorMessageConstants.NO_OUTGOING_MESSAGE_ACH, cashflowId)),
						this));
			}
		}
	}

    /**
     * Validates the outgoing ACH message ID and its associated data.
     */
	private void validateOutgoingACHMessage() {
		if (!StringUtility.isNilOrBlank(achOutId)) {
			this.setOutgoingACHMessage((LiqMNOutgoingAutomatedClearingHouse) LiqMNOutgoingAutomatedClearingHouse.clazz.getForId(achOutId));

			if (null == this.getOutgoingACHMessage()) {
				ExceptionUtility.throwException(new LiqError(Messages.liqNlsExternalizedMessage(
						String.format(ErrorMessageConstants.INVALID_OUTGOING_MESSAGE_ACH, achOutId)), this));
			}

			if (!StringUtility.isNilOrBlank(cashflowId) && !this.getCashflowId()
					.equals(this.getOutgoingACHMessage().getCashflowId())) {
				ExceptionUtility.throwException(new LiqError(Messages.liqNlsExternalizedMessage(
						String.format(ErrorMessageConstants.INVALID_OUTGOING_MESSAGE_FOR_CASHFLOW_ACH, achOutId,
								cashflowId)), this));
			}
		}
	}

    /**
     * Validates the queue status of the outgoing ACH message.
     */
	public void validateQueueStatus() {
		if (null != this.getOutgoingACHMessage() && null != this.getQueueStatus()) {
			if (!APICommonConstants.OUTBOUND_MESSAGE_QUEUE_STATUS.contains(this.getQueueStatus()))
				ExceptionUtility.throwException(new LiqError(Messages.liqNlsExternalizedMessage(
						String.format(ErrorMessageConstants.INVALID_QUEUE_STATUS, this.getQueueStatus())), this));
		}
	}

    /**
     * Represents the metadata and schema mapping for this class.
     */
	public static class Class extends LiqAPIExecutableData.Class implements StClass {
		protected Class() {
		}

		public StObject basicNew() {
			return new LiqAPIUpdateOutgoingACHMessageIntegration();
		}

		public List documentedExceptions() {
			List t = super.documentedExceptions();
			return t;
		}

		public java.lang.Class getJavaClass() {
			return LiqAPIUpdateOutgoingACHMessageIntegration.class;
		}

		/**
		 * This method has been added to get whether the result type is mixed while generating schema.
		 *
		 * @return whether the result type is mixed
		 */
		public boolean getResultMixedType() {
			return true;
		}

		public StClass getStSuperclass() {
			return LiqAPIExecutableData.clazz;
		}

		public List primitiveFieldMappings() {
			List<LiqAPIViewPrimitiveFieldMapping> primitiveFieldMappings = super.primitiveFieldMappings();
			primitiveFieldMappings.add(createViewMapping("cashflowId", "id", false));
			primitiveFieldMappings.add(createViewMapping("achOutId", "id", true));

			//narrative maxSize is set to 1750 chars to align with database length.
			LiqAPIViewPrimitiveFieldMapping narrative = (LiqAPIViewPrimitiveFieldMapping) LiqAPIViewPrimitiveFieldMapping.clazz.newStObject();
			narrative.setFieldName("narrative");
			narrative.setLogicalFieldName("description");
			narrative.setMaxSize(1750);
			narrative.setIsRequired(false);
			primitiveFieldMappings.add(narrative);

			primitiveFieldMappings.add(createViewMapping("queueStatus", "description", true));
			primitiveFieldMappings.add(createViewMapping("sequenceNumber", "positiveInteger", false));
			primitiveFieldMappings.add(createViewMapping("timestampSent", "timeStamp", false));

			return primitiveFieldMappings;
		}

		public List<LiqAPIReturnData> getReturnType() {
			return List.of(LiqAPIReturnData.getInstance(LiqAPIUpdateOutgoingACHMessageIntegrationAsReturnValue.clazz, false));
		}

		public List documentedReturnValues() {
			return CollectionUtility.listWith(LiqAPIUpdateOutgoingACHMessageIntegrationAsReturnValue.clazz);
		}

		public String securityFunctionParent() {
			return SECURITY_ACCESS_SYMBOL_CASHFLOW;
		}

		public String securityAccessSymbol() {
			return SECURITY_ACCESS_SYMBOL_UPDATE_OUTGOING_ACH_MESSAGE;
		}

		public boolean isRest() {
			return true;
		}
	}
}
```

---

## 5. Pattern B — Admin User Profile (No Security Checks)

**Source:** `LoanIQ/srcgen/com/misys/liq/api/rest/executable/user/LiqAPIUpdateUserProfileIntegration.java`

```java
package com.misys.liq.api.rest.executable.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;

import com.finastra.liq.api.annotation.LiqAPIFieldMapper;
import com.misys.liq.Messages;
import com.misys.liq.api.constants.APICommonConstants;
import com.misys.liq.api.data.LiqAPINonPrimitiveFieldMapping;
import com.misys.liq.api.data.LiqAPIViewPrimitiveFieldMapping;
import com.misys.liq.api.executable.IAPIRestIntegration;
import com.misys.liq.api.executable.LiqAPIExecutableData;
import com.misys.liq.api.executable.user.LiqAPIAbstractDepartment;
import com.misys.liq.api.executable.user.LiqAPIAbstractRiskBook;
import com.misys.liq.api.executable.user.LiqAPIAbstractSalesProduct;
import com.misys.liq.api.executable.user.LiqAPIAbstractSecondaryProcessingArea;
import com.misys.liq.api.executable.user.LiqAPIAbstractUserDepartment;
import com.misys.liq.api.executable.user.LiqAPIAbstractUserRiskBookAPI;
import com.misys.liq.api.executable.user.LiqAPIAbstractUserSalesProduct;
import com.misys.liq.api.executable.user.LiqAPIAbstractUserSecondaryProcessingArea;
import com.misys.liq.api.executable.user.LiqAPIAbstractUserWorkflowTemplateGroup;
import com.misys.liq.api.executable.user.LiqAPIAbstractWorkflowTemplateGroup;
import com.misys.liq.api.executable.user.LiqAPIUpdateUserProfile;
import com.misys.liq.api.rest.executable.update.helper.Node;
import com.misys.liq.api.rest.executable.update.helper.Node.NodeBuilder;
import com.misys.liq.bm.user.SalesTeam;
import com.misys.liq.bm.user.UserProfile;
import com.misys.liq.bm.user.UserProfileDepartment;
import com.misys.liq.infrastructure.bm.labstrct.LS2UpdateableData;
import com.misys.liq.infrastructure.exceptions.LiqError;
import com.sxsy.smtj.StClass;
import com.sxsy.smtj.StClassRegistry;
import com.sxsy.smtj.StObject;
import com.sxsy.smtj.exceptions.ExceptionUtility;
import com.sxsy.smtj.utilities.StringUtility;

public class LiqAPIUpdateUserProfileIntegration extends LiqAPIUpdateUserProfile implements IAPIRestIntegration {

	@LiqAPIFieldMapper(name = "UserDepartments", className =  "com.misys.liq.api.rest.executable.user.LiqAPIUserDepartmentsIntegration" )
	public List<LiqAPIUserDepartmentsIntegration> userDepartments;

	@LiqAPIFieldMapper(name = "UserWorkflowTemplateGroups", className =  "com.misys.liq.api.rest.executable.user.LiqAPIUserWorkflowTemplateGroupsIntegration" )
	public List<LiqAPIUserWorkflowTemplateGroupsIntegration> userWorkflowTemplateGroups;

	@LiqAPIFieldMapper(name = "UserSecondaryProcessingAreas", className =  "com.misys.liq.api.rest.executable.user.LiqAPIUserSecondaryProcessingAreasIntegration" )
	public List<LiqAPIUserSecondaryProcessingAreasIntegration> userSecondaryProcessingAreas;

	@LiqAPIFieldMapper(name = "UserSalesProducts", className =  "com.misys.liq.api.rest.executable.user.LiqAPIUserSalesProductsIntegration" )
	public List<LiqAPIUserSalesProductsIntegration> userSalesProducts;
	
	@LiqAPIFieldMapper(name = "UserRiskBooks", className =  "com.misys.liq.api.rest.executable.user.LiqAPIUserRiskBooksIntegration" )
	public List<LiqAPIUserRiskBooksIntegration> userRiskBooks;
	
	public boolean riskBooksViewAllInd;	

	public boolean isRiskBooksViewAllInd() {
		return riskBooksViewAllInd;
	}

	public void setRiskBooksViewAllInd(boolean riskBooksViewAllInd) {
		this.riskBooksViewAllInd = riskBooksViewAllInd;
	}

	public List<LiqAPIUserDepartmentsIntegration> getUserDepartments() {
		return userDepartments;
	}

	public void setUserDepartments(
			List<LiqAPIUserDepartmentsIntegration> userDepartments) {
		this.userDepartments = userDepartments;
	}

	public List<LiqAPIUserWorkflowTemplateGroupsIntegration> getUserWorkflowTemplateGroups() {
		return userWorkflowTemplateGroups;
	}

	public void setUserWorkflowTemplateGroups(
			List<LiqAPIUserWorkflowTemplateGroupsIntegration> userWorkflowTemplateGroups) {
		this.userWorkflowTemplateGroups = userWorkflowTemplateGroups;
	}

	public List<LiqAPIUserSecondaryProcessingAreasIntegration> getUserSecondaryProcessingAreas() {
		return userSecondaryProcessingAreas;
	}

	public void setUserSecondaryProcessingAreas(
			List<LiqAPIUserSecondaryProcessingAreasIntegration> userSecondaryProcessingAreas) {
		this.userSecondaryProcessingAreas = userSecondaryProcessingAreas;
	}

	public List<LiqAPIUserSalesProductsIntegration> getUserSalesProducts() {
		return userSalesProducts;
	}

	public void setUserSalesProducts(
			List<LiqAPIUserSalesProductsIntegration> userSalesProducts) {
		this.userSalesProducts = userSalesProducts;
	}

	public List<LiqAPIUserRiskBooksIntegration> getUserRiskBooks() {
		return userRiskBooks;
	}

	public void setUserRiskBooks(
			List<LiqAPIUserRiskBooksIntegration> userRiskBooks) {
		this.userRiskBooks = userRiskBooks;
	}

	public static final Class clazz = new Class();

	static {
		StClassRegistry.register(clazz);
	}

	public StClass getStClass() {
		return clazz;
	}

	public String getStatusCode() {
		return "PEND";
	}

	@Override
	public LiqAPIExecutableData validateLicense() {
		return this;
	}

	@Override
	public boolean isIntegrationAPI() {
		return true;
	}

	public String securityAccessSymbol() {
		return APICommonConstants.SECURITY_ACCESS_SYMBOL_UPDATE_USER_PROFILE;
	} 

	@Override
	public void basicValidate() {
		super.basicValidate();
		this.validateSocialSecurityNo();
		this.validateTimeStamp(this.getUserProfile(), this.getMatchUpdatedTimestamp());
	}

	private void setIdFromLoginId() {
		if (StringUtility.isNilOrBlank(this.getId()) && !StringUtility.isNilOrBlank(this.getLoginId())) {
			UserProfile userProfile = (UserProfile) this.getUserProfileBasedonLoginId(this.getLoginId());
			this.setId(userProfile.getId());
		}
	}

	@Override
	public Object basicExecute() {
		try{
			this.lockAPIData();
			setIdFromLoginId();
			mapUserDepartments();
			mapUserWorkflowTemplateGroups();
			mapUserSecondaryProcessingAreas();
			mapUserSalesProducts();
			mapUserRiskBooks();
			super.basicExecute();
			this.singleCommit();
		}finally {
			this.unLockAPIData();
		}
		return response();
	}
	
	public void mapUserSecondaryProcessingAreas() {
		if (CollectionUtils.isNotEmpty(userSecondaryProcessingAreas)) {
	        List<LiqAPIAbstractUserSecondaryProcessingArea> abstractAreaList = new ArrayList<>();

	        for (LiqAPIUserSecondaryProcessingAreasIntegration integration : userSecondaryProcessingAreas) {
	            if (integration.getUserSecondaryProcessingArea() != null) {
	                List<LiqAPIAbstractSecondaryProcessingArea> abstractSecondaryProcessingAreas = new ArrayList<>();

	                for (LiqAPISecondaryProcessingAreaIntegration secondaryProcessingArea : integration.getUserSecondaryProcessingArea()) {
	                    LiqAPIAbstractSecondaryProcessingArea abstractSecondaryProcessingArea =
	                        (LiqAPIAbstractSecondaryProcessingArea) LiqAPIAbstractSecondaryProcessingArea.clazz.basicNew();

	                    abstractSecondaryProcessingArea.setProcessingAreaCode(secondaryProcessingArea.getProcessingAreaCode());
	                    if(Objects.isNull(secondaryProcessingArea.getDeleteIndicator())) {
	                    	abstractSecondaryProcessingArea.setDeleteIndicator(Boolean.FALSE);
	                    } else {
	                    	abstractSecondaryProcessingArea.setDeleteIndicator(secondaryProcessingArea.getDeleteIndicator());
	                    }
	                    abstractSecondaryProcessingAreas.add(abstractSecondaryProcessingArea);
	                }

	                LiqAPIAbstractUserSecondaryProcessingArea abstractArea =
	                    (LiqAPIAbstractUserSecondaryProcessingArea) LiqAPIAbstractUserSecondaryProcessingArea.clazz.basicNew();
	                abstractArea.setSecondaryProcessingAreaCode(abstractSecondaryProcessingAreas);

	                abstractAreaList.add(abstractArea);
	            }
	        }

	        super.setSecondaryProcessingAreaCodes(abstractAreaList);
	    }
		
	}

	public void mapUserRiskBooks() {
		if (CollectionUtils.isNotEmpty(userRiskBooks)) {
	        List<LiqAPIAbstractUserRiskBookAPI> abstractRiskBookList = new ArrayList<>();

	        for (LiqAPIUserRiskBooksIntegration integration : userRiskBooks) {
	            if (integration.getUserRiskBook() != null) {
	                List<LiqAPIAbstractRiskBook> abstractRiskBooks = new ArrayList<>();

	                for (LiqAPIRiskBookIntegration riskBook : integration.getUserRiskBook()) {
	                    LiqAPIAbstractRiskBook abstractRiskBook =
	                        (LiqAPIAbstractRiskBook) LiqAPIAbstractRiskBook.clazz.basicNew();

	                    abstractRiskBook.setRiskBookCode(riskBook.getRiskBookCode());
	                    abstractRiskBook.setBuySellIndicator(riskBook.getBuySellIndicator());
	                    abstractRiskBook.setMarkIndicator(riskBook.getMarkIndicator());
	                    abstractRiskBook.setViewPricesIndicator(riskBook.getViewPricesIndicator());
	                    abstractRiskBooks.add(abstractRiskBook);
	                }

	                LiqAPIAbstractUserRiskBookAPI abstractArea =
	                    (LiqAPIAbstractUserRiskBookAPI) LiqAPIAbstractUserRiskBookAPI.clazz.basicNew();
	                abstractArea.setRiskBookDetail(abstractRiskBooks);

	                abstractRiskBookList.add(abstractArea);
	            }
	        }

	        super.setRiskBooks(abstractRiskBookList);
	    }
		
	}

	public void mapUserSalesProducts() {
		if (CollectionUtils.isNotEmpty(userSalesProducts)) {
	        List<LiqAPIAbstractUserSalesProduct> abstractSalesProductList = new ArrayList<>();

	        for (LiqAPIUserSalesProductsIntegration integration : userSalesProducts) {
	            if (integration.getUserSalesProduct() != null) {
	                List<LiqAPIAbstractSalesProduct> abstractSalesProducts = new ArrayList<>();

	                for (LiqAPISalesProductIntegration salesProduct : integration.getUserSalesProduct()) {
	                    LiqAPIAbstractSalesProduct abstractSalesProduct =
	                        (LiqAPIAbstractSalesProduct) LiqAPIAbstractSalesProduct.clazz.basicNew();

	                    abstractSalesProduct.setSalesProductName(salesProduct.getSalesProductCode());
	                    abstractSalesProduct.setPrimaryInd(salesProduct.getPrimaryInd());
	                    if(Objects.isNull(salesProduct.getDeleteIndicator())) {
	                    	abstractSalesProduct.setDeleteIndicator(Boolean.FALSE);
	                    } else {
	                    	abstractSalesProduct.setDeleteIndicator(salesProduct.getDeleteIndicator());
	                    }
	                    abstractSalesProducts.add(abstractSalesProduct);
	                }

	                LiqAPIAbstractUserSalesProduct abstractArea =
	                    (LiqAPIAbstractUserSalesProduct) LiqAPIAbstractUserSalesProduct.clazz.basicNew();
	                abstractArea.setSalesProduct(abstractSalesProducts);

	                abstractSalesProductList.add(abstractArea);
	            }
	        }

	        super.setSalesProducts(abstractSalesProductList);
		}
	}

	public void mapUserWorkflowTemplateGroups() {
		if (CollectionUtils.isNotEmpty(userWorkflowTemplateGroups)) {
	        List<LiqAPIAbstractUserWorkflowTemplateGroup> abstractGroupList = new ArrayList<>();

	        for (LiqAPIUserWorkflowTemplateGroupsIntegration integration : userWorkflowTemplateGroups) {
	            if (integration.getUserWorkflowTemplateGroup() != null) {
	                List<LiqAPIAbstractWorkflowTemplateGroup> abstractWorkflowTemplateGroups = new ArrayList<>();

	                for (LiqAPIWorkflowTemplateGroupIntegration workflowTemplateGroup : integration.getUserWorkflowTemplateGroup()) {
	                    LiqAPIAbstractWorkflowTemplateGroup abstractWorkflowTemplateGroup =
	                        (LiqAPIAbstractWorkflowTemplateGroup) LiqAPIAbstractWorkflowTemplateGroup.clazz.basicNew();

	                    abstractWorkflowTemplateGroup.setGroupCode(workflowTemplateGroup.getGroupCode());
	                    if(Objects.isNull(workflowTemplateGroup.getDeleteIndicator())) {
	                    	abstractWorkflowTemplateGroup.setDeleteIndicator(Boolean.FALSE);
	                    } else {
	                    	abstractWorkflowTemplateGroup.setDeleteIndicator(workflowTemplateGroup.getDeleteIndicator());
	                    }
	                    abstractWorkflowTemplateGroups.add(abstractWorkflowTemplateGroup);
	                }

	                LiqAPIAbstractUserWorkflowTemplateGroup abstractArea =
	                    (LiqAPIAbstractUserWorkflowTemplateGroup) LiqAPIAbstractUserWorkflowTemplateGroup.clazz.basicNew();
	                abstractArea.setGroup(abstractWorkflowTemplateGroups);

	                abstractGroupList.add(abstractArea);
	            }
	        }

	        super.setGroups(abstractGroupList);
	    }
		
	}

	public void mapUserDepartments() {
		if (CollectionUtils.isNotEmpty(userDepartments)) {
	        List<LiqAPIAbstractUserDepartment> abstractDepartmentList = new ArrayList<>();

	        for (LiqAPIUserDepartmentsIntegration integration : userDepartments) {
	            if (integration.getUserDepartment() != null) {
	                List<LiqAPIAbstractDepartment> abstractDepartments = new ArrayList<>();

	                for (LiqAPIDepartmentIntegration department : integration.getUserDepartment()) {
	                    LiqAPIAbstractDepartment abstractDepartment =
	                        (LiqAPIAbstractDepartment) LiqAPIAbstractDepartment.clazz.basicNew();

	                    abstractDepartment.setDepartmentName(department.getDepartmentCode());
	                    abstractDepartment.setPrimaryDeptInd(department.getPrimaryDeptInd());
	                    if(Objects.isNull(department.getDeleteIndicator())) {
	                    	abstractDepartment.setDeleteIndicator(Boolean.FALSE);
	                    } else {
	                    	abstractDepartment.setDeleteIndicator(department.getDeleteIndicator());
	                    }
	                    abstractDepartments.add(abstractDepartment);
	                }

	                LiqAPIAbstractUserDepartment abstractArea =
	                    (LiqAPIAbstractUserDepartment) LiqAPIAbstractUserDepartment.clazz.basicNew();
	                abstractArea.setDepartment(abstractDepartments);

	                abstractDepartmentList.add(abstractArea);
	            }
	        }

	        super.setDepartments(abstractDepartmentList);
		}
		
	}
	
	@Override
	public void addSalesTeamCode() {
		this.validateSalesTeamCode();
		this.userProfile.setAttributeValue("salesTeamCode", this.getSalesTeamCode());
	}
	
	@Override
	public void addSalesGroup() {
		if(this.userProfile.isDistribution() || this.userProfile.isSalesperson()) {
			this.validateSalesGroup();
			this.userProfile.setAttributeValue("salesGroup", this.getSalesGroup());
		}
	}
	
	public void validateSalesTeamCode() {
		if(StringUtility.isNilOrBlank(this.getSalesTeamCode())) {
			ExceptionUtility.throwException(new LiqError(Messages.liqNlsExternalizedMessage("You must specify a sales team for a salesperson."), this));
		}
		
		SalesTeam salesTeam = SalesTeam.clazz.getForCode(this.getSalesTeamCode());
		
		if(Objects.isNull(salesTeam)) {
			ExceptionUtility.throwException(new LiqError(StringUtility.bindWith(Messages.liqNlsExternalizedMessage("Sales Team code %1 is invalid."), this.getSalesTeamCode()), this));
		}
		
		if(!Objects.equals(this.getSalesGroup(), salesTeam.salesGroupCode)) {
			ExceptionUtility.throwException(new LiqError(StringUtility.bindWith(Messages.liqNlsExternalizedMessage("Sales Team code %1 does not associate with the given Sales group '%2'"), this.getSalesTeamCode(), this.getSalesGroup()), this));
		}
	}
	
	@Override
	public void validateSalesGroup() {
		if (StringUtility.isNilOrBlank(this.getSalesGroup())) {
			ExceptionUtility.throwException(new LiqError(Messages.liqNlsExternalizedMessage("You must specify a sales group for distribution users."), this));
		}
	}
	
	@Override
	public void setRiskBooksViewAllIndicator() {
		if(!StringUtility.isNilOrBlank(riskBooksViewAllInd)) {
			this.userProfile.setRiskBooksViewAllInd(riskBooksViewAllInd);
		}
	}
	
	@Override
	public void validateDepartmentsForDistributionUser() {
		if(this.userProfile.isDistribution() && !this.userProfile.distributionMayHaveDept()) {
			List<UserProfileDepartment> userDepartments = this.userProfile.getDepartments();
			
			if(userDepartments != null && !userDepartments.isEmpty())
				ExceptionUtility.throwException(new LiqError(StringUtility.bindWith(Messages.liqNlsExternalizedMessage("Departments are not applicable for distribution business area"), this)));
		}
	}

	@Override
	public Object response() {
		Object object = LiqAPIUserProfileIntegrationAsReturnValue.clazz.forUpdate(this.getUserProfile());
		this.addIds(List.of(this.userProfile));
		return this.userProfile == null || !this.userProfile.isSaved() ? new String() : null != object ?object:new String();
	}

	@Override
	public void addIds(List<LS2UpdateableData> objects) {
		if(null == objects || objects.isEmpty()) {
			return;
		}
		setIds(objects.stream().map(tran -> tran.getId()).collect(Collectors.toList()));
	}

	@Override
	public void lockAPIData() {
		super.lockUserProfile();
	}

	@Override
	public void unLockAPIData() {
		super.unlockUserProfile();
	}

	@Override
	public void checkCustomerSecurity() {
		// TODO Auto-generated method stub
	}

	@Override
	public void checkDealSecurity() {
		// TODO Auto-generated method stub
	}
	
	public Boolean isEnabledForUpdateDelete() {
		return Boolean.TRUE;
	}
	
	public Set<String> fetchMandatoryAttributesForQuery() {
		return Set.of("id", "loginId");
	}
	
	public Node updateStructure() {
		
		Node userDepartment = NodeBuilder.getInstance()
				.setAttributeName("userDepartment")
				.addPrimaryKeys("departmentCode")
				.setIsNonPrimitiveCollection(Boolean.TRUE).build();

		Node userDepartments = NodeBuilder.getInstance()
				.setAttributeName("userDepartments")
				.addPrimaryKeys("userDepartment")
				.setIsNonPrimitiveCollection(Boolean.TRUE)
				.addChildren(userDepartment).build();
		
		Node userWorkflowTemplateGroup = NodeBuilder.getInstance()
				.setAttributeName("userWorkflowTemplateGroup")
				.addPrimaryKeys("groupCode")
				.setIsNonPrimitiveCollection(Boolean.TRUE).build();

		Node userWorkflowTemplateGroups = NodeBuilder.getInstance()
				.setAttributeName("userWorkflowTemplateGroups")
				.addPrimaryKeys("userWorkflowTemplateGroup")
				.setIsNonPrimitiveCollection(Boolean.TRUE)
				.addChildren(userWorkflowTemplateGroup).build();

		Node userSecondaryProcessingArea = NodeBuilder.getInstance()
				.setAttributeName("userSecondaryProcessingArea")
				.addPrimaryKeys("processingAreaCode")
				.setIsNonPrimitiveCollection(Boolean.TRUE).build();

		Node userSecondaryProcessingAreas = NodeBuilder.getInstance()
				.setAttributeName("userSecondaryProcessingAreas")
				.addPrimaryKeys("userSecondaryProcessingArea")
				.setIsNonPrimitiveCollection(Boolean.TRUE)
				.addChildren(userSecondaryProcessingArea).build();

		Node userSalesProduct = NodeBuilder.getInstance()
				.setAttributeName("userSalesProduct")
				.addPrimaryKeys("salesProductName")
				.setIsNonPrimitiveCollection(Boolean.TRUE).build();

		Node userSalesProducts = NodeBuilder.getInstance()
				.setAttributeName("userSalesProducts")
				.addPrimaryKeys("userSalesProduct")
				.setIsNonPrimitiveCollection(Boolean.TRUE)
				.addChildren(userSalesProduct).build();

		Node root = NodeBuilder.getInstance()
				.setAttributeName("UpdateUserProfileIntegration")
				.addChildren(userDepartments)
				.addChildren(userWorkflowTemplateGroups)
				.addChildren(userSecondaryProcessingAreas)
				.addChildren(userSalesProducts)
				.build();

		return root;
	}
	
	public Node queryStructure() {
		
		Node userDepartment = NodeBuilder.getInstance()
				.setAttributeName("userDepartment")
				.addPrimaryKeys("departmentCode")
				.setQueryMode(Boolean.TRUE)
				.setUpdatePayloadAssociatedAttribute("userDepartment")
				.setIsNonPrimitiveCollection(Boolean.TRUE)
				.build();

		Node userDepartments = NodeBuilder.getInstance()
				.setAttributeName("userDepartments")
				.addPrimaryKeys("userDepartment")
				.setQueryMode(Boolean.TRUE)
				.setUpdatePayloadAssociatedAttribute("userDepartments")
				.setIsNonPrimitiveCollection(Boolean.TRUE)
				.addChildren(userDepartment)
				.build();

		Node userWorkflowTemplateGroup = NodeBuilder.getInstance()
				.setAttributeName("userWorkflowTemplateGroup")
				.addPrimaryKeys("groupCode")
				.setQueryMode(Boolean.TRUE)
				.setUpdatePayloadAssociatedAttribute("userWorkflowTemplateGroup")
				.setIsNonPrimitiveCollection(Boolean.TRUE)
				.build();

		Node userWorkflowTemplateGroups = NodeBuilder.getInstance()
				.setAttributeName("userWorkflowTemplateGroups")
				.addPrimaryKeys("userWorkflowTemplateGroup")
				.setQueryMode(Boolean.TRUE)
				.setUpdatePayloadAssociatedAttribute("userWorkflowTemplateGroups")
				.setIsNonPrimitiveCollection(Boolean.TRUE)
				.addChildren(userWorkflowTemplateGroup)
				.build();

		Node userSecondaryProcessingArea = NodeBuilder.getInstance()
				.setAttributeName("userSecondaryProcessingArea")
				.addPrimaryKeys("processingAreaCode")
				.setQueryMode(Boolean.TRUE)
				.setUpdatePayloadAssociatedAttribute("userSecondaryProcessingArea")
				.setIsNonPrimitiveCollection(Boolean.TRUE)
				.build();

		Node userSecondaryProcessingAreas = NodeBuilder.getInstance()
				.setAttributeName("userSecondaryProcessingAreas")
				.addPrimaryKeys("userSecondaryProcessingArea")
				.setQueryMode(Boolean.TRUE)
				.setUpdatePayloadAssociatedAttribute("userSecondaryProcessingAreas")
				.setIsNonPrimitiveCollection(Boolean.TRUE)
				.addChildren(userSecondaryProcessingArea)
				.build();

		Node userSalesProduct = NodeBuilder.getInstance()
				.setAttributeName("userSalesProduct")
				.addPrimaryKeys("salesProductName")
				.setQueryMode(Boolean.TRUE)
				.setUpdatePayloadAssociatedAttribute("userSalesProduct")
				.setIsNonPrimitiveCollection(Boolean.TRUE)
				.build();

		Node userSalesProducts = NodeBuilder.getInstance()
				.setAttributeName("userSalesProducts")
				.addPrimaryKeys("userSalesProduct")
				.setQueryMode(Boolean.TRUE)
				.setUpdatePayloadAssociatedAttribute("userSalesProducts")
				.setIsNonPrimitiveCollection(Boolean.TRUE)
				.addChildren(userSalesProduct)
				.build();

		Node root = NodeBuilder.getInstance()
				.setAttributeName("UpdateUserProfileIntegration")
				.addChildren(userDepartments)
				.addChildren(userWorkflowTemplateGroups)
				.addChildren(userSecondaryProcessingAreas)
				.addChildren(userSalesProducts)
				.setQueryMode(Boolean.TRUE)
				.build();

		return root;
	}

	public static class Class extends LiqAPIUpdateUserProfile.Class implements StClass {
		protected Class() {
		}

		public StObject basicNew() {
			return new LiqAPIUpdateUserProfileIntegration();
		}

		public java.lang.Class getJavaClass() {
			return LiqAPIUpdateUserProfileIntegration.class;
		}

		public StClass getStSuperclass() {
			return LiqAPIUpdateUserProfile.clazz;
		}

		public List nonPrimitiveFieldCollectionMappings() {
			LiqAPINonPrimitiveFieldMapping t1 = (LiqAPINonPrimitiveFieldMapping) LiqAPINonPrimitiveFieldMapping.clazz.newStObject();
			t1.setFieldName("userDepartments");
			t1.setFieldApiClass(LiqAPIUserDepartmentsIntegration.clazz);
			
			LiqAPINonPrimitiveFieldMapping t2 = (LiqAPINonPrimitiveFieldMapping) LiqAPINonPrimitiveFieldMapping.clazz.newStObject();
			t2.setFieldName("userWorkflowTemplateGroups");
			t2.setFieldApiClass(LiqAPIUserWorkflowTemplateGroupsIntegration.clazz);
			
			LiqAPINonPrimitiveFieldMapping t3 = (LiqAPINonPrimitiveFieldMapping) LiqAPINonPrimitiveFieldMapping.clazz.newStObject();
			t3.setFieldName("userSalesProducts");
			t3.setFieldApiClass(LiqAPIUserSalesProductsIntegration.clazz);
			
			LiqAPINonPrimitiveFieldMapping t4 = (LiqAPINonPrimitiveFieldMapping) LiqAPINonPrimitiveFieldMapping.clazz.newStObject();
			t4.setFieldName("userSecondaryProcessingAreas");
			t4.setFieldApiClass(LiqAPIUserSecondaryProcessingAreasIntegration.clazz);
			
			LiqAPINonPrimitiveFieldMapping t5 = (LiqAPINonPrimitiveFieldMapping) LiqAPINonPrimitiveFieldMapping.clazz.newStObject();
			t5.setFieldName("userRiskBooks");
			t5.setFieldApiClass(LiqAPIUserRiskBooksIntegration.clazz);
			
			List mappings = super.nonPrimitiveFieldCollectionMappings();
			mappings.add(t5);
			mappings.add(t4);
			mappings.add(t3);
			mappings.add(t2);
			mappings.add(t1);
			
			return mappings;
		}

		public List nonPrimitiveFieldMappings() {
			return super.nonPrimitiveFieldMappings();
		}

		public List primitiveFieldMappings() {
			
			LiqAPIViewPrimitiveFieldMapping t1 = (LiqAPIViewPrimitiveFieldMapping) LiqAPIViewPrimitiveFieldMapping.clazz.newStObject();
			t1.setFieldName("branch");
			t1.setLogicalFieldName("branchCode");
			t1.setIsRequired(true);
			LiqAPIViewPrimitiveFieldMapping t2 = (LiqAPIViewPrimitiveFieldMapping) LiqAPIViewPrimitiveFieldMapping.clazz.newStObject();
			t2.setFieldName("jobFunctionCode");
			t2.setLogicalFieldName("jobFunctionCode");
			t2.setIsRequired(true);
			LiqAPIViewPrimitiveFieldMapping t3 = (LiqAPIViewPrimitiveFieldMapping) LiqAPIViewPrimitiveFieldMapping.clazz.newStObject();
			t3.setFieldName("firstName");
			t3.setLogicalFieldName("description");
			t3.setIsRequired(true);
			t3.setMaxSize(20);
			LiqAPIViewPrimitiveFieldMapping t4 = (LiqAPIViewPrimitiveFieldMapping) LiqAPIViewPrimitiveFieldMapping.clazz.newStObject();
			t4.setFieldName("lastName");
			t4.setLogicalFieldName("description");
			t4.setIsRequired(true);
			t4.setMaxSize(20);
			LiqAPIViewPrimitiveFieldMapping t5 = (LiqAPIViewPrimitiveFieldMapping) LiqAPIViewPrimitiveFieldMapping.clazz.newStObject();
			t5.setFieldName("titleCode");
			t5.setLogicalFieldName("titleCode");
			t5.setIsRequired(true);
			LiqAPIViewPrimitiveFieldMapping t6 = (LiqAPIViewPrimitiveFieldMapping) LiqAPIViewPrimitiveFieldMapping.clazz.newStObject();
			t6.setFieldName("processingAreaCode");
			t6.setLogicalFieldName("processingAreaCode");
			t6.setIsRequired(true);
			LiqAPIViewPrimitiveFieldMapping t7 = (LiqAPIViewPrimitiveFieldMapping) LiqAPIViewPrimitiveFieldMapping.clazz.newStObject();
			t7.setFieldName("countryCode");
			t7.setLogicalFieldName("countryCode");
			t7.setIsRequired(true);
			
			List<LiqAPIViewPrimitiveFieldMapping> coll = super.primitiveFieldMappings();
			
			coll.removeIf(e -> List.of("groupAddress").contains(e.getFieldName()));
			
			LiqAPIViewPrimitiveFieldMapping t8 = (LiqAPIViewPrimitiveFieldMapping) LiqAPIViewPrimitiveFieldMapping.clazz.newStObject();
			t8.setFieldName("groupAddress");
			t8.setLogicalFieldName("description");
			t8.setIsRequired(false);
			t8.setMaxSize(256);
			
			coll.add(t8);
			coll.add(t7);
			coll.add(t6);
			coll.add(t5);
			coll.add(t4);
			coll.add(t3);
			coll.add(t2);
			coll.add(t1);
			
			return coll;
		}

		@Override
		public boolean isRest() {
			return true;
		}

	}

}
```
