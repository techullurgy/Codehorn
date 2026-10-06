package com.techullurgy.codehorn.common.code.execution.services.client

import com.github.dockerjava.api.DockerClient
import com.github.dockerjava.api.async.ResultCallback
import com.github.dockerjava.api.command.WaitContainerResultCallback
import com.github.dockerjava.api.exception.DockerClientException
import com.github.dockerjava.api.model.Frame
import com.github.dockerjava.api.model.HostConfig
import com.github.dockerjava.api.model.StreamType
import com.github.dockerjava.core.DefaultDockerClientConfig
import com.github.dockerjava.core.DockerClientImpl
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient
import com.github.dockerjava.transport.DockerHttpClient
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream
import java.io.Closeable
import java.io.File
import java.io.FileOutputStream
import java.nio.charset.StandardCharsets
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

class CodeExecutorClient(
    private val baseImage: String,
    private val workingDirInContainer: String = "/tmp",
    private val memory: Long = 512 * 1024 * 1024L,  // 512MB RAM
    private val memorySwap: Long = 512 * 1024 * 1024L, // No extra swap storage
    private val nanoCpus: Long = 1_000_000_000L // 1 vCPUs
) {
    private val dockerClient: DockerClient

    init {
        val dockerHost = System.getenv("TEMP_DOCKER_HOST") ?: DOCKER_HOST

        val config = DefaultDockerClientConfig.createDefaultConfigBuilder()
            .withDockerHost(dockerHost)
            .withDockerTlsVerify(false)
            .build()

        val httpClient: DockerHttpClient = ApacheDockerHttpClient.Builder()
            .dockerHost(config.dockerHost)
            .sslConfig(config.sslConfig)
            .build()

        dockerClient = DockerClientImpl.getInstance(config, httpClient)
    }

    fun createContainer(
        vararg command: String, // "javac", "Main.java"
        envs: List<String> = emptyList(),
    ): ContainerId {
        return try {
            val containerResponse = dockerClient.createContainerCmd(baseImage)
                .withHostConfig(
                    HostConfig.newHostConfig()
                        .withMemory(memory) // Hard limit RAM to (eg: 512MB)
                        .withMemorySwap(memorySwap) // Disables extra swap usage (stops swap escape) (eg: 512MB)
                        .withNanoCPUs(nanoCpus) // Cap execution capabilities to exactly 1 vCPU Core
                )
                .withNetworkDisabled(true) // No internet access inside container
                .withWorkingDir(workingDirInContainer)
                .withCmd(*command)
                .withEnv(envs)
                .exec()

            ContainerId(containerResponse.id)
        } catch (e: DockerClientException) {
            throw e
        }
    }

    fun uploadFile(
        containerId: ContainerId,
        hostAbsolutePath: String,
        containerPath: String,
    ) {
        dockerClient.copyArchiveToContainerCmd(containerId.id)
            .withHostResource(hostAbsolutePath)
            .withRemotePath(containerPath)
            .exec()
    }

    fun startContainer(containerId: ContainerId) {
        dockerClient.startContainerCmd(containerId.id).exec()
    }

    fun waitForContainer(
        containerId: ContainerId,
        timeoutInSeconds: Long = 60,
    ): Int {
        return try {
            dockerClient.waitContainerCmd(containerId.id)
                .exec(WaitContainerResultCallback())
                .awaitStatusCode(timeoutInSeconds, TimeUnit.SECONDS)
        } catch (e: DockerClientException) {
            throw TimeoutException(e.message)
        }
    }

    fun extractLogStream(containerId: ContainerId): LogStream {
        val latch = CountDownLatch(1)
        val stdoutBuffer = StringBuilder()
        val stderrBuffer = StringBuilder()

        dockerClient.logContainerCmd(containerId.id)
            .withStdOut(true)
            .withStdErr(true)
            .withFollowStream(true)
            .exec(object : ResultCallback.Adapter<Frame>() {
                override fun onNext(frame: Frame?) {
                    if(frame == null) return
                    val chunk = String(frame.payload)

                    when(frame.streamType) {
                        StreamType.STDOUT -> stdoutBuffer.append(chunk)
                        StreamType.STDERR -> stderrBuffer.append(chunk)
                        else -> println("Alternate frame type [${frame.streamType}] metadata observed: $chunk")
                    }
                }

                override fun onComplete() {
                    latch.countDown()
                }
            })

        val finishedGracefully = latch.await(1, TimeUnit.MINUTES)
        if(!finishedGracefully) {
            // Time Limit Exceeded
            println("Not Gracefully finished!")
        }

        return LogStream(stdoutBuffer.toString(), stderrBuffer.toString())
    }

    fun downloadFile(
        containerId: ContainerId,
        hostAbsoluteDirPath: String,
        containerPath: String,
    ) {
        dockerClient.copyArchiveFromContainerCmd(containerId.id, containerPath)
            .exec()
            .use { input ->
                TarArchiveInputStream(input).use { tais ->
                    var tarEntry = tais.nextEntry

                    while(tarEntry != null) {
                        val inPlaceExtractPathConvert = tarEntry.name.substringAfter("/")
                        val targetFile = File("$hostAbsoluteDirPath/$inPlaceExtractPathConvert")

                        if(tarEntry.isDirectory) {
                            targetFile.mkdirs()
                        } else {
                            // Create parent directories if they don't exist yet
                            targetFile.parentFile.mkdirs();
                            FileOutputStream(targetFile).use {
                                tais.transferTo(it)
                            }
                        }
                        tarEntry = tais.nextEntry
                    }
                }
            }
    }

    fun downloadSingleFileAsString(
        containerId: ContainerId,
        containerFilePath: String,
    ): String {
        return dockerClient.copyArchiveFromContainerCmd(containerId.id, containerFilePath)
            .exec()
            .use { input ->
                TarArchiveInputStream(input).use { tais ->
                    val tarEntry = tais.nextEntry ?: throw NoSuchFileException(
                        file = File(containerFilePath),
                        reason = "File ($containerFilePath) not found in the container (${containerId.id})"
                    )

                    if(tarEntry.isDirectory) {
                        throw IllegalStateException("Tar Entry is Directory ${tarEntry.name}, and we expect single file")
                    }

                    tais.readBytes().toString(StandardCharsets.UTF_8)
                }
            }
    }

    fun removeContainer(containerId: ContainerId) {
        dockerClient.removeContainerCmd(containerId.id)
            .withForce(true) // Forces removal even if unexpectedly running
            .exec()
    }


    internal fun exec(containerId: ContainerId, vararg command: String): LogStream {
        val latch = CountDownLatch(1)
        val stdoutBuffer = StringBuilder()
        val stderrBuffer = StringBuilder()

        dockerClient.execCreateCmd(containerId.id)
            .withAttachStdout(true)
            .withAttachStderr(true)
            .withWorkingDir(workingDirInContainer)
            .withCmd(*command)
            .exec()
            .also {
                dockerClient.execStartCmd(it.id)
                    .exec(object: ResultCallback<Frame> {
                        override fun onStart(closeable: Closeable?) {
                            println("onStart")
                        }

                        override fun onNext(frame: Frame?) {
                            if(frame != null) {
                                val chunk = String(frame.payload)

                                when(frame.streamType) {
                                    StreamType.STDOUT -> stdoutBuffer.append(chunk)
                                    StreamType.STDERR -> stderrBuffer.append(chunk)
                                    else -> println("Alternate frame type [${frame.streamType}] metadata observed: $chunk")
                                }
                            }
                        }

                        override fun onError(throwable: Throwable?) {
                            println("onError: ${throwable?.printStackTrace()}")
                        }

                        override fun onComplete() {
                            println("onComplete")
                            latch.countDown()
                        }

                        override fun close() {
                            println("onClose")
                        }
                    })
            }

        val finishedGracefully = latch.await(1, TimeUnit.MINUTES)
        if(!finishedGracefully) {
            // Time Limit Exceeded
            println("Not Gracefully finished!")
        }

        return LogStream(stdoutBuffer.toString(), stderrBuffer.toString())
    }
}

private const val DOCKER_HOST = "tcp://localhost:2375"