package com.example.a1_news.model

import com.example.a1_news.model.Article

data class News(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)