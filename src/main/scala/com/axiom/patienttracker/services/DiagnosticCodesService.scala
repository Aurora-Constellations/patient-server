    package com.axiom.patienttracker.services

    import zio.*
    import com.axiom.patienttracker.domain.data.DiagnosticCodes
    import com.axiom.patienttracker.http.requests.CreateDiagnosticCodeRequest
    import com.axiom.patienttracker.repositories.DiagnosticCodesRepository

    trait DiagnosticCodesService:
        def create(req: CreateDiagnosticCodeRequest): Task[DiagnosticCodes]

    class DiagnosticCodeServiceLive private (repo: DiagnosticCodesRepository) extends DiagnosticCodesService:
        override def create(req: CreateDiagnosticCodeRequest): Task[DiagnosticCodes] =
            repo.create(req.toDiagnosticCodes)

    object DiagnosticCodeServiceLive:
        val layer: ZLayer[DiagnosticCodesRepository, Nothing, DiagnosticCodesService] =
            ZLayer.fromFunction(new DiagnosticCodeServiceLive(_))