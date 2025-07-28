package com.axiom.patienttracker.services

import zio.*
import com.axiom.patienttracker.domain.data.Account
import com.axiom.patienttracker.http.requests.CreateAccountRequest
import com.axiom.patienttracker.repositories.AccountRepository
import com.axiom.patienttracker.http.requests.UpdateAccountRequest

//Logic
// in between the HTTP layer and the Database layer
trait AccountService:
    def create(req: CreateAccountRequest): Task[Account]
    def getById(accountId: Long): Task[Option[Account]]
    def getByPatientId(patientId: Long): Task[Option[Account]]
    def getAll: Task[List[Account]]
    def update(accountId: Long, req: UpdateAccountRequest): Task[Account]

class AccountServiceLive private (repo: AccountRepository) extends AccountService:
    override def create(req: CreateAccountRequest): Task[Account] = 
        repo.create(req.toAccount(-1L))

    override def getById(accountId: Long): Task[Option[Account]] = 
        repo.getById(accountId)

    override def getByPatientId(patientId: Long): Task[Option[Account]] = 
        repo.getByPatientId(patientId)

    override def getAll: Task[List[Account]] = 
        repo.getAll

    override def update(accountId: Long, req: UpdateAccountRequest): Task[Account] =
        repo.getById(accountId).flatMap {
            case Some(account) =>
                repo.update(accountId, existing => applyUpdates(existing, req))
            case None =>
                ZIO.fail(new NoSuchElementException(s"Account with ID $accountId not found"))
        }

    private def applyUpdates(existing: Account, update: UpdateAccountRequest): Account =
        existing.copy(
            startDate = update.startDate.getOrElse(existing.startDate),
            endDate = update.endDate.orElse(existing.endDate),
            notes = update.notes.orElse(existing.notes)
        )

object AccountServiceLive:
    val layer = ZLayer{
        for{
            repo <- ZIO.service[AccountRepository]
        } yield new AccountServiceLive(repo)
    }