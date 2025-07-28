package com.axiom.patienttracker.http.endpoints

import sttp.tapir.*
import sttp.tapir.json.zio.*
import sttp.tapir.generic.auto.* // imports the type class of derivation package
import com.axiom.patienttracker.domain.data.BillingCode

trait BillingCodeEndpoints extends BaseEndpoint:
    val billingCodeEndpoint = baseEndpoint
        .tag("billing")
        .name("billingCode")
        .description("Billing Code Health check")
        .get
        .in("billingcode")
        .out(jsonBody[String])
    
    val createBillingCodeEndpoint = baseEndpoint
        .tag("billing")
        .name("createBillingCode")
        .description("Create a new billing code")
        .post
        .in("billingcode")
        .in(jsonBody[BillingCode])
        .out(jsonBody[BillingCode])

    val getBillingCodeEndpoint = baseEndpoint
        .tag("billing")
        .name("getBillingCode")
        .description("Get a billing code by code")
        .get
        .in("billingcode" / path[String]("billingCode"))
        .out(jsonBody[Option[BillingCode]])