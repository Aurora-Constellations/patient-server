package com.axiom.patienttracker.http.requests

import zio.json._
import zio.Chunk
import java.time.LocalDateTime
import zio.json.JsonEncoder.fromCodec
import com.axiom.patienttracker.domain.data.Encounter

final case class CreateEncounterRequest(
    accountId: Long,
    doctorId: Long,
    startDate: LocalDateTime,
    endDate: Option[LocalDateTime],
    auroraFileContent: Option[Array[Byte]]
):
    def toEncounter(encounterId: Long): Encounter =
        Encounter(encounterId, accountId, doctorId, startDate, endDate, auroraFileContent)

object CreateEncounterRequest {
    given codec: JsonCodec[CreateEncounterRequest] = DeriveJsonCodec.gen[CreateEncounterRequest]
}
