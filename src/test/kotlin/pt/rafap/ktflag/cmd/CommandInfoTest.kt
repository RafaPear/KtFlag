package pt.rafap.ktflag.cmd

import kotlin.test.Test
import kotlin.test.assertTrue

class CommandInfoTest {
    @Test
    fun toString_containsDescriptionAliasesAndUsage() {
        val info = CommandInfo(
            title = "T",
            description = "Short desc",
            longDescription = "Long desc",
            aliases = listOf("a", "b"),
            usage = "use it",
            minArgs = 0,
            maxArgs = 1
        )
        val s = info.toString()
        assertTrue("Short desc" in s)
        assertTrue("Aliases: a, b" in s)
        assertTrue("Usage: use it" in s)
    }
}
