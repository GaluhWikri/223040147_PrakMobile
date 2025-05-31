package id.ac.unpas.mynote.repositories

import androidx.annotation.WorkerThread
import com.skydoves.sandwich.message
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnException
import com.skydoves.sandwich.suspendOnSuccess
import com.skydoves.whatif.whatIfNotNull
import id.ac.unpas.mynote.dao.NoteDao
import id.ac.unpas.mynote.models.Note
import id.ac.unpas.mynote.networks.NoteApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class NoteRepository @Inject constructor(
    private val api: NoteApi,
    private val dao: NoteDao
) {

    @WorkerThread
    fun loadItems(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) = flow {
        val list: List<Note> = dao.getAllNotes()
        api.all()
            .suspendOnSuccess {
                data.whatIfNotNull {
                    dao.insertNotes(it.data)
                    emit(it.data)
                    onSuccess()
                }
            }
            .suspendOnError {
                onError(message())
                emit(list)
            }
            .suspendOnException {
                onError(message())
                emit(list)
            }
    }.flowOn(Dispatchers.IO)

    suspend fun insert(item: Note, onSuccess: () -> Unit, onError: (String) -> Unit) {
        api.insert(item)
            .suspendOnSuccess {
                dao.insertNote(item)
                onSuccess()
            }
            .suspendOnError {
                onError(message())
            }
            .suspendOnException {
                onError(message())
            }
    }

    suspend fun update(id: String, item: Note, onSuccess: () -> Unit, onError: (String) -> Unit) {
        api.update(id, item)
            .suspendOnSuccess {
                dao.insertNote(item)
                onSuccess()
            }
            .suspendOnError {
                onError(message())
            }
            .suspendOnException {
                onError(message())
            }
    }

    suspend fun delete(id: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        api.delete(id)
            .suspendOnSuccess {
                dao.deleteNoteById(id)
                onSuccess()
            }
            .suspendOnError {
                onError(message())
            }
            .suspendOnException {
                onError(message())
            }
    }
}
