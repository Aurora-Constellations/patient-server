package com.axiom.patienttracker.http.endpoints

import java.io.File
import sttp.tapir.*
import sttp.tapir.server.model.EndpointExtensions.*
import sttp.model.Part

trait FileUploadEndpoints extends BaseEndpoint:
  val uploadTextFileEndpoint = baseEndpoint
    .tag("upload")
    .name("upload")
    .description("upload text file")
    .in("upload")
    .post
    .in(multipartBody)
    .out(plainBody[String])