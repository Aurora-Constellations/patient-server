package com.axiom.patienttracker.http.requests

import zio.json.{DeriveJsonCodec, JsonCodec}

final case class UpdateDiagnosticCodeRequest(
  label: Option[String] = None,
  description: Option[String] = None
)

object UpdateDiagnosticCodeRequest:
  given JsonCodec[UpdateDiagnosticCodeRequest] = DeriveJsonCodec.gen