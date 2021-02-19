package com.mrlmoro.core.ui

sealed class PagingEvent {
    object Loading : PagingEvent()
    object Success : PagingEvent()
    object Error : PagingEvent()
}