package pt.isel.pt.rafap.ktflag.cmd

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
     * Executes the command with the provided arguments.
     *
     * @param arg Positional arguments supplied to the command.
     * @param context Optional execution context shared across commands.
     * @return A [CommandResult] representing the outcome of the execution.
     */
    fun execute(vararg arg: String, context: T?): CommandResult<T>
}