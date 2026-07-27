/* This is an Auto Generated code.
 * This contains all the common framework methods.
 * However, developers have to complete the code based on the API requirement.
 * Hence , this class can be modified to have new methods implementation
 * as well as modification of an existing methods.*/

package com.misys.liq.api.rest.data.outstanding.principal;

// TODO : ADD all the imports here


public class LiqAPIUpdateLoanPrincipalPaymentIntegration extends LiqAPIExecutableData	implements IAPIRestIntegration , StObject {

	public String eventComment;

	public Boolean preventOnlineDeletionIndicator;

	public String transactionDescription;

	public String loanAlias;

	public String loanId;

	public Boolean suppressBreakfunding;

	public LiqDate transactionDate ;

	public String systemSourceId;

	public String sourceRefNum;

	public Boolean applyToEarliestItem;

	public LiqDate scheduleDate;

	public Boolean autoReduceFacility;

	public static final Class clazz = new Class();

	static {
		StClassRegistry.register(clazz);
	}

	public StClass getStClass() {
		return clazz;
	}

	public String statusCode;

	public String getStatusCode() {
		return statusCode();
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode();
	}

public String statusCode() {
		 /*TODO: Developer might need to change this code based on the API used.
 		Below code is just a sample.*/
		if(nonNull(getOutstandingTransactionIdentifier()) && nonNull(getOutstandingTran())) {
			return getOutstandingTran().getObjectStateCode();
		}
		return null;
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
		return UpdateLoanPrincipalPaymentIntegration;
	} 

	public String getEventComment() {
		return eventComment;
	}

	public void setEventComment(String eventComment) {
		this.eventComment = eventComment;
	}

	public Boolean getPreventOnlineDeletionIndicator() {
		return preventOnlineDeletionIndicator;
	}

	public void setPreventOnlineDeletionIndicator(Boolean preventOnlineDeletionIndicator) {
		this.preventOnlineDeletionIndicator = preventOnlineDeletionIndicator;
	}

	public String getTransactionDescription() {
		return transactionDescription;
	}

	public void setTransactionDescription(String transactionDescription) {
		this.transactionDescription = transactionDescription;
	}

	public String getLoanAlias() {
		return loanAlias;
	}

	public void setLoanAlias(String loanAlias) {
		this.loanAlias = loanAlias;
	}

	public String getLoanId() {
		return loanId;
	}

	public void setLoanId(String loanId) {
		this.loanId = loanId;
	}

	public Boolean getSuppressBreakfunding() {
		return suppressBreakfunding;
	}

	public void setSuppressBreakfunding(Boolean suppressBreakfunding) {
		this.suppressBreakfunding = suppressBreakfunding;
	}

	public LiqDate getTransactionDate () {
		return transactionDate ;
	}

	public void setTransactionDate (LiqDate transactionDate ) {
		this.transactionDate  = transactionDate ;
	}

	public String getSystemSourceId() {
		return systemSourceId;
	}

	public void setSystemSourceId(String systemSourceId) {
		this.systemSourceId = systemSourceId;
	}

	public String getSourceRefNum() {
		return sourceRefNum;
	}

	public void setSourceRefNum(String sourceRefNum) {
		this.sourceRefNum = sourceRefNum;
	}

	public Boolean getApplyToEarliestItem() {
		return applyToEarliestItem;
	}

	public void setApplyToEarliestItem(Boolean applyToEarliestItem) {
		this.applyToEarliestItem = applyToEarliestItem;
	}

	public LiqDate getScheduleDate() {
		return scheduleDate;
	}

	public void setScheduleDate(LiqDate scheduleDate) {
		this.scheduleDate = scheduleDate;
	}

	public Boolean getAutoReduceFacility() {
		return autoReduceFacility;
	}

	public void setAutoReduceFacility(Boolean autoReduceFacility) {
		this.autoReduceFacility = autoReduceFacility;
	}

	//TODO: Developer might need to add business validations here
	@Override
	public void basicValidate() {
		validateIdentifiers();
		super.basicValidate();
		//TODO: Developer might have to select the correct Transaction for getting the Update Timestamp value
		validateTimeStamp(this.newLiqBusinessObject.getUpdateTimeStamp(),this.getMatchUpdatedTimestamp());
	}

	@Override
	public Object basicExecute() {
		try{
			checkDealSecurity();
			checkCustomerSecurity();
			this.lockAPIData();
			super.basicExecute();
			this.singleCommit();
		}finally {
			this.unLockAPIData();
		}
		return response();
	}

	@Override
	public Object response() {
		Object object = LiqAPILoanPrincipalPaymentIntegrationAsReturnValue.clazz.forUpdate(this.newLiqBusinessObject);
		this.addIds(List.of(this.newLiqBusinessObject));
		return this.newLiqBusinessObject == null || !this.newLiqBusinessObject.isSaved() ? new String() : null != object ?object:new String();
	}

	@Override
	public void addIds(List<LS2UpdateableData> objects) {
		if(null == objects || objects.isEmpty()) {
			return;
		}
		setIds(objects.stream().map(tran -> tran.getId()).collect(Collectors.toList()));
	}

	 //TODO: Developer might have to modify this method to get the borrowerId
	@Override
	public void checkCustomerSecurity() {
		customerSecurity(this.newLiqBusinessObject.getBorrowerId());
	}

	 //TODO: Developer might have to modify this method to get the dealId
	@Override
	public void checkDealSecurity() {
		dealSecurity(this.newLiqBusinessObject.getDealId());
	}

	@Override
	public void lockAPIData() {
		this.lockData();
	}

	@Override
	public void unLockAPIData() {
		this.unlockData();
	}

	public static class Class extends LiqAPILiqAPIExecutableData.Class implements StClass {
		protected Class() {
		}

		public StObject basicNew() {
			return new LiqAPIUpdateLoanPrincipalPaymentIntegration();
		}

		public java.lang.Class getJavaClass() {
			return LiqAPIUpdateLoanPrincipalPaymentIntegration.class;
		}

		public StClass getStSuperclass() {
			return LiqAPILiqAPIUpdateLoanPrincipalPayment.clazz;
		}

		//TODO: Developer might need to add non primitive field collection mappings here
		public List nonPrimitiveFieldCollectionMappings() {
			return super.nonPrimitiveFieldCollectionMappings();
		}

		//TODO: Developer might need to add non primitive field mappings here
		public List nonPrimitiveFieldMappings() {
			return super.nonPrimitiveFieldMappings();
		}

		//TODO: Developer might need to add primitive field mappings here
		public List primitiveFieldMappings() {
			return super.primitiveFieldMappings();
		}

		@Override
		public boolean isRest() {
			return true;
		}

	}

}

