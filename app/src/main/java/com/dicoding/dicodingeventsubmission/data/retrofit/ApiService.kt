package com.dicoding.dicodingeventsubmission.data.retrofit

import com.dicoding.dicodingeventsubmission.data.response.DetailEventResponse
import com.dicoding.dicodingeventsubmission.data.response.EventResponse
import com.dicoding.dicodingeventsubmission.data.response.ListEventsItem
import retrofit2.Call
import retrofit2.http.*
import retrofit2.http.GET

interface ApiService {
    @GET("events")
    fun getActiveListEvent(
        @Query("active") active: Int = 1
    ): Call<EventResponse>

    @GET("events")
    fun getInactiveListEvent(
        @Query("active") active: Int = 0
    ):Call<EventResponse>

    @GET("events/{id}")
    fun getDetailEvent(
        @Path("id") id: Int
    ): Call<DetailEventResponse>

    @GET("events")
    fun getSearchEvent(
        @Query("active") active: Int = -1,
        @Query("q") query: String?
    ): Call<EventResponse>
}