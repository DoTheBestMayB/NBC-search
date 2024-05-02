package com.dothebestmayb.nbc_search.presentation.shared

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dothebestmayb.nbc_search.presentation.model.SearchListItem
import com.dothebestmayb.nbc_search.presentation.util.Event

class SearchSharedViewModel : ViewModel() {

    private val _bookmarkedItem = MutableLiveData<List<SearchListItem>>()
    val bookmarkedItem: LiveData<List<SearchListItem>>
        get() = _bookmarkedItem

    private val _unBookmarkedItem = MutableLiveData<Event<SearchListItem>>()
    val unBookmarkedItem: LiveData<Event<SearchListItem>>
        get() = _unBookmarkedItem

    private val _bookmarkingCheckedItem = MutableLiveData<Event<List<SearchListItem>>>()
    val bookmarkingCheckedItem: LiveData<Event<List<SearchListItem>>>
        get() = _bookmarkingCheckedItem

    fun update(item: SearchListItem) {
        val newItem = when (item) {
            is SearchListItem.ImageItem -> item.copy(bookmarked = item.bookmarked.not())
        }
        val items = _bookmarkedItem.value?.toMutableList() ?: run {
            _bookmarkedItem.value = listOf(newItem)
            return
        }
        val idx = items.indexOfFirst {
            it.id == item.id
        }
        if (idx == -1) {
            if (newItem.bookmarked) {
                items.add(newItem)
            }
        } else {
            items[idx] = newItem
        }

        _bookmarkedItem.value = items
    }

    fun remove(item: SearchListItem) {
        val items = _bookmarkedItem.value?.toMutableList() ?: return
        items.remove(item)

        _bookmarkedItem.value = items
        _unBookmarkedItem.value = Event(item)
    }

    fun checkBookmark(items: List<SearchListItem>) {
        val bookmarkedItem = _bookmarkedItem.value ?: run {
            _bookmarkingCheckedItem.value = Event(items)
            return
        }

        val checkedItems = items.map { item ->
            if (bookmarkedItem.any { it.id == item.id }) {
                when (item) {
                    is SearchListItem.ImageItem -> item.copy(bookmarked = true)
                }
            } else {
                item
            }
        }
        _bookmarkingCheckedItem.value = Event(checkedItems)
    }
}