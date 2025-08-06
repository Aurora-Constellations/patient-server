package com.axiom.patienttracker.http.endpoints

import sttp.tapir.*
import sttp.tapir.json.zio.*
import sttp.tapir.generic.auto.* // imports the type class of derivation package
import com.axiom.patienttracker.http.requests.CreateBillingRequest
import com.axiom.patienttracker.domain.data.Billing
import com.axiom.patienttracker.http.requests.UpdateBillingRequest


trait BillingEndpoints extends BaseEndpoint:
    val billingsEndpoint = baseEndpoint
        .tag("billings")
        .name("billings")
        .description("billings Health check")
        .get
        .in("billings"/"health")
        .out(jsonBody[String])
        
    val createBillingEndpoint = baseEndpoint
        .tag("billings")
        .name("createBilling")
        .description("Create a new billing tuple")
        .post
        .in("billings")
        .in(jsonBody[CreateBillingRequest])
        .out(jsonBody[Billing])

    val getBillingByIdEndpoint = baseEndpoint
        .tag("billing")
        .name("getBillingById")
        .description("Get an billing by ID")
        .get
        .in("billing" / path[Long]("billingId"))
        .out(jsonBody[Option[Billing]])
    
    val getAllBillingEndpoint = baseEndpoint
        .tag("billing")
        .name("getAllBillings")
        .description("Get all billings")    
        .get
        .in("billings")
        .out(jsonBody[List[Billing]])
    
    val updateBillingEndpoint = baseEndpoint
        .tag("billing")
        .name("updateBilling")
        .description("Update Billing by billing ID")
        .in("billing" / path[Long]("billingId"))
        .put
        .in(jsonBody[UpdateBillingRequest])
        .out(jsonBody[Billing])

    val deleteBillingEndpoint = baseEndpoint
        .tag("billing")
        .name("deleteBilling")
        .description("Delete Billing by billing ID")
        .delete
        .in("billing" / path[Long]("billingId"))
        .out(jsonBody[Billing])