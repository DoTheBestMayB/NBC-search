package com.dothebestmayb.nbc_search.presentation.shared

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dothebestmayb.nbc_search.presentation.search.SearchListItem

class SearchSharedViewModel: ViewModel() {

    private val _bookmarkedItem = MutableLiveData<List<SearchListItem>>()
    val bookmarkedItem: LiveData<List<SearchListItem>>
        get() = _bookmarkedItem

    fun update(item: List<SearchListItem>) {
        _bookmarkedItem.value = item.filter { it.bookmarked }
    }
}