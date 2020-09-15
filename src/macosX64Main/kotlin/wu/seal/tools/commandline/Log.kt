package wu.seal.tools.commandline

import com.github.ajalt.clikt.output.TermUi

val L = Log("Application")

class Log(private val logTag: String = "", private val logLevel: LogLevel = INFO) {

    fun i(msg: String, tag: String = logTag) {
        if (logLevel <= INFO) {
            if (tag.isNotBlank()) {
                TermUi.echo("$tag\t$msg")
            } else {
                println(msg)
            }
        }
    }

    fun w(msg: String, tag: String = logTag) {
        if (logLevel <= WARN) {
            if (tag.isNotBlank()) {
                TermUi.echo("$tag\t$msg")
            } else {
                TermUi.echo(msg)
            }
        }
    }

    fun e(msg: String, tag: String = logTag) {
        if (logLevel <= ERROR) {
            if (tag.isNotBlank()) {
                TermUi.echo("$tag\t$msg", err = true)
            } else {
                TermUi.echo(msg, err = true)
            }
        }
    }

    fun v(msg: String, tag: String = logTag) {
        if (logLevel <= VERBOSE) {
            if (tag.isNotBlank()) {
                TermUi.echo("$tag\t$msg")
            } else {
                TermUi.echo(msg)
            }
        }
    }
}

sealed class LogLevel(private val level: Int) : Comparable<LogLevel> {
    override fun compareTo(other: LogLevel): Int {
        return this.level - other.level
    }
}

object INFO : LogLevel(1)
object WARN : LogLevel(2)
object ERROR : LogLevel(3)
object VERBOSE : LogLevel(0)
