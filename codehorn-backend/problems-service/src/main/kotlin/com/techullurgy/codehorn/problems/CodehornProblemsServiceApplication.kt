package com.techullurgy.codehorn.problems

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@SpringBootApplication
@EnableDiscoveryClient
class CodehornProblemsServiceApplication

fun main(args: Array<String>) {
    runApplication<CodehornProblemsServiceApplication>(*args)
}