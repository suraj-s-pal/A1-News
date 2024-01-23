package com.example.a1_news.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.a1_news.R
import com.example.a1_news.ui.DetailActivity
import com.example.a1_news.model.Article

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
        if (!article.urlToImage.isNullOrEmpty()) {
            try {
                Glide.with(context)
                    .load(article.urlToImage)
                    .into(holder.newsImage)
            } catch (e: Exception) {
                e.printStackTrace()
                // Handle the exception, for example, set a placeholder image
                Glide.with(context)
                    .load(R.drawable.baseline_broken_image_24)
                    .into(holder.newsImage)
            }
        } else {
            // Handle the case where the URL is empty or null
            // You can set a placeholder image or hide the ImageView
            Glide.with(context)
                .load(R.drawable.baseline_broken_image_24)
                .into(holder.newsImage)
        }
        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("URL",article.url)
            context.startActivity(intent)

        }
    }

}