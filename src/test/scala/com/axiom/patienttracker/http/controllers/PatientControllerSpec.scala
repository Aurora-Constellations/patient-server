package com.axiom.patienttracker.http.controllers

import zio.*
import zio.test.*
import zio.json.*

import sttp.client3.*
import sttp.tapir.generic.auto.*
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.server.stub.TapirStubInterpreter
import sttp.client3.testing.SttpBackendStub
import sttp.monad.MonadError // we have same package in cats too
import sttp.tapir.ztapir.RIOMonadError

import java.time.format.DateTimeFormatter
import java.time.LocalDate

import com.axiom.patienttracker.services.PatientServiceLive
import com.axiom.patienttracker.http.requests.CreatePatientRequest
import com.axiom.patienttracker.domain.data.Patient
import com.axiom.patienttracker.repositories.PatientRepositoryLive
import com.axiom.patienttracker.repositories.Repository

import com.axiom.patienttracker.syntax.*
import com.axiom.patienttracker.services.PatientService
import com.axiom.patienttracker.http.requests.UpdatePatientRequest

object PatientControllerSpec extends ZIOSpecDefault:
    // building MonadErro by ourselves
    private given zioME: MonadError[Task] = new RIOMonadError[Any]// ZIO monad error

    private val serviceStub = new PatientService {
        override def create(req: CreatePatientRequest): Task[Patient] = ???
        override def update(unitNumber: String, req: UpdatePatientRequest): Task[Patient] = ???
        override def getAll: Task[List[Patient]] = ???
        override def getById(id: Long): Task[Option[Patient]] = ???
        override def getByUnitNumber(unitNumber: String): Task[Option[Patient]] = ???
        override def delete(unitNumber: String): Task[Patient] = ???
    }

    def stringToDate(dateString: String) =
        val pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date = LocalDate.parse(dateString, pattern) //returns yyyy-MM-dd
        date

    private def backendStubZIO(endpointFunc: PatientController => ServerEndpoint[Any, Task]) = 
        // we need `for comprehension` because our Controller in private
        // So the constructor can only be created effectfully
        for{
            //create controller
            controller <- PatientController.makeZIO
            // build tapir backend
            backendStub <- ZIO.succeed(
                TapirStubInterpreter(SttpBackendStub(MonadError[Task]))// or can write SttpBackendStub(zioME)
                .whenServerEndpointRunLogic(endpointFunc(controller))
                .backend() //backend stub, to invoke synchronously with HTTP request
            )
        } yield backendStub

    override def spec: Spec[TestEnvironment & Scope, Any] = 
        suite("PatientControllerSpec")(
            test("Post Patient"):
                // we need `for comprehension` because our Controller in private
                // So the constructor can only be created effectfully
                val program = for{
                    backendStub <- backendStubZIO(_.create)
                    // run http request
                    response <- basicRequest // similar to endpoint value when we create patient endpoints
                        .post(uri"/patients") // uri is a custom interpretor as "/patients" will be just string orelse
                        .body(CreatePatientRequest("TB309089/23","TB00202100","testing", "the jvm", "male", Some(stringToDate("2024-08-21"))).toJson)
                        .send(backendStub)
                } yield response.body
                
                // inspect http response
                program.assert{    
                    respBody =>
                        respBody.toOption.flatMap(_.fromJson[Patient].toOption) //Option[Patient]
                            .contains(Patient(27, "TB309089/23", "TB00202100", "testing", "the jvm", "male", Some(stringToDate("2024-08-21"))))
                }
            ,
            test("Get all Patients"):
                val program = for {
                backendStub <- backendStubZIO(_.getAll)
                response <- basicRequest
                    .get(uri"/patients")
                    .send(backendStub)
                } yield response.body
                program.assert{    
                    respBody =>
                        respBody.toOption.flatMap(_.fromJson[List[Patient]].toOption) //Option[Patient]
                            .contains(List())
                }
            ,
            test("Get Patients by id"):
                val program = for {
                backendStub <- backendStubZIO(_.getById)
                response <- basicRequest
                    .get(uri"/patients/27")
                    .send(backendStub)
                } yield response.body
                program.assert{    
                    respBody =>
                        respBody.toOption.flatMap(_.fromJson[Patient].toOption) //Option[Patient]
                            .contains(Patient(27, "TB309089/23", "TB00202100", "testing", "the jvm", "male", Some(stringToDate("2024-08-21"))))
                }
        ).provide(
                ZLayer.succeed(serviceStub)
        )
