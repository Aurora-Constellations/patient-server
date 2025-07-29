package com.axiom.patienttracker.repositories

import zio.* 
import io.getquill.* 
import io.getquill.jdbczio.Quill

import com.axiom.patienttracker.domain.data.DiagnosticCodes
import com.axiom.patienttracker.domain.data.DiagnosticCodes.given_SchemaMeta_DiagnosticCodes

trait DiagnosticCodesRepository:
    def create(diagnosticCode: DiagnosticCodes): Task[DiagnosticCodes]
    def update(code: String, apply: DiagnosticCodes => DiagnosticCodes): Task[DiagnosticCodes]
    def getByCode(code: String): Task[Option[DiagnosticCodes]]
    def delete(code: String): Task[DiagnosticCodes]

class DiagnosticCodesRepositoryLive(quill: Quill.Postgres[SnakeCase]) extends DiagnosticCodesRepository:
    import quill.* //gives us access to methods such as run, query, filter or lift

    inline given schema: SchemaMeta[DiagnosticCodes] = schemaMeta[DiagnosticCodes]("diagnosticcodes") // Table name `"DiagnosticCodes"`
   

    override def create(diagnosticCode: DiagnosticCodes): Task[DiagnosticCodes] = 
        run {
            query[DiagnosticCodes]
                .insertValue(lift(diagnosticCode))
                .returning(d => d)
        } 

    override def getByCode(code: String): Task[Option[DiagnosticCodes]] =
    run {
      query[DiagnosticCodes].filter(_.diagnosticCode == lift(code))
    }.map(_.headOption)

    override def update(code: String, apply: DiagnosticCodes => DiagnosticCodes): Task[DiagnosticCodes] =
        for {
        current <- getByCode(code).someOrFail(new RuntimeException(s"Diagnostic code not found: $code"))
        updated = apply(current)
        _ <- run {
            query[DiagnosticCodes]
            .filter(_.diagnosticCode == lift(code))
            .updateValue(lift(updated))
        }
        } yield updated
    
    override def delete(code: String): Task[DiagnosticCodes] =
        for {
            existing <- getByCode(code).someOrFail(new RuntimeException(s"Diagnostic code not found: $code"))
            _ <- run {
            query[DiagnosticCodes]
                .filter(_.diagnosticCode == lift(code))
                .delete
            }
        } yield existing


object DiagnosticCodesRepositoryLive:
    val layer = ZLayer {
        ZIO.service[Quill.Postgres[SnakeCase]].map(quill => DiagnosticCodesRepositoryLive(quill))
    }