package com.techullurgy.codehorn.problems.service

import com.techullurgy.codehorn.common.models.Difficulty
import com.techullurgy.codehorn.problems.domain.model.Problem
import com.techullurgy.codehorn.problems.domain.model.TestCase
import com.techullurgy.codehorn.problems.domain.repository.ProblemRepository
import com.techullurgy.codehorn.problems.dto.CreateProblemRequest
import com.techullurgy.codehorn.problems.dto.UpdateProblemRequest
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ProblemsService(
    private val problemRepository: ProblemRepository
) {
    fun getAllProblems(difficulty: Difficulty? = null, category: String? = null, search: String? = null): List<Problem> {
        return problemRepository.findAll(difficulty, category, search)
    }

    fun getProblemBySlug(slug: String): Problem? {
        return problemRepository.findBySlug(slug)
    }

    fun getProblemTestcases(slug: String, sampleOnly: Boolean = false): List<TestCase>? {
        val problem = problemRepository.findBySlug(slug) ?: return null
        return if (sampleOnly) {
            problem.testcases.filter { it.isSample }
        } else {
            problem.testcases
        }
    }

    fun getProblemTemplates(slug: String): Map<String, String>? {
        val problem = problemRepository.findBySlug(slug) ?: return null
        return problem.templates
    }

    fun getProblemSolutions(slug: String): Map<String, String>? {
        val problem = problemRepository.findBySlug(slug) ?: return null
        return problem.solutions
    }

    fun createProblem(request: CreateProblemRequest): Problem {
        val slug = request.slug?.takeIf { it.isNotBlank() } ?: generateSlug(request.title)
        val id = UUID.randomUUID().toString()

        val problem = Problem(
            id = id,
            title = request.title,
            slug = slug,
            difficulty = request.difficulty,
            category = request.category,
            acceptanceRate = request.acceptanceRate,
            description = request.description,
            examples = request.examples,
            constraints = request.constraints,
            hints = request.hints,
            editorial = request.editorial,
            solutionApproaches = request.solutionApproaches,
            starterCode = request.starterCode,
            templates = request.templates,
            solutions = request.solutions,
            testcases = request.testcases
        )

        return problemRepository.save(problem)
    }

    fun updateProblem(slug: String, request: UpdateProblemRequest): Problem? {
        val existing = problemRepository.findBySlug(slug) ?: return null

        val updated = existing.copy(
            title = request.title ?: existing.title,
            difficulty = request.difficulty ?: existing.difficulty,
            category = request.category ?: existing.category,
            acceptanceRate = request.acceptanceRate ?: existing.acceptanceRate,
            description = request.description ?: existing.description,
            examples = request.examples ?: existing.examples,
            constraints = request.constraints ?: existing.constraints,
            hints = request.hints ?: existing.hints,
            editorial = request.editorial ?: existing.editorial,
            solutionApproaches = request.solutionApproaches ?: existing.solutionApproaches,
            starterCode = request.starterCode ?: existing.starterCode,
            templates = request.templates ?: existing.templates,
            solutions = request.solutions ?: existing.solutions,
            testcases = request.testcases ?: existing.testcases
        )

        return problemRepository.save(updated)
    }

    fun deleteProblem(slug: String): Boolean {
        return problemRepository.deleteBySlug(slug)
    }

    private fun generateSlug(title: String): String {
        return title.lowercase()
            .replace(Regex("[^a-z0-9\\s-]"), "")
            .replace(Regex("\\s+"), "-")
    }
}
