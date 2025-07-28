package com.axiom.patienttracker.http.controllers

import zio.*
import sttp.tapir.*

import sttp.tapir.server.ServerEndpoint
import com.axiom.patienttracker.http.endpoints.BillingCodeEndpoints
import com.axiom.patienttracker.services.BillingCodeService


class BillingCodeController private (service: BillingCodeService) extends BaseController with BillingCodeEndpoints:
    val billingCode: ServerEndpoint[Any, Task] = billingCodeEndpoint
        .serverLogicSuccess[Task](_ => ZIO.succeed("All set!"))

    val createBillingCode: ServerEndpoint[Any, Task] = createBillingCodeEndpoint
        .serverLogic[Task]{
            req => service.create(req).either
        }

    override val routes: List[ServerEndpoint[Any, Task]] = List(billingCode, createBillingCode)

object BillingCodeController:
    val makeZIO = for {
        service <- ZIO.service[BillingCodeService]
    }  yield new BillingCodeController(service) 