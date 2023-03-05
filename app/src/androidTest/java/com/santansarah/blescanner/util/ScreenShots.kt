package com.santansarah.blescanner.util

import android.content.Context
import android.graphics.Bitmap
import androidx.test.core.app.ApplicationProvider
import java.io.FileOutputStream

fun saveScreenshot(filename: String, bmp: Bitmap) {
    val context: Context = ApplicationProvider.getApplicationContext()
    val path = context.filesDir.canonicalPath
    FileOutputStream("$path/$filename.png").use { out ->
        bmp.compress(Bitmap.CompressFormat.PNG, 100, out)
    }
    println("Saved screenshot to $path/$filename.png")
}