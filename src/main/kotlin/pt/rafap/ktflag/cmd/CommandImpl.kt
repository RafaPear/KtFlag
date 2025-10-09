package pt.rafap.ktflag.cmd

/**
 * Contract for a CLI command implementation.
 *
 * Implementations encapsulate the metadata about the command via [info]
 * and the execution logic via [execute].
 *
 * @param T The type of the optional execution context passed to [execute].
 */
abstract class CommandImpl<T> {
    /**
     * Descriptive and usage information for this command, including aliases and
     * argument cardinality constraints.
     */
    abstract val info: CommandInfo

    /**
     * Checks whether the provided argument count [got] is within the inclusive
     * range defined by this command's [CommandInfo.minArgs] and [CommandInfo.maxArgs].
     */
    fun CommandImpl<*>.verifyArgsCount(got: Int): Boolean {
        val min = this.info.minArgs
        val max = this.info.maxArgs
        if (max < 0) return got >= min
        return got in min..max
    }

    /**
     * Executes the command with the provided arguments.
     *
     * @param args Positional arguments supplied to the command.
     * @param context Optional execution context shared across commands.
     * @return A [CommandResult] representing the outcome of the execution.
     */
    abstract fun execute(vararg args: String, context: T?): CommandResult<T>

    /**
     * DO NOT OVERRIDE THIS METHOD.
     * Wrapper around [execute] that first verifies the argument count.
     *
     * If the count is invalid, returns [CommandResult.INVALID_ARGS] without
     * invoking [execute].
     *
     * @param arg Positional arguments supplied to the command.
     * @param context Optional execution context shared across commands.
     * @return A [CommandResult] representing the outcome of the execution,
     *         or [CommandResult.INVALID_ARGS] if the argument count is wrong.
     */
    fun executeWrapper(vararg arg: String, context: T?): CommandResult<T> {
        if (!verifyArgsCount(arg.size)) return CommandResult.INVALID_ARGS(info, arg.size)
        return execute(*arg, context = context)
    }
}