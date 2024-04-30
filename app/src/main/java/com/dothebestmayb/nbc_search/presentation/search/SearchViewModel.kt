package com.dothebestmayb.nbc_search.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SearchViewModel : ViewModel() {

    private val _item = MutableLiveData<List<SearchListItem>>()
    val item: LiveData<List<SearchListItem>>
        get() = _item

    fun update(searchListItem: SearchListItem) {
        val item = _item.value?.toMutableList() ?: return
        val index = item.indexOfFirst { it.id == searchListItem.id }
        if (index == -1) {
            return
        }
        item[index] = searchListItem
        _item.value = item
    }
}
