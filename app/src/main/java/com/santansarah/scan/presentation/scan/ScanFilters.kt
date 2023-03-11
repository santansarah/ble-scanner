package com.santansarah.scan.presentation.scan

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
import androidx.compose.ui.unit.dp
import com.santansarah.scan.domain.models.SCAN_FILTERS
import com.santansarah.scan.domain.models.ScanFilterOption
import com.santansarah.scan.presentation.previewparams.landscapeNormal
import com.santansarah.scan.presentation.previewparams.portrait
import com.santansarah.scan.presentation.theme.SanTanScanTheme
import com.santansarah.scan.utils.windowinfo.AppLayoutInfo
import com.santansarah.scan.utils.windowinfo.AppLayoutMode

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
                    .padding(start = 16.dp, end = 16.dp,
                        top = 10.dp),
            ) {
                ScanFilterButtons(scanFilterOption, onFilter)
            }
        } else {

            val sidePadding = if (appLayoutInfo.appLayoutMode == AppLayoutMode.PORTRAIT_NARROW)
                16.dp
            else
                4.dp

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top =4.dp, bottom = 4.dp, start = sidePadding, end = sidePadding),
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
fun PreviewScanFilters() {
    SanTanScanTheme() {
        Surface {
            ScanFilters({}, ScanFilterOption.FAVORITES, portrait)
        }
    }
}

@Preview
@Composable
fun PreviewScanFiltersLandscape() {
    SanTanScanTheme() {
        Surface {
            ScanFilters({}, ScanFilterOption.FAVORITES, landscapeNormal)
        }
    }
}
