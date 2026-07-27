package com.finastra.integrationapi.tool.utility;

import com.finastra.integrationapi.tool.constant.ExcelConstants;
import com.finastra.integrationapi.tool.engine.*;
import com.finastra.integrationapi.tool.engine.DeleteLiqAPIEngine;
import com.finastra.integrationapi.tool.engine.DeleteLiqAPITestEngine;
import com.finastra.integrationapi.tool.model.ExcelCommonData;
import com.finastra.integrationapi.tool.model.ExcelData;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public enum ExcelDataUtility {
    INSTANCE;

    // A map to store processed Excel data categorized by keys.
    private Map<String, List<ExcelData>> map = new HashMap<>();
    private List<ExcelData> list; // Temporary list to hold data for a specific key.
    private String key; // Current key for categorizing data.
    private boolean hasSoap;

    private Map<String, ExcelCommonData> commonMap = new HashMap<>();

    public ExcelCommonData getCommonDataForActionTypeAs(String actionType){
        return commonMap.get(actionType);
    }

    public void hasSoapApiClass(Boolean hasSoap){
        this.hasSoap =  hasSoap;
    }

    private void setCommonProperties(Sheet sheet){
        ExcelCommonData commonData = ExcelCommonData.builder().build();
        Iterator<Row> rowIterator = sheet.iterator();
        while (rowIterator.hasNext()) {

            Row row = rowIterator.next();
            Cell firstCell = row.getCell(0);
            if (firstCell == null || firstCell.getCellType() == CellType.BLANK) {
                continue;
            }
            if(firstCell.getCellType() != CellType.STRING) {
                break;
            }
            if(firstCell.getStringCellValue().equalsIgnoreCase("Prerequisites")
            || firstCell.getStringCellValue().equalsIgnoreCase("R")
            || firstCell.getStringCellValue().equalsIgnoreCase("NR")) {
                continue;
            }
            else if(firstCell.getStringCellValue().equalsIgnoreCase("PCP")) {
                // Set common properties for the ExcelCommonData object.
                Cell valCell = row.getCell(1);
                commonData.setIsPCP(valCell != null && valCell.getStringCellValue().equalsIgnoreCase("y"));
                continue;
            }
            else if(firstCell.getStringCellValue().equalsIgnoreCase("FILE_OP_PATH")) {
                // Override file output path to use workspace-relative directory
                commonData.setFileOpPath(ExcelConstants.JAVA_GENERATED_FILE_DIR);
                continue;
            }
            else if(firstCell.getStringCellValue().equalsIgnoreCase("SOAP_CLASS")) {
                Cell valCell = row.getCell(1);
                if(valCell == null || valCell.getStringCellValue() == null || valCell.getStringCellValue().isEmpty()){
                    this.hasSoapApiClass(false);
                }else{
                    // Set common properties for the ExcelCommonData object.
                    commonData.setSoapClassName(valCell.getStringCellValue());
                    this.hasSoapApiClass(true);
                }
                continue;
            }
            else if(firstCell.getStringCellValue().equalsIgnoreCase("INTEGRATION_CLASS")) {
                // Set common properties for the ExcelCommonData object.
                Cell valCell = row.getCell(1);
                if (valCell != null) commonData.setIntegrationApiClassName(valCell.getStringCellValue());
                continue;
            }else if(firstCell.getStringCellValue().equalsIgnoreCase("PACKAGE_NAME")) {
                // Set common properties for the ExcelCommonData object.
                Cell valCell = row.getCell(1);
                if (valCell != null) commonData.setPackageName(valCell.getStringCellValue());
                continue;
            }else if(firstCell.getStringCellValue().equalsIgnoreCase("RESPONSE_CLASS")) {
                // Set common properties for the ExcelCommonData object.
                Cell valCell = row.getCell(1);
                if (valCell != null) commonData.setResponseClassName(valCell.getStringCellValue());
                continue;
            }else{
                break;
            }
        }
        commonMap.put(sheet.getSheetName().toLowerCase(), commonData);
        // Add the common data to the map with the sheet name as the key.
        ////System.out.println("Common Map: " + commonMap);
    }

    // List of HTTP actions to filter relevant sheets.
    List<String> httpActions = List.of("create", "update", "getbyid", "delete");

    /**
     * Reads an Excel file from the provided file path and processes its content.
     *
     * @param filePath The file path of the Excel file to be read.
     */
    public Map<String, List<ExcelData>> readExcelFile(String filePath) {
        try (FileInputStream fileInputStream = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fileInputStream)) {

            // Iterate through all sheets in the workbook.
            Iterator<Sheet> sheetIterator = workbook.sheetIterator();
            while (sheetIterator.hasNext()) {
                Sheet sheet = sheetIterator.next();
                ////System.out.println("Sheet Name: " + sheet.getSheetName());

                // Skip sheets that are not in the list of HTTP actions.
                if (!httpActions.contains(sheet.getSheetName().toLowerCase())) {
                    ////System.out.println("Found Sheet, skipping..." + sheet.getSheetName());
                    continue;
                }

                setCommonProperties(sheet);

                // Iterate through rows in the sheet.
                Iterator<Row> rowIterator = sheet.iterator();
                while (rowIterator.hasNext()) {
                    ExcelData data = ExcelData.builder().build(); // Create a new ExcelData object.
                    Row row = rowIterator.next();

                    // Skip rows where the first cell is null or not numeric.
                    Cell firstCell = row.getCell(0);
                    if (firstCell == null || firstCell.getCellType() != CellType.NUMERIC) {
                        ////System.out.println("Skipping row: " + row.getRowNum());

                        // Handle "Input" and "Output" rows to categorize data.
                        if (firstCell != null && firstCell.getCellType() == CellType.STRING
                                && firstCell.getStringCellValue().equalsIgnoreCase("Input")) {
                            key = sheet.getSheetName().toLowerCase().concat("Input");
                            list = new ArrayList<>();
                        } else if (firstCell != null && firstCell.getCellType() == CellType.STRING
                                && firstCell.getStringCellValue().equalsIgnoreCase("Output")) {
                            map.put(key, list);
                            key = sheet.getSheetName().toLowerCase().concat("Output");
                            list = new ArrayList<>();
                        }
                        continue;
                    }

                    // Iterate through cells in the row.
                    Iterator<Cell> cellIterator = row.cellIterator();
                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();

                        // Process cell values based on their column index.
                        switch (cell.getColumnIndex()) {
                            case 0:
                                ////System.out.print(cell.getNumericCellValue() + "\t");
                                data.setSlNo(cell.getNumericCellValue());
                                break;
                            case 1:
                                ////System.out.print(cell.getStringCellValue() + "\t");
                                data.setClassName(cell.getStringCellValue());
                                break;
                            case 2:
                                ////System.out.print(cell.getStringCellValue() + "\t");
                                data.setAttributeCategory(cell.getStringCellValue());
                                break;
                            case 3:
                                //System.out.print(cell.getStringCellValue() + "\t");
                                data.setSwaggerAttributeCategory(cell.getStringCellValue());
                                break;
                            case 4:
                                //System.out.print(cell.getStringCellValue() + "\t");
                                data.setIsList(cell.getStringCellValue().equalsIgnoreCase("Y"));
                                break;
                            case 5:
                                //System.out.print(cell.getStringCellValue() + "\t");
                                data.setAttributeSubCategory1(cell.getStringCellValue());
                                break;
                            case 6:
                                //System.out.print(cell.getStringCellValue() + "\t");
                                data.setSwaggerAttributeSubCategory1(cell.getStringCellValue());
                                break;
                            case 7:
                                //System.out.print(cell.getStringCellValue() + "\t");
                                data.setIsList1(cell.getStringCellValue().equalsIgnoreCase("Y"));
                                break;
                            case 8:
                                //System.out.print(cell.getStringCellValue() + "\t");
                                data.setAttributeSubCategory2(cell.getStringCellValue());
                                break;
                            case 9:
                                //System.out.print(cell.getStringCellValue() + "\t");
                                data.setSwaggerAttributeSubCategory2(cell.getStringCellValue());
                                break;
                            case 10:
                                //System.out.print(cell.getStringCellValue() + "\t");
                                data.setIsList2(cell.getStringCellValue().equalsIgnoreCase("Y"));
                                break;
                            case 11:
                                //System.out.print(cell.getStringCellValue() + "\t");
                                data.setAttributeSubCategory3(cell.getStringCellValue());
                                break;
                            case 12:
                                //System.out.print(cell.getStringCellValue() + "\t");
                                data.setSwaggerAttributeSubCategory3(cell.getStringCellValue());
                                break;
                            case 13:
                                //System.out.print(cell.getStringCellValue() + "\t");
                                data.setIsList3(cell.getStringCellValue().equalsIgnoreCase("Y"));
                                break;
                            case 14:
                                //System.out.print(cell.getStringCellValue() + "\t");
                                data.setAttributeFieldName(cell.getStringCellValue());
                                break;
                            case 15:
                                //System.out.print(cell.getStringCellValue() + "\t");
                                data.setSwaggerAttributeFieldName(cell.getStringCellValue());
                                break;
                            case 16:
                                //System.out.print(cell.getStringCellValue() + "\t");
                                data.setDataType(cell.getStringCellValue());
                                break;
                            case 17:
                                //System.out.print(cell.getStringCellValue() + "\t");
                                data.setIsRequired(cell.getStringCellValue().equalsIgnoreCase("Y"));
                                break;
                            case 18:
                                //System.out.print(cell.getStringCellValue() + "\t");
                                data.setAttributeDescription(cell.getStringCellValue());
                                break;
                            case 19:
                                //System.out.print(cell.getStringCellValue() + "\t");
                                data.setSwaggerAttributeDescription(cell.getStringCellValue());
                                break;
                            case 20:
                                //System.out.print(cell.getStringCellValue() + "\t");
                                data.setIsUpdatable(cell.getStringCellValue().equalsIgnoreCase("Y"));
                                break;
                            case 21:
                                //System.out.print(cell.getStringCellValue() + "\t");
                                data.setInSoapApi(cell.getStringCellValue().equalsIgnoreCase("Y"));
                                break;
                            case 22:
                                int minSize = getCellIntValue(cell);
                                if(minSize >= 0)
                                    data.setMinSize(minSize);
                                break;
                            case 23:
                                int maxSize = getCellIntValue(cell);
                                if(maxSize >= 0)
                                    data.setMaxSize(maxSize);
                                break;
                            default:
                                //System.out.println("Other columns not required");
                                break;
                        }
                    }
                    list.add(data); // Add the processed data to the list.
                }
                map.put(key, list); // Add the list to the map with the current key.
            }
        } catch (IOException e) {
            e.printStackTrace(); // Print the stack trace in case of an exception.
        }
         // Return the map.
        return map;
    }

    private int getCellIntValue(Cell cell) {
        if (cell.getCellType() == CellType.NUMERIC) {
            return (int) cell.getNumericCellValue();
        } else if (cell.getCellType() == CellType.STRING) {
            try {
                return Integer.parseInt(cell.getStringCellValue().trim());
            } catch (NumberFormatException e) {
                return -1;
            }
        }
        return -1;
    }

    public void generateLiqAPIClasses(Map<String, List<ExcelData>> map) {
        createLiqAPIClasses(map.get("createInput"),ExcelDataUtility.INSTANCE.getCommonDataForActionTypeAs("create"));
        updateLiqAPIClasses(map.get("updateInput"),ExcelDataUtility.INSTANCE.getCommonDataForActionTypeAs("update"));
        queryLiqAPIClasses(map.get("getbyidInput"),ExcelDataUtility.INSTANCE.getCommonDataForActionTypeAs("getbyid"));
        deleteLiqAPIClasses(map.get("deleteInput"),ExcelDataUtility.INSTANCE.getCommonDataForActionTypeAs("delete"));
        responseLiqAPIClasses(getOutputMap(map),ExcelDataUtility.INSTANCE.getCommonDataForActionTypeAs("create"));
    }

    public void generateSwaggerAPIClasses(Map<String, List<ExcelData>> map) {
        createSwaggerAPIClasses(map.get("createInput"),ExcelDataUtility.INSTANCE.getCommonDataForActionTypeAs("create"));
        updateSwaggerAPIClasses(map.get("updateInput"),ExcelDataUtility.INSTANCE.getCommonDataForActionTypeAs("update"));
        querySwaggerAPIClasses(map.get("getbyidInput"),ExcelDataUtility.INSTANCE.getCommonDataForActionTypeAs("getbyid"));
        responseSwaggerAPIClasses(getOutputMap(map,"createOutput"),ExcelDataUtility.INSTANCE.getCommonDataForActionTypeAs("create"));
        responseSwaggerAPIClasses(getOutputMap(map,"updateOutput"),ExcelDataUtility.INSTANCE.getCommonDataForActionTypeAs("update"));
        responseSwaggerAPIClasses(getOutputMap(map,"getbyidOutput"),ExcelDataUtility.INSTANCE.getCommonDataForActionTypeAs("getbyid"));
        controllerSwaggerAPIClasses(null,ExcelDataUtility.INSTANCE.getCommonDataForActionTypeAs("create"));
    }

    private List<ExcelData> getOutputMap(Map<String, List<ExcelData>> map){
        // Combine the lists into a single ArrayList
        List<ExcelData> combinedList = new ArrayList<>();
        if (map.get("createOutput") != null) {
            combinedList.addAll(map.get("createOutput"));
        }
        if (map.get("updateOutput") != null) {
            combinedList.addAll(map.get("updateOutput"));
        }
        if (map.get("getbyidOutput") != null) {
            combinedList.addAll(map.get("getbyidOutput"));
        }

        // Print the combined list
        ////System.out.println("Combined Set: " + combinedList);

        // Get unique ExcelData objects based on AttributeCategory and AttributeFieldName
        List<ExcelData> excelNonPrimitiveDataList=getUniqueExcelDataByAttributeCategoryAndAttributeFieldName(combinedList);
        ////System.out.println("Unique List NonPrmitiveFields: " + excelNonPrimitiveDataList);
        // Get unique ExcelData objects based on AttributeFieldName
        List<ExcelData> excelPrimitiveDataList=getUniqueExcelDataByAttributeFieldName(combinedList);
        ////System.out.println("Unique List NonPrmitiveFields: " + excelPrimitiveDataList);
        // Combine the two lists into a final list
        List<ExcelData> finalList = new ArrayList<>();
        finalList.addAll(excelNonPrimitiveDataList);
        finalList.addAll(excelPrimitiveDataList);
        return finalList;
    }

    private List<ExcelData> getOutputMap(Map<String, List<ExcelData>> map,String actionType){
        return map.get(actionType);
    }

    //
    public List<ExcelData> getUniqueExcelDataByAttributeCategoryAndAttributeFieldName(List<ExcelData> excelDataList) {
        // Use a HashMap to store unique ExcelData based on AttributeCategory and AttributeFieldName
        Map<String, ExcelData> uniqueDataMap = new HashMap<>();

        for (ExcelData data : excelDataList) {
            if(data.getAttributeCategory() == null || data.getAttributeCategory().isEmpty()) {
                continue; // Skip if either field is null
            }
            // Create a unique key using AttributeCategory and AttributeFieldName
            String uniqueKey = data.getAttributeCategory() + "|" + data.getAttributeFieldName();
            // Add to the map, overwriting any existing entry with the same key
            uniqueDataMap.put(uniqueKey, data);
        }

        // Return the values of the map as a list
        return uniqueDataMap.values().stream().collect(Collectors.toList());
    }

    // This method retrieves unique ExcelData objects based on the AttributeFieldName.
    public List<ExcelData> getUniqueExcelDataByAttributeFieldName(List<ExcelData> excelDataList) {
        // Use a HashMap to store unique ExcelData based on AttributeCategory and AttributeFieldName
        Map<String, ExcelData> uniqueDataMap = new HashMap<>();

        for (ExcelData data : excelDataList) {
            if(data.getAttributeCategory() != null && !data.getAttributeCategory().isEmpty()) {
                continue; // Skip if either field is null
            }
            // Create a unique key using AttributeCategory and AttributeFieldName
            String uniqueKey = data.getAttributeFieldName();
            // Add to the map, overwriting any existing entry with the same key
            uniqueDataMap.put(uniqueKey, data);
        }

        // Return the values of the map as a list
        return uniqueDataMap.values().stream().collect(Collectors.toList());
    }

    private void responseLiqAPIClasses(List<ExcelData> responseDataList, ExcelCommonData responseData) {
        ////System.out.println("Response Input: " + responseDataList);
        ////System.out.println("Response Common Data: " + responseData);
        // Generate classes based on the createInput and common data.
        if(responseData == null) return;
        responseData.setType("output");
        responseData.setIsResponse(true);
        String response = ResponseLiqApiEngine.INSTANCE.getResponseLiqAPIClass(responseDataList,responseData,hasSoap);
        String fileName= responseData.getFileOpPath().concat("/").concat(responseData.getResponseClassName().concat(".java"));
        FileUtility.INSTANCE.writeToFile(fileName, response);
    }

    private void queryLiqAPIClasses(List<ExcelData> queryInput, ExcelCommonData query) {
        ////System.out.println("Query Input: " + queryInput);
        ////System.out.println("Query Common Data: " + query);
        // Generate classes based on the createInput and common data.
        if(query == null) return;
        query.setType("query");
        query.setIsResponse(false);
        String response = QueryLiqAPIEngine.INSTANCE.getQueryLiqAPIClass(queryInput,query,hasSoap);
        String fileName= query.getFileOpPath().concat("/").concat("LiqAPI").concat(query.getIntegrationApiClassName().concat(".java"));
        FileUtility.INSTANCE.writeToFile(fileName, response);
    }

    private void updateLiqAPIClasses(List<ExcelData> updateInput, ExcelCommonData update) {
        ////System.out.println("Update Input: " + updateInput);
        ////System.out.println("Update Common Data: " + update);
        // Generate classes based on the createInput and common data.
        if(update == null) return;
        update.setType("update");
        update.setIsResponse(false);
        String response = UpdateLiqAPIEngine.INSTANCE.getUpdateLiqAPIClass(updateInput,update,hasSoap);
        String fileName= update.getFileOpPath().concat("/").concat("LiqAPI").concat(update.getIntegrationApiClassName().concat(".java"));
        FileUtility.INSTANCE.writeToFile(fileName, response);
    }


    private void createLiqAPIClasses(List<ExcelData> createInput, ExcelCommonData create) {
        ////System.out.println("Create Input: " + createInput);
        ////System.out.println("Create Common Data: " + create);
        // Generate classes based on the createInput and common data.
        if(create == null) return;
        create.setType("create");
        create.setIsResponse(false);
        String response = CreateLiqAPIEngine.INSTANCE.getCreateLiqAPIClass(createInput,create,hasSoap);
        String fileName= create.getFileOpPath().concat("/").concat("LiqAPI").concat(create.getIntegrationApiClassName().concat(".java"));
        FileUtility.INSTANCE.writeToFile(fileName, response);

    }

    private void createSwaggerAPIClasses(List<ExcelData> createInput, ExcelCommonData create) {
        ////System.out.println("Create Input: " + createInput);
        ////System.out.println("Create Common Data: " + create);
        // Generate classes based on the createInput and common data.
        if(create == null) return;
        create.setType("create");
        create.setIsResponse(false);
        String response = RequestSwaggerAPIEngine.INSTANCE.genrateSwaggerAPIClass(createInput,create,hasSoap);
        String fileName= create.getFileOpPath().concat("/").concat(create.getIntegrationApiClassName().concat(".java"));
        FileUtility.INSTANCE.writeToFile(fileName, response);

    }

    private void updateSwaggerAPIClasses(List<ExcelData> updateInput, ExcelCommonData update) {
        if(update == null) return;
        update.setType("update");
        update.setIsResponse(false);
        String response = RequestSwaggerAPIEngine.INSTANCE.genrateSwaggerAPIClass(updateInput,update,hasSoap);
        String fileName= update.getFileOpPath().concat("/").concat(update.getIntegrationApiClassName().concat(".java"));
        FileUtility.INSTANCE.writeToFile(fileName, response);

    }

    private void querySwaggerAPIClasses(List<ExcelData> updateInput, ExcelCommonData update) {
        if(update == null) return;
        update.setType("query");
        update.setIsResponse(false);
        String response = RequestSwaggerAPIEngine.INSTANCE.genrateSwaggerAPIClass(updateInput,update,hasSoap);
        String fileName= update.getFileOpPath().concat("/").concat(update.getIntegrationApiClassName().concat(".java"));
        FileUtility.INSTANCE.writeToFile(fileName, response);

    }

    private void responseSwaggerAPIClasses(List<ExcelData> responseDataList, ExcelCommonData responseData) {
        if(responseData == null) return;
        responseData.setType("output");
        responseData.setIsResponse(true);
        String response = ResponseSwaggerAPIEngine.INSTANCE.genrateSwaggerAPIClass(responseDataList,responseData,hasSoap);
        String fileName= responseData.getFileOpPath().concat("/").concat(responseData.getIntegrationApiClassName().concat("Response.java"));
        FileUtility.INSTANCE.writeToFile(fileName, response);
    }

    private void controllerSwaggerAPIClasses(List<ExcelData> responseDataList, ExcelCommonData responseData) {
        //responseData.setType("output");
        //responseData.setIsResponse(true);
        if(responseData == null) return;
        String response = ControllerSwaggerEngine.INSTANCE.genrateSwaggerAPIClass(responseDataList,responseData,hasSoap);
        String fileName= responseData.getFileOpPath().concat("/").concat(ControllerSwaggerEngine.INSTANCE.getModuleName(responseData.getIntegrationApiClassName()).concat("Controller.java"));
        FileUtility.INSTANCE.writeToFile(fileName, response);
    }


    public void generateLiqAPITestClasses(Map<String, List<ExcelData>> map) {
        createLiqAPITestClasses(map.get("createInput"),ExcelDataUtility.INSTANCE.getCommonDataForActionTypeAs("create"));
        updateLiqAPITestClasses(map.get("updateInput"),ExcelDataUtility.INSTANCE.getCommonDataForActionTypeAs("update"));
        queryLiqAPITestClasses(map.get("getbyidInput"),ExcelDataUtility.INSTANCE.getCommonDataForActionTypeAs("getbyid"));
        deleteLiqAPITestClasses(map.get("deleteInput"),ExcelDataUtility.INSTANCE.getCommonDataForActionTypeAs("delete"));
        responseLiqAPITestClasses(getOutputMap(map),ExcelDataUtility.INSTANCE.getCommonDataForActionTypeAs("create"));
    }

    private void responseLiqAPITestClasses(List<ExcelData> responseDataList, ExcelCommonData responseData) {
        ////System.out.println("Response Test Input: " + responseDataList);
        ////System.out.println("Response Test Common Data: " + responseData);
        // Generate classes based on the createInput and common data.
        if(responseData == null) return;
        responseData.setType("output");
        responseData.setIsResponse(true);
        String response = ResponseLiqApiTestEngine.INSTANCE.getResponseLiqAPITestClass(responseDataList,responseData,hasSoap);
        String fileName= responseData.getFileOpPath().concat("/").concat(responseData.getResponseClassName().concat("Test").concat(".java"));
        FileUtility.INSTANCE.writeToFile(fileName, response);
    }

    private void queryLiqAPITestClasses(List<ExcelData> queryInput, ExcelCommonData query) {
        ////System.out.println("Query Test Input: " + queryInput);
        ////System.out.println("Query Test Common Data: " + query);
        // Generate classes based on the createInput and common data.
        if(query == null) return;
        query.setType("query");
        query.setIsResponse(false);
        String response = QueryLiqAPITestEngine.INSTANCE.getQueryLiqAPITestClass(queryInput,query,hasSoap);
        String fileName= query.getFileOpPath().concat("/").concat("LiqAPI").concat(query.getIntegrationApiClassName().concat("Test").concat(".java"));
        FileUtility.INSTANCE.writeToFile(fileName, response);
    }

    private void updateLiqAPITestClasses(List<ExcelData> updateInput, ExcelCommonData update) {
        ////System.out.println("Update Test Input: " + updateInput);
        ////System.out.println("Update Test Common Data: " + update);
        // Generate classes based on the createInput and common data.
        if(update == null) return;
        update.setType("update");
        update.setIsResponse(false);
        String response = UpdateLiqAPITestEngine.INSTANCE.getUpdateLiqAPITestClass(updateInput,update,hasSoap);
        String fileName= update.getFileOpPath().concat("/").concat("LiqAPI").concat(update.getIntegrationApiClassName().concat("Test").concat(".java"));
        FileUtility.INSTANCE.writeToFile(fileName, response);
    }


    private void createLiqAPITestClasses(List<ExcelData> createInput, ExcelCommonData create) {
        ////System.out.println("Create Test Input: " + createInput);
        ////System.out.println("Create Test Common Data: " + create);
        // Generate classes based on the createInput and common data.
        if(create == null) return;
        create.setType("create");
        create.setIsResponse(false);
        String response = CreateLiqAPITestEngine.INSTANCE.getCreateLiqAPITestClass(createInput,create,hasSoap);
        String fileName= create.getFileOpPath().concat("/").concat("LiqAPI").concat(create.getIntegrationApiClassName().concat("Test").concat(".java"));
        FileUtility.INSTANCE.writeToFile(fileName, response);

    }

    private void deleteLiqAPIClasses(List<ExcelData> deleteInput, ExcelCommonData delete) {
        if(delete == null) return;
        delete.setType("delete");
        delete.setIsResponse(false);
        String response = DeleteLiqAPIEngine.INSTANCE.getDeleteLiqAPIClass(deleteInput,delete,hasSoap);
        String fileName= delete.getFileOpPath().concat("/").concat("LiqAPI").concat(delete.getIntegrationApiClassName().concat(".java"));
        FileUtility.INSTANCE.writeToFile(fileName, response);
    }

    private void deleteLiqAPITestClasses(List<ExcelData> deleteInput, ExcelCommonData delete) {
        if(delete == null) return;
        delete.setType("delete");
        delete.setIsResponse(false);
        String response = DeleteLiqAPITestEngine.INSTANCE.getDeleteLiqAPITestClass(deleteInput,delete,hasSoap);
        String fileName= delete.getFileOpPath().concat("/").concat("LiqAPI").concat(delete.getIntegrationApiClassName().concat("Test").concat(".java"));
        FileUtility.INSTANCE.writeToFile(fileName, response);
    }


}
