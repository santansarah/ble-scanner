package com.santansarah.blescanner.presentation.control

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import com.santansarah.blescanner.domain.bleparsables.ELKBLEDOM
import com.santansarah.blescanner.utils.windowinfo.AppLayoutInfo

@Composable
fun StandardColorPicker(
    appLayoutInfo: AppLayoutInfo,
    selectedColorIdx: Int,
    onSelectedIdxChanged: (Int) -> Unit,
    onColorChanged: (String) -> Unit
) {

    if (appLayoutInfo.appLayoutMode.isLandscape()) {

        OutlinedCard(
            modifier = Modifier.padding(6.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.onSecondaryContainer
            )
        ) {
            ColorGrid(onSelectedIdxChanged, onColorChanged)
        }
    } else {
        ColorGrid(onSelectedIdxChanged = onSelectedIdxChanged, onColorChanged = onColorChanged)
    }
}

@Composable
private fun ColorGrid(
    onSelectedIdxChanged: (Int) -> Unit,
    onColorChanged: (String) -> Unit
) {
    LazyVerticalGrid(
        // modifier = Modifier.padding(4.dp),
        columns = GridCells.Fixed(4)
    ) {
        itemsIndexed(ELKBLEDOM.colorsHex) { idx, color ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.Center)
                    .padding(6.dp)
                    .clickable {
                        onSelectedIdxChanged(idx)
                        onColorChanged(color)
                    }
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color("#$color".toColorInt()))
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.onSurface,
                            RoundedCornerShape(6.dp)
                        )
                )
            }
        }

    }
}