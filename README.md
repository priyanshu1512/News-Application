# News App

A modern news application built with Kotlin and Jetpack Compose that fetches and displays news articles from the News API. The app allows users to browse top headlines and filter news by categories.

## Features
- Browse top headlines
- Filter news by categories (Business, Entertainment, General, Health, Science, Sports, Technology)
- View detailed news articles with images
- Responsive UI built with Jetpack Compose
- Clean Architecture design pattern

## Screenshots
(Show Image)
(Show Image)

## Tech Stack
- **Language**: Kotlin
- **UI**: Jetpack Compose
- **Architecture**: MVVM (Model-View-ViewModel)
- **Networking**: Retrofit
- **Image Loading**: Coil
- **Concurrency**: Kotlin Coroutines
- **Navigation**: Jetpack Compose Navigation

## Project Structure
```
app/
├── src/
│   ├── main/
│   │   ├── java/eu/tutorials/mynewsapp/
│   │   │   ├── data/
│   │   │   │   ├── models/
│   │   │   │   │   ├── Article.kt
│   │   │   │   │   ├── NewsResponse.kt
│   │   │   │   │   └── Source.kt
│   │   │   │   └── api/
│   │   │   │       └── NewsApiService.kt
│   │   │   ├── ui/
│   │   │   │   ├── screens/
│   │   │   │   │   ├── NewsScreen.kt
│   │   │   │   │   └── NewsDetailScreen.kt
│   │   │   │   ├── components/
│   │   │   │   │   ├── ArticleItem.kt
│   │   │   │   │   └── CategoryTabs.kt
│   │   │   │   └── theme/
│   │   │   │       └── Theme.kt
│   │   │   ├── utils/
│   │   │   │   └── DateUtils.kt
│   │   │   ├── viewmodel/
│   │   │   │   └── MainViewModel.kt
│   │   │   ├── navigation/
│   │   │   │   └── Screen.kt
│   │   │   └── MainActivity.kt
│   │   └── res/
│   │       └── ...
│   └── test/
└── build.gradle
```

## Installation

1. Clone this repository:
   ```sh
   git clone https://github.com/yourusername/news-app.git
   ```

2. Open the project in Android Studio.

3. Add your News API key in the `NewsApiService.kt` file:
   ```kotlin
   @GET("top-headlines")
   suspend fun getTopHeadlines(
       @Query("country") country: String = "us",
       @Query("apiKey") apiKey: String = "YOUR_API_KEY_HERE"
   ): NewsResponse
   ```

4. Build and run the application.

## API Key
This application uses the [News API](https://newsapi.org/) to fetch news articles. You need to register for an API key and replace the placeholder in the code with your own API key.

## Future Enhancements
- Add search functionality
- Implement bookmarks feature
- Add dark theme support
- Implement offline caching
- Add unit and UI tests
- Add sharing functionality

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
