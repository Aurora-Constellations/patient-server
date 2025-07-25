package com.axiom.patienttracker.services

import zio.*
import com.axiom.patienttracker.domain.data.Doctor
import com.axiom.patienttracker.http.requests.CreateDoctorRequest
import com.axiom.patienttracker.repositories.DoctorRepository
import com.axiom.patienttracker.http.requests.UpdateDoctorRequest

//Logic
// in between the HTTP layer and the Database layer
trait DoctorService:
    def create(req: CreateDoctorRequest): Task[Doctor]
    def update(req: UpdateDoctorRequest): Task[Doctor]
    def delete(providerId: String): Task[Doctor] 
    


class DoctorServiceLive private (repo: DoctorRepository) extends DoctorService:
    override def create(req: CreateDoctorRequest): Task[Doctor] = 
        repo.create(req.toDoctor(-1L))

    private def applyUpdates(existing: Doctor, update: UpdateDoctorRequest): Doctor =
        existing.copy(
        name = update.name.getOrElse(existing.name)
        
        )
    override def update(req: UpdateDoctorRequest): Task[Doctor] = 
        req.providerId match
        case Some(id) =>
            repo.update(id, existing => applyUpdates(existing, req))
        case None =>
            ZIO.fail(new RuntimeException("Missing providerId in update request"))
    
    override def delete(providerId: String): Task[Doctor] =
    repo.delete(providerId)


object DoctorServiceLive:
    val layer = ZLayer{
        for{
            repo <- ZIO.service[DoctorRepository]
        } yield new DoctorServiceLive(repo)
    }