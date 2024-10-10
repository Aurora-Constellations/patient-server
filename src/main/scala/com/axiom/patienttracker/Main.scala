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
import com.axiom.patienttracker.repositories.UserRepositoryLive
import com.axiom.patienttracker.config.Configs
import com.axiom.patienttracker.config.JWTConfig
import com.axiom.patienttracker.repositories.RecoveryTokensRepositoryLive
import com.axiom.patienttracker.config.RecoveryTokensConfig
import com.axiom.dataimport.api.importpatients
import zio.http.Server.RequestStreaming

object Main extends ZIOAppDefault:
  val customConfig = Server.Config.default.copy(
    requestStreaming = RequestStreaming.Enabled // increase request body length
  )
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
      // Replace Server.default with custom config
      //Server.default,
      Server.live,
      ZLayer.succeed(customConfig),
      //service
      PatientServiceLive.layer,
      ReportServiceLive.layer,
      UserServiceLive.layer,
      JWTServiceLive.configuredLayer,
      EmailServiceLive.configuredLayer,
      FileUploadServiceLive.layer,
      // repo dependency
      PatientRepositoryLive.layer,
      ReportRepositoryLive.layer,
      UserRepositoryLive.layer,
      RecoveryTokensRepositoryLive.configuredLayer,
      // postgres dependency, qill layer
      Repository.dataLayer
    )

// TODO Try to run inside zio
@main def runDataImportsCSV(): Unit =
  println(importpatients.map(patient => patient.unitNumber))
