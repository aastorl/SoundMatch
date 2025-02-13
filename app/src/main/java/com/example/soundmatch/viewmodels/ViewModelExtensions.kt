package com.example.soundmatch.viewmodels

import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.soundmatch.SoundMatchApplication
import java.util.Locale

fun AndroidViewModel.getCountryCode(): String {
    val localeList = this.getApplication<SoundMatchApplication>()
        .resources.configuration.locales

    val countryCode = if (localeList.isEmpty) {
        Locale.getDefault().country // Usa el valor predeterminado del sistema si la lista está vacía
    } else {
        localeList[0].country // Obtiene el primer locale configurado en el dispositivo
    }

    Log.d("CountryCode", "Current country code: $countryCode") // Log para verificar el código obtenido
    return countryCode
}
