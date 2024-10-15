package com.axiom.patienttracker.http.controllers

import zio.*
import com.axiom.patienttracker.http.endpoints.FileEndpoints
import sttp.model.Part
import sttp.tapir.server.ServerEndpoint
import com.axiom.patienttracker.services.FileService

class FileController private (service: FileService) extends BaseController with FileEndpoints:
    val uploadFile: ServerEndpoint[Any, Task] = uploadTextFileEndpoint.serverLogic { req =>
        service.uploadLogic(req).either
    }

    val importFileData: ServerEndpoint[Any, Task] = importFileEndpoint.serverLogic{ req => 
        service.importFile().either    
    }
    override val routes: List[ServerEndpoint[Any, Task]] = List(uploadFile, importFileData)

object FileController:
    val makeZIO =  for{
        service <- ZIO.service[FileService]
    }yield new FileController(service)
