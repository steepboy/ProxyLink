import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import me.yiski.ButtonsActions
import org.jetbrains.compose.resources.painterResource
import proxylink.composeapp.generated.resources.Res
import proxylink.composeapp.generated.resources.github_mark_white

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "ProxyLink",
        state = rememberWindowState(width = 370.dp, height = 480.dp),
        resizable = false
    ) {
        App()
    }
}

@Composable
fun App() {
    var isConnected by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "ProxyLink",
                fontSize = 50.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4169E1),
                modifier = Modifier.padding(top = 10.dp)
            )
            Spacer(modifier = Modifier.height(40.dp))

            Switch(
                checked = isConnected,
                onCheckedChange = { isConnected = it },
                modifier = Modifier.scale(4.4f),
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color(0xFF353535),
                    checkedTrackColor = Color(0xFF3c4bc8),
                    uncheckedThumbColor = Color.Gray,
                    uncheckedTrackColor = Color.LightGray
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                if (isConnected) "Connected" else "Disconnected",
                fontSize = 18.sp,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                "Your internet is ${if (isConnected) "protected." else "unprotected."}",
                fontSize = 16.sp,
                color = if (isConnected) Color(0xFF4169E1) else Color.Red
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Gray),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text("bambooland", color = Color.White)
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(Color(0xFF3c3c3c)),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { ButtonsActions().openLinkInBrowser("https://github.com/steepboy/ProxyLink") } ) {
                Image(
                    painter = painterResource(Res.drawable.github_mark_white),
                    contentDescription = "Github logo",
                    modifier = Modifier.size(24.dp)
                )
            }

            IconButton(onClick = {}) {
                Icon(
                    Icons.Default.Settings,
                    contentDescription = "Settings",
                    modifier = Modifier.size(24.dp),
                    tint = Color.White
                )
            }
        }
    }
}
