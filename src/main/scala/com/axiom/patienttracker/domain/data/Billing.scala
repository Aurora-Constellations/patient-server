package com.axiom.patienttracker.domain.data

import zio.json._
import zio.Chunk
import java.time.LocalDateTime
import zio.json.JsonEncoder.fromCodec

final case class Billing(
    billingId: Long,
    encounterId: Long,
    billingCode: String,
    diagnosticCode: String,
    recordedTime: Option[LocalDateTime],
    unitCount: Int,
    Notes: Option[String],
)

object Billing {
    given codec: JsonCodec[Billing] = DeriveJsonCodec.gen[Billing]
}


