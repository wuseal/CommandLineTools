package wu.seal.tools.commandline

import kotlinx.cinterop.convert
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals

class FileUtilTest {

    private val filePath = "./temp.txt"

    @Test
    fun testWriteAndReadFile() {
        val text = "hello, this is the text,\n this is the second line text"
        useFile(filePath) {
            writeText(text)
            assert(text == readAllText())
        }
    }

    @Test
    fun testWriteFile() {
        val text = "fjlsdjfakjd;lsafjljl3249020,.m.2,m4j3lk24032"
        useFile(filePath) {
            writeText(text)
            assertEquals(text.encodeToByteArray().size.convert(), length())
        }
    }

    @AfterTest
    fun clean() {
        useFile(filePath) { delete() }
    }

}