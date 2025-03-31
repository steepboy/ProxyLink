import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import me.yiski.Main
import me.yiski.SelectionStorage
import java.nio.file.Files
import java.nio.file.Paths

@Serializable
data class Country(val code: String, val name: String)

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun selectableDropdown(jsonPath: String, textFieldValue: String, setAny: Boolean? = null, isEnabled: Boolean) {
    var expanded by remember { mutableStateOf(false) }
    var selectedCountry by remember { mutableStateOf<Country?>(null) }

    val json = readJsonFile(Main().getUrlResourcePath(jsonPath))
    val countries = parseCountriesJson(json)

    LaunchedEffect(countries) {
        if (selectedCountry == null && countries.isNotEmpty()) {
            selectedCountry = countries.first()
            if (textFieldValue == "Select Country") {
                SelectionStorage.selectedCountry = selectedCountry
            } else if (textFieldValue == "Select Protocol") {
                SelectionStorage.selectedProtocol = selectedCountry!!.code
            }
        }
    }

    Column {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            TextField(
                value = selectedCountry?.name ?: textFieldValue,
                onValueChange = {},
                label = {
                    Text(
                        textFieldValue,
                        style = TextStyle(color = Color(0xFF4169E1))
                    )
                },
                readOnly = true,
                textStyle = TextStyle(color = Color.White),
                trailingIcon = {
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(Icons.Default.ArrowDropDown, contentDescription = "Expand dropdown")
                    }
                },
                modifier = Modifier
                    .background(Color(0xFF333333), CircleShape)
                    .clip(CircleShape),
                enabled = isEnabled
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .background(Color(0xFF333333), RoundedCornerShape(4.dp))
                    .clip(RoundedCornerShape(4.dp))
            ) {
                countries.forEach { country ->
                    DropdownMenuItem(onClick = {
                        selectedCountry = country
                        expanded = false

                        if (textFieldValue == "Select Country") {
                            SelectionStorage.selectedCountry = country
                        } else if (textFieldValue == "Select Protocol") {
                            SelectionStorage.selectedProtocol = country.code
                        }
                    }) {
                        Text(
                            text = country.name,
                            color = Color.White,
                            fontWeight = FontWeight.Light
                        )
                    }
                }
            }
        }

        Spacer(Modifier.padding(bottom = 8.dp))
    }
}

private fun readJsonFile(path: String): String {
    return String(Files.readAllBytes(Paths.get(path)))
}

private fun parseCountriesJson(json: String): List<Country> {
    val countriesMap = Json.decodeFromString<Map<String, String>>(json)

    return countriesMap.map { (code, name) -> Country(code, name) }
}

@Composable
fun selectableCountryDropdown(isEnabled: Boolean) {
    selectableDropdown("countries.json", "Select Country", setAny = true, isEnabled = isEnabled)
}

@Composable
fun selectableProtocolsDropdown(isEnabled: Boolean) {
    selectableDropdown("protocols.json", "Select Protocol", isEnabled = isEnabled)
}
