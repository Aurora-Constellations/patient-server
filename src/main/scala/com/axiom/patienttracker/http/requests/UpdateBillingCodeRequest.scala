package com.axiom.patienttracker.http.requests

import zio.json.JsonCodec
import zio.json.DeriveJsonCodec

final case class UpdateBillingCodeRequest(
    label: Option[String] = None,
    amount: Option[BigDecimal] = None,
    description: Option[String] = None
)

object UpdateBillingCodeRequest: // Companion object for UpdateBillingCodeRequest
    given codec: JsonCodec[UpdateBillingCodeRequest] = DeriveJsonCodec.gen[UpdateBillingCodeRequest]