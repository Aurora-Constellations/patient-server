package com.axiom.patienttracker.services

import zio.*
import com.axiom.patienttracker.domain.data.BillingCode
import com.axiom.patienttracker.repositories.BillingCodeRepository
import com.axiom.patienttracker.http.requests.UpdateBillingCodeRequest

//Logic
// in between the HTTP layer and the Database layer
trait BillingCodeService:
    def create(req: BillingCode): Task[BillingCode]
    def getBillingCode(billingCode: String): Task[Option[BillingCode]]
    def updateBillingCode(billingCode: String, req: UpdateBillingCodeRequest): Task[BillingCode]

class BillingCodeServiceLive private (repo: BillingCodeRepository) extends BillingCodeService:
    override def create(req: BillingCode): Task[BillingCode] = 
        repo.create(req)

    override def getBillingCode(billingCode: String): Task[Option[BillingCode]] = 
        repo.getBillingCode(billingCode)

    override def updateBillingCode(billingCode: String, req: UpdateBillingCodeRequest): Task[BillingCode] = 
        repo.updateBillingCode(billingCode, existing => applyUpdates(existing, req))

    private def applyUpdates(existing: BillingCode, update: UpdateBillingCodeRequest): BillingCode =
        existing.copy(
            label = update.label.getOrElse(existing.label),
            amount = update.amount.getOrElse(existing.amount),
            description = update.description.orElse(existing.description)
        )

object BillingCodeServiceLive:
    val layer = ZLayer{
        for{
            repo <- ZIO.service[BillingCodeRepository]
        } yield new BillingCodeServiceLive(repo)
    }