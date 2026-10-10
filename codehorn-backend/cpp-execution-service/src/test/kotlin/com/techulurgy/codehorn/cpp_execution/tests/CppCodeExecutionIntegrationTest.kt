package com.techulurgy.codehorn.cpp_execution.tests

import com.techullurgy.codehorn.common.code.execution.parsers.CodehornTestcaseParserStrategy
import com.techullurgy.codehorn.common.code.execution.services.CodeEvaluationService
import com.techullurgy.codehorn.common.models.ParsedTestcase
import com.techullurgy.codehorn.common.models.ProblemTestcase
import com.techullurgy.codehorn.common.models.TestcaseCollectionType
import com.techullurgy.codehorn.common.models.TestcaseDataType
import com.techullurgy.codehorn.common.models.TestcaseType
import com.techullurgy.codehorn.cpp_execution.services.CppCodeEvaluator
import com.techullurgy.codehorn.cpp_execution.services.CppCodeEvaluatorFactory
import com.techulurgy.codehorn.cpp_execution.test_utils.TestEnvProvider
import com.techulurgy.codehorn.cpp_execution.test_utils.TestFileContent
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName
import kotlin.system.measureTimeMillis
import kotlin.test.assertEquals

@Testcontainers
class CppCodeExecutionIntegrationTest {

    companion object {
        @JvmStatic
        @Container
        private val dindContainer = GenericContainer(
            DockerImageName.parse("docker:29.8.2-dind")
        )
            .withPrivilegedMode(true)
            .withEnv("DOCKER_TLS_CERTDIR", "")
            .withExposedPorts(2375)
            .waitingFor(Wait.forLogMessage(".*API listen on \\[::\\]:2375.*\\n", 1))

        @JvmStatic
        @BeforeAll
        fun beforeAll() {
            val dockerPull = dindContainer.execInContainer(
                "docker", "pull", CppCodeEvaluator.BASE_IMAGE
            )

            assertEquals(0, dockerPull.exitCode, "Pre pull failed: ${dockerPull.stderr}")
        }
    }

    private lateinit var codeEvaluationService: CodeEvaluationService

    private lateinit var parsedTestcaseProvider: (List<List<String>>) -> List<ParsedTestcase>

    @BeforeEach
    fun setup() {
        val dockerHostPort = dindContainer.getMappedPort(2375)
        val dockerHost = "tcp://${dindContainer.host}:$dockerHostPort"

        codeEvaluationService = CodeEvaluationService(
            codeEvaluatorFactory = CppCodeEvaluatorFactory(
                envProvider = TestEnvProvider(
                    envMap = mapOf(
                        "APP_DOCKER_HOST" to dockerHost,
                    )
                )
            ),
        )

        val testcaseParserStrategy = CodehornTestcaseParserStrategy()

        parsedTestcaseProvider = {
            it.mapIndexed { index, inputs ->
                ProblemTestcase(
                    id = "${index+1}",
                    inputNames = listOf("x", "y"),
                    inputs = inputs,
                    masks = listOf(
                        TestcaseType(
                            dataType = TestcaseDataType.INT,
                            collectionType = TestcaseCollectionType.SINGLE
                        ),
                        TestcaseType(
                            dataType = TestcaseDataType.INT,
                            collectionType = TestcaseCollectionType.SINGLE
                        ),
                    ).map { it.mask }
                )
            }.map { pb ->
                ParsedTestcase(
                    id = pb.id,
                    testcase = testcaseParserStrategy.parse(pb)
                )
            }
        }
    }

    @Test
    fun allTestcaseShouldBeAccepted() {
        val totalTimeTaken = measureTimeMillis {
            val testcases = listOf(
                listOf("29", "54"),
                listOf("89", "-182"),
                listOf("92783", "78884"),
            )
            val parsedTestcases = parsedTestcaseProvider(testcases)

            val fileContent = TestFileContent.AddTwoNumbers.buildWithUserCode(
                """   
                    int addTwoNumbers(int x, int y) {
                        std::cout << "Answer is " << (x + y) << std::endl;
                        return x + y;
                    }
                """.trimIndent()
            )

            runBlocking {
                val results = codeEvaluationService.evaluateFor(
                    evaluationId = "test1",
                    fileContent = fileContent,
                    testcases = parsedTestcases
                )

//                assertEquals(testcases.size, results.size)
                println(results)
            }
        }

        println("===== TOTAL TIME TAKEN : [$totalTimeTaken] milliseconds ======")
    }

    @Test
    fun compilationError() {
        val totalTimeTaken = measureTimeMillis {
            val testcases = listOf(
                listOf("29", "54"),
                listOf("89", "-182"),
                listOf("92783", "78884"),
            )
            val parsedTestcases = parsedTestcaseProvider(testcases)

            val fileContent = TestFileContent.AddTwoNumbers.buildWithUserCode(
                """
                    int addTwoNumbers(int x, int y) {
                        std::cout << "Answer is " << (x + y) << std::endl
                        return x + y;
                    }
                """.trimIndent()
            )

            runBlocking {
                val results = codeEvaluationService.evaluateFor(
                    evaluationId = "test2",
                    fileContent = fileContent,
                    testcases = parsedTestcases
                )

//                assertEquals(testcases.size, results.size)
                println(results)
            }
        }

        println("===== TOTAL TIME TAKEN : [$totalTimeTaken] milliseconds ======")
    }

    @Test
    fun runtimeError() {
        val totalTimeTaken = measureTimeMillis {
            val testcases = listOf(
                listOf("29", "54"),
                listOf("89", "-182"),
                listOf("92783", "78884"),
            )
            val parsedTestcases = parsedTestcaseProvider(testcases)

            val fileContent = TestFileContent.AddTwoNumbers.buildWithUserCode(
                """
                    int addTwoNumbers(int x, int y) {        
                        std::cout << "Answer is " << (x + y) << std::endl;
                        if(x == 89) {
                            int unused = x / 0; // Divide by Zero
                        }
                        return x + y;
                    }
                """.trimIndent()
            )

            runBlocking {
                val results = codeEvaluationService.evaluateFor(
                    evaluationId = "test3",
                    fileContent = fileContent,
                    testcases = parsedTestcases
                )

//                assertEquals(testcases.size, results.size)
                println(results)
            }
        }

        println("===== TOTAL TIME TAKEN : [$totalTimeTaken] milliseconds ======")
    }

    @Test
    fun timeLimitExceeded() {
        val totalTimeTaken = measureTimeMillis {
            val testcases = listOf(
                listOf("29", "54"),
                listOf("89", "-182"),
                listOf("92783", "78884"),
            )
            val parsedTestcases = parsedTestcaseProvider(testcases)

            val fileContent = TestFileContent.AddTwoNumbers.buildWithUserCode(
                """
                    int addTwoNumbers(int x, int y) {
                        std::cout << "Answer is " << (x + y) << std::endl;
                        if(x == 89) {
                            while(true) {}
                        }
                        return x + y;
                    }
                """.trimIndent()
            )

            runBlocking {
                val results = codeEvaluationService.evaluateFor(
                    evaluationId = "test3",
                    fileContent = fileContent,
                    testcases = parsedTestcases
                )

//                assertEquals(testcases.size, results.size)
                println(results)
            }
        }

        println("===== TOTAL TIME TAKEN : [$totalTimeTaken] milliseconds ======")
    }
}