package com.zaclippard.androidworkshopapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.zaclippard.androidworkshopapp.models.Book
import com.zaclippard.androidworkshopapp.models.Library
import com.zaclippard.androidworkshopapp.models.School
import com.zaclippard.androidworkshopapp.ui.components.CircleView
import com.zaclippard.androidworkshopapp.ui.components.CustomAndroidView
import com.zaclippard.androidworkshopapp.ui.theme.AndroidWorkshopAppTheme
import com.zaclippard.androidworkshopapp.ui.theme.Purple40
import com.zaclippard.androidworkshopapp.ui.theme.PurpleGrey40
import com.zaclippard.androidworkshopapp.ui.theme.Typography

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        println("onCreate")
//        setContentView(R.layout.main_activity)
//
//        val composeView = findViewById<ComposeView>(R.id.compose_view)
//
//        composeView.setContent {
//            AndroidWorkshopAppTheme {
////                GreetingCard("Android") {}
//                AndroidView(
//                    factory = { context ->
//                        val view = CustomAndroidView(context)
//                        view.setText("Hello from CustomAndroidView!")
//                        view
//                    }
//                )
//                AndroidView(
//                    factory = { context ->
//                        CircleView(context)
//                    }
//                )
//            }
//        }

        enableEdgeToEdge()
        setContent {
            AndroidWorkshopAppTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = {
                                Text("Title Area")
                            },
                            navigationIcon = {
                                IconButton(onClick = { finish() }) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                        contentDescription = "Localized description"
                                    )
                                }
                            }
                        )
                    },
                ) { innerPadding ->
                    LibraryBookList(modifier = Modifier.padding(innerPadding)) {
                        val intent = Intent(this@MainActivity, SecondActivity::class.java)
                        startActivity(intent)
                    }
//                    GreetingScreen(modifier = Modifier.padding(innerPadding))
//                    Column(modifier = Modifier.padding(innerPadding)) {
//                        GreetingCard("Android")
//                        AndroidView(
//                            factory = { context ->
//                                val inflater = LayoutInflater.from(context)
//                                inflater.inflate(R.layout.main_activity, null) as LinearLayout
//                            },
//                        )
//                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        println("onStart")
    }

    override fun onResume() {
        super.onResume()
        println("onResume")
    }

    override fun onPause() {
        super.onPause()
        println("onPause")
    }

    override fun onStop() {
        super.onStop()
        println("onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        println("onDestroy")
    }


}

@Composable
fun LibraryBookList(modifier: Modifier = Modifier, onClick: () -> Unit) {
    LazyColumn(modifier) {
        items(Library.books) { book ->
            BookCard(book, onClick)
        }
    }
}

@Composable
fun BookCard(book: Book, onClick: () -> Unit) {
    Card(modifier = Modifier.padding(4.dp), onClick = onClick) {
        Text("Title: ${book.title}")
        Text("Author: ${book.author}")
        Text("Pages: ${book.pageCount}")
    }
}

@Composable
fun GreetingScreen(modifier: Modifier = Modifier) {
    Column(modifier) {
        val list = listOf(
            "Android",
            "iOS",
            "Windows",
            "Linux",
            "macOS",
        )
        var rowIndex by remember { mutableIntStateOf(0) }

        Text("Currently selected row: ${rowIndex + 1}")

        LazyColumn {

            itemsIndexed(list) { index, item ->
                GreetingCard(item) { rowIndex = index }
            }
        }
    }
}

@Composable
fun GreetingCard(name: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
        onClick = onClick
    ) {
        Greeting(name, modifier)
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(4.dp)) {
        Row(horizontalArrangement = Arrangement.Center) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("https://www.talkandroid.com/wp-content/uploads/2010/12/android_alien.waving_left.full_.png")
                    .crossfade(true)
                    .build(),
                error = painterResource(R.drawable.android_wave),
                contentDescription = stringResource(R.string.image_description),
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center,
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(color = PurpleGrey40)
                    .align(Alignment.CenterVertically),
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Hello $name!",
                    modifier = modifier,
                )
                Text(
                    text = "It's great to have you here",
                    textAlign = TextAlign.Center,
                    style = Typography.bodySmall,
                    fontStyle = FontStyle.Italic,
                    color = Purple40,
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 150)
@Composable
private fun GreetingCardPreview() {
    AndroidWorkshopAppTheme {
        GreetingCard("Android") {}
    }
}
