package ru.baccasoft.zadachnik.icons.additions

import androidx.compose.ui.graphics.vector.ImageVector
import ru.baccasoft.zadachnik.icons.additions.myiconpack._24
import ru.baccasoft.zadachnik.icons.additions.myiconpack._32
import kotlin.collections.List as ____KtList

public object MyIconPack

private var __AllIcons: ____KtList<ImageVector>? = null

public val MyIconPack.AllIcons: ____KtList<ImageVector>
  get() {
    if (__AllIcons != null) {
      return __AllIcons!!
    }
    __AllIcons= listOf(_24, _32)
    return __AllIcons!!
  }
