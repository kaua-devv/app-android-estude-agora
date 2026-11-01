package com.app.estudeagora.controller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.estudeagora.model.entity.Learn
import com.app.estudeagora.model.usecase.LearnService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LearnViewModel @Inject constructor(private val service: LearnService): ViewModel() {

    private val _learn = MutableStateFlow<Learn?>(null)
    val learn: StateFlow<Learn?> = _learn.asStateFlow()

    init { random() }

    fun random() {
        // Fora da main thread: a primeira chamada carrega o JSON; as seguintes usam o cache.
        viewModelScope.launch(Dispatchers.Default) {
            _learn.value = service.findRandomLearn()
        }
    }


}