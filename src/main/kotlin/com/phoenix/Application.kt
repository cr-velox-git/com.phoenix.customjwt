package com.phoenix

import com.phoenix.data.models.MongoUserDataSourceImpl
import io.ktor.server.application.*
import com.phoenix.plugins.*
import com.phoenix.security.hashing.SHA256HashingService
import com.phoenix.security.token.JwtTokenServiceImpl
import com.phoenix.security.token.TokenConfig
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {

    val mongoPas = System.getenv("MONGO_PAS")
    val mongoDb = "custom-jwt"
    val db = KMongo.createClient(
        connectionString = "mongodb+srv://crvelox:$mongoPas@cluster0.qpxv3fe.mongodb.net/$mongoDb?retry=true&w=majority"
    ).coroutine
        .getDatabase(mongoDb)

    val userDataSource = MongoUserDataSourceImpl(db)

    /*  GlobalScope.launch {
          val user = User(
              username = "raj",
              password = "gggg",
              salt = "salt"
          )
          userDataSource.insertUser(user)
      }*/

    val tokenService = JwtTokenServiceImpl()
    val tokenConfig = TokenConfig(
        issuer = environment.config.property("jwt.issuer").getString(),
        audience = environment.config.property("jwt.audience").getString(),
        expiresIn = 365L * 1000L * 60L * 60L * 24L,
        secret = System.getenv("JWT_SECRET")
    )
    val hashingService = SHA256HashingService()
    configureRouting(userDataSource, hashingService, tokenService, tokenConfig)
    configureSerialization()
//    configureMonitoring()
    configureSecurity(tokenConfig)
}
