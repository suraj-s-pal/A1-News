package com.example.a1_news

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
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
    var pageNum = 1
    var totalResults = 0
    var isLoading = false
    var lastVisibleItemPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val progressBar = findViewById<ProgressBar>(R.id.pgBarMain)

        adapter = NewsAdapter(this@MainActivity, articles)
        val newsList = findViewById<RecyclerView>(R.id.newsList)

        val layoutManager = LinearLayoutManager(this@MainActivity)
        newsList.layoutManager = layoutManager
        newsList.adapter = adapter

        newsList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

                // Logic of Scrolling down, load more news only if there are more items to load and not currently loading
                if (dy > 0 && !isLoading && lastVisibleItemPosition >= totalItemCount - 1) {

                    Log.d("last-item", "$visibleItemCount")

                    progressBar.visibility = View.VISIBLE
                    pageNum++
                    getNews()
                }
            }
        })

        getNews()
    }

    private fun getNews() {
        isLoading = true
        val news = NewsService.newsInstance.getHeadlines("in", pageNum)

        news.enqueue(object : Callback<News> {
            override fun onFailure(call: Call<News>, t: Throwable) {
                Log.d("Suraj", "Error in fetching news: ${t.message}")
                Toast.makeText(this@MainActivity, "Error in fetching news", Toast.LENGTH_LONG).show()
                isLoading = false
            }

            override fun onResponse(call: Call<News>, response: Response<News>) {
                val news = response.body()
                if (news != null) {
                    Log.d("Suraj", news.toString())
                    totalResults = news.totalResults
                    val progressBar = findViewById<ProgressBar>(R.id.pgBarMain)
                    progressBar.visibility = View.INVISIBLE
                    articles.addAll(news.articles)
                    adapter.notifyDataSetChanged()
                    Log.d("Suraj", "Total items: ${adapter.itemCount}, Total results: $totalResults")
                }
                isLoading = false
            }
        })
    }
}
