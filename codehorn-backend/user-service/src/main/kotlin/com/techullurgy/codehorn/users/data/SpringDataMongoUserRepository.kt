package com.techullurgy.codehorn.users.data

import com.techullurgy.codehorn.users.domain.model.User
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface SpringDataMongoUserRepository : MongoRepository<User, String> {
    fun findByUsername(username: String): User?
    fun findByEmail(email: String): User?
    fun deleteByUserId(userId: String): Long
}
