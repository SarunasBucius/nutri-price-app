package com.github.sarunasbucius.nutriprice.core.design.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SearchInList(
    input: String,
    onInputChange: (String) -> Unit
) {
    TextField(
        value = input,
        onValueChange = {
            onInputChange(it)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        label = { Text("Search") },
    )
}