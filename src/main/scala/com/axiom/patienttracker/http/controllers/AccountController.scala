package com.axiom.patienttracker.http.controllers

import zio.*
import sttp.tapir.*

import sttp.tapir.server.ServerEndpoint

import com.axiom.patienttracker.http.endpoints.AccountEndpoints
import com.axiom.patienttracker.services.AccountService


class AccountController private (service: AccountService) extends BaseController with AccountEndpoints:
    val account: ServerEndpoint[Any, Task] = accountEndpoint
        .serverLogicSuccess[Task](_ => ZIO.succeed("All set!"))

    val createAccount: ServerEndpoint[Any, Task] = createAccountEndpoint
        .serverLogic[Task](
            req => service.create(req).either
        )

    override val routes: List[ServerEndpoint[Any, Task]] = List(account, createAccount)

object AccountController:
    val makeZIO = for {
        service <- ZIO.service[AccountService]
    }  yield new AccountController(service)