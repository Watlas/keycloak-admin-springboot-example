package com.keycloakadmin.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

    private String id;
    private String email;
    private String password;
    private String firstname;
    private String lastname;
    private int statusCode;
    private String status;
    private String token;

}
