package pt.rafap.ktflag.cmd.args

/**
 * Describes a single flag/option that a command accepts.
 *
 * A [CommandArg] can be matched by any of its [aliases]. It may optionally
 * [returnsValue] (consuming the next token as its value) and can be marked
 * as [isRequired]. Parsing behavior is implemented by [CommandArgsParser].
 *
 * Example: an optional boolean switch `--verbose` with no value, and a
 * required option `--name <value>` that consumes the next token.
 *
 * @property name Human-friendly identifier of the argument (not used for matching).
 * @property aliases Tokens that trigger this argument (e.g., "--test", "-t"). Must not be empty.
 * @property returnsValue When true, the next token is consumed as the value for this arg.
 * @property isRequired When true, parser will fail if this arg is not present.
 */
class CommandArg(
    val name: String,
    val aliases: Array<String>,
    val description: String = "",
    val returnsValue: Boolean,
    val isRequired: Boolean,
) {
    init {
        require(aliases.isNotEmpty() && name.isNotEmpty())
    }

    /**
     * Checks if the provided argument [arg] matches any of the defined aliases.
     * @return `true` if [arg] matches an alias, `false` otherwise.
     */
    fun matches(arg: String): Boolean = aliases.contains(arg)
}