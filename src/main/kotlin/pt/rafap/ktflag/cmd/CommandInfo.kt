package pt.rafap.ktflag.cmd

/**
 * Immutable metadata that describes a command.
 *
 * This includes a human-friendly [title], short [description], a detailed [longDescription],
 * a list of [aliases] used to invoke the command, usage syntax in [usage], and
 * the expected argument cardinality via [minArgs] and [maxArgs].
 *
 * Instances of this class are typically provided by command implementations via
 * [CommandImpl.info].
 *
 * @property title Display name of the command.
 * @property description Short description shown in listings.
 * @property longDescription Detailed description shown in command-specific help. Defaults to [description].
 * @property aliases All names (tokens) that can be used to invoke the command.
 * @property usage Text describing how to invoke the command and its arguments.
 * @property minArgs Minimum number of positional arguments accepted.
 * @property maxArgs Maximum number of positional arguments accepted.
 */
data class CommandInfo(
    val title: String,
    val description: String,
    val longDescription: String = description,
    val aliases: List<String>,
    val usage: String,
    val minArgs: Int,
    val maxArgs: Int
) {
    init {
        require(title.isNotEmpty()) { "Command title cannot be empty" }
        require(description.isNotEmpty()) { "Command description cannot be empty" }
        require(usage.isNotEmpty()) { "Command usage cannot be empty" }
        require(minArgs >= 0) { "Command minArgs cannot be negative" }
        require(maxArgs >= -1) { "Command maxArgs cannot be less than -1" }
        require(maxArgs == -1 || maxArgs >= minArgs) { "Command maxArgs must be -1 (unbounded) or >= minArgs" }
        require(aliases.isNotEmpty() && aliases.all { it.isNotEmpty() }) { "Command must have at least one non-empty alias" }
    }

    /**
     * Renders a compact human-readable representation of this command for listings.
     */
    override fun toString(): String {
        return "$title\n$description\nAliases: ${aliases.joinToString(", ")}\nUsage: $usage"
    }
}