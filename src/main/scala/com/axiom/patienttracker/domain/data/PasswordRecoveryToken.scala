package com.axiom.patienttracker.domain.data

final case class PasswordRecoveryToken(
    email: String,
    token: String,
    ttl: Long
)
