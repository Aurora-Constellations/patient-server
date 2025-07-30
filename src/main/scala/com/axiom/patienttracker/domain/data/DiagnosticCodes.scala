package com.axiom.patienttracker.domain.data

import zio.json.JsonCodec
import zio.json.DeriveJsonCodec
import io.getquill.*    

final case class DiagnosticCodes(
    diagnosticCode: String,
    label: String,
    description: String
)

object DiagnosticCodes: 
    given codec: JsonCodec[DiagnosticCodes] = DeriveJsonCodec.gen[DiagnosticCodes]
    given SchemaMeta[DiagnosticCodes] = schemaMeta[DiagnosticCodes]("diagnostic_codes")