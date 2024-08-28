package com.axiom.patienttracker.services

import zio.*
import zio.test.*
import com.axiom.patienttracker.domain.data.User
import com.axiom.patienttracker.config.JWTConfig

object JWTServiceSpec extends ZIOSpecDefault:
    override def spec: Spec[TestEnvironment & Scope, Any] = 
        suite("JWTServiceSpec")(
            test("create and validate token"):
                for{
                    service <- ZIO.service[JWTService]
                    userToken <- service.createToken(User(1l, "aurora@constellations.com", "not required"))
                    userId <- service.verifyToken(userToken.token)
                }yield assertTrue(userId.id == 1L && userId.email == "aurora@constellations.com")
        ).provide(
            JWTServiceLive.layer,
            ZLayer.succeed(JWTConfig("secret", 3600))// 1 hour
        )