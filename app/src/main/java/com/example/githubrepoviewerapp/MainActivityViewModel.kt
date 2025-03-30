package com.example.githubrepoviewerapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainActivityViewModel: ViewModel() {
    private val apiService = APIService.create()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _repoList = MutableStateFlow<List<Repo>>(emptyList())
    val repoList: StateFlow<List<Repo>> = _repoList

    private val _errorMsg = MutableStateFlow<String?>(null)
    val errorMsg: StateFlow<String?> = _errorMsg

    private val _hasMorePages = MutableStateFlow(false)
    val hasMorePages: StateFlow<Boolean> = _hasMorePages

    private var _currPage = MutableStateFlow(1)
    val currPage: StateFlow<Int> = _currPage

    fun resetState() {
        _repoList.value = emptyList()
        _errorMsg.value = null
        _hasMorePages.value = false
        _currPage.value = 1
    }

    fun fetchRepos(username: String, page: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMsg.value = null
            try {
                val response = apiService.getUserRepos(username, page)
                if (response.isEmpty()) {
                    _repoList.value = emptyList()
                    _errorMsg.value = "No repos found for \"$username\"."
                    _hasMorePages.value = false
                } else {
                    _repoList.value = response
                    _errorMsg.value = null
                    _hasMorePages.value = response.size >= 30
                }
            } catch (e: Exception) {
                _repoList.value = emptyList()
                _errorMsg.value = "Failed to load repos: ${e.message}"
                println("LOGGING: FAILED TO LOAD REPOS - ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadNextPage(username: String) {
        if (_isLoading.value || !_hasMorePages.value) return
        _currPage.value++
        fetchRepos(username, _currPage.value)
    }

    fun loadPreviousPage(username: String) {
        if (_isLoading.value || _currPage.value <= 1) return
        _currPage.value--
        fetchRepos(username, _currPage.value)
    }
}