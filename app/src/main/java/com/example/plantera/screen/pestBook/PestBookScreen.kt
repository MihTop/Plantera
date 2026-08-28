package com.example.plantera.screen.pestBook

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PestBookScreen(
    navController: NavController,
    viewModel: PestBookViewModel = hiltViewModel()
){
    val pests by viewModel.pests.collectAsStateWithLifecycle(initialValue = emptyList())

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(pests, key = {it.id}){ pest ->
            Card(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF98FA9F)
                ),
                onClick = {navController.navigate("pest_info_screen/${pest.id}")},
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Text(pest.name)
                Text(pest.prevention)
                Text(pest.description)
                Text("Тута картинка: ${pest.description}")
            }
        }
    }
}