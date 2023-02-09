package ru.baccasoft.zadachnik.logs

import android.content.Context
import android.os.Environment
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import ru.baccasoft.zadachnik.Utils
import timber.log.Timber
import timber.log.Timber.DebugTree
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*


class FileLoggingTree(
    @ApplicationContext private val context: Context
) : DebugTree() {

    private val simpleDateFormat = SimpleDateFormat("dd MM yyyy HH:mm:ss:SSS", Locale.getDefault())
    private val LOG_TAG = FileLoggingTree::class.java.simpleName

    private val isExternalStorageAvailable: Boolean
        get() = (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState())

    var file: File? = null

    init {
        val fileNameTimeStamp: String = SimpleDateFormat(
            "dd-MM-yyyy",
            Locale.getDefault()
        ).format(Date())
        val fileName = "$fileNameTimeStamp.txt"
        file = Utils().generateFile(context, "Log", fileName)
    }

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        runCatching {
            val logTimeStamp: String = simpleDateFormat.format(Date())
            if (file != null) {
                val writer = FileWriter(file, true)
                    .append(logTimeStamp)
                    .append(" : ")
                    .append(tag)
                    .append(" : ")
                    .append(message)
                    .append("\n")
                writer.flush()
                writer.close()
            }
        }.onFailure { throwable ->
            Timber.e(throwable)
        }
    }

    override fun createStackElementTag(element: StackTraceElement): String {
        // Add log statements line number to the log
        return super.createStackElementTag(element) + " - " + element.lineNumber
    }
}