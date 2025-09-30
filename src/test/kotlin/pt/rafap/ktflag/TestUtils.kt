package pt.rafap.ktflag

import java.io.ByteArrayOutputStream
import java.io.PrintStream

/** Utility to capture stdout produced inside [block] and return it as a String (without ANSI). */
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

/** Removes ANSI color/style escape sequences from [s] for robust assertions. */
fun stripAnsi(s: String): String = s.replace("\u001B\\[[;\\d]*m".toRegex(), "")
