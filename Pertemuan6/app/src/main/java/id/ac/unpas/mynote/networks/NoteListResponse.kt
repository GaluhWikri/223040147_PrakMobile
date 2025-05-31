package id.ac.unpas.mynote.networks

import id.ac.unpas.mynote.models.Note

data class NoteListResponse(
    val message: String,
    val data: List<Note>
)
