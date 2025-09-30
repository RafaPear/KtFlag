package pt.rafap.ktflag.cmd

import pt.rafap.ktflag.cmd.CommandInfo
import pt.rafap.ktflag.cmd.CommandResult
import pt.rafap.ktflag.captureStdout
import pt.rafap.ktflag.cmd.CommandResult.Companion.ERROR
import pt.rafap.ktflag.cmd.CommandResult.Companion.INVALID_ARGS
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CommandResultTest {
    @Test
    fun alternateConstructor_setsCauseAsLabelAndMessage() {
        val r = CommandResult<String>("Something happened", isError = true)
        assertEquals("Error", r.cause)
        assertEquals("Something happened", r.message)
        assertTrue(r.isError)
    }

    @Test
    fun errorFactory_hasExpectedCauseAndMessage() {
        val r = ERROR<String>("Boom")
        assertEquals("An error occurred", r.cause)
        assertEquals("Boom", r.message)
        assertTrue(r.isError)
    }

    @Test
    fun invalidArgs_formatsMessage() {
        val info = CommandInfo(
            title = "X",
            description = "d",
            aliases = listOf("x"),
            usage = "x",
            minArgs = 1,
            maxArgs = 2
        )
        val r = INVALID_ARGS<String>(info, got = 3)
        assertEquals("Invalid arguments", r.cause)
        assertTrue(r.isError)
        assertTrue(r.message.contains("between 1 and 2, got 3"))
    }

    @Test
    fun printError_printsStandardizedPrefix() {
        val r = ERROR<String>("Nope")
        val out = captureStdout { r.printError() }
        assertTrue(out.contains("[ERROR]"))
        // Full structure: [ERROR] <cause>: <message>
        assertTrue(out.contains("[ERROR] An error occurred: Nope"))
    }
}
