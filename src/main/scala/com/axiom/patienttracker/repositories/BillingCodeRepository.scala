package com.axiom.patienttracker.repositories

import zio.* 
import io.getquill.* 
import io.getquill.jdbczio.Quill

import com.axiom.patienttracker.domain.data.BillingCode

trait BillingCodeRepository:
    def create(billingCode: BillingCode): Task[BillingCode]
    def getBillingCode(billingCode: String): Task[Option[BillingCode]]
    def getAllBillingCodes(): Task[List[BillingCode]]
    def updateBillingCode(billingCode: String, op: BillingCode => BillingCode): Task[BillingCode]

class BillingCodeRepositoryLive(quill: Quill.Postgres[SnakeCase]) extends BillingCodeRepository:
    import quill.* //gives us access to methods such as run, query, filter or lift

    inline given schema: SchemaMeta[BillingCode] = schemaMeta[BillingCode]("billing_codes") // Table name `"billing_codes"`
    inline given insMeta: InsertMeta[BillingCode] = insertMeta[BillingCode]() // No arguments because no auto-generated id
    inline given upMeta: UpdateMeta[BillingCode] = updateMeta[BillingCode]()
    override def create(billingCode: BillingCode): Task[BillingCode] = 
        run {
            query[BillingCode]
                .insertValue(lift(billingCode))
                .returning(d => d)
        } // During complilation we can see the type safe query

    override def getBillingCode(billingCode: String): Task[Option[BillingCode]] =
        run {
            query[BillingCode]
                .filter(_.billingCode == lift(billingCode))
        }.map(_.headOption) // Returns the first element wrapped in an Option

    override def getAllBillingCodes(): Task[List[BillingCode]] =
        run {
            query[BillingCode]
        } // Returns all billing codes
    
    override def updateBillingCode(billingCode: String, op: BillingCode => BillingCode): Task[BillingCode] =
        for {
            existing <- getBillingCode(billingCode)
                .someOrFail(new RuntimeException(s"Could not update: missing Billing Code: $billingCode"))
            updated <- run {
                query[BillingCode]
                    .filter(_.billingCode == lift(billingCode))
                    .updateValue(lift(op(existing)))
                    .returning(d => d)
            }
        } yield updated

object BillingCodeRepositoryLive:
    val layer = ZLayer {
        ZIO.service[Quill.Postgres[SnakeCase]].map(quill => BillingCodeRepositoryLive(quill))
    }