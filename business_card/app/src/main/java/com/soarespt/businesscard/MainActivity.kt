package com.soarespt.businesscard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import com.soarespt.businesscard.ui.theme.BusinessCardTheme
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.core.graphics.toColorInt
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.Icon


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BusinessCardTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    BusinessCardTemplate(
                        name = "Sergio Soares",
                        textBelow = "Android Developer Extraordinaire",
                        phoneNumber = "+358 403740545",
                        modifier = Modifier
                    )
                }
            }
        }
    }
}

@Composable
fun BusinessCardTemplate(name: String, textBelow: String, phoneNumber : String, modifier: Modifier = Modifier) {
    val image = painterResource(R.drawable.android_logo)
    Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .background(Color("#D2E8D4".toColorInt()))
                .fillMaxSize()
    ) {
        Box (
            modifier = Modifier
                    .size(180.dp)
                    .background(Color("#073042".toColorInt()))
        ) {
            Image(
                painter = image,
                contentDescription = null,
                contentScale = ContentScale.Fit,
            )
        }

        Text(
            text = name,
            //fontWeight = FontWeight.Bold,
            color = Color.Black,
            fontSize = 45.sp,
            lineHeight = 50.sp,
            textAlign = TextAlign.Center,
            modifier = modifier
        )
        Text(
            text = textBelow,
            color = Color("#006B38".toColorInt()),
            modifier = modifier
        )

        Spacer(Modifier.height(20.dp))
        Box(Modifier.size(100.dp))
        Spacer(Modifier.width(20.dp))
        Icon(Icons.Rounded.Menu, contentDescription = "Localized description")
        Text(
            text = phoneNumber, fontWeight = FontWeight.Bold,
            color = Color.Red,
            modifier = modifier
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BusinessCardTemplatePreview() {
    BusinessCardTheme {
        BusinessCardTemplate("Sergio Soares", "Android Developer Extraordinaire", "+358 403740545")
    }
}