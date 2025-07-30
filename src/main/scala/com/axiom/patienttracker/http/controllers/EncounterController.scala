package com.axiom.patienttracker.http.controllers

import zio.*
import sttp.tapir.*

import sttp.tapir.server.ServerEndpoint
import com.axiom.patienttracker.http.endpoints.EncounterEndpoints


class EncounterController private extends BaseController with EncounterEndpoints:
    val encounter: ServerEndpoint[Any, Task] = encounterEndpoint
        .serverLogicSuccess[Task](_ => ZIO.succeed("All set!"))

    override val routes: List[ServerEndpoint[Any, Task]] = List(encounter)

object EncounterController  :
    val makeZIO = for {
        _ <- ZIO.logInfo("Creating EncounterController")
    }  yield new EncounterController()