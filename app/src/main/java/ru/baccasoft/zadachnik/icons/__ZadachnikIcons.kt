package ru.baccasoft.zadachnik.icons

import androidx.compose.ui.graphics.vector.ImageVector
import ru.baccasoft.zadachnik.icons.zadachnikicons.*
import kotlin.collections.List as ____KtList

public object ZadachnikIcons

private var __AllIcons: ____KtList<ImageVector>? = null

public val ZadachnikIcons.AllIcons: ____KtList<ImageVector>
    get() {
        if (__AllIcons != null) {
            return __AllIcons!!
        }
        __AllIcons = listOf(
            Archive, Cross, DatailsSendArrow, DatailsSunFull, DetailsAddFiles,
            DetailsAudio, DetailsCalendar, DetailsClock, DetailsDoc, DetailsMic, DetailsSunHalf,
            DetailsUser, DetailsWeek, HintsDown, HintsLamp, MenuAddTask, MenuArchive, MenuTasks,
            SearchLight, Unarchive
        )
        return __AllIcons!!
    }
