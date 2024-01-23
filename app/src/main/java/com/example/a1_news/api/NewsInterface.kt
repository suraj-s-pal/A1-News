package com.example.a1_news.api

import com.example.a1_news.model.News
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val BASE_URL = "https://newsapi.org"
//const val API_KEY = "642867dd40174823808e138684afd0ff"
//const val API_KEY = "6400ede5a803452888f745e0bea10eef"
//const val API_KEY = "5ce582463e364b23958a220375d49296"
//const val API_KEY = "91ec37dff4ab40319b9033cc3b1cb7d1"
const val API_KEY = "a7d06964bf93483e9d0499e79e20c4cd"

interface NewsInterface {
    @GET("v2/top-headlines?apiKey=$API_KEY")
    fun getHeadlines(
        @Query("country")
        country: String,
        @Query("category")
        category: String,
        @Query("page")
        page: Int,
    ): Call<News>
}


//Making a singleton object of retrofit for calling API
object NewsService {

    val newsInstance : NewsInterface
    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()


        newsInstance = retrofit.create(NewsInterface::class.java)
    }

}