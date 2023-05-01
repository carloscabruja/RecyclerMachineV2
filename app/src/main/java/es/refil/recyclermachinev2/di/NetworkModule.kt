package es.refil.recyclermachinev2.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import es.refil.recyclermachinev2.common.Constants
import es.refil.recyclermachinev2.data.remote.FastApiService
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    @Provides
    @Singleton
    fun provideRetrofit(moshi: Moshi): FastApiService = Retrofit.Builder()
        .run {
            baseUrl(Constants.FAST_API_BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
        }.create(FastApiService::class.java)
}