package pt.rafap.ktflag.cmd

import pt.rafap.ktflag.ImplHelp

/**
 * Registry that holds and resolves command implementations by alias.
 *
 * By default, the registry auto-registers a built-in help command that lists all
 * available commands and shows details for a specific command. A custom help command
 * can be provided via [helpCmd].
 *
 * @param T The type of the optional execution context shared by commands.
 * @param helpCmd Optional custom help command; when null, a default implementation is registered.
 */
open class CommandRegister<T>(helpCmd: CommandImpl<T>? = null) {
    /** Map of alias to command implementation (aliases can map to the same command). */
    open val commands: MutableMap<String, CommandImpl<T>> = mutableMapOf()

    init {
        if (helpCmd != null)
            registerCommands(helpCmd)
        else
            registerCommands(ImplHelp<T>(this))
    }

    /**
     * Registers one or more [commands], binding each of their [CommandInfo.aliases]
     * to the implementation.
     */
    fun registerCommands(vararg commands: CommandImpl<T>) {
        for (cmd in commands) {
            for (alias in cmd.info.aliases) {
                this.commands[alias] = cmd
            }
        }
    }

    /** Returns the command associated with [alias], or null if none is registered. */
    operator fun get(alias: String): CommandImpl<T>? = commands[alias]

    /** Returns the distinct list of registered command implementations. */
    fun getAllCommands(): List<CommandImpl<T>> = commands.values.distinct().toList()
}