package com.soarespt.lemonade

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soarespt.lemonade.ui.theme.LemonadeTheme
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LemonadeTheme {
                Surface (
                    modifier = Modifier.fillMaxSize()
                ){
                    LemonadeApp()
                }
            }
        }
    }
}

@Composable
fun LemonadeAppWithImages(modifier: Modifier = Modifier) {
    var state by remember { mutableIntStateOf(1) }
    var tapsRequired by remember { mutableIntStateOf(0) }
    var currentTaps by remember { mutableIntStateOf(0) }

    val imageResource = when (state) {
        1 -> R.drawable.lemon_tree
        2 -> R.drawable.lemon_squeeze
        3 -> R.drawable.lemon_drink
        else -> R.drawable.lemon_restart
    }

    val textResource = when (state) {
        1 -> R.string.tap_tree
        2 -> R.string.keep_tapping
        3 -> R.string.tap_to_drink
        else -> R.string.tap_to_empty
    }

    fun stateHandler() {
        if (state == 1) {
            state = 2
            tapsRequired = Random.nextInt(2, 5)
            currentTaps = 0
        } else if (state == 2) {
            currentTaps++
            if (currentTaps >= tapsRequired) {
                state = 3
            }
        } else if (state < 4) {
            state += 1
        } else {
            state = 1
        }
    }

    Column(
        modifier = Modifier
            .wrapContentSize(Alignment.Center),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box (
            modifier = Modifier
                .clip(CircleShape)
                .background(Color.LightGray)
        ) {
            Image(
                painter = painterResource(imageResource),
                contentDescription = stringResource(textResource),
                modifier = Modifier
                    .clickable {
                        stateHandler()
                    }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(textResource),
            fontSize = 18.sp,
        )
    }
}


@Preview(showBackground = true)
@Composable
fun LemonadeApp() {
    LemonadeTheme {
        LemonadeAppWithImages(modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
        )
    }
}