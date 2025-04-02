package eu.tutorials.mynewsapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import eu.tutorials.myrecipeapp.Article
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun NewsDetailScreen(article: Article) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = article.title,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = article.source.name,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp
            )

            Text(
                text = formatDate(article.publishedAt),
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (!article.urlToImage.isNullOrEmpty()) {
            Image(
                painter = rememberAsyncImagePainter(article.urlToImage),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Make the text content scrollable
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            article.description?.let {
                Text(
                    text = it,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Justify
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            article.content?.let {
                Text(
                    text = it,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Justify
                )
            }

            // For displaying the full article content
            // You can concatenate description and content or get full content from the API
            Text(
                text = article.content ?: "",
                fontSize = 16.sp,
                textAlign = TextAlign.Justify
            )
        }
    }
}
fun formatDate(dateString: String): String {
    try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        return outputFormat.format(date ?: Date())
    } catch (e: Exception) {
        return dateString
    }
}