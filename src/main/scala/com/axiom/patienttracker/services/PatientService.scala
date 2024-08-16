package com.axiom.patienttracker.services

import zio.*
import com.axiom.patienttracker.domain.data.*
import collection.mutable
import com.axiom.patienttracker.http.requests.CreatePatientRequest

//Logic
// in between the HTTP layer and the Database layer
trait PatientService:
    def create(req: CreatePatientRequest): Task[Patient]
    def getAll: Task[List[Patient]]
    def getById(id: Long): Task[Option[Patient]]
    def getByUnitNumber(unitNumber: String): Task[Option[Patient]]

object PatientService:
    val dummyLayer = ZLayer.succeed(new PatientServiceDummy)

class PatientServiceDummy extends PatientService:
    // Currently using in-memory Database
    val db = mutable.Map[Long, Patient]()

    override def create(req: CreatePatientRequest): Task[Patient] = 
        ZIO.succeed:
            val newId = db.keys.maxOption.getOrElse(0L) + 1 // 0L beacuse we using Long
            val newPatient = req.toPatient(newId)
            db += (newId -> newPatient)
            newPatient

    override def getAll: Task[List[Patient]] = 
        ZIO.succeed(db.values.toList)

    override def getById(id: Long): Task[Option[Patient]] = 
        ZIO.succeed(db.get(id))

    override def getByUnitNumber(unitNumber: String): Task[Option[Patient]] = 
        ZIO.succeed(db.values.find(_.unitNumber == unitNumber))

