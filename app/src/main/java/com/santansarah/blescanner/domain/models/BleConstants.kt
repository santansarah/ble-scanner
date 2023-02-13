package com.santansarah.blescanner.domain.models



const val UUID_DEFAULT = "-0000-1000-8000-00805F9B34FB"


enum class BlePermissions(val value: Int) {
    PERMISSION_READ(1),
    PERMISSION_READ_ENCRYPTED(2),
    PERMISSION_READ_ENCRYPTED_MITM(4),
    PERMISSION_WRITE(16),
    PERMISSION_WRITE_ENCRYPTED(32),
    PERMISSION_WRITE_ENCRYPTED_MITM(64),
    PERMISSION_WRITE_SIGNED(128),
    PERMISSION_WRITE_SIGNED_MITM(256);

    companion object {

        fun getAllPermissions(bleValue: Int): List<BlePermissions> {
            var propertyList = mutableListOf<BlePermissions>()

            values().forEach {
                if (bleValue and it.value > 0)
                    propertyList.add(it)
            }
            return propertyList

        }
    }
}

enum class BleWriteTypes(val value: Int) {
    WRITE_TYPE_DEFAULT(2),
    WRITE_TYPE_NO_RESPONSE(1),
    WRITE_TYPE_SIGNED(4);

    companion object {

        fun getAllTypes(bleValue: Int): List<BleWriteTypes> {
            var propertyList = mutableListOf<BleWriteTypes>()

            values().forEach {
                if (bleValue and it.value > 0)
                    propertyList.add(it)
            }
            return propertyList

        }
    }
}

enum class BleProperties(val value: Int) {
    PROPERTY_BROADCAST(1),
    PROPERTY_EXTENDED_PROPS(128),
    PROPERTY_INDICATE(32),
    PROPERTY_NOTIFY(16),
    PROPERTY_READ(2),
    PROPERTY_SIGNED_WRITE(64),
    PROPERTY_WRITE(8),
    PROPERTY_WRITE_NO_RESPONSE(4);

    companion object {

        fun getAllProperties(bleValue: Int): List<BleProperties> {
            var propertyList = mutableListOf<BleProperties>()

            values().forEach {
                if (bleValue and it.value > 0)
                    propertyList.add(it)
            }
            return propertyList

        }
    }
}

fun List<BleProperties>.canRead(): Boolean = this.contains(BleProperties.PROPERTY_READ)
fun List<BleProperties>.canWriteProperties(): Boolean = this.any(
    listOf(
        BleProperties.PROPERTY_WRITE, BleProperties.PROPERTY_SIGNED_WRITE,
        BleProperties.PROPERTY_WRITE_NO_RESPONSE
    )::contains
)

fun List<BlePermissions>.canWritePermissions(): Boolean = this.any(
    listOf(
        BlePermissions.PERMISSION_WRITE, BlePermissions.PERMISSION_WRITE_ENCRYPTED,
        BlePermissions.PERMISSION_WRITE_ENCRYPTED_MITM, BlePermissions.PERMISSION_WRITE_SIGNED,
        BlePermissions.PERMISSION_WRITE_SIGNED_MITM
    )::contains
)

fun List<BleProperties>.propsToString() =
    this.joinToString(", ") { it.name }

fun List<BleWriteTypes>.writeTypesToString() = this.joinToString(", ") { it.name }

