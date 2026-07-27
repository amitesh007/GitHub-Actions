package com.finastra.integrationapi.tool.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class ExcelData {

    private Double slNo;
    private String className;
    private String attributeCategory;
    private String swaggerAttributeCategory;
    private Boolean isList;
    private String attributeSubCategory1;
    private String swaggerAttributeSubCategory1;
    private Boolean isList1;
    private String attributeSubCategory2;
    private String swaggerAttributeSubCategory2;
    private Boolean isList2;
    private String attributeSubCategory3;
    private String swaggerAttributeSubCategory3;
    private Boolean isList3;
    private String attributeFieldName;
    private String swaggerAttributeFieldName;
    private String dataType;
    private Boolean isRequired;
    private String attributeDescription;
    private String swaggerAttributeDescription;
    private Boolean isUpdatable;
    private Boolean inSoapApi;
    private Integer minSize;
    private Integer maxSize;
    private boolean type;
}
