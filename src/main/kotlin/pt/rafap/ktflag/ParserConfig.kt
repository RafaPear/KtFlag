package pt.rafap.ktflag

import pt.rafap.ktflag.cmd.CommandImpl
import pt.rafap.ktflag.style.Colors
import pt.rafap.ktflag.style.Colors.colorText

data class ParserConfig<T>(
    val prompt: String = colorText("> ", Colors.PURPLE),
    val helpCmd: CommandImpl<T>? = null,
)