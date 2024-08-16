package com.axiom.patienttracker.http.controllers

import zio.*
import zio.test.*
import sttp.client3.*
import zio.json.*
import sttp.tapir.generic.auto.*
import sttp.tapir.server.stub.TapirStubInterpreter
import sttp.client3.testing.SttpBackendStub
import sttp.monad.MonadError // we have same package in cats too
import sttp.tapir.ztapir.RIOMonadError
import com.axiom.patienttracker.http.requests.CreatePatientRequest
import com.axiom.patienttracker.domain.data.Patient
import java.time.format.DateTimeFormatter
import java.time.LocalDate

object PatientControllerSpec extends ZIOSpecDefault:
    // building MonadErro by ourselves
    private given zioME: MonadError[Task] = new RIOMonadError[Any]
    def stringToDate(dateString: String) =
        val pattern = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val date = LocalDate.parse(dateString, pattern) //returns yyyy-MM-dd
        date

    override def spec: Spec[TestEnvironment & Scope, Any] = 
        suite("PatientControllerSpec")(
            test("Post Patient"):
                // we need for comprehension because our Controller in private 
                // So the constructor can only be created effectfully
                for{
                    //create controller
                    controller <- PatientController.makeZIO
                    // build tapir backend
                    backendStub <- ZIO.succeed(
                        TapirStubInterpreter(SttpBackendStub(MonadError[Task]))
                        .whenServerEndpointRunLogic(controller.create)
                        .backend() //backend stub, to invoke synchronously with HTTP request
                        )
                    // run http request
                    response <- basicRequest // similar to endpoint value when we create patient endpoints
                        .post(uri"/patients") // uri is a custom interpretor
                        .body(CreatePatientRequest("456","rock", "the jvm", "male", stringToDate("2024-08-16")))
                        .send(backendStub)
                    // inspect http response\
                } yield response.body

                assertZIO(program)(
                    Assertion.assertion("inspect http response from getAll"):
                        respBody =>
                            respBody.toOption.flatMap(_.fromJson[Patient].toOption) //Option[Patient]
                            .contains(Patient(1, "rock", "the jvm", "male", stringToDate("2024-08-16")))
                )
        )
