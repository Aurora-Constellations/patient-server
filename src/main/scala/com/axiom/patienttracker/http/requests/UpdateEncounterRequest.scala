package com.axiom.patienttracker.http.requests

import zio.json.JsonCodec
import zio.json.DeriveJsonCodec

import java.time.LocalDateTime

final case class UpdateEncounterRequest(
    startDate: Option[LocalDateTime] = None,
    endDate: Option[LocalDateTime] = None,
    auroraFileContent: Option[Array[Byte]] = None
)

object UpdateEncounterRequest:
    given codec: JsonCodec[UpdateEncounterRequest] = DeriveJsonCodec.gen[UpdateEncounterRequest]