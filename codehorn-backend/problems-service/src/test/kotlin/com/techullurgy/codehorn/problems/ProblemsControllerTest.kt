package com.techullurgy.codehorn.problems

import com.techullurgy.codehorn.common.models.Difficulty
import com.techullurgy.codehorn.problems.controllers.ProblemsController
import com.techullurgy.codehorn.problems.data.InMemoryProblemRepository
import com.techullurgy.codehorn.problems.dto.CreateProblemRequest
import com.techullurgy.codehorn.problems.service.ProblemsService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus

class ProblemsControllerTest {

    private lateinit var problemsService: ProblemsService
    private lateinit var controller: ProblemsController

    @BeforeEach
    fun setUp() {
        val repository = InMemoryProblemRepository()
        problemsService = ProblemsService(repository)
        controller = ProblemsController(problemsService)
    }

    @Test
    fun `test getAllProblems returns pre-seeded problems`() {
        val problems = controller.getAllProblems(null, null, null)
        assertTrue(problems.isNotEmpty())
        assertEquals(5, problems.size)
    }

    @Test
    fun `test getProblemBySlug returns problem with starterCode templates and solutions`() {
        val response = controller.getProblemBySlug("two-sum")
        assertEquals(HttpStatus.OK, response.statusCode)

        val problem = response.body
        assertNotNull(problem)
        assertEquals("Two Sum", problem?.title)
        assertTrue(problem?.starterCode?.containsKey("python") == true)
        assertTrue(problem?.templates?.containsKey("java") == true)
        assertTrue(problem?.solutions?.containsKey("javascript") == true)
    }

    @Test
    fun `test getProblemTemplates returns driver execution templates`() {
        val response = controller.getProblemTemplates("two-sum")
        assertEquals(HttpStatus.OK, response.statusCode)

        val templates = response.body
        assertNotNull(templates)
        assertTrue(templates!!.containsKey("java"))
        assertTrue(templates["java"]!!.contains("public class Main"))
    }

    @Test
    fun `test filter problems by difficulty`() {
        val easyProblems = controller.getAllProblems(Difficulty.Easy, null, null)
        assertTrue(easyProblems.all { it.difficulty == Difficulty.Easy })
    }

    @Test
    fun `test getProblemTestcases returns testcases`() {
        val response = controller.getProblemTestcases("two-sum", sampleOnly = true)
        assertEquals(HttpStatus.OK, response.statusCode)
        val testcases = response.body
        assertNotNull(testcases)
        assertTrue(testcases!!.all { it.isSample })
    }

    @Test
    fun `test createProblem adds new problem`() {
        val request = CreateProblemRequest(
            title = "Reverse String",
            difficulty = Difficulty.Easy,
            category = "Algorithms / Strings",
            description = "Reverse string s in-place",
            starterCode = mapOf("python" to "def reverseString(s): pass"),
            templates = mapOf("python" to "# USER_CODE_HERE\nprint(reverseString('hello'))"),
            solutions = mapOf("python" to "def reverseString(s): return s[::-1]")
        )

        val response = controller.createProblem(request)
        assertEquals(HttpStatus.CREATED, response.statusCode)
        val created = response.body
        assertNotNull(created)
        assertEquals("reverse-string", created?.slug)
        assertTrue(created?.templates?.containsKey("python") == true)
    }
}
