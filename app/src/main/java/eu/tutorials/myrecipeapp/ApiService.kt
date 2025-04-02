package eu.tutorials.mynewsapp

import eu.tutorials.myrecipeapp.NewsResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private val retrofit = Retrofit.Builder()
    .baseUrl("https://newsapi.org/v2/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val newsService = retrofit.create(NewsApiService::class.java)

interface NewsApiService {
    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country: String = "us",
        @Query("apiKey") apiKey: String = "0fdbe326f288482490c65e9b419bb98c"
    ): NewsResponse

    @GET("top-headlines")
    suspend fun getNewsByCategory(
        @Query("country") country: String = "us",
        @Query("category") category: String,
        @Query("apiKey") apiKey: String = "0fdbe326f288482490c65e9b419bb98c"
    ): NewsResponse
}