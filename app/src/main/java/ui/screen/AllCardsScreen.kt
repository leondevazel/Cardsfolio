package com.cs407.cardfolio.ui.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.cs407.cardfolio.CardStore
import com.cs407.cardfolio.CardEntry
import com.cs407.cardfolio.FavoriteStore
import com.cs407.cardfolio.ui.theme.AppTheme

@Composable
fun AllCardsScreen(
    onBack: () -> Unit = {}
) {
    val gradientTopColor = AppTheme.customColors.gradientTop
    val gradientBottomColor = AppTheme.customColors.gradientBottom
    val context = LocalContext.current

    // Figure 12: Declaration of cardState and favorites as Compose state variables
    var cardState by remember { mutableStateOf(CardStore.cards.toMutableMap()) }
    var favorites by remember {
        mutableStateOf(
            CardStore.cards.filter { (_, card) ->
                FavoriteStore.favorites.contains(card)
            }.keys.toMutableSet()
        )
    }

    // For delete dialog
    var showDeleteDialog by remember { mutableStateOf(false) }
    var cardToDeleteId by remember { mutableStateOf<String?>(null) }
    var cardToDelete by remember { mutableStateOf<CardEntry?>(null) }

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
                    text = "All Cards",
                    style = MaterialTheme.typography.headlineLarge
                )
                // Spacer to balance the layout
                Spacer(modifier = Modifier.width(48.dp))
            }

            if (cardState.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No cards added yet",
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
                    items(cardState.entries.toList()) { (cardId, card) ->
                        ElevatedCard(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Column(
                                    modifier = Modifier.weight(1f),
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

                                Row {
                                    // Favorite toggle button implementation from Figure 9(a)
                                    IconButton(
                                        onClick = {
                                            if (favorites.contains(cardId)) {
                                                favorites = favorites.toMutableSet().also { it.remove(cardId) }
                                                FavoriteStore.remove(card)
                                            } else {
                                                favorites = favorites.toMutableSet().also { it.add(cardId) }
                                                FavoriteStore.add(card)
                                            }
                                        }
                                    ) {
                                        if (favorites.contains(cardId)) {
                                            Icon(
                                                imageVector = Icons.Filled.Favorite,
                                                contentDescription = "Remove from favorites",
                                                tint = MaterialTheme.colorScheme.primary
                                            )
                                        } else {
                                            Icon(
                                                imageVector = Icons.Outlined.FavoriteBorder,
                                                contentDescription = "Add to favorites",
                                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }

                                    // Delete button implementation from Figure 9(b)
                                    IconButton(
                                        onClick = {
                                            cardToDeleteId = cardId
                                            cardToDelete = card
                                            showDeleteDialog = true
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.Delete,
                                            contentDescription = "Delete card",
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Figure 14 & 15: Delete confirmation dialog implementation
    if (showDeleteDialog && cardToDeleteId != null && cardToDelete != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Confirm Delete") },
            text = { Text("Are you sure you want to delete this card?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        cardToDeleteId?.let { id ->
                            cardToDelete?.let { card ->
                                cardState = cardState.toMutableMap().also { it.remove(id) }
                                CardStore.cards.remove(id)
                                FavoriteStore.remove(card)
                                if (favorites.contains(id)) {
                                    favorites = favorites.toMutableSet().also { it.remove(id) }
                                }

                                Toast.makeText(context, "Card deleted successfully!", Toast.LENGTH_SHORT).show()
                            }
                        }
                        showDeleteDialog = false
                        cardToDeleteId = null
                        cardToDelete = null
                    }
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        cardToDeleteId = null
                        cardToDelete = null
                    }
                ) {
                    Text("No")
                }
            }
        )
    }
}