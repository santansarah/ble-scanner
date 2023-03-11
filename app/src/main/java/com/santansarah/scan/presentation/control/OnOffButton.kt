package com.santansarah.scan.presentation.control

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.santansarah.scan.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnOffButton(
    turnOn: () -> Unit,
    turnOff: () -> Unit,
) {

    var whichWasClicked by rememberSaveable {
        mutableStateOf(-1)
    }

    val onWasClicked = if (whichWasClicked == 0)
        Color.Black.copy(.7f)
    else
        MaterialTheme.colorScheme.tertiary.copy(.3f)

    val offWasClicked = if (whichWasClicked == 1)
        Color.Black.copy(.7f)
    else
        MaterialTheme.colorScheme.tertiary.copy(.3f)


    Row() {
        FilledIconButton(
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = onWasClicked,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                disabledContainerColor = MaterialTheme.colorScheme.tertiary.copy(.3f),
                disabledContentColor = MaterialTheme.colorScheme.onPrimary
            ),
            onClick = {
                whichWasClicked = 0
                turnOn()
            },
            content = {
                Icon(
                    painter = painterResource(id = R.drawable.light_on),
                    contentDescription = "On"
                )
            })


        FilledIconButton(
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = offWasClicked,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                disabledContainerColor = MaterialTheme.colorScheme.tertiary.copy(.3f),
                disabledContentColor = MaterialTheme.colorScheme.onPrimary
            ),
            onClick = {
                whichWasClicked = 1
                turnOff()
            },
            content = {
                Icon(
                    painter = painterResource(id = R.drawable.power),
                    contentDescription = "Off"
                )
            })
    }

}