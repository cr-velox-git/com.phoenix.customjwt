package com.phoenix.data.models

import com.phoenix.data.models.User

interface UserDataSource {
    suspend fun getUserByUserName(userName: String): User?
    suspend fun insertUser(user: User):Boolean
    suspend fun updateUser(user: User):Boolean
    suspend fun deleteUser(userId: String):Boolean
}