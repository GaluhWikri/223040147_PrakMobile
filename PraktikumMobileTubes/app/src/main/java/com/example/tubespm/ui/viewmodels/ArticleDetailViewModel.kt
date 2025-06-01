package com.example.tubespm.ui.viewmodels

// ArticleDetailViewModel.kt
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tubespm.data.model.Article
import com.example.tubespm.data.model.Comment
import com.example.tubespm.repository.BlogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ArticleDetailViewModel @Inject constructor(
    private val repository: BlogRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val articleId: String = savedStateHandle.get<String>("articleId") ?: ""

    private val _article = MutableStateFlow<Article?>(null)
    val article: StateFlow<Article?> = _article.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _commentText = MutableStateFlow("")
    val commentText: StateFlow<String> = _commentText.asStateFlow()

    private val _authorName = MutableStateFlow("Anonymous")
    val authorName: StateFlow<String> = _authorName.asStateFlow()

    private val _deleteCommentResult = MutableSharedFlow<Result<Unit>>()
    val deleteCommentResult: SharedFlow<Result<Unit>> = _deleteCommentResult.asSharedFlow()

    val comments: StateFlow<List<Comment>> = repository.getCommentsByArticleId(articleId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        loadArticle()
    }

    private fun loadArticle() {
        viewModelScope.launch {
            _isLoading.value = true
            _article.value = repository.getArticleById(articleId)
            _isLoading.value = false
        }
    }

    fun updateCommentText(text: String) {
        _commentText.value = text
    }

    fun updateAuthorName(name: String) {
        _authorName.value = name
    }

    fun addComment() {
        if (_commentText.value.isBlank() || _authorName.value.isBlank()) return

        viewModelScope.launch {
            val comment = Comment(
                id = UUID.randomUUID().toString(),
                articleId = articleId,
                content = _commentText.value,
                authorName = _authorName.value,
                createdAt = Date()
            )

            repository.createComment(comment)
            _commentText.value = ""
        }
    }

    fun deleteComment(comment: Comment) {
        viewModelScope.launch {
            val result = repository.deleteComment(comment)
            _deleteCommentResult.emit(result)
        }
    }
}