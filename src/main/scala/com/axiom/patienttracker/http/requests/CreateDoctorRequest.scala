package com.axiom.patienttracker.http.requests

import zio.json.JsonCodec
import zio.json.DeriveJsonCodec
import com.axiom.patienttracker.domain.data.Doctor

final case class CreateDoctorRequest(
    name: String,
    providerId: String
):
    def toDoctor(doctorId: Long) =
        Doctor(doctorId, name, providerId)

object CreateDoctorRequest:
    given codec: JsonCodec[CreateDoctorRequest] = DeriveJsonCodec.gen[CreateDoctorRequest]