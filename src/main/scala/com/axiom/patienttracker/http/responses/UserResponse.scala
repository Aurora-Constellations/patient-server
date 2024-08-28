package com.axiom.patienttracker.http.responses

import zio.json.JsonCodec

final case class UserResponse(
    email: String
) derives JsonCodec
