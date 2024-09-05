package com.axiom.patienttracker.http.controllers

import com.axiom.patienttracker.services.ReportService
import sttp.tapir.server.ServerEndpoint
import zio.*
import com.axiom.patienttracker.http.endpoints.ReportEndpoints
import io.getquill.ast.StringOperator.toLong
import com.axiom.patienttracker.services.JWTService
import com.axiom.patienttracker.domain.data.UserID

class ReportController private (reportService: ReportService, jwtService: JWTService) extends BaseController with ReportEndpoints:
    val report: ServerEndpoint[Any, Task] = reportEndpoint
        .serverLogicSuccess[Task](_ => ZIO.succeed("Hey reports!"))

    val create: ServerEndpoint[Any, Task] = createEndpoint
        .serverSecurityLogic[UserID, Task](token => 
            jwtService.verifyToken(token).either)
        .serverLogic(_ => req =>
            reportService.create(req).either)

    val getAll: ServerEndpoint[Any, Task] = getAllEndpoint
        .serverSecurityLogic[UserID, Task](token => 
            jwtService.verifyToken(token).either)
        .serverLogic(_ => req =>
            reportService.getAll().either)

    val getById: ServerEndpoint[Any, Task] = getByIdEndpoint
        .serverSecurityLogic[UserID, Task](token => 
            jwtService.verifyToken(token).either
        )
        .serverLogic(_ => id =>
            reportService.getById(id).either
        )
    
    val getByUnitNumber: ServerEndpoint[Any, Task] = getByUnitNumberEndpoint
        .serverSecurityLogic[UserID, Task](token => 
            jwtService.verifyToken(token).either)
        .serverLogic(_ => unitNumber => 
            reportService.getByUnitNumber(unitNumber).either)

    val delete: ServerEndpoint[Any, Task] = deleteEndpoint
        .serverSecurityLogic[UserID, Task](token => 
            jwtService.verifyToken(token).either
        )
        .serverLogic(_ => id => 
            reportService.delete(id).either
        )
    override val routes: List[ServerEndpoint[Any, Task]] = List(create, getAll, getById, getByUnitNumber, delete)

object ReportController:
    val makeZIO = for{
        reportService <- ZIO.service[ReportService]
        jwtService <- ZIO.service[JWTService]
    } yield new ReportController(reportService, jwtService)