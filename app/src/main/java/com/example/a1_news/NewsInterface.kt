package com.example.a1_news

import com.example.s_news.models.News
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val BASE_URL = "https://newsapi.org"
//const val API_KEY = "642867dd40174823808e138684afd0ff"
//const val API_KEY = "6400ede5a803452888f745e0bea10eef"
const val API_KEY = "5ce582463e364b23958a220375d49296"

interface NewsInterface {
    @GET("v2/top-headlines?apiKey=$API_KEY")
    fun getHeadlines(
        @Query("country")
        country: String,
        @Query("page")
        page: Int,
    ): Call<News>
}

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