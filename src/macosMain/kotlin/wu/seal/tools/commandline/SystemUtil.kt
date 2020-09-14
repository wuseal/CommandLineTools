package wu.seal.tools.commandline

import kotlinx.cinterop.*
import platform.posix.NULL
import platform.posix.fgets
import platform.posix.pclose
import platform.posix.popen
import sagiegurari.c_scriptexec.scriptexec_run

private val logger = Log("SystemUtil")

/**
 * 运行单个命令行，并返回运行结果
 * 运行命令成功返回运行成功时输出到控制台的标准输出的字符串
 * 运行命令失败，则返回错误信息
 */
fun executeCommand(command: String, vararg args: String): String {
    val commandLine = buildString {
        append(command)
        val argsString = args.joinToString(" ")
        if (argsString.isNotEmpty()) {
            append(" ")
            append(argsString)
        }
    }
    val pp = popen("$commandLine 2>&1", "r")
    if (pp == null) {
        logger.w("execute command failed $commandLine")
        return ""
    }

    val returnContent = buildString {
        pp.run {
            ByteArray(4096).usePinned {
                while (fgets(it.addressOf(0), 4096, this) != NULL) {
                    append(it.get().toKString())
                }
            }
        }
    }
    pclose(pp)

    return returnContent.trim()
}

/**
 * 这个方法可以执行一段脚本，可包含换行符,同样也可以运行一个指令,返回执行脚本的结果对象[ScriptExecuteResult]
 */
fun executeScript(script: String): ScriptExecuteResult {
    return memScoped {
        scriptexec_run(script).useContents {
            ScriptExecuteResult(
                code,
                out?.toKString()?.trim(),
                err?.toKString()?.trim()
            )
        }
    }
}

/**
 * 脚本执行的结果对象
 *
 * code:错误码，0表示执行成功
 *
 * output表示执行成功的标准输出字符串
 *
 * error:表示执行失败时的错误信息
 */
data class ScriptExecuteResult(val code: Int, val output: String?, val error: String?)


/**
 * 判断某个指令名称是否存在
 */
fun commandExist(command: String): Boolean {
    return commandNotExist(command).not()
}

/**
 * 判断某个指令名称是否不存在
 */
fun commandNotExist(command: String): Boolean {
    return executeCommand(command).contains("command not found")
}
