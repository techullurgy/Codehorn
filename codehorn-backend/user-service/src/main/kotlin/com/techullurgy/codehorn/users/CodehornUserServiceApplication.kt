package com.techullurgy.codehorn.users

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@SpringBootApplication
@EnableDiscoveryClient
class CodehornUserServiceApplication

fun main(args: Array<String>) {
    runApplication<CodehornUserServiceApplication>(*args)
}