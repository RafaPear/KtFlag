package pt.rafap.ktflag

import pt.rafap.ktflag.cmd.*
import pt.rafap.ktflag.style.Colors

/**
 * Default implementation of a help command.
 *
 * Lists all available commands in the provided [register] or prints detailed
 * help for a specific command when a name is supplied.
 */
internal class ImplHelp<T>(val register: CommandRegister<T>, val config: ParserConfig<T> = ParserConfig()) : CommandImpl<T>() {
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
    override fun execute(vararg args: String, context: T?): CommandResult<T> {
        if (!this.verifyArgsCount(args.size))
            return CommandResult.INVALID_ARGS(info, args.size)

        printPreamble()
        return when {
            args.size <= info.minArgs -> printAll()
            else                      -> printCommand(args[0])
        }
    }

    /** Prints the headings for the help listing. */
    private fun printPreamble() {
        print(Colors.colorText("Format: ", Colors.BOLD))
        print(Colors.colorText("usage", config.helpUsageColor))
        print(" - ")
        print(Colors.colorText("aliases", config.helpAliasColor))
        print(" - ")
        println(Colors.colorText("description", config.helpDescColor))
        println()
    }

    /** Prints a one-line listing for each registered command. */
    private fun printAll(): CommandResult<T> {
        register.getAllCommands().forEach { cmd ->
            val info = cmd.info
            print(Colors.colorText(info.usage, config.helpUsageColor, Colors.BOLD))
            print(" - ")
            print(Colors.colorText(info.aliases.joinToString(), config.helpAliasColor))
            print(" - ")
            println(Colors.colorText(info.description, config.helpDescColor))
        }
        return CommandResult("Printed all commands", CommandResultType.SUCCESS)
    }

    /** Prints detailed help for the command with [name], or an error if not found. */
    private fun printCommand(name: String): CommandResult<T> {
        val command = register[name] ?: return CommandResult("Command '$name' not found", CommandResultType.ERROR)

        val cmdInfo = command.info

        print(Colors.colorText("Usage: ", Colors.BOLD, config.helpUsageColor))
        println(Colors.colorText(cmdInfo.usage, config.helpUsageColor, Colors.BOLD))

        print(Colors.colorText("Aliases: ", Colors.BOLD, config.helpAliasColor))
        println(Colors.colorText(cmdInfo.aliases.joinToString(), config.helpAliasColor))

        print(Colors.colorText("Description: ", Colors.BOLD, config.helpDescColor))
        println(Colors.colorText(cmdInfo.longDescription, config.helpDescColor))

        return CommandResult("Printed help for command '$name'", CommandResultType.SUCCESS)
    }
}