
package com.finastra.integrationapi.tool.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.NoArgsConstructor;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "path",
    "modular"
})

//@Builder
//@AllArgsConstructor
//@NoArgsConstructor
public class IntegrationAPI {

    @JsonProperty("path")
    private String path;

    @JsonProperty("modular")
    private Modular modular;

    @JsonProperty("path")
    public String getPath() {
        return path;
    }

    @JsonProperty("path")
    public void setPath(String path) {
        this.path = path;
    }

    @JsonProperty("modular")
    public Modular getModular() {
        return modular;
    }

    @JsonProperty("modular")
    public void setModular(Modular modular) {
        this.modular = modular;
    }

}
