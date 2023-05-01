package es.refil.recyclermachinev2.data.repo

import android.util.Log
import es.refil.recyclermachinev2.common.Resource
import es.refil.recyclermachinev2.data.dto.toImagePrediction
import es.refil.recyclermachinev2.data.dto.toUserDetails
import es.refil.recyclermachinev2.data.remote.FastApiService
import es.refil.recyclermachinev2.domain.model.ImagePrediction
import es.refil.recyclermachinev2.domain.model.UserDetails
import es.refil.recyclermachinev2.domain.repo.NetworkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class NetworkRepositoryImpl @Inject constructor(
    private val api: FastApiService,
) : NetworkRepository {
    override suspend fun getUserDetails(uuid: String): Flow<Resource<UserDetails>> = flow {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(api.getUser(uuid).toUserDetails()))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "An error occurred"))
        }
    }

    override suspend fun uploadImage(imageFile: File): Flow<Resource<ImagePrediction>> = flow {
        Log.d("NetworkRepositoryImpl", "uploadImage: $imageFile")
        val imageData: MultipartBody.Part = MultipartBody.Part.createFormData(
            "file",
            imageFile.name,
            imageFile.asRequestBody()
        )
        Log.d("NetworkRepositoryImpl", "uploadImage: ${imageData.body}")
        emit(Resource.Loading())
        try {
            emit(Resource.Success(api.classifyBottle(imageData).toImagePrediction()))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "An error occurred"))
        } finally {
            imageFile.delete()
        }
    }

    override suspend fun processBottle(
        uuid: String,
        barcode: String,
        imagePrediction: String
    ): Flow<Resource<Unit>> {
        return flow {
            emit(Resource.Loading())
            try {
                emit(Resource.Success(api.processBottle(uuid, barcode, imagePrediction)))
            } catch (e: Exception) {
                emit(Resource.Error(e.message ?: "An error occurred"))
            }
        }
    }
}