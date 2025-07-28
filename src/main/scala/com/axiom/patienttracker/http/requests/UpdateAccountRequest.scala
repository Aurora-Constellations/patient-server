package com.axiom.patienttracker.http.requests

import zio.json.JsonCodec
import zio.json.DeriveJsonCodec

import java.time.LocalDateTime

final case class UpdateAccountRequest(
    startDate: Option[LocalDateTime] = None,
    endDate: Option[LocalDateTime] = None,
    notes: Option[String] = None
)

object UpdateAccountRequest:
    given codec: JsonCodec[UpdateAccountRequest] = DeriveJsonCodec.gen[UpdateAccountRequest]