package com.fitness.fitsplit.repository.split

import com.fitness.fitsplit.model.Day
import com.fitness.fitsplit.model.Exercise
import com.fitness.fitsplit.model.WorkoutLog
import com.fitness.fitsplit.model.WorkoutSplit

interface SplitRepo {
    fun addSplit(
        split: WorkoutSplit,
        days: List<Day>,
        exercises: Map<Int, List<Exercise>>,
        callback: (Boolean, String) -> Unit
    )

    fun getAllSplits(
        userId: String,
        callback: (Boolean, String, List<WorkoutSplit>) -> Unit
    )

    fun deleteSplit(
        splitId: String,
        callback: (Boolean, String) -> Unit
    )

    fun getDaysForSplit(
        splitId: String,
        callback: (Boolean, String, List<Day>) -> Unit
    )

    fun getExercisesForDay(
        dayId: String,
        callback: (Boolean, String, List<Exercise>) -> Unit
    )

    fun saveWorkoutLog(
        log: WorkoutLog,
        callback: (Boolean, String) -> Unit
    )

    fun getWorkoutLog(
        userId: String,
        date: String,
        callback: (Boolean, String, WorkoutLog?) -> Unit
    )

    fun getAllWorkoutLogs(
        userId: String,
        callback: (Boolean, String, List<WorkoutLog>) -> Unit
    )
}
