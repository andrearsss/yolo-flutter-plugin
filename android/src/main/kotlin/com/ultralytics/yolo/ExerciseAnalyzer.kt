package com.ultralytics.yolo

import kotlin.math.*
import android.graphics.PointF
import com.ultralytics.yolo.AngleState.*
import com.ultralytics.yolo.Exercise.*

enum class Exercise {
    SQUAT
    //PULL_UP,
    //PUSH_UP,
    //DEADLIFT,
    //BICEP_CURL
}

enum class AngleState {
    NEUTRAL,
    DEEP_BEND,
    EXTENDED
}

object ExerciseAnalyzer {
    
    // Static state variables
    private var skipFrames = 0
    private var currentExercise: Exercise? = null
    private var repCount = 0
    private var lastAngleState = NEUTRAL
    
    data class ExerciseResult(
        val angle: Float,
        val shouldSkipFrame: Boolean,
        val repDetected: Boolean,
        val repCount: Int,
        val lastAngleState: AngleState
    )
    
// Main calculation function for exercise analysis
fun calculateExercise(exercise: Exercise, keypoints: List<PointF>): ExerciseResult {
    currentExercise = exercise

    // Skip some frames for stability
    if (skipFrames > 0) {
        skipFrames--
        return ExerciseResult(0f, true, false, repCount, lastAngleState)
    }

    return when (exercise) {
        Exercise.SQUAT -> calculateSquat(keypoints)
    }
}

/**
 * Squat analysis: Hip-Knee-Ankle angle
 * Keypoints: 0=hip, 1=knee, 2=ankle
 */
private fun calculateSquat(keypoints: List<PointF>): ExerciseResult {
    if (keypoints.size < 3) {
        return ExerciseResult(-1f, true, false, repCount, lastAngleState)
    }

    val hipKneeAnkleAngle = calculateAngle(keypoints[0], keypoints[1], keypoints[2])
    var repDetected = false

    when {
        hipKneeAnkleAngle < 90f -> {
            // Deep squat position
            if (lastAngleState != DEEP_BEND) {
                lastAngleState = DEEP_BEND
            }
        }
        hipKneeAnkleAngle > 150f -> {
            // Standing/extended position
            if (lastAngleState == DEEP_BEND) {
                repDetected = true
                repCount++
            }
            lastAngleState = EXTENDED
        }
        else -> {
            lastAngleState = NEUTRAL
        }
    }
    skipFrames = 10 // Skip frames for stability

    return ExerciseResult(
        hipKneeAnkleAngle,
        false,
        repDetected,
        repCount,
        lastAngleState
    )
}

    // Reset all static state variables
    fun reset() {
        skipFrames = 0
        currentExercise = null
        repCount = 0
        lastAngleState = NEUTRAL
    }

    fun getRepCount(): Int = repCount
    
    fun getRemainingSkipFrames(): Int = skipFrames
    
    fun isSkippingFrames(): Boolean = skipFrames > 0
}