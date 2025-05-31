package id.ac.unpas.mynote.networks

import id.ac.unpas.mynote.models.Note

data class NoteSingleResponse(
    val message: String,
    val data: Note?
)
