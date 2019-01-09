package com.smort.api.v1.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CategoryDTO {

    @ApiModelProperty(required = true, value = "Category name", example = "Sveze Voce")
    private String name;

    @ApiModelProperty(hidden = true)
    private Long id;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("category_url")
    private String categoryUrl;

}
