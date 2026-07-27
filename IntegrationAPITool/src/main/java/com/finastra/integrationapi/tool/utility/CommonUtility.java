package com.finastra.integrationapi.tool.utility;

import com.finastra.integrationapi.tool.model.ExcelCommonData;
import com.finastra.integrationapi.tool.model.ExcelData;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public enum CommonUtility {
    INSTANCE;

    public void insertDisclaimer(StringBuilder builder, ExcelCommonData data, Boolean hasSoap) {
        builder.append("/* This is an Auto Generated code.\n");
        builder.append(" * This contains all the common framework methods.\n");
        builder.append(" * However, developers have to complete the code based on the API requirement.\n");
        builder.append(" * Hence , this class can be modified to have new methods implementation\n");
        builder.append(" * as well as modification of an existing methods.*/\n");
        builder.append("\n");
    }

    public void insertPackageAndImports(StringBuilder builder, ExcelCommonData data, Boolean hasSoap) {
        //Insert package name
        builder.append("package ").append(data.getPackageName()).append(";\n\n");
        //Insert all the required Imports
        builder.append("// TODO : ADD all the imports here\n\n\n");
    }

    public void insertClassHeader(StringBuilder builder, ExcelCommonData data, Boolean hasSoap) {
        //Starting syntax for class
        builder.append("public class ").append("LiqAPI").append(data.getIntegrationApiClassName()).append(" extends ");
        insertSuperClass(builder, data, hasSoap);
        builder.append("\timplements");

        if(data.getType().equalsIgnoreCase("create")){
            builder.append(" IAPICreateRestIntegration");
            if(data.getIsPCP()){
                builder.append( " , IAPICreatePCPIntegration");
                builder.append( " , PaperClipInterface");
            }
        }else if(data.getType().equalsIgnoreCase("update")){
            builder.append( " IAPIRestIntegration");
            if(data.getIsPCP()){
                builder.append( " , PaperClipInterface");
            }
        }else if(data.getType().equalsIgnoreCase("delete")){
            builder.append( " IAPIRestIntegration");
            if(data.getIsPCP()){
                builder.append( " , PaperClipInterface");
            }
        }
        if(!hasSoap){
            if(data.getType().equalsIgnoreCase("query")){
                builder.append( " StObject");
            }else{
                builder.append( " , StObject");
            }
        }

        builder.append(" {\n\n");
    }

    public void insertSuperClass(StringBuilder builder, ExcelCommonData data, Boolean hasSoap) {
        if(hasSoap){
            builder.append(data.getSoapClassName());
        }else{
            builder.append("LiqAPIExecutableData");
        }
    }

    public void insertStaticAPICode(StringBuilder builder, ExcelCommonData data, Boolean hasSoap) {
        builder.append("\tpublic static final Class clazz = new Class();\n\n");
        builder.append("\tstatic {\n" +
                "\t\tStClassRegistry.register(clazz);\n" +
                "\t}");
        builder.append("\n\n");
        builder.append("\tpublic StClass getStClass() {\n" +
                "\t\treturn clazz;\n" +
                "\t}");
        builder.append("\n\n");
    }

    public void insertStatusCode(StringBuilder builder, ExcelCommonData data, Boolean hasSoap) {
        builder.append("\tpublic String statusCode =\"PEND\";")
                .append("\n\n");
        builder.append("\tpublic String getStatusCode() {\n" +
                "\t\treturn \"PEND\";\n" +
                "\t}").append("\n\n");
        builder.append("\tpublic void setStatusCode(String statusCode) {\n" +
                "\t\tthis.statusCode = \"PEND\";\n" +
                "\t}").append("\n\n");
    }

    public void insertLicenseCode(StringBuilder builder, ExcelCommonData data, Boolean hasSoap) {
        builder.append("\t@Override\n" +
                "\tpublic LiqAPIExecutableData validateLicense() {\n" +
                "\t\treturn this;\n" +
                "\t}").append("\n\n");

        builder.append("\t@Override\n" +
                "\tpublic boolean isIntegrationAPI() {\n" +
                "\t\treturn true;\n" +
                "\t}").append("\n\n");

        builder.append("\tpublic String securityAccessSymbol() {\n" +
                "\t\treturn ").append(data.getIntegrationApiClassName()).append(";\n\t} \n\n");
    }

    public void insertPrimitiveInstanceVariables(StringBuilder builder, List<ExcelData> createInput, ExcelCommonData data, Boolean hasSoap) {
        createInput.stream().filter(e -> !Boolean.TRUE.equals(e.getInSoapApi()) && (e.getAttributeCategory() == null || e.getAttributeCategory().isEmpty()))
                .forEach(e ->{
                    builder.append("\tpublic ").append(returnDataType(e.getDataType())).append(" ")
                            .append(e.getAttributeFieldName()).append(";\n\n");
                });
    }

    public void insertNonPrimitiveInstanceVariables(StringBuilder builder, List<ExcelData> dataList, ExcelCommonData data, Boolean hasSoap) {
        Set<String> attributeCategorySet = new HashSet<String>();
        dataList.stream().filter(e -> !Boolean.TRUE.equals(e.getInSoapApi()) && (e.getAttributeCategory() != null && !e.getAttributeCategory().isEmpty()))
                .forEach(e ->{
                    attributeCategorySet.add(e.getAttributeCategory());
                });

        attributeCategorySet.stream()
                .forEach(attributeCategory ->{
                    builder.append("\t@LiqAPIFieldMapper(name = \"").append(attributeCategory)
                            .append("\",").append(" className = ")
                            .append(" \"<TODO: developer should add the correct package name of this non primitive class>\" ")
                            .append(")\n");

                    if(isList(dataList, attributeCategory)){
                        builder.append("\tpublic List<").append("LiqAPI").append(attributeCategory).append("> ")
                                .append(attributeCategory.substring(0,1).toLowerCase()).append(attributeCategory.substring(1)).append(";\n\n");
                    }else{
                        builder.append("\tpublic ").append(attributeCategory).append(" ")
                                .append(attributeCategory.substring(0,1).toLowerCase()).append(attributeCategory.substring(1)).append(";\n\n");
                    }


                });
    }

    public void insertPrimitiveFields(StringBuilder builder, List<ExcelData> createInput, ExcelCommonData data, Boolean hasSoap) {
        //Insert primitiveFieldAttributes
        createInput.stream().filter(e -> !Boolean.TRUE.equals(e.getInSoapApi()) && (e.getAttributeCategory() == null || e.getAttributeCategory().isEmpty()))
                .forEach(e ->{
//                    builder.append("\tpublic ").append(returnDataType(e.getDataType())).append(" ")
//                            .append(e.getAttributeFieldName()).append(";\n\n");
                    builder.append("\tpublic ").append(returnDataType(e.getDataType())).append(" get")
                            .append(e.getAttributeFieldName().substring(0,1).toUpperCase())
                            .append(e.getAttributeFieldName().substring(1)).append("() {\n")
                            .append("\t\treturn ").append(e.getAttributeFieldName()).append(";\n")
                            .append("\t}\n\n");
                    builder.append("\tpublic void set")
                            .append(e.getAttributeFieldName().substring(0,1).toUpperCase())
                            .append(e.getAttributeFieldName().substring(1)).append("(")
                            .append(returnDataType(e.getDataType())).append(" ").append(e.getAttributeFieldName()).append(") {\n")
                            .append("\t\tthis.").append(e.getAttributeFieldName()).append(" = ")
                            .append(e.getAttributeFieldName()).append(";\n")
                            .append("\t}\n\n");
                });
    }

    public String returnDataType(String dataType) {
        if (dataType.equalsIgnoreCase("LocalDate")) {
            return "LiqDate";
        }else if (dataType.equalsIgnoreCase("LocalDateTime")) {
            return "Date";
        } else {
            return dataType;
        }
    }

    public void insertNonPrimitiveFields(StringBuilder builder, List<ExcelData> dataList, ExcelCommonData data, Boolean hasSoap) {
        Set<String> attributeCategorySet = new HashSet<String>();
        dataList.stream().filter(e -> !Boolean.TRUE.equals(e.getInSoapApi()) && (e.getAttributeCategory() != null && !e.getAttributeCategory().isEmpty()))
                .forEach(e ->{
                    attributeCategorySet.add(e.getAttributeCategory());
                });

        attributeCategorySet.stream()
                .forEach(attributeCategory ->{
//                    builder.append("\t@LiqAPIFieldMapper(name = \"").append(attributeCategory)
//                            .append("\",").append(" className = ")
//                            .append(" \"<TODO: developer should add the correct package name of this non primitive class>\" ")
//                            .append(")\n");

                    if(isList(dataList, attributeCategory)){
//                        builder.append("\tpublic List<").append("LiqAPI").append(attributeCategory).append("> ")
//                                .append(attributeCategory.substring(0,1).toLowerCase()).append(attributeCategory.substring(1)).append(";\n\n");
                        builder.append("\tpublic List<").append("LiqAPI").append(attributeCategory).append("> get")
                                .append(attributeCategory).append("() {\n")
                                .append("\t\treturn ").append(attributeCategory.substring(0,1).toLowerCase()).append(attributeCategory.substring(1)).append(";\n")
                                .append("\t}\n\n");
                        builder.append("\tpublic void set")
                                .append(attributeCategory).append("(")
                                .append("List<").append("LiqAPI").append(attributeCategory).append("> ").append(attributeCategory.substring(0,1).toLowerCase()).append(attributeCategory.substring(1)).append(") {\n")
                                .append("\t\tthis.").append(attributeCategory.substring(0,1).toLowerCase()).append(attributeCategory.substring(1)).append(" = ")
                                .append(attributeCategory.substring(0,1).toLowerCase()).append(attributeCategory.substring(1)).append(";\n")
                                .append("\t}\n\n");
                    }else{

//                        builder.append("\tpublic ").append(attributeCategory).append(" ")
//                                .append(attributeCategory.substring(0,1).toLowerCase()).append(attributeCategory.substring(1)).append(";\n");
                        builder.append("\tpublic ").append(attributeCategory).append(" get")
                                .append(attributeCategory).append("() {\n")
                                .append("\t\treturn ").append(attributeCategory.substring(0,1).toLowerCase()).append(attributeCategory.substring(1)).append(";\n")
                                .append("\t}\n\n");
                        builder.append("\tpublic void set")
                                .append(attributeCategory).append("(")
                                .append("LiqAPI").append(attributeCategory).append(" ").append(attributeCategory.substring(0,1).toLowerCase()).append(attributeCategory.substring(1)).append(") {\n")
                                .append("\t\tthis.").append(attributeCategory.substring(0,1).toLowerCase()).append(attributeCategory.substring(1)).append(" = ")
                                .append(attributeCategory.substring(0,1).toLowerCase()).append(attributeCategory.substring(1)).append(";\n")
                                .append("\t}\n\n");
                    }


                });
    }

    public boolean isList(List<ExcelData> dataList, String attributeCategory) {
        return dataList.stream().filter(e -> e.getAttributeCategory().equalsIgnoreCase(attributeCategory) && e.getIsList()).count() > 0;
    }

    public void insertFinalCloseBracket(StringBuilder builder, ExcelCommonData data, Boolean hasSoap) {
        builder.append("}\n\n");
    }

    public void insertBasicValidate(StringBuilder builder, ExcelCommonData data, Boolean hasSoap) {
        builder.append("\t//TODO: Developer might need to add business validations here\n");
        builder.append("\t@Override\n");
        builder.append("\tpublic void basicValidate() {\n");
        if(data.getType().equalsIgnoreCase("create")){
            builder.append("\t\tsuper.basicValidate();\n");
        }
        else if(data.getType().equalsIgnoreCase("update")){
            builder.append("\t\tvalidateIdentifiers();\n");
            builder.append("\t\tsuper.basicValidate();\n");
            builder.append("\t\t//TODO: Developer might have to select the correct Transaction for getting the Update Timestamp value\n");
            builder.append("\t\tvalidateTimeStamp(this.newLiqBusinessObject.getUpdateTimeStamp(),this.getMatchUpdatedTimestamp());\n");
        }else if(data.getType().equalsIgnoreCase("query")){
            builder.append("\t\t//TODO: Developer might need to add business validations here\n");
            builder.append("\t\tsuper.basicValidate();\n");
        }else if(data.getType().equalsIgnoreCase("delete")){
            builder.append("\t\tvalidateIdentifiers();\n");
            builder.append("\t\tsuper.basicValidate();\n");
        }
        builder.append("\t}").append("\n\n");
    }

    public void insertBasicExecute(StringBuilder builder, ExcelCommonData data, Boolean hasSoap) {
        builder.append("\t@Override\n");
        builder.append("\tpublic Object basicExecute() {\n");
        if(data.getType().equalsIgnoreCase("query")) {
            builder.append("\t\treturn LiqAPI");
            builder.append(getReturnClassName(data.getIntegrationApiClassName()));
            builder.append("AsReturnValue.clazz.forQuery(getTransaction());\n");
            builder.append("\t}\n\n");
            return;
        }
        builder.append("\t\ttry{\n");
        builder.append("\t\t\tcheckDealSecurity();\n");
        builder.append("\t\t\tcheckCustomerSecurity();\n");
        builder.append("\t\t\tthis.lockAPIData();\n");
        builder.append("\t\t\tsuper.basicExecute();\n");
        if(data.getType().equalsIgnoreCase("create")) {
            builder.append("\t\t\tcreateIdempotency();\n");
        }else if(data.getType().equalsIgnoreCase("update")) {
            builder.append("\t\t\tthis.singleCommit();\n");
        }else if(data.getType().equalsIgnoreCase("delete")) {
            builder.append("\t\t\tthis.singleCommit();\n");
        }
        builder.append("\t\t}finally {\n");
        builder.append("\t\t\tthis.unLockAPIData();\n");
        builder.append("\t\t}\n");
        builder.append("\t\treturn response();");
        builder.append("\n\t}");
        builder.append("\n\n");
    }

    private String getReturnClassName(String integrationApiClassName) {
        String name = "";
        if(integrationApiClassName.toLowerCase().startsWith("create")){
            name = integrationApiClassName.replaceAll("Create","");
        }else if(integrationApiClassName.toLowerCase().startsWith("update")){
            name = integrationApiClassName.replaceAll("Update","");
        }else if(integrationApiClassName.toLowerCase().startsWith("query")) {
            name = integrationApiClassName.replaceAll("Query", "");
        }else if(integrationApiClassName.toLowerCase().startsWith("delete")) {
            name = integrationApiClassName.replaceAll("Delete", "");
        }

        return name;
    }

    public void insertResponse(StringBuilder builder, ExcelCommonData data, Boolean hasSoap) {
        builder.append("\t@Override\n");
        builder.append("\tpublic Object response() {\n");
        builder.append("\t\tObject object = ");
        builder.append("LiqAPI");
        builder.append(getReturnClassName(data.getIntegrationApiClassName()));
        if(data.getType().equalsIgnoreCase("create")) {
            builder.append("AsReturnValue.clazz.forCreate(this.newLiqBusinessObject);\n");
        }else if(data.getType().equalsIgnoreCase("update")) {
            builder.append("AsReturnValue.clazz.forUpdate(this.newLiqBusinessObject);\n");
        }else if(data.getType().equalsIgnoreCase("delete")) {
            builder.append("AsReturnValue.clazz.forDelete(this.newLiqBusinessObject);\n");
        }
        builder.append("\t\tthis.addIds(List.of(this.newLiqBusinessObject));\n");
        builder.append("\t\treturn this.newLiqBusinessObject == null || !this.newLiqBusinessObject.isSaved() ? new String() : null != object ?object:new String();\n");
        builder.append("\t}\n\n");
    }

    public void insertAddIds(StringBuilder builder, ExcelCommonData data, Boolean hasSoap) {
        builder.append("\t@Override\n");
        builder.append("\tpublic void addIds(List<LS2UpdateableData> objects) {\n");
        builder.append("\t\tif(null == objects || objects.isEmpty()) {\n");
        builder.append("\t\t\treturn;\n");
        builder.append("\t\t}\n");
        builder.append("\t\tsetIds(objects.stream().map(tran -> tran.getId()).collect(Collectors.toList()));\n");
        builder.append("\t}\n\n");
    }

    public void insertCustomerSecurity(StringBuilder builder, ExcelCommonData data, Boolean hasSoap) {
        builder.append("\t //TODO: Developer might have to modify this method to get the borrowerId\n");
        builder.append("\t@Override\n");
        builder.append(("\tpublic void checkCustomerSecurity() {\n" +
                "\t\tcustomerSecurity(this.newLiqBusinessObject.getBorrowerId());\n" +
                "\t}\n\n"));
    }

    public void insertDealSecurity(StringBuilder builder, ExcelCommonData data, Boolean hasSoap) {
        builder.append("\t //TODO: Developer might have to modify this method to get the dealId\n");
        builder.append("\t@Override\n");
        builder.append("\tpublic void checkDealSecurity() {\n" +
                "\t\tdealSecurity(this.newLiqBusinessObject.getDealId());\n" +
                "\t}\n\n");
    }

    public void insertLockAPIData(StringBuilder builder, ExcelCommonData data, Boolean hasSoap) {
        builder.append("\t@Override\n");
        builder.append("\tpublic void lockAPIData() {\n" +
                "\t\tthis.lockData();\n" +
                "\t}\n\n");
    }

    public void insertUnLockAPIData(StringBuilder builder, ExcelCommonData data, Boolean hasSoap) {
        builder.append("\t@Override\n");
        builder.append("\tpublic void unLockAPIData() {\n" +
                "\t\tthis.unlockData();\n" +
                "\t}\n\n");
    }

    public void insertStaticInnerClassHeader(StringBuilder builder, ExcelCommonData data, Boolean hasSoap) {
        builder.append("\tpublic static class Class extends");
        if(data.getIsResponse()){
            builder.append(" LiqAPIReturnData.Class implements StClass {\n");
        }else{
            builder.append(" LiqAPI");
            insertSuperClass(builder, data, hasSoap);
            builder.append(".Class implements StClass {\n");
        }
    }

    public void insertStaticProtectedMethod(StringBuilder builder, ExcelCommonData data, Boolean hasSoap) {
        builder.append("\t\tprotected Class() {\n" +
                "\t\t}\n\n");
    }

    public void insertStaticBasicNewMethod(StringBuilder builder, ExcelCommonData data, Boolean hasSoap) {
        builder.append("\t\tpublic StObject basicNew() {\n");
        if(data.getIsResponse()){
            builder.append("\t\t\treturn new ").append(data.getResponseClassName()).append("();\n\t\t}\n\n");
        }else{
            builder.append("\t\t\treturn new LiqAPI");
            builder.append(data.getIntegrationApiClassName());
            builder.append("();\n\t\t}\n\n");
        }
    }

    public void insertStaticSuperClassMethod(StringBuilder builder, ExcelCommonData data, Boolean hasSoap) {
        builder.append("\t\tpublic StClass getStSuperclass() {\n");
        if(data.getIsResponse()){
            builder.append("\t\t\treturn LiqAPIReturnData.clazz;");
        }else {
            builder.append("\t\t\treturn LiqAPI");
            builder.append(data.getSoapClassName()).append(".clazz;");
        }
        builder.append("\n\t\t}\n\n");
    }

    public void insertStaticJavaClassMethod(StringBuilder builder, ExcelCommonData data, Boolean hasSoap) {
        builder.append("\t\tpublic java.lang.Class getJavaClass() {\n");
        if(data.getIsResponse()){
            builder.append("\t\t\treturn ").append(data.getResponseClassName()).append(".class;");
        }else {
            builder.append("\t\t\treturn LiqAPI");
            builder.append(data.getIntegrationApiClassName()).append(".class;");
        }
        builder.append("\n\t\t}\n\n");
    }

    public void insertStaticNonPrimitiveFieldCollectionMappings(StringBuilder builder, List<ExcelData> dataList, ExcelCommonData data, Boolean hasSoap) {
        builder.append("\t\t//TODO: Developer might need to add non primitive field collection mappings here\n");
        builder.append("\t\tpublic List nonPrimitiveFieldCollectionMappings() {\n");
        builder.append("\t\t\treturn super.nonPrimitiveFieldCollectionMappings();\n");
        builder.append("\t\t}\n\n");
    }

    public void insertStaticNonPrimitiveFieldMappings(StringBuilder builder, List<ExcelData> dataList, ExcelCommonData data, Boolean hasSoap) {
        builder.append("\t\t//TODO: Developer might need to add non primitive field mappings here\n");
        builder.append("\t\tpublic List nonPrimitiveFieldMappings() {\n");
        builder.append("\t\t\treturn super.nonPrimitiveFieldMappings();\n");
        builder.append("\t\t}\n\n");
    }

    public void insertStaticPrimitiveFieldMappings(StringBuilder builder, List<ExcelData> dataList, ExcelCommonData data, Boolean hasSoap) {
        builder.append("\t\t//TODO: Developer might need to add primitive field mappings here\n");
        builder.append("\t\tpublic List primitiveFieldMappings() {\n");
        builder.append("\t\t\treturn super.primitiveFieldMappings();\n");
        builder.append("\t\t}\n\n");
    }

    public void insertIsRest(StringBuilder builder, ExcelCommonData data, Boolean hasSoap) {
        builder.append("\t\t@Override\n");
        builder.append("\t\tpublic boolean isRest() {\n");
        builder.append("\t\t\treturn true;\n");
        builder.append("\t\t}\n\n");
    }

    public void insertInnerStaticClassCloseBracket(StringBuilder builder, ExcelCommonData data, Boolean hasSoap) {
        builder.append("\t}\n\n");
    }
}
