package com.dothebestmayb.nbc_search.presentation.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.dothebestmayb.nbc_search.databinding.FragmentSearchBinding
import com.dothebestmayb.nbc_search.presentation.model.SearchListItem
import com.dothebestmayb.nbc_search.presentation.shared.SearchSharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding: FragmentSearchBinding
        get() = _binding!!

    private val sharedViewModel: SearchSharedViewModel by activityViewModels()
    private val searchViewModel: SearchViewModel by viewModels()


    private val adapter = SearchAdapter(object : SearchItemClickListener {
        override fun onClick(item: SearchListItem) {
            searchViewModel.update(item)
        }
    })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()
        setObserve()
    }

    private fun setAdapter() = with(binding) {
        rv.adapter = adapter
    }

    private fun setObserve() {
        searchViewModel.item.observe(viewLifecycleOwner) {
            sharedViewModel.update(it)
        }
    }


    override fun onDestroyView() {
        _binding = null

        super.onDestroyView()
    }
}