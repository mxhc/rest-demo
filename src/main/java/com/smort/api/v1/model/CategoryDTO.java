package com.smort.api.v1.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CategoryDTO {

    @ApiModelProperty(required = true, value = "Category name", example = "Sveze Voce")
    private String name;

    private Long id;

}
