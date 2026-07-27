package com.finastra.integrationapi.tool;

import com.finastra.integrationapi.tool.model.ExcelData;
import com.finastra.integrationapi.tool.model.IntegrationAPI;
import com.finastra.integrationapi.tool.utility.ExcelDataUtility;

import java.util.*;

/**
 * A utility class to read and process data from an Excel file.
 * The class uses Apache POI to parse Excel sheets and extract data into a structured format.
 */
public class ExcelReader {



    /**
     * The main method to test the ExcelReader functionality.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        //String filePath = "C:\\Auto\\Interest Payment API v1.11.xlsx"; // Replace with your file path.Interest Payment API
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the file path for the Excel file: ");
        String filePath = scanner.nextLine();
        Map<String, List<ExcelData>> map = ExcelDataUtility.INSTANCE.readExcelFile(filePath); // Call the method to read and process the Excel file.
        //System.out.println("Map : " + map); // Print the size of the map containing processed data.

        ExcelDataUtility.INSTANCE.generateLiqAPIClasses(map); // Call the method to generate API classes based on the processed data.
        ExcelDataUtility.INSTANCE.generateLiqAPITestClasses(map); // Call the method to generate API Test classes based on the processed data.

    }

    public static void execute(String path) {
        //String filePath = "C:\\Auto\\Interest Payment API v1.11.xlsx"; // Replace with your file path.
        //Scanner scanner = new Scanner(System.in);
        //System.out.print("Enter the file path for the Excel file: ");
        String filePath = path;//scanner.nextLine();
        Map<String, List<ExcelData>> map = ExcelDataUtility.INSTANCE.readExcelFile(filePath); // Call the method to read and process the Excel file.
        System.out.println("Map : " + map); // Print the size of the map containing processed data.

        ExcelDataUtility.INSTANCE.generateLiqAPIClasses(map); // Call the method to generate API classes based on the processed data.
        ExcelDataUtility.INSTANCE.generateLiqAPITestClasses(map); // Call the method to generate API Test classes based on the processed data.
        ExcelDataUtility.INSTANCE.generateSwaggerAPIClasses(map); // Call the method to generate Swagger API classes based on the processed data.

    }




}