package com.fitness.fitsplit.viewModel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.fitness.fitsplit.model.Day
import com.fitness.fitsplit.model.Exercise
import com.fitness.fitsplit.model.WorkoutLog
import com.fitness.fitsplit.model.WorkoutSplit
import com.fitness.fitsplit.repository.split.SplitRepo
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class SplitViewModel(val repo: SplitRepo) : ViewModel() {

    // Wizard state
    var splitName = mutableStateOf("")
    var numberOfDays = mutableStateOf("")
    var selectedDays = mutableStateListOf<String>()
    var dayNames = mutableStateMapOf<String, String>()
    var dayExercises = mutableStateMapOf<String, MutableList<String>>()

    // Dashboard state
    var splits = mutableStateOf<List<WorkoutSplit>>(emptyList())
    var isLoading = mutableStateOf(false)
    var hasSplits = mutableStateOf(false)
    var splitsChecked = mutableStateOf(false)

    // Today's workout state
    var todayDay = mutableStateOf<Day?>(null)
    var todayExercises = mutableStateOf<List<Exercise>>(emptyList())
    var todayLoading = mutableStateOf(false)
    var todaySplitName = mutableStateOf("")
    var todaySplitId = mutableStateOf("")

    // Active split tracking
    var activeSplitId = mutableStateOf<String?>(null)

    // Workout log state
    var todayLog = mutableStateOf<WorkoutLog?>(null)
    var logLoading = mutableStateOf(false)

    companion object {
        private val DAY_ORDER = listOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
    }

    fun resetWizard() {
        splitName.value = ""
        numberOfDays.value = ""
        selectedDays.clear()
        dayNames.clear()
        dayExercises.clear()
    }

    fun toggleDay(day: String) {
        val maxDays = numberOfDays.value.toIntOrNull() ?: 0
        if (selectedDays.contains(day)) {
            selectedDays.remove(day)
            dayNames.remove(day)
            dayExercises.remove(day)
        } else if (selectedDays.size < maxDays) {
            selectedDays.add(day)
        }
    }

    fun canSelectMoreDays(): Boolean {
        val maxDays = numberOfDays.value.toIntOrNull() ?: 0
        return selectedDays.size < maxDays
    }

    fun getOrderedSelectedDays(): List<String> {
        return selectedDays.sortedBy { DAY_ORDER.indexOf(it) }
    }

    // Exercise management for wizard
    fun addExercise(day: String) {
        val list = dayExercises.getOrPut(day) { mutableListOf() }
        list.add("")
        dayExercises[day] = list.toMutableList()
    }

    fun updateExercise(day: String, index: Int, name: String) {
        val list = dayExercises[day] ?: return
        if (index < list.size) {
            list[index] = name
            dayExercises[day] = list.toMutableList()
        }
    }

    fun removeExercise(day: String, index: Int) {
        val list = dayExercises[day] ?: return
        if (index < list.size) {
            list.removeAt(index)
            dayExercises[day] = list.toMutableList()
        }
    }

    fun saveSplit(
        userId: String,
        callback: (Boolean, String) -> Unit
    ) {
        val orderedDays = getOrderedSelectedDays()

        val split = WorkoutSplit(
            userId = userId,
            name = splitName.value.trim(),
            numberOfDays = numberOfDays.value.toIntOrNull() ?: 0,
            selectedDays = orderedDays
        )

        val days = orderedDays.mapIndexed { index, dayOfWeek ->
            Day(
                dayNumber = index + 1,
                dayName = dayNames[dayOfWeek]?.trim() ?: dayOfWeek,
                dayOfWeek = dayOfWeek,
                order = index
            )
        }

        val exercisesMap = mutableMapOf<Int, List<Exercise>>()
        orderedDays.forEachIndexed { index, dayOfWeek ->
            val names = dayExercises[dayOfWeek]?.filter { it.isNotBlank() } ?: emptyList()
            if (names.isNotEmpty()) {
                exercisesMap[index] = names.mapIndexed { exIndex, exName ->
                    Exercise(
                        name = exName.trim(),
                        order = exIndex
                    )
                }
            }
        }

        repo.addSplit(split, days, exercisesMap) { success, message ->
            if (success) {
                loadSplits(userId)
            }
            callback(success, message)
        }
    }

    fun loadSplits(
        userId: String,
        callback: (Boolean, String) -> Unit = { _, _ -> }
    ) {
        isLoading.value = true
        repo.getAllSplits(userId) { success, message, splitList ->
            splits.value = splitList
            hasSplits.value = splitList.isNotEmpty()
            splitsChecked.value = true
            isLoading.value = false

            // Auto-activate if only one split and none active
            if (splitList.size == 1 && activeSplitId.value == null) {
                splitList.first().id?.let { setActiveSplit(userId, it) }
            }

            callback(success, message)
        }
    }

    fun deleteSplit(
        splitId: String,
        userId: String,
        callback: (Boolean, String) -> Unit
    ) {
        repo.deleteSplit(splitId) { success, message ->
            if (success) {
                loadSplits(userId)
            }
            callback(success, message)
        }
    }

    fun getTodayDayOfWeek(): String {
        val calendar = Calendar.getInstance()
        return when (calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.SUNDAY -> "Sunday"
            Calendar.MONDAY -> "Monday"
            Calendar.TUESDAY -> "Tuesday"
            Calendar.WEDNESDAY -> "Wednesday"
            Calendar.THURSDAY -> "Thursday"
            Calendar.FRIDAY -> "Friday"
            Calendar.SATURDAY -> "Saturday"
            else -> "Sunday"
        }
    }

    fun getTodayDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Calendar.getInstance().time)
    }

    fun loadTodayWorkout(userId: String) {
        todayLoading.value = true
        val today = getTodayDayOfWeek()

        val activeSplit = if (activeSplitId.value != null) {
            splits.value.firstOrNull { it.id == activeSplitId.value }
        } else {
            splits.value.firstOrNull { it.selectedDays.contains(today) }
        }

        if (activeSplit == null) {
            todayDay.value = null
            todayExercises.value = emptyList()
            todaySplitName.value = ""
            todaySplitId.value = ""
            todayLoading.value = false
            return
        }

        todaySplitName.value = activeSplit.name ?: ""
        todaySplitId.value = activeSplit.id ?: ""

        activeSplit.id?.let { splitId ->
            repo.getDaysForSplit(splitId) { success, _, days ->
                if (success) {
                    val dayForToday = days.firstOrNull { it.dayOfWeek == today }
                    todayDay.value = dayForToday

                    if (dayForToday?.id != null) {
                        repo.getExercisesForDay(dayForToday.id) { exSuccess, _, exercises ->
                            todayExercises.value = if (exSuccess) exercises else emptyList()
                            todayLoading.value = false
                            // Load today's workout log
                            loadTodayLog(userId)
                        }
                    } else {
                        todayExercises.value = emptyList()
                        todayLoading.value = false
                    }
                } else {
                    todayDay.value = null
                    todayExercises.value = emptyList()
                    todayLoading.value = false
                }
            }
        } ?: run {
            todayLoading.value = false
        }
    }

    fun loadTodayLog(userId: String) {
        val date = getTodayDate()
        repo.getWorkoutLog(userId, date) { _, _, log ->
            todayLog.value = log
        }
    }

    fun markWorkoutDone(
        userId: String,
        note: String? = null,
        callback: (Boolean, String) -> Unit
    ) {
        val log = WorkoutLog(
            userId = userId,
            splitId = todaySplitId.value,
            dayId = todayDay.value?.id,
            date = getTodayDate(),
            completed = true,
            note = note?.takeIf { it.isNotBlank() }
        )
        repo.saveWorkoutLog(log) { success, message ->
            if (success) {
                todayLog.value = log
            }
            callback(success, message)
        }
    }

    fun saveNote(
        userId: String,
        note: String,
        callback: (Boolean, String) -> Unit
    ) {
        val existingLog = todayLog.value
        val log = WorkoutLog(
            userId = userId,
            splitId = todaySplitId.value,
            dayId = todayDay.value?.id,
            date = getTodayDate(),
            completed = existingLog?.completed ?: false,
            note = note.takeIf { it.isNotBlank() }
        )
        repo.saveWorkoutLog(log) { success, message ->
            if (success) {
                todayLog.value = log
            }
            callback(success, message)
        }
    }

    fun setActiveSplit(userId: String, splitId: String, callback: (Boolean) -> Unit = {}) {
        activeSplitId.value = splitId
        FirebaseDatabase.getInstance().getReference("users")
            .child(userId).child("activeSplitId").setValue(splitId)
            .addOnCompleteListener { callback(it.isSuccessful) }
    }

    fun loadActiveSplit(userId: String) {
        FirebaseDatabase.getInstance().getReference("users")
            .child(userId).child("activeSplitId")
            .get().addOnSuccessListener { snapshot ->
                activeSplitId.value = snapshot.getValue(String::class.java)
            }
    }
}
