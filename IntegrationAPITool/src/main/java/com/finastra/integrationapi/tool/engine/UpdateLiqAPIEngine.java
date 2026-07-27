package com.finastra.integrationapi.tool.engine;

import com.finastra.integrationapi.tool.model.ExcelCommonData;
import com.finastra.integrationapi.tool.model.ExcelData;
import com.finastra.integrationapi.tool.utility.CommonUtility;

import java.util.List;

public enum UpdateLiqAPIEngine {
    INSTANCE;
    public String getUpdateLiqAPIClass(List<ExcelData> updateInput, ExcelCommonData update, Boolean hasSoap){
        StringBuilder builder = new StringBuilder();
        CommonUtility.INSTANCE.insertDisclaimer(builder, update, hasSoap);
        CommonUtility.INSTANCE.insertPackageAndImports(builder, update, hasSoap);
        CommonUtility.INSTANCE.insertClassHeader(builder, update, hasSoap);
        CommonUtility.INSTANCE.insertPrimitiveInstanceVariables(builder, updateInput, update, hasSoap);
        CommonUtility.INSTANCE.insertNonPrimitiveInstanceVariables(builder, updateInput, update, hasSoap);
        CommonUtility.INSTANCE.insertStaticAPICode(builder, update, hasSoap);
        insertStatusCode(builder, update, hasSoap);
        CommonUtility.INSTANCE.insertLicenseCode(builder, update, hasSoap);
        CommonUtility.INSTANCE.insertPrimitiveFields(builder, updateInput, update, hasSoap);
        CommonUtility.INSTANCE.insertNonPrimitiveFields(builder, updateInput, update, hasSoap);
        CommonUtility.INSTANCE.insertBasicValidate(builder, update, hasSoap);
        CommonUtility.INSTANCE.insertBasicExecute(builder, update, hasSoap);
        CommonUtility.INSTANCE.insertResponse(builder, update, hasSoap);
        CommonUtility.INSTANCE.insertAddIds(builder, update, hasSoap);
        CommonUtility.INSTANCE.insertCustomerSecurity(builder, update, hasSoap);
        CommonUtility.INSTANCE.insertDealSecurity(builder, update, hasSoap);
        CommonUtility.INSTANCE.insertLockAPIData(builder, update, hasSoap);
        CommonUtility.INSTANCE.insertUnLockAPIData(builder, update, hasSoap);
        CommonUtility.INSTANCE.insertStaticInnerClassHeader(builder, update, hasSoap);
        CommonUtility.INSTANCE.insertStaticProtectedMethod(builder, update, hasSoap);
        CommonUtility.INSTANCE.insertStaticBasicNewMethod(builder, update, hasSoap);
        CommonUtility.INSTANCE.insertStaticJavaClassMethod(builder, update, hasSoap);
        CommonUtility.INSTANCE.insertStaticSuperClassMethod(builder, update, hasSoap);
        CommonUtility.INSTANCE.insertStaticNonPrimitiveFieldCollectionMappings(builder, updateInput, update, hasSoap);
        CommonUtility.INSTANCE.insertStaticNonPrimitiveFieldMappings(builder, updateInput, update, hasSoap);
        CommonUtility.INSTANCE.insertStaticPrimitiveFieldMappings(builder, updateInput, update, hasSoap);
        CommonUtility.INSTANCE.insertIsRest(builder, update, hasSoap);
        CommonUtility.INSTANCE.insertInnerStaticClassCloseBracket(builder, update, hasSoap);
        CommonUtility.INSTANCE.insertFinalCloseBracket(builder, update, hasSoap);
        //System.out.println(builder.toString());
        return builder.toString();
    }

    public void insertStatusCode(StringBuilder builder, ExcelCommonData data, Boolean hasSoap) {
        builder.append("\tpublic String statusCode;")
                .append("\n\n");
        builder.append("\tpublic String getStatusCode() {\n" +
                "\t\treturn statusCode();\n" +
                "\t}\n\n");
        builder.append("\tpublic void setStatusCode(String statusCode) {\n" +
                "\t\tthis.statusCode = statusCode();\n" +
                "\t}\n\n");
        builder.append("public String statusCode() {\n" +
                "\t\t /*TODO: Developer might need to change this code based on the API used.\n " +
                "\t\tBelow code is just a sample.*/\n" +
                "\t\tif(nonNull(getOutstandingTransactionIdentifier()) && nonNull(getOutstandingTran())) {\n" +
                "\t\t\treturn getOutstandingTran().getObjectStateCode();\n" +
                "\t\t}\n" +
                "\t\treturn null;\n" +
                "\t}\n\n");
    }
}
