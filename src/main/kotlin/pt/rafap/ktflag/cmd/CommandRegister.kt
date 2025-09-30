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
class CommandRegister<T>(vararg cmd: CommandImpl<T>, helpCmd: CommandImpl<T>? = null) {
    /** Map of alias to command implementation (aliases can map to the same command). */
    val commands: Map<String, CommandImpl<T>>

    init {
        val tempCommands = cmd.toList().plus(helpCmd ?: ImplHelp<T>(this))
        this.commands = parseCommands(*tempCommands.toTypedArray())
    }

    /**
     * Registers one or more [commands], binding each of their [CommandInfo.aliases]
     * to the implementation.
     */
    private fun parseCommands(vararg commands: CommandImpl<T>): Map<String, CommandImpl<T>> =
        buildMap {
            for (cmd in commands) {
                for (alias in cmd.info.aliases) {
                    put(alias, cmd)
                }
            }
        }

    /** Returns the command associated with [alias], or null if none is registered. */
    operator fun get(alias: String): CommandImpl<T>? = commands[alias]

    /** Returns the distinct list of registered command implementations. */
    fun getAllCommands(): List<CommandImpl<T>> = commands.values.distinct().toList()
}