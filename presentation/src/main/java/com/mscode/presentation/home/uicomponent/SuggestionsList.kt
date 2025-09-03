package com.mscode.presentation.home.uicomponent

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mscode.presentation.home.model.UiLeague


@Composable
fun SuggestionsList(
    suggestions: List<UiLeague>,
    onSuggestionClick: (league: UiLeague) -> Unit
) {
    LazyColumn {
        items(suggestions.size) { index ->
            Text(
                text = suggestions[index].league,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSuggestionClick(suggestions[index]) }
                    .padding(8.dp)
            )
            Divider()
        }
    }
}