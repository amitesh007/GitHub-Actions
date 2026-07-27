package com.finastra.integrationapi.tool.engine;

import com.finastra.integrationapi.tool.model.ExcelCommonData;
import com.finastra.integrationapi.tool.model.ExcelData;
import com.finastra.integrationapi.tool.utility.CommonUtility;

import java.util.List;

public enum ResponseLiqApiEngine {
    INSTANCE;

    public String getResponseLiqAPIClass(List<ExcelData> dataList, ExcelCommonData data, Boolean hasSoap){
        StringBuilder builder = new StringBuilder();
        CommonUtility.INSTANCE.insertDisclaimer(builder, data, hasSoap);
        CommonUtility.INSTANCE.insertPackageAndImports(builder, data, hasSoap);
        insertClassHeaderForResponse(builder, data, hasSoap);
        CommonUtility.INSTANCE.insertPrimitiveInstanceVariables(builder, dataList, data, hasSoap);
        CommonUtility.INSTANCE.insertNonPrimitiveInstanceVariables(builder, dataList, data, hasSoap);
        CommonUtility.INSTANCE.insertStaticAPICode(builder, data, hasSoap);
        CommonUtility.INSTANCE.insertPrimitiveFields(builder, dataList, data, hasSoap);
        CommonUtility.INSTANCE.insertNonPrimitiveFields(builder, dataList, data, hasSoap);
        insertQueryMessage(builder, data, hasSoap);
        CommonUtility.INSTANCE.insertStaticInnerClassHeader(builder, data, hasSoap);
        CommonUtility.INSTANCE.insertStaticProtectedMethod(builder, data, hasSoap);
        CommonUtility.INSTANCE.insertStaticBasicNewMethod(builder, data, hasSoap);
        CommonUtility.INSTANCE.insertStaticJavaClassMethod(builder, data, hasSoap);
        CommonUtility.INSTANCE.insertStaticSuperClassMethod(builder, data, hasSoap);
        CommonUtility.INSTANCE.insertStaticNonPrimitiveFieldCollectionMappings(builder, dataList, data, hasSoap);
        CommonUtility.INSTANCE.insertStaticNonPrimitiveFieldMappings(builder, dataList, data, hasSoap);
        CommonUtility.INSTANCE.insertStaticPrimitiveFieldMappings(builder, dataList, data, hasSoap);
        CommonUtility.INSTANCE.insertIsRest(builder, data, hasSoap);
        insertForCreate(builder, data, hasSoap);
        insertForUpdate(builder, data, hasSoap);
        insertForQuery(builder, data, hasSoap);
        insertForDelete(builder, data, hasSoap);
        insertForCancel(builder, data, hasSoap);
        insertForSearch(builder, data, hasSoap);
        CommonUtility.INSTANCE.insertInnerStaticClassCloseBracket(builder, data, hasSoap);
        CommonUtility.INSTANCE.insertFinalCloseBracket(builder, data, hasSoap);
        //System.out.println(builder.toString());
        return builder.toString();
    }

    private void insertForSearch(StringBuilder builder, ExcelCommonData data, Boolean hasSoap) {
        builder.append("\t\tpublic Object forSearch(List<List<String>> searchResult, LiqAPIPagination pagination) {\n");
        builder.append("\t\t\tList<LiqAPIData> finalList = new ArrayList<>();\n");
        builder.append("\t\t\tfor(List<String> search : searchResult) {\n");
        builder.append("\t\t\t\t").append(data.getResponseClassName()).append("t = (").append(data.getResponseClassName()).append(")this.newStObject();\n");
        builder.append("\t\t\t\t//TODO: Developer should write the code here.\n");
        builder.append("\t\t\t\tfinalList.add(t);\n");
        builder.append("\t\t\t}\n");
        builder.append("\t\t\tfinalList.add(pagination(null != searchResult?searchResult.size():0,pagination));\n");
        builder.append("\t\t}\n\n");
    }

    private void insertForCancel(StringBuilder builder, ExcelCommonData data, Boolean hasSoap) {
        builder.append("\t\tpublic Object forCancel(LiqBusinessObject newBuisnessObjec) {\n");
        builder.append("\t\t\t").append(data.getResponseClassName()).append("t = (").append(data.getResponseClassName()).append(")this.newStObject();\n");
        builder.append("\t\t\tt.setUpdateTimeStamp(newBuisnessObjec.getUpdateTimeStamp());\n");
        builder.append("\t\t}\n\n");
    }

    private void insertForDelete(StringBuilder builder, ExcelCommonData data, Boolean hasSoap) {
        builder.append("\t\tpublic Object forDelete() {\n");
        builder.append("\t\t\t").append(data.getResponseClassName()).append("t = (").append(data.getResponseClassName()).append(")this.newStObject();\n");
        builder.append("\t\t\tt.setUpdateTimeStamp(CalendarUtility.getCurrentUTCTimestamp());\n");
        builder.append("\t\t}\n\n");
    }

    private void insertForCreate(StringBuilder builder, ExcelCommonData data, Boolean hasSoap) {
        builder.append("\t\tpublic Object forCreate(LiqBusinessObject newBuisnessObject) {\n");
        builder.append("\t\t\tObject result = null;\n");
        builder.append("\t\t\tif (newBuisnessObject == null || !newBuisnessObject.isSaved()) {\n");
        builder.append("\t\t\t\tExceptionUtility.throwException(new LiqError(Messages.liqNlsExternalizedMessage(\"<TODO: develo[per should write a proper error message here.>\"), this));\n");
        builder.append("\t\t\t}else {\n");
        builder.append("\t\t\t").append(data.getResponseClassName()).append("t = (").append(data.getResponseClassName()).append(")this.newStObject();\n");
        builder.append("\t\t\t\t//TODO: Developer should write the code here.\n");
        builder.append("\t\t\t\tt.setUpdateTimeStamp(newBuisnessObject.getUpdateTimeStamp());\n");
        builder.append("\t\t\t\tresult = t;\n");
        builder.append("\t\t\t}\n");
        builder.append("\t\t\treturn result;\n");
        builder.append("\t\t}\n\n");
    }

    private void insertForUpdate(StringBuilder builder, ExcelCommonData data, Boolean hasSoap) {
        builder.append("\t\tpublic Object forUpdate(LiqBusinessObject newBuisnessObject) {\n");
        builder.append("\t\t\tObject result = null;\n");
        builder.append("\t\t\tif (newBuisnessObject == null || !newBuisnessObject.isSaved()) {\n");
        builder.append("\t\t\t\tExceptionUtility.throwException(new LiqError(Messages.liqNlsExternalizedMessage(\"<TODO: develo[per shou;d write a proper error message here.>\"), this));\n");
        builder.append("\t\t\t}else {\n");
        builder.append("\t\t\t\t").append(data.getResponseClassName()).append("t = (").append(data.getResponseClassName()).append(")this.newStObject();\n");
        builder.append("\t\t\t\t//TODO: Developer should write the code here.\n");
        builder.append("\t\t\t\tt.setUpdateTimeStamp(newBuisnessObject.getUpdateTimeStamp());\n");
        builder.append("\t\t\t\tresult = t;\n");
        builder.append("\t\t\t}\n");
        builder.append("\t\t\treturn result;\n");
        builder.append("\t\t}\n\n");
    }

    private void insertForQuery(StringBuilder builder, ExcelCommonData data, Boolean hasSoap) {
        builder.append("\t\tpublic Object forQuery(List<LiqBusinessObject> boList) {\n");
        builder.append("\t\t\tList<").append(data.getResponseClassName()).append("> objects = new ArrayList<>();\n");
        builder.append("\t\t\tfor(LiqBusinessObject bo : boList) {\n");
        builder.append("\t\t\t\tif(bo==null) {\n");
        builder.append("\t\t\t\t\tExceptionUtility.throwException(new LiqError(\"<TODO: develo[per should write a proper error message here.>\"));\n");
        builder.append("\t\t\t\t}\n");
        builder.append("\t\t\t\tDeal.clazz.passesDealSecurity(bo.getDealId());\n");
        builder.append("\t\t\t\tCustomer.clazz.passesDepartmentSecurityForCustomerId(bo.getBorrowerId());\n");
        builder.append("\t\t\t\t").append(data.getResponseClassName()).append("t = (").append(data.getResponseClassName()).append(")this.newStObject();\n");
        builder.append("\t\t\t\tobjects.add(t.queryMessage(t, tran));\n");
        builder.append("\t\t\t}\n");
        builder.append("\t\t\treturn objects;\n");
        builder.append("\t\t}\n\n");
    }

    private void insertClassHeaderForResponse(StringBuilder builder, ExcelCommonData data, Boolean hasSoap) {
        builder.append("public class ").append(data.getResponseClassName()).append(" extends LiqAPIReturnData implements StObject{\n\n");
    }

    private void insertQueryMessage(StringBuilder builder, ExcelCommonData data, Boolean hasSoap) {
        builder.append("\t //TODO: Developer will have to implement this method.\n");
        builder.append("\tpublic LiqAPILoanInterestPaymentIntegrationAsReturnValue queryMessage(\n");
        builder.append("\t\t").append(data.getResponseClassName()).append(" t,");
        builder.append("LiqBusinessObject bo) {").append("\n");
        builder.append("\t\t //TODO: Developer will have to add the code here. \n");
        builder.append("\t\t return null; \n");
        builder.append("\t}\n\n");
    }
}
