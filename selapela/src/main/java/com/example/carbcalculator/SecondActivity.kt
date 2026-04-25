
/*package com.example.carbcalculator

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
            color = Color(0xFF8B008B),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth()) {

            Button(
                onClick = { selectedSize = "Mic" },
                modifier = Modifier.weight(1f).height(70.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedSize == "Mic") Color(0xFF4CAF50) else Color(
                        0xFFE0E0E0
                    ),
                    contentColor = Color.Black
                )
            )
            {
                Text(
                    "50g",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }



            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = { selectedSize = "Mediu" },
                modifier = Modifier.weight(1f).height(70.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedSize == "Mediu") Color(0xFF4CAF50) else Color(
                        0xFFE0E0E0
                    ),
                    contentColor = Color.Black
                )
            ) {
                Text(
                    "100g",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }


            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = { selectedSize = "Mare" },
                modifier = Modifier.weight(1f).height(70.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedSize == "Mare") Color(0xFF4CAF50) else Color(
                        0xFFE0E0E0
                    ),
                    contentColor = Color.Black
                )
            ) {
                Text(
                    "150g",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        } 


            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Aliment / Food",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF8B008B)
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
                fontSize = 35.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFF5252),
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
    }*/
/*package com.example.carbcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Locale

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
    val lang = remember { if (Locale.getDefault().language == "ro") "ro" else "en" }

    val foodList = remember {
        try {
            val json = context.assets.open("food.json").bufferedReader().use { it.readText() }
            val type = object : TypeToken<List<Food>>() {}.type
            Gson().fromJson<List<Food>>(json, type)
        } catch (e: Exception) { emptyList() }
    }

    fun getFoodName(food: Food): String = food.names[lang]?.firstOrNull() ?: food.names["en"]?.firstOrNull() ?: food.id

    // FILTRARE STRICTĂ: DOAR CEEA CE ÎNCEPE CU CE AI SCRIS
    val filteredFoods = remember(foodInput, foodList) {
        val query = foodInput.trim()
        if (query.isEmpty()) {
            foodList.sortedBy { getFoodName(it).lowercase() }
        } else {
            foodList
                .filter { getFoodName(it).startsWith(query, ignoreCase = true) }
                .sortedBy { getFoodName(it).lowercase() }
        }
    }

    fun calculate() {
        val selectedFood = foodList.firstOrNull { getFoodName(it).equals(foodInput.trim(), ignoreCase = true) }
        result = if (selectedFood != null) {
            val grams = when (selectedSize) {
                "Mic" -> 50
                "Mediu" -> 100
                "Mare" -> 150
                else -> 100
            }
            "Glucide: %.1f g".format(selectedFood.glucide_100g * grams / 100)
        } else {
            "Aliment negăsit"
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Mărime aliment", color = Color(0xFF8B008B), fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            val sizes = listOf("Mic" to "50g", "Mediu" to "100g", "Mare" to "150g")
            sizes.forEach { (label, weight) ->
                Button(
                    onClick = { selectedSize = label },
                    modifier = Modifier.weight(1f).height(60.dp).padding(horizontal = 4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedSize == label) Color(0xFF4CAF50) else Color(0xFFE0E0E0),
                        contentColor = Color.Black
                    )
                ) { Text(weight, fontSize = 18.sp, fontWeight = FontWeight.Bold) }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = foodInput,
            onValueChange = { foodInput = it; hasCalculated = false },
            label = { Text("Caută aliment...") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { hasCalculated = true; calculate() },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) { Text("CALCULEAZĂ", color = Color.White) }

        Spacer(modifier = Modifier.height(12.dp))
        Text(text = result, fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF5252))
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.fillMaxWidth().weight(1f)) {
            items(filteredFoods) { food ->
                val name = getFoodName(food)
                Column(modifier = Modifier.fillMaxWidth().clickable {
                    foodInput = name
                    hasCalculated = true
                    calculate()
                }) {
                    Text(text = name, modifier = Modifier.padding(16.dp), fontSize = 19.sp)
                    HorizontalDivider(color = Color.LightGray, thickness = 0.5.dp)
                }
            }
        }
    }
}*/
package com.example.carbcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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

    //  SMART MATCH (Păstrat pentru funcția calculate originală)
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

    // AUTOCOMPLETE MODIFICAT: Filtrare alfabetică și strictă (Începe cu...)
    val filteredFoods = remember(foodInput, foodList) {
        val query = foodInput.trim()
        if (query.isBlank()) {
            foodList.sortedBy { getFoodName(it).lowercase() }
        } else {
            foodList
                .filter { getFoodName(it).startsWith(query, ignoreCase = true) }
                .sortedBy { getFoodName(it).lowercase() }
        }
    }

    // CALCUL (Originalul tău)
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
            color = Color(0xFF8B008B),
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
            ) {
                Text("50g", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = { selectedSize = "Mediu" },
                modifier = Modifier.weight(1f).height(70.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedSize == "Mediu") Color(0xFF4CAF50) else Color(0xFFE0E0E0),
                    contentColor = Color.Black
                )
            ) {
                Text("100g", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = { selectedSize = "Mare" },
                modifier = Modifier.weight(1f).height(70.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedSize == "Mare") Color(0xFF4CAF50) else Color(0xFFE0E0E0),
                    contentColor = Color.Black
                )
            ) {
                Text("150g", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Aliment / Food",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF8B008B)
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
            fontSize = 35.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFFF5252),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        // LISTA MODIFICATĂ: LazyColumn pentru scroll și toate rezultatele
        LazyColumn(
            modifier = Modifier.fillMaxWidth().weight(1f)
        ) {
            items(filteredFoods) { food ->
                Text(
                    text = getFoodName(food),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            foodInput = getFoodName(food)
                            hasCalculated = true
                            calculate()
                        }
                        .padding(8.dp),
                    fontSize = 18.sp
                )
            }
        }
    }
}