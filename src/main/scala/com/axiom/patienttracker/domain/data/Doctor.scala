package com.axiom.patienttracker.domain.data

import zio.json.JsonCodec
import zio.json.DeriveJsonCodec
import io.getquill.*

final case class Doctor(
    doctorId: Long,
    name: String,
    providerId: String
)

object Doctor: //Companion object for Doctor
    given codec: JsonCodec[Doctor] = DeriveJsonCodec.gen[Doctor]