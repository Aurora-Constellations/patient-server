package com.axiom.patienttracker.services

import zio.*
import com.axiom.patienttracker.domain.data.Billing
import com.axiom.patienttracker.http.requests.CreateBillingRequest
import com.axiom.patienttracker.repositories.BillingRepository

//Logic
// in between the HTTP layer and the Database layer
trait BillingService:
    def create(req: CreateBillingRequest): Task[Billing]

class BillingServiceLive private (repo: BillingRepository) extends BillingService:
    
    override def create(req: CreateBillingRequest): Task[Billing] = 
        repo.create(req.toBilling(-1L))


object BillingServiceLive:
    val layer = ZLayer{
        for{
            repo <- ZIO.service[BillingRepository]
        } yield new BillingServiceLive(repo)
    }