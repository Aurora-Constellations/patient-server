package com.axiom.patienttracker.services

import zio.* 
import zio.test.* 

import java.time.format.DateTimeFormatter
import java.time.LocalDate

import com.axiom.patienttracker.syntax.*
import com.axiom.patienttracker.http.requests.CreatePatientRequest
import com.axiom.patienttracker.repositories.Repository
import com.axiom.patienttracker.repositories.PatientRepository
import com.axiom.patienttracker.domain.data.Patient

object PatientServiceSpec extends ZIOSpecDefault:

    val service = ZIO.serviceWithZIO[PatientService]

    val stubRepoLayer = ZLayer.succeed(
        new PatientRepository {
            val db = collection.mutable.Map[Long, Patient]()
            override def create(patient: Patient): Task[Patient] = 
                ZIO.succeed{
                    val nextId = db.keys.maxOption.getOrElse(0L) + 1
                    val newPatient = patient.copy(id = nextId)
                    db += (nextId -> newPatient)
                    newPatient
                }
            override def getAll(): Task[List[Patient]] = 
                ZIO.succeed(db.values.toList)
            override def getById(id: Long): Task[Option[Patient]] = 
                ZIO.succeed(db.get(id))
            override def getByUnitNumber(unitNumber: String): Task[Option[Patient]] = 
                ZIO.succeed(db.values.find(_.unitNumber == unitNumber))
            override def update(unitNumber: String, op: Patient => Patient): Task[Patient] = 
                ZIO.attempt {
                    db.values.find(_.unitNumber == unitNumber) match {
                        case Some(patient) =>
                            val updatedPatient = op(patient)
                            db += (patient.id -> updatedPatient)
                            updatedPatient
                        case None =>
                            throw new NoSuchElementException(s"Patient with unitNumber $unitNumber not found")
                    }
                }
            override def delete(unitNumber: String): Task[Patient] = 
                ZIO.attempt{
                    db.values.find(_.unitNumber == unitNumber) match {
                        case Some(patient) =>
                            db -= patient.id
                            patient
                        case None =>
                            throw new NoSuchElementException(s"Patient with unitNumber $unitNumber not found")
                    }
                }
        }
    )

    def stringToDate(dateString: String) =
        val pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date = LocalDate.parse(dateString, pattern) //returns yyyy-MM-dd
        date
    override def spec: Spec[TestEnvironment & Scope, Any] = 
        suite("Patient service test")(
            test("create patient"):
                val patientZIO = service(_.create(CreatePatientRequest("TB309089/23","TB00202100","testing", "the jvm", "male", Some(stringToDate("2024-08-21")))))
                patientZIO.assert{patient =>
                    patient.accountNumber == "TB309089/23" &&
                    patient.unitNumber == "TB00202100" &&
                    patient.firstName == "testing" &&
                    patient.lastName == "the jvm" &&
                    patient.sex == "male" &&
                    patient.dob == Some(stringToDate("2024-08-21"))
                }
        ).provide(
            PatientServiceLive.layer,
            stubRepoLayer
        )