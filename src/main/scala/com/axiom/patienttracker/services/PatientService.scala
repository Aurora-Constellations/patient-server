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
            unitNumber = updatedPatient.unitNumber.getOrElse(patient.unitNumber),
            firstName = updatedPatient.firstName.getOrElse(patient.firstName),
            lastName = updatedPatient.lastName.getOrElse(patient.lastName),
            sex = updatedPatient.sex.getOrElse(patient.sex),
            dob = updatedPatient.dob.getOrElse(patient.dob),
            hcn = updatedPatient.hcn.orElse(patient.hcn),
            family = updatedPatient.family.orElse(patient.family),
            famPriv = updatedPatient.famPriv.orElse(patient.famPriv),
            hosp = updatedPatient.hosp.orElse(patient.hosp),
            flag = updatedPatient.flag.orElse(patient.flag),
            address1 = updatedPatient.address1.orElse(patient.address1),
            address2 = updatedPatient.address2.orElse(patient.address2),
            city = updatedPatient.city.orElse(patient.city),
            province = updatedPatient.province.orElse(patient.province),
            postalCode = updatedPatient.postalCode.orElse(patient.postalCode),
            homePhoneNumber = updatedPatient.homePhoneNumber.orElse(patient.homePhoneNumber),
            workPhoneNumber = updatedPatient.workPhoneNumber.orElse(patient.workPhoneNumber),
            ohip = updatedPatient.ohip.orElse(patient.ohip),
            familyPhysician = updatedPatient.familyPhysician.orElse(patient.familyPhysician),
            attending = updatedPatient.attending.orElse(patient.attending),
            collab1 = updatedPatient.collab1.orElse(patient.collab1),
            collab2 = updatedPatient.collab2.orElse(patient.collab2)
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