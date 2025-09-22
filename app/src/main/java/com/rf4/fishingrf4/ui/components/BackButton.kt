package com.rf4.fishingrf4.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.rf4.fishingrf4.R

@Composable
fun BackButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6B7280))
    ) {
        Text(stringResource(R.string.back))
    }
}