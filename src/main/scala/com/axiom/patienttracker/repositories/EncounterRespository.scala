package com.axiom.patienttracker.repositories

import zio.* 
import io.getquill.* 
import io.getquill.jdbczio.Quill
import io.getquill.MappedEncoding
import zio.Chunk

import com.axiom.patienttracker.domain.data.Encounter

trait EncounterRepository:
    def create(encounter: Encounter): Task[Encounter]

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

object EncounterRepositoryLive:
    val layer = ZLayer {
        ZIO.service[Quill.Postgres[SnakeCase]].map(quill => EncounterRepositoryLive(quill))
    }