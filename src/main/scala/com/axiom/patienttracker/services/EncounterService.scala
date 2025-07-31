package com.axiom.patienttracker.services

import zio.*
import com.axiom.patienttracker.domain.data.Encounter
import com.axiom.patienttracker.http.requests.CreateEncounterRequest
import com.axiom.patienttracker.repositories.EncounterRepository

//Logic
// in between the HTTP layer and the Database layer
trait EncounterService:
    def create(req: CreateEncounterRequest): Task[Encounter]
    def getById(encounterId: Long): Task[Option[Encounter]]
    def getAll(): Task[List[Encounter]]
    def getByAccountId(accountId: Long): Task[List[Encounter]]
    def getByDoctorId(doctorId: Long): Task[List[Encounter]]
    def getByADId(accountId: Long, doctorId: Long): Task[List[Encounter]]

class EncounterServiceLive private (repo: EncounterRepository) extends EncounterService:
    override def create(req: CreateEncounterRequest): Task[Encounter] = 
        repo.create(req.toEncounter(-1L))

    override def getById(encounterId: Long): Task[Option[Encounter]] = 
        repo.getById(encounterId).flatMap{
            case Some(encounter) => ZIO.succeed(Some(encounter))
            case None => ZIO.fail(new NoSuchElementException(s"Encounter with ID $encounterId not found"))
        }

    override def getAll(): Task[List[Encounter]] =
        repo.getAll()

    override def getByAccountId(accountId: Long): Task[List[Encounter]] =
        repo.getByAccountId(accountId)

    override def getByDoctorId(doctorId: Long): Task[List[Encounter]] =
        repo.getByDoctorId(doctorId)

    override def getByADId(accountId: Long, doctorId: Long): Task[List[Encounter]] = 
        repo.getByADId(accountId, doctorId)

object EncounterServiceLive:
    val layer = ZLayer{
        for{
            repo <- ZIO.service[EncounterRepository]
        } yield new EncounterServiceLive(repo)
    }