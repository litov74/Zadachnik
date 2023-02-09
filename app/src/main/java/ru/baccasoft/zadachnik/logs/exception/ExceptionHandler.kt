package ru.baccasoft.zadachnik.logs.exception

import timber.log.Timber
import kotlin.system.exitProcess


class ExceptionHandler : Thread.UncaughtExceptionHandler {

    override fun uncaughtException(t: Thread, e: Throwable) {
        Timber.e(e)
        exitProcess(1) // If you don't kill the VM here the app goes into limbo
    }

}