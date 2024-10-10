package com.axiom.patienttracker.http.controllers

import zio.*
import com.axiom.patienttracker.http.endpoints.FileUploadEndpoints
import sttp.model.Part
import sttp.tapir.server.ServerEndpoint
import com.axiom.patienttracker.services.FileUploadService

class FileUploadController private (service: FileUploadService) extends BaseController with FileUploadEndpoints:
    val file: ServerEndpoint[Any, Task] = uploadTextFileEndpoint.serverLogic { req =>
        service.uploadLogic(req).either
    }
    override val routes: List[ServerEndpoint[Any, Task]] = List(file)

object FileUploadController:
    val makeZIO =  for{
        service <- ZIO.service[FileUploadService]
    }yield new FileUploadController(service)
