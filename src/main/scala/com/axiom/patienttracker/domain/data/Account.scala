package com.axiom.patienttracker.domain.data

import zio.json.JsonCodec
import zio.json.DeriveJsonCodec
import java.time.LocalDateTime

final case class Account(
    accountId: Long,
    patientId: Long,
    startDate: LocalDateTime,
    endDate: Option[LocalDateTime] = None,
    notes: Option[String] = None
)

object Account: //Companion object for Account
    given codec: JsonCodec[Account] = DeriveJsonCodec.gen[Account]