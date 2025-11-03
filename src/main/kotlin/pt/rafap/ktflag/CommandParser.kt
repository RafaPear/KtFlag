package pt.rafap.ktflag

import pt.rafap.ktflag.cmd.CommandImpl
import pt.rafap.ktflag.cmd.CommandRegister
import pt.rafap.ktflag.cmd.CommandResult
import pt.rafap.ktflag.style.Colors
import pt.rafap.ktflag.style.Colors.colorText

/**
 * A simple command line parser that reads user input, parses it into commands
 * and arguments, and executes the corresponding registered command.
 *
 * @param T The type of the context object passed to command executions.
 * @property config The parser config.
 */
@Suppress("unused")
class CommandParser<T>(
    vararg cmd: CommandImpl<T>,
    val config: ParserConfig<T> = ParserConfig(),
) {
    private val commandRegister: CommandRegister<T> = CommandRegister(*cmd, helpCmd = config.helpCmd)

    /**
     * Retrieves a registered command by its name or alias.
     *
     * @param name The name or alias of the command to retrieve.
     * @return The corresponding CommandImpl if found, or null otherwise.
     */
    fun getCommand(name: String): CommandImpl<T>? = commandRegister[name]

    /**
     * Retrieves all registered commands.
     *
     * @return A list of all CommandImpl instances registered.
     */
    fun getAllCommands(): List<CommandImpl<T>> = commandRegister.getAllCommands()

    /**
     * Prints the configured prompt and reads a single line from stdin.
     * This is a thin wrapper around [readln].
     *
     * @return The raw user input line (never null, may be empty).
     */
    fun readInput(): String {
        print(colorText(config.prompt, config.promptColor))
        return readln()
    }

    /**
     * Parses a previously captured [input] string into a command invocation and
     * executes it using the underlying register, returning its [CommandResult].
     *
     * @param input The raw line containing the command name followed by arguments separated by spaces.
     * @param context Optional context forwarded to the command execution.
     * @return The execution result, or null if the input is null/blank.
     */
    fun parseInputToResult(input: String?, context: T?): CommandResult<T>? {
        if (input == null || input.isEmpty()) return null

        val parts = input.split(" ")
        val cmdName = parts[0]
        val args = parts.drop(1).toTypedArray()

        val cmd = commandRegister[cmdName] ?: return CommandResult.UNKNOWN_COMMAND(cmdName)


        return try {
            cmd.executeWrapper(*args, context = context)
        } catch (ex: Exception) {
            CommandResult.ERROR("Error retrieving command '$cmdName': ${ex.message}")
        }
    }

    /**
     * Reads a line of input from the user, parses it into a command and arguments,
     * and executes the corresponding registered command with the provided context.
     *
     * @param context An optional context object passed to the command execution.
     * @return The CommandResult from executing the command, or null if input is empty
     *         or the command is not found.
     */
    fun readInputAndGetResult(context: T?): CommandResult<T>? {
        val input = readInput()
        return parseInputToResult(input, context)
    }

    /**
     * Prints a standardized error message for an unknown command plus helpful suggestions:
     * - A similar command if one is heuristically found.
     * - A hint to use the help command (first alias) if available.
     *
     * @param cmd The unknown command token the user attempted.
     * @param result The result object describing the unknown command error.
     */
    fun printUnknownCommandError(cmd: String, result: CommandResult<*>) {
        result.printError(config)
        val similarAlias = findTheMostSimilarCommand(cmd)?.getLargestAlias()
        val helpCmdAlias = getCommand("help")?.getLargestAlias()?.lowercase()
        if (similarAlias != null)
            println(colorText("[TIP]: Did you mean '$similarAlias'?", Colors.YELLOW))
        if (helpCmdAlias != null)
            println(colorText("[TIP]: Use '$helpCmdAlias' for more information.", Colors.YELLOW))
    }

    /**
     * Returns the longest alias token for this command. Useful for tips/suggestions
     * where a more descriptive alias improves readability.
     */
    fun CommandImpl<*>.getLargestAlias(): String =
        info.aliases.reduce { a, b -> if (a.length > b.length) a else b }

    /**
     * Finds commands whose alias contains the provided [name] fragment (case-insensitive).
     * Each command is returned at most once.
     */
    fun findSimilarCommands(name: String): List<CommandImpl<T>> =
        commandRegister.getSimilarCommands(name)

    /**
     * Attempts to find the most relevant command for the passed alias using the
     * underlying register's heuristic (exact match > prefix > substring).
     *
     * @return The most similar command or null if none match.
     */
    fun findTheMostSimilarCommand(name: String): CommandImpl<T>? =
        commandRegister.getTheMostSimilarCommand(name)
}