package es.refil.recyclermachinev2.data.dto

import es.refil.recyclermachinev2.domain.model.UserDetails

data class UserDetailsDto(
    val uuid: String,
    val email: String,
    val name: String,
    val points: Int,
)

fun UserDetailsDto.toUserDetails() = UserDetails(
    uuid = uuid,
    email = email,
    name = name,
    points = points,
)
