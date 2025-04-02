package com.axiom.patienttracker.repositories

import zio.* 
import zio.test.*

import org.testcontainers.containers.PostgreSQLContainer

import com.axiom.patienttracker.syntax.*
import java.time.format.DateTimeFormatter
import java.time.LocalDate
import com.axiom.patienttracker.domain.data.Patient
import javax.sql.DataSource
import org.postgresql.ds.PGSimpleDataSource

object PatientRepositorySpec extends ZIOSpecDefault:
    def stringToDate(dateString: String) =
        val pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date = LocalDate.parse(dateString, pattern) //returns yyyy-MM-dd
        date
    override def spec: Spec[TestEnvironment & Scope, Any] = 
        suite("Patient Repository Test")(
            test("create patient"):
                val date = stringToDate("2024-08-21")
                val program = for{
                    repo <- ZIO.service[PatientRepository]
                    patient <- repo.create(Patient(1L, "TB00202100","testing", "the jvm", "male", stringToDate("2024-08-21")))
                }yield patient
                program.assert{
                    case Patient(_, "TB00202100","testing", "the jvm", "male", date, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _) => true
                    case _ => false
                }
        ).provide(
            PatientRepositoryLive.layer,
            dataSourceLayer,
            Repository.quillLayer
        )
    
    //test containers
    // allows us to spin up docker instances
    // very lightweight and used for testing
    // spawn a Postgres instance on Docker just for the test
    def createContainer() = 
        // Typed with F bounded polymorphism
        val container: PostgreSQLContainer[Nothing] = 
            PostgreSQLContainer("postgres")
                .withInitScript("sql/patients.sql") //TODO store this under src/test/resources
        container.start()
        container

    // Create a DataSource to connect to the postgres
    def createDataSource(container: PostgreSQLContainer[Nothing]): DataSource =
        val dataSource = new PGSimpleDataSource()
        dataSource.setUrl(container.getJdbcUrl())
        dataSource.setUser(container.getUsername())
        dataSource.setPassword(container.getPassword())
        dataSource

    // use the DataSource (as a ZLayer) to build the Quill instance (as a ZLayer)
    val dataSourceLayer = ZLayer{
        for{
            container <- ZIO.acquireRelease(ZIO.attempt(createContainer()))(container => ZIO.attempt(container.stop())).ignoreLogged
            dataSource <- ZIO.attempt(createDataSource(container))
        }yield dataSource
    }
