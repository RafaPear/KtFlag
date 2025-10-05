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
    private fun parseCommands(vararg commands: CommandImpl<T>): Map<String, CommandImpl<T>> = buildMap {
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

    /**
     * Returns a list of commands whose alias contains the given [alias] string
     * (case-insensitive). Each command is returned at most once.
     */
    fun getSimilarCommands(alias: String): List<CommandImpl<T>> {
        val lowerAlias = alias.lowercase()
        return commands.filterKeys { it.lowercase().contains(lowerAlias) }.values.distinct().toList()
    }

    /**
     * Attempts to find the most similar command to the provided [alias] by applying
     * the following precedence (case-insensitive):
     * 1. Exact alias match
     * 2. Alias starting with the query
     * 3. Alias containing the query
     * Returns the first match found, or null if none match.
     */
    fun getTheMostSimilarCommand(alias: String): CommandImpl<T>? {
        val lowerAlias = alias.lowercase()
        return commands.filterKeys { it.lowercase() == lowerAlias }.values.firstOrNull()
               ?: commands.filterKeys { it.lowercase().startsWith(lowerAlias) }.values.firstOrNull()
               ?: commands.filterKeys { it.lowercase().contains(lowerAlias) }.values.firstOrNull()
    }
}