package com.example.soundmatch.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.FileReader

object CsvUtils {
    suspend fun loadPreviewsFromCsv(csvFilePath: String): Map<String, String?> =
        withContext(Dispatchers.IO) {
            val previews = mutableMapOf<String, String?>()
            try {
                val reader = BufferedReader(FileReader(csvFilePath))
                reader.readLine() // Omitir la primera línea (encabezados)
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    val parts = line!!.split(",")
                    val id = parts[0]
                    val previewUrl = parts.last().trim('"').ifEmpty { null } // Eliminar comillas dobles y manejar cadenas vacías
                    previews[id] = previewUrl
                }
                reader.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            previews
        }
}