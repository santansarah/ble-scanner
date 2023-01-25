package com.santansarah.blescanner.domain.models

data class DeviceDetails(
    val services: List<Service>
)

data class Service(
    val uuid: String,
    val name: String,
    val characteristics: List<Characteristics>
)

data class Characteristics(
    val uuid: String,
    val descriptor: String?
)
