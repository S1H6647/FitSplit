package com.fitness.fitsplit.repository.split

import com.fitness.fitsplit.model.Day
import com.fitness.fitsplit.model.Exercise
import com.fitness.fitsplit.model.WorkoutLog
import com.fitness.fitsplit.model.WorkoutSplit
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SplitRepoImpl : SplitRepo {
    private val database = FirebaseDatabase.getInstance()
    private val splitsRef = database.getReference("splits")
    private val daysRef = database.getReference("days")
    private val exercisesRef = database.getReference("exercises")
    private val workoutLogsRef = database.getReference("workoutLogs")

    override fun addSplit(
        split: WorkoutSplit,
        days: List<Day>,
        exercises: Map<Int, List<Exercise>>,
        callback: (Boolean, String) -> Unit
    ) {
        val splitKey = splitsRef.push().key
        if (splitKey == null) {
            callback(false, "Failed to generate split ID")
            return
        }

        val splitWithId = split.copy(id = splitKey)
        splitsRef.child(splitKey).setValue(splitWithId.toMap())
            .addOnCompleteListener { response ->
                if (response.isSuccessful) {
                    if (days.isEmpty()) {
                        callback(true, "Split created successfully")
                        return@addOnCompleteListener
                    }

                    // Count total leaf operations:
                    // - days with exercises: count only their exercises
                    // - days without exercises: count the day itself
                    var totalOps = 0
                    days.forEachIndexed { index, _ ->
                        val dayExCount = exercises[index]?.size ?: 0
                        totalOps += if (dayExCount > 0) dayExCount else 1
                    }

                    var completedOps = 0
                    var hasFailed = false

                    fun checkCompletion() {
                        completedOps++
                        if (completedOps >= totalOps) {
                            if (hasFailed) {
                                callback(false, "Split created but some items failed to save")
                            } else {
                                callback(true, "Split created successfully")
                            }
                        }
                    }

                    days.forEachIndexed { index, day ->
                        val dayKey = daysRef.push().key ?: return@forEachIndexed
                        val dayWithId = day.copy(id = dayKey, splitId = splitKey)
                        daysRef.child(dayKey).setValue(dayWithId.toMap())
                            .addOnCompleteListener { dayResponse ->
                                if (!dayResponse.isSuccessful) {
                                    hasFailed = true
                                }

                                // Save exercises for this day
                                val dayExercises = exercises[index] ?: emptyList()
                                if (dayExercises.isNotEmpty()) {
                                    dayExercises.forEach { exercise ->
                                        val exKey = exercisesRef.push().key ?: return@forEach
                                        val exWithId = exercise.copy(id = exKey, dayId = dayKey)
                                        exercisesRef.child(exKey).setValue(exWithId.toMap())
                                            .addOnCompleteListener { exResponse ->
                                                if (!exResponse.isSuccessful) {
                                                    hasFailed = true
                                                }
                                                checkCompletion()
                                            }
                                    }
                                } else {
                                    // No exercises — count the day itself
                                    checkCompletion()
                                }
                            }
                    }
                } else {
                    callback(false, "${response.exception?.message}")
                }
            }
    }

    override fun getAllSplits(
        userId: String,
        callback: (Boolean, String, List<WorkoutSplit>) -> Unit
    ) {
        splitsRef.orderByChild("userId").equalTo(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val splits = mutableListOf<WorkoutSplit>()
                    for (child in snapshot.children) {
                        val split = WorkoutSplit(
                            id = child.key,
                            userId = child.child("userId").getValue(String::class.java),
                            name = child.child("name").getValue(String::class.java),
                            numberOfDays = child.child("numberOfDays").getValue(Int::class.java),
                            selectedDays = child.child("selectedDays").children.mapNotNull {
                                it.getValue(String::class.java)
                            },
                            createdAt = child.child("createdAt").getValue(Long::class.java)
                                ?: System.currentTimeMillis()
                        )
                        splits.add(split)
                    }
                    callback(true, "Splits loaded", splits)
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(false, error.message, emptyList())
                }
            })
    }

    override fun deleteSplit(
        splitId: String,
        callback: (Boolean, String) -> Unit
    ) {
        splitsRef.child(splitId).removeValue()
            .addOnCompleteListener { response ->
                if (response.isSuccessful) {
                    // Delete associated days and their exercises
                    daysRef.orderByChild("splitId").equalTo(splitId)
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                for (child in snapshot.children) {
                                    val dayId = child.key ?: continue
                                    // Delete exercises for this day
                                    exercisesRef.orderByChild("dayId").equalTo(dayId)
                                        .addListenerForSingleValueEvent(object : ValueEventListener {
                                            override fun onDataChange(exSnapshot: DataSnapshot) {
                                                for (exChild in exSnapshot.children) {
                                                    exChild.ref.removeValue()
                                                }
                                            }
                                            override fun onCancelled(error: DatabaseError) {}
                                        })
                                    child.ref.removeValue()
                                }
                                callback(true, "Split deleted successfully")
                            }

                            override fun onCancelled(error: DatabaseError) {
                                callback(true, "Split deleted but cleanup failed")
                            }
                        })
                } else {
                    callback(false, "${response.exception?.message}")
                }
            }
    }

    override fun getDaysForSplit(
        splitId: String,
        callback: (Boolean, String, List<Day>) -> Unit
    ) {
        daysRef.orderByChild("splitId").equalTo(splitId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val days = mutableListOf<Day>()
                    for (child in snapshot.children) {
                        val day = Day(
                            id = child.key,
                            splitId = child.child("splitId").getValue(String::class.java),
                            dayNumber = child.child("dayNumber").getValue(Int::class.java),
                            dayName = child.child("dayName").getValue(String::class.java),
                            dayOfWeek = child.child("dayOfWeek").getValue(String::class.java),
                            order = child.child("order").getValue(Int::class.java)
                        )
                        days.add(day)
                    }
                    callback(true, "Days loaded", days.sortedBy { it.order })
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(false, error.message, emptyList())
                }
            })
    }

    override fun getExercisesForDay(
        dayId: String,
        callback: (Boolean, String, List<Exercise>) -> Unit
    ) {
        exercisesRef.orderByChild("dayId").equalTo(dayId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val exercises = mutableListOf<Exercise>()
                    for (child in snapshot.children) {
                        val exercise = Exercise(
                            id = child.key,
                            dayId = child.child("dayId").getValue(String::class.java),
                            name = child.child("name").getValue(String::class.java),
                            sets = child.child("sets").getValue(Int::class.java),
                            reps = child.child("reps").getValue(String::class.java),
                            order = child.child("order").getValue(Int::class.java),
                            notes = child.child("notes").getValue(String::class.java)
                        )
                        exercises.add(exercise)
                    }
                    callback(true, "Exercises loaded", exercises.sortedBy { it.order })
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(false, error.message, emptyList())
                }
            })
    }

    override fun saveWorkoutLog(
        log: WorkoutLog,
        callback: (Boolean, String) -> Unit
    ) {
        // Use date+userId as key to prevent duplicate logs per day
        val logKey = "${log.date}_${log.userId}"
        workoutLogsRef.child(logKey).setValue(log.toMap())
            .addOnCompleteListener { response ->
                if (response.isSuccessful) {
                    callback(true, "Workout logged!")
                } else {
                    callback(false, "${response.exception?.message}")
                }
            }
    }

    override fun getWorkoutLog(
        userId: String,
        date: String,
        callback: (Boolean, String, WorkoutLog?) -> Unit
    ) {
        val logKey = "${date}_${userId}"
        workoutLogsRef.child(logKey)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val log = WorkoutLog(
                            id = snapshot.key,
                            userId = snapshot.child("userId").getValue(String::class.java),
                            splitId = snapshot.child("splitId").getValue(String::class.java),
                            dayId = snapshot.child("dayId").getValue(String::class.java),
                            date = snapshot.child("date").getValue(String::class.java),
                            completed = snapshot.child("completed").getValue(Boolean::class.java) ?: false,
                            note = snapshot.child("note").getValue(String::class.java),
                            timestamp = snapshot.child("timestamp").getValue(Long::class.java)
                                ?: System.currentTimeMillis()
                        )
                        callback(true, "Log found", log)
                    } else {
                        callback(true, "No log", null)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(false, error.message, null)
                }
            })
    }

    override fun getAllWorkoutLogs(
        userId: String,
        callback: (Boolean, String, List<WorkoutLog>) -> Unit
    ) {
        workoutLogsRef.orderByChild("userId").equalTo(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val logs = mutableListOf<WorkoutLog>()
                    for (child in snapshot.children) {
                        val log = WorkoutLog(
                            id = child.key,
                            userId = child.child("userId").getValue(String::class.java),
                            splitId = child.child("splitId").getValue(String::class.java),
                            dayId = child.child("dayId").getValue(String::class.java),
                            date = child.child("date").getValue(String::class.java),
                            completed = child.child("completed").getValue(Boolean::class.java) ?: false,
                            note = child.child("note").getValue(String::class.java),
                            timestamp = child.child("timestamp").getValue(Long::class.java)
                                ?: System.currentTimeMillis()
                        )
                        logs.add(log)
                    }
                    // Sort by date descending (newest first)
                    logs.sortByDescending { it.date }
                    callback(true, "Logs loaded", logs)
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(false, error.message, emptyList())
                }
            })
    }
}
