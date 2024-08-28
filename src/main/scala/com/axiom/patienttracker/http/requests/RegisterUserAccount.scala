package com.axiom.patienttracker.http.requests

import zio.json.JsonCodec
// import zio.json.DeriveJsonCodec

final case class RegisterUserAccount(
    email: String,
    password: String
) derives JsonCodec

// object RegisterUserAccount:
//     given codec: JsonCodec[RegisterUserAccount] = DeriveJsonCodec.gen[RegisterUserAccount]