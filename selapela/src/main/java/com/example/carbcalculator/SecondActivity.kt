
package com.example.carbcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Locale

// MODEL JSON
data class Food(
    val id: String,
    val glucide_100g: Double,
    val names: Map<String, List<String>>
)

class SecondActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CarbScreen()
        }
    }
}

@Composable
fun CarbScreen() {

    var selectedSize by remember { mutableStateOf("Mediu") }
    var foodInput by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }
    var hasCalculated by remember { mutableStateOf(false) }

    val context = LocalContext.current

    fun norm(s: String) = s.trim().lowercase()

    // LIMBA TELEFONULUI
    val lang = remember {
        val l = Locale.getDefault().language
        if (l == "ro") "ro" else "en"
    }

    // LOAD JSON
    val foodList = remember {
        val json = context.assets.open("food.json")
            .bufferedReader()
            .use { it.readText() }

        val gson = Gson()
        val type = object : TypeToken<List<Food>>() {}.type

        gson.fromJson<List<Food>>(json, type)
    }

    // ia toate numele (RO + EN)
    fun getAllNames(food: Food): List<String> {
        return food.names.values.flatten()
    }

    // nume pentru UI (limba telefonului)
    fun getFoodName(food: Food): String {
        return food.names[lang]?.firstOrNull()
            ?: food.names["en"]?.firstOrNull()
            ?: food.id
    }

    //  SMART MATCH (Google-like)
    fun scoreMatch(query: String, text: String): Int {
        val q = norm(query)
        val t = norm(text)

        if (q == t) return 100
        if (t.startsWith(q)) return 80
        if (t.contains(q)) return 50

        var i = 0
        var j = 0
        var match = 0

        while (i < q.length && j < t.length) {
            if (q[i] == t[j]) {
                match++
                i++
            }
            j++
        }

        return if (i == q.length) match * 10 else 0
    }

    // AUTOCOMPLETE SMART
    val filteredFoods = remember(foodInput, foodList) {
        if (foodInput.isBlank()) emptyList()
        else foodList
            .map { food ->
                val bestScore = getAllNames(food).maxOf {
                    scoreMatch(foodInput, it)
                }
                food to bestScore
            }
            .filter { it.second > 0 }
            .sortedByDescending { it.second }
            .take(6)
            .map { it.first }
    }

    // CALCUL
    fun calculate() {
        val selectedFood = foodList
            .map { food ->
                val bestScore = getAllNames(food).maxOf {
                    scoreMatch(foodInput, it)
                }
                food to bestScore
            }
            .maxByOrNull { it.second }
            ?.first

        result = if (selectedFood != null) {

            val grams = when (selectedSize) {
                "Mic" -> 50
                "Mediu" -> 100
                "Mare" -> 150
                else -> 100
            }

            val carbs = selectedFood.glucide_100g * grams / 100

            "Glucide: %.1f g".format(carbs)

        } else {
            "Food not found / Aliment negăsit"
        }
    }

    LaunchedEffect(foodInput, selectedSize) {
        if (!hasCalculated) return@LaunchedEffect
        calculate()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Mărime aliment / Portion size",
            color = Color.Red,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth()) {

            Button(
                onClick = { selectedSize = "Mic" },
                modifier = Modifier.weight(1f).height(70.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedSize == "Mic") Color(0xFF4CAF50) else Color(0xFFE0E0E0),
                    contentColor = Color.Black
                )
            ) { Text("Mic\n50g") }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = { selectedSize = "Mediu" },
                modifier = Modifier.weight(1f).height(70.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedSize == "Mediu") Color(0xFF4CAF50) else Color(0xFFE0E0E0),
                    contentColor = Color.Black
                )
            ) { Text("Mediu\n100g") }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = { selectedSize = "Mare" },
                modifier = Modifier.weight(1f).height(70.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedSize == "Mare") Color(0xFF4CAF50) else Color(0xFFE0E0E0),
                    contentColor = Color.Black
                )
            ) { Text("Mare\n150g") }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Aliment / Food",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Red
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = foodInput,
            onValueChange = { foodInput = it },
            label = { Text("Type food / Scrie aliment") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 26.sp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Button(
            onClick = {
                hasCalculated = true
                calculate()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red,
                contentColor = Color.White
            )
        ) {
            Text("CALCULATE", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = result,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF4CAF50),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        filteredFoods.forEach { food ->
            Text(
                text = getFoodName(food),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        foodInput = getFoodName(food)
                    }
                    .padding(8.dp),
                fontSize = 18.sp
            )
        }
    }
}