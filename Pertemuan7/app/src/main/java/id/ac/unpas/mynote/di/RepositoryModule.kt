package id.ac.unpas.mynote.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import id.ac.unpas.mynote.networks.LoginApi
import id.ac.unpas.mynote.networks.NoteApi
import id.ac.unpas.mynote.dao.NoteDao
import id.ac.unpas.mynote.repositories.LoginRepository
import id.ac.unpas.mynote.repositories.NoteRepository
import id.ac.unpas.mynote.repositories.SessionRepository

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

    @Provides
    @ViewModelScoped
    fun provideSessionRepository(
        @ApplicationContext context: Context
    ): SessionRepository {
        return SessionRepository(context)
    }

    @Provides
    @ViewModelScoped
    fun provideLoginRepository(
        api: LoginApi
    ): LoginRepository {
        return LoginRepository(api)
    }
}
