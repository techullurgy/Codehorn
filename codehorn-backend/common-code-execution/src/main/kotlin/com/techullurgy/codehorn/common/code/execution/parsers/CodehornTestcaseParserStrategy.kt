package com.techullurgy.codehorn.common.code.execution.parsers

import com.techullurgy.codehorn.common.models.ProblemTestcase
import org.springframework.boot.json.JsonParseException
import org.springframework.stereotype.Component
import tools.jackson.core.type.TypeReference
import tools.jackson.databind.ObjectMapper

@Component
class CodehornTestcaseParserStrategy: TestcaseParserStrategy() {
    override fun parse(testcase: ProblemTestcase): String {
        val builder = StringBuilder()
        val mapper = ObjectMapper()

        testcase.inputs.forEachIndexed { index, input ->
            val node = mapper.readTree(input)
            val currentMask = testcase.masks[index]

            var parsed = false

            try {
                if(isSingleType(currentMask)) {
                    if(isNullAllowed(currentMask)) {
                        if(node.isNull) {
                            builder.appendWithNewLine(null)
                            parsed = true
                        } else {
                            when {
                                isLongType(currentMask) -> {
                                    if(node.canConvertToLong()) {
                                        builder.appendWithNewLine(node.asLong())
                                        parsed = true
                                    }
                                }
                                isIntType(currentMask) -> {
                                    if(node.isIntegralNumber) {
                                        builder.appendWithNewLine(node.asInt())
                                        parsed = true
                                    }
                                }
                                isDoubleType(currentMask) -> {
                                    if(node.isDouble) {
                                        builder.appendWithNewLine(node.asDouble())
                                        parsed = true
                                    }
                                }
                                isStringType(currentMask) -> {
                                    if(node.isString) {
                                        builder.appendWithNewLine(node.asString())
                                        parsed = true
                                    }
                                }
                            }
                        }
                    } else {
                        when {
                            isLongType(currentMask) -> {
                                if(node.canConvertToLong()) {
                                    builder.appendWithNewLine(node.asLong())
                                    parsed = true
                                }
                            }
                            isIntType(currentMask) -> {
                                if(node.isIntegralNumber) {
                                    builder.appendWithNewLine(node.asInt())
                                    parsed = true
                                }
                            }
                            isDoubleType(currentMask) -> {
                                if(node.isDouble) {
                                    builder.appendWithNewLine(node.asDouble())
                                    parsed = true
                                }
                            }
                            isStringType(currentMask) -> {
                                if(node.isString) {
                                    builder.appendWithNewLine(node.asString())
                                    parsed = true
                                }
                            }
                        }
                    }
                } else if(is2DList(currentMask)) {
                    if(!is2dArray(input)) {
                        throw TestcaseInputNotValidException()
                    }

                    val list = when {
                        isLongType(currentMask) -> {
                            mapper.readValue(input, object : TypeReference<List<List<Long?>>>() {})
                        }
                        isIntType(currentMask) -> {
                            mapper.readValue(input, object : TypeReference<List<List<Int?>>>() {})
                        }
                        isDoubleType(currentMask) -> {
                            mapper.readValue(input, object : TypeReference<List<List<Double?>>>() {})
                        }
                        isStringType(currentMask) -> {
                            mapper.readValue(input, object : TypeReference<List<List<String?>>>() {})
                        }
                        else -> {
                            throw TestcaseInputNotValidException()
                        }
                    }

                    builder.appendWithNewLine(list.size)
                    list.forEach {
                        builder.appendWithNewLine(it.size)
                        it.let { v ->
                            v.forEach { value ->
                                builder.appendWithNewLine(value)
                            }
                        }
                    }
                    parsed = true
                } else if(is1DList(currentMask)) {
                    if(!is1dArray(input)) {
                        throw TestcaseInputNotValidException()
                    }

                    val list = when {
                        isLongType(currentMask) -> {
                            mapper.readValue(input, object : TypeReference<List<Long?>>() {})
                        }
                        isIntType(currentMask) -> {
                            mapper.readValue(input, object : TypeReference<List<Int?>>() {})
                        }
                        isDoubleType(currentMask) -> {
                            mapper.readValue(input, object : TypeReference<List<Double?>>() {})
                        }
                        isStringType(currentMask) -> {
                            mapper.readValue(input, object : TypeReference<List<String?>>() {})
                        }
                        else -> {
                            throw TestcaseInputNotValidException()
                        }
                    }

                    builder.appendWithNewLine(list.size)
                    list.forEach {
                        builder.appendWithNewLine(it)
                    }
                    parsed = true
                }

                if(!parsed) {
                    throw TestcaseInputNotValidException()
                }
            } catch (_: JsonParseException) {
                throw TestcaseInputNotValidException()
            }
        }

        return builder.toString()
    }

    private fun is1dArray(json: String): Boolean {
        try {
            val objectMapper = ObjectMapper()
            val rootNode = objectMapper.readTree(json)
            return rootNode.isArray
        } catch (_: Exception) {
            throw TestcaseInputNotValidException()
        }
    }

    private fun is2dArray(json: String): Boolean {
        try {
            val objectMapper = ObjectMapper()
            val rootNode = objectMapper.readTree(json)

            if (!rootNode.isArray) {
                return false
            }

            for (node in rootNode) {
                if (!node.isArray) {
                    return false
                }
            }

            return true
        } catch (_: Exception) {
            throw TestcaseInputNotValidException()
        }
    }

    private fun StringBuilder.appendWithNewLine(s: Any?) = append("$s\n")
}