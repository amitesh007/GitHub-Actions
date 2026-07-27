package com.finastra.integrationapi.tool.engine;

import com.finastra.integrationapi.tool.model.ExcelCommonData;
import com.finastra.integrationapi.tool.model.ExcelData;
import com.finastra.integrationapi.tool.utility.CommonUtility;

import java.util.List;

public enum DeleteLiqAPIEngine {
    INSTANCE;

    public String getDeleteLiqAPIClass(List<ExcelData> deleteInput, ExcelCommonData delete, Boolean hasSoap){
        StringBuilder builder = new StringBuilder();
        CommonUtility.INSTANCE.insertDisclaimer(builder, delete, hasSoap);
        CommonUtility.INSTANCE.insertPackageAndImports(builder, delete, hasSoap);
        CommonUtility.INSTANCE.insertClassHeader(builder, delete, hasSoap);
        CommonUtility.INSTANCE.insertPrimitiveInstanceVariables(builder, deleteInput, delete, hasSoap);
        CommonUtility.INSTANCE.insertNonPrimitiveInstanceVariables(builder, deleteInput, delete, hasSoap);
        CommonUtility.INSTANCE.insertStaticAPICode(builder, delete, hasSoap);
        CommonUtility.INSTANCE.insertStatusCode(builder, delete, hasSoap);
        CommonUtility.INSTANCE.insertLicenseCode(builder, delete, hasSoap);
        CommonUtility.INSTANCE.insertPrimitiveFields(builder, deleteInput, delete, hasSoap);
        CommonUtility.INSTANCE.insertNonPrimitiveFields(builder, deleteInput, delete, hasSoap);
        CommonUtility.INSTANCE.insertBasicValidate(builder, delete, hasSoap);
        CommonUtility.INSTANCE.insertBasicExecute(builder, delete, hasSoap);
        CommonUtility.INSTANCE.insertResponse(builder, delete, hasSoap);
        CommonUtility.INSTANCE.insertAddIds(builder, delete, hasSoap);
        CommonUtility.INSTANCE.insertCustomerSecurity(builder, delete, hasSoap);
        CommonUtility.INSTANCE.insertDealSecurity(builder, delete, hasSoap);
        CommonUtility.INSTANCE.insertLockAPIData(builder, delete, hasSoap);
        CommonUtility.INSTANCE.insertUnLockAPIData(builder, delete, hasSoap);
        CommonUtility.INSTANCE.insertStaticInnerClassHeader(builder, delete, hasSoap);
        CommonUtility.INSTANCE.insertStaticProtectedMethod(builder, delete, hasSoap);
        CommonUtility.INSTANCE.insertStaticBasicNewMethod(builder, delete, hasSoap);
        CommonUtility.INSTANCE.insertStaticJavaClassMethod(builder, delete, hasSoap);
        CommonUtility.INSTANCE.insertStaticSuperClassMethod(builder, delete, hasSoap);
        CommonUtility.INSTANCE.insertStaticNonPrimitiveFieldCollectionMappings(builder, deleteInput, delete, hasSoap);
        CommonUtility.INSTANCE.insertStaticNonPrimitiveFieldMappings(builder, deleteInput, delete, hasSoap);
        CommonUtility.INSTANCE.insertStaticPrimitiveFieldMappings(builder, deleteInput, delete, hasSoap);
        CommonUtility.INSTANCE.insertIsRest(builder, delete, hasSoap);
        CommonUtility.INSTANCE.insertInnerStaticClassCloseBracket(builder, delete, hasSoap);
        CommonUtility.INSTANCE.insertFinalCloseBracket(builder, delete, hasSoap);
        return builder.toString();
    }
}
