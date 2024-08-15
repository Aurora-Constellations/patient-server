package com.axiom.patienttracker.http.controllers

import zio.*
import collection.mutable
import com.axiom.patienttracker.http.endpoints.PatientEndpoints
import com.axiom.patienttracker.domain.data.Patient
import sttp.tapir.server.ServerEndpoint
import java.time.format.DateTimeFormatter
import java.time.LocalDate

class PatientController private extends BaseController with PatientEndpoints:
    // Currently using in-memory Database
    val db = mutable.Map[Long, Patient](
        -1L -> Patient(-1L, "99999", "Random", "Mr Somebody", "unknown", stringToDate("20-04-1889"))
    )

    val create: ServerEndpoint[Any, Task] = createEndpoint.serverLogicSuccess { req =>
        ZIO.succeed {
            val newId = db.keys.max + 1
            val newPatient = req.toPatient(newId)
            db += (newId -> newPatient)
            newPatient
        }
    }

    val getAll: ServerEndpoint[Any, Task] =
        getAllEndpoint.serverLogicSuccess(_ => ZIO.succeed(db.values.toList))

    val getById = getByIdEndpoint.serverLogicSuccess { id =>
        ZIO
            .attempt(id.toLong)
            .map(db.get)
    }

    val patient: ServerEndpoint[Any, Task] = patientEndpoint
        .serverLogicSuccess[Task](_ => ZIO.succeed("All set!"))
    
    override val routes: List[ServerEndpoint[Any, Task]] = List(create, getAll, getById)
    def stringToDate(dateString: String) =
        val pattern = DateTimeFormatter.ofPattern("dd-MM-YYYY")
        val date = LocalDate.parse(dateString, pattern)
        date

object PatientController:
    val makeZIO = ZIO.succeed(new PatientController)