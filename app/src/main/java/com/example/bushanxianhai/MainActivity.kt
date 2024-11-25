package com.example.bushanxianhai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.bushanxianhai.ui.theme.BushanXianhaiTheme
import androidx.compose.ui.Alignment



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BushanXianhaiTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // 添加 Box 实现居中
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding), // 保持内边距
                        contentAlignment = Alignment.Center
                    ){
                        Greeting(
                            name = "world"
                        )     // 这里是 Box 的内容部分，必须放在花括号中
                    }

                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BushanXianhaiTheme {
        // 在预览中也可以用 Box 居中
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            Greeting("Android")
        }
    }
}