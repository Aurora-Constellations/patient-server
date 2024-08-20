package com.axiom.patienttracker

import sttp.tapir.*
import sttp.tapir.server.ziohttp.*
import zio.*
import zio.http.Server
import com.axiom.patienttracker.http.controllers.PatientController
import com.axiom.patienttracker.http.HttpApi
import com.axiom.patienttracker.services.*
import com.axiom.patienttracker.repositories.PatientRepositoryLive
import com.axiom.patienttracker.repositories.Repository
import com.axiom.patienttracker.repositories.ReportRepositoryLive


object Main extends ZIOAppDefault:

  val serverProgram = for {
    endpoints <- HttpApi.endpointsZIO
    server <- Server.serve(
      ZioHttpInterpreter(
        ZioHttpServerOptions.default
      ).toHttp(endpoints)
    )
  } yield ()
  override def run =
    serverProgram.provide(
      Server.default,
      //service
      PatientServiceLive.layer,
      ReportServiceLive.layer,
      // repo dependency
      PatientRepositoryLive.layer,
      ReportRepositoryLive.layer,
      // postgres dependency, qill layer
      Repository.dataLayer
    )