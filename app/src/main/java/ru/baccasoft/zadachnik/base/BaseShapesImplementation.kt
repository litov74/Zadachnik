package ru.baccasoft.zadachnik.base

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.baccasoft.zadachnik.R
import ru.baccasoft.zadachnik.Utils
import ru.baccasoft.zadachnik.data.network.models.PhoneCodesModel
import ru.baccasoft.zadachnik.data.prefs.PrefsHelper
import ru.baccasoft.zadachnik.features.mainScreen.TaskModel
import ru.baccasoft.zadachnik.icons.ZadachnikIcons
import ru.baccasoft.zadachnik.icons.zadachnikicons.Cross
import ru.baccasoft.zadachnik.icons.zadachnikicons.DetailsDoc
import ru.baccasoft.zadachnik.ui.theme.commonFont
import ru.baccasoft.zadachnik.ui.theme.thinFont
import java.text.CharacterIterator
import java.text.SimpleDateFormat
import java.text.StringCharacterIterator
import java.time.Instant
import java.util.*


@Composable
fun RoundedCornersSquareButton(
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = RoundedCornerShape(50),
    isEnabled: MutableState<Boolean>,
    onClick: () -> Unit,
    content: (@Composable() () -> Unit)? = null
) {
    val redColor = colorResource(id = R.color.colorPrimary).toArgb()
    val greyColor = colorResource(id = R.color.lighter_grey).toArgb()
    ExtendedFloatingActionButton(
        onClick = { if(isEnabled.value) onClick() },
        modifier = modifier
            .padding(start = 16.dp, end = 16.dp)
            .height(56.dp)
            .background(color = Color(redColor), shape = shape),
        shape = shape,
        text = content!!,
        backgroundColor = if(isEnabled.value) Color(redColor) else Color(greyColor),

    )
}


// дикое порево с композом и лямбдами. использовать аккуратно, экспериментальная штука!!!
// если не вводится число, а курсор мигает, см. как сделано в LoginScreen.kt
@Composable
fun OtpCell(
    modifier: Modifier,
    value: String,
    isCursorVisible: Boolean = false
) {
    val scope = rememberCoroutineScope()
    val (cursorSymbol, setCursorSymbol) = remember { mutableStateOf("") }

    LaunchedEffect(key1 = cursorSymbol, isCursorVisible) {
        if (isCursorVisible) {
            scope.launch {
                delay(350)
                setCursorSymbol(if (cursorSymbol.isEmpty()) "" else "")
            }
        }
    }

    Box(
        modifier = modifier
    ) {
        Text(
            text = if (isCursorVisible) cursorSymbol else value,
            style = MaterialTheme.typography.body1,
            modifier = Modifier
                .align(Alignment.Center)
        )
    }
}

@ExperimentalComposeUiApi
@Composable
fun PinInput(
    modifier: Modifier = Modifier,
    length: Int = 4,
    value: String = "",
    onValueChanged: (String) -> Unit,
    _focusRequester: FocusRequester
) {
    val focusRequester = remember { FocusRequester() }
    val interactionSource = remember { MutableInteractionSource() }
    val keyboard = LocalSoftwareKeyboardController.current
    TextField(
        value = value,
        onValueChange = {
            if (it.length <= length) {
                if (it.all { c -> c in '0'..'9' }) {
                    onValueChanged(it)
                }
                /*if (it.length >= length) {
                    keyboard?.hide()
                }*/
            }
        },
        textStyle = TextStyle(
            fontFamily = thinFont,
            fontSize = 1.sp,
            color = Color.Black.copy(0.0f)
        ),
        // Hide the text field
        modifier = Modifier
            .size(0.dp)
            .focusRequester(_focusRequester),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
        )
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(length) {
            OtpCell(
                modifier = modifier
                    .size(width = 10.dp, height = 10.dp)
                    .clip(MaterialTheme.shapes.large)
                    .background(
                        color =
                        if (value.length > it) {
                            colorResource(id = R.color.lighter_black)
                        } else {
                            colorResource(id = R.color.light_grey)
                        },
                        //colorResource(id = R.color.light_grey),
                        shape = RoundedCornerShape(5.dp)
                    )
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        _focusRequester.requestFocus()
                        keyboard?.show()
                    },
                value = "",
                isCursorVisible = value.length == it
            )
            Spacer(modifier = Modifier.size(8.dp))
        }
    }
}

@Composable
fun TaskCell(
    modifier: Modifier,
    height: Dp = 20.dp,
    task: TaskModel,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val utils = Utils()
    val name = if (task.responsibleId == task.creatorId) {
        ""
    } else {
        utils.nameToLetters(task.responsibleFullName!!)
    }
    val dateNoTime = task.deadline!!.split("T").first()
    val date = dateNoTime.split("-").last() + " " + utils.monthToName(dateNoTime.split("-")[1])
    val currentTime = Date.from(Instant.now()).time
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    val deadline = sdf.parse(task.deadline).time + TimeZone.getDefault().rawOffset

    val isOverdue = currentTime > deadline
    Spacer(modifier = Modifier.width(10.dp))
    Row(modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically) {
        Text(
            task.title!!,
            fontSize = 16.sp,
            fontFamily = commonFont,
            maxLines = 2,
            modifier = Modifier
                .fillMaxWidth(0.75f)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) { onClick() }
        )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    date, fontFamily = commonFont, color = if (!isOverdue) {
                        colorResource(id = R.color.darker_gray)
                    } else {
                        colorResource(id = R.color.colorPrimary)
                    }
                )
                Spacer(modifier = Modifier.width(10.dp))
                Box(contentAlignment = Alignment.BottomEnd) {
                    Box(
                        modifier = Modifier
                            .background(color = Color.LightGray, shape = RoundedCornerShape(25))
                            .width(24.dp)
                            .height(24.dp)
                            .wrapContentSize()
                            .padding(2.dp)
                    ) {
                        Text(
                            text = name,
                            fontFamily = commonFont,
                            fontSize = 10.sp
                        )
                    }
                    if (task.isCommented!!) {
                        Box(
                            modifier = Modifier
                                .height(8.dp)
                                .width(8.dp)
                                .background(
                                    color = Color.White,
                                    shape = RoundedCornerShape(4.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .height(4.dp)
                                    .width(4.dp)
                                    .background(
                                        color = colorResource(
                                            id = R.color.colorPrimary,
                                        ),
                                        shape = RoundedCornerShape(2.dp)
                                    )
                            ) {

                            }
                        }
                    }
                }

            }
    }
}

@Composable
fun CodesCell(
    codeModel: PhoneCodesModel,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val painter: Painter
    val res = R.drawable::class.java
    val field = res.getField(codeModel.code.toLowerCase())
    val drawableId = field.getInt(null)
    painter = painterResource(id = drawableId)
    val interactionSource = remember { MutableInteractionSource() }
    Row(modifier = modifier
        .fillMaxWidth(0.9f)
        .height(50.dp)
        .clickable(
            interactionSource = interactionSource,
            indication = null
        ) { onClick() },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth(0.7f)) {
            Image(
                painter = painter, "", Modifier
                    .height(24.dp)
                    .width(24.dp)
            )
            Spacer(modifier = Modifier.width(20.dp))
            Text(text = codeModel.name, fontFamily = commonFont, maxLines = 2)
        }

        Text(
            text = "+" + codeModel.dialCode,
            fontFamily = commonFont,
            color = colorResource(id = R.color.darker_gray),
            maxLines = 1
        )

    }
    Divider(Modifier.fillMaxWidth(0.9f))
}

/**
 * @param modifier
 * is used to modify Row(), not inner elements
 * @param paddingLeadingIconStart
 * to adjust left icon (Zoom icon or other)
 *
 *
 */
@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    paddingLeadingIconStart: Dp = 0.dp,
    paddingLeadingIconEnd: Dp = 0.dp,
    leadingIcon: (@Composable() () -> Unit)? = null,
    trailingIcon: (@Composable() () -> Unit)? = null,
) {
    var state by remember {
        mutableStateOf("")
    }

    val interactionSource = remember { MutableInteractionSource() }

    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        if (leadingIcon != null) {
            leadingIcon()
        }
        Spacer(modifier = Modifier.width(10.dp))
        Box(
            modifier = modifier
                .weight(1f)
                .padding(start = paddingLeadingIconEnd, end = paddingLeadingIconStart),
            contentAlignment = Alignment.CenterStart
        ) {
            BasicTextField(
                value = state, onValueChange = { state = it }, modifier = Modifier
                    .height(36.dp)
                    .padding(top = 7.dp, bottom = 3.dp),
                textStyle = LocalTextStyle.current.copy(
                    textAlign = TextAlign.Start,
                    fontFamily = commonFont,
                    fontSize = 16.sp
                ),
                cursorBrush = Brush.verticalGradient(colors = listOf(
                    colorResource(id = R.color.colorPrimary), colorResource(id = R.color.colorPrimary)))
            )
            if (state.isEmpty()) Text("Поиск", fontFamily = thinFont, fontSize = 16.sp)
        }
        if (trailingIcon != null && state != "" && state != "Поиск") {
            Image(imageVector = ZadachnikIcons.Cross,
                contentDescription = "delete",
                modifier = Modifier
                    .height(24.dp)
                    .width(24.dp)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) { state = "" })
        }
    }
}

@Composable
fun NavDrawerItem(
    modifier: Modifier = Modifier,
    text: String = "item",
    icon: (@Composable() () -> Unit),
    isRed: Boolean = false
) {

    val blackColor = colorResource(id = R.color.black)
    val redColor = colorResource(id = R.color.colorPrimary)

    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        icon()
        Spacer(modifier = Modifier.width(22.dp))
        Text(
            text = text, fontFamily = commonFont, color = if (isRed) {
                redColor
            } else {
                blackColor
            }
        )
    }
}

fun humanReadableByteCountSI(bytes: Long): String {
    var bytes = bytes
    if (-1000 < bytes && bytes < 1000) {
        return "$bytes B"
    }
    val ci: CharacterIterator = StringCharacterIterator("кМГТПЕ")
    while (bytes <= -999950 || bytes >= 999950) {
        bytes /= 1000
        ci.next()
    }
    return java.lang.String.format("%.1f %cБ", bytes / 1000.0, ci.current())
}

@Composable
fun NestedFileItem(
    image: ImageVector,
    title: String,
    size: String,
    onFileClick: () -> Unit,
    currentSelTask: MutableState<String>,
    progress: MutableState<Float>
) {

    val interactionSource = remember { MutableInteractionSource() }
    LaunchedEffect(progress.value) {
        if(progress.value == 1.0f) {
            currentSelTask.value = ""
        }
    }
    Box(
        modifier = Modifier
            .wrapContentSize()
            .width(80.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            Modifier
                .wrapContentSize()
                .width(80.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box{
                Image(
                    imageVector = image, contentDescription = "", Modifier
                        .height(45.dp)
                        .width(35.dp)
                        .align(Alignment.Center)
                        .clickable(interactionSource = interactionSource, indication = null) {
                            onFileClick()
                        }
                )
                if(progress.value != 0.0f && currentSelTask.value == title.toString()+size.toString()) {
                    CircularProgressIndicator(color = colorResource(id = R.color.colorPrimary), modifier = Modifier
                        .size(30.dp, 30.dp)
                        .align(
                            Alignment.Center
                        ), progress = progress.value)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = title, fontFamily = commonFont, fontSize = 10.sp, modifier = Modifier.sizeIn(minWidth = 5.dp, maxWidth = 50.dp), maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(text = humanReadableByteCountSI(size.toLong()), fontFamily = commonFont, fontSize = 10.sp, modifier = Modifier.sizeIn(minWidth = 5.dp, maxWidth = 50.dp), maxLines = 1, overflow = TextOverflow.Ellipsis, color = colorResource(
                id = R.color.darker_gray
            ))
        }


    }
}

@Composable
@Preview
fun showNestedFile(){
    val progress = remember {
        mutableStateOf(0.9f)
    }
    val sel = remember {
        mutableStateOf("0.9f")
    }
    NestedFileItem(
        image = ZadachnikIcons.DetailsDoc,
        title = "тестовый файл",
        size = "10987612",
        onFileClick = { /*TODO*/ }, progress = progress, currentSelTask = sel)
}

@Composable
fun CustomSwitch(
    width: Dp = 60.dp,
    height: Dp = 32.dp,
    uncheckedTrackColor: Color = Color(0xFFFFFFFF),
    gapBetweenThumbAndTrackEdge: Dp = 6.dp,
    iconInnerPadding: Dp = 8.dp,
    thumbSize: Dp = 24.dp,
    ctx: Context,
    initialValue: MutableState<Boolean>,
    onClick: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {

    val redColor = Color(colorResource(id = R.color.colorPrimary).toArgb())
    val prefs = PrefsHelper(ctx)
    val lightGreyColor = Color(colorResource(id = R.color.light_grey).toArgb())

    val interactionSource = remember {
        MutableInteractionSource()
    }

    val alignment by animateAlignmentAsState(if (initialValue.value) 1f else -1f)

    // outer rectangle with border
    Box(
        modifier = Modifier
            .size(width = width, height = height)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                onClick(!initialValue.value)
            },
        contentAlignment = Alignment.Center
    ) {

        Box(
            modifier = Modifier
                .padding(
                    start = gapBetweenThumbAndTrackEdge,
                    end = gapBetweenThumbAndTrackEdge
                )
                .background(
                    color = if (initialValue.value) redColor else lightGreyColor,
                    shape = CircleShape
                )
                .fillMaxSize(),
            contentAlignment = alignment
        ) {

            // thumb with icon
            Box(
                modifier = Modifier
                    .size(size = thumbSize)
                    .background(
                        color = if (initialValue.value) uncheckedTrackColor else uncheckedTrackColor,
                        shape = CircleShape
                    )
                    .padding(all = iconInnerPadding)
            )
        }
    }
}


@Composable
fun RoundedCornerContainer(
    image: ImageVector? = null,
    content: String,
    modifier: Modifier,
    isWhite: Boolean = false,

    ) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (image != null) {
                Image(
                    imageVector = image, contentDescription = "", Modifier
                        .height(24.dp)
                        .width(24.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
            }
            if (isWhite) {
                Text(text = content, fontFamily = commonFont, fontSize = 13.sp, color = Color.White)
            } else {
                Text(text = content, fontFamily = commonFont, fontSize = 13.sp)
            }

        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
private fun animateAlignmentAsState(
    targetBiasValue: Float
): State<BiasAlignment> {
    val bias by animateFloatAsState(targetBiasValue)
    return derivedStateOf { BiasAlignment(horizontalBias = bias, verticalBias = 0f) }
}