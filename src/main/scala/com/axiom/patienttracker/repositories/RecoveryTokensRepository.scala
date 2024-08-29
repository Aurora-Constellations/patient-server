package com.axiom.patienttracker.repositories

import zio.*
import io.getquill.*
import io.getquill.jdbczio.Quill
import com.axiom.patienttracker.domain.data.PasswordRecoveryToken
import com.axiom.patienttracker.config.Configs
import com.axiom.patienttracker.config.RecoveryTokensConfig

trait RecoveryTokensRepository:
    def getToken(email: String): Task[Option[String]]
    def checkToken(email: String, token: String): Task[Boolean]

class RecoveryTokensRepositoryLive private (tokenConfig: RecoveryTokensConfig, quill: Quill.Postgres[SnakeCase], userRepo: UserRepository) extends RecoveryTokensRepository:
    import quill.*

    inline given schema: SchemaMeta[PasswordRecoveryToken] = schemaMeta[PasswordRecoveryToken]("recovery_tokens") //table name
    inline given insMeta: InsertMeta[PasswordRecoveryToken] = insertMeta[PasswordRecoveryToken]()// nothing to exclude
    inline given upMeta: UpdateMeta[PasswordRecoveryToken] = updateMeta[PasswordRecoveryToken](_.email)

    private val tokenDuration = 600000 // TODO pass from config file, 10 minutes of expiration

    private def randomUpperCaseString(len: Int): Task[String] = 
        ZIO.succeed(scala.util.Random.alphanumeric.take(len).mkString.toUpperCase)

    private def findToken(email: String): Task[Option[String]] = 
        run{
            query[PasswordRecoveryToken]
            .filter(_.email == lift(email))
        }.map(_.headOption.map(_.token))

    private def replaceToken(email: String): Task[String] = 
        for {
            token <- randomUpperCaseString(8)
            _ <- run{
                    query[PasswordRecoveryToken]
                    .updateValue(lift(PasswordRecoveryToken(email, token, java.lang.System.currentTimeMillis() + tokenDuration)))
                    .returning(prt => prt)
                }
        } yield token

    private def generateToken(email: String): Task[String] = 
        for{
            token <- randomUpperCaseString(8)
            _ <- run{
                query[PasswordRecoveryToken]
                .insertValue(lift(PasswordRecoveryToken(email, token, java.lang.System.currentTimeMillis() + tokenDuration)))
                .returning(prt => prt)
            }
        } yield token

    private def makeFreshToken(email: String): Task[String] =  
        //find token in the table
        // if exist then replace orelse create
        findToken(email).flatMap{
            case Some(token) => replaceToken(email)
            case None => generateToken(email)
        }

    override def getToken(email: String): Task[Option[String]] = 
        //check user in the database
        userRepo.getByEmail(email).flatMap{
            case None => ZIO.none
            case Some(user) => makeFreshToken(email).map(Some(_))
        }
        // if user exits then make fresh token

    override def checkToken(email: String, token: String): Task[Boolean] = 
        run{
            query[PasswordRecoveryToken]
            .filter(prt => prt.email == lift(email) && prt.token == lift(token))
        }.map(_.nonEmpty)

object RecoveryTokensRepositoryLive:
    val layer = ZLayer{
        for{
            config <- ZIO.service[RecoveryTokensConfig]
            quill <- ZIO.service[Quill.Postgres[SnakeCase]]
            userRepo <- ZIO.service[UserRepository]
        }yield new RecoveryTokensRepositoryLive(config, quill, userRepo)
    }

    val configuredLayer = Configs.makeLayer[RecoveryTokensConfig]("patienttracker.recoverytokens") >>> layer
