package com.axiom.patienttracker

import sttp.tapir.*
import sttp.tapir.server.ziohttp.*
import zio.*
import zio.http.Server
import com.axiom.patienttracker.http.controllers.PatientController
import com.axiom.patienttracker.http.HttpApi

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
      Server.default
    )