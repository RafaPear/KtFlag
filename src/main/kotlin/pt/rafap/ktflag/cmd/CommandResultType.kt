package pt.rafap.ktflag.cmd

/**
 * Categories of command execution outcomes. Each value provides a [prefix]
 * used when rendering messages to the console.
 */
enum class CommandResultType(val prefix: String) {
    /** Command executed successfully. */
    SUCCESS("[SUCCESS]"),

    /** Generic error during execution. */
    ERROR("[ERROR]"),

    /** Provided arguments did not match the required arity. */
    INVALID_ARGS("[INVALID ARGS]"),

    /** The referenced command name or alias does not exist. */
    UNKNOWN_COMMAND("[UNKNOWN COMMAND]"),

    /** Placeholder for functionality that has not yet been implemented. */
    NOT_IMPLEMENTED("[NOT IMPLEMENTED]");

    operator fun <T> invoke(
        message: String,
        result: T? = null
    ): CommandResult<T>{
        return CommandResult(message, type = this, result = result)
    }
}