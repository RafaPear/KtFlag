package pt.rafap.ktflag.cmd

import pt.rafap.ktflag.cmd.CommandResult.ERROR
import pt.rafap.ktflag.cmd.CommandResult.INVALID_ARGS
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CommandResultTest {
    /**
     * Checks that the alternate constructor sets the cause label and message correctly for error results.
     */
    @Test
    fun alternateConstructor_setsCauseAsLabelAndMessage() {
        val r = CommandResult<String>("Something happened", CommandResultType.ERROR)
        assertEquals("Something happened", r.cause)
        assertEquals("Something happened", r.message)
        assertEquals(r.type, CommandResultType.ERROR)
    }

    /**
     * Verifies that the error factory produces the expected cause and message.
     */
    @Test
    fun errorFactory_hasExpectedCauseAndMessage() {
        val r = ERROR<String>("Boom")
        assertEquals("An error occurred", r.cause)
        assertEquals("Boom", r.message)
        assertEquals(r.type, CommandResultType.ERROR)
    }

    /**
     * Ensures that invalidArgs formats the error message with argument details.
     */
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
        assertEquals(r.type, CommandResultType.INVALID_ARGS)
        assertTrue(r.message.contains("between 1 and 2, got 3"))
    }
}
