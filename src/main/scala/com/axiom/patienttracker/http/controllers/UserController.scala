package com.axiom.patienttracker.http.controllers

import zio.*
import sttp.tapir.*
import sttp.tapir.server.*

import com.axiom.patienttracker.http.endpoints.UserEndpoints
import com.axiom.patienttracker.services.UserService
import com.axiom.patienttracker.http.responses.UserResponse
import com.axiom.patienttracker.domain.errors.UnauthorizedException
import com.axiom.patienttracker.services.JWTService
import com.axiom.patienttracker.domain.data.UserID

class UserController private (userService: UserService, jwtService: JWTService) extends BaseController with UserEndpoints:
    val create: ServerEndpoint[Any, Task] = createUserEndpoint
        .serverLogic{req =>
            userService.registerUser(req.email, req.password)
            .map(user => UserResponse(user.email))
            .either
        }

    val login: ServerEndpoint[Any, Task] = loginEndpoint
        .serverLogic{req =>
            userService.generateToken(req.email, req.password)
            .someOrFail(UnauthorizedException)
            .either
        }

    //Change password
    //Check JWT
    val updatePassword: ServerEndpoint[Any, Task] = updatePasswordEndpoint
        .serverSecurityLogic[UserID, Task](token => jwtService.verifyToken(token).either) // .either required
        .serverLogic{userId => req => //curried function
            userService.updatePassword(req.email, req.oldPassword, req.newPassword)
            .map(user => UserResponse(user.email))
            .either
        }

    val delete: ServerEndpoint[Any, Task] = deleteEndpoint
        .serverSecurityLogic[UserID, Task](token => jwtService.verifyToken(token).either) // .either required
        .serverLogic{userId => req => //curried function
            userService.deleteUser(req.email, req.password)
            .map(user => UserResponse(user.email))
            .either
        }
    
    val forgotPassword = forgotPasswordEndpoint
        .serverLogic{req => 
            userService.sendPasswordRecoveryToken(req.email)
            .either    
        }

    val recoverPassword = recoverPasswordEndpoint
        .serverLogic{req =>
            userService.recoverPasswordFromToken(req.email, req.token, req.newPassword)
            .filterOrFail(b => b)(UnauthorizedException)
            .unit //as we have no .out in our endpoint
            .either
        }
    override val routes: List[ServerEndpoint[Any, Task]] = List(create, login, updatePassword, delete)

object UserController:
    val makeZIO = for{
        userService <- ZIO.service[UserService]
        jwtService <- ZIO.service[JWTService]
    }yield new UserController(userService, jwtService)
