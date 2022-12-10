package com.phoenix.security.token

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.Date

class JwtTokenServiceImpl : TokenService {
    override fun generate(config: TokenConfig, vararg claim: TokenClaim): String {
        var token = JWT.create()
            .withAudience(config.audience)
            .withIssuer(config.issuer)
            .withExpiresAt(Date(System.currentTimeMillis() + config.expiresIn))
        claim.forEach { cl ->
            token = token.withClaim(cl.name, cl.value)
        }
        return token.sign(Algorithm.HMAC256(config.secret))
    }
}