package com.axiom.patienttracker.http.requests

import zio.json.JsonCodec
import zio.json.DeriveJsonCodec

import java.time.LocalDateTime

final case class UpdateBillingRequest(
    billingCode: Option[String] = None,
    diagnosticCode: Option[String]= None,
    recordedTime: Option[LocalDateTime] = None,
    unitCount: Option[Int]= None,
    Notes: Option[String] =None,
)

object UpdateBillingRequest:
    given codec: JsonCodec[UpdateBillingRequest] = DeriveJsonCodec.gen[UpdateBillingRequest]