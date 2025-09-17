package com.ultralytics.yolo

import kotlin.math.*
import android.graphics.PointF
import com.ultralytics.yolo.constants.*


object ExerciseAnalyzer {
    
    private var skipFrames: Int = 0
    private var currentExercise: Int = SQUAT
    private var repCount: Int = 0
    private var lastAngleState: Int = NEUTRAL

    // Default skeleton colors
    private var limbColorIndices: IntArray = intArrayOf(
        BLUE_LIGHT, BLUE_LIGHT, BLUE_LIGHT, BLUE_LIGHT, BLUE_LIGHT, BLUE_LIGHT, BLUE_LIGHT, // shoulders down
        ORANGE, ORANGE, ORANGE, ORANGE,                                                     // arms
        GREEN, GREEN, GREEN                                                                 // clavicles and neck
    )
    
    data class ExerciseResult(
        val angle: Float,
        val shouldSkipFrame: Boolean,
        val repDetected: Boolean,
        val repCount: Int,
        val limbColorIndices: IntArray,
    )

    // Main function for exercise analysis
    fun analyzeKeypoints(keypoints: Array<PointF?>, exercise: Int, fps: Int?): ExerciseResult {
        // Skip frames for detection stability
        if (skipFrames > 0) {
            skipFrames--
            return ExerciseResult(0f, true, false, repCount, limbColorIndices)
        }
        currentExercise = exercise
        skipFrames = fps ?: 0 // 1 second
        return when (exercise) {
            SQUAT -> calculateSquat(keypoints)
            else -> return ExerciseResult(0f, true, false, repCount, limbColorIndices)
        }
    }

    /*
    * Squat analysis: Hip-Knee-Ankle angle
    */
    private fun calculateSquat(keypoints: Array<PointF?>): ExerciseResult {
        if (keypoints.size < 3 || keypoints[LEFT_HIP] == null || keypoints[LEFT_KNEE] == null || keypoints[LEFT_ANKLE] == null) {
            return ExerciseResult(-1f, true, false, repCount, limbColorIndices)
        }
        val hipKneeAnkleDegrees = calculateAngle(keypoints[LEFT_HIP]!!, keypoints[LEFT_KNEE]!!, keypoints[LEFT_ANKLE]!!)
        val limb_id = 3 // limb to use as feedback
        var repDetected = false
        when {
            hipKneeAnkleDegrees < 90f -> {
                // Deep squat
                if (lastAngleState != DEEP_BEND) {
                    lastAngleState = DEEP_BEND
                    limbColorIndices[limb_id] = GREEN
                }
            }
            hipKneeAnkleDegrees >= 90 -> {
                // Standing
                if (lastAngleState == DEEP_BEND) {
                    lastAngleState = EXTENDED
                    limbColorIndices[limb_id] = RED
                    repDetected = true
                    repCount++
                }
            }
        }

        return ExerciseResult(
            hipKneeAnkleDegrees,
            false,
            repDetected,
            repCount,
            limbColorIndices
        )
    }

    fun reset() {
        skipFrames = 0
        currentExercise = SQUAT
        repCount = 0
        lastAngleState = NEUTRAL
    }

    fun getRepCount(): Int = repCount
    
    fun getRemainingSkipFrames(): Int = skipFrames
    
    fun isSkippingFrames(): Boolean = skipFrames > 0
}