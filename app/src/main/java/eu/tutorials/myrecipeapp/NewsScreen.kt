package eu.tutorials.mynewsapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import eu.tutorials.myrecipeapp.Article
import eu.tutorials.myrecipeapp.NewsCategory
import eu.tutorials.myrecipeapp.ui.components.NewsTopAppBar

@Composable
fun NewsScreen(
    viewstate: MainViewModel.NewsState,
    selectedTab: MainViewModel.Tab,
    onTabSelected: (MainViewModel.Tab) -> Unit,
    selectedCategory: NewsCategory,
    onCategorySelected: (NewsCategory) -> Unit,
    navigateToDetail: (Article) -> Unit,
    navController: NavController  // Add this parameter
) {
    Scaffold(
        topBar = {
            // Add the TopAppBar here
            NewsTopAppBar(
                onSettingsClick = {
                    navController.navigate(Screen.SettingsScreen.route)
                }
            )
        }
    ) { paddingValues ->
        // Wrap your existing content in this Column
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)  // Use the padding from Scaffold
        ) {
            // Your existing TabRow code
            TabRow(selectedTabIndex = if (selectedTab == MainViewModel.Tab.TOP_NEWS) 0 else 1) {
                Tab(
                    selected = selectedTab == MainViewModel.Tab.TOP_NEWS,
                    onClick = { onTabSelected(MainViewModel.Tab.TOP_NEWS) }
                ) {
                    Text("Top News", modifier = Modifier.padding(16.dp))
                }
                Tab(
                    selected = selectedTab == MainViewModel.Tab.CATEGORIES,
                    onClick = { onTabSelected(MainViewModel.Tab.CATEGORIES) }
                ) {
                    Text("Categories", modifier = Modifier.padding(16.dp))
                }
            }

            // Rest of your existing NewsScreen content...
            if (selectedTab == MainViewModel.Tab.CATEGORIES) {
                CategoryTabs(
                    selectedCategory = selectedCategory,
                    onCategorySelected = onCategorySelected
                )
            }

            Box(modifier = Modifier.fillMaxSize()) {
                when {
                    viewstate.loading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                    viewstate.error != null -> {
                        Text(
                            text = viewstate.error,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    else -> {
                        ArticlesList(articles = viewstate.articles, navigateToDetail = navigateToDetail)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryTabs(
    selectedCategory: NewsCategory,
    onCategorySelected: (NewsCategory) -> Unit
) {
    val categories = NewsCategory.values()

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
    ) {
        items(categories) { category ->
            FilterChip(
                selected = category == selectedCategory,
                onClick = { onCategorySelected(category) },
                label = {
                    Text(
                        text = category.name.lowercase().capitalize(),
                        modifier = Modifier.padding(4.dp)
                    )
                },
                modifier = Modifier.padding(end = 8.dp)
            )
        }
    }
}

@Composable
fun ArticlesList(
    articles: List<Article>,
    navigateToDetail: (Article) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(articles) { article ->
            ArticleItem(article = article, onClick = { navigateToDetail(article) })
        }
    }
}

@Composable
fun ArticleItem(
    article: Article,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            if (!article.urlToImage.isNullOrEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(article.urlToImage),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = article.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = article.source.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(4.dp))

                article.description?.let {
                    Text(
                        text = it,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
