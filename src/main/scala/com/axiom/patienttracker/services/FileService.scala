package com.axiom.patienttracker.services

import zio.*
import sttp.model.Part
import java.nio.file.{Files, Paths}
import java.nio.charset.StandardCharsets
import com.axiom.dataimport.api.importpatients
import com.axiom.patienttracker.config.FileConfig
import com.axiom.patienttracker.config.Configs
import com.axiom.patienttracker.repositories.PatientRepository
import com.axiom.patienttracker.domain.data.Patient

trait FileService:
  def uploadLogic(parts: Seq[Part[Array[Byte]]]): Task[String]
  def importFile(): Task[String]

class FileServiceLive private (config: FileConfig, repo: PatientRepository)
    extends FileService:
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

  override def importFile(): Task[String] =
    // println(s"Path:${config.path}")
    for {
      patients <- ZIO.attempt(importpatients)
      uploadedPatientRecords <- ZIO.collectAll(patients.map(patient =>
        repo.create(
          Patient(
            -1L,
            patient.accountNumber,
            patient.unitNumber,
            patient.lastName,
            patient.firstName,
            patient.sex,
            patient.dob,
            patient.hcn,
            patient.admitDate,
            patient.floor,
            patient.room,
            patient.bed,
            patient.mrp,
            patient.admittingPhys,
            patient.family,
            patient.famPriv,
            patient.hosp,
            patient.flag,
            patient.service,
            patient.address1,
            patient.address2,
            patient.city,
            patient.province,
            patient.postalCode,
            patient.homePhoneNumber,
            patient.workPhoneNumber,
            patient.ohip,
            patient.attending,
            patient.collab1,
            patient.collab2
          )
        )
      ))
      numberOfRecords <- ZIO.attempt(uploadedPatientRecords.length)
    } yield s"$numberOfRecords number of patient data was imported to the database from the file"

object FileServiceLive:
  val layer = ZLayer {
    for {
      config <- ZIO.service[FileConfig]
      repo <- ZIO.service[PatientRepository]
    } yield new FileServiceLive(config, repo)
  }

  val configuredLayer = Configs.makeLayer[FileConfig]("app.adm") >>> layer
