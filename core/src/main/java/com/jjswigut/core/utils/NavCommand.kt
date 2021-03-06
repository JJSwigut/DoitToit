package com.jjswigut.core.utils

import android.net.Uri
import androidx.navigation.NavDirections

sealed class NavCommand {
    data class To(val directions: NavDirections) : NavCommand()
    data class DeepLink(val uri: Uri) : NavCommand()
}
