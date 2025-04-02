package eu.tutorials.mynewsapp

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.tutorials.myrecipeapp.Article
import eu.tutorials.myrecipeapp.NewsCategory
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _newsState = mutableStateOf(NewsState())
    val newsState: State<NewsState> = _newsState

    private val _selectedTab = mutableStateOf(Tab.TOP_NEWS)
    val selectedTab: State<Tab> = _selectedTab

    private val _selectedCategory = mutableStateOf(NewsCategory.GENERAL)
    val selectedCategory: State<NewsCategory> = _selectedCategory

    init {
        fetchTopNews()
    }

    private fun fetchTopNews() {
        viewModelScope.launch {
            try {
                _newsState.value = _newsState.value.copy(loading = true)
                val response = newsService.getTopHeadlines()
                _newsState.value = _newsState.value.copy(
                    articles = response.articles,
                    loading = false,
                    error = null
                )
            } catch (e: Exception) {
                _newsState.value = _newsState.value.copy(
                    loading = false,
                    error = "Error fetching news: ${e.message}"
                )
            }
        }
    }

    fun fetchNewsByCategory(category: NewsCategory) {
        viewModelScope.launch {
            try {
                _newsState.value = _newsState.value.copy(loading = true)
                _selectedCategory.value = category
                val response = newsService.getNewsByCategory(category = category.value)
                _newsState.value = _newsState.value.copy(
                    articles = response.articles,
                    loading = false,
                    error = null
                )
            } catch (e: Exception) {
                _newsState.value = _newsState.value.copy(
                    loading = false,
                    error = "Error fetching ${category.value} news: ${e.message}"
                )
            }
        }
    }

    fun setSelectedTab(tab: Tab) {
        _selectedTab.value = tab
        if (tab == Tab.TOP_NEWS) {
            fetchTopNews()
        } else {
            fetchNewsByCategory(_selectedCategory.value)
        }
    }

    data class NewsState(
        val loading: Boolean = true,
        val articles: List<Article> = emptyList(),
        val error: String? = null
    )

    enum class Tab {
        TOP_NEWS, CATEGORIES
    }
}