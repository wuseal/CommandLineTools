package wu.seal.tools.commandline

import kotlin.test.Test

class MacToolsTest {

    @Test
    fun testUninstallBrew() {
       MacTools.uninstallBrew().run {
           assert(code==0)
           println(output)
           println(error)
       }
    }
}