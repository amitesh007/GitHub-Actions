package com.finastra.integrationapi.tool.engine;

import com.finastra.integrationapi.tool.model.ExcelCommonData;
import com.finastra.integrationapi.tool.model.ExcelData;
import com.finastra.integrationapi.tool.utility.CommonSwaggerUtility;
import com.finastra.integrationapi.tool.utility.CommonUtility;

import java.util.List;

public enum ControllerSwaggerEngine {
    INSTANCE;

    public String genrateSwaggerAPIClass(List<ExcelData> dataList, ExcelCommonData data, Boolean hasSoap){
        StringBuilder builder = new StringBuilder();
        CommonSwaggerUtility.INSTANCE.insertDisclaimer(builder, data, hasSoap);
        insertPackageAndImports( builder, data, hasSoap);
        CommonUtility.INSTANCE.insertClassHeader(builder, data, hasSoap);
        insertInstanceVariables(builder, data, hasSoap);
        insertPostMethod(builder, data, hasSoap);
        insertPutMethod(builder, data, hasSoap);
        insertGetMethod(builder, data, hasSoap);
        CommonUtility.INSTANCE.insertFinalCloseBracket( builder, data, hasSoap);
        return builder.toString();
    }

    public void insertPackageAndImports(StringBuilder builder, ExcelCommonData data, Boolean hasSoap) {
        //Insert package name
        builder.append("package ").append(data.getPackageName()).append(";\n\n");
        //Insert all the required Imports
        builder.append("import import com.finastra.liq.module.data.response.ResponseData;\n");
        builder.append("import ").append(data.getPackageName()).append(".request.*;\n");
        builder.append("import ").append(data.getPackageName()).append(".common.*;\n");
        builder.append("import ").append(data.getPackageName()).append(".response.*;\n");
        builder.append("import com.finastra.liq.module.util.core.model.Request;\n");
        builder.append("import com.finastra.liq.module.util.log.LoggingManager;\n");
        builder.append("import com.liq.module.common.controller.ApiBaseController;\n");
        builder.append("import com.liq.module.common.utils.ConstantsUtil;\n");
        builder.append("import com.liq.module.error.handler.ErrorResponse;\n");
        builder.append("import com.liq.module.integration.service.IntegrationService;\n");
        builder.append("import io.swagger.v3.oas.annotations.Operation;\n");
        builder.append("import io.swagger.v3.oas.annotations.Parameter;\n");
        builder.append("import io.swagger.v3.oas.annotations.media.Content;\n");
        builder.append("import io.swagger.v3.oas.annotations.media.Schema;\n");
        builder.append("import io.swagger.v3.oas.annotations.responses.ApiResponse;\n");
        builder.append("import io.swagger.v3.oas.annotations.responses.ApiResponses;\n");
        builder.append("import io.swagger.v3.oas.annotations.tags.Tag;\n");
        builder.append("import jakarta.validation.constraints.NotNull;\n");
        builder.append("import org.springframework.beans.factory.annotation.Autowired;\n");
        builder.append("import org.springframework.beans.factory.annotation.Value;\n");
        builder.append("import org.springframework.http.HttpHeaders;\n");
        builder.append("import org.springframework.http.ResponseEntity;\n");
        builder.append("import org.springframework.web.bind.annotation.GetMapping;\n");
        builder.append("import org.springframework.web.bind.annotation.PathVariable;\n");
        builder.append("import org.springframework.web.bind.annotation.PostMapping;\n");
        builder.append("import org.springframework.web.bind.annotation.PutMapping;\n");
        builder.append("import org.springframework.web.bind.annotation.RequestBody;\n");
        builder.append("import org.springframework.web.bind.annotation.RequestHeader;\n");
        builder.append("import org.springframework.web.bind.annotation.RequestMapping;\n");
        builder.append("import java.util.List;\n");
        builder.append("import static com.liq.module.common.utils.ConstantsUtil.HTTP_HEADER_LIQ_CORRELATION_ID;\n");
        builder.append("\n");
    }

    public void insertClassHeader(StringBuilder builder, ExcelCommonData data, Boolean hasSoap, Boolean isResponse) {
       builder.append("@RequestMapping(\"/").append(transformWord(data.getIntegrationApiClassName())).append("/v1\")");
       builder.append("@ApiResponses(value = {\n");
       builder.append("@ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = \"400\", description = \"Invalid input provided or bad request\"),\n");
       builder.append("@ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = \"401\", description = \"Unauthorized\"),\n");
       builder.append("@ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = \"403\", description = \"Forbidden\"),\n");
       builder.append("@ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = \"404\", description = \"Resource not found\"),\n");
       builder.append("@ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = \"409\", description = \"Conflict\"),\n");
       builder.append("@ApiResponse(content = @Content(schema = @Schema(implementation = ErrorResponse.class)), responseCode = \"500\", description = \"Internal Server Error\"),\n");
       builder.append("});\n");

       builder.append("public class ").append(transformWordToAddController(data.getIntegrationApiClassName())).append(" extends ApiBaseController ").append("{\n");
    }

    public void insertInstanceVariables(StringBuilder builder, ExcelCommonData data, Boolean hasSoap) {
        builder.append("@Value(\"${externalIdp.tokenValidation}\")\n");
        builder.append("private String tokenValidation;\n\n");
        builder.append("@Autowired\n");
        builder.append("private IntegrationService service;\n\n");
    }

    private void insertPostMethod(StringBuilder builder, ExcelCommonData data, Boolean hasSoap) {
        builder.append("\t@PostMapping(value = \"/create\", produces = \"application/json\", consumes = \"application/json\")\n");
        builder.append("\t@Operation(operationId = \"").append(getModuleName(data.getIntegrationApiClassName())).append("\", description = \"Creates a new instance of ").append(getModuleName(data.getIntegrationApiClassName())).append("\" , summary = \"Create ").append(getModuleName(data.getIntegrationApiClassName())).append(" using core api\")\n");
        builder.append("\t@ApiResponse(responseCode = \"200\", description = \"Creates ").append(getModuleName(data.getIntegrationApiClassName())).append(" using core api\", content = @Content(mediaType = \"application/json\", schema = @Schema(implementation = ").append(data.getIntegrationApiClassName()).append(".class)))\n");
        builder.append("\t@Tag(name = \"Create  ").append(getModuleName(data.getIntegrationApiClassName())).append("\")\n");
        builder.append("\tpublic ResponseEntity<?> ").append(getModuleName(data.getIntegrationApiClassName()).substring(0,1)).append(getModuleName(data.getIntegrationApiClassName()).substring(01)).append("(@RequestBody ").append(data.getIntegrationApiClassName()).append(" body,\n");
        builder.append("\t\t\t\t@RequestHeader(value = \"Idempotency-Key\", defaultValue = \"\") @Schema(description = \"Idempotency key will be valid for 24 hours.\", maxLength = 50) String idempotencyKey,\n");
        builder.append("\t\t\t\t@RequestHeader(\"Authorization\") @Schema(description = \"Authorization token\") String token,\n");
        builder.append("\t\t\t\t@RequestHeader(HTTP_HEADER_LIQ_CORRELATION_ID) @Schema(description = \"Correlation Id\") String xCorrelationId) {\n");
        builder.append("\t\tLoggingManager.logDebug(\"Request").append(getModuleName(data.getIntegrationApiClassName())).append(" Data for Create : \" + body);\n");
        builder.append("\t\tthis.setCorrelationId(xCorrelationId);\n");
        builder.append("\t\tthis.runIdempotencyKeyValidation(idempotencyKey,false);\n");
        builder.append("\t\tthis.runTokenValidation(token, tokenValidation,false);\n");
        builder.append("\t\ttoken = this.generateOrForwardB2BToken(token,false);\n");
        builder.append("\t\tRequest request = this.generateRequestForIntegrationApi(body, ConstantsUtil.INTEGRATION_APP_ID, idempotencyKey, token, null, null,\n");
        builder.append("\t\t\t\t\"").append(getModuleName(data.getIntegrationApiClassName())).append("\", \"createliqdata\", null,xCorrelationId, null,false, null, null);\n");
        builder.append("\t\tResponseData responseData = this.primitiveExecute(request, service);\n");
        builder.append("\t\t").append(data.getIntegrationApiClassName()).append("Response response = (").append(data.getIntegrationApiClassName()).append("Response) this.generateModel(responseData,\n" +
                "\t\t\t\t").append(data.getIntegrationApiClassName()).append("Response.class);\n");
        builder.append("\t\tHttpHeaders responseHeaders = new HttpHeaders();\n");
        builder.append("\t\tresponseHeaders.set(HTTP_HEADER_LIQ_CORRELATION_ID, \n" +
                "\t\t\t\tcorrelationResponseHeader(responseData));\n");
        builder.append("\t\treturn ResponseEntity.ok().headers(responseHeaders).body(response);\n");
        builder.append("\t}\n\n");
    }

    private void insertPutMethod(StringBuilder builder, ExcelCommonData data, Boolean hasSoap) {
        builder.append("\t@PutMapping(value = \"/update\", produces = \"application/json\", consumes = \"application/json\")\n");
        builder.append("\t@Operation(operationId = \"").append(getModuleName(data.getIntegrationApiClassName())).append("\", description = \"Update ").append(getModuleName(data.getIntegrationApiClassName())).append(" using Core Api\", summary = \"Update ").append(getModuleName(data.getIntegrationApiClassName())).append(" using core api\")\n");
        builder.append("\t@ApiResponse(responseCode = \"200\", description = \"Update ").append(getModuleName(data.getIntegrationApiClassName())).append(" using core api\", content = @Content(mediaType = \"application/json\", schema = @Schema(implementation = Update").append(getModuleName(data.getIntegrationApiClassName())).append("Integration.class)))\n");
        builder.append("\t@Tag(name = \"Update  ").append(getModuleName(data.getIntegrationApiClassName())).append("\")\n");
        builder.append("\tpublic ResponseEntity<?> update").append(getModuleName(data.getIntegrationApiClassName())).append("(\n");
        builder.append("\t        @RequestHeader(value = \"If-Match\", defaultValue = \"\") @Schema(description = \"For updates this header needs to be present and contain an ETag value\", maxLength = 50, required = true) String ifMatch,\n");
        builder.append("\t        @RequestBody Update").append(getModuleName(data.getIntegrationApiClassName())).append("Integration body,\n");
        builder.append("\t        @RequestHeader(\"Authorization\") @Schema(description = \"Authorization token\") String token,\n");
        builder.append("\t        @RequestHeader(HTTP_HEADER_LIQ_CORRELATION_ID) @Schema(description = \"Correlation Id\") String xCorrelationId) {\n");
        builder.append("\n");
        builder.append("\t    LoggingManager.logDebug(\"Request ").append(getModuleName(data.getIntegrationApiClassName())).append(" Data for Update : \" + body);\n");
        builder.append("\t    this.validateIfMatch(ifMatch);\n");
        builder.append("\t    this.setCorrelationId(xCorrelationId);\n");
        builder.append("\t    this.runTokenValidation(token, tokenValidation,false);\n");
        builder.append("\t    token = this.generateOrForwardB2BToken(token,false);\n");
        builder.append("\t    Request request = this.generateRequestForIntegrationApi(body, ConstantsUtil.INTEGRATION_APP_ID, null, token, null, null, \"").append(getModuleName(data.getIntegrationApiClassName())).append("\",\n");
        builder.append("\t            \"updateliqdata\", null,xCorrelationId, null,false,ifMatch, null);\n");
        builder.append("\t    ResponseData responseData = this.primitiveExecute(request, service);\n");
        builder.append("\t    LoggingManager.logDebug(\"Response Data from Core API : \" + responseData);\n");
        builder.append("\t    this.runResponseDataValidation(responseData);\n");
        builder.append("\t    Update").append(getModuleName(data.getIntegrationApiClassName())).append("IntegrationResponse response = (Update").append(getModuleName(data.getIntegrationApiClassName())).append("IntegrationResponse) this.generateModel(responseData,\n");
        builder.append("\t            Update").append(getModuleName(data.getIntegrationApiClassName())).append("IntegrationResponse.class);\n");
        builder.append("\t    HttpHeaders responseHeaders = new HttpHeaders();\n");
        builder.append("\t    responseHeaders.set(HTTP_HEADER_LIQ_CORRELATION_ID,\n");
        builder.append("\t            correlationResponseHeader(responseData));\n");
        builder.append("\t    return ResponseEntity.ok().headers(responseHeaders).body(response);\n");
        builder.append("\t}\n\n");
    }

    private void insertGetMethod(StringBuilder builder, ExcelCommonData data, Boolean hasSoap) {
        builder.append("\t@GetMapping(value = \"/loan-transaction-id/{loanTransactionId}\", produces = \"application/json\")\n");
        builder.append("\t@Operation(operationId = \"get-").append(getModuleName(data.getIntegrationApiClassName())).append("-loanTransactionId\", description = \"Fetches ").append(getModuleName(data.getIntegrationApiClassName())).append(" from Core Api based on customer externalId \", summary = \"Get ").append(getModuleName(data.getIntegrationApiClassName())).append(" using loanTransactionId\")\n");
        builder.append("\t@ApiResponse(responseCode = \"200\", description = \"Fetches a ").append(getModuleName(data.getIntegrationApiClassName())).append(" based on ").append(getModuleName(data.getIntegrationApiClassName())).append(" loanTransactionId \", content = @Content(mediaType = \"application/json\", schema = @Schema(implementation = Query").append(getModuleName(data.getIntegrationApiClassName())).append("Integration.class)))\n");
        builder.append("\t@Tag(name = \"Get ").append(getModuleName(data.getIntegrationApiClassName())).append(" By loanTransactionId\")\n");
        builder.append("\tpublic ResponseEntity<?> get").append(getModuleName(data.getIntegrationApiClassName())).append("ByLoanTransactionId(\n");
        builder.append("\t        @NotNull @PathVariable(\"loanTransactionId\") @Parameter(required = true, description = \"loanTransactionId\") @Schema(description = \"A unique identifier sent in request.\", maxLength = 15) String loanTransactionId,\n");
        builder.append("\t        @RequestHeader(\"Authorization\") @Schema(description = \"Authorization token\") String token,\n");
        builder.append("\t        @RequestHeader(HTTP_HEADER_LIQ_CORRELATION_ID) @Schema(description = \"Correlation Id\") String xCorrelationId) {\n");
        builder.append("\n");
        builder.append("\t    LoggingManager.logDebug(\"").append(getModuleName(data.getIntegrationApiClassName())).append(" loanTransactionId for retrieval  : \" + loanTransactionId);\n");
        builder.append("\t    this.setCorrelationId(xCorrelationId);\n");
        builder.append("\t    this.runTokenValidation(token, tokenValidation,false);\n");
        builder.append("\t    token = this.generateOrForwardB2BToken(token,false);\n");
        builder.append("\t    Query").append(getModuleName(data.getIntegrationApiClassName())).append("Integration query").append(getModuleName(data.getIntegrationApiClassName())).append("Integration = Query").append(getModuleName(data.getIntegrationApiClassName())).append("Integration.builder().outstandingTransactionIdentifier(OutstandingTransactionIdentifier.builder().identifierType(\"transactionId\").identifierValue(loanTransactionId).build()).build();\n");
        builder.append("\t    Request request = this.generateRequestForIntegrationApi(query").append(getModuleName(data.getIntegrationApiClassName())).append("Integration, ConstantsUtil.INTEGRATION_APP_ID, null, token,\n");
        builder.append("\t            \"loanTransactionId\", loanTransactionId, \"").append(getModuleName(data.getIntegrationApiClassName())).append("\", \"queryliqdata\", null,xCorrelationId, null,false,null, null);\n");
        builder.append("\t    ResponseData responseData = this.primitiveExecute(request, service);\n");
        builder.append("\t    LoggingManager.logDebug(\"Response Data from Core API : \" + responseData);\n");
        builder.append("\t    this.runResponseDataValidation(responseData);\n");
        builder.append("\t    Query").append(getModuleName(data.getIntegrationApiClassName())).append("IntegrationResponse response = (Query").append(getModuleName(data.getIntegrationApiClassName())).append("IntegrationResponse) this.generateModel(responseData,\n");
        builder.append("\t            Query").append(getModuleName(data.getIntegrationApiClassName())).append("IntegrationResponse.class);\n");
        builder.append("\t    HttpHeaders responseHeaders = new HttpHeaders();\n");
        builder.append("\t    responseHeaders.set(HTTP_HEADER_LIQ_CORRELATION_ID,\n");
        builder.append("\t            correlationResponseHeader(responseData));\n");
        builder.append("\t    responseHeaders.set(HttpHeaders.ETAG, getResponseWithETag(responseData));\n");
        builder.append("\t    return ResponseEntity.ok().headers(responseHeaders).body(response);\n");
        builder.append("\t}\n\n");
    }

    public String transformWord(String input) {
        // Remove specific words
        String modifiedWord = input.replaceAll("(?i)create|update|query|integration", "");

        // Add hyphen before each uppercase letter
        modifiedWord = modifiedWord.replaceAll("([A-Z])", "-$1");

        // Convert the entire word to lowercase
        return modifiedWord.toLowerCase();
    }

    public  String transformWordToAddController(String input) {
        // Remove specific words
        String modifiedWord = input.replaceAll("(?i)create|update|query|integration", "");

        // Append the suffix "Controller"
        return modifiedWord + "Controller";
    }

    public String getModuleName(String input){
        return input.replaceAll("(?i)create|update|query|integration", "");
    }


}



