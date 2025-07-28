package com.axiom.patienttracker.domain.data

import zio.json.JsonCodec
import zio.json.DeriveJsonCodec

final case class BillingCode(
    billingCode: String,
    label: String,
    amount: BigDecimal,
    description: Option[String] = None
)

object BillingCode: // Companion object for BillingCode
    given codec: JsonCodec[BillingCode] = DeriveJsonCodec.gen[BillingCode]