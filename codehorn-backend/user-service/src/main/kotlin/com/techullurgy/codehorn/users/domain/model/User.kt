package com.techullurgy.codehorn.users.domain.model

import com.techullurgy.codehorn.common.models.Difficulty
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

data class UserSubmissionRecord(
    val id: String,
    val problemId: String,
    val problemSlug: String,
    val problemTitle: String,
    val difficulty: Difficulty,
    val status: String,
    val language: String,
    val runtime: Int,
    val memory: Double,
    val timestamp: Instant = Instant.now()
)

data class UserStats(
    val easySolved: Int = 0,
    val mediumSolved: Int = 0,
    val hardSolved: Int = 0,
    val totalSolved: Int = 0,
    val totalSubmissions: Int = 0,
    val acceptedSubmissions: Int = 0,
    val acceptanceRate: Double = 0.0
)

data class UserStreak(
    val currentStreak: Int = 0,
    val maxStreak: Int = 0,
    val lastActiveDate: String? = null
)

@Document(collection = "users")
data class User(
    @Id val userId: String,
    @Indexed(unique = true) val username: String,
    @Indexed(unique = true) val email: String,
    val fullName: String,
    val avatarUrl: String,
    val bio: String,
    val githubUrl: String? = null,
    val linkedInUrl: String? = null,
    val points: Int = 0,
    val ranking: Int = 1,
    val joinedAt: Instant = Instant.now(),
    val stats: UserStats = UserStats(),
    val streak: UserStreak = UserStreak(),
    val solvedProblemIds: Set<String> = emptySet(),
    val recentSubmissions: List<UserSubmissionRecord> = emptyList(),
    val activityMap: Map<String, Int> = emptyMap()
)
