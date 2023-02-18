package com.santansarah.blescanner.presentation.scan

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.santansarah.blescanner.domain.models.SCAN_FILTERS
import com.santansarah.blescanner.domain.models.ScanFilterOption
import com.santansarah.blescanner.presentation.theme.BLEScannerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanFilters(
    onFilter: (ScanFilterOption?) -> Unit,
    scanFilterOption: ScanFilterOption?
) {

    //val filterState = rememberSaveable { mutableStateOf(-1) }
    Surface(
        color = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SCAN_FILTERS.forEachIndexed { index, scanFilter ->
                FilterChip(
                    border = FilterChipDefaults.filterChipBorder(
                        borderColor = MaterialTheme.colorScheme.secondary,
                        //selectedBorderColor = MaterialTheme.colorScheme.outline
                    ),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.secondary,
                        selectedLabelColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        labelColor = MaterialTheme.colorScheme.onSecondaryContainer
                    ),
                    selected = scanFilterOption?.ordinal == index,
                    onClick = {
                        if (scanFilterOption?.ordinal == index) {
                            onFilter(null)
                        } else {
                            onFilter(scanFilter.filterOption)
                        }

                    },
                    label = {
                        Text(
                            text = scanFilter.text,
                            style = MaterialTheme.typography.bodySmall
                            //color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    },
                    leadingIcon = {
                        if (scanFilterOption?.ordinal == index) {
                            Icon(imageVector = Icons.Outlined.Check,
                                contentDescription = "Selected",
                            tint = MaterialTheme.colorScheme.onSecondaryContainer)
                        }
                        /*Icon(
                            painter = painterResource(id = scanFilter.icon),
                            contentDescription = scanFilter.text,
                            tint = MaterialTheme.colorScheme.primary
                            //modifier = Modifier.requiredSize(ChipDefaults.LeadingIconSize)
                        )*/
                    }

                )
            }
        }
    }

}

@Preview
@Composable
fun PreviewScanFilters() {
    BLEScannerTheme() {
        Surface {
            ScanFilters({}, ScanFilterOption.RSSI)
        }
    }
}