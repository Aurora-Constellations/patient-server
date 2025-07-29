package com.axiom.patienttracker.repositories

import zio.* 
import io.getquill.* 
import io.getquill.jdbczio.Quill

import com.axiom.patienttracker.domain.data.DiagnosticCodes
import com.axiom.patienttracker.domain.data.DiagnosticCodes.given_SchemaMeta_DiagnosticCodes

trait DiagnosticCodesRepository:
    def create(diagnosticCode: DiagnosticCodes): Task[DiagnosticCodes]

class DiagnosticCodesRepositoryLive(quill: Quill.Postgres[SnakeCase]) extends DiagnosticCodesRepository:
    import quill.* //gives us access to methods such as run, query, filter or lift

    inline given schema: SchemaMeta[DiagnosticCodes] = schemaMeta[DiagnosticCodes]("DiagnosticCodes") // Table name `"DiagnosticCodes"`
   

    override def create(diagnosticCode: DiagnosticCodes): Task[DiagnosticCodes] = 
        run {
            query[DiagnosticCodes]
                .insertValue(lift(diagnosticCode))
                .returning(d => d)
        } 

object DiagnosticCodesRepositoryLive:
    val layer = ZLayer {
        ZIO.service[Quill.Postgres[SnakeCase]].map(quill => DiagnosticCodesRepositoryLive(quill))
    }