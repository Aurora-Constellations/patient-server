package com.axiom.patienttracker.http.controllers

import zio.*
import sttp.tapir.*

import sttp.tapir.server.ServerEndpoint
import com.axiom.patienttracker.http.endpoints.EncounterEndpoints
import com.axiom.patienttracker.services.EncounterService

class EncounterController private (service: EncounterService) extends BaseController with EncounterEndpoints:
    val encounter: ServerEndpoint[Any, Task] = encounterEndpoint
        .serverLogicSuccess[Task](_ => ZIO.succeed("All set!"))

    val createEncounter: ServerEndpoint[Any, Task] = createEncounterEndpoint
        .serverLogicSuccess { 
            request => service.create(request)
        }

    override val routes: List[ServerEndpoint[Any, Task]] = List(encounter, createEncounter)

object EncounterController  :
    val makeZIO = for {
        service <- ZIO.service[EncounterService]
    }  yield new EncounterController(service)