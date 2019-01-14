package com.smort.api.v1.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActionDTO {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ApiModelProperty(value = "Action url", hidden = true, example = "/api/v1/orders/13/actions/purchase")
    private String url;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ApiModelProperty(value = "Http method", hidden = true, example = "POST")
    private String method;

}
