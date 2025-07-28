package com.axiom.patienttracker.services

import zio.*
import com.axiom.patienttracker.domain.data.BillingCode
import com.axiom.patienttracker.repositories.BillingCodeRepository

//Logic
// in between the HTTP layer and the Database layer
trait BillingCodeService:
    def create(req: BillingCode): Task[BillingCode]

class BillingCodeServiceLive private (repo: BillingCodeRepository) extends BillingCodeService:
    override def create(req: BillingCode): Task[BillingCode] = 
        repo.create(req)

object BillingCodeServiceLive:
    val layer = ZLayer{
        for{
            repo <- ZIO.service[BillingCodeRepository]
        } yield new BillingCodeServiceLive(repo)
    }