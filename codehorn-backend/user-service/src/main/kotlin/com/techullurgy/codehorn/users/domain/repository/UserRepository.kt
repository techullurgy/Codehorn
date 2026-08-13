package com.techullurgy.codehorn.users.domain.repository

import com.techullurgy.codehorn.users.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun findByUserId(userId: String): User?
    suspend fun findByUsername(username: String): User?
    suspend fun save(user: User): User
    fun findAll(): Flow<User>
    suspend fun deleteByUserId(userId: String): Boolean
}
