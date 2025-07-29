package com.axiom.patienttracker.http.requests

import zio.json.JsonCodec
import zio.json.DeriveJsonCodec
import com.axiom.patienttracker.domain.data.DiagnosticCodes

final case class CreateDiagnosticCodeRequest(
    diagnosticCode: String,
    label: String,
    description: String
):
    def toDiagnosticCodes =
    DiagnosticCodes(diagnosticCode, label, description)

object CreateDiagnosticCodeRequest:
  given JsonCodec[CreateDiagnosticCodeRequest] = DeriveJsonCodec.gen[CreateDiagnosticCodeRequest]