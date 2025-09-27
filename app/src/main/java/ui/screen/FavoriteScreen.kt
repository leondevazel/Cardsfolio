package com.cs407.cardfolio.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.cs407.cardfolio.FavoriteStore
import com.cs407.cardfolio.CardStore
import com.cs407.cardfolio.ui.theme.AppTheme

@Composable
fun FavoriteScreen(
    onBack: () -> Unit = {}
) {
    val gradientTopColor = AppTheme.customColors.gradientTop
    val gradientBottomColor = AppTheme.customColors.gradientBottom

    // Observe favorites list changes
    val favorites by remember { derivedStateOf { FavoriteStore.favorites.toList() } }

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
            // Header with back button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
                Text(
                    text = "Favorites",
                    style = MaterialTheme.typography.headlineLarge
                )
                // Spacer to balance the layout
                Spacer(modifier = Modifier.width(48.dp))
            }

            if (favorites.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No favorites yet",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(favorites) { card ->
                        // Find the ID for this card from CardStore
                        val cardId = CardStore.cards.entries.find { it.value == card }?.key ?: "Unknown"

                        ElevatedCard(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = card.name,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = "Hobby: ${card.hobby}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "Age: ${card.age}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}