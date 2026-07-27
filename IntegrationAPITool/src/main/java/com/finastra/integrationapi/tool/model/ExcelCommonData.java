package com.finastra.integrationapi.tool.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExcelCommonData {
    private String soapClassName;
    private String fileOpPath;
    private String integrationApiClassName;
    private Boolean isPCP;
    private String packageName;
    private String responseClassName;
    private String type;//create,update,query,delete
    private Boolean isResponse;
}
