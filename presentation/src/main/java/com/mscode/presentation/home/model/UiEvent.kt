package com.mscode.presentation.home.model

sealed class UiEvent {

    data class QueryChanged(val query: String): UiEvent()

}