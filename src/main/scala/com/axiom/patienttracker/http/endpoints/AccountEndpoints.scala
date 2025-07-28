package com.axiom.patienttracker.http.endpoints

import sttp.tapir.*
import sttp.tapir.json.zio.*
import sttp.tapir.generic.auto.* // imports the type class of derivation package
import com.axiom.patienttracker.http.requests.CreateAccountRequest
import com.axiom.patienttracker.domain.data.Account
import com.axiom.patienttracker.http.requests.UpdateAccountRequest

trait AccountEndpoints extends BaseEndpoint:
    val accountEndpoint = baseEndpoint
        .tag("account")
        .name("account")
        .description("Account Data")
        .get
        .in("account")
        .out(plainBody[String])

    val createAccountEndpoint = baseEndpoint
        .tag("account")
        .name("createAccount")
        .description("Create a new account for the patient")
        .post
        .in("account")
        .in(jsonBody[CreateAccountRequest])
        .out(jsonBody[Account])

    val getAccountEndpoint = baseEndpoint
        .tag("account")
        .name("getAccount")
        .description("Get account details")
        .get
        .in("account" / path[Long]("accountId"))
        .out(jsonBody[Option[Account]])

    val getAccountByPatientIdEndpoint = baseEndpoint
        .tag("account")
        .name("getAccountByPatientId")
        .description("Get account details by patient ID")
        .get
        .in("account" / "patient" / path[Long]("patientId"))
        .out(jsonBody[List[Account]])

    val getAllAccountsEndpoint = baseEndpoint
        .tag("account")
        .name("getAllAccounts")
        .description("Get all accounts")
        .get
        .in("accounts")
        .out(jsonBody[List[Account]])

    val updateAccountEndpoint = baseEndpoint
        .tag("account")
        .name("updateAccount")
        .description("Update an existing account")
        .put
        .in("account" / path[Long]("accountId"))
        .in(jsonBody[UpdateAccountRequest])
        .out(jsonBody[Account])

    val deleteAccountEndpoint = baseEndpoint
        .tag("account")
        .name("deleteAccount")
        .description("Delete an existing account")
        .delete
        .in("account" / path[Long]("accountId"))
        .out(jsonBody[Account])

    val deleteAllAccountsEndpoint = baseEndpoint
        .tag("account")
        .name("deleteAllAccounts")
        .description("Delete all accounts with a specific patient ID")
        .delete
        .in("accounts" / "deleteAll" / path[Long]("patientId"))
        .out(jsonBody[List[Account]]) // Assuming you want to return the deleted accounts