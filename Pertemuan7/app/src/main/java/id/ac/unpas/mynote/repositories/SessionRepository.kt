package id.ac.unpas.mynote.repositories

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "session"
)

class SessionRepository(context: Context) {
    private val dataStore = context.dataStore

    private companion object {
        val TOKEN = stringPreferencesKey("token")
    }

    suspend fun setToken(token: String) {
        dataStore.edit { settings ->
            settings[TOKEN] = token
        }
    }

    val token: Flow<String> = dataStore.data.map { preferences ->
        preferences[TOKEN] ?: ""
    }
}
