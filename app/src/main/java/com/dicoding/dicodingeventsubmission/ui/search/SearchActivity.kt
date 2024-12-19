package com.dicoding.dicodingeventsubmission.ui.search

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.dicodingeventsubmission.R
import com.dicoding.dicodingeventsubmission.data.response.ListEventsItem
import com.dicoding.dicodingeventsubmission.databinding.ActivitySearchBinding
import com.dicoding.dicodingeventsubmission.ui.upcoming.UpcomingAdapter

class SearchActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySearchBinding
    private lateinit var adapter : UpcomingAdapter
    private val searchViewModel by viewModels<SearchViewModel>()

    companion object {
        const val TEXT_SEARCH = "text_search"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.searchActivity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }
        supportActionBar?.hide()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val searchEvent = intent.getStringExtra(TEXT_SEARCH)
        searchViewModel.findSearchEvent(searchEvent)
        binding.searchAppBar.setText(searchEvent)

        setupSearchBar()
        setupRecyclerView()
        observeViewModel()
    }
    private fun setupRecyclerView() {
        adapter = UpcomingAdapter()
        binding.rvSearchEvent.layoutManager = LinearLayoutManager(this)
        binding.rvSearchEvent.adapter = adapter

        val itemDecoration = DividerItemDecoration(this@SearchActivity, DividerItemDecoration.VERTICAL)
        binding.rvSearchEvent.addItemDecoration(itemDecoration)
    }
    private fun observeViewModel() {
        searchViewModel.listSearchEvent.observe(this) { listSearchData ->
            setListData(listSearchData)
        }
        searchViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }
    }
    private fun setupSearchBar() {
        binding.searchAppView.setupWithSearchBar(binding.searchAppBar)
        binding.searchAppView.editText.setOnEditorActionListener { textView, actionId, event ->
            val searchNewEvent = binding.searchAppView.text.toString()
            searchViewModel.findSearchEvent(searchNewEvent)
            binding.searchAppBar.setText(binding.searchAppView.text)
            binding.searchAppView.hide()
            false
        }
    }
    private fun setListData(listData: List<ListEventsItem?>?) {
        adapter.submitList(listData)
    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}