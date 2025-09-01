package com.mscode.presentation.home.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.mscode.presentation.common.ErrorScreen
import com.mscode.presentation.home.model.UiEvent
import com.mscode.presentation.home.model.UiState
import com.mscode.presentation.home.viewmodel.HomeViewModel

@Composable
fun HomeScreen(homeViewModel: HomeViewModel) {
    val uiState = homeViewModel.uiState.collectAsState()
    when (val state = uiState.value) {
        is UiState.Loading -> Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }

        is UiState.Error -> ErrorScreen()
        is UiState.AvailableLeaguesState -> {
            Column(modifier = Modifier.padding(16.dp)) {
                TextField(
                    value = state.query,
                    onValueChange = { homeViewModel.onEvent( UiEvent.QueryChanged(it))},
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search icon"
                        )
                    },
                    trailingIcon = {
                        if (state.query.isNotEmpty()) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(Color.LightGray)
                                    .clickable { homeViewModel.onEvent(UiEvent.QueryChanged("")) },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Clear text",
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    },
                    label = { Text("Search by league") },
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn {
                    items(state.suggestions.size) { index ->
                        Text(
                            text = state.suggestions[index].league,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    homeViewModel.onEvent(UiEvent.LeagueSelected(state.suggestions[index]))
                                    homeViewModel.onEvent(UiEvent.GetLeagueTeams(state.suggestions[index].league))
                                }
                                .padding(8.dp)
                        )
                        Divider()
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxHeight()
                ) {
                    items(state.teams) { team ->
                        val imageUrl = team.badge ?: team.logo

                        if (imageUrl != null) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .padding(8.dp)
                            ) {
                                AsyncImage(
                                    model = imageUrl,
                                    contentDescription = team.name,
                                    modifier = Modifier
                                        .size(128.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}