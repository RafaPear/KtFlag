package pt.rafap.ktflag.cmd

/**
 * Contract for a CLI command implementation.
 *
 * Implementations encapsulate the metadata about the command via [info]
 * and the execution logic via [execute].
 *
 * @param T The type of the optional execution context passed to [execute].
 */
interface CommandImpl<T> {
    /**
     * Descriptive and usage information for this command, including aliases and
     * argument cardinality constraints.
     */
    val info: CommandInfo

    /**
     * Checks whether the provided argument count [got] is within the inclusive
     * range defined by this command's [CommandInfo.minArgs] and [CommandInfo.maxArgs].
     */
    fun CommandImpl<*>.verifyArgsCount(got: Int): Boolean {
        val min = this.info.minArgs
        val max = this.info.maxArgs
        return got in min..max
    }

    /**
     * Executes the command with the provided arguments.
     *
     * @param arg Positional arguments supplied to the command.
     * @param context Optional execution context shared across commands.
     * @return A [CommandResult] representing the outcome of the execution.
     */
    fun execute(vararg arg: String, context: T?): CommandResult<T>

    fun joinToString(): String{
        return this.info.toString()
    }
}