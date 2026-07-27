package com.finastra.integrationapi.tool.utility;

import com.finastra.integrationapi.tool.model.IntegrationAPI;
import com.finastra.integrationapi.tool.model.Modular;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public enum ConverterXMLToModularBO {
	
	INSTANCE;
	
	public void executeAll(IntegrationAPI model) {
		Modular modular = model.getModular();
		String rootClassName = modular.getIntegrationAPIName();
		String ipFilePath = modular.getIpFilePath();
		String opFilePath = modular.getOpFilePath();
		String packageNameRequest = modular.getRequestPackageName();
		String packageNameCommon = modular.getCommonPackageName();
		String packageNameResponse = modular.getResponsePackageName();
		
		/*Start request file creation*/
		
		String createRootClassName = "Create".concat(rootClassName);
		String  createFileName = modular.getCreateRequestFileName();
		String createIpFilePath = null != createFileName?ipFilePath.concat("\\").concat(createFileName):null;
		
		String udpateRootClassName = "Update".concat(rootClassName);
		String  updateFileName = modular.getUpdateRequestFileName();
		String updateIpFilePath = null != updateFileName?ipFilePath.concat("\\").concat(updateFileName):null;
		
		String queryRootClassName = "Query".concat(rootClassName);
		String  queryFileName = modular.getQueryRequestFileName();
		String queryIpFilePath = null != queryFileName?ipFilePath.concat("\\").concat(queryFileName):null;
		
		String cancelRootClassName = "Cancel".concat(rootClassName);
		String cancelFileName = modular.getCancelRequestFileName();
		String cancelIpFilePath = null != cancelFileName?ipFilePath.concat("\\").concat(cancelFileName):null;
		
		String deleteRootClassName = "Delete".concat(rootClassName);
		String deleteFileName = modular.getDeleteRequestFileName();
		String deleteIpFilePath = null != deleteFileName?ipFilePath.concat("\\").concat(deleteFileName):null;
		
		String searchRootClassName = "Search".concat(rootClassName);
		String searchFileName = modular.getSearchRequestFileName();
		String searchIpFilePath = null != searchFileName?ipFilePath.concat("\\").concat(searchFileName):null;
		
		
		
		/*End request file creation*/
		
		/*Start response file creation*/
		
		String createRootClassNameResponse = "Create".concat(rootClassName).concat("Response");
		String  createResponseFileName = modular.getCreateResponseFileName();
		String createResponseIpFilePath =  null != createResponseFileName?ipFilePath.concat("\\").concat(createResponseFileName):null;
		
		String updateRootClassNameResponse = "Update".concat(rootClassName).concat("Response");
		String uapdateResponseFileName = modular.getUpdateResponseFileName();
		String updateResponseIpFilePath = null != uapdateResponseFileName?ipFilePath.concat("\\").concat(uapdateResponseFileName):null;
		
		String queryRootClassNameResponse = "Query".concat(rootClassName).concat("Response");
		String queryResponseFileName = modular.getQueryResponseFileName();
		String queryResponseIpFilePath = null != queryResponseFileName?ipFilePath.concat("\\").concat(queryResponseFileName):null;
		
		String deleteRootClassNameResponse = "Delete".concat(rootClassName).concat("Response");
		String deleteResponseFileName = modular.getDeleteResponseFileName();
		String deleteResponseIpFilePath = null != deleteResponseFileName?ipFilePath.concat("\\").concat(deleteResponseFileName):null;
		
		String cancelRootClassNameResponse = "Cancel".concat(rootClassName).concat("Response");
		String canceleResponseFileName = modular.getCancelResponseFileName();
		String cancelResponseIpFilePath = null != canceleResponseFileName?ipFilePath.concat("\\").concat(canceleResponseFileName):null;
		
		String searchRootClassNameResponse = "Search".concat(rootClassName).concat("Response");
		String  searchResponseFileName = modular.getSearchResponseFileName();
		String searchResponseIpFilePath = null != searchResponseFileName?ipFilePath.concat("\\").concat(searchResponseFileName):null;
		
		
		/*End response file creation*/
		if(null != createRootClassName && !createRootClassName.isBlank()
				&& null != createIpFilePath && !createIpFilePath.isBlank()) {
			generateFiles(createRootClassName,packageNameRequest,createIpFilePath,opFilePath,"createRequest");
			System.out.println("Create Request Object generated successfully...");
		}
		
		if(null != udpateRootClassName && !udpateRootClassName.isBlank()
				&& null != updateIpFilePath && !updateIpFilePath.isBlank()) {
			generateFiles(udpateRootClassName,packageNameRequest,updateIpFilePath,opFilePath,"updateRequest");
			System.out.println("Update Request Object generated successfully...");
		}
		
		if(null != queryRootClassName && !queryRootClassName.isBlank()
				&& null != queryIpFilePath && !queryIpFilePath.isBlank()) {
			generateFiles(queryRootClassName,packageNameRequest,queryIpFilePath,opFilePath,"queryRequest");
			System.out.println("Query Request Object generated successfully...");
		}
		
		if(null != cancelRootClassName && !cancelRootClassName.isBlank()
				&& null != cancelIpFilePath && !cancelIpFilePath.isBlank()) {
			generateFiles(cancelRootClassName,packageNameRequest,cancelIpFilePath,opFilePath,"cancelRequest");
			System.out.println("Cancel Request Object generated successfully...");
		}
		
		if(null != deleteRootClassName && !deleteRootClassName.isBlank()
				&& null != deleteIpFilePath && !deleteIpFilePath.isBlank()) {
			generateFiles(deleteRootClassName,packageNameRequest,deleteIpFilePath,opFilePath,"deleteRequest");
			System.out.println("Delete Request Object generated successfully...");
		}
		
		if(null != searchRootClassName && !searchRootClassName.isBlank()
				&& null != searchIpFilePath && !searchIpFilePath.isBlank()) {
			generateFiles(searchRootClassName,packageNameRequest,searchIpFilePath,opFilePath,"searchRequest");
			System.out.println("Search Request Object generated successfully...");
		}
			
		if(null != createRootClassNameResponse && !createRootClassNameResponse.isBlank()
				&& null != createResponseIpFilePath && !createResponseIpFilePath.isBlank()) {
			generateFiles(createRootClassNameResponse,packageNameResponse,createResponseIpFilePath,opFilePath,"createResponse");
			System.out.println("Create Response Object generated successfully...");
		}
		
		if(null != updateRootClassNameResponse && !updateRootClassNameResponse.isBlank()
				&& null != updateResponseIpFilePath && !updateResponseIpFilePath.isBlank()) {
			generateFiles(updateRootClassNameResponse,packageNameResponse,updateResponseIpFilePath,opFilePath,"updateResponse");
			System.out.println("Update Response Object generated successfully...");
		}
		
		if(null != queryRootClassNameResponse && !queryRootClassNameResponse.isBlank()
				&& null != queryResponseIpFilePath && !queryResponseIpFilePath.isBlank()) {
			generateFiles(queryRootClassNameResponse,packageNameResponse,queryResponseIpFilePath,opFilePath,"queryResponse");
			System.out.println("Query Request Object generated successfully...");
		}
		
		if(null != cancelRootClassNameResponse && !cancelRootClassNameResponse.isBlank()
				&& null != cancelResponseIpFilePath && !cancelResponseIpFilePath.isBlank()) {
			generateFiles(cancelRootClassNameResponse,packageNameResponse,cancelResponseIpFilePath,opFilePath,"cancelResponse");
			System.out.println("Cancel Request Object generated successfully...");
		}
		
		if(null != deleteRootClassNameResponse && !deleteRootClassNameResponse.isBlank()
				&& null != deleteResponseIpFilePath && !deleteResponseIpFilePath.isBlank()) {
			generateFiles(deleteRootClassNameResponse,packageNameResponse,deleteResponseIpFilePath,opFilePath,"deleteResponse");
			System.out.println("Delete Request Object generated successfully...");
		}
		
		if(null != searchRootClassNameResponse && !searchRootClassNameResponse.isBlank()
				&& null != searchResponseIpFilePath && !searchResponseIpFilePath.isBlank()) {
			generateFiles(searchRootClassNameResponse,packageNameResponse,searchResponseIpFilePath,opFilePath,"searchResponse");
			System.out.println("Search Response Object generated successfully...");
		}
		
		if(null != queryRootClassNameResponse && !queryRootClassNameResponse.isBlank()
				&& null != queryResponseIpFilePath && !queryResponseIpFilePath.isBlank()) {
			generateCommonFiles(queryRootClassNameResponse,packageNameCommon,queryResponseIpFilePath,opFilePath);
		}
		
		if(null != searchRootClassNameResponse && !searchRootClassNameResponse.isBlank()
				&& null != searchResponseIpFilePath && !searchResponseIpFilePath.isBlank()) {
			generateCommonFiles(searchRootClassNameResponse,packageNameCommon,searchResponseIpFilePath,opFilePath);
		}
		
		System.out.println("Common Object generated successfully...");
		
	}
	
	public void generateCommonFiles(String queryRootClassNameResponse, String packageNameCommon,
			String queryResponseIpFilePath, String opFilePath) {
		Map<String,Object> map = XMLJSONParsingUtils.Utils.convertXMLAPI2Map(getXMLFile(queryResponseIpFilePath));
		List<Map<String,Object>> list = new ArrayList<>();
		List<Map<String,Object>> listOfChildElementsMap = getChildElementsMap((Map<String, Object>) map.get(queryRootClassNameResponse),list);
		System.out.println("listOfChildElementsMap size- "+listOfChildElementsMap.size());
		List<Map<String,Object>> uniqeListOfChildeLementMaps = getUniqeListOfChildElementMap(listOfChildElementsMap);
		System.out.println("uniqeListOfChildeLementMaps size- "+uniqeListOfChildeLementMaps.size());
		uniqeListOfChildeLementMaps.stream().forEach(m1 -> {
			m1.entrySet().stream().forEach(m -> {
				generateModularBOClasses(m.getKey(), packageNameCommon, getMap(m),
						opFilePath.concat("\\modular\\".concat(packageNameCommon.replace(".", "\\\\"))), "common");
			});
		});
		
	}
	
	public List<Map<String, Object>> getUniqeListOfChildElementMap(
			List<Map<String, Object>> listOfChildElementsMap) {
		List<Map<String, Object>> list = new ArrayList<>();
		List<String> listKeys = new ArrayList<>();
		for(Map<String, Object> map : listOfChildElementsMap) {
			map.entrySet().stream().forEach(m ->{
				if(!listKeys.contains(m.getKey())) {
					listKeys.add(m.getKey());
					list.add(getMap(m));
				}else {
					System.out.println("Common keys- "+m.getKey());
				}
			});
		}
		return list;
	}

	public List<Map<String, Object>> getChildElementsMap(Map<String, Object> map,List<Map<String,Object>> list) {
		for(Map.Entry<String, Object> entry : map.entrySet()) {
			if(!(entry.getValue() instanceof String)) {
				list.add(getMap(entry));
				getChildElementsMap((LinkedHashMap<String, Object>) entry.getValue(),list);
			}
		}
		return list;
	}

	public Map<String, Object> getMap(Entry<String, Object> m) {
		Map<String,Object> map = new LinkedHashMap<>();
		map.put(m.getKey(), m.getValue());
		return map;
	}

	public void generateFiles(String rootClassName, String packageName, String ipFilePath,
			String opFilePath, String type) {
		Map<String,Object> map = XMLJSONParsingUtils.Utils.convertXMLAPI2Map(getXMLFile(ipFilePath));
		generateModularBOClasses(rootClassName,packageName,map,opFilePath.concat("\\modular\\".concat(packageName.replace(".", "\\\\"))),type);
	}

	public void generateModularBOClasses(String rootClassName, String packageName, Map<String, Object> map, String opFilePath,String type) {
		Map<String,Object> rootMap = (Map<String, Object>) map.get(rootClassName);
		StringBuilder builder = createPackageAndImportDetails(rootMap,packageName,type);
		builder = createModularBOClassStructure(builder,rootMap,rootClassName,packageName,type);
		//System.out.println(builder.toString());
		saveFile(rootClassName,builder.toString(),opFilePath);
	}

	public void saveFile(String className, String classContent, String opFilePath) {
		try {
			String fileName = opFilePath.concat("\\").concat(className).concat(".java");
			File f = new File(fileName);
			if (!f.exists()) {
				f.getParentFile().mkdirs();
				f.createNewFile();
			}
			Path path = Paths.get(fileName);
			byte[] strToBytes = classContent.getBytes();
			Files.write(path, strToBytes);

			System.out.println("Generated " + className + ".java");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public StringBuilder createPackageAndImportDetails(Map<String, Object> rootMap, String packageName,String type) {
		StringBuilder builder = new StringBuilder();
		builder.append("/* This is an auto - generated class. Do not modify this class.*/\n\n");
		builder.append("package ").append(packageName).append(";\n\n");
		if(!"queryRequest".equalsIgnoreCase(type)) {
			builder.append("import io.swagger.v3.oas.annotations.media.Schema;\n");
		}
		
		builder.append("import java.util.List;\n");
		builder.append("import jakarta.validation.constraints.NotNull;\n");
		builder.append("import jakarta.validation.constraints.Size;\n");
		builder.append("import com.fasterxml.jackson.annotation.JsonProperty;\n");
		builder.append("import com.liq.module.common.annotations.ModelClassMapper;\n");
		builder.append("import com.liq.module.common.annotations.ModelFieldMapper;\n");
		builder.append("import lombok.AllArgsConstructor;\n");
		builder.append("import lombok.Builder;\n");
		builder.append("import lombok.NoArgsConstructor;\n");
		builder.append("import lombok.ToString;\n");
		
		rootMap.entrySet().stream().filter(m-> !(m.getValue() instanceof Map))
		.filter(m1 -> "LiqDate".equalsIgnoreCase(getDataTypeModularFieldMapper(m1)))
		.forEach(m -> {
			builder.append("import java.time.LocalDate;\n");
			builder.append("import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;\n");
			builder.append("import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;\n");
			builder.append("import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;\n");
		});
		
		rootMap.entrySet().stream()
		.filter(map -> map.getValue() instanceof Map)
		.forEach(map -> {
			builder.append("import ").append(packageName).append(".").append(map.getKey()).append(";\n");
		});
		return builder.append("\n\n");
	}

	public StringBuilder createModularBOClassStructure(StringBuilder builder, Map<String, Object> rootMap, String rootClassName, String packageName, String type) {
		if(!"queryRequest".equalsIgnoreCase(type)) {
			builder.append("@Schema(description = ").append("\"${").append(rootClassName.toLowerCase()).append(".description}\"").append(",  title = ").append("\"").append(rootClassName).append("\")\n");
		}
		builder.append("@ModelClassMapper(name=\"").append(rootClassName).append("\", className = \"").append(rootClassName).append("\")\n");
		builder.append("@ToString\n");
		builder.append("@Builder\n");
		builder.append("@NoArgsConstructor\n");
		builder.append("@AllArgsConstructor\n");
		builder.append("public class ").append(rootClassName).append("{\n\n");
		rootMap.entrySet().stream().filter(m-> !("version").equalsIgnoreCase(m.getKey()))
		.filter(m-> !("isList").equalsIgnoreCase(m.getKey()))
		.forEach(m ->{
			if(m.getValue() instanceof String) {
				if(!"queryRequest".equalsIgnoreCase(type)) {
					getSchemaDetails(builder,m,rootClassName);
					getSize(builder,m);
					getNotNull(builder,m);
				}
			}
			getModularFieldMapper(builder,m,packageName);
			getJsonProperty(builder,getName(m.getKey()));
			getAttributeDetails(builder,m);
		});
		rootMap.entrySet().stream()
		.filter(m-> !("version").equalsIgnoreCase(m.getKey()))
		.filter(m-> !("isList").equalsIgnoreCase(m.getKey()))
		.forEach(m ->{
			addGettersSetters(builder,m);
		});
		builder.append("}");
		return builder;
	}


	public void getSchemaDetails(StringBuilder builder, Entry<String, Object> m,String rootClassName) {
		builder.append("\t@Schema(name=\"")
			   .append(getName(m.getKey()))
		       .append("\", example = \"${").append(rootClassName.toLowerCase()).append(".").append(m.getKey()).append(".example}\"")
		       .append(", required = ").append(getRequired(m))
		       .append(", description = \"${").append(rootClassName.toLowerCase()).append(".").append(m.getKey().toLowerCase()).append(".description}\")\n");
	}
	
	public String getName(String key) {
		String value = null;
		int counter = 0;
		for(char c : key.toCharArray()) {
			if(Character.isUpperCase(c)) {
				key = key.replace(String.valueOf(key.charAt(counter)), String.valueOf(key.charAt(counter)).toLowerCase());
				++counter;
			}else {
				break;
			}
		}
		return key;
	}
	
	public String getRequired(Entry<String, Object> m) {
		String value = "false";
		
		if(m.getValue() instanceof String) {
			value = getRequiredValueForStringType(m.getValue());
		}else {
			value = "false";
//			Map<String,Object> map = (Map<String, Object>) m.getValue();
//			if(map.get(m.getKey()) instanceof String) {
//				value = getRequiredValueForStringType(map.get(m.getKey()));
//			}
		}
		return value;
	}
	
	public String getRequiredValueForStringType(Object object) {
		String value = (String) object;
		String[] tokens = value.split(",");
		for(String v : tokens) {
			if(v.contains("reqd")) {
				String[] reqValueToken = v.split("=");
				value = reqValueToken[1];
			}
		}
		return "Y".equalsIgnoreCase(value)?"true":"false";
	}

	public void getSize(StringBuilder builder, Entry<String, Object> m) {
		if(m.getValue() instanceof String && hasSize((String)m.getValue())) {
			builder.append("\t@Size(min = ").append(getMin(m)).append(",max= ").append(getMax(m)).append(")\n");
			//System.out.println(builder.toString());
		}
		
	}
	
	public boolean hasSize(String value) {
		String[] tokens = value.split(",");
		for(String v : tokens) {
			if(v.contains("size")) {
				return true;
			}
		}
		return false;
	}

	public Object getMax(Entry<String, Object> m) {
		String max = "0";
		if(m.getValue() instanceof String) {
			max = getMaxForStringType(m.getValue());
		}else {
			Map<String,Object> map = (Map<String, Object>) m.getValue();
			if(map.get(m.getKey()) instanceof String) {
				max = getMaxForStringType(map.get(m.getKey()));
			}
		}
		return max;
	}
	
	public String getMaxForStringType(Object object) {
		String value = "0";
		String[] tokens = ((String) object).split(",");
		for(String v : tokens) {
			if(v.contains("size")) {
				String[] reqValueToken = v.split("=");
				String value1 = reqValueToken[1];
				String[] value1Tokens = value1.split("-");
				value = value1Tokens[1];
				break;
			}
		}
		return value;
	}

	public Object getMin(Entry<String, Object> m) {
		String min = "0";
		if(m.getValue() instanceof String) {
			min = getMinForStringType(m.getValue());
		}else {
			Map<String,Object> map = (Map<String, Object>) m.getValue();
			if(map.get(m.getKey()) instanceof String) {
				min = getMinForStringType(map.get(m.getKey()));
			}
		}
		return min;
	}
	
	public String getMinForStringType(Object object) {
		String value ="0";
		String[] tokens = ((String) object).split(",");
		for(String v : tokens) {
			if(v.contains("size")) {
				String[] reqValueToken = v.split("=");
				String value1 = reqValueToken[1];
				String[] value1Tokens = value1.split("-");
				value = value1Tokens[0];
				break;
			}
		}
		return value;
	}

	public void getNotNull(StringBuilder builder, Entry<String, Object> m) {
		if("true".equalsIgnoreCase(getRequired(m))) {
			builder.append("\t@NotNull\n");
		}
	}

	public void getModularFieldMapper(StringBuilder builder, Entry<String, Object> m, String packageName) {
		if(m.getValue() instanceof String) {
			builder.append("\t@ModelFieldMapper(name=\"").append(m.getKey()).append("\", type=\"").append(getDataTypeModularFieldMapper(m)).append("\", isMandatory=").append(getRequired(m)).append(")\n");
		}else {
			builder.append("\t@ModelFieldMapper(name=\"").append(m.getKey()).append("\", className=\"").append(packageName).append(".").append(m.getKey()).append("\" , type=\"").append(getDataTypeModularFieldMapper(m)).append("\", isMandatory=").append(getRequired(m)).append(")\n");
		}
		
	}
	
	public String getDataTypeModularFieldMapper(Entry<String, Object> m) {
		if(m.getValue() instanceof String) {
			String[] tokens = ((String) m.getValue()).split(",");
			for(String v : tokens) {
				if(v.startsWith("type")) {
					String[] reqValueToken = v.split("=");
					String value1 = reqValueToken[1];
					if("Boolean".equalsIgnoreCase(value1)) {
						return "Boolean";
					}else if("Alphanumeric".equalsIgnoreCase(value1)) {
						return "String";
					}else if("Numeric".equalsIgnoreCase(value1)) {
						return "Money";
					}else if("Date".equalsIgnoreCase(value1)) {
						return "LiqDate";
					}else if("Timestamp".equalsIgnoreCase(value1)) {
						return "LocalDateTime";
					}else if("Integer".equalsIgnoreCase(value1)) {
						return "Integer";
					}
				}
			}
		}else {
			Map<String,Object> map = (Map<String, Object>) m.getValue();
			for(Map.Entry<String, Object> entry : map.entrySet()) {
				if(entry.getKey().equalsIgnoreCase("isList")) {
					if("true".equalsIgnoreCase((String)entry.getValue())) {
						return "List";
					}else {
						 return "Object";
					}
					
				}
			}
		}
		return null;
	}
	

	public void getJsonProperty(StringBuilder builder, String name) {
		builder.append("\t@JsonProperty(\"").append(name).append("\")\n");
	}
	
	public void getAttributeDetails(StringBuilder builder, Entry<String, Object> m) {
		builder.append("\tprivate ").append(getDataType(m)).append(" ").append(getName(m.getKey())).append(";\n\n");
	}
	
	public void addGettersSetters(StringBuilder builder, Entry<String, Object> m) {
		builder.append("\tpublic ").append(getDataType(m)).append(" get").append(StringUtils.capitalize(getName(m.getKey()))).append("(){\n");
		builder.append(" \t return ").append(getName(m.getKey())).append(";\n");
		builder.append("\t}\n\n");
		
		builder.append("\tpublic ").append("void ").append("set").append(StringUtils.capitalize(getName(m.getKey()))).append("(").append(getDataType(m)).append(" ").append(getName(m.getKey())).append("){\n");
		builder.append(" \t this.").append(getName(m.getKey())).append(" = ").append(getName(m.getKey())).append(";\n");
		builder.append("\t}\n\n");
	}
	
	public String getDataType(Entry<String, Object> m) {
		if(m.getValue() instanceof String) {
			String[] tokens = ((String) m.getValue()).split(",");
			for(String v : tokens) {
				if(v.startsWith("type")) {
					String[] reqValueToken = v.split("=");
					String value1 = reqValueToken[1];
					if("Boolean".equalsIgnoreCase(value1)) {
						return "Boolean";
					}else if("Alphanumeric".equalsIgnoreCase(value1)) {
						return "String";
					}else if("Numeric".equalsIgnoreCase(value1)) {
						return "BigDecimal";
					}else if("Date".equalsIgnoreCase(value1)) {
						return "LocalDate";
					}else if("Timestamp".equalsIgnoreCase(value1)) {
						return "LocalDateTime";
					}else if("Integer".equalsIgnoreCase(value1)) {
						return "Integer";
					}
				}
			}
		}else {
			Map<String,Object> map = (Map<String, Object>) m.getValue();
			for(Map.Entry<String, Object> entry : map.entrySet()) {
				if(entry.getKey().equalsIgnoreCase("isList")) {
					if("true".equalsIgnoreCase((String)entry.getValue())) {
						return "List<".concat(m.getKey()).concat(">");
					}else {
						 return m.getKey();
					}
					
				}
			}
		}
		return null;
	}

	

	public String getXMLFile(String pathToXML) {
		String data = null;
	    Path path = Paths.get(pathToXML);
		try {
			Stream<String> lines = Files.lines(path);
		    data = lines.collect(Collectors.joining("\n"));
		    lines.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    return data;
	}

}
