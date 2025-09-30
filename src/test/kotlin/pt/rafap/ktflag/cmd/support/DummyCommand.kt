package pt.rafap.ktflag.cmd.support

import pt.isel.pt.rafap.ktflag.cmd.CommandImpl
import pt.isel.pt.rafap.ktflag.cmd.CommandInfo
import pt.isel.pt.rafap.ktflag.cmd.CommandResult

class DummyCommand<T>(
    override val info: CommandInfo,
    private val exec: (Array<out String>, T?) -> CommandResult<T> = { _, _ -> CommandResult("Executed", false) }
) : CommandImpl<T> {
    override fun execute(vararg arg: String, context: T?): CommandResult<T> = exec(arg, context)
}