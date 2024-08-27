package com.axiom.patienttracker.domain.data

final case class UserToken(
    email: String,
    token: String,
    expires: Long //seconds
)
