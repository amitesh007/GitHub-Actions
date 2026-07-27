package com.finastra.integrationapi.tool.engine;

import com.finastra.integrationapi.tool.model.ExcelCommonData;
import com.finastra.integrationapi.tool.model.ExcelData;
import com.finastra.integrationapi.tool.utility.CommonTestUtility;
import com.finastra.integrationapi.tool.utility.CommonUtility;

import java.util.List;

public enum DeleteLiqAPITestEngine {
    INSTANCE;

    public String getDeleteLiqAPITestClass(List<ExcelData> dataList, ExcelCommonData data, Boolean hasSoap){
        StringBuilder builder = new StringBuilder();
        CommonUtility.INSTANCE.insertDisclaimer(builder, data, hasSoap);
        CommonUtility.INSTANCE.insertPackageAndImports(builder, data, hasSoap);
        CommonTestUtility.INSTANCE.insertClassHeader(builder, data, hasSoap);
        CommonTestUtility.INSTANCE.insertLogger(builder, data, hasSoap);
        CommonTestUtility.INSTANCE.insertProperties(builder, data, hasSoap);
        CommonTestUtility.INSTANCE.insertInstanceVariable(builder, data, hasSoap);
        CommonTestUtility.INSTANCE.inserTestandOrder(builder, 1);
        CommonTestUtility.INSTANCE.insertTestBasicValidate(builder, data, hasSoap);
        CommonTestUtility.INSTANCE.inserTestandOrder(builder, 2);
        CommonTestUtility.INSTANCE.insertTestBasicExecute(builder, data, hasSoap);
        CommonTestUtility.INSTANCE.inserTestandOrder(builder, 3);
        CommonTestUtility.INSTANCE.insertTestResponse(builder, data, hasSoap);
        CommonTestUtility.INSTANCE.inserTestandOrder(builder, 4);
        CommonTestUtility.INSTANCE.insertTestAddIds(builder, data, hasSoap);
        CommonTestUtility.INSTANCE.inserTestandOrder(builder, 5);
        CommonTestUtility.INSTANCE.insertTestCheckCustomerSecurity(builder, data, hasSoap);
        CommonTestUtility.INSTANCE.inserTestandOrder(builder, 6);
        CommonTestUtility.INSTANCE.insertTestCheckDealSecurity(builder, data, hasSoap);
        CommonTestUtility.INSTANCE.inserTestandOrder(builder, 7);
        CommonTestUtility.INSTANCE.insertTestLockAPIData(builder, data, hasSoap);
        CommonTestUtility.INSTANCE.inserTestandOrder(builder, 8);
        CommonTestUtility.INSTANCE.insertTestUnLockAPIData(builder, data, hasSoap);
        CommonTestUtility.INSTANCE.inserTestandOrder(builder, 9);
        CommonTestUtility.INSTANCE.insertTestBasicNew(builder, data, hasSoap);
        CommonTestUtility.INSTANCE.inserTestandOrder(builder, 10);
        CommonTestUtility.INSTANCE.insertTestJavaClass(builder, data, hasSoap);
        CommonTestUtility.INSTANCE.inserTestandOrder(builder, 11);
        CommonTestUtility.INSTANCE.insertTestStSuperclass(builder, data, hasSoap);
        CommonTestUtility.INSTANCE.inserTestandOrder(builder, 12);
        CommonTestUtility.INSTANCE.insertTestNonPrimitiveFieldCollectionMappings(builder, data, hasSoap);
        CommonTestUtility.INSTANCE.inserTestandOrder(builder, 13);
        CommonTestUtility.INSTANCE.insertTestNonPrimitiveFieldMappings(builder, data, hasSoap);
        CommonTestUtility.INSTANCE.inserTestandOrder(builder, 14);
        CommonTestUtility.INSTANCE.insertTestPrimitiveFieldMappings(builder, data, hasSoap);
        CommonTestUtility.INSTANCE.inserTestandOrder(builder, 15);
        CommonTestUtility.INSTANCE.insertTestIsRest(builder, data, hasSoap);
        builder.append("}\n");
        return builder.toString();
    }
}
