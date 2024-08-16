package com.axiom.patienttracker.http.controllers

import zio.*

import com.axiom.patienttracker.http.endpoints.PatientEndpoints
import com.axiom.patienttracker.domain.data.Patient
import sttp.tapir.server.ServerEndpoint
import java.time.format.DateTimeFormatter
import java.time.LocalDate
import com.axiom.patienttracker.services.PatientService

class PatientController private (service: PatientService) extends BaseController with PatientEndpoints:
    val create: ServerEndpoint[Any, Task] = createEndpoint.serverLogicSuccess { req =>
       service.create(req)
    }

    val getAll: ServerEndpoint[Any, Task] =
        getAllEndpoint.serverLogicSuccess(_ => 
            service.getAll
        )

    val getById = getByIdEndpoint.serverLogicSuccess { id =>
        ZIO
            .attempt(id.toLong)
            .flatMap(service.getById)
            .catchSome:
                case _: NumberFormatException =>
                    service.getByUnitNumber(id)
    }

    val patient: ServerEndpoint[Any, Task] = patientEndpoint
        .serverLogicSuccess[Task](_ => ZIO.succeed("All set!"))
    
    override val routes: List[ServerEndpoint[Any, Task]] = List(create, getAll, getById)

object PatientController:
    val makeZIO = for{
        service <- ZIO.service[PatientService]
    }yield new PatientController(service)