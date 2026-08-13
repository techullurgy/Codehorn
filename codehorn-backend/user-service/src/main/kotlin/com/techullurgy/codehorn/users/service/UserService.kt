package com.techullurgy.codehorn.users.service

import com.techullurgy.codehorn.common.models.Difficulty
import com.techullurgy.codehorn.users.domain.model.User
import com.techullurgy.codehorn.users.domain.model.UserStats
import com.techullurgy.codehorn.users.domain.model.UserStreak
import com.techullurgy.codehorn.users.domain.model.UserSubmissionRecord
import com.techullurgy.codehorn.users.domain.repository.UserRepository
import com.techullurgy.codehorn.users.dto.*
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.LocalDate
import java.util.UUID

@Service
class UserService(
    private val userRepository: UserRepository
) {
    suspend fun getUserProfile(userId: String): UserProfileDto? {
        val user = userRepository.findByUserId(userId) ?: return null
        return user.toProfileDto()
    }

    suspend fun getUserDashboard(userId: String): UserDashboardDto? {
        val user = userRepository.findByUserId(userId) ?: return null
        return user.toDashboardDto()
    }

    suspend fun getUserSubmissions(userId: String): List<RecentSubmissionDto>? {
        val user = userRepository.findByUserId(userId) ?: return null
        return user.recentSubmissions.map { it.toDto() }
    }

    suspend fun getUserSolvedProblemIds(userId: String): Set<String>? {
        val user = userRepository.findByUserId(userId) ?: return null
        return user.solvedProblemIds
    }

    suspend fun updateProfile(userId: String, request: UpdateUserProfileRequest): UserProfileDto? {
        val existing = userRepository.findByUserId(userId) ?: return null

        val updated = existing.copy(
            fullName = request.fullName ?: existing.fullName,
            avatarUrl = request.avatarUrl ?: existing.avatarUrl,
            bio = request.bio ?: existing.bio,
            githubUrl = request.githubUrl ?: existing.githubUrl,
            linkedInUrl = request.linkedInUrl ?: existing.linkedInUrl
        )

        val saved = userRepository.save(updated)
        return saved.toProfileDto()
    }

    suspend fun recordActivity(userId: String, request: RecordActivityRequest): UserDashboardDto? {
        val user = userRepository.findByUserId(userId) ?: return null
        val today = LocalDate.now().toString()
        val yesterday = LocalDate.now().minusDays(1).toString()

        val isAccepted = request.status.equals("Accepted", ignoreCase = true)
        val isNewSolved = isAccepted && !user.solvedProblemIds.contains(request.problemId)

        val newSolvedSet = if (isNewSolved) user.solvedProblemIds + request.problemId else user.solvedProblemIds

        var easyCount = user.stats.easySolved
        var mediumCount = user.stats.mediumSolved
        var hardCount = user.stats.hardSolved
        var addedPoints = 0

        if (isNewSolved) {
            when (request.difficulty) {
                Difficulty.Easy -> { easyCount++; addedPoints = 10 }
                Difficulty.Medium -> { mediumCount++; addedPoints = 20 }
                Difficulty.Hard -> { hardCount++; addedPoints = 30 }
            }
        }

        val newTotalSubmissions = user.stats.totalSubmissions + 1
        val newAcceptedSubmissions = if (isAccepted) user.stats.acceptedSubmissions + 1 else user.stats.acceptedSubmissions
        val newAcceptanceRate = Math.round((newAcceptedSubmissions.toDouble() / newTotalSubmissions * 100.0) * 10.0) / 10.0

        val updatedStats = UserStats(
            easySolved = easyCount,
            mediumSolved = mediumCount,
            hardSolved = hardCount,
            totalSolved = if (isNewSolved) user.stats.totalSolved + 1 else user.stats.totalSolved,
            totalSubmissions = newTotalSubmissions,
            acceptedSubmissions = newAcceptedSubmissions,
            acceptanceRate = newAcceptanceRate
        )

        // Calculate Streak
        val lastActive = user.streak.lastActiveDate
        val newCurrentStreak = when (lastActive) {
            today -> user.streak.currentStreak
            yesterday -> user.streak.currentStreak + 1
            else -> 1
        }
        val newMaxStreak = Math.max(newCurrentStreak, user.streak.maxStreak)
        val updatedStreak = UserStreak(
            currentStreak = newCurrentStreak,
            maxStreak = newMaxStreak,
            lastActiveDate = today
        )

        // Activity Heatmap
        val currentCount = user.activityMap[today] ?: 0
        val updatedActivityMap = user.activityMap + (today to (currentCount + 1))

        // Submission Record
        val newRecord = UserSubmissionRecord(
            id = "sub-" + UUID.randomUUID().toString().take(8),
            problemId = request.problemId,
            problemSlug = request.problemSlug,
            problemTitle = request.problemTitle,
            difficulty = request.difficulty,
            status = request.status,
            language = request.language,
            runtime = request.runtime,
            memory = request.memory,
            timestamp = Instant.now()
        )

        val updatedRecentSubmissions = (listOf(newRecord) + user.recentSubmissions).take(20)

        val updatedUser = user.copy(
            points = user.points + addedPoints,
            stats = updatedStats,
            streak = updatedStreak,
            solvedProblemIds = newSolvedSet,
            recentSubmissions = updatedRecentSubmissions,
            activityMap = updatedActivityMap
        )

        val saved = userRepository.save(updatedUser)
        return saved.toDashboardDto()
    }

    private fun User.toProfileDto() = UserProfileDto(
        userId = userId,
        username = username,
        email = email,
        fullName = fullName,
        avatarUrl = avatarUrl,
        bio = bio,
        githubUrl = githubUrl,
        linkedInUrl = linkedInUrl,
        points = points,
        ranking = ranking,
        joinedAt = joinedAt
    )

    private fun User.toDashboardDto() = UserDashboardDto(
        profile = toProfileDto(),
        problemStats = ProblemStatsDto(
            easySolved = stats.easySolved,
            mediumSolved = stats.mediumSolved,
            hardSolved = stats.hardSolved,
            totalSolved = stats.totalSolved
        ),
        submissionStats = SubmissionStatsDto(
            totalSubmissions = stats.totalSubmissions,
            acceptedSubmissions = stats.acceptedSubmissions,
            acceptanceRate = stats.acceptanceRate
        ),
        streak = StreakDto(
            currentStreak = streak.currentStreak,
            maxStreak = streak.maxStreak,
            lastActiveDate = streak.lastActiveDate
        ),
        solvedProblemIds = solvedProblemIds,
        recentSubmissions = recentSubmissions.map { it.toDto() },
        activityMap = activityMap
    )

    private fun UserSubmissionRecord.toDto() = RecentSubmissionDto(
        id = id,
        problemId = problemId,
        problemSlug = problemSlug,
        problemTitle = problemTitle,
        difficulty = difficulty,
        status = status,
        language = language,
        runtime = runtime,
        memory = memory,
        timestamp = timestamp
    )
}
