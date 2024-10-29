package com.example.myfap_v3


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.myfap_v3.ui.AppNavigation
import com.example.myfap_v3.ui.MainScreen
import com.example.myfap_v3.ui.ScheduleViewModel
import com.example.myfap_v3.ui.theme.MyFAP_v3Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val scheduleViewModel = viewModel<ScheduleViewModel>()
            AppNavigation(scheduleViewModel)
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
    MyFAP_v3Theme {
        Greeting("Android")
    }
}