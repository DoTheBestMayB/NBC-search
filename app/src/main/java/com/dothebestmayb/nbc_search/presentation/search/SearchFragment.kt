package com.dothebestmayb.nbc_search.presentation.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
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

        binding.btnSearch.isFocusableInTouchMode = true
        setAdapter()
        setListener()
        setObserve()
    }

    private fun setAdapter() = with(binding) {
        rv.adapter = adapter
    }

    private fun setListener() = with(binding) {
        edtSearch.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
                hideInput()
                edtSearch.clearFocus()
                binding.root.requestFocus()
                searchViewModel.search()
                return@setOnKeyListener true
            }

            return@setOnKeyListener false
        }

        // 두 번째 시동한 방법 : 커서가 맨 앞으로 이동함. 사용자가 텍스트 중간에 수정하다가 엔터를 누르는 경우는 대응 못함
//        edtSearch.addTextChangedListener(object : TextWatcher {
//            var isModified = false
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//            }
//
//            override fun afterTextChanged(s: Editable?) {
//                if (isModified || !(s?.isNotEmpty() == true && s.last() == '\n')) {
//                    searchViewModel.updateQuery(s.toString())
//                    return
//                }
//
//                isModified = true
//                edtSearch.text = s.delete(s.lastIndex, s.lastIndex + 1)
//                hideInput()
//                edtSearch.clearFocus()
//                searchViewModel.search()
//                isModified = false
//
//            }
//        })

        // 세 번째 시도한 방법 : ACTION_DOWN이 너무 여러 번 호출되어
        edtSearch.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideInput()
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
            // 포커스를 다시 editText에 주기
            binding.edtSearch.requestFocus()
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