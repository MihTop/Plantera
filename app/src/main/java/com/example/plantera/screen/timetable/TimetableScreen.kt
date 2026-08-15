package com.example.plantera.screen.timetable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Attachment
import androidx.compose.material.icons.filled.Backpack
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimetableScreen(
    navController: NavController,
    viewModel: TimetableViewModel = hiltViewModel()
){
    val timetables by viewModel.timetables.collectAsStateWithLifecycle(initialValue = emptyList())
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Расписание",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Возврат"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF5E7A3C),
                    titleContentColor = Color.White
                )
            )
        },
        floatingActionButton = { ExtendedFloatingActionButton(
            onClick = {
                showAddDialog = true
            },
            icon = { Icon(Icons.Default.Add,"Добавление задания") },
            text = { Text(text = "Запланировать работу") },
        ) }
    ) { paddingValues ->
        if (showAddDialog){
            AddTimetableDialog(onDismiss = {showAddDialog = false},
                onAdd = {name, description, typeOfWork, date, time, equipment ->
                    viewModel.addTimetable(name, description, typeOfWork, date, time, equipment)
                }
            )
        }
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(timetables, key = {it.id}){ timetable ->
                    Card(modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF98FA9F)
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically) {
                            Column(modifier = Modifier.fillMaxWidth(0.9f)) {
                                Text(timetable.name)
                                Text(timetable.description)
                                Text(timetable.typeOfWork)
                                Text(timetable.date)
                                Text(timetable.time)
                                Text(timetable.equipment?: "")
                            }
                            IconButton(onClick = {viewModel.updateDone(!timetable.isDone,timetable.id)}) {
                                Icon(imageVector = if (timetable.isDone){Icons.Default.Done} else{Icons.Default.Close}, contentDescription = "Отметка работы")
                            }
                        }
                    }
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTimetableDialog(
    onDismiss: () -> Unit,
    onAdd: (String, String, String, String, String, String?) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        val name = remember { mutableStateOf("") }
        val description = remember { mutableStateOf("") }
        val typeOfWork = remember { mutableStateOf("") }

        val day = remember { mutableStateOf("") }
        val month = remember { mutableStateOf("") }
        val year = remember { mutableStateOf("") }

        val hour = remember { mutableStateOf("") }
        val minute = remember { mutableStateOf("") }

        val equipment = remember { mutableStateOf("") }

        Surface(
            modifier = Modifier
                .shadow(2.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Введите параметры грядки")

                OutlinedTextField(
                    value = name.value,
                    onValueChange = { name.value = it },
                    label = { Text("Название") }
                )

                OutlinedTextField(
                    value = description.value,
                    onValueChange = { description.value = it },
                    label = { Text("Описание") }
                )

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = typeOfWork.value,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Вид работы") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            leadingIcon = { Icon(Icons.Default.BugReport, contentDescription = "Борьба с вредителями") },
                            onClick = {
                                typeOfWork.value = "Борьба с вредителями"
                                expanded = false
                            },
                            text = { Text("Борьба с вредителями") }
                        )
                        DropdownMenuItem(
                            leadingIcon = { Icon(Icons.Default.WaterDrop, contentDescription = "Поливка растений") },
                            onClick = {
                                typeOfWork.value = "Поливка растений"
                                expanded = false
                            },
                            text = { Text("Поливка растений") }
                        )
                        DropdownMenuItem(
                            leadingIcon = { Icon(Icons.Default.Add, contentDescription = "Посадка растений") },
                            onClick = {
                                typeOfWork.value = "Посадка растений"
                                expanded = false
                            },
                            text = { Text("Посадка растений") }
                        )
                        DropdownMenuItem(
                            leadingIcon = { Icon(Icons.Default.Backpack, contentDescription = "Внести удобрения") },
                            onClick = {
                                typeOfWork.value = "Внести удобрения"
                                expanded = false
                            },
                            text = { Text("Внести удобрения") }
                        )
                        DropdownMenuItem(
                            leadingIcon = { Icon(Icons.Default.Remove, contentDescription = "Уборка урожая") },
                            onClick = {
                                typeOfWork.value = "Уборка урожая"
                                expanded = false
                            },
                            text = { Text("Уборка урожая") }
                        )
                        DropdownMenuItem(
                            leadingIcon = { Icon(Icons.Default.Attachment, contentDescription = "Прочее") },
                            onClick = {
                                typeOfWork.value = "Прочее"
                                expanded = false
                            },
                            text = { Text("Прочее") }
                        )
                    }
                }

                Text("Дата", style = MaterialTheme.typography.labelMedium)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = day.value,
                        onValueChange = {
                            if (it.length <= 2 && it.all { char -> char.isDigit() })
                                day.value = it
                        },
                        label = { Text("ДД") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    Text("/", modifier = Modifier.align(Alignment.CenterVertically))
                    OutlinedTextField(
                        value = month.value,
                        onValueChange = {
                            if (it.length <= 2 && it.all { char -> char.isDigit() })
                                month.value = it
                        },
                        label = { Text("ММ") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    Text("/", modifier = Modifier.align(Alignment.CenterVertically))
                    OutlinedTextField(
                        value = year.value,
                        onValueChange = {
                            if (it.length <= 4 && it.all { char -> char.isDigit() })
                                year.value = it
                        },
                        label = { Text("ГГГГ") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }

                Text("Время", style = MaterialTheme.typography.labelMedium)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = hour.value,
                        onValueChange = {
                            if (it.length <= 2 && it.all { char -> char.isDigit() }) {
                                val h = it.toIntOrNull()
                                if (h == null || (h in 0..23)) hour.value = it
                            }
                        },
                        label = { Text("ЧЧ") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    Text(":", modifier = Modifier.align(Alignment.CenterVertically))
                    OutlinedTextField(
                        value = minute.value,
                        onValueChange = {
                            if (it.length <= 2 && it.all { char -> char.isDigit() }) {
                                val m = it.toIntOrNull()
                                if (m == null || (m in 0..59)) minute.value = it
                            }
                        },
                        label = { Text("ММ") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }

                OutlinedTextField(
                    value = equipment.value,
                    onValueChange = { equipment.value = it },
                    label = { Text("Оборудование") }
                )

                Button(
                    onClick = {
                        val dateString = if (day.value.isNotEmpty() && month.value.isNotEmpty() && year.value.isNotEmpty()) {
                            "${day.value}.${month.value}.${year.value}"
                        } else ""

                        val timeString = if (hour.value.isNotEmpty() && minute.value.isNotEmpty()) {
                            "${hour.value}:${minute.value}"
                        } else ""

                        onAdd(
                            name.value,
                            description.value,
                            typeOfWork.value,
                            dateString,
                            timeString,
                            equipment.value
                        )
                        onDismiss()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Добавить")
                }
            }
        }
    }
}