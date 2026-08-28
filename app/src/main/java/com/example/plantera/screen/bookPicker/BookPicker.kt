package com.example.plantera.screen.bookPicker

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.example.plantera.screen.fertilizer.FertilizerScreen
import com.example.plantera.screen.pestBook.PestBookScreen
import com.example.plantera.screen.plantBook.PlantBookScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookPickerScreen(
    navController: NavController
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Растения", "Удобрения", "Вредители и болезни")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Справочники",
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF5E7A3C),
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            PrimaryTabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color(0xFFE8F5E9),
                contentColor = Color(0xFF2E4A1E)
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }

            when (selectedTab) {
                0 -> PlantBookScreen(navController)
                1 -> FertilizerScreen(navController)
                2 -> PestBookScreen(navController)
            }
        }
    }
}