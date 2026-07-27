package com.finastra.integrationapi.tool.engine;

import com.finastra.integrationapi.tool.model.ExcelCommonData;
import com.finastra.integrationapi.tool.model.ExcelData;
import com.finastra.integrationapi.tool.utility.CommonSwaggerUtility;
import com.finastra.integrationapi.tool.utility.CommonUtility;

import java.util.List;

public enum ResponseSwaggerAPIEngine {

    INSTANCE;

    public String genrateSwaggerAPIClass(List<ExcelData> dataList, ExcelCommonData data, Boolean hasSoap){
        StringBuilder builder = new StringBuilder();
        CommonSwaggerUtility.INSTANCE.insertDisclaimer(builder, data, hasSoap);
        CommonSwaggerUtility.INSTANCE.insertPackageAndImports(builder, data, hasSoap);
        CommonSwaggerUtility.INSTANCE.insertClassHeader(builder, data, hasSoap, true);
        CommonSwaggerUtility.INSTANCE.insertPrimitiveInstanceVariables(builder, dataList, data, hasSoap);
        CommonSwaggerUtility.INSTANCE.insertNonPrimitiveInstanceVariables(builder, dataList, data, hasSoap);
        CommonSwaggerUtility.INSTANCE.insertPrimitiveFields(builder, dataList, data, hasSoap);
        CommonSwaggerUtility.INSTANCE.insertNonPrimitiveFields(builder, dataList, data, hasSoap);
        CommonUtility.INSTANCE.insertFinalCloseBracket(builder, data, hasSoap);
        return builder.toString();
    }
}
