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
            override def update(id: Long, op: Patient => Patient): Task[Patient] = 
                ZIO.attempt:
                    val patient = db(id) // can crash
                    db += (id -> op(patient))
                    patient
            override def delete(id: Long): Task[Patient] = 
                ZIO.attempt{
                    val patient = db(id)
                    db -= id
                    patient
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
                val patientZIO = service(_.create(CreatePatientRequest("TB00202100","testing", "the jvm", "male", stringToDate("2024-08-21"))))
                patientZIO.assert{patient =>
                    patient.unitNumber == "TB00202100" &&
                    patient.firstName == "testing" &&
                    patient.lastName == "the jvm" &&
                    patient.sex == "male" &&
                    patient.dob == stringToDate("2024-08-21")
                }
        ).provide(
            PatientServiceLive.layer,
            stubRepoLayer
        )