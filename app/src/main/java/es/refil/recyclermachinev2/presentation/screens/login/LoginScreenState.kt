package es.refil.recyclermachinev2.presentation.screens.login

import es.refil.recyclermachinev2.domain.model.UserDetails

data class LoginScreenState(
    val barcodeScanned: String = "Identify yourself with your QR code",
    val userDetails: UserDetails? = null,
    val error: String? = null,
    val isLoading: Boolean = false
)
