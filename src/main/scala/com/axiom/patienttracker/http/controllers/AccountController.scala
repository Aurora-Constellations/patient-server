package com.axiom.patienttracker.http.controllers

import zio.*
import sttp.tapir.*

import sttp.tapir.server.ServerEndpoint

import com.axiom.patienttracker.http.endpoints.AccountEndpoints


class AccountController private extends BaseController with AccountEndpoints:
    val account: ServerEndpoint[Any, Task] = accountEndpoint
        .serverLogicSuccess[Task](_ => ZIO.succeed("All set!"))
    override val routes: List[ServerEndpoint[Any, Task]] = List(account)

object AccountController:
    val makeZIO = for {
        _ <- ZIO.logInfo("Creating AccountController")
    }  yield new AccountController