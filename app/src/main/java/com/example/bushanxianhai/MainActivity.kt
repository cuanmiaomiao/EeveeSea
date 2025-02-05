package com.example.bushanxianhai

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.bushanxianhai.ui.theme.BushanXianhaiTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.ui.platform.LocalDensity
import androidx.documentfile.provider.DocumentFile
import androidx.compose.foundation.Image
import androidx.compose.foundation.lazy.grid.items
import coil.compose.rememberAsyncImagePainter


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BushanXianhaiTheme {
                // 设置导航控制器
                val navController = rememberNavController()

                // 设置 NavHost
                NavHost(navController = navController, startDestination = "home") {
                    composable("home") {
                        HomeScreen(navController)
                    }
                    composable("secondScreen") {
                        SecondScreen()
                    }
                }
            }
        }
    }


// 主页面
@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Greeting("World")
            Spacer(modifier = Modifier.height(16.dp))
            // 在页面底部添加“开始”按钮
            Button(onClick = {
                // 点击按钮后，导航到 secondScreen
                navController.navigate("secondScreen")
            }) {
                Text("开始")
            }
        }
    }
}

// 第二个页面
@Composable
fun SecondScreen() {
    val context = LocalContext.current
    val images = remember { mutableStateListOf<Uri>() }
    // ActivityResult API用于获取文件夹路径
    val openFolderLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocumentTree(),
        onResult = { uri: Uri? ->
            uri?.let {
                // 读取文件夹中的图片并展示
                val imageUris = getImageFilesInDirectory(context, it)
                images.clear()
                images.addAll(imageUris)
            }
        }
    )    // 获取屏幕宽度和密度
    val screenWidth = LocalContext.current.resources.displayMetrics.widthPixels
    LocalDensity.current.density

    // 每张图片的间距和最大图片宽度
    val paddingInDp = 16.dp
    val paddingInPx = with(LocalDensity.current) { paddingInDp.toPx() }

    // 计算每张图片的宽度，保证每行固定显示3张图片
    val imageWidthInPx = remember(screenWidth) {
        (screenWidth / 3 - paddingInPx * 2) // 3 张图片，减去左右间隔
    }

    val imageWidthInDp = with(LocalDensity.current) { imageWidthInPx.toDp() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Button(onClick = {
            openFolderLauncher.launch(null) // 打开文件夹选择器
        }) {
            Text("导入文件夹")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (images.isEmpty()) {
            Text("没有图片")
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3), // 每行固定显示 3 张图片
                contentPadding = PaddingValues(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(images) { imageUri ->
                    val painter = rememberAsyncImagePainter(imageUri)

                    Image(
                        painter = painter,
                        contentDescription = null,
                        modifier = Modifier
                            .size(imageWidthInDp) // 设置动态调整后的图片大小
                            .padding(paddingInDp) // 设置图片间距
                    )

        }
        }
    }
}}

private fun getImageFilesInDirectory(context: Context, uri: Uri): List<Uri> {
    val documentFile = DocumentFile.fromTreeUri(context, uri) ?: return emptyList()
    val imageUris = mutableListOf<Uri>()

    // 遍历文件夹中的文件
    for (file in documentFile.listFiles()) {
        if (file.isFile && file.type?.startsWith("image/") == true) {
            imageUris.add(file.uri)
        }
    }
    return imageUris
}


@Composable
fun Greeting(name: String) {
    Text(text = "Hello, $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BushanXianhaiTheme {
        HomeScreen(navController = rememberNavController())
    }
}
}