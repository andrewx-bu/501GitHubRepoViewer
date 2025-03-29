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

    fun fetchRepos(username: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMsg.value = null
            try {
                val response = apiService.getUserRepos(username)
                if (response.isEmpty()) {
                    _repoList.value = emptyList()
                    _errorMsg.value = "No repos found for \"$username\"."
                } else {
                    _repoList.value = response
                    _errorMsg.value = null
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
}