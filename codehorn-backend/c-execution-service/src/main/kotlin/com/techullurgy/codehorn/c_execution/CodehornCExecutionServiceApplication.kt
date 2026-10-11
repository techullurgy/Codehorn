package com.techullurgy.codehorn.c_execution

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@SpringBootApplication(scanBasePackages = ["com.techullurgy.codehorn"])
@EnableDiscoveryClient
class CodehornCExecutionServiceApplication

fun main(args: Array<String>) {
    runApplication<CodehornCExecutionServiceApplication>(*args)
}