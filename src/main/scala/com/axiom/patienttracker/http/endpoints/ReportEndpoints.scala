package com.axiom.patienttracker.http.endpoints

import sttp.tapir.*

trait ReportEndpoints:
    val reportEndpoint = endpoint
        .tag("report")
        .name("report")
        .description("patient reports")
        .get
        .in("report")
        .out(plainBody[String])