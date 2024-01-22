package com.example.a1_news

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.s_news.models.Article
import com.example.s_news.models.News
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    lateinit var adapter: NewsAdapter
    private var articles = mutableListOf<Article>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter=NewsAdapter(this@MainActivity,articles)
        val newsList = findViewById<RecyclerView>(R.id.newsList)
        newsList.adapter=adapter
        newsList.layoutManager=LinearLayoutManager(this@MainActivity)

        getNews()
    }

    private fun getNews() {
        val news = NewsService.newsInstance.getHeadlines("in",1)

        news.enqueue(object: Callback<News> {

            override fun onFailure(call: Call<News>, t: Throwable) {
                Log.d("Suraj","Error in fetching news")
            }

            override fun onResponse(call: Call<News>, response: Response<News>) {
                val news = response.body()
                if (news!=null){
                    Log.d("Suraj",news.toString())
                    articles.addAll(news.articles)
                    adapter.notifyDataSetChanged()

                }
            }
        })
    }
}