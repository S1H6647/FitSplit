package com.fitness.fitsplit.viewModel

import com.fitness.fitsplit.repository.split.SplitRepo
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class SplitViewModelTest {

    private lateinit var splitRepo: SplitRepo
    private lateinit var splitViewModel: SplitViewModel

    @Before
    fun setUp() {
        splitRepo = mock()
        splitViewModel = SplitViewModel(splitRepo)
    }

    @Test
    fun `test resetWizard clears all state`() {
        splitViewModel.splitName.value = "Push"
        splitViewModel.numberOfDays.value = "3"
        splitViewModel.selectedDays.add("Monday")
        splitViewModel.dayNames["Monday"] = "Push"
        splitViewModel.dayExercises["Monday"] = mutableListOf("Bench")

        splitViewModel.resetWizard()

        assertEquals("", splitViewModel.splitName.value)
        assertEquals("", splitViewModel.numberOfDays.value)
        assertTrue(splitViewModel.selectedDays.isEmpty())
        assertTrue(splitViewModel.dayNames.isEmpty())
        assertTrue(splitViewModel.dayExercises.isEmpty())
    }

    @Test
    fun `test deleteSplit calls repository`() {
        val splitId = "split_123"
        val userId = "user_abc"

        splitViewModel.deleteSplit(splitId, userId) { _, _ -> }

        verify(splitRepo).deleteSplit(eq(splitId), any())
    }
}
