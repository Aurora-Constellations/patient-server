package com.axiom.patienttracker.http.endpoints

import sttp.tapir.*
import sttp.tapir.json.zio.*
import sttp.tapir.generic.auto.* // imports the type class of derivation package
import com.axiom.patienttracker.domain.data.BillingCode
import com.axiom.patienttracker.http.requests.UpdateBillingCodeRequest

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

    val getAllBillingCodesEndpoint = baseEndpoint
        .tag("billing")
        .name("getAllBillingCodes")
        .description("Get all billing codes")
        .get
        .in("billingcodes")
        .out(jsonBody[List[BillingCode]])

    val updateBillingCodeEndpoint = baseEndpoint
        .tag("billing")
        .name("updateBillingCode")
        .description("Update an existing billing code")
        .put
        .in("billingcode" / path[String]("billingCode"))
        .in(jsonBody[UpdateBillingCodeRequest])
        .out(jsonBody[BillingCode])