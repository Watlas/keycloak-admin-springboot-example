package com.keycloakadmin.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RealmRoleDTO {

    private String id;
    private String name;
    private String containerId;

}
