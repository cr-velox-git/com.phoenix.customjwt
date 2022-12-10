package com.phoenix.data.models

import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class MongoUserDataSourceImpl(
    db: CoroutineDatabase
) : UserDataSource {
    val users = db.getCollection<User>()
    override suspend fun getUserByUserName(userName: String): User? {
return users.findOne(User::username eq userName)
    }

    override suspend fun insertUser(user: User): Boolean {
        return users.insertOne(user).wasAcknowledged()
    }

    override suspend fun updateUser(user: User): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun deleteUser(userId: String): Boolean {
        TODO("Not yet implemented")
    }
}