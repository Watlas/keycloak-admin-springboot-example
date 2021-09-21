package com.keycloakadmin.controller;

import com.keycloakadmin.DTO.UserDTO;
import com.keycloakadmin.service.KeycloakService;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = "/keycloak")
@RestController
@RequiredArgsConstructor
public class KeycloakController {

    private final KeycloakService keycloakService;

    /**
     * Method create user in keycloak.
     *
     * @param userDTO
     * @return user created
     * @throws Exception
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO) throws Exception {
        UserDTO user = keycloakService.createUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    /**
     * Method send email of verification in mail user.
     *
     * @param id
     * @return
     * @throws Exception
     */
    @PostMapping("email_verify/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> sendEmailVerify(@PathVariable String id) throws Exception {
        keycloakService.sendEmailVerify(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Method update user in keycloak.
     *
     * @param userDTO
     * @return
     * @throws Exception
     */
    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> updateUser(@RequestBody UserDTO userDTO) throws Exception {
        UserDTO user = keycloakService.updateUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    /**
     * method update password user in keycloak
     *
     * @param userCredentials
     * @return
     * @throws Exception
     */
    @PutMapping("/update_password")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> updatePassword(@RequestBody CredentialRepresentation userCredentials) throws Exception {
        keycloakService.updatePassword(userCredentials);
        return ResponseEntity.noContent().build();
    }

    /**
     * Method send email forgot password in mail user.
     *
     * @param id
     * @return
     * @throws Exception
     */
    @PutMapping("/forgot_password/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> forgotPassword(@PathVariable String id) throws Exception {
        keycloakService.forgotPassword(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Method resets all user tokens.
     *
     * @param id
     * @return
     * @throws Exception
     */
    @PutMapping("/logout/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> logoutUser(@PathVariable String id) throws Exception {
        keycloakService.logout(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Method return all users in keycloak
     *
     * @return
     * @throws Exception
     */
    @GetMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> getUsers() throws Exception {
        return ResponseEntity.ok().body(keycloakService.getAllUsers());
    }

    /**
     * return user by Id.
     *
     * @param id
     * @return
     * @throws Exception
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> getUserById(@PathVariable String id) throws Exception {
        return ResponseEntity.ok().body(keycloakService.getUserById(id));
    }

    /**
     * delete user by Id.
     *
     * @param id
     * @return
     * @throws Exception
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> deleteUser(@PathVariable String id) throws Exception {
        keycloakService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * add role in user by id.
     *
     * @param userId
     * @param role
     * @return
     * @throws Exception
     */
    @PostMapping("/assingnee-role")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> assingneeRole(@RequestParam("userId") String userId, @RequestParam("role") String role) throws Exception {
        keycloakService.assigneeRole(userId, role);
        return ResponseEntity.noContent().build();
    }

    /**
     * remove role by ID user.
     *
     * @param userId
     * @param role
     * @return
     * @throws Exception
     */
    @DeleteMapping("/assingnee-role/remove")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> removeAssingneeRole(@RequestParam("userId") String userId, @RequestParam("role") String role) throws Exception {
        keycloakService.removeRole(userId, role);
        return ResponseEntity.noContent().build();
    }

    /**
     * return all roles by user Id.
     *
     * @param id
     * @return
     * @throws Exception
     */
    @GetMapping("/assingnee-role/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> getAssingneeRoleByUser(@PathVariable String id) throws Exception {
        return ResponseEntity.ok().body(keycloakService.getAssignedRolesByUser(id));
    }

    /**
     * return token access user.
     *
     * @param userDTO
     * @return
     * @throws Exception
     */
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> login(UserDTO userDTO) throws Exception {
        return ResponseEntity.ok().body(keycloakService.login(userDTO));
    }

}
