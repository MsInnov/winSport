package com.mscode.presentation.home.uicomponent

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mscode.presentation.R

@Composable
fun PlaceholderInfo() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Please select a sports league to display its team logos.",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color(0xFF64B5F6),
                fontStyle = FontStyle.Italic
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Only one logo out of two will be displayed in descending order.",
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            color = Color.Gray,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(128.dp))

        Image(
            painter = painterResource(id = R.drawable.winsport_logo_icon),
            contentDescription = "App Icon",
            modifier = Modifier
                .size(256.dp)
                .align(Alignment.CenterHorizontally)
        )
    }
}