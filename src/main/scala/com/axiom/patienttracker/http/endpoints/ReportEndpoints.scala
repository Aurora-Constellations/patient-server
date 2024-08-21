package com.axiom.patienttracker.http.endpoints

import sttp.tapir.*
import sttp.tapir.json.zio.*
import sttp.tapir.generic.auto.* // imports the type class of derivation package
import com.axiom.patienttracker.domain.data.Report
import com.axiom.patienttracker.http.requests.CreateReportRequest

trait ReportEndpoints:
    val reportEndpoint = endpoint
        .tag("report")
        .name("report")
        .description("patient reports")
        .get
        .in("report")
        .out(plainBody[String])

    val createEndpoint = endpoint
        .tag("reports")
        .name("reports")
        .description("create a new patient report")
        .in("reports")
        .post
        .in(jsonBody[CreateReportRequest])
        .out(jsonBody[Report])

    val getAllEndpoint = endpoint
        .tag("reports")
        .name("reports")
        .description("get all the reports")
        .get
        .in("reports")
        .out(jsonBody[List[Report]])
    
    val getByIdEndpoint = endpoint
        .tag("reports")
        .name("reports")
        .description("get reports by id")
        .in("reports" / path[Long]("id"))
        .get
        .out(jsonBody[Option[Report]])

    val getByUnitNumberEndpoint = endpoint
        .tag("reports")
        .name("reports")
        .description("get patient report by unit number")
        .in("reports" / "patients" / path[String]("unitNumber"))
        .get
        .out(jsonBody[List[Report]])