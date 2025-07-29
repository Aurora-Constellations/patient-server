    package com.axiom.patienttracker.services

    import zio.*
    import com.axiom.patienttracker.domain.data.DiagnosticCodes
    import com.axiom.patienttracker.repositories.DiagnosticCodesRepository
    import com.axiom.patienttracker.http.requests.UpdateDiagnosticCodeRequest

    trait DiagnosticCodesService:
        def create(req: DiagnosticCodes): Task[DiagnosticCodes]
        def update(code: String, req: UpdateDiagnosticCodeRequest): Task[DiagnosticCodes]
        def delete(code: String): Task[DiagnosticCodes]

    class DiagnosticCodeServiceLive private (repo: DiagnosticCodesRepository) extends DiagnosticCodesService:
        override def create(req: DiagnosticCodes): Task[DiagnosticCodes] =
            repo.create(req)

        override def update(code: String, req: UpdateDiagnosticCodeRequest): Task[DiagnosticCodes] =
            repo.update(code, existing =>
            existing.copy(
                label = req.label.getOrElse(existing.label),
                description = req.description.getOrElse(existing.description)
            )
        )

        override def delete(code: String): Task[DiagnosticCodes] =
            repo.delete(code)


    object DiagnosticCodeServiceLive:
        val layer: ZLayer[DiagnosticCodesRepository, Nothing, DiagnosticCodesService] =
            ZLayer.fromFunction(new DiagnosticCodeServiceLive(_))