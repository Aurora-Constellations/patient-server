package com.axiom.patienttracker.http.requests

import zio.json._
import zio.Chunk
import java.time.LocalDateTime
import zio.json.JsonEncoder.fromCodec
import com.axiom.patienttracker.domain.data.Billing


final case class CreateBillingRequest(
    encounterId: Long,
    billingCode: String,
    diagnosticCode: String,
    recordedTime: Option[LocalDateTime],
    unitCount: Int,
    Notes: Option[String],
):

    def toBilling(billingId: Long): Billing =
        Billing(billingId, encounterId, billingCode, diagnosticCode,recordedTime, unitCount, Notes )

object CreateBillingRequest {
    given codec: JsonCodec[CreateBillingRequest] = DeriveJsonCodec.gen[CreateBillingRequest]
}