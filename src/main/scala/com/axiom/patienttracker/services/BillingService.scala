package com.axiom.patienttracker.services

import zio.*
import com.axiom.patienttracker.domain.data.Billing
import com.axiom.patienttracker.http.requests.CreateBillingRequest
import com.axiom.patienttracker.repositories.BillingRepository
import com.axiom.patienttracker.http.requests.UpdateBillingRequest
//Logic
// in between the HTTP layer and the Database layer
trait BillingService:
    def create(req: CreateBillingRequest): Task[Billing]
    def getById(billingId: Long): Task[Option[Billing]]
    def getAll(): Task[List[Billing]]
    def update(billingId: Long, req: UpdateBillingRequest): Task[Billing]
    def delete(billingId: Long): Task[Billing]

class BillingServiceLive private (repo: BillingRepository) extends BillingService:
    
    override def create(req: CreateBillingRequest): Task[Billing] = 
        repo.create(req.toBilling(-1L))

    
    override def getById(billingId: Long): Task[Option[Billing]] = 
        repo.getById(billingId).flatMap{
            case Some(billing) => ZIO.succeed(Some(billing))
            case None => ZIO.fail(new NoSuchElementException(s"Encounter with ID $billingId not found"))
        }
    
    override def getAll(): Task[List[Billing]] =
        repo.getAll()

    override def update(billingId: Long, req: UpdateBillingRequest): Task[Billing] = 
        repo.getById(billingId).flatMap {
            case Some(billing) =>
                repo.update(billingId, existing => applyUpdates(existing, req))
            case None =>
                ZIO.fail(new NoSuchElementException(s"Account with ID $billingId not found"))
        }

    override def delete(billingId: Long): Task[Billing] = 
        repo.delete(billingId)

    private def applyUpdates(existing: Billing, update: UpdateBillingRequest): Billing =
    existing.copy(
        billingCode = update.billingCode.getOrElse(existing.billingCode),
        diagnosticCode = update.diagnosticCode.getOrElse(existing.diagnosticCode),
        recordedTime = update.recordedTime.orElse(existing.recordedTime),
        unitCount = update.unitCount.getOrElse(existing.unitCount),
        Notes = update.Notes.orElse(existing.Notes)
    )



object BillingServiceLive:
    val layer = ZLayer{
        for{
            repo <- ZIO.service[BillingRepository]
        } yield new BillingServiceLive(repo)
    }