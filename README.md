# KEYCLOAK ADMIN REST EXAMPLE SIMPLE

* create user
* update user
* send email verify user
* send email forgot password user
* logout
* get users
* config roles
* more

## how to use?

### configure properties using your keycloak

![Alt text](config.png "config")

## Start Keycloak

```rust
docker run -p 8080:8080 -e KEYCLOAK_USER=admin -e KEYCLOAK_PASSWORD=admin quay.io/keycloak/keycloak:15.0.2
```

### How to create a Realm, see the documents. (It's quite simple)
![Alt text](create_realm.png "create_realm")

#### source

```rust
https://www.keycloak.org/getting-started/getting-started-docker
```

## create a client

### Why bearer-only?

#### Bearer-only access type means that the application only allows bearer token requests. If this is turned on, this application cannot participate in browser logins.

#### So if you select your client as bearer-only then in that case keycloak adapter will not attempt to authenticate users, but only verify bearer tokens. That why keycloak documentation also mentioned bearer-only application will not allow the login from browser.
### Client Config

![Alt text](cliente_config.png "client_config")

## important

### Remember, the requests are then public, so when going up to production anyone could access, one way to protect yourself is by using 'antMatchers', and creating the 'ADMIN' role in the keycloak, as shown in the example below.

![Alt text](admin.png "admin")

### But before doing that create ROLE ADMIN in keycloak.
