package com.dicoding.dicodingeventsubmission.ui.finished

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.dicoding.dicodingeventsubmission.data.response.ListEventsItem
import com.dicoding.dicodingeventsubmission.databinding.FragmentFinishedBinding

class FinishedFragment : Fragment() {

    private var _binding: FragmentFinishedBinding? = null
    private val binding get() = _binding!!
    private val finishedViewModel by viewModels<FinishedViewModel>()
    private lateinit var adapter: FinishedAdapter

    private fun showSelectedEvent(listEventsItem : ListEventsItem) {
        Toast.makeText(requireActivity(), "Kamu Memilih ${listEventsItem.name}", Toast.LENGTH_SHORT).show()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinishedBinding.inflate(inflater, container, false)

        setupRecyclerView()
        observeViewModel()

        return binding.root
    }

    private fun setupRecyclerView() {
        adapter = FinishedAdapter()
        val spanCount = if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            4
        } else {
            2
        }
        binding.finishedRvEvent.layoutManager = GridLayoutManager(requireActivity(), spanCount)
        binding.finishedRvEvent.adapter = adapter

        val itemDecoration = DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL)
        binding.finishedRvEvent.addItemDecoration(itemDecoration)

        adapter.setOnItemClickedCallback(object : FinishedAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ListEventsItem) {
                showSelectedEvent(data)
            }
        })
    }
    private fun observeViewModel(){
        finishedViewModel.listEventFinished.observe(viewLifecycleOwner) { listData ->
            setListData(listData)
        }
        finishedViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }
    }
    private fun setListData(listData: List<ListEventsItem?>?) {
        adapter.submitList(listData)
    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}