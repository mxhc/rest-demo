package com.smort.api.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VendorDTO {

    @NotBlank(message = "{vendor.name.blank}")
    @Size(min = 2, message = "{vendor.name.minsize}")
    @ApiModelProperty(value = "Vendor Name", required = true, example = "Sveze Voce d.o.o.")
    private String name;

    @ApiModelProperty(hidden = true)
    @JsonProperty("vendor_url")
    private String vendorUrl;

}
