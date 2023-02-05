package com.santansarah.blescanner.presentation.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AppBarWithBackButton(
    title: String,
    onBackClicked: () -> Unit
) {

    CenterAlignedTopAppBar(
        //modifier = Modifier.border(2.dp, Color.Blue),
        windowInsets = WindowInsets(
            top = 0.dp,
            bottom = 0.dp
        ),
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent
        ),
        title = {
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClicked) {
                BackIcon(contentDesc = "Go Back")
            }
        }
    )
}


