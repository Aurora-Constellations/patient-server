package com.axiom.patienttracker.http.controllers

import zio.*
import sttp.tapir.*

import sttp.tapir.server.ServerEndpoint

import com.axiom.patienttracker.http.endpoints.DoctorEndpoints
import com.axiom.patienttracker.services.DoctorService

class DoctorController private (service: DoctorService) extends BaseController with DoctorEndpoints:
    val doctor: ServerEndpoint[Any, Task] = doctorEndpoint
        .serverLogicSuccess[Task](_ => ZIO.succeed("All set!"))

    val createDoctor: ServerEndpoint[Any, Task] = createDoctorEndpoint
        .serverLogic[Task](
            req => service.create(req).either
        )

    override val routes: List[ServerEndpoint[Any, Task]] = List(doctor, createDoctor)

object DoctorController:
    val makeZIO = for{
        service <- ZIO.service[DoctorService]
    }yield new DoctorController(service)