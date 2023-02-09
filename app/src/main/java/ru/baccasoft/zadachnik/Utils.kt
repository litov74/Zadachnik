package ru.baccasoft.zadachnik

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.provider.CalendarContract.CalendarAlerts
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import org.joda.time.DateTime
import ru.baccasoft.zadachnik.data.network.models.SelectedDateModel
import ru.baccasoft.zadachnik.data.network.models.SelectedTimeModel
import ru.baccasoft.zadachnik.data.prefs.PrefsHelper
import timber.log.Timber
import java.io.File
import java.text.SimpleDateFormat
import java.time.YearMonth
import java.util.*

class Utils {

    fun monthToName(month: String): String {
        return when (month) {
            "01" -> "янв"
            "02" -> "фев"
            "03" -> "мар"
            "04" -> "апр"
            "05" -> "май"
            "06" -> "июн"
            "07" -> "июл"
            "08" -> "авг"
            "09" -> "сен"
            "10" -> "окт"
            "11" -> "ноя"
            "12" -> "дек"
            else -> ""
        }
    }

    fun monthToFullName(month: String): String {
        return when (month) {
            "01" -> "января"
            "02" -> "февраля"
            "03" -> "марта"
            "04" -> "апреля"
            "05" -> "мая"
            "06" -> "июня"
            "07" -> "июля"
            "08" -> "августа"
            "09" -> "сентября"
            "10" -> "октября"
            "11" -> "ноября"
            "12" -> "декабря"
            else -> ""
        }
    }

    fun nameToLetters(name: String): String {
        val out: String =
            try {
                if (name.split(" ").size == 1) {
                    name.split(" ").first()[0].toString()
                } else {
                    name.split(" ").first()[0].toString() + name.split(" ").last()[0].toString()
                }
            } catch (e: java.lang.Exception) {
                ""
            }
        return out
    }

    fun checkDate(date: String): Boolean {
        try {
            val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            val mDate = formatter.parse(date)
            if (Date().after(mDate)) {
                return true
            }
        } catch (e: Exception) {
            return false
        }
        return false

    }

    fun compareDates(dateNoTime: String): Boolean {
        val sdf = SimpleDateFormat("yyyy-M-dd")
        val currentDate = sdf.format(Date())
        val modifiedDate = dateNoTime.split("T").first()
        //true - when is equal or bigger
        //false - other
        return when {
            currentDate.compareTo(modifiedDate) <= 0 -> {
                false
            }
            else -> {
                true
            }
        }
    }

    fun hasContactPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) ==
                PackageManager.PERMISSION_GRANTED
    }

    fun requestPermissionReadContacts(context: Context, activity: Activity) {
        if (!hasContactPermission(context)) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.READ_CONTACTS),
                1
            )
        }
    }

    fun Context.getActivity(): AppCompatActivity? = when (this) {
        is AppCompatActivity -> this
        is ContextWrapper -> baseContext.getActivity()
        else -> null
    }

    fun processDate(date: String): ArrayList<String> {

        var out = ArrayList<String>()

        val dateNoMs = date.takeWhile {
            it.toString() != "."
        }
        val dateNoTime = dateNoMs.split("T").first()
        val timeNoDate = dateNoMs.split("T").last()

        val year = dateNoTime.split("-").first()
        val month = dateNoTime.split("-")[1]
        val day = dateNoTime.split("-").last()

        val hour = timeNoDate.split(":").first()
        val minute = timeNoDate.split(":")[1]
        val second = timeNoDate.split(":").last()

        out.add(dateNoTime) //0
        out.add(timeNoDate) //1
        out.add(year) //2
        out.add(month) //3
        out.add(day) //4
        out.add(hour) //5
        out.add(minute) //6
        out.add(second) //7

        return out

    }

    fun generateDate(
        date: SelectedDateModel,
        time: SelectedTimeModel,
        timeSelector: Int = 4,
        forReminder: Boolean,
        forPreview: Boolean = false
    ) : String {
        var out = ""
        Log.d("Utils time", timeSelector.toString())
        Log.d("Utils forReminder", forReminder.toString())
        // region preparations
        val initialDate = "${date.year}-${date.month}-${date.day}T${time.hour}:${time.minute}:${time.second}"
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val processedDate = formatter.parse(initialDate)
        processedDate.time = processedDate.time - TimeZone.getDefault().rawOffset
        var dt = DateTime(processedDate)
        // endregion
        if(forReminder) {
            when(timeSelector) {
                0 -> {
                    dt = dt.minusMinutes(15)
                    Log.d("Utils minusMinutes15", dt.toString())
                }
                1 -> {
                    dt = dt.minusHours(1)
                    Log.d("Utils minusHours(1)", dt.toString())
                }
                2 -> {
                    dt = dt.minusHours(4)
                    Log.d("Utils minusHours(4)", dt.toString())
                }
                3 -> {
                    dt = dt.minusDays(1)
                    Log.d("Utils minusDays(1)", dt.toString())
                }
            }
        }


        if(forPreview) {
            out = dt.toString().takeWhile {
                it != "T".toCharArray()[0]
            }.replace("-", ".")
        } else {
            val readyDate = processDate(dt.toString())
            out = "${readyDate[2]}-${readyDate[3]}-${readyDate[4]}T${readyDate[5]}:${readyDate[6]}:${readyDate[7]}"
            Log.d("Utils out", out)
        }
        return out
    }

    fun generateCurrentDateAndTime() : String {
        var out = ""
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
        out += sdf.format(Date())
        Timber.tag("generateCurrentDateAndTime").d(out)
        return out
    }

    fun generateFile(context: Context, path: String, fileName: String): File? {
        var file: File? = null
        val root = File(
            context.getExternalFilesDir(null),
            path
        )
        var dirExists = true
        if (!root.exists()) {
            dirExists = root.mkdirs()
        }
        if (dirExists) {
            file = File(root, fileName)
        }
        return file
    }

    fun getContactByName(){

    }

}

