package com.dicoding.dicodingeventsubmission.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.dicodingeventsubmission.data.response.ListEventsItem
import com.dicoding.dicodingeventsubmission.databinding.FragmentHomeBinding
import com.dicoding.dicodingeventsubmission.ui.finished.FinishedAdapter
import com.dicoding.dicodingeventsubmission.ui.finished.FinishedViewModel
import com.dicoding.dicodingeventsubmission.ui.search.SearchActivity
import com.dicoding.dicodingeventsubmission.ui.search.SearchActivity.Companion.TEXT_SEARCH
import com.dicoding.dicodingeventsubmission.ui.upcoming.UpcomingAdapter
import com.dicoding.dicodingeventsubmission.ui.upcoming.UpcomingViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeComingViewModel by viewModels<UpcomingViewModel>()
    private val homeFinishedViewModel by viewModels<FinishedViewModel>()
    private lateinit var adapterComing : UpcomingAdapter
    private lateinit var adapterFinish : FinishedAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        setupSearchView()

        setupComingRecyclerView()
        setupFinishRecyclerView()

        observeComingViewModel()
        observeFinishViewModel()

        return binding.root
    }
    private fun setupSearchView() {
        binding.searchView.setupWithSearchBar(binding.searchBar)
        binding.searchView.editText.setOnEditorActionListener { textView, actionId, event ->
            val textSearch = binding.searchView.text
//            binding.searchBar.setText(binding.searchView.text)
            val intentSearch = Intent(requireActivity(), SearchActivity::class.java)
            intentSearch.putExtra(TEXT_SEARCH, textSearch.toString())
            startActivity(intentSearch)
            binding.searchView.hide()
//            Toast.makeText(requireActivity(), binding.searchView.text, Toast.LENGTH_SHORT).show()
            false
        }
    }
    private fun setupComingRecyclerView() {
        adapterComing = UpcomingAdapter()
        binding.homeRvVertical.layoutManager = LinearLayoutManager(requireActivity())
        binding.homeRvVertical.adapter = adapterComing

        val itemDecoration = DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL)
        binding.homeRvVertical.addItemDecoration(itemDecoration)
    }
    private fun setupFinishRecyclerView() {
        adapterFinish = FinishedAdapter()
        binding.homeRvHorizontal.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        binding.homeRvHorizontal.adapter = adapterFinish

        val itemFinishedDecoration = DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL)
        binding.homeRvHorizontal.addItemDecoration(itemFinishedDecoration)
    }
    private fun observeComingViewModel() {
        homeComingViewModel.listEvent.observe(viewLifecycleOwner) { listComingData ->
            setComingListData(listComingData)
        }
        homeComingViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }
    }
    private fun observeFinishViewModel() {
        homeFinishedViewModel.listEventFinished.observe(viewLifecycleOwner) { listFinishData ->
            setFinishedListData(listFinishData)
        }
        homeFinishedViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }
    }
    private fun setComingListData(listData: List<ListEventsItem?>?) {
        val limitedList = listData?.take(5)
        adapterComing.submitList(limitedList)
    }
    private fun setFinishedListData(listData: List<ListEventsItem?>?) {
        val limitedList = listData?.take(5)
        adapterFinish.submitList(limitedList)
    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}