package com.dothebestmayb.nbc_search.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dothebestmayb.nbc_search.data.retrofit.KakaoSearchRepository
import com.dothebestmayb.nbc_search.presentation.model.SearchListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val kakaoSearchRepository: KakaoSearchRepository,
) : ViewModel() {

    private val _item = MutableLiveData<List<SearchListItem>>()
    val item: LiveData<List<SearchListItem>>
        get() = _item

    private val query = MutableLiveData<String>()

    private val _error = MutableLiveData<ErrorType>()
    val error: LiveData<ErrorType>
        get() = _error

    fun search() {
        val query = query.value ?: run {
            _error.value = ErrorType.QUERY_IS_EMPTY
            return
        }

        viewModelScope.launch {
            runCatching {
                val items = kakaoSearchRepository.getImage(query)
                _item.postValue(items)
            }.onFailure {
                _error.value = ErrorType.NETWORK
            }
        }
    }

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
