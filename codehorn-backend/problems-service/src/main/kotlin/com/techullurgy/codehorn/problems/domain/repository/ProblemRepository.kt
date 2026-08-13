package com.techullurgy.codehorn.problems.domain.repository

import com.techullurgy.codehorn.common.models.Difficulty
import com.techullurgy.codehorn.problems.domain.model.Problem

interface ProblemRepository {
    fun findAll(difficulty: Difficulty? = null, category: String? = null, search: String? = null): List<Problem>
    fun findBySlug(slug: String): Problem?
    fun findById(id: String): Problem?
    fun save(problem: Problem): Problem
    fun deleteBySlug(slug: String): Boolean
}
