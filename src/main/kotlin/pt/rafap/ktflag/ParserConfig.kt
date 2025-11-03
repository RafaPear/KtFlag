package pt.rafap.ktflag

import pt.rafap.ktflag.cmd.CommandImpl
import pt.rafap.ktflag.style.Colors.BLUE
import pt.rafap.ktflag.style.Colors.CYAN
import pt.rafap.ktflag.style.Colors.GREEN
import pt.rafap.ktflag.style.Colors.PURPLE
import pt.rafap.ktflag.style.Colors.RED
import pt.rafap.ktflag.style.Colors.YELLOW

/**
 * Configuration for a [CommandParser].
 *
 * @param T Type of the context shared across commands.
 * @property prompt String printed before each input read. Defaults to a colored "> ".
 * @property helpCmd Optional custom help command. When null, a default help implementation is added.
 */
data class ParserConfig<T>(
    val prompt: String = "> ",
    val promptColor: String = PURPLE,
    val errorColor: String = RED,
    val warningColor: String = GREEN,
    val infoColor: String = GREEN,
    val helpUsageColor: String = CYAN,
    val helpAliasColor: String = BLUE,
    val helpDescColor: String = YELLOW,
    val helpCmd: CommandImpl<T>? = null,
)