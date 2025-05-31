package id.ac.unpas.mynote.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import id.ac.unpas.mynote.dao.NoteDao
import id.ac.unpas.mynote.networks.NoteApi
import id.ac.unpas.mynote.repositories.NoteRepository

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @Provides
    @ViewModelScoped
    fun provideNoteRepository(
        api: NoteApi,
        dao: NoteDao
    ): NoteRepository {
        return NoteRepository(api, dao)
    }
}
