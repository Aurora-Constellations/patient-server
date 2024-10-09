package com.axiom.patienttracker.services

import zio.*
import com.axiom.patienttracker.domain.data.*
import com.auth0.jwt.*
import java.time.Instant
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.JWTVerifier.BaseVerification
import com.axiom.patienttracker.config.JWTConfig
import com.axiom.patienttracker.config.Configs


trait JWTService:
    def createToken(user: User): Task[UserToken]
    def verifyToken(token: String): Task[UserID]


class JWTServiceLive (jwtConfig: JWTConfig, clock: java.time.Clock) extends JWTService:

    private val ISSUER = "AuroraConstellations"
    private val CLAIM_USERNAME = "username"

    private val algorithm = Algorithm.HMAC512(jwtConfig.secret)
    private val verifier: JWTVerifier = JWT
            .require(algorithm)
            .withIssuer(ISSUER)
            .asInstanceOf[BaseVerification]
            .build(clock)
        
    override def createToken(user: User): Task[UserToken] = for{
        now <- ZIO.attempt(clock.instant())
        expiration <- ZIO.succeed(now.plusSeconds(jwtConfig.ttl))
        token <- ZIO.attempt(
            JWT // it is a builder pattern
                .create() // builder
                .withIssuer(ISSUER) // authority, used for validation
                .withIssuedAt(now) // time of issue
                .withExpiresAt(expiration) // expires after 10 days
                .withSubject(user.id.toString) // content of JWT, user identifier
                //we can store permissions, access rights, etc
                .withClaim(CLAIM_USERNAME, user.email) // store user's email, "username" is the identifier for the same
                .sign(algorithm)// hashing the JWT using the specified algorithm
        )
    }yield UserToken(user.email, token, expiration.getEpochSecond)
    // Token prints the 3 parts, separated by "." dot
    //eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9. --> This is the header, specifies the type of token and type of algorithm used for hashing
    //eyJpc3MiOiJBdXJvcmFDb25zdGVsbGF0aW9ucyIsImlhdCI6MTcyNDY5NDUzOSwiZXhwIjoxNzI3Mjg2NTM5LCJzdWIiOiIxIiwidXNlcm5hbWUiOiJhdXJvcmFAY29uc3RlbGxhdGlvbi5jb20ifQ. --> the payload, conatins all the claims
    //qTTf_s7SOP7zzP5vt9ag86gE014QH8aw_VnTsL0gpOxxV2UXSljIK0SSQkig2ELTnFOWdw_JGZmcywnK6S3Tog --> signature, uses the algorithm to hash the header, all the claims, algorithm, the "secret" which is used as salt for hashing JWT token

    override def verifyToken(token: String): Task[UserID] = for{
        decoded <- ZIO.attempt(verifier.verify(token))
        userId <- ZIO.attempt(
            UserID(
                decoded.getSubject().toLong,
                decoded.getClaim(CLAIM_USERNAME).asString()
            )
        )
    } yield userId

object JWTServiceLive:
    val layer = ZLayer{
        for{
            jwtConfig <- ZIO.service[JWTConfig]
            clock <- Clock.javaClock
        }yield new JWTServiceLive(jwtConfig, clock)
    }
    // ZLayer.succeed(JWTConfig("secret", 30 * 24 * 3600))
    // from application.conf -> turns into case class -> turns the ZLayer
    // >>> -> it feeds the config layer to layer
    val configuredLayer = Configs.makeLayer[JWTConfig]("patienttracker.jwt") >>> layer 

object JWTServiceDemo extends ZIOAppDefault:
    val program = for{
        service <- ZIO.service[JWTService]
        userToken <- service.createToken(User(1l, "drkim@aurora.com", "patients"))
        _ <- Console.printLine(userToken)
        userId <- service.verifyToken(userToken.token)
        _ <- Console.printLine(userId.toString)
    } yield()

    override def run: ZIO[Any & (ZIOAppArgs & Scope), Any, Any] = 
        program.provide(
            JWTServiceLive.layer,
            Configs.makeLayer[JWTConfig]("patienttracker.jwt")
        )
