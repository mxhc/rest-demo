package com.smort.api.v1.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserListDTO {

    private MetaDTO meta;

    private List<UserInfoDTO> users;

}
