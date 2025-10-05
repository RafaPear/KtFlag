package pt.rafap.ktflag.cmd

import pt.rafap.ktflag.style.Colors

/**
 * Represents the outcome of executing a command.
 *
 * A result contains a short [cause] label, a human-friendly [message], a
 * [type] categorising the outcome (success / error variants), and an optional
 * typed payload [result] supplied by successful executions.
 *
 * @param T Type of the optional payload returned by the command.
 * @property cause Short label describing the category of the result (e.g., "Success", "Error").
 * @property message Detailed message about the outcome.
 * @property type Kind of result; influences formatting and semantics.
 * @property result Optional payload returned by the command execution.
 */
open class CommandResult<T>(
    val cause: String,
    val message: String,
    val type: CommandResultType,
    val result: T? = null
) {

    /**
     * Convenience constructor where [cause] doubles as the [message].
     */
    constructor(cause: String, type: CommandResultType, result: T? = null) :
            this(
                cause = cause,
                message = cause,
                type = type,
                result = result
            )

    /**
     * Prints a single colored line representing this result when it is
     * considered an error (any non-success [type]). Successes are also
     * printed with the same format for consistency.
     */
    fun printError() {
        println(Colors.colorText("${type.prefix}: $cause: $message", Colors.ERROR_COLOR))
    }

    /**
     * Appends [newMessage] to the existing message, separated by a newline.
     * Returns a new [CommandResult] instance with the updated message.
     */
    fun CommandResult<T>.appendMessage(newMessage: String): CommandResult<T> {
        return CommandResult(
            cause = this.cause,
            message = this.message + "\n" + newMessage,
            type = this.type,
            result = this.result
        )
    }

    /**
     * Creates a copy of the current [CommandResult] with optional overrides
     * for [cause], [message], [type], and [result].
     */
    fun CommandResult<T>.copy(
        cause: String = this.cause,
        message: String = this.message,
        type: CommandResultType = this.type,
        result: T? = this.result
    ): CommandResult<T> {
        return CommandResult(cause, message, type, result)
    }

    /**
     * Prints a standardized message for an unknown command [name], hinting the
     * user to consult the provided help command [cmdHelp].
     */
    @Deprecated("This function requires a help cmd as an argument which implies some bad meeh logic")
    fun printUnknownCommand(name: String, cmdHelp: CommandImpl<*>) {
        println(Colors.colorText("Unknown command: $name", Colors.ERROR_COLOR))
        print(Colors.colorText("Use", Colors.INFO_COLOR))
        print(Colors.colorText(" ${cmdHelp.info.aliases} ", Colors.HELP_ALIAS_COLOR))
        println(Colors.colorText("to see the list of available commands.", Colors.INFO_COLOR))
    }

    /** Generic error result with the provided [message]. */
    class ERROR<T>(message: String) :
        CommandResult<T>("An error occurred", message, CommandResultType.ERROR)

    /** Error for invalid argument count. */
    class INVALID_ARGS<T>(
        info: CommandInfo,
        got: Int
    ) : CommandResult<T>(
        "Invalid arguments",
        "Argument count must be between ${info.minArgs} and ${info.maxArgs}, got $got",
        CommandResultType.INVALID_ARGS
    )

    /** Not implemented marker error with a human-friendly [message]. */
    class NOT_IMPLEMENTED<T>(message: String) :
        CommandResult<T>("Not implemented", message, CommandResultType.NOT_IMPLEMENTED)

    /** Result indicating the user referenced a command name that does not exist. */
    class UNKNOWN_COMMAND<T>(message: String) :
        CommandResult<T>("Invalid input", message, CommandResultType.UNKNOWN_COMMAND)
}