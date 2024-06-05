package com.thedearbear

import jadx.api.*
import jadx.core.dex.instructions.ConstStringNode
import jadx.core.dex.instructions.InsnType
import java.io.File

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        error("Please provide file!")
    } else if (args.size > 1) {
        error("Too many arguments")
    }

    val jArgs = JadxArgs()
    jArgs.isSkipResources = true
    jArgs.decompilationMode = DecompilationMode.FALLBACK
    jArgs.setInputFile(File(args[0]))

    try {
        val decompiler = JadxDecompiler(jArgs)
        decompiler.load()

        for (clazz: JavaClass in decompiler.classes) {
            for (method: JavaMethod in clazz.methods) {
                val devKey = extractDevKey(method)

                if (devKey != null) {
                    println("DevKey found: $devKey")
                    println("Found in method ${method.fullName}")
                    return
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

private fun extractDevKey(method: JavaMethod): String? {
    if (method.arguments.size != 0 ||
        method.isClassInit || method.isConstructor ||
        !method.returnType.isObject) {
        return null
    }

    val node = method.methodNode
    node.reload()

    if (node.instructions == null) {
        return null
    }

    val instructions = node.instructions.filterNotNull()

    if (instructions.size != 4) {
        return null
    }

    var current = instructions[0]
    if (current.type != InsnType.NEW_INSTANCE ||
        !current.result.initType.isObject ||
        current.result.initType.`object` != method.returnType.`object`) {
        return null
    }

    current = instructions[1]

    return if (current is ConstStringNode && current.string.matches(Regex("[0-9a-f]+"))) {
        current.string
    } else {
        null
    }
}
