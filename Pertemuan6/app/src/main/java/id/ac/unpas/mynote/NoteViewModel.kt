package id.ac.unpas.mynote

import android.util.Log
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import id.ac.unpas.mynote.models.Note
import id.ac.unpas.mynote.repositories.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val noteRepository: NoteRepository
) : ViewModel() {

    private val _loadList = MutableLiveData<Boolean>(false)

    val list: LiveData<List<Note>> = _loadList.switchMap {
        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO) {
            noteRepository.loadItems(
                onSuccess = {
                    Log.d("NoteViewModel", "load list success")
                },
                onError = {
                    Log.d("NoteViewModel", it)
                }
            ).collect {
                emit(it)
            }
        }
    }

    fun refreshList() {
        _loadList.value = !(_loadList.value ?: false) // trigger reload
    }

    fun insertNote(note: Note) {
        viewModelScope.launch {
            noteRepository.insert(note,
                onSuccess = {
                    Log.d("NoteViewModel", "insert note success")
                    refreshList()
                },
                onError = {
                    Log.d("NoteViewModel", it)
                }
            )
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            noteRepository.delete(note.id,
                onSuccess = {
                    Log.d("NoteViewModel", "delete note success")
                    refreshList()
                },
                onError = {
                    Log.d("NoteViewModel", it)
                }
            )
        }
    }
}
