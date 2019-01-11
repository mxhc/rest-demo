package com.smort.api.v1.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class CategoryDTO {

    @NotBlank(message = "{category.name.blank}")
    @Size(min = 2, message = "{category.name.minsize}")
    @ApiModelProperty(required = true, value = "Category name", example = "Sveze Voce", position = 0)
    private String name;

    @ApiModelProperty(example =  "/api/v1/categories/Fruits", position = 2)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("category_url")
    private String categoryUrl;

}
