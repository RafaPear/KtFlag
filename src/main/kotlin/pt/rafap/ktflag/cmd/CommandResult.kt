package pt.rafap.ktflag.cmd

import pt.rafap.ktflag.style.Colors

/**
 * Represents the outcome of executing a command.
 *
 * A result contains a [cause] label, a human-friendly [message], a boolean flag
 * [isError] indicating failure/success semantics, and an optional typed [result]
 * payload provided by successful executions.
 *
 * @param T Type of the optional payload returned by the command.
 * @property cause Short label describing the category of the result (e.g., "Success", "Error").
 * @property message Detailed message about the outcome.
 * @property isError Whether the result represents an error condition.
 * @property result Optional payload returned by the command execution.
 */
open class CommandResult<T>(
    val cause: String,
    val message: String,
    val isError: Boolean,
    val result: T? = null
) {

    /**
     * Convenience constructor where [cause] doubles as the [message] and the
     * [isError] flag controls the labeled [cause] ("Error" or "Info").
     */
    constructor(cause: String, isError: Boolean = false, result: T? = null) :
            this(
                cause = if (isError) "Error" else "Info",
                message = cause,
                isError = isError,
                result = result
            )

    /** Prints a standardized colored error line when [isError] is true. */
    fun printError() {
        println(Colors.colorText("[ERROR] $cause: $message", Colors.ERROR_COLOR))
    }

    /** Common factory results and convenience wrappers. */

        /** Generic error result with the provided [message]. */
        class ERROR<T>(message: String) :
            CommandResult<T>("An error occurred", message, true)
        
        /** Error for invalid argument count: expected [info.minArgs]..[info.maxArgs], got [got]. */
        class INVALID_ARGS<T>(
            info: CommandInfo,
            got: Int
        ) : CommandResult<T>(
                "Invalid arguments",
                "Argument count must be between ${info.minArgs} and ${info.maxArgs}, got $got",
                true)

        /** Not implemented marker error with a human-friendly [message]. */
        class NOT_IMPLEMENTED<T>(message: String) :
            CommandResult<T>("Not implemented", message, true)

}