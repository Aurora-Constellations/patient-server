package com.axiom.patienttracker.repositories

import zio.* 
import io.getquill.* 
import io.getquill.jdbczio.Quill

import com.axiom.patienttracker.domain.data.Doctor

trait DoctorRepository:
    def create(doctor: Doctor): Task[Doctor]
    def update(providerId: String, op: Doctor => Doctor): Task[Doctor]
    def getByProviderId(providerId: String): Task[Option[Doctor]]
    def getAllDoctors(): Task[List[Doctor]]
    def delete(providerId: String): Task[Doctor] 

class DoctorRepositoryLive(quill: Quill.Postgres[SnakeCase]) extends DoctorRepository:
    import quill.* //gives us access to methods such as run, query, filter or lift

    inline given schema: SchemaMeta[Doctor] = schemaMeta[Doctor]("doctors") // Table name `"doctors"`
    inline given insMeta: InsertMeta[Doctor] = insertMeta[Doctor](_.doctorId) // Columns to generate on its own
    inline given upMeta: UpdateMeta[Doctor] = updateMeta[Doctor](_.doctorId)
    override def create(doctor: Doctor): Task[Doctor] = 
        run {
            query[Doctor]
                .insertValue(lift(doctor))
                .returning(d => d)
        } // During complilation we can see the type safe query
        /* 
        The RETURNING in quill gives us the columns in the exact order of our case class.
        Our scala compiler doesnot know the which string values belongs to which column but our quill knows
         */

    override def getByProviderId(providerId: String): Task[Option[Doctor]] =
        run {
            query[Doctor].filter(_.providerId == lift(providerId))
        }.map(_.headOption)

    override def getAllDoctors(): Task[List[Doctor]] =
        run {
            query[Doctor]
        }

    
    override def update(providerId: String, op: Doctor => Doctor): Task[Doctor] =
        for {
            current <- getByProviderId(providerId)
            .someOrFail(new RuntimeException(s"Could not update: missing Provider ID: $providerId"))
            updated <- run {
            query[Doctor]
                .filter(_.providerId == lift(providerId))
                .updateValue(lift(op(current)))
                .returning(d => d)
            }
        } yield updated
        
    override def delete(providerId: String): Task[Doctor] =
        for {
            current <- getByProviderId(providerId)
            .someOrFail(new RuntimeException(s"No doctor found with providerId: $providerId"))
            deleted <- run {
            query[Doctor].filter(_.providerId == lift(providerId)).delete
            }.as(current) // return the deleted doctor info
        } yield deleted



object DoctorRepositoryLive:
    val layer = ZLayer {
        ZIO.service[Quill.Postgres[SnakeCase]].map(quill => DoctorRepositoryLive(quill))
    }