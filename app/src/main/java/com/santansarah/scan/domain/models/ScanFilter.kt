package com.santansarah.scan.domain.models

import com.santansarah.scan.R

enum class ScanFilterOption{
    RSSI,
    NAME,
    FAVORITES,
    FORGET
}

data class ScanFilter(
    val filterOption: ScanFilterOption,
    val icon: Int,
    val text: String
)

val SCAN_FILTERS = listOf(
    ScanFilter(ScanFilterOption.RSSI, R.drawable.signal, "RSSI"),
    ScanFilter(ScanFilterOption.NAME, R.drawable.az_sort, "Name"),
    ScanFilter(ScanFilterOption.FAVORITES, R.drawable.favorite_selected, "Favorites"),
    ScanFilter(ScanFilterOption.FORGET, R.drawable.delete_forever, "Forget"),
)
