package com.axiom.patienttracker.http.controllers

import com.axiom.patienttracker.services.ReportService
import sttp.tapir.server.ServerEndpoint
import zio.*
import com.axiom.patienttracker.http.endpoints.ReportEndpoints
import io.getquill.ast.StringOperator.toLong

class ReportController private (service: ReportService) extends BaseController with ReportEndpoints:
    val report: ServerEndpoint[Any, Task] = reportEndpoint
        .serverLogicSuccess[Task](_ => ZIO.succeed("Hey reports!"))

    val create: ServerEndpoint[Any, Task] = createEndpoint
        .serverLogic(req =>
            service.create(req).either)

    val getAll: ServerEndpoint[Any, Task] = getAllEndpoint
        .serverLogic(req =>
            service.getAll().either)

    val getById: ServerEndpoint[Any, Task] = getByIdEndpoint
        .serverLogic( id =>
            service.getById(id).either
        )
    
    val getByUnitNumber: ServerEndpoint[Any, Task] = getByUnitNumberEndpoint
        .serverLogic( unitNumber => 
            service.getByUnitNumber(unitNumber).either)

    val delete: ServerEndpoint[Any, Task] = deleteEndpoint
        .serverLogic( id => 
            service.delete(id).either
        )
    override val routes: List[ServerEndpoint[Any, Task]] = List(create, getAll, getById, getByUnitNumber, delete)

object ReportController:
    val makeZIO = ZIO.service[ReportService].map(service => new ReportController(service))