package com.fitness.fitsplit.viewModel

import com.fitness.fitsplit.repository.user.UserRepo
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class UserViewModelTest {

    @Mock
    private lateinit var userRepo: UserRepo

    private lateinit var userViewModel: UserViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        userViewModel = UserViewModel(userRepo)
    }

    @Test
    fun `test login calls repository logic`() {
        val email = "test@example.com"
        val password = "password123"
        val callback: (Boolean, String) -> Unit = { _, _ -> }

        userViewModel.login(email, password, callback)

        verify(userRepo).login(email, password, callback)
    }

    @Test
    fun `test register calls repository logic`() {
        val email = "test@example.com"
        val password = "password123"
        val callback: (Boolean, String, String) -> Unit = { _, _, _ -> }

        userViewModel.register(email, password, callback)

        verify(userRepo).register(email, password, callback)
    }
}
