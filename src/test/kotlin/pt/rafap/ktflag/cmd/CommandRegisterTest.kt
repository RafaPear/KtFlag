package pt.rafap.ktflag.cmd

import pt.rafap.ktflag.cmd.support.DummyCommand
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertSame

class CommandRegisterTest {
    /**
     * Checks that the built-in help command is auto-registered with common aliases.
     */
    @Test
    fun autoRegistersHelpWithCommonAliases() {
        val reg = CommandRegister<Unit>()
        val help = reg["help"]
        assertNotNull(help)
        // Built-in help uses aliases h, help, ?
        assertSame(help, reg["h"])
        assertSame(help, reg["?"])
    }

    /**
     * Verifies that registering commands binds all aliases and deduplicates them in getAllCommands().
     */
    @Test
    fun registerCommands_bindsAllAliasesAndDedupsInGetAllCommands() {
        val dummyInfo = CommandInfo(
            title = "Dummy",
            description = "desc",
            aliases = listOf("a", "alpha"),
            usage = "a [x]",
            minArgs = 0,
            maxArgs = 1
        )
        val dummy = DummyCommand<Unit>(dummyInfo)

        val reg = CommandRegister(dummy)
        assertSame(dummy, reg["a"])
        assertSame(dummy, reg["alpha"])

        val all = reg.getAllCommands()
        // Expect exactly 1 command: built-in help + dummy
        assertEquals(2, all.size)
    }

    /**
     * Ensures that a custom help command in the constructor replaces the default only for the given aliases.
     */
    @Test
    fun customHelpInConstructor_replacesDefaultHelpOnlyForGivenAliases() {
        val customHelpInfo = CommandInfo(
            title = "XHelp",
            description = "custom",
            aliases = listOf("help"),
            usage = "help",
            minArgs = 0,
            maxArgs = 1
        )
        val customHelp = DummyCommand<Unit>(customHelpInfo)
        val reg = CommandRegister(helpCmd = customHelp)

        assertSame(customHelp, reg["help"]) // provided alias present
        // Default aliases like "h" or "?" are not automatically added for custom help
        // so they should not resolve to the customHelp unless explicitly provided.
        // They may be null depending on implementation choice.
        assertEquals(null, reg["h"])
        assertEquals(null, reg["?"])
    }
}
