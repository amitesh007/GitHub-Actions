/* This is an Auto Generated code.
 * This contains all the common framework methods.
 * However, developers have to complete the code based on the API requirement.
 * Hence , this class can be modified to have new methods implementation
 * as well as modification of an existing methods.*/

package com.misys.liq.api.rest.data.outstanding.principal;

// TODO : ADD all the imports here


public class LiqAPILoanPrincipalPaymentIntegrationAsReturnValue extends LiqAPIReturnData implements StObject{

	public String Message;

	public String loanTransactionId;

	public LiqDate transactionDate ;

	public String systemSourceId;

	public String transactionDescription;

	public LiqDate updateTimeStamp;

	public String eventComment;

	public String StatusCode;

	public Boolean Success;

	public Boolean suppressBreakfunding;

	public Boolean autoReduceFacility;

	public Boolean success;

	public String sourceRefNum;

	public LiqDate scheduleDate;

	public String loanAlias;

	public String requestedAmount;

	public Boolean preventOnlineDeletionIndicator;

	public LiqDate effectiveDate;

	public String loanId;

	public static final Class clazz = new Class();

	static {
		StClassRegistry.register(clazz);
	}

	public StClass getStClass() {
		return clazz;
	}

	public String getMessage() {
		return Message;
	}

	public void setMessage(String Message) {
		this.Message = Message;
	}

	public String getLoanTransactionId() {
		return loanTransactionId;
	}

	public void setLoanTransactionId(String loanTransactionId) {
		this.loanTransactionId = loanTransactionId;
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

	public String getTransactionDescription() {
		return transactionDescription;
	}

	public void setTransactionDescription(String transactionDescription) {
		this.transactionDescription = transactionDescription;
	}

	public LiqDate getUpdateTimeStamp() {
		return updateTimeStamp;
	}

	public void setUpdateTimeStamp(LiqDate updateTimeStamp) {
		this.updateTimeStamp = updateTimeStamp;
	}

	public String getEventComment() {
		return eventComment;
	}

	public void setEventComment(String eventComment) {
		this.eventComment = eventComment;
	}

	public String getStatusCode() {
		return StatusCode;
	}

	public void setStatusCode(String StatusCode) {
		this.StatusCode = StatusCode;
	}

	public Boolean getSuccess() {
		return Success;
	}

	public void setSuccess(Boolean Success) {
		this.Success = Success;
	}

	public Boolean getSuppressBreakfunding() {
		return suppressBreakfunding;
	}

	public void setSuppressBreakfunding(Boolean suppressBreakfunding) {
		this.suppressBreakfunding = suppressBreakfunding;
	}

	public Boolean getAutoReduceFacility() {
		return autoReduceFacility;
	}

	public void setAutoReduceFacility(Boolean autoReduceFacility) {
		this.autoReduceFacility = autoReduceFacility;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public String getSourceRefNum() {
		return sourceRefNum;
	}

	public void setSourceRefNum(String sourceRefNum) {
		this.sourceRefNum = sourceRefNum;
	}

	public LiqDate getScheduleDate() {
		return scheduleDate;
	}

	public void setScheduleDate(LiqDate scheduleDate) {
		this.scheduleDate = scheduleDate;
	}

	public String getLoanAlias() {
		return loanAlias;
	}

	public void setLoanAlias(String loanAlias) {
		this.loanAlias = loanAlias;
	}

	public String getRequestedAmount() {
		return requestedAmount;
	}

	public void setRequestedAmount(String requestedAmount) {
		this.requestedAmount = requestedAmount;
	}

	public Boolean getPreventOnlineDeletionIndicator() {
		return preventOnlineDeletionIndicator;
	}

	public void setPreventOnlineDeletionIndicator(Boolean preventOnlineDeletionIndicator) {
		this.preventOnlineDeletionIndicator = preventOnlineDeletionIndicator;
	}

	public LiqDate getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(LiqDate effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public String getLoanId() {
		return loanId;
	}

	public void setLoanId(String loanId) {
		this.loanId = loanId;
	}

	 //TODO: Developer will have to implement this method.
	public LiqAPILoanInterestPaymentIntegrationAsReturnValue queryMessage(
		LiqAPILoanPrincipalPaymentIntegrationAsReturnValue t,LiqBusinessObject bo) {
		 //TODO: Developer will have to add the code here. 
		 return null; 
	}

	public static class Class extends LiqAPIReturnData.Class implements StClass {
		protected Class() {
		}

		public StObject basicNew() {
			return new LiqAPILoanPrincipalPaymentIntegrationAsReturnValue();
		}

		public java.lang.Class getJavaClass() {
			return LiqAPILoanPrincipalPaymentIntegrationAsReturnValue.class;
		}

		public StClass getStSuperclass() {
			return LiqAPIReturnData.clazz;
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

		public Object forCreate(LiqBusinessObject newBuisnessObject) {
			Object result = null;
			if (newBuisnessObject == null || !newBuisnessObject.isSaved()) {
				ExceptionUtility.throwException(new LiqError(Messages.liqNlsExternalizedMessage("<TODO: develo[per should write a proper error message here.>"), this));
			}else {
			LiqAPILoanPrincipalPaymentIntegrationAsReturnValuet = (LiqAPILoanPrincipalPaymentIntegrationAsReturnValue)this.newStObject();
				//TODO: Developer should write the code here.
				t.setUpdateTimeStamp(newBuisnessObject.getUpdateTimeStamp());
				result = t;
			}
			return result;
		}

		public Object forUpdate(LiqBusinessObject newBuisnessObject) {
			Object result = null;
			if (newBuisnessObject == null || !newBuisnessObject.isSaved()) {
				ExceptionUtility.throwException(new LiqError(Messages.liqNlsExternalizedMessage("<TODO: develo[per shou;d write a proper error message here.>"), this));
			}else {
				LiqAPILoanPrincipalPaymentIntegrationAsReturnValuet = (LiqAPILoanPrincipalPaymentIntegrationAsReturnValue)this.newStObject();
				//TODO: Developer should write the code here.
				t.setUpdateTimeStamp(newBuisnessObject.getUpdateTimeStamp());
				result = t;
			}
			return result;
		}

		public Object forQuery(List<LiqBusinessObject> boList) {
			List<LiqAPILoanPrincipalPaymentIntegrationAsReturnValue> objects = new ArrayList<>();
			for(LiqBusinessObject bo : boList) {
				if(bo==null) {
					ExceptionUtility.throwException(new LiqError("<TODO: develo[per should write a proper error message here.>"));
				}
				Deal.clazz.passesDealSecurity(bo.getDealId());
				Customer.clazz.passesDepartmentSecurityForCustomerId(bo.getBorrowerId());
				LiqAPILoanPrincipalPaymentIntegrationAsReturnValuet = (LiqAPILoanPrincipalPaymentIntegrationAsReturnValue)this.newStObject();
				objects.add(t.queryMessage(t, tran));
			}
			return objects;
		}

		public Object forDelete() {
			LiqAPILoanPrincipalPaymentIntegrationAsReturnValuet = (LiqAPILoanPrincipalPaymentIntegrationAsReturnValue)this.newStObject();
			t.setUpdateTimeStamp(CalendarUtility.getCurrentUTCTimestamp());
		}

		public Object forCancel(LiqBusinessObject newBuisnessObjec) {
			LiqAPILoanPrincipalPaymentIntegrationAsReturnValuet = (LiqAPILoanPrincipalPaymentIntegrationAsReturnValue)this.newStObject();
			t.setUpdateTimeStamp(newBuisnessObjec.getUpdateTimeStamp());
		}

		public Object forSearch(List<List<String>> searchResult, LiqAPIPagination pagination) {
			List<LiqAPIData> finalList = new ArrayList<>();
			for(List<String> search : searchResult) {
				LiqAPILoanPrincipalPaymentIntegrationAsReturnValuet = (LiqAPILoanPrincipalPaymentIntegrationAsReturnValue)this.newStObject();
				//TODO: Developer should write the code here.
				finalList.add(t);
			}
			finalList.add(pagination(null != searchResult?searchResult.size():0,pagination));
		}

	}

}

