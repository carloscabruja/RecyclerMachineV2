package es.refil.recyclermachinev2.data.dto

import es.refil.recyclermachinev2.domain.model.ImagePrediction

data class ImagePredictionDto(
    val prediction: String = "",
)

fun ImagePredictionDto.toImagePrediction() = ImagePrediction(
    prediction = prediction,
)
