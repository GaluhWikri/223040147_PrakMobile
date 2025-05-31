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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class NoteRepository @Inject constructor(
    private val api: NoteApi,
    private val dao: NoteDao
) {

    @WorkerThread
    fun loadItems(
        token: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ): Flow<List<Note>> = flow {
        val list: List<Note> = dao.getAllNotes()
        if (token.isEmpty()) {
            onError("Token is empty")
            emit(list)
        } else {
            api.findAll("Bearer $token")
                .suspendOnSuccess {
                    data.whatIfNotNull {
                        dao.insertNotes(data.data!!)
                        val localList: List<Note> = dao.getAllNotes()
                        emit(localList)
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
        }
    }.flowOn(Dispatchers.IO)

    suspend fun insert(
        token: String,
        item: Note,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        api.insert("Bearer $token", item)
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

    suspend fun update(
        token: String,
        id: String,
        item: Note,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        api.update("Bearer $token", id, item)
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

    suspend fun delete(
        token: String,
        id: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        api.delete("Bearer $token", id)
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
