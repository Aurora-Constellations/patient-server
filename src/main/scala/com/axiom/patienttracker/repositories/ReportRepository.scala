package com.axiom.patienttracker.repositories

import zio.*
import io.getquill.* 
import io.getquill.jdbczio.Quill
import com.axiom.patienttracker.domain.data.Report

trait ReportRepository:
    def create(report: Report): Task[Report]
    def getById(id: Long): Task[Option[Report]]
    def getByPatientId(id: Long): Task[List[Report]]
    def getByUnitNumber(unitNumber: String): Task[List[Report]]
    //TODO get all patient reported with diabetes or hypertension
    def update(id: Long, op: Report => Report): Task[Report]
    def delete(id: Long): Task[Report]

class ReportRepositoryLive(quill: Quill.Postgres[SnakeCase]) extends ReportRepository:
    import quill.*

    inline given schema: SchemaMeta[Report] = schemaMeta[Report]("reports")
    inline given insMeta: InsertMeta[Report] = insertMeta[Report](_.id)
    inline given upMeta: UpdateMeta[Report] = updateMeta[Report](_.id)

    override def create(report: Report): Task[Report] = 
        run{
            query[Report]
                .insertValue(lift(report))
                .returning(r => r)
        }

    override def getById(id: Long): Task[Option[Report]] = 
        run{
            query[Report]
                .filter(_.id == lift(id))
        }.map(_.headOption)

    override def getByPatientId(id: Long): Task[List[Report]] = 
        run{
            query[Report]
                .filter(_.patientId == lift(id))
        }

    override def getByUnitNumber(unitNumber: String): Task[List[Report]] = 
        run{
            query[Report]
                .filter(_.unitNumber == lift(unitNumber))
        }
    override def update(id: Long, op: Report => Report): Task[Report] = 
        for{
            current <- getById(id).someOrFail(new RuntimeException(s"Could not update: missing id: $id"))
            updated <- run{
                query[Report]
                    .updateValue(lift(op(current)))
                    .returning(r => r)
            }
        } yield updated
    override def delete(id: Long): Task[Report] = 
        run{
            query[Report]
                .filter(_.id == lift(id))
                .delete
                .returning(r => r)
        }

object ReportRepositoryLive:
    val layer = ZLayer{
        for{
            quill <- ZIO.service[Quill.Postgres[SnakeCase]]
        } yield new ReportRepositoryLive(quill)
    }