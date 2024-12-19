package com.dicoding.dicodingeventsubmission.ui.finished

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

class FinishedViewModel : ViewModel() {

    private val _listEventFinished = MutableLiveData<List<ListEventsItem?>?>()
    val listEventFinished: LiveData<List<ListEventsItem?>?> = _listEventFinished

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    companion object {
        private const val TAG = "FinishedViewModel"
    }
    init {
        findEventFinished()
    }
//    private fun getInactiveListEvent() : Call<InactiveEventResponse>{
//        val inactiveEvent = ApiConfig.getApiService().getActiveListEvent(active = 0)
//        return inactiveEvent
//    }
    private fun findEventFinished(){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getInactiveListEvent()
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(
                call: Call<EventResponse>,
                response: Response<EventResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listEventFinished.value = response.body()?.listEvents
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