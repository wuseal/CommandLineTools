package wu.seal.tools.commandline

import kotlinx.cinterop.*
import platform.posix.*

private const val TAG = "FileUtil"
private val logger = Log(TAG)

/**
 * 文件操作是存在文件指针的，读取和写都会移动文件指针,这里就不用文件指针操作偏移，直接开关文件进行重置
 */
class KFile(val filePath: String) {
    private var fileP: CPointer<FILE>? = null
    private val fileInfo: stat?
        get() {
            return memScoped {
                val fileInfo = alloc<stat>().ptr
                if (stat(filePath, fileInfo) != 0) {
                    logger.w("obtain file info failed, file = $filePath")
                    return@memScoped null
                } else {
                    return@memScoped fileInfo.pointed
                }
            }
        }

    val fileName: String by lazy {
        filePath.substringAfterLast("/", filePath)
    }

    fun readAllText(): String? {
        close()
        fileP = fopen(filePath, "r")
        if (fileP == null) {
            logger.w("Open file failed :$filePath")
            return null
        }
        val fileInfo = fileInfo ?: return null
        val fileSize = fileInfo.st_size
        if (fileSize == 0L) {
            return ""
        }
        return ByteArray(fileSize.convert()).apply {
            usePinned {
                fread(it.addressOf(0), fileSize.convert(), fileSize.convert(), fileP)
            }
        }.toKString()
    }

    fun writeText(content: String) {
        close()
        fileP = fopen(filePath, "w")
        if (fileP == null) {
            logger.w("Open file failed :$filePath")
            return
        }
        fprintf(fileP, content)
        close()
    }

    fun appendText(content: String) {
        close()
        fileP = fopen(filePath, "a")
        if (fileP == null) {
            logger.w("Open file failed :$filePath")
            return
        }
        fprintf(fileP, content)
        close()
    }

    /**
     * File length, file byte size
     */
    fun length(): Long {
        val fileInfo = fileInfo
        return fileInfo?.st_size ?: 0L
    }

    fun delete() {
        remove(filePath)
    }

    fun close() {
        fileP?.run { fclose(this) }
        fileP = null
    }

    fun exist(): Boolean = access(filePath, F_OK) == 0


    /**
     * 创建文件,如果父目录不存在，则会一起把父目录一起创建了
     * 创建成功返回true
     */
    fun create():Boolean {
        var createSuccess = false
        if (!parent.exist()) {
            parent.create()
        }
        fileP = fopen(filePath, "r")
        if (fileP == NULL) {
            fileP = fopen(fileName, "w");
            if (fileP != null) {
                createSuccess = true
            }
        }
        fclose(fileP)
        return createSuccess
    }

    val parent: KDir by lazy {
        val parentDirPath = filePath.substringBeforeLast("/", ".")
        KDir(parentDirPath)
    }
}

fun <R> useFile(filePath: String, fileOperation: KFile.() -> R): R? {
    val kFile = KFile(filePath)
    val result = kFile.fileOperation()
    kFile.close()
    return result
}

