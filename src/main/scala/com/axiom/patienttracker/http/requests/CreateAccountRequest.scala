package com.axiom.patienttracker.http.requests

import zio.json.JsonCodec
import zio.json.DeriveJsonCodec
import java.time.LocalDateTime
import com.axiom.patienttracker.domain.data.Account

final case class CreateAccountRequest(
    patientId: Long,
    startDate: LocalDateTime,
    endDate: Option[LocalDateTime] = None,
    notes: Option[String] = None
):
    def toAccount(accountId: Long): Account =
        Account(accountId, patientId, startDate, endDate, notes)

object CreateAccountRequest:
    given codec: JsonCodec[CreateAccountRequest] = DeriveJsonCodec.gen[CreateAccountRequest]