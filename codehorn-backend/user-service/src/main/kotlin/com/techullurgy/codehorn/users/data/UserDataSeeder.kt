package com.techullurgy.codehorn.users.data

import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class UserDataSeeder(
    private val mongoRepo: SpringDataMongoUserRepository,
    private val inMemoryRepo: InMemoryUserRepository
) : CommandLineRunner {

    override fun run(vararg args: String) {
        runCatching {
            if (mongoRepo.count() == 0L) {
                runBlocking {
                    val seedUsers = inMemoryRepo.findAll().toList()
                    mongoRepo.saveAll(seedUsers)
                    println("UserDataSeeder: Successfully seeded ${seedUsers.size} users into MongoDB.")
                }
            }
        }.onFailure { ex ->
            println("UserDataSeeder: MongoDB not connected or error during seeding: ${ex.message}")
        }
    }
}
