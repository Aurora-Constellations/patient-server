package com.axiom.patienttracker.services

import zio.*
import com.axiom.patienttracker.domain.data.Billing
import com.axiom.patienttracker.http.requests.CreateBillingRequest
import com.axiom.patienttracker.repositories.BillingRepository

//Logic
// in between the HTTP layer and the Database layer
trait BillingService:
    def create(req: CreateBillingRequest): Task[Billing]
    def getById(billingId: Long): Task[Option[Billing]]

class BillingServiceLive private (repo: BillingRepository) extends BillingService:
    
    override def create(req: CreateBillingRequest): Task[Billing] = 
        repo.create(req.toBilling(-1L))

    
    override def getById(billingId: Long): Task[Option[Billing]] = 
        repo.getById(billingId).flatMap{
            case Some(billing) => ZIO.succeed(Some(billing))
            case None => ZIO.fail(new NoSuchElementException(s"Encounter with ID $billingId not found"))
        }


object BillingServiceLive:
    val layer = ZLayer{
        for{
            repo <- ZIO.service[BillingRepository]
        } yield new BillingServiceLive(repo)
    }