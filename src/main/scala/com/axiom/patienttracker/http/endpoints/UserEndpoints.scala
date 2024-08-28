package com.axiom.patienttracker.http.endpoints

import sttp.tapir.*
import sttp.tapir.json.zio.*
import sttp.tapir.generic.auto.*

import com.axiom.patienttracker.http.requests.{RegisterUserAccount ,UpdatePasswordRequest, DeleteAccountRequest, LoginRequest}
import com.axiom.patienttracker.http.responses.UserResponse
import com.axiom.patienttracker.domain.data.UserToken

trait UserEndpoints extends BaseEndpoint:
    val createUserEndpoint = baseEndpoint
        .tag("users")
        .name("register")
        .description("Register a user account with username and password")
        .in("users")
        .post
        .in(jsonBody[RegisterUserAccount])
        .out(jsonBody[UserResponse])

    // should be an authorized endpoint (need JWT)
    val updatePasswordEndpoint = secureBaseEndpoint
        .tag("users")
        .name("update password")
        .description("Update User Password")
        .in("users" / "password")
        .put
        .in(jsonBody[UpdatePasswordRequest])
        .out(jsonBody[UserResponse])
    
    // should be an authorized endpoint (need JWT)
    val deleteEndpoint = secureBaseEndpoint
        .tag("users")
        .name("delete account")
        .description("Delete user account")
        .in("users")
        .delete
        .in(jsonBody[DeleteAccountRequest])
        .out(jsonBody[UserResponse])

    val loginEndpoint = baseEndpoint
        .tag("users")
        .name("login")
        .description("Log in and generate JWT token")
        .in("users" / "login")
        .post
        .in(jsonBody[LoginRequest])
        .out(jsonBody[UserToken])