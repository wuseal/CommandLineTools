package wu.seal.tools.commandline

import com.github.ajalt.clikt.core.CliktCommand

class CommandLineImpl : CliktCommand() {
    override fun run() = Unit
}

class AsyncConfig : CliktCommand() {

    private val userDir = KDir(USER_HOME_DIR)
    private val localConfigDir = KDir(userDir, "seal/config")

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
        val dotGitDir = KDir(localConfigDir, ".git")
        if (dotGitDir.exist()) { //已经有本地仓库
            L.i("当前存在本地配置仓库，进入配置仓库目录")
            //合并本机环境配置覆盖到当前这个Git仓库
            mergeLocalConfig()
            //推送更新到远程配置仓库
            pushToRemote()
        } else {
            //本地还未有仓库，开始执行下拉配置，进行配置应用
            L.i("本地还未有仓库，开始执行下拉配置，进行配置应用")
            cloneRemoteToLocal()
            copyConfigToOS()
        }
    }

    private fun copyConfigToOS() {

    }

    private fun cloneRemoteToLocal() {
        executeCommand("git clone git@github.com:wuseal/OSConfigs.git ${localConfigDir.dirPath}")
    }

    private fun pushToRemote() {
        L.i("开始提交本地配置并推送到远程仓库")
        L.i(executeCommand("cd ${localConfigDir.dirPath} \n git add ."))
        L.i(executeCommand("cd ${localConfigDir.dirPath} \n git commit -m \"update local config to remote\""))
        L.i(executeCommand("cd ${localConfigDir.dirPath} \n git push"))
    }

    private fun mergeLocalConfig() {
        L.i("开始合并本地配置到本地仓库")
        mergeLocalSSHConfig()
        mereLocalVimConfig()
        mergeLocalGradleConfig()
        L.i("合并完成")
    }

    private fun mergeLocalGradleConfig() {
       L.i("收集本地的Gradle配置")
        val dotGradleDir = KDir(userDir,".gradle")
        val dotGradleLocalDir = KDir(localConfigDir,".gradle")
        val gradlePropertiesFile = KFile(dotGradleDir,"gradle.properties")
        val gradlePropertiesFileLocal = KFile(dotGradleLocalDir,"gradle.properties")
        gradlePropertiesFile.copyTo(gradlePropertiesFileLocal)
        KFile(dotGradleDir,"init.gradle").copyTo(KFile(dotGradleLocalDir,"init.gradle"))
        L.i("收集本地的Gradle配置完成")
    }

    private fun mereLocalVimConfig() {
        L.i("收集本地vim配置")
        val vimrcConfigFile = KFile(userDir, ".vimrc")
        val vimrcLocalFile = KFile(localConfigDir, ".vimrc")
        vimrcConfigFile.copyTo(vimrcLocalFile)

        val ideaVimrc = KFile(userDir, ".ideavimrc")
        val localIdeaVimrc = KFile(localConfigDir, ".ideavimrc")
        ideaVimrc.copyTo(localIdeaVimrc)

        L.i("收集本地vim配置完成")
    }

    private fun mergeLocalSSHConfig() {
        L.i("收集本地ssh配置")
        val sshConfigDir = KDir(localConfigDir, ".ssh")
        sshConfigDir.deleteRecursively()
        val localSshConfigDir = KDir(userDir, ".ssh")
        localSshConfigDir.copyTo(sshConfigDir)
        L.i("收集本地ssh配置完成")
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