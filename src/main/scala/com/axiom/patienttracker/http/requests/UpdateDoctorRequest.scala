package com.axiom.patienttracker.http.requests

import zio.json.JsonCodec
import zio.json.DeriveJsonCodec

final case class UpdateDoctorRequest(
    name: Option[String] = None,
    providerId: Option[String] = None
)

object UpdateDoctorRequest:
    given codec: JsonCodec[UpdateDoctorRequest] = DeriveJsonCodec.gen[UpdateDoctorRequest]