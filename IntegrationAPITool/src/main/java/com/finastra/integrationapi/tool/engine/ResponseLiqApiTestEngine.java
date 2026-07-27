package com.finastra.integrationapi.tool.engine;

import com.finastra.integrationapi.tool.model.ExcelCommonData;
import com.finastra.integrationapi.tool.model.ExcelData;
import com.finastra.integrationapi.tool.utility.CommonTestUtility;
import com.finastra.integrationapi.tool.utility.CommonUtility;

import java.util.List;

public enum ResponseLiqApiTestEngine {
    INSTANCE;

    public String getResponseLiqAPITestClass(List<ExcelData> dataList, ExcelCommonData data, Boolean hasSoap){
        StringBuilder builder = new StringBuilder();
        CommonUtility.INSTANCE.insertDisclaimer(builder, data, hasSoap);
        CommonUtility.INSTANCE.insertPackageAndImports(builder, data, hasSoap);
        CommonTestUtility.INSTANCE.insertClassHeader(builder, data, hasSoap);
        CommonTestUtility.INSTANCE.insertLogger(builder, data, hasSoap);
        CommonTestUtility.INSTANCE.insertInstanceVariable(builder, data, hasSoap);
        CommonTestUtility.INSTANCE.insertProperties( builder, data, hasSoap);
        CommonTestUtility.INSTANCE.inserTestandOrder( builder, 1);
        insertTestQueryMessage(builder, data, hasSoap);
        CommonTestUtility.INSTANCE.inserTestandOrder( builder, 2);
        CommonTestUtility.INSTANCE.insertTestBasicNew(builder, data, hasSoap);
        CommonTestUtility.INSTANCE.inserTestandOrder( builder, 3);
        CommonTestUtility.INSTANCE.insertTestJavaClass(builder, data, hasSoap);
        CommonTestUtility.INSTANCE.inserTestandOrder( builder, 4);
        CommonTestUtility.INSTANCE.insertTestStSuperclass(builder, data, hasSoap);
        CommonTestUtility.INSTANCE.inserTestandOrder( builder, 5);
        CommonTestUtility.INSTANCE.insertTestNonPrimitiveFieldCollectionMappings(builder, data, hasSoap);
        CommonTestUtility.INSTANCE.inserTestandOrder( builder, 6);
        CommonTestUtility.INSTANCE.insertTestNonPrimitiveFieldMappings(builder, data, hasSoap);
        CommonTestUtility.INSTANCE.inserTestandOrder( builder, 7);
        CommonTestUtility.INSTANCE.insertTestPrimitiveFieldMappings(builder, data, hasSoap);
        CommonTestUtility.INSTANCE.inserTestandOrder( builder, 8);
        CommonTestUtility.INSTANCE.insertTestIsRest(builder, data, hasSoap);
        CommonTestUtility.INSTANCE.inserTestandOrder( builder, 9);
        insertTestForCreate(builder, data, hasSoap);
        CommonTestUtility.INSTANCE.inserTestandOrder( builder, 10);
        insertTestForUpdate(builder, data, hasSoap);
        CommonTestUtility.INSTANCE.inserTestandOrder( builder, 11);
        insertTestForQuery(builder, data, hasSoap);
        CommonTestUtility.INSTANCE.inserTestandOrder( builder, 12);
        insertTestForDelete(builder, data, hasSoap);
        CommonTestUtility.INSTANCE.inserTestandOrder( builder, 13);
        insertTestForCancel(builder, data, hasSoap);
        CommonTestUtility.INSTANCE.inserTestandOrder( builder, 14);
        insertTestForSearch(builder, data, hasSoap);
        builder.append("}\n");
        //System.out.println(builder.toString());
        return builder.toString();
    }

    private void insertTestQueryMessage(StringBuilder builder, ExcelCommonData data, Boolean hasSoap) {
        builder.append("\tpublic void testQueryMessage() {\n");
        builder.append("\t\t//<TODO: Developer should write the code here.>\n");
        builder.append("\t}\n\n");
    }

    public void insertTestForCreate(StringBuilder builder, ExcelCommonData data, Boolean hasSoap) {
        builder.append("\tpublic void insertTestForCreate {\n");
        builder.append("\t\t//<TODO: Developer should write the code here.>\n");
        builder.append("\t}\n\n");
    }

    public void insertTestForUpdate(StringBuilder builder, ExcelCommonData data, Boolean hasSoap) {
        builder.append("\tpublic void insertTestForUpdate {\n");
        builder.append("\t\t//<TODO: Developer should write the code here.>\n");
        builder.append("\t}\n\n");
    }

    public void insertTestForQuery(StringBuilder builder, ExcelCommonData data, Boolean hasSoap) {
        builder.append("\tpublic void insertTestForQuery {\n");
        builder.append("\t\t//<TODO: Developer should write the code here.>\n");
        builder.append("\t}\n\n");
    }

    public void insertTestForDelete(StringBuilder builder, ExcelCommonData data, Boolean hasSoap) {
        builder.append("\tpublic void insertTestForDelete {\n");
        builder.append("\t\t//<TODO: Developer should write the code here.>\n");
        builder.append("\t}\n\n");
    }

    public void insertTestForCancel(StringBuilder builder, ExcelCommonData data, Boolean hasSoap) {
        builder.append("\tpublic void insertTestForCancel {\n");
        builder.append("\t\t//<TODO: Developer should write the code here.>\n");
        builder.append("\t}\n\n");
    }

    public void insertTestForSearch(StringBuilder builder, ExcelCommonData data, Boolean hasSoap) {
        builder.append("\tpublic void insertTestForSearch {\n");
        builder.append("\t\t//<TODO: Developer should write the code here.>\n");
        builder.append("\t}\n\n");
    }
}
