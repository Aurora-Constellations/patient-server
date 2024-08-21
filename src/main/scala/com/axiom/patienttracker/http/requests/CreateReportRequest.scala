package com.axiom.patienttracker.http.requests

import zio.json.JsonCodec
import zio.json.DeriveJsonCodec
import com.axiom.patienttracker.domain.data.Report

final case class CreateReportRequest(
    patientId: Long, //Foreign Key
    unitNumber: String,
    systolicPressure: Int,
    diastolicPressure: Int,
    hasHypertension: Option[Boolean] = None,
    glucoseLevel: Double,
    glycatedHemoglobin: Double,
    hasDiabetes: Option[Boolean] = None,
):
    def toReport(id: Long) =
        Report(id, patientId, unitNumber, systolicPressure, diastolicPressure, CreateReportRequest.calcHypertension(systolicPressure, diastolicPressure), glucoseLevel, glycatedHemoglobin, CreateReportRequest.calcDiabetes(glucoseLevel, glycatedHemoglobin))

object CreateReportRequest:
    given codec: JsonCodec[CreateReportRequest] = DeriveJsonCodec.gen[CreateReportRequest]

    def calcHypertension(systolicPressure: Int, diastolicPressure: Int): Boolean = 
        (systolicPressure >= 140) || (diastolicPressure >= 90)

    def calcDiabetes(glucoseLevel: Double, glycatedHemoglobin: Double): Boolean = 
        (glucoseLevel >= 126) || (glycatedHemoglobin >= 6.5)