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
import zio.http.Middleware.CorsConfig
import zio.http.Header.{AccessControlAllowMethods, AccessControlAllowOrigin, Origin}
import zio.http.Method
import zio.http.Header.AccessControlAllowHeaders
import zio.http.Middleware
import com.axiom.patienttracker.repositories.DoctorRepositoryLive
import com.axiom.patienttracker.repositories.AccountRepositoryLive
import com.axiom.patienttracker.repositories.BillingCodeRepositoryLive
import com.axiom.patienttracker.repositories.DiagnosticCodesRepositoryLive

object Main extends ZIOAppDefault:
  // Define your CORS configuration
  private val corsConfig: CorsConfig = CorsConfig(
    allowedOrigin = _ => Some(AccessControlAllowOrigin.All),
    allowedMethods = AccessControlAllowMethods(
      Method.GET,
      Method.POST,
      Method.PUT,
      Method.DELETE,
      Method.OPTIONS  // Include OPTIONS for preflight requests
    ),
    allowedHeaders = AccessControlAllowHeaders.All
  )

  val customConfig = Server.Config.default.copy(
    requestStreaming = RequestStreaming.Enabled // increase request body length
  )
  val serverProgram = for {
    endpoints <- HttpApi.endpointsZIO
    corsMiddleware = Middleware.cors(corsConfig)
    server <- Server.serve(
      corsMiddleware(
        ZioHttpInterpreter(
          ZioHttpServerOptions.default
        ).toHttp(endpoints)
      )
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
      DoctorServiceLive.layer,
      AccountServiceLive.layer,
      DiagnosticCodeServiceLive.layer,
      BillingCodeServiceLive.layer,
      JWTServiceLive.configuredLayer,
      EmailServiceLive.configuredLayer,
      FileServiceLive.configuredLayer,
      // repo dependency
      PatientRepositoryLive.layer,
      ReportRepositoryLive.layer,
      UserRepositoryLive.layer,
      DoctorRepositoryLive.layer,
      AccountRepositoryLive.layer,
      DiagnosticCodesRepositoryLive.layer,
      BillingCodeRepositoryLive.layer,
      RecoveryTokensRepositoryLive.configuredLayer,
      // postgres dependency, qill layer
      Repository.dataLayer
    )

// TODO Try to run inside zio
@main def runDataImportsCSV(): Unit =
  println(importpatients.map(patient => patient.unitNumber))
