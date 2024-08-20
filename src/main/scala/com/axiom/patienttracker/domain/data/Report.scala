package com.axiom.patienttracker.domain.data

import zio.json.JsonCodec
import zio.json.DeriveJsonCodec

final case class Report(
    id: Long,
    patientId: Long, //Foreign Key
    unitNumber: String,
    systolicPressure: Int,
    diastolicPressure: Int,
    hasHypertension: Boolean,
    glucoseLevel: Double,
    glycatedHemoglobin: Double,
    hasDiabetes: Boolean,
)

object Report:
    given codec: JsonCodec[Report] = DeriveJsonCodec.gen[Report]
