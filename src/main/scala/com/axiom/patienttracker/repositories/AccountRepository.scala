package com.axiom.patienttracker.repositories

import zio.* 
import io.getquill.* 
import io.getquill.jdbczio.Quill

import com.axiom.patienttracker.domain.data.Account

trait AccountRepository:
    def create(account: Account): Task[Account]
    def getById(accountId: Long): Task[Option[Account]]
    def getByPatientId(patientId: Long): Task[Option[Account]]
    def getAll: Task[List[Account]]

class AccountRepositoryLive(quill: Quill.Postgres[SnakeCase]) extends AccountRepository:
    import quill.* //gives us access to methods such as run, query, filter or lift

    inline given schema: SchemaMeta[Account] = schemaMeta[Account]("accounts") // Table name `"accounts"`
    inline given insMeta: InsertMeta[Account] = insertMeta[Account](_.accountId) // Columns to generate on its own
    inline given upMeta: UpdateMeta[Account] = updateMeta[Account](_.accountId)
    override def create(account: Account): Task[Account] = 
        run {
            query[Account]
                .insertValue(lift(account))
                .returning(d => d)
        } // During complilation we can see the type safe query

    override def getById(accountId: Long): Task[Option[Account]] = 
        run {
            query[Account]
                .filter(_.accountId == lift(accountId))
        }.map(_.headOption)

    override def getByPatientId(patientId: Long): Task[Option[Account]] = 
        run {
            query[Account]
                .filter(_.patientId == lift(patientId))
        }.map(_.headOption)

    override def getAll: Task[List[Account]] = 
        run {
            query[Account]
        } // No filter, so it returns all accounts

object AccountRepositoryLive:
    val layer = ZLayer {
        ZIO.service[Quill.Postgres[SnakeCase]].map(quill => AccountRepositoryLive(quill))
    }