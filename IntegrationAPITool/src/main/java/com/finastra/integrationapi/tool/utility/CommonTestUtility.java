package com.finastra.integrationapi.tool.utility;

import com.finastra.integrationapi.tool.model.ExcelCommonData;

public enum CommonTestUtility {
    INSTANCE;


//    public void insertClassHeaderForTestResponse(StringBuilder builder, ExcelCommonData data, Boolean hasSoap) {
//        builder.append("@TestMethodOrder(MethodOrderer.OrderAnnotation.class)\n");
//        builder.append("public class ").append(data.getResponseClassName()).append("Test extends BaseTestLoanIQ {\n");
//    }

    public void insertClassHeader(StringBuilder builder, ExcelCommonData data, Boolean hasSoap) {
        if(data.getIsResponse()){
            builder.append("@TestMethodOrder(MethodOrderer.OrderAnnotation.class)\n");
            builder.append("public class ").append(data.getResponseClassName()).append("Test extends BaseTestLoanIQ {\n\n");
        }else{
            builder.append("@TestMethodOrder(MethodOrderer.OrderAnnotation.class)\n");
            builder.append("public class ").append("LiqAPI").append(data.getIntegrationApiClassName()).append("Test extends BaseTestLoanIQ{\n\n");
        }

    }

    public void insertLogger(StringBuilder builder, ExcelCommonData data, Boolean hasSoap) {
        if(data.getIsResponse()){
            builder.append("\tprivate static final Logger LOG = org.apache.logging.log4j.LogManager.getLogger(").append(data.getResponseClassName()).append("Test.class);\n\n");
        }else{
            builder.append("\tprivate static final Logger LOG = org.apache.logging.log4j.LogManager.getLogger(LiqAPI").append(data.getIntegrationApiClassName()).append("Test.class);\n\n");
        }

    }

    public void insertInstanceVariable(StringBuilder builder, ExcelCommonData data, Boolean hasSoap) {
        builder.append("\tprivate LiqAPIResponse response;\n\n");
    }

    public void insertProperties( StringBuilder builder, ExcelCommonData data, Boolean hasSoap) {
        builder.append("\t@BeforeEach\n");
        builder.append("\tpublic void setProperties(){\n");
        builder.append("\t\tProperties props = System.getProperties();\n");
        builder.append("\t\tprops.setProperty(\"RestServices\", \"Y\");\n");
        builder.append("\t}\n\n");
    }

    public void inserTestandOrder(StringBuilder builder,int order){
        builder.append("\t@Test\n");
        builder.append("\t@Order(").append(order).append(")\n");
    }

    public void insertTestBasicValidate(StringBuilder builder, ExcelCommonData data, Boolean hasSoap) {
        builder.append("\tpublic void testBasicValidate() {\n");
        builder.append("\t\t//<TODO: Developer should write the code here.>\n");
        builder.append("\t}\n\n");
    }

    public void insertTestBasicExecute(StringBuilder builder, ExcelCommonData data, Boolean hasSoap) {
        builder.append("\tpublic void testBasicExecute() {\n");
        builder.append("\t\t//<TODO: Developer should write the code here.>\n");
        builder.append("\t}\n\n");
    }

    public void insertTestResponse(StringBuilder builder, ExcelCommonData data, Boolean hasSoap) {
        builder.append("\tpublic void testResponse() {\n");
        builder.append("\t\t//<TODO: Developer should write the code here.>\n");
        builder.append("\t}\n\n");
    }

    public void insertTestAddIds(StringBuilder builder, ExcelCommonData data, Boolean hasSoap) {
        builder.append("\tpublic void testAddIds() {\n");
        builder.append("\t\t//<TODO: Developer should write the code here.>\n");
        builder.append("\t}\n\n");
    }

    public void insertTestCreateIdempotency(StringBuilder builder, ExcelCommonData data, Boolean hasSoap) {
        builder.append("\tpublic void insertTestCreateIdempotency() {\n");
        builder.append("\t\t//<TODO: Developer should write the code here.>\n");
        builder.append("\t}\n\n");
    }

    public void insertTestCheckCustomerSecurity(StringBuilder builder, ExcelCommonData data, Boolean hasSoap) {
        builder.append("\tpublic void insertTestCheckCustomerSecurity() {\n");
        builder.append("\t\t//<TODO: Developer should write the code here.>\n");
        builder.append("\t}\n\n");
    }

    public void insertTestCheckDealSecurity(StringBuilder builder, ExcelCommonData data, Boolean hasSoap) {
        builder.append("\tpublic void insertTestCheckDealSecurity() {\n");
        builder.append("\t\t//<TODO: Developer should write the code here.>\n");
        builder.append("\t}\n\n");
    }

    public void insertTestLockAPIData(StringBuilder builder, ExcelCommonData data, Boolean hasSoap) {
        builder.append("\tpublic void insertTestLockAPIData() {\n");
        builder.append("\t\t//<TODO: Developer should write the code here.>\n");
        builder.append("\t}\n\n");
    }

    public void insertTestUnLockAPIData(StringBuilder builder, ExcelCommonData data, Boolean hasSoap) {
        builder.append("\tpublic void insertTestUnLockAPIData() {\n");
        builder.append("\t\t//<TODO: Developer should write the code here.>\n");
        builder.append("\t}\n\n");
    }

    public void insertTestBasicNew(StringBuilder builder, ExcelCommonData data, Boolean hasSoap) {
        builder.append("\tpublic void testBasicNew() {\n");
        builder.append("\t\t//<TODO: Developer should write the code here.>\n");
        builder.append("\t}\n\n");
    }

    public void insertTestJavaClass(StringBuilder builder, ExcelCommonData data, Boolean hasSoap) {
        builder.append("\tpublic void testJavaClass() {\n");
        builder.append("\t\t//<TODO: Developer should write the code here.>\n");
        builder.append("\t}\n\n");
    }

    public void insertTestStSuperclass(StringBuilder builder, ExcelCommonData data, Boolean hasSoap) {
        builder.append("\tpublic void insertTestStSuperclass() {\n");
        builder.append("\t\t//<TODO: Developer should write the code here.>\n");
        builder.append("\t}\n\n");
    }

    public void insertTestNonPrimitiveFieldCollectionMappings(StringBuilder builder, ExcelCommonData data, Boolean hasSoap) {
        builder.append("\tpublic void insertTestNonPrimitiveFieldCollectionMappings() {\n");
        builder.append("\t\t//<TODO: Developer should write the code here.>\n");
        builder.append("\t}\n\n");
    }

    public void insertTestNonPrimitiveFieldMappings(StringBuilder builder, ExcelCommonData data, Boolean hasSoap) {
        builder.append("\tpublic void insertTestNonPrimitiveFieldMappings() {\n");
        builder.append("\t\t//<TODO: Developer should write the code here.>\n");
        builder.append("\t}\n\n");
    }

    public void insertTestPrimitiveFieldMappings(StringBuilder builder, ExcelCommonData data, Boolean hasSoap) {
        builder.append("\tpublic void insertTestPrimitiveFieldMappings() {\n");
        builder.append("\t\t//<TODO: Developer should write the code here.>\n");
        builder.append("\t}\n\n");
    }

    public void insertTestIsRest(StringBuilder builder, ExcelCommonData data, Boolean hasSoap) {
        builder.append("\tpublic void insertTestIsRest() {\n");
        builder.append("\t\t//<TODO: Developer should write the code here.>\n");
        builder.append("\t}\n\n");
    }


}
