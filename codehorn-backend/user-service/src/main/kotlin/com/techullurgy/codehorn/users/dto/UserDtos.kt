package com.techullurgy.codehorn.users.dto

import com.techullurgy.codehorn.common.models.Difficulty
import java.time.Instant

data class UserProfileDto(
    val userId: String,
    val username: String,
    val email: String,
    val fullName: String,
    val avatarUrl: String,
    val bio: String,
    val githubUrl: String? = null,
    val linkedInUrl: String? = null,
    val points: Int,
    val ranking: Int,
    val joinedAt: Instant
)

data class ProblemStatsDto(
    val easySolved: Int,
    val mediumSolved: Int,
    val hardSolved: Int,
    val totalSolved: Int
)

data class SubmissionStatsDto(
    val totalSubmissions: Int,
    val acceptedSubmissions: Int,
    val acceptanceRate: Double
)

data class StreakDto(
    val currentStreak: Int,
    val maxStreak: Int,
    val lastActiveDate: String?
)

data class RecentSubmissionDto(
    val id: String,
    val problemId: String,
    val problemSlug: String,
    val problemTitle: String,
    val difficulty: Difficulty,
    val status: String,
    val language: String,
    val runtime: Int,
    val memory: Double,
    val timestamp: Instant
)

data class UserDashboardDto(
    val profile: UserProfileDto,
    val problemStats: ProblemStatsDto,
    val submissionStats: SubmissionStatsDto,
    val streak: StreakDto,
    val solvedProblemIds: Set<String>,
    val recentSubmissions: List<RecentSubmissionDto>,
    val activityMap: Map<String, Int>
)

data class UpdateUserProfileRequest(
    val fullName: String? = null,
    val avatarUrl: String? = null,
    val bio: String? = null,
    val githubUrl: String? = null,
    val linkedInUrl: String? = null
)

data class RecordActivityRequest(
    val problemId: String,
    val problemSlug: String,
    val problemTitle: String,
    val difficulty: Difficulty,
    val status: String,
    val language: String,
    val runtime: Int,
    val memory: Double
)
