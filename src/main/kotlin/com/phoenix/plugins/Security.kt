package com.phoenix.plugins

import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.phoenix.security.token.TokenConfig
import io.ktor.server.application.*

fun Application.configureSecurity(config: TokenConfig) {

    authentication {
        jwt {
            val environmentConfig = this@configureSecurity.environment.config
//                val jwtAudience = environmentConfig.property("jwt.audience").getString()
            realm = environmentConfig.property("jwt.realm").getString()
            verifier(
                JWT
                    .require(Algorithm.HMAC256(config.secret))
                    .withAudience(config.audience)
                    .withIssuer(config.issuer)
                    .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains(config.audience)) {
                    JWTPrincipal(credential.payload)
                } else null
            }
        }
    }

}
