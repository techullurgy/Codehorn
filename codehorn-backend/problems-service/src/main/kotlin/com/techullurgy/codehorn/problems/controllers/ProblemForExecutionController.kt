package com.techullurgy.codehorn.problems.controllers

import com.techullurgy.codehorn.common.dto.ProblemExecutionMetadataDto
import com.techullurgy.codehorn.common.models.Language
import com.techullurgy.codehorn.common.responses.ProblemForExecutionResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/problems/execution")
class ProblemForExecutionController {

    @GetMapping("sample/{problemId}/{language}")
    suspend fun getProblemForSampleExecution(
        @PathVariable("problemId") problemId: String,
        @PathVariable("language") language: Language
    ): ResponseEntity<ProblemForExecutionResponse> {
        return ResponseEntity.ok(
            ProblemForExecutionResponse(
                problemId = problemId,
                language = language,
                testcases = listOf(),
                metadata = ProblemExecutionMetadataDto(
                    language = language,
                    imports = "",
                    main = "",
                    utils = "",
                    solution = "",
                )
            )
        )
    }

    @GetMapping("full/{problemId}/{language}")
    suspend fun getProblemForFullExecution(
        @PathVariable("problemId") problemId: String,
        @PathVariable("language") language: Language
    ): ResponseEntity<ProblemForExecutionResponse> {
        return ResponseEntity.ok(
            ProblemForExecutionResponse(
                problemId = problemId,
                language = language,
                testcases = listOf(),
                metadata = ProblemExecutionMetadataDto(
                    language = language,
                    imports = "",
                    main = "",
                    utils = "",
                    solution = "",
                )
            )
        )
    }
}