package com.techullurgy.codehorn.problems.controllers

import com.techullurgy.codehorn.common.models.Difficulty
import com.techullurgy.codehorn.problems.domain.model.Problem
import com.techullurgy.codehorn.problems.domain.model.TestCase
import com.techullurgy.codehorn.problems.dto.CreateProblemRequest
import com.techullurgy.codehorn.problems.dto.UpdateProblemRequest
import com.techullurgy.codehorn.problems.service.ProblemsService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping
class ProblemsController(
    private val problemsService: ProblemsService
) {

    @GetMapping("", "/", "/api/problems", "/problems")
    fun getAllProblems(
        @RequestParam(required = false) difficulty: Difficulty?,
        @RequestParam(required = false) category: String?,
        @RequestParam(required = false) search: String?
    ): List<Problem> {
        return problemsService.getAllProblems(difficulty, category, search)
    }

    @GetMapping("/{slug}", "/api/problems/{slug}", "/problems/{slug}")
    fun getProblemBySlug(@PathVariable slug: String): ResponseEntity<Problem> {
        val problem = problemsService.getProblemBySlug(slug)
            ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(problem)
    }

    @GetMapping("/{slug}/testcases", "/api/problems/{slug}/testcases", "/problems/{slug}/testcases")
    fun getProblemTestcases(
        @PathVariable slug: String,
        @RequestParam(required = false, defaultValue = "false") sampleOnly: Boolean
    ): ResponseEntity<List<TestCase>> {
        val testcases = problemsService.getProblemTestcases(slug, sampleOnly)
            ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(testcases)
    }

    @GetMapping("/{slug}/templates", "/api/problems/{slug}/templates", "/problems/{slug}/templates")
    fun getProblemTemplates(@PathVariable slug: String): ResponseEntity<Map<String, String>> {
        val templates = problemsService.getProblemTemplates(slug)
            ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(templates)
    }

    @GetMapping("/{slug}/solutions", "/api/problems/{slug}/solutions", "/problems/{slug}/solutions")
    fun getProblemSolutions(@PathVariable slug: String): ResponseEntity<Map<String, String>> {
        val solutions = problemsService.getProblemSolutions(slug)
            ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(solutions)
    }

    @PostMapping("", "/", "/api/problems", "/problems")
    fun createProblem(@RequestBody request: CreateProblemRequest): ResponseEntity<Problem> {
        val created = problemsService.createProblem(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(created)
    }

    @PutMapping("/{slug}", "/api/problems/{slug}", "/problems/{slug}")
    fun updateProblem(
        @PathVariable slug: String,
        @RequestBody request: UpdateProblemRequest
    ): ResponseEntity<Problem> {
        val updated = problemsService.updateProblem(slug, request)
            ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(updated)
    }

    @DeleteMapping("/{slug}", "/api/problems/{slug}", "/problems/{slug}")
    fun deleteProblem(@PathVariable slug: String): ResponseEntity<Void> {
        val deleted = problemsService.deleteProblem(slug)
        return if (deleted) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}
