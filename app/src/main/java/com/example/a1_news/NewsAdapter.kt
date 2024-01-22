package com.example.a1_news

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.s_news.models.Article
import kotlinx.coroutines.internal.artificialFrame

class NewsAdapter (val context: Context, val article: List<Article>):RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>(){

    class ArticleViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val newsImage = itemView.findViewById<ImageView>(R.id.image_view)
        val newsTitle=itemView.findViewById<TextView>(R.id.textViewTitle)
        val newsSource=itemView.findViewById<TextView>(R.id.textViewSource)
        val newsDate=itemView.findViewById<TextView>(R.id.textViewDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ArticleViewHolder(view)
    }

    override fun getItemCount(): Int {
        return article.size
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = article[position]
        holder.newsDate.text=article.publishedAt
        holder.newsSource.text= article.source.name
        holder.newsTitle.text=article.title
        Glide.with(context).load(article.urlToImage).into(holder.newsImage)
    }

}