package com.smort.api.v1.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductsMetaDTO {

    @ApiModelProperty(readOnly = true, example = "55")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Long count;

    @ApiModelProperty(readOnly = true, example = "30")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Integer limit;

    @ApiModelProperty(readOnly = true, example = "1")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Integer page;

    @ApiModelProperty(readOnly = true, example = "/api/v1/products/?page=1&limit=30")
    @JsonProperty(value = "next_url")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String nextUrl;

}
