package com.smort.api.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class CustomerDTO {

    @NotBlank(message = "{custome.first.name.blank}")
    @Size(min = 2, message = "{customer.first.name.minsize}")
    @ApiModelProperty(value = "First Name", required = true, example = "Milojko")
    private String firstname;

    @NotBlank(message = "{custome.last.name.blank}")
    @Size(min = 2, message = "{customer.last.name.minsize}")
    @ApiModelProperty(value = "Last Name", required = true, example = "Pantic")
    private String lastname;

    @ApiModelProperty(hidden = true)
    @JsonProperty("customer_url")
    private String customerUrl;
}
