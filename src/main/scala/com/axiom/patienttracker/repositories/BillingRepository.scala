package com.axiom.patienttracker.repositories

import zio.* 
import io.getquill.* 
import io.getquill.jdbczio.Quill
import io.getquill.MappedEncoding
import zio.Chunk

import com.axiom.patienttracker.domain.data.Billing

trait BillingRepository:
    def create(billings: Billing): Task[Billing]
    def getById(encounterId: Long): Task[Option[Billing]]

class BillingRepositoryLive(quill: Quill.Postgres[SnakeCase]) extends BillingRepository:
    import quill.* //gives us access to methods such as run, query, filter or lift

    inline given schema: SchemaMeta[Billing] = schemaMeta[Billing]("billings") // Table name `"billings"`
    inline given insMeta: InsertMeta[Billing] = insertMeta[Billing](_.billingId) // Columns to generate on its own
    inline given upMeta: UpdateMeta[Billing] = updateMeta[Billing](_.billingId)

    override def create(billings: Billing): Task[Billing] = 
        run {
            query[Billing]
                .insertValue(lift(billings))
                .returning(d => d)
        } // During complilation we can see the type safe query

    override def getById(billingId: Long): Task[Option[Billing]] = 
        run {
            query[Billing]
                .filter(_.billingId == lift(billingId))
        }.map(_.headOption) // Returns a list, we take the first element or None if empty

object BillingRepositoryLive:
    val layer = ZLayer {
        ZIO.service[Quill.Postgres[SnakeCase]].map(quill => BillingRepositoryLive(quill))
    }