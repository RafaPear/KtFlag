package pt.rafap.ktflag.cmd.args

/**
 * Parses CLI flags/options for a command based on a declared set of [CommandArg].
 *
 * The parser walks the provided tokens left-to-right, matching them against
 * known argument [aliases][CommandArg.aliases]. For an argument marked as
 * [CommandArg.returnsValue], the next token is consumed as its value; otherwise
 * an empty string is stored as the value. Required arguments are enforced via
 * [CommandArg.isRequired].
 *
 * Error modes:
 * - Unknown argument token -> IllegalArgumentException
 * - Missing value for an argument that returns a value -> IllegalArgumentException
 * - One or more required arguments missing -> IllegalArgumentException
 */
class CommandArgsParser(private vararg val cmdArgs: CommandArg) {

    /**
     * Parses [args] and returns a map from [CommandArg] to its parsed value.
     *
     * For non-valued flags, the mapped value will be an empty string.
     * Each argument may appear at most once; later occurrences overwrite earlier ones.
     *
     * @throws IllegalArgumentException on unknown flags, missing values, or missing required flags.
     */
    fun parseArgs(vararg args: String): Map<CommandArg, String> {
        val argsMap = mutableMapOf<CommandArg, String>()

        val argsToKill = args.toMutableList()

        val gotRequired = mutableListOf<CommandArg>()
        val expectedRequired = cmdArgs.filter { it.isRequired }

        while (argsToKill.isNotEmpty()) {
            val arg = argsToKill.removeAt(0)
            val cmdArg = cmdArgs.find { it.matches(arg) }
            if (cmdArg != null) {
                if (cmdArg.isRequired) gotRequired.add(cmdArg)
                if (cmdArg.returnsValue) {
                    val value = argsToKill.getOrNull(0)
                        ?: throw IllegalArgumentException("Argument $arg requires a value")
                    argsToKill.removeAt(0)
                    argsMap[cmdArg] = value
                }
                else argsMap[cmdArg] = ""
            }
            else throw IllegalArgumentException("Unknown argument: $arg")
        }

        if (gotRequired.size != expectedRequired.size) {
            val missing = expectedRequired - gotRequired.toSet()
            throw IllegalArgumentException("Missing required arguments: ${missing.joinToString()}")
        }

        return argsMap
    }
}