
package com.finastra.integrationapi.tool.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "integrationAPIName",
    "ipFilePath",
    "opFilePath",
    "requestPackageName",
    "responsePackageName",
    "commonPackageName",
    "createRequestFileName",
    "updateRequestFileName",
    "queryRequestFileName",
    "deleteRequestFileName",
    "cancelRequestFileName",
    "searchRequestFileName",
    "createResponseFileName",
    "updateResponseFileName",
    "queryResponseFileName",
    "deleteResponseFileName",
    "cancelResponseFileName",
    "searchResponseFileName"
})

public class Modular {

    @JsonProperty("integrationAPIName")
    private String integrationAPIName;
    @JsonProperty("ipFilePath")
    private String ipFilePath;
    @JsonProperty("opFilePath")
    private String opFilePath;
    @JsonProperty("requestPackageName")
    private String requestPackageName;
    @JsonProperty("responsePackageName")
    private String responsePackageName;
    @JsonProperty("commonPackageName")
    private String commonPackageName;
    @JsonProperty("createRequestFileName")
    private String createRequestFileName;
    @JsonProperty("updateRequestFileName")
    private String updateRequestFileName;
    @JsonProperty("queryRequestFileName")
    private String queryRequestFileName;
    
    @JsonProperty("deleteRequestFileName")
    private String deleteRequestFileName;
    @JsonProperty("cancelRequestFileName")
    private String cancelRequestFileName;
    
    @JsonProperty("searchRequestFileName")
    private String searchRequestFileName;
    @JsonProperty("createResponseFileName")
    private String createResponseFileName;
    @JsonProperty("updateResponseFileName")
    private String updateResponseFileName;
    @JsonProperty("queryResponseFileName")
    private String queryResponseFileName;
    
    @JsonProperty("deleteResponseFileName")
    private String deleteResponseFileName;
    @JsonProperty("cancelResponseFileName")
    private String cancelResponseFileName;
    
    @JsonProperty("searchResponseFileName")
    private String searchResponseFileName;

    @JsonProperty("integrationAPIName")
    public String getIntegrationAPIName() {
        return integrationAPIName;
    }

    @JsonProperty("integrationAPIName")
    public void setIntegrationAPIName(String integrationAPIName) {
        this.integrationAPIName = integrationAPIName;
    }

    @JsonProperty("ipFilePath")
    public String getIpFilePath() {
        return ipFilePath;
    }

    @JsonProperty("ipFilePath")
    public void setIpFilePath(String ipFilePath) {
        this.ipFilePath = ipFilePath;
    }

    @JsonProperty("opFilePath")
    public String getOpFilePath() {
        return opFilePath;
    }

    @JsonProperty("opFilePath")
    public void setOpFilePath(String opFilePath) {
        this.opFilePath = opFilePath;
    }

    @JsonProperty("requestPackageName")
    public String getRequestPackageName() {
        return requestPackageName;
    }

    @JsonProperty("requestPackageName")
    public void setRequestPackageName(String requestPackageName) {
        this.requestPackageName = requestPackageName;
    }

    @JsonProperty("responsePackageName")
    public String getResponsePackageName() {
        return responsePackageName;
    }

    @JsonProperty("responsePackageName")
    public void setResponsePackageName(String responsePackageName) {
        this.responsePackageName = responsePackageName;
    }

    @JsonProperty("commonPackageName")
    public String getCommonPackageName() {
        return commonPackageName;
    }

    @JsonProperty("commonPackageName")
    public void setCommonPackageName(String commonPackageName) {
        this.commonPackageName = commonPackageName;
    }

    @JsonProperty("createRequestFileName")
    public String getCreateRequestFileName() {
        return createRequestFileName;
    }

    @JsonProperty("createRequestFileName")
    public void setCreateRequestFileName(String createRequestFileName) {
        this.createRequestFileName = createRequestFileName;
    }

    @JsonProperty("updateRequestFileName")
    public String getUpdateRequestFileName() {
        return updateRequestFileName;
    }

    @JsonProperty("updateRequestFileName")
    public void setUpdateRequestFileName(String updateRequestFileName) {
        this.updateRequestFileName = updateRequestFileName;
    }

    @JsonProperty("queryRequestFileName")
    public String getQueryRequestFileName() {
        return queryRequestFileName;
    }

    @JsonProperty("queryRequestFileName")
    public void setQueryRequestFileName(String queryRequestFileName) {
        this.queryRequestFileName = queryRequestFileName;
    }

    @JsonProperty("createResponseFileName")
    public String getCreateResponseFileName() {
        return createResponseFileName;
    }

    @JsonProperty("createResponseFileName")
    public void setCreateResponseFileName(String createResponseFileName) {
        this.createResponseFileName = createResponseFileName;
    }

    @JsonProperty("updateResponseFileName")
    public String getUpdateResponseFileName() {
        return updateResponseFileName;
    }

    @JsonProperty("updateResponseFileName")
    public void setUpdateResponseFileName(String updateResponseFileName) {
        this.updateResponseFileName = updateResponseFileName;
    }

    @JsonProperty("queryResponseFileName")
    public String getQueryResponseFileName() {
        return queryResponseFileName;
    }

    @JsonProperty("queryResponseFileName")
    public void setQueryResponseFileName(String queryResponseFileName) {
        this.queryResponseFileName = queryResponseFileName;
    }

	public String getSearchRequestFileName() {
		return searchRequestFileName;
	}

	public void setSearchRequestFileName(String searchRequestFileName) {
		this.searchRequestFileName = searchRequestFileName;
	}

	public String getSearchResponseFileName() {
		return searchResponseFileName;
	}

	public void setSearchResponseFileName(String searchResponseFileName) {
		this.searchResponseFileName = searchResponseFileName;
	}

	public String getDeleteRequestFileName() {
		return deleteRequestFileName;
	}

	public void setDeleteRequestFileName(String deleteRequestFileName) {
		this.deleteRequestFileName = deleteRequestFileName;
	}

	public String getCancelRequestFileName() {
		return cancelRequestFileName;
	}

	public void setCancelRequestFileName(String cancelRequestFileName) {
		this.cancelRequestFileName = cancelRequestFileName;
	}

	public String getDeleteResponseFileName() {
		return deleteResponseFileName;
	}

	public void setDeleteResponseFileName(String deleteResponseFileName) {
		this.deleteResponseFileName = deleteResponseFileName;
	}

	public String getCancelResponseFileName() {
		return cancelResponseFileName;
	}

	public void setCancelResponseFileName(String cancelResponseFileName) {
		this.cancelResponseFileName = cancelResponseFileName;
	}

}
