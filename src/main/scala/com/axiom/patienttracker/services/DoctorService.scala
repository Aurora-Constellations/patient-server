package com.axiom.patienttracker.services

import zio.*
import com.axiom.patienttracker.domain.data.Doctor
import com.axiom.patienttracker.http.requests.CreateDoctorRequest
import com.axiom.patienttracker.repositories.DoctorRepository

//Logic
// in between the HTTP layer and the Database layer
trait DoctorService:
    def create(req: CreateDoctorRequest): Task[Doctor]

class DoctorServiceLive private (repo: DoctorRepository) extends DoctorService:
    override def create(req: CreateDoctorRequest): Task[Doctor] = 
        repo.create(req.toDoctor(-1L))


object DoctorServiceLive:
    val layer = ZLayer{
        for{
            repo <- ZIO.service[DoctorRepository]
        } yield new DoctorServiceLive(repo)
    }