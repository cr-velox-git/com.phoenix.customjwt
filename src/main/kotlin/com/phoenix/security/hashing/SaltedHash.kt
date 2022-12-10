package com.phoenix.security.hashing

data class SaltedHash(
    val hash: String,
    val salt: String,
)