/* This is an Auto Generated code.
 * This contains all the common framework methods.
 * However, developers have to complete the code based on the API requirement.
 * Hence , this class can be modified to have new methods implementation
 * as well as modification of an existing methods.*/

package com.misys.liq.api.rest.data.outstanding.principal;

// TODO : ADD all the imports here


public class LiqAPICreateLoanPrincipalPaymentIntegration extends LiqAPIExecutableData	implements IAPICreateRestIntegration , StObject {

	public String requestedAmount;

	public static final Class clazz = new Class();

	static {
		StClassRegistry.register(clazz);
	}

	public StClass getStClass() {
		return clazz;
	}

	public String statusCode ="PEND";

	public String getStatusCode() {
		return "PEND";
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = "PEND";
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
		return CreateLoanPrincipalPaymentIntegration;
	} 

	public String getRequestedAmount() {
		return requestedAmount;
	}

	public void setRequestedAmount(String requestedAmount) {
		this.requestedAmount = requestedAmount;
	}

	//TODO: Developer might need to add business validations here
	@Override
	public void basicValidate() {
		super.basicValidate();
	}

	@Override
	public Object basicExecute() {
		try{
			checkDealSecurity();
			checkCustomerSecurity();
			this.lockAPIData();
			super.basicExecute();
			createIdempotency();
		}finally {
			this.unLockAPIData();
		}
		return response();
	}

	@Override
	public Object response() {
		Object object = LiqAPILoanPrincipalPaymentIntegrationAsReturnValue.clazz.forCreate(this.newLiqBusinessObject);
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

	@Override
	public void createIdempotency() {
		this.createIdempotency(this.getIdempotencyKey(),this.newLiqBusinessObject);
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
			return new LiqAPICreateLoanPrincipalPaymentIntegration();
		}

		public java.lang.Class getJavaClass() {
			return LiqAPICreateLoanPrincipalPaymentIntegration.class;
		}

		public StClass getStSuperclass() {
			return LiqAPInull.clazz;
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

