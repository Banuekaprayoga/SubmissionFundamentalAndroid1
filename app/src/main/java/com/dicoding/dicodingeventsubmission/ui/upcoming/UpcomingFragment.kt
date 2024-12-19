package com.dicoding.dicodingeventsubmission.ui.upcoming

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
import com.dicoding.dicodingeventsubmission.databinding.FragmentUpcomingBinding

class UpcomingFragment : Fragment() {

    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding!!
    private val upcomingViewModel by viewModels<UpcomingViewModel>()
    private lateinit var adapter: UpcomingAdapter

    private fun showSelectedEvent(listEventsItem: ListEventsItem) {
        Toast.makeText(requireActivity(), "Kamu Memilih ${listEventsItem.name}", Toast.LENGTH_SHORT).show()
    }
    // This property is only valid between onCreateView and
    // onDestroyView.
//    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)

        setupRecyclerView()
        observeViewModel()

        return binding.root
    }

    private fun setupRecyclerView() {
        adapter = UpcomingAdapter()
        binding.upcomingRvEvent.layoutManager = LinearLayoutManager(requireActivity())
        binding.upcomingRvEvent.adapter = adapter

        val itemDecoration = DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL)
        binding.upcomingRvEvent.addItemDecoration(itemDecoration)

        adapter.setOnItemClickCallback(object : UpcomingAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ListEventsItem) {
                showSelectedEvent(data)
            }
        })
    }
    private fun observeViewModel() {
        upcomingViewModel.listEvent.observe(viewLifecycleOwner) { listData ->
            setListData(listData)
        }
        upcomingViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }
    }
    private fun setListData(listData : List<ListEventsItem?>?) {
        adapter.submitList(listData)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}