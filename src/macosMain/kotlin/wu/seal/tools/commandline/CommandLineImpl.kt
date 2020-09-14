package wu.seal.tools.commandline

import com.github.ajalt.clikt.core.CliktCommand

class CommandLineImpl : CliktCommand() {
    override fun run() = Unit
}

class AsyncConfig : CliktCommand() {

    private val localConfigDir = KDir("~/seal/config")

    init {
        localConfigDir.create()
    }

    override fun run() {
        baseCheck()
        initLocalConfigRepo()
//        if (accessGithubAccount().not) {
//           return
//        }
//        syncConfig()
//        if (configChanged().not) {
//            return
//        }
//        applyConfig()
    }

    private fun initLocalConfigRepo() {
        val dotGitDir = KDir(".git")
        if (dotGitDir.exist()) {
            //已经有本地仓库
            //合并本机环境配置覆盖到当前这个Git仓库
            //推送更新到远程配置仓库
        } else {
            //本地还未有仓库，开始执行下拉配置，进行配置应用

        }
    }

    private fun baseCheck() {
        checkGitInstallState()
        checkBrewInstall()
    }

    private fun checkBrewInstall() {
        if (commandNotExist("brew")) {
            MacTools.installBrew()
        }
    }

    private fun checkGitInstallState() {
        if (commandNotExist("git")) {
            error("请安装git")
        }
    }
}