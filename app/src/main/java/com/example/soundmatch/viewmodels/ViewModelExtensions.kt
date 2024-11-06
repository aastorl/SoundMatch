package com.example.soundmatch.viewmodels

import androidx.lifecycle.AndroidViewModel
import com.example.soundmatch.di.SoundMatchApplication

fun AndroidViewModel.getCountryCode(): String = this.getApplication<SoundMatchApplication>()
    .resources
    .configuration
    .locale
    .country