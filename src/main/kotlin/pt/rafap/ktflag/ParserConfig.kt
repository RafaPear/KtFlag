package pt.rafap.ktflag

import pt.rafap.ktflag.cmd.CommandImpl
import pt.rafap.ktflag.style.Colors
import pt.rafap.ktflag.style.Colors.colorText

/**
 * Configuration for a [CommandParser].
 *
 * @param T Type of the context shared across commands.
 * @property prompt String printed before each input read. Defaults to a colored "> ".
 * @property helpCmd Optional custom help command. When null, a default help implementation is added.
 */
data class ParserConfig<T>(
    val prompt: String = colorText("> ", Colors.PURPLE),
    val helpCmd: CommandImpl<T>? = null,
)