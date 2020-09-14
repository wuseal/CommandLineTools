package wu.seal.tools.commandline

import kotlinx.cinterop.convert
import kotlinx.cinterop.pointed
import kotlinx.cinterop.toKString
import platform.posix.*

class KDir(val dirPath: String) {
    fun listFiles(): List<KFile> {
        val dir = opendir(dirPath)
        dir ?: return listOf()
        var mDirent: dirent? = readdir(dir)?.pointed

        val kFiles = mutableListOf<KFile>()
        val cleanDirPath = if (dirPath.endsWith("/")) dirPath.dropLast(1) else dirPath
        while (mDirent != null) {
            //判断对象类型是文件的时候添加到文件列表里
            if (mDirent.d_type.toInt() == DT_REG) {
                val fileName = mDirent.d_name.toKString()
                val filePath = "$cleanDirPath/$fileName"
                kFiles.add(KFile(filePath))
            }
            mDirent = readdir(dir)?.pointed
        }
        return kFiles
    }

    fun listDirs(): List<KDir> {
        val dir = opendir(dirPath)
        dir ?: return listOf()
        var mDirent: dirent? = readdir(dir)?.pointed

        val kDirs = mutableListOf<KDir>()
        val cleanDirPath = if (dirPath.endsWith("/")) dirPath.dropLast(1) else dirPath
        while (mDirent != null) {
            if (mDirent.d_type.toInt() == DT_DIR) {
                val dirName = mDirent.d_name.toKString()
                if (dirName == "." || dirName == "..") {
                    //如果是这种目录，直接略过吧，应该用不上
                } else {
                    val dirPath = "$cleanDirPath/$dirName"
                    kDirs.add(KDir(dirPath))
                }
            }
            mDirent = readdir(dir)?.pointed
        }
        return kDirs
    }

    val dirName: String by lazy {
        val cleanDirPath = if (dirPath.endsWith("/")) dirPath.dropLast(1) else dirPath
        cleanDirPath.substringAfterLast("/", cleanDirPath)
    }

    fun exist(): Boolean = access(dirPath, F_OK) == 0


    /**
     * 创建目录，如果父目录不存在，则会把父目录一起创建了
     * 创建成功返回true
     */
    fun create(): Boolean {
        //如果父目录不存在，则创建父目录
        if (parent.exist().not()) {
            parent.create()
        }
        if (exist().not()) {
            return mkdir(dirPath, 511.convert()) == 0
        }
        return false
    }

    val parent: KDir by lazy {
        val cleanDirPath = if (dirPath.endsWith("/")) dirPath.dropLast(1) else dirPath
        val parentDirPath = cleanDirPath.substringBeforeLast("/", "..")
        KDir(parentDirPath)
    }

    /**
     * 删除空文件夹
     * 删除成功返回true
     */
    fun delete() = rmdir(dirPath) == 0


    /**
     *递归删除文件夹及文件夹下的所有内容
     * 删除成功返回true
     */
    fun deleteRecursively(): Boolean {
        listDirs().forEach { it.deleteRecursively() }
        listFiles().forEach { it.delete() }
        return delete()
    }
}