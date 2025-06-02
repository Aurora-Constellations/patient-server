package com.axiom.patienttracker.repositories

import zio.* 
import io.getquill.* 
import io.getquill.jdbczio.Quill

import com.axiom.patienttracker.domain.data.Patient
import scala.runtime.BoxesRunTime

trait PatientRepository:
    def create(patient: Patient): Task[Patient]
    def update(unitNumber: String, op: Patient => Patient): Task[Patient]
    def delete(unitNumber: String): Task[Patient]
    def getById(id: Long): Task[Option[Patient]]
    def getByUnitNumber(unitNumber: String): Task[Option[Patient]]
    def getAll(): Task[List[Patient]]

class PatientRepositoryLive(quill: Quill.Postgres[SnakeCase]) extends PatientRepository:
    import quill.* //gives us access to methods such as run, query, filter or lift

    inline given schema: SchemaMeta[Patient] = schemaMeta[Patient]("patients") // Table name `"patient"`
    inline given insMeta: InsertMeta[Patient] = insertMeta[Patient](_.id) // Columns to generate on its own
    inline given upMeta: UpdateMeta[Patient] = updateMeta[Patient](_.id)
    override def create(patient: Patient): Task[Patient] = 
        run {
            query[Patient]
                .insertValue(lift(patient))
                .returning(p => p)
        } // During complilation we can see the type safe query
        /* 
        The RETURNING in quill gives us the columns in the exact order of our case class.
        Our scala compiler doesnot know the which string values belongs to which column but our quill knows
         */
    override def getById(id: Long): Task[Option[Patient]] = 
        run{
            // whenever we want to translate a value in the JVM to the value in Postgres or translated by quill we wrap it with lift
            query[Patient]
                .filter(_.id == lift(id))
        }.map(_.headOption)

    override def getByUnitNumber(unitNumber: String): Task[Option[Patient]] = 
        run{
            // whenever we want to translate a value in the JVM to the value in Postgres or translated by quill we wrap it with lift
            query[Patient]
                .filter(_.unitNumber == lift(unitNumber))
        }.map(_.headOption)

    override def getAll(): Task[List[Patient]] = 
        run{
            query[Patient]
        }
    override def update(unitNumber: String, op: Patient => Patient): Task[Patient] = 
        for{
            current <- getByUnitNumber(unitNumber).someOrFail(new RuntimeException(s"Could not update: missing Unit Number: $unitNumber"))// someOrFail helps us to force change the return type from Task[Option[Patient]] to Task[Patient]
            updated <- run{
                query[Patient]
                    .filter(_.unitNumber == lift(unitNumber))
                    .updateValue(lift(op(current)))// applying op function
                    .returning(p => p)
            }
        } yield updated

    override def delete(unitNumber: String): Task[Patient] = 
        run{
            query[Patient]
                .filter(_.unitNumber == lift(unitNumber))
                .delete
                .returning(p => p)
        }

object PatientRepositoryLive:
    val layer = ZLayer {
        ZIO.service[Quill.Postgres[SnakeCase]].map(quill => PatientRepositoryLive(quill))
    }