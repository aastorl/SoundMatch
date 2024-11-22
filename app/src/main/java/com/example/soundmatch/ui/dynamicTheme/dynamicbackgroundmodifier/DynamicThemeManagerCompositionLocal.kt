package com.example.soundmatch.ui.dynamicTheme.dynamicbackgroundmodifier

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import com.example.soundmatch.ui.dynamicTheme.manager.DynamicThemeManager
import com.example.soundmatch.ui.dynamicTheme.manager.SoundMatchDynamicThemeManager
import com.example.soundmatch.usecases.downloadDrawableFromUrlCase.SoundMatchDownloadDrawableFromUrlUseCase
import kotlinx.coroutines.Dispatchers

val LocalDynamicThemeManager: ProvidableCompositionLocal<DynamicThemeManager> =
    staticCompositionLocalOf {
        SoundMatchDynamicThemeManager(
            downloadDrawableFromUrlUseCase = SoundMatchDownloadDrawableFromUrlUseCase(Dispatchers.IO),
            defaultDispatcher = Dispatchers.IO
        )
    }