package wu.seal.tools.commandline

object MacTools {
    /**
     * 安装HomeBrew
     */
    fun installBrew() =
        executeScript(
            """/bin/bash -c "${'$'}(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install.sh)"
"""
        )

    fun uninstallBrew() = executeScript(
        """/bin/bash -c "${'$'}(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/uninstall.sh)"
"""
    )
}