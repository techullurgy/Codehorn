package com.techullurgy.codehorn.common.utils

import kotlin.io.encoding.Base64

fun String.toReadableString(): String = Base64.decode(this).decodeToString()

fun String.toBase64String(): String = Base64.encode(this.toByteArray())