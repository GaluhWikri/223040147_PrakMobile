package id.ac.unpas.mynote.ui.screens

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.ac.unpas.mynote.models.Note
import id.ac.unpas.mynote.repositories.NoteRepository
import id.ac.unpas.mynote.repositories.SessionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private val _token: MutableLiveData<String> = MutableLiveData()

    val list: LiveData<List<Note>> = _token.switchMap { token ->
        liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
            emitSource(
                noteRepository.loadItems(
                    token,
                    onSuccess = {
                        Log.d("NoteViewModel", "load list success")
                    },
                    onError = {
                        Log.d("NoteViewModel", it)
                    }
                ).asLiveData()
            )
        }
    }

    fun updateToken() {
        viewModelScope.launch {
            sessionRepository.token
                .catch {
                    Log.e("NoteViewModel", "Error collecting token")
                }
                .collect {
                    Log.d("NoteViewModel", "Token $it")
                    _token.postValue(it)
                }
        }
    }

    fun insertNote(note: Note) {
        viewModelScope.launch {
            sessionRepository.token.catch {
                Log.e("NoteViewModel", "Error collecting token")
            }.collect { token ->
                viewModelScope.launch {
                    noteRepository.insert(
                        token,
                        note,
                        onSuccess = {
                            Log.d("NoteViewModel", "insert note success")
                        },
                        onError = {
                            Log.d("NoteViewModel", it)
                        }
                    )
                }
            }
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            sessionRepository.token.catch {
                Log.e("NoteViewModel", "Error collecting token")
            }.collect { token ->
                viewModelScope.launch {
                    noteRepository.delete(
                        token,
                        note.id,
                        onSuccess = {
                            Log.d("NoteViewModel", "delete note success")
                        },
                        onError = {
                            Log.d("NoteViewModel", it)
                        }
                    )
                }
            }
        }
    }
}
