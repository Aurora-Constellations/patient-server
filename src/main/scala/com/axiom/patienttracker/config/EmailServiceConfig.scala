package com.axiom.patienttracker.config

final case class EmailServiceConfig(
    host: String,
    port: Int,
    user: String,
    password: String
)
