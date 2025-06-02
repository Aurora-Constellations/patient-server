package com.axiom.patienttracker.http.controllers

import zio.*
import sttp.tapir.*

import sttp.tapir.server.ServerEndpoint
import java.time.format.DateTimeFormatter
import java.time.LocalDate

import com.axiom.patienttracker.services.PatientService
import com.axiom.patienttracker.http.endpoints.PatientEndpoints
import com.axiom.patienttracker.domain.data.Patient
import com.axiom.patienttracker.domain.errors.HttpError

class PatientController private (service: PatientService) extends BaseController with PatientEndpoints:
    val create: ServerEndpoint[Any, Task] = createEndpoint.serverLogic { req =>
       service.create(req).either
    }

    val update: ServerEndpoint[Any, Task] = updateEndpoint.serverLogic { req =>
        service.update(req._1, req._2).either    
    }

    val getAll: ServerEndpoint[Any, Task] =
        getAllEndpoint.serverLogic(_ => 
            service.getAll.either
        )

    val getById: ServerEndpoint[Any, Task] = getByIdEndpoint.serverLogic { id =>
        ZIO
            .attempt(id.toLong)
            .flatMap(service.getById)
            .catchSome:
                case _: NumberFormatException =>
                    service.getByUnitNumber(id)
            .either
    }

    val delete: ServerEndpoint[Any, Task] = deleteEndpoint.serverLogic { unitNumber =>
        service.delete(unitNumber).either
    }

    val patient: ServerEndpoint[Any, Task] = patientEndpoint
        .serverLogicSuccess[Task](_ => ZIO.succeed("All set!"))

    val errorRoute = errorEndpoint
        //ZIO.fail returns Task[Nothing] or Task whatever is specified in the .out of the endpoint
        // in our case Task[String]
        /*
        VERY VERY VERY IMPORTANT
        NEVER EVER MISS 
        .either to statisfy the input type for .serverLogic because the compiler do not catches it. 
         */
        .serverLogic[Task](_ => ZIO.fail(new RuntimeException("9/11 !!!")).either) //Task[Either[HttpError, String]] 
    
    override val routes: List[ServerEndpoint[Any, Task]] = List(create, update, getAll, getById, delete, errorRoute)

object PatientController:
    val makeZIO = for{
        service <- ZIO.service[PatientService]
    }yield new PatientController(service)