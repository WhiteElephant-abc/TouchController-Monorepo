package top.fifthlight.touchcontroller.resources.generator

import kotlinx.serialization.json.Json
import top.fifthlight.bazel.worker.api.Worker
import java.io.PrintWriter
import java.io.StringWriter
import java.nio.file.Path
import java.util.*
import kotlin.io.path.readText
import kotlin.io.path.writer

fun main(vararg args: String) = object : Worker() {
    override fun handleRequest(
        out: PrintWriter,
        sandboxDir: Path,
        vararg args: String
    ): Int {
        val file = sandboxDir.resolve(Path.of(args[0]))
        val outputFile = sandboxDir.resolve(Path.of(args[1]))
        val map: Map<String, String> = Json.decodeFromString(file.readText())
        val writeBuffer = StringWriter()
        Properties().apply {
            map.entries.forEach { (key, value) ->
                put(key, value.replace("%d", "%s"))
            }
        }.store(writeBuffer, "PARSE_ESCAPES")
        outputFile.writer().use { writer ->
            writeBuffer
                .toString()
                .lineSequence()
                .filterIndexed { index, _ -> index != 1 }
                .forEach {
                    writer.write(it)
                    writer.write('\n'.code)
                }
        }
        return 0
    }
}.run(*args)
