/* This is an Auto Generated code.
 * This contains all the common framework methods.
 * However, developers have to complete the code based on the API requirement.
 * Hence , this class can be modified to have new methods implementation
 * as well as modification of an existing methods.*/

package com.misys.liq.api.rest.data.outstanding.principal;

// TODO : ADD all the imports here


public class LiqAPIGetLoanPrincipalPaymentIntegration extends LiqAPIExecutableData	implements StObject {

	public String loanTransactionId;

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

	@Override
	public boolean isIntegrationAPI() {
		return true;
	}

	public String securityAccessSymbol() {
		return GetLoanPrincipalPaymentIntegration;
	} 

	public String getLoanTransactionId() {
		return loanTransactionId;
	}

	public void setLoanTransactionId(String loanTransactionId) {
		this.loanTransactionId = loanTransactionId;
	}

	//TODO: Developer might need to add business validations here
	@Override
	public void basicValidate() {
		//TODO: Developer might need to add business validations here
		super.basicValidate();
	}

	@Override
	public Object basicExecute() {
		return LiqAPIAsReturnValue.clazz.forQuery(getTransaction());
	}

	@Override
	private List<LiqBusinessObject> getTransaction() {
		List<LiqBusinessObject> transactions = null;
		try {
			transactions = null; //<TODO: get the transaction from a given ID>
		} catch (NullPointerException e) {
			ExceptionUtility.throwException(new LiqError(Messages.liqNlsExternalizedMessage("<TODO: Throw a valid exeption>")));
		} catch (Exception ex) {
			ExceptionUtility.throwException(new LiqError(Messages.liqNlsExternalizedMessage("<TODO: Throw a valid exeption>")));
		}
		if(Objects.nonNull(transactions.get(0))) {
			return loadObjects(transactions);
		}
		setIds(objects.stream().map(tran -> tran.getId()).collect(Collectors.toList()));
			return transactions;
	}

	public static class Class extends LiqAPILiqAPIExecutableData.Class implements StClass {
		protected Class() {
		}

		public StObject basicNew() {
			return new LiqAPIGetLoanPrincipalPaymentIntegration();
		}

		public java.lang.Class getJavaClass() {
			return LiqAPIGetLoanPrincipalPaymentIntegration.class;
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

