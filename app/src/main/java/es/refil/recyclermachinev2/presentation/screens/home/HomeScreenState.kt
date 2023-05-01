package es.refil.recyclermachinev2.presentation.screens.home

import android.net.Uri

data class HomeScreenState(
    val isLoading: Boolean = false,
    val isCameraVisible: Boolean = false,
    val imagePrediction: String = "",
    val barcode: String = "",
    val imageUri: Uri = Uri.EMPTY,
    val userPoints: Int = 0,
)
