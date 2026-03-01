package com.fitness.fitsplit.ui.screens

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.navigation.NavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.fitness.fitsplit.ui.screens.auth.SignupScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SignupScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun signupScreen_displaysExpectedElements() {
        composeTestRule.setContent {
            SignupScreen(navController = NavController(LocalContext.current))
        }

        // Check if the title is displayed
        composeTestRule.onNodeWithText("Welcome to the FitSplit").assertExists()
        
        // Check if important fields exist
        composeTestRule.onNodeWithText("First Name").assertExists()
        composeTestRule.onNodeWithText("Email").assertExists()
    }

    @Test
    fun signupScreen_showsValidationErrorOnEmptySubmit() {
        composeTestRule.setContent {
            SignupScreen(navController = NavController(LocalContext.current))
        }

        // Click signup button without filling fields
        composeTestRule.onNodeWithText("Sign Up").performScrollTo().performClick()
        
        // Verify first name error appears
        composeTestRule.onNodeWithText("First name is required").assertExists()
    }
}
