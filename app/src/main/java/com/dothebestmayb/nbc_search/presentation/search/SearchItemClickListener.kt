package com.dothebestmayb.nbc_search.presentation.search

import com.dothebestmayb.nbc_search.presentation.model.SearchListItem

interface SearchItemClickListener {

    fun onClick(item: SearchListItem)
}