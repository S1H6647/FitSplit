package com.fitness.fitsplit.ui.screens.split

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.fitness.fitsplit.navigation.Routes
import com.fitness.fitsplit.ui.screens.components.MyTextField
import com.fitness.fitsplit.viewModel.SplitViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateSplitScreen(
    navController: NavController,
    splitViewModel: SplitViewModel
) {
    val context = LocalContext.current
    val splitName by splitViewModel.splitName
    val numberOfDays by splitViewModel.numberOfDays

    // Reset wizard when entering this screen fresh
    LaunchedEffect(Unit) {
        splitViewModel.resetWizard()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Create Split",
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
            Spacer(modifier = Modifier.height(16.dp))

            // Step indicator
            Surface(
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    text = "Step 1 of 3",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Title
            Text(
                text = "Name Your Split",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Give your workout split a name and choose how many days it will have",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Split Name Field
            MyTextField(
                value = splitName,
                onValueChange = { splitViewModel.splitName.value = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Split Name") },
                placeholder = { Text("e.g., Push Pull Legs") },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Number of Days Field
            MyTextField(
                value = numberOfDays,
                onValueChange = { value ->
                    // Only allow digits 1-7
                    val filtered = value.filter { it.isDigit() }
                    if (filtered.isEmpty() || (filtered.toIntOrNull() ?: 0) in 1..7) {
                        splitViewModel.numberOfDays.value = filtered
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Number of Days") },
                placeholder = { Text("Enter 1-7") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                supportingText = {
                    Text("How many days per week (1-7)")
                }
            )

            Spacer(modifier = Modifier.weight(1f))

            Spacer(modifier = Modifier.height(32.dp))

            // Next Button
            Button(
                onClick = {
                    when {
                        splitName.isBlank() -> {
                            Toast.makeText(context, "Please enter a split name", Toast.LENGTH_SHORT).show()
                        }
                        numberOfDays.isBlank() || (numberOfDays.toIntOrNull() ?: 0) !in 1..7 -> {
                            Toast.makeText(context, "Please enter a valid number of days (1-7)", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            navController.navigate(Routes.SELECT_DAYS)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Next",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
