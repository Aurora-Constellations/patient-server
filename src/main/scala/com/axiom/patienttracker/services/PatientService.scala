package com.axiom.patienttracker.services

import zio.*
import com.axiom.patienttracker.domain.data.*
import collection.mutable
import com.axiom.patienttracker.http.requests.CreatePatientRequest
import com.axiom.patienttracker.repositories.PatientRepository

//Logic
// in between the HTTP layer and the Database layer
trait PatientService:
    def create(req: CreatePatientRequest): Task[Patient]
    def getAll: Task[List[Patient]]
    def getById(id: Long): Task[Option[Patient]]
    def getByUnitNumber(unitNumber: String): Task[Option[Patient]]

class PatientServiceLive private (repo: PatientRepository) extends PatientService:
    override def create(req: CreatePatientRequest): Task[Patient] = 
        repo.create(req.toPatient(-1L))
    override def getAll: Task[List[Patient]] = 
        repo.getAll()
    override def getById(id: Long): Task[Option[Patient]] = 
        repo.getById(id)
    override def getByUnitNumber(unitNumber: String): Task[Option[Patient]] = 
        repo.getByUnitNumber(unitNumber)

object PatientServiceLive:
    val layer = ZLayer{
        for{
            repo <- ZIO.service[PatientRepository]
        } yield new PatientServiceLive(repo)
    }