package com.santansarah.scan.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.unit.sp
import com.santansarah.scan.R

val monoFamily = FontFamily(
    Font(resId = R.font.space_mono)
)

val titleFamily = FontFamily(
    Font(resId = R.font.anton)
)

val appBarTitle = TextStyle(
    fontFamily = titleFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 24.sp,
    baselineShift = BaselineShift(.23f),
    letterSpacing = .3.sp
)

val pagerHeaders = TextStyle(
    fontFamily = titleFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 20.sp,
    baselineShift = BaselineShift(.23f),
    letterSpacing = .3.sp
)

val codeFont = TextStyle(
    fontFamily = monoFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 10.sp
)

val bodySmallItalic =
    TextStyle(
        fontStyle = FontStyle.Italic,
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp
    )

val labelSmallItalic =
    TextStyle(
        fontStyle = FontStyle.Italic,
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)