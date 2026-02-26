package com.fitness.fitsplit.ui.screens.auth

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.fitness.fitsplit.R
import com.fitness.fitsplit.model.User
import com.fitness.fitsplit.repository.user.UserRepoImpl
import com.fitness.fitsplit.navigation.Routes
import com.fitness.fitsplit.ui.screens.components.MyTextField
import com.fitness.fitsplit.viewModel.UserViewModel

@Composable
fun SignupScreen(navController: NavController) {
    val context = LocalContext.current
    var firstName by rememberSaveable { mutableStateOf("") }
    var lastName by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    
    var firstNameError by rememberSaveable { mutableStateOf<String?>(null) }
    var lastNameError by rememberSaveable { mutableStateOf<String?>(null) }
    var emailError by rememberSaveable { mutableStateOf<String?>(null) }
    var passwordError by rememberSaveable { mutableStateOf<String?>(null) }
    var confirmPasswordError by rememberSaveable { mutableStateOf<String?>(null) }
    
    val firstNameFocusRequester = remember { FocusRequester() }
    val lastNameFocusRequester = remember { FocusRequester() }
    val emailFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }
    val confirmPasswordFocusRequester = remember { FocusRequester() }

    val userViewModel = remember { UserViewModel(repo = UserRepoImpl()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .imePadding()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.signup_logo),
            contentDescription = "Logo",
            modifier = Modifier.size(200.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Welcome to the FitSplit",
            style = TextStyle(
                fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                fontWeight = FontWeight.Black
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Subtitle
        Text(
            text = "Create an account to get started",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(32.dp))

        MyTextField(
            value = firstName,
            onValueChange = { 
                firstName = it
                firstNameError = null
            },
            label = { Text("First Name") },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.user),
                    contentDescription = "First Name"
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(firstNameFocusRequester),
            isError = firstNameError != null,
            supportingText = {
                firstNameError?.let {
                    Text(it, color = MaterialTheme.colorScheme.error)
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        MyTextField(
            value = lastName,
            onValueChange = { 
                lastName = it
                lastNameError = null
            },
            label = { Text("Last Name") },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.user),
                    contentDescription = "Last Name"
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(lastNameFocusRequester),
            isError = lastNameError != null,
            supportingText = {
                lastNameError?.let {
                    Text(it, color = MaterialTheme.colorScheme.error)
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        MyTextField(
            value = email,
            onValueChange = { 
                email = it
                emailError = null
            },
            label = { Text("Email") },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.mail),
                    contentDescription = "Email"
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                capitalization = KeyboardCapitalization.None,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(emailFocusRequester),
            isError = emailError != null,
            supportingText = {
                emailError?.let {
                    Text(it, color = MaterialTheme.colorScheme.error)
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        MyTextField(
            value = password,
            onValueChange = { 
                password = it
                passwordError = null
            },
            label = { Text("Password") },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.lock),
                    contentDescription = "Password"
                )
            },
            trailingIcon = {
                if (password.isNotEmpty()) {
                    IconButton(onClick = {showPassword = !showPassword}) {
                        Icon(if(showPassword) painterResource(R.drawable.eye) else painterResource(R.drawable.eye_off),null
                        )
                    }
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
            visualTransformation = if (showPassword) VisualTransformation.None else
                PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(passwordFocusRequester),
            isError = passwordError != null,
            supportingText = {
                passwordError?.let {
                    Text(it, color = MaterialTheme.colorScheme.error)
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        MyTextField(
            value = confirmPassword,
            onValueChange = { 
                confirmPassword = it
                confirmPasswordError = null
            },
            label = { Text("Confirm Password") },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.lock),
                    contentDescription = "Confirm Password"
                )
            },
            trailingIcon = {
                if (confirmPassword.isNotEmpty()) {
                    IconButton(onClick = {showConfirmPassword = !showConfirmPassword}) {
                        Icon(if(showConfirmPassword) painterResource(R.drawable.eye) else painterResource(R.drawable.eye_off),null
                        )
                    }
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            visualTransformation = if (showConfirmPassword) VisualTransformation.None else
                PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(confirmPasswordFocusRequester),
            isError = confirmPasswordError != null,
            supportingText = {
                confirmPasswordError?.let {
                    Text(it, color = MaterialTheme.colorScheme.error)
                }
            }
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                val passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=]).{8,}$".toRegex()
                var firstFocus: FocusRequester? = null
                
                if (firstName.isBlank()) {
                    firstNameError = "First name is required"
                    firstFocus = firstNameFocusRequester
                } else {
                    firstNameError = null
                }
                
                if (lastName.isBlank()) {
                    lastNameError = "Last name is required"
                    if (firstFocus == null) firstFocus = lastNameFocusRequester
                } else {
                    lastNameError = null
                }
                
                if (email.isBlank()) {
                    emailError = "Email is required"
                    if (firstFocus == null) firstFocus = emailFocusRequester
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailError = "That doesn't look like a valid email address"
                    if (firstFocus == null) firstFocus = emailFocusRequester
                } else {
                    emailError = null
                }
                
                if (password.isBlank()) {
                    passwordError = "Password is required"
                    if (firstFocus == null) firstFocus = passwordFocusRequester
                } else if (password.length < 8) {
                    passwordError = "Password must be at least 8 characters"
                    if (firstFocus == null) firstFocus = passwordFocusRequester
                } else if (!password.matches(passwordPattern)) {
                    passwordError = "Password must contain uppercase, lowercase, number and special character"
                    if (firstFocus == null) firstFocus = passwordFocusRequester
                } else {
                    passwordError = null
                }
                
                if (confirmPassword.isBlank()) {
                    confirmPasswordError = "Please confirm your password"
                    if (firstFocus == null) firstFocus = confirmPasswordFocusRequester
                } else if (password != confirmPassword) {
                    confirmPasswordError = "Passwords do not match"
                    if (firstFocus == null) firstFocus = confirmPasswordFocusRequester
                } else {
                    confirmPasswordError = null
                }
                
                firstFocus?.requestFocus()
                
                if (listOf(firstNameError, lastNameError, emailError, passwordError, confirmPasswordError).all { it == null }) {
                        isLoading = true
                        userViewModel.register(email, password){
                                success, message, userId ->
                            if (success){
                                // Set display name on Firebase Auth
                                (userViewModel.repo as? UserRepoImpl)?.updateDisplayName(firstName)

                                val model = User(
                                    id = userId,
                                    email = email,
                                    firstName = firstName,
                                    lastName = lastName
                                )
                                userViewModel.addUserToDatabase(userId,model){
                                        success,message ->
                                    isLoading = false
                                    if (success){
                                        navController.navigate(Routes.DASHBOARD) {
                                            popUpTo(Routes.LOGIN) { inclusive = true }
                                        }
                                        Toast.makeText(context,
                                            message,
                                            Toast.LENGTH_SHORT).show()
                                    } else {
                                        Toast.makeText(context,
                                            message,
                                            Toast.LENGTH_SHORT).show()
                                    }
                                }
                            } else {
                                isLoading = false
                                Toast.makeText(context,
                                    message,
                                    Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(16.dp),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
            } else {
                Text("Sign Up")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Text("Already have an account?")
            Text("Log in",
                style = TextStyle(
                    color = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier
                    .clickable(onClick = { navController.navigate(Routes.LOGIN) })
                    .padding(4.dp)
            )
        }
    }
}