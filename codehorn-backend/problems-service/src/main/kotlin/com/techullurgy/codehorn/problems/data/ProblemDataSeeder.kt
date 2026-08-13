package com.techullurgy.codehorn.problems.data

import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class ProblemDataSeeder(
    private val mongoRepo: SpringDataMongoProblemRepository,
    private val inMemoryRepo: InMemoryProblemRepository
) : CommandLineRunner {

    override fun run(vararg args: String) {
        runCatching {
            if (mongoRepo.count() == 0L) {
                val seedProblems = inMemoryRepo.findAll(null, null, null)
                mongoRepo.saveAll(seedProblems)
                println("ProblemDataSeeder: Successfully seeded ${seedProblems.size} problems into MongoDB.")
            }
        }.onFailure { ex ->
            println("ProblemDataSeeder: MongoDB not connected or error during seeding: ${ex.message}")
        }
    }
}
