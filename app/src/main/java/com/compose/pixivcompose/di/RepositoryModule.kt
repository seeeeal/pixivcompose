package com.compose.pixivcompose.di

import com.compose.pixivcompose.network.request.PixivService
import com.compose.pixivcompose.ui.module.PixivRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

  @Provides
  @ViewModelScoped
  fun providePixivRepository(
    pixivService: PixivService,
  ): PixivRepository {
    return PixivRepository(pixivService)
  }
}
