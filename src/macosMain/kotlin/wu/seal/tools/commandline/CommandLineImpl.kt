package wu.seal.tools.commandline

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.prompt

class CommandLineImpl:CliktCommand() {
    val action:String by option(help = "行为名称，表示要执行什么动作").prompt("请输入要执行的动作")
    override fun run() {
        if (action == "hello") {
            println(hello())
        }
    }

    fun hello(): String = "Hello, Kotlin/Native!"

}