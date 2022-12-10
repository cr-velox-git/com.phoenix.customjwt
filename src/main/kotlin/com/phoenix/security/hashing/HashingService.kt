package com.phoenix.security.hashing

import io.ktor.server.application.*

interface HashingService {
    fun generateSaltedHash(value: String,saltedLength: Int = 32): SaltedHash
    fun verify(app: Application, value: String, saltedHash: SaltedHash): Boolean
}