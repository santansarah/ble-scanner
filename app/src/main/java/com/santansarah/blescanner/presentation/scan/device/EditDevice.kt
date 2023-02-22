package com.santansarah.blescanner.presentation.scan.device

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.santansarah.blescanner.R
import com.santansarah.blescanner.presentation.theme.BLEScannerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditDevice(
    onSave: (String) -> Unit,
    updateEdit: (Boolean) -> Unit,
    currentName: String
) {

    Column(
        modifier = Modifier
            .padding(10.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Edit Device Name",
            style = MaterialTheme.typography.headlineSmall
        )
        Text(text = "Current Name: $currentName")


        var newName by rememberSaveable { mutableStateOf("") }

        OutlinedTextField(
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, bottom = 24.dp),
            value = newName,
            onValueChange = {newName = it},
            trailingIcon = {
                IconButton(onClick = { newName = "" }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear"
                    )
                }
            },
        )

        Button(
            modifier = Modifier.fillMaxWidth(),
            enabled = newName.isNotEmpty(),
            onClick = {
            onSave(newName)
            updateEdit(false)
        }) {
            Text(text = "Update")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                updateEdit(false)
            }) {
            Text(text = "Cancel")
        }
    }


}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL, showBackground = true,
    showSystemUi = true
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL, showSystemUi = true, showBackground = true
)
@Composable
fun PreviewEditDevice() {
BLEScannerTheme() {

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        val backgroundImage = if (isSystemInDarkTheme())
            painterResource(id = R.drawable.ble_background_dark)
        else
            painterResource(id = R.drawable.ble_background)

        Image(
            painter = backgroundImage,
            contentDescription = "",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds,
        )

        Surface(
            color = Color.Transparent,
            modifier = Modifier
                .statusBarsPadding()
                .navigationBarsPadding()
                .fillMaxSize()
        ) {
            EditDevice(onSave = {}, updateEdit = {}, currentName = "ELK-BLEDOM")
        }
    }
}
}
