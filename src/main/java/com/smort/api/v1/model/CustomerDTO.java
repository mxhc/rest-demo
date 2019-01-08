package com.smort.api.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CustomerDTO {

    @ApiModelProperty(value = "First Name", required = true, example = "Milojko")
    private String firstname;
    @ApiModelProperty(value = "Last Name", required = true, example = "Pantic")
    private String lastname;

    @ApiModelProperty(hidden = true)
    @JsonProperty("customer_url")
    private String customerUrl;
}
