package com.smort.domain;

import lombok.Getter;

@Getter
public enum RolesEnum {

    ROLE_ADMIN("ROLE_ADMIN"), ROLE_USER("ROLE_USER"), ROLE_SUPERADMIN("ROLE_SUPERADMIN");

    private String role;

    RolesEnum(String role) {
        this.role = role;
    }

}
