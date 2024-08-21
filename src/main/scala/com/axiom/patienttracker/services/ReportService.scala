package com.axiom.patienttracker.services

import zio.*
import com.axiom.patienttracker.domain.data.Report
import com.axiom.patienttracker.repositories.ReportRepository
import com.axiom.patienttracker.repositories.ReportRepositoryLive
import com.axiom.patienttracker.http.requests.CreateReportRequest

trait ReportService:
    def create(report: CreateReportRequest): Task[Report]
    def getById(id: Long): Task[Option[Report]]
    def getByPatientId(id: Long): Task[List[Report]]
    def getByUnitNumber(unitNumber: String): Task[List[Report]]
    def getAll(): Task[List[Report]]

class ReportServiceLive private (repo: ReportRepositoryLive) extends ReportService:
    override def create(report: CreateReportRequest): Task[Report] = 
        repo.create(report.toReport(-1L))
    override def getById(id: Long): Task[Option[Report]] = 
        repo.getById(id)
    override def getByPatientId(id: Long): Task[List[Report]] = 
        repo.getByPatientId(id)
    override def getByUnitNumber(unitNumber: String): Task[List[Report]] = 
        repo.getByUnitNumber(unitNumber)
    override def getAll(): Task[List[Report]] = 
        repo.getAll()

object ReportServiceLive:
    val layer = ZLayer{
        ZIO.service[ReportRepositoryLive].map(repo => ReportServiceLive(repo))
    }
