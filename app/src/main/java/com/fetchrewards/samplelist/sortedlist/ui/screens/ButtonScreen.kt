package com.fetchrewards.samplelist.sortedlist.ui.screens

import androidx.compose.runtime.Composable
import com.fetchrewards.samplelist.sortedlist.ui.screens.composables.ButtonScreenUIContent

@Composable
fun ButtonScreen(
    onBtnClick: () -> Unit,
) {
    ButtonScreenUIContent(
        onBtnClick = { onBtnClick() }
    )
}