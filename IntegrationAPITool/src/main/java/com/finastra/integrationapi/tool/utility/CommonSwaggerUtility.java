package com.finastra.integrationapi.tool.utility;

import com.finastra.integrationapi.tool.model.ExcelCommonData;
import com.finastra.integrationapi.tool.model.ExcelData;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public enum CommonSwaggerUtility {

    INSTANCE;

    public void insertDisclaimer(StringBuilder builder, ExcelCommonData data, Boolean hasSoap) {
        builder.append("/* This is an auto - generated class. Do not modify this class.*/\n");
        builder.append("\n");
    }

    public void insertPackageAndImports(StringBuilder builder, ExcelCommonData data, Boolean hasSoap) {
        //Insert package name
        builder.append("package ").append(data.getPackageName()).append(";\n\n");
        //Insert all the required Imports
        builder.append("import java.util.*;\n");
        builder.append("import java.math.BigDecimal;\n");
        builder.append("import java.time.LocalDate;\n");
        builder.append("import jakarta.validation.constraints.*;\n");
        builder.append("import io.swagger.v3.oas.annotations.media.Schema;\n");
        builder.append("import lombok.*;\n");
        builder.append("import com.liq.module.common.annotations.*;\n");
        builder.append("import ").append(data.getPackageName()).append(".request.*;\n");
        builder.append("import ").append(data.getPackageName()).append(".common.*;\n");
    }

    public String getModuleName(String integrationApiClassName) {
        // Remove "create", "update", "query", and "integration" (case-insensitive)
        String processed = integrationApiClassName.replaceAll("(?i)create", "")
                .replaceAll("(?i)update", "")
                .replaceAll("(?i)query", "")
                .replaceAll("(?i)integration", "");
        // Convert the remaining string to lowercase
        return processed.toLowerCase();
    }

    public void insertClassHeader(StringBuilder builder, ExcelCommonData data, Boolean hasSoap, Boolean isResponse) {
        //Add Annotations at class level
        if(isResponse){
            builder.append("@Schema(description = \"").append("This is the response model for ").append(data.getType()).append(" ").append(getModuleName(data.getIntegrationApiClassName())).append(".\", title = \"").append(data.getIntegrationApiClassName()).append("\")\n");
        } else {
            builder.append("@Schema(description = \"").append("This is the request model for ").append(data.getType()).append(" ").append(getModuleName(data.getIntegrationApiClassName())).append(".\", title = \"").append(data.getIntegrationApiClassName()).append("\")\n");
        }

        builder.append("@ModelClassMapper(name=\"").append(data.getIntegrationApiClassName()).append("\", className = \"").append(data.getIntegrationApiClassName()).append("\"\n");
        builder.append("@ToString\n");
        builder.append("@Builder\n");
        builder.append("@NoArgsConstructor\n");
        builder.append("@AllArgsConstructor\n");
        //Starting syntax for class
        builder.append("public class ").append(data.getIntegrationApiClassName()).append("{\n");
    }

    public void insertPrimitiveInstanceVariables(StringBuilder builder, List<ExcelData> createInput, ExcelCommonData data, Boolean hasSoap) {
        createInput.stream().filter(e -> (e.getAttributeCategory() == null || e.getAttributeCategory().isEmpty()))
                .forEach(e ->{
                    builder.append("\t@Schema(name=\"").append(e.getAttributeFieldName()).append("\", example = \"${example}\", required =").append(e.getIsRequired()).append(", description = \"").append(e.getSwaggerAttributeDescription()).append("\")\n");
                    builder.append("\t@ModelFieldMapper(name=\"").append(e.getAttributeFieldName()).append("\", type=\"").append(CommonUtility.INSTANCE.returnDataType(e.getDataType())).append("\", isMandatory=").append(e.getIsRequired()).append(")\n");
                    builder.append("\t@JsonProperty(\"").append(e.getSwaggerAttributeFieldName()).append("\")\n");
                    if(e.getDataType().equalsIgnoreCase("LocalDate")
                    || e.getDataType().equalsIgnoreCase("LocalDateTime")) {
                        builder.append("\t@JsonFormat(pattern = \"yyyy-MM-dd\")\n");
                    }
                    if(e.getIsRequired()){
                        builder.append("\t@NotNull\n");
                    }
                    if(e.getMinSize() != null && e.getMaxSize() != null) {
                        builder.append("\t@Size(min = ").append(e.getMinSize()).append(", max= ").append(e.getMaxSize()).append(")\n");
                    }
                    builder.append("\tpublic ").append(e.getDataType()).append(" ")
                            .append(e.getAttributeFieldName()).append(";\n\n");
                });
    }

    public void insertNonPrimitiveInstanceVariables(StringBuilder builder, List<ExcelData> dataList, ExcelCommonData data, Boolean hasSoap) {
        Set<String> attributeCategorySet = new HashSet<>();
        Map<String, ExcelData> map = new ConcurrentHashMap<>();

        // Collect unique attribute categories
        dataList.stream()
                .filter(e -> e.getAttributeCategory() != null && !e.getAttributeCategory().isEmpty())
                .forEach(e -> attributeCategorySet.add(e.getAttributeCategory()));

        // Map attribute categories to a single data record
        for (String attributeCategory : attributeCategorySet) {
            for (ExcelData d : dataList) {
                if (d.getAttributeCategory().equalsIgnoreCase(attributeCategory)) {
                    map.put(attributeCategory, d);
                    break; // Only need one data record for a given attribute category
                }
            }
        }

        // Generate fields for each attribute category
        map.values().stream()
                .forEach(e -> {
                    builder.append("\t@ModelFieldMapper(name=\"")
                            .append(e.getAttributeCategory())
                            .append("\", className=\"")
                            .append(data.getPackageName())
                            .append(".common.")
                            .append(e.getAttributeCategory())
                            .append("\", type=\"")
                            .append(CommonUtility.INSTANCE.isList(dataList, e.getAttributeCategory()) ? "List" : "Object")
                            .append("\", isMandatory=")
                            .append(e.getIsRequired())
                            .append(")\n");

                    builder.append("\t@JsonProperty(\"")
                            .append(e.getSwaggerAttributeCategory().substring(0, 1).toLowerCase())
                            .append(e.getSwaggerAttributeCategory().substring(1))
                            .append("\")\n");

                    if (CommonUtility.INSTANCE.isList(dataList, e.getAttributeCategory())) {
                        builder.append("\tprivate List<")
                                .append(e.getAttributeCategory())
                                .append("> ")
                                .append(e.getAttributeCategory().substring(0, 1).toLowerCase())
                                .append(e.getAttributeCategory().substring(1))
                                .append(";\n\n");
                    } else {
                        builder.append("\tprivate ")
                                .append(e.getAttributeCategory())
                                .append(" ")
                                .append(e.getAttributeCategory().substring(0, 1).toLowerCase())
                                .append(e.getAttributeCategory().substring(1))
                                .append(";\n\n");
                    }
                });
    }

    public void insertPrimitiveFields(StringBuilder builder, List<ExcelData> createInput, ExcelCommonData data, Boolean hasSoap) {
        //Insert primitiveFieldAttributes
        createInput.stream().filter(e -> e.getAttributeCategory() == null || e.getAttributeCategory().isEmpty())
                .forEach(e ->{
                    builder.append("\tpublic ").append(e.getDataType()).append(" get")
                            .append(e.getAttributeFieldName().substring(0,1).toUpperCase())
                            .append(e.getAttributeFieldName().substring(1)).append("() {\n")
                            .append("\t\treturn ").append(e.getAttributeFieldName()).append(";\n")
                            .append("\t}\n\n");
                    builder.append("\tpublic void set")
                            .append(e.getAttributeFieldName().substring(0,1).toUpperCase())
                            .append(e.getAttributeFieldName().substring(1)).append("(")
                            .append(e.getDataType()).append(" ").append(e.getAttributeFieldName()).append(") {\n")
                            .append("\t\tthis.").append(e.getAttributeFieldName()).append(" = ")
                            .append(e.getAttributeFieldName()).append(";\n")
                            .append("\t}\n\n");
                });
    }

    public void insertNonPrimitiveFields(StringBuilder builder, List<ExcelData> dataList, ExcelCommonData data, Boolean hasSoap) {
        Set<String> attributeCategorySet = new HashSet<String>();
        dataList.stream().filter(e -> !e.getInSoapApi() && (e.getAttributeCategory() != null && !e.getAttributeCategory().isEmpty()))
                .forEach(e ->{
                    attributeCategorySet.add(e.getAttributeCategory());
                });

        attributeCategorySet.stream()
                .forEach(attributeCategory ->{
                    if(CommonUtility.INSTANCE.isList(dataList, attributeCategory)){
                        builder.append("\tpublic List<").append(attributeCategory).append("> get")
                                .append(attributeCategory).append("() {\n")
                                .append("\t\treturn ").append(attributeCategory.substring(0,1).toLowerCase()).append(attributeCategory.substring(1)).append(";\n")
                                .append("\t}\n\n");
                        builder.append("\tpublic void set")
                                .append(attributeCategory).append("(")
                                .append("List<").append(attributeCategory).append("> ").append(attributeCategory.substring(0,1).toLowerCase()).append(attributeCategory.substring(1)).append(") {\n")
                                .append("\t\tthis.").append(attributeCategory.substring(0,1).toLowerCase()).append(attributeCategory.substring(1)).append(" = ")
                                .append(attributeCategory.substring(0,1).toLowerCase()).append(attributeCategory.substring(1)).append(";\n")
                                .append("\t}\n\n");
                    }else{
                        builder.append("\tpublic ").append(attributeCategory).append(" get")
                                .append(attributeCategory).append("() {\n")
                                .append("\t\treturn ").append(attributeCategory.substring(0,1).toLowerCase()).append(attributeCategory.substring(1)).append(";\n")
                                .append("\t}\n\n");
                        builder.append("\tpublic void set")
                                .append(attributeCategory).append("(")
                                .append(attributeCategory).append(" ").append(attributeCategory.substring(0,1).toLowerCase()).append(attributeCategory.substring(1)).append(") {\n")
                                .append("\t\tthis.").append(attributeCategory.substring(0,1).toLowerCase()).append(attributeCategory.substring(1)).append(" = ")
                                .append(attributeCategory.substring(0,1).toLowerCase()).append(attributeCategory.substring(1)).append(";\n")
                                .append("\t}\n\n");
                    }


                });
    }

}
