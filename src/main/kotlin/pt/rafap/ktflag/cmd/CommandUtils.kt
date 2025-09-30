package pt.rafap.ktflag.cmd

import pt.rafap.ktflag.style.Colors

/**
 * Utility helpers for command implementations.
 */
object CommandUtils {
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
     * Prints a standardized message for an unknown command [name], hinting the
     * user to consult the provided help command [cmdHelp].
     */
    fun printUnknownCommand(name: String, cmdHelp: CommandImpl<*>) {
        println(Colors.colorText("Unknown command: $name", Colors.ERROR_COLOR))
        print(Colors.colorText("Use", Colors.INFO_COLOR))
        print(Colors.colorText(" ${cmdHelp.info.aliases} ", Colors.HELP_ALIAS_COLOR))
        println(Colors.colorText("to see the list of available commands.", Colors.INFO_COLOR))
    }
}