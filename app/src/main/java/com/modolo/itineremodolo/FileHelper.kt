package com.modolo.itineremodolo

import android.content.Context

class FileHelper {
    companion object {
        fun getData(context: Context, fileName: String): String {
            return context.assets.open(fileName).use {
                it.bufferedReader().use {
                    it.readText()
                }
            }
        }

    }
}