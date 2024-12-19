package com.dicoding.dicodingeventsubmission.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.dicoding.dicodingeventsubmission.R
import com.dicoding.dicodingeventsubmission.data.response.Event
import com.dicoding.dicodingeventsubmission.databinding.ActivityDetailEventBinding

class DetailEventActivity : AppCompatActivity() {

    companion object {
        const val DETAIL_EVENT = "detail_event"
    }

    private lateinit var binding : ActivityDetailEventBinding
    private val detailEventViewModel by viewModels<DetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailEventBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.detail_activity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val detailID = intent.getIntExtra(DETAIL_EVENT, 8622)
        detailEventViewModel.finDetailEvent(detailID)

        observeViewModel()
    }
    private fun observeViewModel() {
        detailEventViewModel.detailEvent.observe(this) { event ->
            setDetail(event)
        }
        detailEventViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }
    }
    private fun setDetail(event: Event?) {
        binding.apply {
            binding.tvTitleDetail.text = event?.name
            binding.tvSummaryDetail.text = event?.summary
            val quota = if (event?.registrants != null) {
                event.quota?.minus(event.registrants)
            } else {
                event?.quota
            }
            binding.tvQuotaDetail.text = getString(R.string.quota, quota)
            Glide.with(this@DetailEventActivity)
                .load(event?.mediaCover)
                .into(binding.imgCoverDetail)
            binding.tvOwnerDetail.text = getString(R.string.detail_owner, event?.ownerName)
            binding.tvTimeDetail.text = event?.beginTime
            binding.tvDescDetail.text = HtmlCompat.fromHtml(
                event?.description.toString(),
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
            binding.btnRegisterDetail.setOnClickListener {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(event?.link))
                startActivity(browserIntent)
            }
        }
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