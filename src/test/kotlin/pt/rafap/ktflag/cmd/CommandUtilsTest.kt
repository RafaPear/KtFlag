package pt.rafap.ktflag.cmd

import pt.isel.pt.rafap.ktflag.cmd.CommandImpl
import pt.isel.pt.rafap.ktflag.cmd.CommandInfo
import pt.isel.pt.rafap.ktflag.cmd.CommandResult
import pt.isel.pt.rafap.ktflag.cmd.CommandUtils
import pt.rafap.ktflag.captureStdout
import pt.rafap.ktflag.stripAnsi
import pt.isel.pt.rafap.ktflag.cmd.CommandUtils.verifyArgsCount
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CommandUtilsTest {
    private fun dummy(min: Int, max: Int): CommandImpl<Unit> =
        object : CommandImpl<Unit> {
            override val info = CommandInfo(
                title = "D",
                description = "d",
                aliases = listOf("d"),
                usage = "d",
                minArgs = min,
                maxArgs = max
            )
            override fun execute(vararg arg: String, context: Unit?): CommandResult<Unit> = CommandResult("ok", false)
        }

    @Test
    fun verifyArgsCount_trueWhenWithinRange() {
        val cmd = dummy(1, 3)
        assertTrue(cmd.verifyArgsCount(1))
        assertTrue(cmd.verifyArgsCount(2))
        assertTrue(cmd.verifyArgsCount(3))
    }

    @Test
    fun verifyArgsCount_falseWhenOutsideRange() {
        val cmd = dummy(2, 2)
        assertFalse(cmd.verifyArgsCount(1))
        assertFalse(cmd.verifyArgsCount(3))
    }

    @Test
    fun printUnknownCommand_printsHelpfulMessage() {
        val help = object : CommandImpl<Unit> {
            override val info = CommandInfo(
                title = "Help",
                description = "help",
                aliases = listOf("h", "help", "?"),
                usage = "help [<cmd>]",
                minArgs = 0,
                maxArgs = 1
            )
            override fun execute(vararg arg: String, context: Unit?): CommandResult<Unit> = CommandResult("ok", false)
        }
        val out = captureStdout { CommandUtils.printUnknownCommand("foo", help) }
        val s = stripAnsi(out)
        assertTrue(s.contains("Unknown command: foo"))
        assertTrue(s.contains("Use"))
        assertTrue(s.contains("h, help, ?") || s.contains("[h, help, ?]"))
        assertTrue(s.contains("list of available commands"))
    }
}