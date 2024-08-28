package com.axiom.patienttracker.domain.data

import zio.json.JsonCodec
import zio.json.DeriveJsonCodec

final case class UserToken(
    email: String,
    token: String,
    expires: Long //seconds
)

object UserToken:
    given codec: JsonCodec[UserToken] = DeriveJsonCodec.gen[UserToken]
