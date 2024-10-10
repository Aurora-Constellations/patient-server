package com.axiom.patienttracker.services

import zio.*
import sttp.model.Part
import java.nio.file.{Files, Paths}
import java.nio.charset.StandardCharsets

trait FileUploadService:
  def uploadLogic(parts: Seq[Part[Array[Byte]]]): Task[String]

class FileUploadServiceLive private extends FileUploadService:
  override def uploadLogic(parts: Seq[Part[Array[Byte]]]): Task[String] = {
    parts.find(_.name == "file") match {
      case Some(filePart) =>
        ZIO.attempt {
          val fileName = filePart.fileName.getOrElse("uploaded.txt")
          val content = new String(filePart.body, StandardCharsets.UTF_8)

          // Save the content to a file (or do further processing)
          Files.write(Paths.get(s"./src/main/resources/$fileName"), filePart.body)

          s"File '$fileName' uploaded successfully!"
        }
      case None =>
        ZIO.fail(new Exception("No file found in the request"))
    }
  }

object FileUploadServiceLive:
    val layer = ZLayer{
        ZIO.succeed(new FileUploadServiceLive())
    }