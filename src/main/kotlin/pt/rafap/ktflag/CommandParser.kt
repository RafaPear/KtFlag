package pt.rafap.ktflag

import pt.rafap.ktflag.cmd.CommandImpl
import pt.rafap.ktflag.cmd.CommandRegister
import pt.rafap.ktflag.cmd.CommandResult
import pt.rafap.ktflag.style.Colors
import pt.rafap.ktflag.style.Colors.colorText
import kotlin.collections.drop
import kotlin.text.split

/**
 * A simple command line parser that reads user input, parses it into commands
 * and arguments, and executes the corresponding registered command.
 *
 * @param T The type of the context object passed to command executions.
 * @property prompt The prompt string displayed to the user.
 * @property helpCmd An optional help command to be registered by default.
 */
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
     * Reads a line of input from the user, parses it into a command and arguments,
     * and executes the corresponding registered command with the provided context.
     *
     * @param context An optional context object passed to the command execution.
     * @return The CommandResult from executing the command, or null if input is empty
     *         or the command is not found.
     */
    fun readInputAndGetResult(context: T?): CommandResult<T>? {
        print(config.prompt)
        val input = readln()
        if (input.isEmpty()) return null

        val parts = input.split(" ")
        val cmdName = parts[0]
        val args = parts.drop(1).toTypedArray()

        val cmd = commandRegister[cmdName] ?:
        return CommandResult.INVALID_INPUT("Command '$cmdName' was not found")

        return cmd.execute(*args, context = context)
    }
}