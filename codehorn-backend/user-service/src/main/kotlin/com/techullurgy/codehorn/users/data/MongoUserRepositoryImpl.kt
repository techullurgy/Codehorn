package com.techullurgy.codehorn.users.data

import com.techullurgy.codehorn.users.domain.model.User
import com.techullurgy.codehorn.users.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Repository

@Repository
@Primary
class MongoUserRepositoryImpl(
    private val mongoRepo: SpringDataMongoUserRepository,
    private val inMemoryFallbackRepo: InMemoryUserRepository
) : UserRepository {

    override suspend fun findByUserId(userId: String): User? {
        return runCatching {
            mongoRepo.findById(userId).orElse(null)
                ?: mongoRepo.findByUsername(userId)
                ?: inMemoryFallbackRepo.findByUserId(userId)
        }.getOrElse {
            inMemoryFallbackRepo.findByUserId(userId)
        }
    }

    override suspend fun findByUsername(username: String): User? {
        return runCatching {
            mongoRepo.findByUsername(username) ?: inMemoryFallbackRepo.findByUsername(username)
        }.getOrElse {
            inMemoryFallbackRepo.findByUsername(username)
        }
    }

    override suspend fun save(user: User): User {
        return runCatching {
            inMemoryFallbackRepo.save(user)
            mongoRepo.save(user)
        }.getOrElse {
            inMemoryFallbackRepo.save(user)
        }
    }

    override fun findAll(): Flow<User> {
        return runCatching {
            val list = mongoRepo.findAll()
            if (list.isEmpty()) {
                inMemoryFallbackRepo.findAll()
            } else {
                list.asFlow()
            }
        }.getOrElse {
            inMemoryFallbackRepo.findAll()
        }
    }

    override suspend fun deleteByUserId(userId: String): Boolean {
        return runCatching {
            inMemoryFallbackRepo.deleteByUserId(userId)
            mongoRepo.deleteById(userId)
            true
        }.getOrElse {
            inMemoryFallbackRepo.deleteByUserId(userId)
        }
    }
}
