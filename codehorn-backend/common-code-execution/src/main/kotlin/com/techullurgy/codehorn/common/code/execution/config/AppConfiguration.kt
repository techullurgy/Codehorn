package com.techullurgy.codehorn.common.code.execution.config

import com.techullurgy.codehorn.common.code.execution.services.EnvProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AppConfiguration {

    @Bean
    fun envProvider(): EnvProvider = EnvProvider.Default
}