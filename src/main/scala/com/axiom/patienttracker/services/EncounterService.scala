package com.axiom.patienttracker.services

import zio.*
import com.axiom.patienttracker.domain.data.Encounter
import com.axiom.patienttracker.http.requests.CreateEncounterRequest
import com.axiom.patienttracker.repositories.EncounterRepository

//Logic
// in between the HTTP layer and the Database layer
trait EncounterService:
    def create(req: CreateEncounterRequest): Task[Encounter]

class EncounterServiceLive private (repo: EncounterRepository) extends EncounterService:
    override def create(req: CreateEncounterRequest): Task[Encounter] = 
        repo.create(req.toEncounter(-1L))


object EncounterServiceLive:
    val layer = ZLayer{
        for{
            repo <- ZIO.service[EncounterRepository]
        } yield new EncounterServiceLive(repo)
    }