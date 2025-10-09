package pt.rafap.ktflag.cmd.support

import pt.rafap.ktflag.cmd.CommandImpl
import pt.rafap.ktflag.cmd.CommandInfo
import pt.rafap.ktflag.cmd.CommandResult
import pt.rafap.ktflag.cmd.CommandResultType

class DummyCommand<T>(
    override val info: CommandInfo,
    private val exec: (Array<out String>, T?) -> CommandResult<T> = { _, _ -> CommandResult("Executed", CommandResultType.SUCCESS) }
) : CommandImpl<T>() {
    override fun execute(vararg args: String, context: T?): CommandResult<T> = exec(args, context)
}