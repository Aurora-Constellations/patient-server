package com.axiom.patienttracker.services

import zio.*
import com.axiom.patienttracker.domain.data.*
import collection.mutable
import com.axiom.patienttracker.http.requests.CreatePatientRequest
import com.axiom.patienttracker.repositories.PatientRepository
import com.axiom.patienttracker.http.requests.UpdatePatientRequest

//Logic
// in between the HTTP layer and the Database layer
trait PatientService:
    def create(req: CreatePatientRequest): Task[Patient]
    def update(id: Long, req: UpdatePatientRequest): Task[Patient]
    def getAll: Task[List[Patient]]
    def getById(id: Long): Task[Option[Patient]]
    def getByUnitNumber(unitNumber: String): Task[Option[Patient]]
    def delete(id: Long): Task[Patient]

class PatientServiceLive private (repo: PatientRepository) extends PatientService:

    private def applyUpdates(patient: Patient, updatedPatient: UpdatePatientRequest): Patient = 
        patient.copy(
            account = updatedPatient.account.getOrElse(patient.account),
            unitNumber = updatedPatient.unitNumber.getOrElse(patient.unitNumber),
            patient = updatedPatient.patient.getOrElse(patient.patient),
            sex = updatedPatient.sex.getOrElse(patient.sex),
            dob = updatedPatient.dob.getOrElse(patient.dob),
            hcn = updatedPatient.hcn.orElse(patient.hcn),
            admitDate = updatedPatient.admitDate.getOrElse(patient.admitDate),
            location = updatedPatient.location.getOrElse(patient.location),
            room = updatedPatient.room.getOrElse(patient.room),
            bed = updatedPatient.bed.getOrElse(patient.bed),
            admitting = updatedPatient.admitting.orElse(patient.admitting),
            attending = updatedPatient.attending.orElse(patient.attending),
            family = updatedPatient.family.orElse(patient.family),
            famPriv = updatedPatient.famPriv.orElse(patient.famPriv),
            hosp = updatedPatient.hosp.orElse(patient.hosp),
            flag = updatedPatient.flag.orElse(patient.flag),
            service = updatedPatient.service.orElse(patient.service)
        )
    override def create(req: CreatePatientRequest): Task[Patient] = 
        repo.create(req.toPatient(-1L))
    override def update(id: Long, req: UpdatePatientRequest): Task[Patient] = 
        for {
            existingPatient <- repo.getById(id).someOrFail(new RuntimeException(s"Could not update: missing id: $id"))
            updatedPatient = applyUpdates(existingPatient, req)
            result <- repo.update(id, patient => updatedPatient)
        } yield result
    override def getAll: Task[List[Patient]] = 
        repo.getAll()
    override def getById(id: Long): Task[Option[Patient]] = 
        repo.getById(id)
    override def getByUnitNumber(unitNumber: String): Task[Option[Patient]] = 
        repo.getByUnitNumber(unitNumber)

    override def delete(id: Long): Task[Patient] = 
        repo.delete(id)

object PatientServiceLive:
    val layer = ZLayer{
        for{
            repo <- ZIO.service[PatientRepository]
        } yield new PatientServiceLive(repo)
    }