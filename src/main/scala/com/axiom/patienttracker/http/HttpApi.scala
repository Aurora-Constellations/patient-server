package com.axiom.patienttracker.http

import com.axiom.patienttracker.http.controllers.*

object HttpApi:
    def gatherRoutes(controllers: List[BaseController]) =
        controllers.flatMap(_.routes)

    def makeControllers = for {
        patient <- PatientController.makeZIO
        report <- ReportController.makeZIO
        // Keep adding controllers here
    } yield List(patient, report)

    val endpointsZIO = makeControllers.map(gatherRoutes)

