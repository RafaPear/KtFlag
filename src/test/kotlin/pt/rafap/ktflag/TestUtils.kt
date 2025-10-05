package pt.rafap.ktflag

import java.io.ByteArrayOutputStream
import java.io.PrintStream

/**
 * Captures the standard output produced inside the given [block] and returns it as a String without ANSI escape codes.
 * Useful for testing output in assertions.
 */
fun captureStdout(block: () -> Unit): String {
    val original = System.out
    val baos = ByteArrayOutputStream()
    val ps = PrintStream(baos, true, "UTF-8")
    System.setOut(ps)
    try {
        block()
    } finally {
        System.setOut(original)
        ps.flush()
        ps.close()
    }
    val raw = String(baos.toByteArray(), Charsets.UTF_8)
    return stripAnsi(raw)
}

/**
 * Removes ANSI color/style escape sequences from the input string [s].
 * This helps make output assertions robust against terminal formatting.
 */
fun stripAnsi(s: String): String = s.replace("\u001B\\[[;\\d]*m".toRegex(), "")
