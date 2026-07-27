package com.finastra.integrationapi.tool.engine;

import com.finastra.integrationapi.tool.model.ExcelCommonData;
import com.finastra.integrationapi.tool.model.ExcelData;
import com.finastra.integrationapi.tool.utility.CommonUtility;

import java.util.List;

public enum CreateLiqAPIEngine {
    INSTANCE;

    public String getCreateLiqAPIClass(List<ExcelData> createInput, ExcelCommonData create, Boolean hasSoap){
        StringBuilder builder = new StringBuilder();
        CommonUtility.INSTANCE.insertDisclaimer(builder, create, hasSoap);
        CommonUtility.INSTANCE.insertPackageAndImports(builder, create, hasSoap);
        CommonUtility.INSTANCE.insertClassHeader(builder, create, hasSoap);
        CommonUtility.INSTANCE.insertPrimitiveInstanceVariables(builder, createInput, create, hasSoap);
        CommonUtility.INSTANCE.insertNonPrimitiveInstanceVariables(builder, createInput, create, hasSoap);
        CommonUtility.INSTANCE.insertStaticAPICode(builder, create, hasSoap);
        CommonUtility.INSTANCE.insertStatusCode(builder, create, hasSoap);
        CommonUtility.INSTANCE.insertLicenseCode(builder, create, hasSoap);
        CommonUtility.INSTANCE.insertPrimitiveFields(builder, createInput, create, hasSoap);
        CommonUtility.INSTANCE.insertNonPrimitiveFields(builder, createInput, create, hasSoap);
        CommonUtility.INSTANCE.insertBasicValidate(builder, create, hasSoap);
        CommonUtility.INSTANCE.insertBasicExecute(builder, create, hasSoap);
        CommonUtility.INSTANCE.insertResponse(builder, create, hasSoap);
        CommonUtility.INSTANCE.insertAddIds(builder, create, hasSoap);
        insertIdempotency(builder, create, hasSoap);
        CommonUtility.INSTANCE.insertCustomerSecurity(builder, create, hasSoap);
        CommonUtility.INSTANCE.insertDealSecurity(builder, create, hasSoap);
        CommonUtility.INSTANCE.insertLockAPIData(builder, create, hasSoap);
        CommonUtility.INSTANCE.insertUnLockAPIData(builder, create, hasSoap);
        CommonUtility.INSTANCE.insertStaticInnerClassHeader(builder, create, hasSoap);
        CommonUtility.INSTANCE.insertStaticProtectedMethod(builder, create, hasSoap);
        CommonUtility.INSTANCE.insertStaticBasicNewMethod(builder, create, hasSoap);
        CommonUtility.INSTANCE.insertStaticJavaClassMethod(builder, create, hasSoap);
        CommonUtility.INSTANCE.insertStaticSuperClassMethod(builder, create, hasSoap);
        CommonUtility.INSTANCE.insertStaticNonPrimitiveFieldCollectionMappings(builder, createInput, create, hasSoap);
        CommonUtility.INSTANCE.insertStaticNonPrimitiveFieldMappings(builder, createInput, create, hasSoap);
        CommonUtility.INSTANCE.insertStaticPrimitiveFieldMappings(builder, createInput, create, hasSoap);
        CommonUtility.INSTANCE.insertIsRest(builder, create, hasSoap);
        CommonUtility.INSTANCE.insertInnerStaticClassCloseBracket(builder, create, hasSoap);
        CommonUtility.INSTANCE.insertFinalCloseBracket(builder, create, hasSoap);
        //System.out.println(builder.toString());
        return builder.toString();
    }

    private void insertIdempotency(StringBuilder builder, ExcelCommonData create, Boolean hasSoap) {
        builder.append("\t@Override\n");
        builder.append("\tpublic void createIdempotency() {\n" +
                "\t\tthis.createIdempotency(this.getIdempotencyKey(),this.newLiqBusinessObject);\n" +
                "\t}\n\n");
    }
















}
