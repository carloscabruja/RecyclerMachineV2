package es.refil.recyclermachinev2.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserDetails(
    val uuid: String,
    val email: String,
    val name: String,
    val points: Int,
) : Parcelable
