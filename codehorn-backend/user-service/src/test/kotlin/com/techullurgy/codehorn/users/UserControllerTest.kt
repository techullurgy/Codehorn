package com.techullurgy.codehorn.users

import com.techullurgy.codehorn.common.models.Difficulty
import com.techullurgy.codehorn.users.controllers.UserController
import com.techullurgy.codehorn.users.data.InMemoryUserRepository
import com.techullurgy.codehorn.users.dto.RecordActivityRequest
import com.techullurgy.codehorn.users.dto.UpdateUserProfileRequest
import com.techullurgy.codehorn.users.service.UserService
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus

class UserControllerTest {

    private lateinit var userService: UserService
    private lateinit var controller: UserController

    @BeforeEach
    fun setUp() {
        val repository = InMemoryUserRepository()
        userService = UserService(repository)
        controller = UserController(userService)
    }

    @Test
    fun `test getUserProfile returns profile for valid user`() = runBlocking {
        val response = controller.getUserProfile("user-1")
        assertEquals(HttpStatus.OK, response.statusCode)

        val profile = response.body
        assertNotNull(profile)
        assertEquals("codehorn_demo", profile?.username)
        assertEquals("Alex Developer", profile?.fullName)
    }

    @Test
    fun `test getUserDashboard returns dashboard metrics`() = runBlocking {
        val response = controller.getUserDashboard("user-1")
        assertEquals(HttpStatus.OK, response.statusCode)

        val dashboard = response.body
        assertNotNull(dashboard)
        assertEquals(28, dashboard?.problemStats?.totalSolved)
        assertTrue(dashboard?.solvedProblemIds?.contains("1") == true)
        assertTrue(dashboard?.activityMap?.isNotEmpty() == true)
    }

    @Test
    fun `test updateProfile modifies user bio`() = runBlocking {
        val updateRequest = UpdateUserProfileRequest(
            fullName = "Alex Updated",
            bio = "Updated Bio Text"
        )
        val response = controller.updateProfile("user-1", updateRequest)
        assertEquals(HttpStatus.OK, response.statusCode)

        val updatedProfile = response.body
        assertEquals("Alex Updated", updatedProfile?.fullName)
        assertEquals("Updated Bio Text", updatedProfile?.bio)
    }

    @Test
    fun `test recordActivity updates statistics and streak reactively`() = runBlocking {
        val activityRequest = RecordActivityRequest(
            problemId = "99",
            problemSlug = "new-hard-problem",
            problemTitle = "New Hard Problem",
            difficulty = Difficulty.Hard,
            status = "Accepted",
            language = "kotlin",
            runtime = 12,
            memory = 8.5
        )

        val response = controller.recordActivity("user-1", activityRequest)
        assertEquals(HttpStatus.CREATED, response.statusCode)

        val updatedDashboard = response.body
        assertNotNull(updatedDashboard)
        assertEquals(29, updatedDashboard?.problemStats?.totalSolved)
        assertEquals(4, updatedDashboard?.problemStats?.hardSolved)
        assertTrue(updatedDashboard?.solvedProblemIds?.contains("99") == true)
    }
}
