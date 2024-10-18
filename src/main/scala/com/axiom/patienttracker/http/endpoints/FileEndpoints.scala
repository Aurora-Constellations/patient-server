package com.axiom.patienttracker.http.endpoints

import java.io.File
import sttp.tapir.*
import sttp.tapir.json.zio.*
import sttp.tapir.server.model.EndpointExtensions.*
import sttp.model.Part

trait FileEndpoints extends BaseEndpoint:
  val uploadTextFileEndpoint = baseEndpoint
    .tag("upload")
    .name("upload")
    .description("upload text file")
    .in("upload")
    .post
    .in(multipartBody)
    .out(plainBody[String])

  val importFileEndpoint = baseEndpoint
    .tag("import")
    .name("import")
    .description("getting the latest file data")
    .in("import")
    .get
    .out(jsonBody[String])