package com.axiom.patienttracker.services

import zio.*
import sttp.model.Part
import java.nio.file.{Files, Paths}
import java.nio.charset.StandardCharsets
import com.axiom.dataimport.api.importpatients
import com.axiom.patienttracker.config.FileConfig
import com.axiom.patienttracker.config.Configs

trait FileService:
  def uploadLogic(parts: Seq[Part[Array[Byte]]]): Task[String]
  def importFile(): Task[List[String]]

class FileServiceLive private (config: FileConfig) extends FileService:
  override def uploadLogic(parts: Seq[Part[Array[Byte]]]): Task[String] = {
    parts.find(_.name == "file") match {
      case Some(filePart) =>
        ZIO.attempt {
          val actualFileName = filePart.fileName.getOrElse("uploaded.txt")
          val newFileName = config.path.split("/").last
          val content = new String(filePart.body, StandardCharsets.UTF_8)

          // Save the content to a file (or do further processing)
          Files.write(
            Paths.get(config.path),
            filePart.body
          )

          s"File '$actualFileName' uploaded successfully! as $newFileName"
        }
      case None =>
        ZIO.fail(new Exception("No file found in the request"))
    }
  }

  override def importFile(): Task[List[String]] =
    // println(s"Path:${config.path}")
    for {
      patients <- ZIO.attempt(importpatients)
      unitNumbers = patients.map(_.unitNumber)
    } yield unitNumbers

object FileServiceLive:
  val layer = ZLayer {
    for{
      config <- ZIO.service[FileConfig]
    }yield new FileServiceLive(config)
  }

  val configuredLayer = Configs.makeLayer[FileConfig]("app.adm") >>> layer