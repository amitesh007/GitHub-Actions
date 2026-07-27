package com.finastra.integrationapi.tool.controller;

import com.finastra.integrationapi.tool.ExcelReader;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finastra.integrationapi.tool.model.IntegrationAPI;
import com.finastra.integrationapi.tool.utility.ConverterXMLToModularBO;



@RestController
@RequestMapping("/tools")
public class IntegrationAPIController {
	
	@PostMapping(value = "/generateAllFiles", consumes = "application/json", produces = "application/json")
	public String createAllFiles(@RequestBody IntegrationAPI integrationAPI) {
		//ConverterXMLToModularBO.INSTANCE.executeAll(integrationAPI);
		ExcelReader.execute(integrationAPI.getPath());
		return "completed";
	}

}
