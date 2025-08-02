package com.axiom.patienttracker.repositories

import zio.* 
import io.getquill.* 
import io.getquill.jdbczio.Quill
import io.getquill.MappedEncoding
import zio.Chunk

import com.axiom.patienttracker.domain.data.Encounter

trait EncounterRepository:
    def create(encounter: Encounter): Task[Encounter]
    def getById(encounterId: Long): Task[Option[Encounter]]
    def getAll(): Task[List[Encounter]]
    def getByAccountId(accountId: Long): Task[List[Encounter]]
    def getByDoctorId(doctorId: Long): Task[List[Encounter]]
    def getByADId(accountId: Long, doctorId: Long): Task[List[Encounter]]
    def update(encounterId: Long, op: Encounter => Encounter): Task[Encounter]
    def delete(encounterId: Long): Task[Encounter]

class EncounterRepositoryLive(quill: Quill.Postgres[SnakeCase]) extends EncounterRepository:
    import quill.* //gives us access to methods such as run, query, filter or lift

    inline given schema: SchemaMeta[Encounter] = schemaMeta[Encounter]("encounters") // Table name `"encounters"`
    inline given insMeta: InsertMeta[Encounter] = insertMeta[Encounter](_.encounterId) // Columns to generate on its own
    inline given upMeta: UpdateMeta[Encounter] = updateMeta[Encounter](_.encounterId)

    override def create(encounter: Encounter): Task[Encounter] = 
        run {
            query[Encounter]
                .insertValue(lift(encounter))
                .returning(d => d)
        } // During complilation we can see the type safe query

    override def getById(encounterId: Long): Task[Option[Encounter]] = 
        run {
            query[Encounter]
                .filter(_.encounterId == lift(encounterId))
        }.map(_.headOption) // Returns a list, we take the first element or None if empty

    override def getAll(): Task[List[Encounter]] = 
        run {
            query[Encounter]
        } // Returns a list of all encounters

    override def getByAccountId(accountId: Long): Task[List[Encounter]] = 
        run {
            query[Encounter]
                .filter(_.accountId == lift(accountId))
        }

    override def getByDoctorId(doctorId: Long): Task[List[Encounter]] = 
        run {
            query[Encounter]
                .filter(_.doctorId == lift(doctorId))
        }

    override def getByADId(accountId: Long, doctorId: Long): Task[List[Encounter]] = 
        run {
            query[Encounter]
                .filter(e => e.accountId == lift(accountId) && e.doctorId == lift(doctorId))
        }

    override def update(encounterId: Long, op: Encounter => Encounter): Task[Encounter] = 
        for {
            current <- getById(encounterId)
                .someOrFail(new RuntimeException(s"Could not update: missing Encounter ID: $encounterId"))
            updated <- run {
                query[Encounter]
                    .filter(_.encounterId == lift(encounterId))
                    .updateValue(lift(op(current)))
                    .returning(d => d)
            }
        } yield updated

    override def delete(encounterId: Long): Task[Encounter] = 
        for {
            encounter <- getById(encounterId)
                .someOrFail(new RuntimeException(s"Could not delete: missing Encounter ID: $encounterId"))
            _ <- run {
                query[Encounter]
                    .filter(_.encounterId == lift(encounterId))
                    .delete
            }
        } yield encounter


object EncounterRepositoryLive:
    val layer = ZLayer {
        ZIO.service[Quill.Postgres[SnakeCase]].map(quill => EncounterRepositoryLive(quill))
    }