package com.axiom.patienttracker.http.controllers

import zio.*
import sttp.tapir.*

import sttp.tapir.server.ServerEndpoint
import com.axiom.patienttracker.http.endpoints.EncounterEndpoints
import com.axiom.patienttracker.services.EncounterService

class EncounterController private (service: EncounterService) extends BaseController with EncounterEndpoints:
    val encounter: ServerEndpoint[Any, Task] = encounterEndpoint
        .serverLogicSuccess[Task](_ => ZIO.succeed("All set!"))

    val createEncounter: ServerEndpoint[Any, Task] = createEncounterEndpoint
        .serverLogic[Task] { 
            request => service.create(request).either
        }

    val getEncounterById: ServerEndpoint[Any, Task] = getEncounterByIdEndpoint
        .serverLogic[Task] {
            encounterId => service.getById(encounterId).either
        }

    val getAllEncounters: ServerEndpoint[Any, Task] = getAllEncountersEndpoint
        .serverLogic[Task] {
            _ => service.getAll().either
        }

    val getEncounterByAccountId: ServerEndpoint[Any, Task] = getEncounterByAccountIdEndpoint
        .serverLogic[Task] {
            accountId => service.getByAccountId(accountId).either
        }

    val getEncounterByDoctorId: ServerEndpoint[Any, Task] = getEncounterByDoctorIdEndpoint
        .serverLogic[Task] {
            doctorId => service.getByDoctorId(doctorId).either
        }

    val getByAccountAndDoctorId: ServerEndpoint[Any, Task] = getByAccountAndDoctorIdEndpoint
        .serverLogic[Task] {
            (accountId, doctorId) => service.getByADId(accountId, doctorId).either
        }

    val updateEncounter: ServerEndpoint[Any, Task] = updateEncounterEndpoint
        .serverLogic[Task] {
            case (encounterId, req) => service.update(encounterId, req).either
        }

    val deleteEncounter: ServerEndpoint[Any, Task] = deleteEncounterEndpoint
        .serverLogic[Task] {
            encounterId => service.delete(encounterId).either
        }

    override val routes: List[ServerEndpoint[Any, Task]] = List(encounter,
        createEncounter, getEncounterById, getAllEncounters, getEncounterByAccountId,
        getEncounterByDoctorId, getByAccountAndDoctorId, updateEncounter, deleteEncounter)

object EncounterController  :
    val makeZIO = for {
        service <- ZIO.service[EncounterService]
    }  yield new EncounterController(service)