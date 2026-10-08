package com.techullurgy.codehorn.java_execution.tests

import com.techullurgy.codehorn.common.code.execution.parsers.CodehornTestcaseParserStrategy
import com.techullurgy.codehorn.common.code.execution.services.CodeEvaluationService
import com.techullurgy.codehorn.common.code.execution.services.EnvProvider
import com.techullurgy.codehorn.common.code.execution.services.templates.utils.JavaTemplates
import com.techullurgy.codehorn.common.models.ParsedTestcase
import com.techullurgy.codehorn.common.models.ProblemTestcase
import com.techullurgy.codehorn.common.models.TestcaseCollectionType
import com.techullurgy.codehorn.common.models.TestcaseDataType
import com.techullurgy.codehorn.common.models.TestcaseType
import com.techullurgy.codehorn.java_execution.services.JavaCodeEvaluatorFactory
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName
import kotlin.system.measureTimeMillis
import kotlin.test.assertEquals

@Testcontainers
class JavaCodeExecutionIntegrationTest {

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
                "docker", "pull", "amazoncorretto:25"
            )

            assertEquals(0, dockerPull.exitCode, "Pre pull failed: ${dockerPull.stderr}")
        }
    }

    @Test
    fun basicTest() {
        val dockerHostPort = dindContainer.getMappedPort(2375)
        val dockerHost = "tcp://${dindContainer.host}:$dockerHostPort"

        val totalTimeTaken = measureTimeMillis {
            val codeEvaluationService = CodeEvaluationService(
                codeEvaluatorFactory = JavaCodeEvaluatorFactory(
                    envProvider = TestEnvProvider(
                        envMap = mapOf(
                            "APP_DOCKER_HOST" to dockerHost,
                        )
                    )
                ),
            )

            val testcaseParserStrategy = CodehornTestcaseParserStrategy()

            val parsedTestcases = listOf(
                ProblemTestcase(
                    id = "1",
                    inputNames = listOf("x", "y"),
                    inputs = listOf("23", "56"),
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
            ).map { pb ->
                ParsedTestcase(
                    id = pb.id,
                    testcase = testcaseParserStrategy.parse(pb)
                )
            }

            val fileContent = TEST_FILE_CONTENT

            runBlocking {
                val results = codeEvaluationService.evaluateFor(
                    evaluationId = "test",
                    fileContent = fileContent,
                    testcases = parsedTestcases
                )

                println(results)
            }
        }

        println("===== TOTAL TIME TAKEN : [$totalTimeTaken] milliseconds ======")
    }
}

private class TestEnvProvider(
    private val envMap: Map<String, String>
): EnvProvider {
    override fun get(name: String): String? = envMap[name]
}

// Add Two Numbers (Java)
private val TEST_FILE_CONTENT = """
    ${JavaTemplates.IMPORTS}
    
    ${JavaTemplates.UTILS}
    
    class OriginalSolution {
        public int addTwoNumbers(int x, int y) {
            return x + y;
        }
    }
    
    class Solution {
        public int addTwoNumbers(int x, int y) {
            System.out.println("Answer is " + (x+y));
            return x + y;
        }
    }
    
    public class Main {
        public static void main(String[] args) throws Exception{
            MainUtils.readFromFileAndSaveInMap("/tmp/testcase.txt");
            
            int acceptedCode = Integer.parseInt(System.getenv("CODE_ACCEPTED"));
            int wrongAnswerCode = Integer.parseInt(System.getenv("CODE_WRONG_ANSWER"));
            
            int x = MainUtils.getInteger();
            int y = MainUtils.getInteger();
            
            int eResult = new OriginalSolution().addTwoNumbers(x,y);
            int result = new Solution().addTwoNumbers(x,y);
            
            MainUtils.writeResults(String.valueOf(eResult), String.valueOf(result));
            
            if(eResult == result) {
                // Accepted
                System.exit(acceptedCode);
            } else {
                // Wrong Answer
                System.exit(wrongAnswerCode);
            }
        }
    }
""".trimIndent()