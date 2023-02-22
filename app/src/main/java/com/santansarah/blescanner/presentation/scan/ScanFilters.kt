package com.santansarah.blescanner.presentation.scan

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.santansarah.blescanner.domain.models.SCAN_FILTERS
import com.santansarah.blescanner.domain.models.ScanFilterOption
import com.santansarah.blescanner.presentation.previewparams.LayoutPreviews
import com.santansarah.blescanner.presentation.theme.BLEScannerTheme
import com.santansarah.blescanner.utils.windowinfo.AppLayoutInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanFilters(
    onFilter: (ScanFilterOption?) -> Unit,
    scanFilterOption: ScanFilterOption?,
    appLayoutInfo: AppLayoutInfo
) {

    //val filterState = rememberSaveable { mutableStateOf(-1) }
    Surface(
        color = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
    ) {
        if (appLayoutInfo.appLayoutMode.isLandscape()) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(4.dp),
            ) {
                ScanFilterButtons(scanFilterOption, onFilter)
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ScanFilterButtons(scanFilterOption, onFilter)
            }
        }
    }

}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun ScanFilterButtons(
    scanFilterOption: ScanFilterOption?,
    onFilter: (ScanFilterOption?) -> Unit
) {
    SCAN_FILTERS.forEachIndexed { index, scanFilter ->
        FilterChip(
            modifier = Modifier.width(84.dp),
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
                    modifier = Modifier.offset(x = (-5).dp),
                    text = scanFilter.text,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center
                    //color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            },
            /*leadingIcon = {
                if (scanFilterOption?.ordinal == index) {
                    Icon(
                        imageVector = Icons.Outlined.Check,
                        contentDescription = "Selected",
                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }*/
                /*Icon(
                            painter = painterResource(id = scanFilter.icon),
                            contentDescription = scanFilter.text,
                            tint = MaterialTheme.colorScheme.primary
                            //modifier = Modifier.requiredSize(ChipDefaults.LeadingIconSize)
                        )
            }*/

        )
    }
}

@Preview
@Composable
fun PreviewScanFilters(
    @PreviewParameter(LayoutPreviews::class) appLayoutInfo: AppLayoutInfo
) {
    BLEScannerTheme() {
        Surface {
            ScanFilters({}, ScanFilterOption.FAVORITES, appLayoutInfo)
        }
    }
}