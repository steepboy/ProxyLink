import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.IconButton
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.yiski.ButtonsActions
import me.yiski.Providers
import org.jetbrains.compose.resources.painterResource
import proxylink.composeapp.generated.resources.Res
import proxylink.composeapp.generated.resources.github_mark_white
import proxylink.composeapp.generated.resources.power_off_icon
import kotlin.system.exitProcess

@Composable
fun App() {
    var isConnected by remember { mutableStateOf(false) }
    var isSwitchEnabled by remember { mutableStateOf(true) }
    var isButtonsEnabled by remember { mutableStateOf(true) }
    var noProxy by remember { mutableStateOf(false) }
    var description by remember { mutableStateOf("") }

    LaunchedEffect(isConnected) {
        if (isConnected) {
            isSwitchEnabled = false
            isButtonsEnabled = false

            if (!Providers().startWorkingProxy()) {
                noProxy = true
                isSwitchEnabled = true
                isButtonsEnabled = true
                description = "No Proxy Found!"
            } else {
                isSwitchEnabled = true
                isButtonsEnabled = true
                isConnected = true
                description = "Connected!"
            }
        } else {
            isSwitchEnabled = true
            isButtonsEnabled = true
            description = "Disconnected"
        }
    }

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
                enabled = isSwitchEnabled,
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
                description,
                fontSize = 16.sp,
                color = if (isConnected) Color(0xFF4169E1) else Color.Red
            )

            Spacer(modifier = Modifier.height(40.dp))

            selectableCountryDropdown(isButtonsEnabled)
            selectableProtocolsDropdown(isButtonsEnabled)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(Color(0xFF3c3c3c)),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { ButtonsActions().openLinkInBrowser("https://github.com/steepboy/ProxyLink") }) {
                Image(
                    painter = painterResource(Res.drawable.github_mark_white),
                    contentDescription = "Github logo",
                    modifier = Modifier.size(24.dp)
                )
            }

            IconButton(onClick = { exitProcess(0) }) {
                Image(
                    painter = painterResource(Res.drawable.power_off_icon),
                    contentDescription = "Power Off",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}
