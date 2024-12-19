package com.dicoding.dicodingeventsubmission.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.dicodingeventsubmission.data.response.EventResponse
import com.dicoding.dicodingeventsubmission.data.response.ListEventsItem
import com.dicoding.dicodingeventsubmission.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchViewModel : ViewModel() {
    private val _listSearchEvent = MutableLiveData<List<ListEventsItem?>?>()
    val listSearchEvent : LiveData<List<ListEventsItem?>?> = _listSearchEvent

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    companion object {
        private const val TAG = "SearchViewModel"
    }

    fun findSearchEvent(searchEvent: String?) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getSearchEvent(-1, searchEvent)
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listSearchEvent.value = response.body()?.listEvents
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }
}