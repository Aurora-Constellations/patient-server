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

    val getAccount: ServerEndpoint[Any, Task] = getAccountEndpoint
        .serverLogic[Task](
            accountId => service.getById(accountId).either
        )

    val getAccountByPatientId: ServerEndpoint[Any, Task] = getAccountByPatientIdEndpoint
        .serverLogic[Task](
            patientId => service.getByPatientId(patientId).either
        )

    val getAllAccounts: ServerEndpoint[Any, Task] = getAllAccountsEndpoint
        .serverLogic[Task](
            _ => service.getAll.either
        )

    override val routes: List[ServerEndpoint[Any, Task]] = List(account, createAccount, getAccount, getAccountByPatientId, getAllAccounts)

object AccountController:
    val makeZIO = for {
        service <- ZIO.service[AccountService]
    }  yield new AccountController(service)