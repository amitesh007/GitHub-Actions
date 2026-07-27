package com.finastra.integrationapi.tool.engine;

import com.finastra.integrationapi.tool.model.ExcelCommonData;
import com.finastra.integrationapi.tool.model.ExcelData;
import com.finastra.integrationapi.tool.utility.CommonUtility;

import java.util.List;

public enum QueryLiqAPIEngine {
    INSTANCE;

    public String getQueryLiqAPIClass(List<ExcelData> queryInput, ExcelCommonData query, Boolean hasSoap){
        StringBuilder builder = new StringBuilder();
        CommonUtility.INSTANCE.insertDisclaimer(builder, query, hasSoap);
        CommonUtility.INSTANCE.insertPackageAndImports(builder, query, hasSoap);
        CommonUtility.INSTANCE.insertClassHeader(builder, query, hasSoap);
        CommonUtility.INSTANCE.insertPrimitiveInstanceVariables(builder, queryInput, query, hasSoap);
        CommonUtility.INSTANCE.insertNonPrimitiveInstanceVariables(builder, queryInput, query, hasSoap);
        CommonUtility.INSTANCE.insertStaticAPICode(builder, query, hasSoap);
        CommonUtility.INSTANCE.insertLicenseCode(builder, query, hasSoap);
        CommonUtility.INSTANCE.insertPrimitiveFields(builder, queryInput, query, hasSoap);
        CommonUtility.INSTANCE.insertNonPrimitiveFields(builder, queryInput, query, hasSoap);
        CommonUtility.INSTANCE.insertBasicValidate(builder, query, hasSoap);
        CommonUtility.INSTANCE.insertBasicExecute(builder, query, hasSoap);
        insertGetTransaction(builder, query, hasSoap);
        CommonUtility.INSTANCE.insertStaticInnerClassHeader(builder, query, hasSoap);
        CommonUtility.INSTANCE.insertStaticProtectedMethod(builder, query, hasSoap);
        CommonUtility.INSTANCE.insertStaticBasicNewMethod(builder, query, hasSoap);
        CommonUtility.INSTANCE.insertStaticJavaClassMethod(builder, query, hasSoap);
        CommonUtility.INSTANCE.insertStaticSuperClassMethod(builder, query, hasSoap);
        CommonUtility.INSTANCE.insertStaticNonPrimitiveFieldCollectionMappings(builder, queryInput, query, hasSoap);
        CommonUtility.INSTANCE.insertStaticNonPrimitiveFieldMappings(builder, queryInput, query, hasSoap);
        CommonUtility.INSTANCE.insertStaticPrimitiveFieldMappings(builder, queryInput, query, hasSoap);
        CommonUtility.INSTANCE.insertIsRest(builder, query, hasSoap);
        CommonUtility.INSTANCE.insertInnerStaticClassCloseBracket(builder, query, hasSoap);
        CommonUtility.INSTANCE.insertFinalCloseBracket(builder, query, hasSoap);
        //System.out.println(builder.toString());
        return builder.toString();
    }

    private void insertGetTransaction(StringBuilder builder, ExcelCommonData data, Boolean hasSoap) {
        builder.append("\t@Override\n");
        builder.append("\tprivate List<LiqBusinessObject> getTransaction() {\n");
        builder.append("\t\tList<LiqBusinessObject> transactions = null;\n");
        builder.append("\t\ttry {\n");
        builder.append("\t\t\ttransactions = null; //<TODO: get the transaction from a given ID>\n");
        builder.append("\t\t} catch (NullPointerException e) {\n");
        builder.append("\t\t\tExceptionUtility.throwException(new LiqError(Messages.liqNlsExternalizedMessage(\"<TODO: Throw a valid exeption>\")));\n");
        builder.append("\t\t} catch (Exception ex) {\n");
        builder.append("\t\t\tExceptionUtility.throwException(new LiqError(Messages.liqNlsExternalizedMessage(\"<TODO: Throw a valid exeption>\")));\n");
        builder.append("\t\t}\n");
        builder.append("\t\tif(Objects.nonNull(transactions.get(0))) {\n");
        builder.append("\t\t\treturn loadObjects(transactions);\n");
        builder.append("\t\t}\n");
        builder.append("\t\tsetIds(objects.stream().map(tran -> tran.getId()).collect(Collectors.toList()));\n");
        builder.append("\t\t\treturn transactions;\n");
        builder.append("\t}\n\n");
    }


}
