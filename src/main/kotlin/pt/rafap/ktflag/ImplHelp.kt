package pt.rafap.ktflag

import pt.rafap.ktflag.cmd.CommandImpl
import pt.rafap.ktflag.cmd.CommandInfo
import pt.rafap.ktflag.cmd.CommandRegister
import pt.rafap.ktflag.cmd.CommandResult
import pt.rafap.ktflag.cmd.CommandResult.INVALID_ARGS
import pt.rafap.ktflag.cmd.CommandResultType
import pt.rafap.ktflag.style.Colors

/**
 * Default implementation of a help command.
 *
 * Lists all available commands in the provided [register] or prints detailed
 * help for a specific command when a name is supplied.
 */
internal class ImplHelp<T>(val register: CommandRegister<T>) : CommandImpl<T>() {
    /** Metadata describing the help command. */
    override val info =
        CommandInfo(
            title = "Help",
            description = "Displays help information about available commands.",
            aliases = listOf("h", "help", "?"),
            usage = "help [<command>]",
            minArgs = 0,
            maxArgs = 1
        )

    /**
     * Executes the help command.
     * - With no args, prints a compact list of all commands.
     * - With one arg, prints detailed help for that command name.
     */
    override fun execute(vararg arg: String, context: T?): CommandResult<T> {
        if (!this.verifyArgsCount(arg.size))
            return INVALID_ARGS(info, arg.size)

        printPreamble()
        return when {
            arg.size <= info.minArgs -> printAll()
            else                     -> printCommand(arg[0])
        }
    }

    /** Prints the headings for the help listing. */
    private fun printPreamble() {
        print(Colors.colorText("Format: ", Colors.BOLD))
        print(Colors.colorText("usage", Colors.HELP_USAGE_COLOR))
        print(" - ")
        print(Colors.colorText("aliases", Colors.HELP_ALIAS_COLOR))
        print(" - ")
        println(Colors.colorText("description", Colors.HELP_DESC_COLOR))
        println()
    }

    /** Prints a one-line listing for each registered command. */
    private fun printAll(): CommandResult<T> {
        register.getAllCommands().forEach { cmd ->
            val info = cmd.info
            print(Colors.colorText(info.usage, Colors.HELP_USAGE_COLOR, Colors.BOLD))
            print(" - ")
            print(Colors.colorText(info.aliases.joinToString(), Colors.HELP_ALIAS_COLOR))
            print(" - ")
            println(Colors.colorText(info.description, Colors.HELP_DESC_COLOR))
        }
        return CommandResult("Printed all commands", CommandResultType.SUCCESS)
    }

    /** Prints detailed help for the command with [name], or an error if not found. */
    private fun printCommand(name: String): CommandResult<T> {
        val command = register[name] ?: return CommandResult("Command '$name' not found", CommandResultType.ERROR)

        val cmdInfo = command.info

        print(Colors.colorText("Usage: ", Colors.BOLD, Colors.HELP_USAGE_COLOR))
        println(Colors.colorText(cmdInfo.usage, Colors.HELP_USAGE_COLOR, Colors.BOLD))

        print(Colors.colorText("Aliases: ", Colors.BOLD, Colors.HELP_ALIAS_COLOR))
        println(Colors.colorText(cmdInfo.aliases.joinToString(), Colors.HELP_ALIAS_COLOR))

        print(Colors.colorText("Description: ", Colors.BOLD, Colors.HELP_DESC_COLOR))
        println(Colors.colorText(cmdInfo.longDescription, Colors.HELP_DESC_COLOR))

        return CommandResult("Printed help for command '$name'", CommandResultType.SUCCESS)
    }
}