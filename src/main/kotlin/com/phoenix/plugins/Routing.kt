package com.phoenix.plugins

import com.phoenix.*
import com.phoenix.data.models.UserDataSource
import com.phoenix.security.hashing.HashingService
import com.phoenix.security.token.TokenConfig
import com.phoenix.security.token.TokenService
import io.ktor.server.routing.*
import io.ktor.server.application.*

fun Application.configureRouting(
    userDataSource: UserDataSource,
    hashingService: HashingService,
    tokenService: TokenService,
    tokenConfig: TokenConfig
) {

    routing {
        getTest()
        signIn(application,hashingService, userDataSource, tokenService, tokenConfig)
        signUp(application,hashingService, userDataSource)
        authenticate()
        getSecretInfo()
    }
}
