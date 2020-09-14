package wu.seal.tools.commandline

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class SystemUtilTest {

    @Test
    fun testExecuteCommandFailed() {
        val result = executeCommand("l")
        assertEquals("sh: l: command not found", result)
    }

    @Test
    fun testExecuteCommandSuccessfully() {
        val result = executeCommand("echo", "hello")
        assertEquals("hello", result)
    }

    @Test
    fun testExecuteScriptSuccessfully() {
        val result = executeScript("echo 1\necho 2\necho 3\necho 4")
        assertEquals("1\n2\n3\n4", result.output)
    }

    @Test
    fun testExecuteScriptFailed() {
        val result = executeScript("l")
        assertNotEquals(0, result.code)
        val endsWithResult = result.error?.endsWith("script: line 3: l: command not found", true)
        assert(endsWithResult == true)
    }

    @Test
    fun testExecuteSingleCommandScriptSuccessfully() {
        val result = executeScript("echo 1")
        assertEquals("1", result.output)
    }

    @Test
    fun testCommandExist() {
        assert(commandExist("ls"))
        assert(commandExist("cd"))
        assert(commandExist("time"))
    }

    @Test
    fun testCommandNotExist() {
        assert(commandNotExist("l"))
        assert(commandNotExist("xxxxx"))
    }
}