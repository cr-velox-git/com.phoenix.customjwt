package com.phoenix.security.hashing

import io.ktor.server.application.*
import org.apache.commons.codec.binary.Hex
import org.apache.commons.codec.digest.DigestUtils
import java.security.SecureRandom

class SHA256HashingService : HashingService {
    override fun generateSaltedHash(value: String, saltedLength: Int): SaltedHash {
        val salt = SecureRandom.getInstance("SHA1PRNG").generateSeed(saltedLength)
        val saltAsHex = Hex.encodeHexString(salt)
        val hash = DigestUtils.sha256Hex("$saltAsHex$value")
        return SaltedHash(salt = saltAsHex, hash = hash)
    }

    override fun verify(app:Application,value: String, saltedHash: SaltedHash): Boolean {
        return DigestUtils.sha256Hex("${saltedHash.salt}$value") == saltedHash.hash
    }
}