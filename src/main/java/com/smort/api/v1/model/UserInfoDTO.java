package com.smort.api.v1.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDTO {

    @ApiModelProperty(value = "user name", required = true, example = "someusername", position = 0)
    @Size(min = 3, message = "{user.name.minsize}")
    private String userName;

    @ApiModelProperty(required = true, example = "somePassword", value = "password", position = 1)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String clearPassword;

    @ApiModelProperty(position = 6)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<RoleDTO> roles;

    @ApiModelProperty(required = true, example = "Petar", value = "first name", position = 2)
    private String firstName;

    @ApiModelProperty(required = true, example = "Stojic", value = "last name", position = 3)
    private String lastName;

    @ApiModelProperty(required = false, example = "Zimbabwe", value = "country", position = 4)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String country;

    @ApiModelProperty(required = true, example = "someemail@mail.com", value = "email", position = 5)
    private String email;

    @ApiModelProperty(readOnly = true)
    private boolean enabled;

    @JsonProperty("user_url")
    @ApiModelProperty(readOnly = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String userUrl;

    public UserInfoDTO(String userName, String firstName, String lastName) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
    }


}
