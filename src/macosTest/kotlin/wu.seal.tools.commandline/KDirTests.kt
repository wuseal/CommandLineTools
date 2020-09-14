package wu.seal.tools.commandline

import kotlin.test.Test
import kotlin.test.assertFalse

class KDirTests {
    @Test
    fun testListDirFiles() {
        val dir = "scriptexec"
        KDir(dir).run {
            val listFiles = listFiles()
            assert(listFiles.size == 1)
            listFiles[0].fileName == "scriptexec.def"
            val listDirs = listDirs()
            assert(listDirs.size == 2)
            assert(listDirs[0].dirName == "include")
            assert(listDirs[1].dirName == "lib")
            assert(dirName == dir)
        }
    }

    @Test
    fun testListDirDirs() {
        val dir = "scriptexec"
        KDir(dir).run {
            val listDirs = listDirs()
            assert(listDirs.size == 2)
            assert(listDirs[0].dirName == "include")
            assert(listDirs[1].dirName == "lib")
        }
    }

    @Test
    fun testDirName() {
        val dir = "scriptexec"
        KDir(dir).run {
            assert(dirName == dir)
        }
    }

    @Test
    fun testDirExist() {
        val dir = "scriptexec"
        assert(KDir(dir).exist())
    }

    @Test
    fun testDirNotExist() {
        val dir = "xxxx"
        assertFalse(KDir(dir).exist())
    }

    @Test
    fun testDirParent() {
        val dir = "gradle/wrapper"
        assert(KDir(dir).parent.dirPath == "gradle")
    }

    @Test
    fun testCreate() {
        val dir = "./wu/seal/go"
        KDir(dir).create()
        assert(KDir(dir).exist())
        KDir(dir).deleteRecursively()
        assert(KDir(dir).exist().not())
    }
}