package com.axiom.patienttracker.http.endpoints

import sttp.tapir.*
import sttp.tapir.json.zio.*
import sttp.tapir.generic.auto.* // imports the type class of derivation package
import com.axiom.patienttracker.http.requests.CreateBillingRequest
import com.axiom.patienttracker.domain.data.Billing

trait BillingEndpoints extends BaseEndpoint:
    val billingsEndpoint = baseEndpoint
        .tag("billings")
        .name("billings")
        .description("billings Health check")
        .get
        .in("billings")
        .out(jsonBody[String])
        
    val createBillingEndpoint = baseEndpoint
        .tag("billings")
        .name("createBilling")
        .description("Create a new billing tuple")
        .post
        .in("billings")
        .in(jsonBody[CreateBillingRequest])
        .out(jsonBody[Billing])