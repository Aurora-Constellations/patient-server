package com.axiom.patienttracker.http.controllers


import zio.*
import sttp.tapir.*

import sttp.tapir.server.ServerEndpoint
import com.axiom.patienttracker.http.endpoints.BillingEndpoints
import com.axiom.patienttracker.services.BillingService

class BillingController private (service: BillingService) extends BaseController with BillingEndpoints:
  
  val billingHealth: ServerEndpoint[Any, Task] = billingsEndpoint
    .serverLogicSuccess[Task](_ => ZIO.succeed("Billing module is up"))
  
  val createBilling: ServerEndpoint[Any, Task] = createBillingEndpoint
    .serverLogic[Task] { 
      request => service.create(request).either
    }

  override val routes: List[ServerEndpoint[Any, Task]] = List(billingHealth, createBilling)

object BillingController:
    val makeZIO = for {
        service <- ZIO.service[BillingService]
    }  yield new BillingController(service)