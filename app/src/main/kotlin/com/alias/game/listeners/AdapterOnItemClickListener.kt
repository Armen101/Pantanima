package com.example.pantanima.ui.listeners

interface AdapterOnItemClickListener<T> {
    fun onItemClick(item: T)

    fun onDeleteClick(item: T) {

    }
}