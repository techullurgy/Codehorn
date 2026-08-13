package com.techullurgy.codehorn.users.controllers

import com.techullurgy.codehorn.users.dto.*
import com.techullurgy.codehorn.users.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping
class UserController(
    private val userService: UserService
) {

    @GetMapping("/{userId}", "/api/users/{userId}", "/users/{userId}")
    suspend fun getUserProfile(@PathVariable userId: String): ResponseEntity<UserProfileDto> {
        val profile = userService.getUserProfile(userId)
            ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(profile)
    }

    @GetMapping("/{userId}/dashboard", "/api/users/{userId}/dashboard", "/users/{userId}/dashboard")
    suspend fun getUserDashboard(@PathVariable userId: String): ResponseEntity<UserDashboardDto> {
        val dashboard = userService.getUserDashboard(userId)
            ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(dashboard)
    }

    @GetMapping("/{userId}/submissions", "/api/users/{userId}/submissions", "/users/{userId}/submissions")
    suspend fun getUserSubmissions(@PathVariable userId: String): ResponseEntity<List<RecentSubmissionDto>> {
        val submissions = userService.getUserSubmissions(userId)
            ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(submissions)
    }

    @GetMapping("/{userId}/solved", "/api/users/{userId}/solved", "/users/{userId}/solved")
    suspend fun getUserSolvedProblemIds(@PathVariable userId: String): ResponseEntity<Set<String>> {
        val solved = userService.getUserSolvedProblemIds(userId)
            ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(solved)
    }

    @PutMapping("/{userId}/profile", "/api/users/{userId}/profile", "/users/{userId}/profile")
    suspend fun updateProfile(
        @PathVariable userId: String,
        @RequestBody request: UpdateUserProfileRequest
    ): ResponseEntity<UserProfileDto> {
        val updated = userService.updateProfile(userId, request)
            ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(updated)
    }

    @PostMapping("/{userId}/activity", "/api/users/{userId}/activity", "/users/{userId}/activity")
    suspend fun recordActivity(
        @PathVariable userId: String,
        @RequestBody request: RecordActivityRequest
    ): ResponseEntity<UserDashboardDto> {
        val updatedDashboard = userService.recordActivity(userId, request)
            ?: return ResponseEntity.notFound().build()
        return ResponseEntity.status(HttpStatus.CREATED).body(updatedDashboard)
    }
}
