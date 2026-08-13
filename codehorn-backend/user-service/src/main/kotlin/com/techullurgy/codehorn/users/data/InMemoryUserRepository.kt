package com.techullurgy.codehorn.users.data

import com.techullurgy.codehorn.common.models.Difficulty
import com.techullurgy.codehorn.users.domain.model.User
import com.techullurgy.codehorn.users.domain.model.UserStats
import com.techullurgy.codehorn.users.domain.model.UserStreak
import com.techullurgy.codehorn.users.domain.model.UserSubmissionRecord
import com.techullurgy.codehorn.users.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import org.springframework.stereotype.Repository
import java.time.Instant
import java.time.LocalDate
import java.util.concurrent.ConcurrentHashMap

@Repository
class InMemoryUserRepository : UserRepository {
    private val users = ConcurrentHashMap<String, User>()

    init {
        seedInitialUsers().forEach { user ->
            users[user.userId] = user
        }
    }

    override suspend fun findByUserId(userId: String): User? {
        return users[userId] ?: users.values.find { it.username.equals(userId, ignoreCase = true) }
    }

    override suspend fun findByUsername(username: String): User? {
        return users.values.find { it.username.equals(username, ignoreCase = true) }
    }

    override suspend fun save(user: User): User {
        users[user.userId] = user
        return user
    }

    override fun findAll(): Flow<User> {
        return users.values.asFlow()
    }

    override suspend fun deleteByUserId(userId: String): Boolean {
        return users.remove(userId) != null
    }

    private fun seedInitialUsers(): List<User> {
        val today = LocalDate.now().toString()
        val yesterday = LocalDate.now().minusDays(1).toString()
        val twoDaysAgo = LocalDate.now().minusDays(2).toString()

        return listOf(
            User(
                userId = "user-1",
                username = "codehorn_demo",
                email = "demo@codehorn.io",
                fullName = "Alex Developer",
                avatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=256&q=80",
                bio = "Competitive programmer & Full Stack Engineer passionate about algorithms, clean code, and cloud architectures.",
                githubUrl = "https://github.com/codehorndemo",
                linkedInUrl = "https://linkedin.com/in/codehorndemo",
                points = 1450,
                ranking = 42,
                joinedAt = Instant.now().minusSeconds(86400 * 180),
                stats = UserStats(
                    easySolved = 15,
                    mediumSolved = 10,
                    hardSolved = 3,
                    totalSolved = 28,
                    totalSubmissions = 45,
                    acceptedSubmissions = 32,
                    acceptanceRate = 71.1
                ),
                streak = UserStreak(
                    currentStreak = 7,
                    maxStreak = 14,
                    lastActiveDate = today
                ),
                solvedProblemIds = setOf("1", "2", "3", "4", "5"),
                recentSubmissions = listOf(
                    UserSubmissionRecord(
                        id = "sub-101",
                        problemId = "1",
                        problemSlug = "two-sum",
                        problemTitle = "Two Sum",
                        difficulty = Difficulty.Easy,
                        status = "Accepted",
                        language = "python",
                        runtime = 28,
                        memory = 14.5,
                        timestamp = Instant.now().minusSeconds(3600)
                    ),
                    UserSubmissionRecord(
                        id = "sub-102",
                        problemId = "2",
                        problemSlug = "valid-parentheses",
                        problemTitle = "Valid Parentheses",
                        difficulty = Difficulty.Easy,
                        status = "Accepted",
                        language = "javascript",
                        runtime = 45,
                        memory = 18.2,
                        timestamp = Instant.now().minusSeconds(86400)
                    ),
                    UserSubmissionRecord(
                        id = "sub-103",
                        problemId = "3",
                        problemSlug = "longest-substring-without-repeating-characters",
                        problemTitle = "Longest Substring Without Repeating Characters",
                        difficulty = Difficulty.Medium,
                        status = "Wrong Answer",
                        language = "java",
                        runtime = 62,
                        memory = 42.1,
                        timestamp = Instant.now().minusSeconds(86400 * 2)
                    )
                ),
                activityMap = mapOf(
                    today to 4,
                    yesterday to 3,
                    twoDaysAgo to 5
                )
            ),
            User(
                userId = "user-2",
                username = "algo_master",
                email = "master@codehorn.io",
                fullName = "Sarah Chen",
                avatarUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?auto=format&fit=crop&w=256&q=80",
                bio = "Data Structures & Algorithms enthusiast. Building high-performance microservices.",
                githubUrl = "https://github.com/algomaster",
                linkedInUrl = "https://linkedin.com/in/algomaster",
                points = 2890,
                ranking = 5,
                joinedAt = Instant.now().minusSeconds(86400 * 365),
                stats = UserStats(
                    easySolved = 45,
                    mediumSolved = 60,
                    hardSolved = 25,
                    totalSolved = 130,
                    totalSubmissions = 180,
                    acceptedSubmissions = 150,
                    acceptanceRate = 83.3
                ),
                streak = UserStreak(
                    currentStreak = 21,
                    maxStreak = 45,
                    lastActiveDate = today
                ),
                solvedProblemIds = setOf("1", "2", "3", "4", "5", "6", "7"),
                recentSubmissions = emptyList(),
                activityMap = mapOf(today to 8, yesterday to 6)
            )
        )
    }
}
