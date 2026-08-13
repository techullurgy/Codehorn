package com.techullurgy.codehorn.problems.data

import com.techullurgy.codehorn.common.models.Difficulty
import com.techullurgy.codehorn.problems.domain.model.Problem
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface SpringDataMongoProblemRepository : MongoRepository<Problem, String> {
    fun findBySlug(slug: String): Problem?
    fun findByDifficulty(difficulty: Difficulty): List<Problem>
    fun findByCategoryContainingIgnoreCase(category: String): List<Problem>
    fun findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(title: String, description: String): List<Problem>
    fun deleteBySlug(slug: String): Long
}
