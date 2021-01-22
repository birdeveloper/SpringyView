package com.birdeveloper.springyview.sample.service

import com.birdeveloper.springyview.sample.model.remote.ListUserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SampleService {

    @GET("api/users")
    fun listUsersRepos(@Query("page") page: Int): Call<ListUserResponse>?

}