package com.keycloakadmin.service;

import com.keycloakadmin.DTO.RealmRoleDTO;
import com.keycloakadmin.DTO.UserDTO;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.MappingsRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class KeycloakService {

    @Value("${keycloak.auth-server-url}")
    private String authServerUrl;
    @Value("${keycloak.realm}")
    private String realm;
    @Value("${password-admin}")
    private String passwordAdmin;
    @Value("${user-admin}")
    private String userAdmin;

    public UserDTO createUser(UserDTO userDTO) throws Exception {
        try {
            UsersResource usersResource = getUsersResource();
            UserRepresentation user = new UserRepresentation();
            user.setEnabled(true);
            user.setUsername(userDTO.getEmail());
            user.setFirstName(userDTO.getFirstname());
            user.setLastName(userDTO.getLastname());
            user.setEmail(userDTO.getEmail());
            user.setEmailVerified(true); //set mail verified, optional.
            Response response = usersResource.create(user);
            userDTO.setStatusCode(response.getStatus());
            userDTO.setStatus(response.getStatusInfo().toString());
            if (response.getStatus() == 201) {
                String userId = CreatedResponseUtil.getCreatedId(response);
                CredentialRepresentation passwordCred = new CredentialRepresentation();
                passwordCred.setTemporary(false);
                passwordCred.setType(CredentialRepresentation.PASSWORD);
                passwordCred.setValue(userDTO.getPassword());
                UserResource userResource = usersResource.get(userId);
                userResource.resetPassword(passwordCred);
            }
            return userDTO;
        } catch (Exception e) {
            throw new Exception("error create user keycloak: " + e.getMessage());
        }
    }

    public UserDTO createUserAndVerifyEmail(UserDTO userDTO) throws Exception {
        try {
            UsersResource usersResource = getUsersResource();
            UserRepresentation user = new UserRepresentation();
            user.setEnabled(true);
            user.setUsername(userDTO.getEmail());
            user.setFirstName(userDTO.getFirstname());
            user.setLastName(userDTO.getLastname());
            user.setEmail(userDTO.getEmail());
            user.setEmailVerified(false);
            Response response = usersResource.create(user);
            userDTO.setStatusCode(response.getStatus());
            userDTO.setStatus(response.getStatusInfo().toString());
            if (response.getStatus() == 201) {
                String userId = CreatedResponseUtil.getCreatedId(response);
                CredentialRepresentation passwordCred = new CredentialRepresentation();
                passwordCred.setTemporary(false);
                passwordCred.setType(CredentialRepresentation.PASSWORD);
                passwordCred.setValue(userDTO.getPassword());
                UserResource userResource = usersResource.get(userId);
                userResource.resetPassword(passwordCred);
                this.sendEmailVerify(userId);
            }
            return userDTO;
        } catch (Exception e) {
            throw new Exception("error create user keycloak: " + e.getMessage());
        }
    }

    public UserDTO updateUser(UserDTO userDTO) throws Exception {
        try {
            UsersResource usersResource = getUsersResource();
            UserRepresentation user = usersResource.get(userDTO.getId()).toRepresentation();
            user.setEnabled(true);
            user.setFirstName(userDTO.getFirstname());
            user.setLastName(userDTO.getLastname());
            user.setEmail(userDTO.getEmail());
            usersResource.get(user.getId()).update(user);
            return userDTO;
        } catch (Exception e) {
            throw new Exception("error update user keycloak: " + e.getMessage());
        }
    }

    public void sendEmailVerify(String idUser) throws Exception {
        try {
            UsersResource usersResource = getUsersResource();
            usersResource.get(idUser).sendVerifyEmail();
        } catch (Exception e) {
            throw new Exception("error send user verify keycloak: " + e.getMessage());
        }
    }

    public void updatePassword(CredentialRepresentation userCredentials) throws Exception {
        try {
            UsersResource usersResource = getUsersResource();
            CredentialRepresentation passwordCred = new CredentialRepresentation();
            passwordCred.setTemporary(false);
            passwordCred.setType(CredentialRepresentation.PASSWORD);
            passwordCred.setValue(userCredentials.getValue());
            UserResource userResource = usersResource.get(userCredentials.getId());
            userResource.resetPassword(passwordCred);
        } catch (Exception e) {
            throw new Exception("error update password user keycloak: " + e.getMessage());
        }
    }

    public void forgotPassword(String idUser) throws Exception {
        try {
            UsersResource usersResource = getUsersResource();
            usersResource.get(idUser).executeActionsEmail(Collections.singletonList("UPDATE_PASSWORD"));
        } catch (Exception e) {
            throw new Exception("error send email forgot passoword keycloak: " + e.getMessage());
        }
    }

    public void logout(String idUser) throws Exception {
        try {
            UsersResource usersResource = getUsersResource();
            usersResource.get(idUser).logout();
        } catch (Exception e) {
            throw new Exception("error send email forgot passoword keycloak: " + e.getMessage());
        }
    }

    public UserRepresentation getUserById(String userId) throws Exception {
        try {
            UsersResource usersResource = getUsersResource();
            UserRepresentation user = usersResource.get(userId).toRepresentation();
            if (Objects.nonNull(user))
                return user;
            else
                throw new Exception();
        } catch (Exception e) {
            throw new NotFoundException("User Not Found: User Id: " + userId);
        }
    }

    public void deleteUser(String userId) {
        try {
            UsersResource usersResource = getUsersResource();
            UserResource userResource = usersResource.get(userId);
            userResource.remove();
        } catch (Exception e) {
            throw new NotFoundException("error delete user: User Id: " + userId);
        }
    }

    public void leaveGroup(String group, String userId) {
        try {
            UsersResource usersResource = getUsersResource();
            UserResource userResource = usersResource.get(userId);
            userResource.leaveGroup(group);
        } catch (Exception e) {
            throw new NotFoundException("error leave group: User Id: " + userId);
        }
    }

    public void joinGroup(String group, String userId) {
        try {
            UsersResource usersResource = getUsersResource();
            UserResource userResource = usersResource.get(userId);
            userResource.joinGroup(group);
        } catch (Exception e) {
            throw new NotFoundException("error join group: User Id: " + userId);
        }
    }

    public void assigneeRole(String userId, String role) throws Exception {
        try {
            RealmResource realmResource = getRealmResource();
            UsersResource usersResource = getUsersResource();
            RoleRepresentation realmRoleUser = realmResource.roles().get(role).toRepresentation();
            UserResource userResource = usersResource.get(userId);
            userResource.roles().realmLevel().add(Collections.singletonList(realmRoleUser));
        } catch (Exception e) {
            throw new Exception("error assingne role user: User Id: " + userId);
        }
    }

    public void removeRole(String userId, String role) throws Exception {
        try {
            RealmResource realmResource = getRealmResource();
            UsersResource usersResource = getUsersResource();
            RoleRepresentation realmRoleUser = realmResource.roles().get(role).toRepresentation();
            UserResource userResource = usersResource.get(userId);
            userResource.roles().realmLevel().remove(Collections.singletonList(realmRoleUser));
        } catch (Exception e) {
            throw new Exception("error remove role user: User Id: " + userId);
        }
    }

    public List<UserDTO> getAllUsers() throws Exception {
        try {
            RealmResource realmResource = getRealmResource();
            List<UserRepresentation> listUsers = realmResource.users().list();
            return listUsers.stream().map(user -> {
                UserDTO userDto = new UserDTO();
                userDto.setId(user.getId());
                userDto.setFirstname(user.getFirstName());
                userDto.setLastname(user.getLastName());
                userDto.setEmail(user.getEmail());
                return userDto;
            }).collect(Collectors.toList());
        } catch (Exception e) {
            throw new Exception("error list users keyclak" + e.getMessage());

        }
    }

    public List<RealmRoleDTO> getAssigneedRolesByUser(String userId) throws Exception {
        try {
            UsersResource usersResource = getUsersResource();
            UserResource userResource = usersResource.get(userId);
            MappingsRepresentation representationRoles = userResource.roles().getAll();
            return representationRoles.getRealmMappings()
                    .stream()
                    .map(role -> {
                        RealmRoleDTO roleModel = new RealmRoleDTO();
                        roleModel.setName(role.getName());
                        roleModel.setContainerId(role.getContainerId());
                        roleModel.setId(role.getId());
                        return roleModel;
                    }).collect(Collectors.toList());
        } catch (Exception e) {
            throw new Exception("error list user roles keyclak" + e.getMessage());
        }
    }


    /**
     * Method makes an instance of keycloakADMIN.
     *
     * @return instance of keycloakADMIN.
     */
    private Keycloak getInstanceKeycloak() {
        return KeycloakBuilder.builder().serverUrl(authServerUrl)
                .grantType(OAuth2Constants.PASSWORD).realm("master").clientId("admin-cli")
                .username(userAdmin).password(passwordAdmin)
                .resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build()).build();
    }

    /**
     * Method makes userResource ADMIN keycloak.
     *
     * @return ADMIN userResource keycloak.
     */
    private UsersResource getUsersResource() {
        Keycloak keycloak = this.getInstanceKeycloak();
        keycloak.tokenManager().getAccessToken();
        RealmResource realmResource = keycloak.realm(realm);
        return realmResource.users();
    }

    /**
     * Method makes RealmResource ADMIN keycloak.
     *
     * @return ADMIN RealmResource keycloak.
     */
    private RealmResource getRealmResource() {
        Keycloak keycloak = this.getInstanceKeycloak();
        keycloak.tokenManager().getAccessToken();
        return keycloak.realm(realm);
    }
}
