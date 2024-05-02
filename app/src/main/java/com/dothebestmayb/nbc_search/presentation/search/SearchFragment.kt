package com.dothebestmayb.nbc_search.presentation.search

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.dothebestmayb.nbc_search.R
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
        setListener()
        setObserve()
    }

    private fun setAdapter() = with(binding) {
        rv.adapter = adapter
    }

    private fun setListener() = with(binding) {
        edtSearch.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE || (event?.keyCode == KeyEvent.KEYCODE_ENTER)) {
                hideInput()
                vForRemoveFocus.requestFocus()
                searchViewModel.search()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
        edtSearch.doOnTextChanged { text, _, _, _ ->
            searchViewModel.updateQuery(text.toString())
        }
        btnSearch.setOnClickListener {
            hideInput()
            edtSearch.clearFocus()
            searchViewModel.search()
        }
    }

    private fun hideInput() {
        getSystemService(requireContext(), InputMethodManager::class.java)?.hideSoftInputFromWindow(
            binding.root.windowToken,
            0
        )
    }

    private fun setObserve() {
        searchViewModel.item.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            sharedViewModel.update(it)
        }
        searchViewModel.error.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { errorType ->
                val text = when (errorType) {
                    ErrorType.QUERY_IS_EMPTY -> getString(R.string.query_is_empty)
                    ErrorType.NETWORK -> getString(R.string.network_error)
                }
                Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onDestroyView() {
        _binding = null

        super.onDestroyView()
    }
}