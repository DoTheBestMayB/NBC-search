package com.dothebestmayb.nbc_search.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dothebestmayb.nbc_search.data.retrofit.KakaoSearchRepository
import com.dothebestmayb.nbc_search.data.retrofit.SearchQueryRepository
import com.dothebestmayb.nbc_search.presentation.model.SearchListItem
import com.dothebestmayb.nbc_search.presentation.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val kakaoSearchRepository: KakaoSearchRepository,
    private val searchQueryRepository: SearchQueryRepository,
) : ViewModel() {

    // 북마킹 여부를 검사하지 않은 아이템
    private val _pendingItem = MutableLiveData<List<SearchListItem>>()
    val pendingItem: LiveData<List<SearchListItem>>
        get() = _pendingItem

    // 북마킹 여부를 검사한 아이템
    private val _item = MutableLiveData<List<SearchListItem>>()
    val item: LiveData<List<SearchListItem>>
        get() = _item

    private val query = MutableLiveData<String>()

    private val _restoredQuery = MutableLiveData<String>()
    val restoredQuery: LiveData<String>
        get() = _restoredQuery

    private val _error = MutableLiveData<Event<ErrorType>>()
    val error: LiveData<Event<ErrorType>>
        get() = _error

    private val searchQueryKey = "query"

    init {
        viewModelScope.launch {
            val query = searchQueryRepository.read(searchQueryKey) ?: return@launch
            this@SearchViewModel.query.value = query
            _restoredQuery.value = query
        }
    }

    fun search() {
        val query = query.value ?: run {
            _error.value = Event(ErrorType.QUERY_IS_EMPTY)
            return
        }

        viewModelScope.launch {
            runCatching {
                val items = kakaoSearchRepository.getImage(query)
                _pendingItem.postValue(items)
            }.onFailure {
                _error.value = Event(ErrorType.NETWORK)
            }
        }
    }

    fun update(searchListItem: SearchListItem) {
        val item = _item.value?.toMutableList() ?: return
        val index = item.indexOfFirst { it.id == searchListItem.id }
        if (index == -1) {
            return
        }
        val newItem = when (searchListItem) {
            is SearchListItem.ImageItem -> searchListItem.copy(bookmarked = searchListItem.bookmarked.not())
        }
        item[index] = newItem
        _item.value = item
    }

    fun remove(searchListItem: SearchListItem) {
        val item = _item.value?.map {
            if (it.id == searchListItem.id) {
                when (searchListItem) {
                    is SearchListItem.ImageItem -> searchListItem.copy(bookmarked = searchListItem.bookmarked.not())
                }
            } else {
                it
            }
        } ?: listOf()
        _item.value = item
    }

    fun updateQuery(text: String) {
        query.value = text
        viewModelScope.launch {
            searchQueryRepository.save(searchQueryKey, text)
        }
    }

    fun addBookmarkingCheckedItem(items: List<SearchListItem>) {
        _item.value = items
    }
}
