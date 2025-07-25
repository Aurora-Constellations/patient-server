package com.axiom.patienttracker.services

import zio.*
import com.axiom.patienttracker.domain.data.Account
import com.axiom.patienttracker.http.requests.CreateAccountRequest
import com.axiom.patienttracker.repositories.AccountRepository

//Logic
// in between the HTTP layer and the Database layer
trait AccountService:
    def create(req: CreateAccountRequest): Task[Account]


class AccountServiceLive private (repo: AccountRepository) extends AccountService:
    override def create(req: CreateAccountRequest): Task[Account] = 
        repo.create(req.toAccount(-1L))


object AccountServiceLive:
    val layer = ZLayer{
        for{
            repo <- ZIO.service[AccountRepository]
        } yield new AccountServiceLive(repo)
    }