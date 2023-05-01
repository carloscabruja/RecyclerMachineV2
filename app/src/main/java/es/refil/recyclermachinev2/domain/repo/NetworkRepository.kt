package es.refil.recyclermachinev2.domain.repo

import es.refil.recyclermachinev2.common.Resource
import es.refil.recyclermachinev2.domain.model.ImagePrediction
import es.refil.recyclermachinev2.domain.model.UserDetails
import kotlinx.coroutines.flow.Flow
import java.io.File

interface NetworkRepository {
    suspend fun getUserDetails(uuid: String): Flow<Resource<UserDetails>>
    suspend fun uploadImage(imageFile: File): Flow<Resource<ImagePrediction>>
    suspend fun processBottle(
        uuid: String,
        barcode: String,
        imagePrediction: String
    ): Flow<Resource<Unit>>
}