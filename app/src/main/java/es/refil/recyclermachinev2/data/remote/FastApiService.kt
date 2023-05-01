package es.refil.recyclermachinev2.data.remote

import es.refil.recyclermachinev2.data.dto.ImagePredictionDto
import es.refil.recyclermachinev2.data.dto.UserDetailsDto
import okhttp3.MultipartBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface FastApiService {

    @GET("api/user")
    suspend fun getUser(@Query("uuid") uuid: String): UserDetailsDto

    @Multipart
    @POST("api/bottle_classify")
    suspend fun classifyBottle(@Part imageFile: MultipartBody.Part): ImagePredictionDto

    @POST("api/bottle")
    suspend fun processBottle(
        @Query("uuid") uuid: String,
        @Query("barcode") barcode: String,
        @Query("image_prediction") imagePrediction: String,
    )
}