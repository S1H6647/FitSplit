package com.fitness.fitsplit.ui.screens.split

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.fitness.fitsplit.navigation.Routes
import com.fitness.fitsplit.repository.user.UserRepoImpl
import com.fitness.fitsplit.ui.screens.components.MyTextField
import com.fitness.fitsplit.viewModel.SplitViewModel
import com.fitness.fitsplit.viewModel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NameDaysScreen(
    navController: NavController,
    splitViewModel: SplitViewModel
) {
    val context = LocalContext.current
    val userViewModel = remember { UserViewModel(repo = UserRepoImpl()) }
    val orderedDays = splitViewModel.getOrderedSelectedDays()
    var isSaving by remember { mutableStateOf(false) }

    // Local state for workout names — keyed by day
    val localDayNames = remember {
        mutableStateMapOf<String, String>().apply {
            orderedDays.forEach { day ->
                put(day, splitViewModel.dayNames[day] ?: "")
            }
        }
    }

    // Local state for exercises — keyed by day, each day has a list of exercise names
    val localExercises = remember {
        mutableStateMapOf<String, MutableList<String>>().apply {
            orderedDays.forEach { day ->
                put(day, (splitViewModel.dayExercises[day]?.toMutableList()) ?: mutableListOf())
            }
        }
    }

    // Track a version counter to force recomposition only when add/remove happens
    var exerciseVersion by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Setup Workouts",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Step indicator
            Surface(
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    text = "Step 3 of 3",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Name & Add Exercises",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Name each workout day and add exercises",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Read exerciseVersion to subscribe to structural changes
            @Suppress("UNUSED_EXPRESSION")
            exerciseVersion

            // Day cards with workout name + exercises
            orderedDays.forEachIndexed { index, day ->
                val exercises = localExercises[day] ?: mutableListOf()

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        // Day header
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Surface(
                                color = MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.size(32.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = "${index + 1}",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                }
                            }
                            Text(
                                text = day,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Workout Name — local state, no ViewModel round-trip per keystroke
                        MyTextField(
                            value = localDayNames[day] ?: "",
                            onValueChange = { localDayNames[day] = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Workout Name") },
                            placeholder = { Text("e.g., Chest & Triceps") },
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Exercises section
                        if (exercises.isNotEmpty()) {
                            Text(
                                text = "Exercises",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )

                            exercises.forEachIndexed { exIndex, _ ->
                                key("${day}_ex_${exIndex}_v${exerciseVersion}") {
                                    ExerciseInputRow(
                                        initialValue = exercises.getOrElse(exIndex) { "" },
                                        index = exIndex,
                                        onValueChange = { newValue ->
                                            exercises[exIndex] = newValue
                                        },
                                        onRemove = {
                                            exercises.removeAt(exIndex)
                                            exerciseVersion++
                                        }
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))
                        }

                        // Add Exercise button
                        TextButton(
                            onClick = {
                                val list = localExercises.getOrPut(day) { mutableListOf() }
                                list.add("")
                                exerciseVersion++
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Add Exercise")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Save Button
            Button(
                onClick = {
                    // Sync local state back to ViewModel before saving
                    localDayNames.forEach { (day, name) ->
                        splitViewModel.dayNames[day] = name
                    }
                    localExercises.forEach { (day, exercises) ->
                        splitViewModel.dayExercises[day] = exercises.toMutableList()
                    }

                    val emptyDays = orderedDays.filter { (localDayNames[it] ?: "").isBlank() }
                    if (emptyDays.isNotEmpty()) {
                        Toast.makeText(context, "Please name all workout days", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    val userId = userViewModel.getCurrentUser()?.uid
                    if (userId == null) {
                        Toast.makeText(context, "Please log in first", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    isSaving = true
                    splitViewModel.saveSplit(userId) { success, message ->
                        isSaving = false
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        if (success) {
                            navController.navigate(Routes.DASHBOARD) {
                                popUpTo(Routes.DASHBOARD) { inclusive = true }
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                enabled = !isSaving
            ) {
                if (isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Saving...", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                } else {
                    Icon(imageVector = Icons.Default.Check, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Save Split", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun ExerciseInputRow(
    initialValue: String,
    index: Int,
    onValueChange: (String) -> Unit,
    onRemove: () -> Unit
) {
    // Each exercise field has its own local state — no ViewModel round-trip per keystroke
    var text by remember { mutableStateOf(initialValue) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Exercise number badge
        Surface(
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
            shape = RoundedCornerShape(6.dp),
            modifier = Modifier.size(24.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = "${index + 1}",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        MyTextField(
            value = text,
            onValueChange = {
                text = it
                onValueChange(it)
            },
            modifier = Modifier.weight(1f),
            placeholder = { Text("Exercise name") },
            singleLine = true
        )

        IconButton(
            onClick = onRemove,
            modifier = Modifier.size(36.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Remove exercise",
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
