package com.dothebestmayb.nbc_search.presentation.store

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.dothebestmayb.nbc_search.databinding.FragmentStoreBinding
import com.dothebestmayb.nbc_search.presentation.shared.SearchSharedViewModel

class StoreFragment : Fragment() {

    private var _binding: FragmentStoreBinding? = null
    private val binding: FragmentStoreBinding
        get() = _binding!!

    private val sharedViewModel: SearchSharedViewModel by activityViewModels()
    private val adapter: StoreAdapter = StoreAdapter {
        sharedViewModel.remove(it)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setObserve()
        setAdapter()
    }

    private fun setObserve() {
        sharedViewModel.bookmarkedItem.observe(viewLifecycleOwner) { searchListItems ->
            adapter.submitList(searchListItems)
        }
    }

    private fun setAdapter() {
        binding.rv.adapter = adapter
    }

    override fun onDestroyView() {
        _binding = null

        super.onDestroyView()
    }

}