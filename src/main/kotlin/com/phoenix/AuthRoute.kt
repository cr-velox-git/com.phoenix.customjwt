package com.phoenix

import com.phoenix.data.models.User
import com.phoenix.data.models.UserDataSource
import com.phoenix.data.request.AuthRequest
import com.phoenix.data.responses.AuthResponse
import com.phoenix.security.hashing.HashingService
import com.phoenix.security.hashing.SaltedHash
import com.phoenix.security.token.TokenClaim
import com.phoenix.security.token.TokenConfig
import com.phoenix.security.token.TokenService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.signUp(
    app: Application,
    hashingService: HashingService,
    userDataSource: UserDataSource
) {
    post("signup") {
        val request = call.receiveOrNull<AuthRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val areFieldBlank = request.username.isBlank() || request.password.isBlank()
        val isPwTooShort = request.password.length < 8
        if (areFieldBlank || isPwTooShort) {
            call.respond(HttpStatusCode.Conflict, "blank filled or too short password")
            return@post
        }

        val saltedHash = hashingService.generateSaltedHash(request.password)
        val user = User(
            username = request.username,
            password = saltedHash.hash,
            salt = saltedHash.salt
        )

        val wasAcknowledge = userDataSource.insertUser(user)
        if (!wasAcknowledge) {
            call.respond(HttpStatusCode.Conflict)
            return@post
        }
        call.respond(HttpStatusCode.OK)
    }
}

fun Route.signIn(
    app: Application,
    hashingService: HashingService,
    userDataSource: UserDataSource,
    tokenService: TokenService,
    tokenConfig: TokenConfig
) {
    post("signin") {
        val request =  call.receiveOrNull<AuthRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val user = userDataSource.getUserByUserName(request.username)
        app.log.error("${user?.username} - ${user?.password}")
        if (user == null) {
            call.respond(HttpStatusCode.Conflict, "incorrect username and password")
            return@post
        }

        app.log.error(request.password)
        val isValidPassword = hashingService.verify(
            app = app,
            value = request.password,
            saltedHash = SaltedHash(
                hash = user.password,
                salt = user.salt
            )
        )

        if (!isValidPassword) {
            call.respond(HttpStatusCode.Conflict, "Invalid password")
            return@post
        }

        val token = tokenService.generate(
            config = tokenConfig,
            TokenClaim(
                name = "userId",
                value = user.id.toString()
            )
        )

        call.respond(
            status = HttpStatusCode.OK,
            message = AuthResponse(
                token = token
            )
        )
    }
}

fun Route.authenticate() {
    authenticate {
        get("authenticate") {
            call.respond(HttpStatusCode.OK)
        }
    }
}

fun Route.getSecretInfo() {
    authenticate {
        get("secret") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("userId", String::class)
            call.respond(
                status = HttpStatusCode.OK,
                message = "Your userId is $userId"
            )
        }
    }
}

fun Route.getTest() {
        get("test") {
//            val principal = call.principal<JWTPrincipal>()
//            val userId = principal?.getClaim("userId", String::class)
            call.respond(
                status = HttpStatusCode.OK,
                message = "Test Connection established"
            )
        }

}