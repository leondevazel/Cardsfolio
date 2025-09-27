package com.cs407.cardfolio.ui.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.cs407.cardfolio.CardStore
import com.cs407.cardfolio.CardEntry
import com.cs407.cardfolio.ui.theme.AppTheme

@Composable
fun AddCardScreen(
    onSave: () -> Unit = {},
    onCancel: () -> Unit = {}
) {
    var name by remember { mutableStateOf("") }
    var hobby by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }

    val context = LocalContext.current
    val gradientTopColor = AppTheme.customColors.gradientTop
    val gradientBottomColor = AppTheme.customColors.gradientBottom

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(gradientTopColor, gradientBottomColor)
                )
            ),
        color = Color.Transparent
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header with back button (acts as Cancel)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onCancel) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
                Text(
                    text = "Add Card",
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            // Centered card form
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .wrapContentHeight(),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Add Card",
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.padding(bottom = 20.dp)
                        )

                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Name") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp)
                        )

                        OutlinedTextField(
                            value = hobby,
                            onValueChange = { hobby = it },
                            label = { Text("Hobby") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp)
                        )

                        OutlinedTextField(
                            value = age,
                            onValueChange = { input ->
                                if (input.all { it.isDigit() }) { age = input }
                            },
                            label = { Text("Age") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 20.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            OutlinedButton(
                                onClick = onCancel,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Cancel")
                            }

                            Button(
                                onClick = {
                                    if (name.isBlank() || hobby.isBlank() || age.isBlank()) {
                                        Toast.makeText(
                                            context,
                                            "Please fill all fields",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        // Generate a meaningful ID like TA1, TA2, etc.
                                        val cardCount = CardStore.cards.size + 1
                                        val id = "TA$cardCount"
                                        val entry = CardEntry(name, hobby, age.toInt())
                                        CardStore.cards[id] = entry

                                        Toast.makeText(
                                            context,
                                            "Card added successfully!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        onSave()
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Save")
                            }
                        }
                    }
                }
            }
        }
    }
}