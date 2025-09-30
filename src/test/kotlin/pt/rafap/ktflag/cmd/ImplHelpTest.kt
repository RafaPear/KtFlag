package pt.rafap.ktflag.cmd

import pt.rafap.ktflag.captureStdout
import pt.rafap.ktflag.cmd.support.DummyCommand
import pt.rafap.ktflag.stripAnsi
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ImplHelpTest {

    private fun dummy(): DummyCommand<Unit> {
        val info = CommandInfo(
            title = "Dummy",
            description = "Does dummy things",
            longDescription = "Long dummy description",
            aliases = listOf("d", "dummy"),
            usage = "dummy [x]",
            minArgs = 0,
            maxArgs = 1
        )
        return DummyCommand(info)
    }

    @Test
    fun help_withoutArgs_printsListingForAllCommands() {
        val reg = CommandRegister<Unit>()
        val d = dummy()
        reg.registerCommands(d)
        val help = reg["help"]!!

        val out = captureStdout { help.execute(context = null) }
        val s = stripAnsi(out)
        // Preamble
        assertTrue(s.contains("Format: usage - aliases - description"))
        // Contains one-line entry for dummy command
        assertTrue(s.contains("dummy [x] - d, dummy - Does dummy things"))
    }

    @Test
    fun help_withKnownCommand_printsDetailedHelp() {
        val reg = CommandRegister<Unit>()
        val d = dummy()
        reg.registerCommands(d)
        val help = reg["help"]!!

        val out = captureStdout { help.execute("dummy", context = null) }
        val s = stripAnsi(out)
        assertTrue(s.contains("Usage: dummy [x]"))
        assertTrue(s.contains("Aliases: d, dummy"))
        assertTrue(s.contains("Description: Long dummy description"))
    }

    @Test
    fun help_withTooManyArgs_returnsInvalidArgsError() {
        val reg = CommandRegister<Unit>()
        val help = reg["help"]!!

        val result = help.execute("one", "two", context = null)
        assertTrue(result.isError)
        assertEquals("Invalid arguments", result.cause)
        assertTrue(result.message.contains("between 0 and 1, got 2"))
    }

    @Test
    fun help_withUnknownCommand_returnsError() {
        val reg = CommandRegister<Unit>()
        val help = reg["help"]!!

        val out = captureStdout {
            val r = help.execute("nope", context = null)
            assertTrue(r.isError)
            assertTrue(r.message.contains("Command 'nope' not found"))
        }
        val s = stripAnsi(out)
        // Preamble still printed
        assertTrue(s.contains("Format: usage - aliases - description"))
    }
}