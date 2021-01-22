package com.birdeveloper.springyview.sample.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.birdeveloper.springyview.SpringyRecyclerView
import com.birdeveloper.springyview.sample.R
import com.birdeveloper.springyview.sample.model.remote.ListUserResponse
import com.birdeveloper.springyview.sample.model.remote.User
import com.birdeveloper.springyview.sample.service.SampleService
import com.birdeveloper.springyview.sample.util.AppConstants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {
    var page = 1
    lateinit var retrofit: Retrofit
    lateinit var service: SampleService
    lateinit var springyRecyclerView: SpringyRecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        springyRecyclerView = findViewById(R.id.springyRecyclerView)
        retrofit = Retrofit.Builder().baseUrl(AppConstants.BASE_URL).addConverterFactory(
            GsonConverterFactory.create()).build()
        service = retrofit.create(SampleService::class.java)

        getUsers(page){
            it?.let {userList ->
                springyRecyclerView.adapter = UserAdapter(userList)
                springyRecyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)

            }
        }
    }



    fun getUsers(page: Int, resultData: (data: MutableList<User?>?) -> Unit){
        service.listUsersRepos(page)?.enqueue(object : Callback<ListUserResponse>{
            override fun onResponse(call: Call<ListUserResponse>, response: Response<ListUserResponse>) {
                if (call.isExecuted){
                    if (response.isSuccessful){
                        return resultData(response.body()?.data)
                    }
                }
            }

            override fun onFailure(call: Call<ListUserResponse>, t: Throwable) {
                Log.e(this@MainActivity.localClassName,t.message.toString())
            }

        })
    }
}