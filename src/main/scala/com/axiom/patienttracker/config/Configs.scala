package com.axiom.patienttracker.config

import zio.*
import zio.config.*
import zio.config.magnolia.* //old scala library for type class derivation, it was introduced even before scala3
import com.typesafe.config.ConfigFactory
import zio.config.typesafe.TypesafeConfig

object Configs:
    // FIXME In future, might need to move to the newer version of magnolia and fix this
    //case-class type: C
    def makeLayer[C](path: String)(using desc: Descriptor[C], tag: Tag[C]): ZLayer[Any, Throwable, C] = 
        TypesafeConfig.fromTypesafeConfig(
            //ConfigFactory.load() -> parses the application.conf file
            ZIO.attempt(ConfigFactory.load().getConfig(path)),// returns hash-map of key value pair, path in our case would be patienttracker.jwt
            descriptor[C] // it will used for parsing the application.conf file and convert it into our desired case class type, in our case 'JWTConfig'
            // descriptor and fromTypeSafeConfig has some implicits, that is why we use given..using clause
        )