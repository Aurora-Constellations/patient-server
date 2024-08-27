package com.axiom.patienttracker.config

final case class JWTConfig(
    secret: String,
    ttl: Long
)
