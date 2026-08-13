package com.techullurgy.codehorn.problems.data

import com.techullurgy.codehorn.common.models.Difficulty
import com.techullurgy.codehorn.problems.domain.model.Problem
import com.techullurgy.codehorn.problems.domain.repository.ProblemRepository
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Repository

@Repository
@Primary
class MongoProblemRepositoryImpl(
    private val mongoRepo: SpringDataMongoProblemRepository,
    private val inMemoryFallbackRepo: InMemoryProblemRepository
) : ProblemRepository {

    override fun findAll(difficulty: Difficulty?, category: String?, search: String?): List<Problem> {
        return runCatching {
            var list = mongoRepo.findAll()
            if (difficulty != null) {
                list = list.filter { it.difficulty == difficulty }
            }
            if (!category.isNullOrBlank()) {
                list = list.filter { it.category.contains(category, ignoreCase = true) }
            }
            if (!search.isNullOrBlank()) {
                list = list.filter {
                    it.title.contains(search, ignoreCase = true) ||
                    it.description.contains(search, ignoreCase = true) ||
                    it.category.contains(search, ignoreCase = true)
                }
            }
            if (list.isEmpty()) {
                inMemoryFallbackRepo.findAll(difficulty, category, search)
            } else {
                list
            }
        }.getOrElse {
            inMemoryFallbackRepo.findAll(difficulty, category, search)
        }
    }

    override fun findBySlug(slug: String): Problem? {
        return runCatching {
            mongoRepo.findBySlug(slug) ?: mongoRepo.findById(slug).orElse(null) ?: inMemoryFallbackRepo.findBySlug(slug)
        }.getOrElse {
            inMemoryFallbackRepo.findBySlug(slug)
        }
    }

    override fun findById(id: String): Problem? {
        return runCatching {
            mongoRepo.findById(id).orElse(null) ?: mongoRepo.findBySlug(id) ?: inMemoryFallbackRepo.findById(id)
        }.getOrElse {
            inMemoryFallbackRepo.findById(id)
        }
    }

    override fun save(problem: Problem): Problem {
        return runCatching {
            inMemoryFallbackRepo.save(problem)
            mongoRepo.save(problem)
        }.getOrElse {
            inMemoryFallbackRepo.save(problem)
        }
    }

    override fun deleteBySlug(slug: String): Boolean {
        return runCatching {
            inMemoryFallbackRepo.deleteBySlug(slug)
            val count = mongoRepo.deleteBySlug(slug)
            count > 0
        }.getOrElse {
            inMemoryFallbackRepo.deleteBySlug(slug)
        }
    }
}
