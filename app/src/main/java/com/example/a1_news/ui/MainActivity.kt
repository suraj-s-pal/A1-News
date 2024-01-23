package com.example.a1_news.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a1_news.adapter.NewsAdapter
import com.example.a1_news.api.NewsService
import com.example.a1_news.R
import com.example.a1_news.model.Article
import com.example.a1_news.model.News
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    lateinit var adapter: NewsAdapter
    private var articles = mutableListOf<Article>()
    var pageNum = 1
    var totalResults = 0
    var category = "general"
    var isLoading = false
    var lastVisibleItemPosition = 0
    var lastCategory: String? = null
    var linearProgressBar: ProgressBar? = null
    var searchView: SearchView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val progressBar = findViewById<ProgressBar>(R.id.pgBarMain)
        linearProgressBar = findViewById(R.id.linearProgressBar)

        val buttonIds = arrayOf(
            R.id.button1,
            R.id.button2,
            R.id.button3,
            R.id.button4,
            R.id.button5,
            R.id.button6,
            R.id.button7
        )

        //Logic of Search view
        searchView = findViewById(R.id.searchView)

        // Use SearchView.OnQueryTextListener directly
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrBlank()) {
                    performSearch(query)
                    return true
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Handle text change if needed
                return false
            }
        })

        val commonClickListener = View.OnClickListener { view ->
            when (view.id) {
                R.id.button1 -> {
                    category = "business"
                    getHeadlines(category)
                }

                R.id.button2 -> {
                    category = "entertainment"
                    getHeadlines(category)
                }

                R.id.button3 -> {
                    category = "general"
                    getHeadlines(category)
                }

                R.id.button4 -> {
                    category = "health"
                    getHeadlines(category)
                }

                R.id.button5 -> {
                    category = "science"
                    getHeadlines(category)
                }

                R.id.button6 -> {
                    category = "sports"
                    getHeadlines(category)
                }

                R.id.button7 -> {
                    category = "technology"
                    getHeadlines(category)
                }
            }
        }

        buttonIds.forEach { buttonId ->
            val button = findViewById<Button>(buttonId)
            button.setOnClickListener(commonClickListener)
        }

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
                    // Increment pageNum for pagination
                    pageNum++
                    getHeadlines(category)
                }
            }
        })

        // Initial API call
        getHeadlines(category)
    }

    private fun getHeadlines(newCategory: String) {
        isLoading = true

        linearProgressBar?.visibility = View.VISIBLE

        // Clear existing articles only if the category has changed
        if (lastCategory != newCategory) {
            articles.clear()
            lastCategory = newCategory
            adapter.notifyDataSetChanged()
            pageNum = 1 // Reset page number when category changes
        }

        val news = NewsService.newsInstance.getHeadlines(country="in", newCategory, pageNum)

        news.enqueue(object : Callback<News> {
            override fun onFailure(call: Call<News>, t: Throwable) {
                Log.d("Suraj", "Error in fetching news: ${t.message}")
                Toast.makeText(this@MainActivity, "Error in fetching news", Toast.LENGTH_LONG).show()
                isLoading = false
                linearProgressBar?.visibility = View.GONE
            }

            override fun onResponse(call: Call<News>, response: Response<News>) {
                val news = response.body()
                if (news != null) {
                    Log.d("Suraj", news.articles.toString())
                    totalResults = news.totalResults
                    val progressBar = findViewById<ProgressBar>(R.id.pgBarMain)
                    progressBar.visibility = View.INVISIBLE

                    articles.addAll(news.articles)
                    adapter.notifyDataSetChanged()
                    Log.d("Suraj", "Total items: ${adapter.itemCount}, Total results: $totalResults")
                }
                isLoading = false
                linearProgressBar?.visibility = View.GONE
            }
        })
    }

    private fun getNews(query: String) {
        isLoading = true

        linearProgressBar?.visibility = View.VISIBLE

        // Clear existing articles
        articles.clear()
        lastCategory = null


        val news = NewsService.newsInstance.searchNews(query)

        news.enqueue(object : Callback<News> {
            override fun onResponse(call: Call<News>, response: Response<News>) {
                val newsResponse = response.body()

                if (newsResponse != null) {
                    totalResults = newsResponse.totalResults
                    val progressBar = findViewById<ProgressBar>(R.id.pgBarMain)
                    progressBar.visibility = View.INVISIBLE

                    articles.addAll(newsResponse.articles)
                    adapter.notifyDataSetChanged()
                    Log.d(
                        "Suraj",
                        "Total items: ${adapter.itemCount}, Total results: $totalResults"
                    )
                } else {
                    Log.d("Suraj", "Empty response body")
                }

                isLoading = false
                linearProgressBar?.visibility = View.GONE
            }

            override fun onFailure(call: Call<News>, t: Throwable) {
                Log.d("Suraj", "Error in fetching news: ${t.message}")
                Toast.makeText(this@MainActivity, "Error in fetching news", Toast.LENGTH_LONG).show()

                isLoading = false
                linearProgressBar?.visibility = View.GONE
            }
        })
    }
    private fun performSearch(query: String) {
        getNews(query)
    }
}