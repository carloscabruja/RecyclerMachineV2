package es.refil.recyclermachinev2.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import es.refil.recyclermachinev2.data.repo.MainRepositoryImpl
import es.refil.recyclermachinev2.data.repo.NetworkRepositoryImpl
import es.refil.recyclermachinev2.domain.repo.MainRepository
import es.refil.recyclermachinev2.domain.repo.NetworkRepository

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    @ViewModelScoped
    abstract fun bindMainRepository(
        mainRepositoryImpl: MainRepositoryImpl
    ): MainRepository

    @Binds
    @ViewModelScoped
    abstract fun bindNetworkRepository(
        networkRepositoryImpl: NetworkRepositoryImpl
    ): NetworkRepository
}