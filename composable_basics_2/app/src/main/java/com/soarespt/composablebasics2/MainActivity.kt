package com.soarespt.composablebasics2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soarespt.composablebasics2.ui.theme.ComposableBasics2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposableBasics2Theme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Greeting(
                        text1 = "All tasks completed",
                        text2 = "All tasks completed",
                        modifier = Modifier
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(text1: String, text2: String, modifier: Modifier = Modifier) {
    val image = painterResource(R.drawable.ic_task_completed)
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.align(Alignment.Center)
        ) {
            Image(
                painter = image, contentDescription = null, contentScale = ContentScale.Fit
            )
            Text(
                text = text1,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
            )
            Text(
                text = text2, textAlign = TextAlign.Center, fontSize = 16.sp, modifier = modifier
            )
        }

    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ComposableBasics2Theme {
        Greeting("All tasks completed", "Nice work!")
    }
}