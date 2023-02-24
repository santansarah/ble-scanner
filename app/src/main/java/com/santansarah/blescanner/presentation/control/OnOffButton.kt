package com.santansarah.blescanner.presentation.control

import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.santansarah.blescanner.R

@Composable
fun OnOffButton(
    checked: Boolean,
    onCheckChanged: () -> Unit
) {
    FilledIconToggleButton(
        colors = IconButtonDefaults.iconToggleButtonColors(
            containerColor = MaterialTheme.colorScheme.onSurface.copy(.5f),
            contentColor = MaterialTheme.colorScheme.outline,
            checkedContainerColor = MaterialTheme.colorScheme.primary,
            checkedContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        checked = checked,
        onCheckedChange = { onCheckChanged() }) {
        if (checked)
            Icon(
                painter = painterResource(id = R.drawable.light_on),
                contentDescription = "On/Off"
            )
        else
            Icon(
                painter = painterResource(id = R.drawable.light_off),
                contentDescription = "On/Off"
            )
    }

}