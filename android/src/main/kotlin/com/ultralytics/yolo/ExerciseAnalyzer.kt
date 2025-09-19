package com.ultralytics.yolo

import kotlin.math.*
import android.graphics.PointF
import com.ultralytics.yolo.constants.*


object ExerciseAnalyzer {
    
    private var skipFrames: Int = 0
    private var currentExercise: Int = SQUAT
    private var repCount: Int = 0
    private var lastRepState: Int = NEUTRAL

    // Default skeleton colors
    private var limbColorIndices: IntArray = intArrayOf(
        BLUE_LIGHT, BLUE_LIGHT, BLUE_LIGHT, BLUE_LIGHT, BLUE_LIGHT, BLUE_LIGHT, BLUE_LIGHT, // shoulders down
        ORANGE, ORANGE, ORANGE, ORANGE,                                                     // arms
        GREEN, GREEN, GREEN                                                                 // clavicles and neck
    )
    
    data class ExerciseResult(
        val feedback: String,
        val repDetected: Boolean,
        val repCount: Int,
        val limbColorIndices: IntArray,
    )

    // Main function for exercise analysis
    fun analyzeKeypoints(keypoints: Array<PointF?>, exercise: Int, fps: Int?): ExerciseResult {
        // Skip frames for detection stability
        if (skipFrames > 0) {
            skipFrames--
            return ExerciseResult("", false, repCount, limbColorIndices)
        }
        currentExercise = exercise
        skipFrames = fps ?: 0 // 1 second
        return when (exercise) {
            SQUAT -> analyzeSquat(keypoints)
            else -> return ExerciseResult("", false, repCount, limbColorIndices)
        }
    }

/*
 * Squat analysis: track knee joint flexion, hip joint flexion, ankle joint dorsiflexion angles and 
                    parallelism of the femur with the ground
 */
private fun analyzeSquat(keypoints: Array<PointF?>): ExerciseResult {
    if (keypoints.size < 4 || 
        keypoints[LEFT_HIP] == null || 
        keypoints[LEFT_KNEE] == null || 
        keypoints[LEFT_ANKLE] == null ||
        keypoints[LEFT_SHOULDER] == null) 
    {
        return ExerciseResult("", false, repCount, limbColorIndices) // todo: handle error
    }

    val hip = keypoints[LEFT_HIP]!!
    val knee = keypoints[LEFT_KNEE]!!
    val ankle = keypoints[LEFT_ANKLE]!!
    val shoulder = keypoints[LEFT_SHOULDER]!!

    val kneeAnkleVertical = angleWithVertical(knee, ankle)
    val kneeAnkleHorizontal = angleWithHorizontal(knee, ankle)
    val kneeHipHorizontal = angleWithHorizontal(knee, hip)
    val hipShoulderHorizontal = angleWithHorizontal(hip, shoulder)

    val invalidStates = mutableSetOf<Int>()

    if (kneeAnkleVertical < 17f) invalidStates.add(KNEE_TOO_CLOSE_TO_VERTICAL)
    if (kneeAnkleVertical > 29f) invalidStates.add(KNEE_TOO_FAR_FROM_VERTICAL)

    if (kneeAnkleHorizontal < 126f) invalidStates.add(KNEE_TOO_FLEXED)
    if (kneeAnkleHorizontal > 140f) invalidStates.add(KNEE_NOT_FLEXED_ENOUGH)

    if (hipShoulderHorizontal < 119f) invalidStates.add(BACK_TOO_UPRIGHT)
    if (hipShoulderHorizontal > 137f) invalidStates.add(BACK_TOO_BENT)

    // Get correction feedback
    val feedback = getSquatCorrectionFeedback(invalidStates)

    // Rep detection
    val limbId = 3
    var repDetected = false
    if (kneeHipHorizontal < 0f && lastRepState != DEEP_BEND) {
        lastRepState = DEEP_BEND
        limbColorIndices[limbId] = GREEN
    } else if (kneeHipHorizontal >= 0f && lastRepState == DEEP_BEND) {
        lastRepState = EXTENDED
        limbColorIndices[limbId] = RED
        repDetected = true
        repCount++
    }

    return ExerciseResult(
        feedback,
        repDetected,
        repCount,
        limbColorIndices
    )
}

fun getSquatCorrectionFeedback(invalidStates: Set<Int>): String {
    val corrections = mapOf(
        KNEE_TOO_CLOSE_TO_VERTICAL to "Push your knees out more!",
        KNEE_TOO_FAR_FROM_VERTICAL to "Keep your knees in a straighter line!",
        KNEE_TOO_FLEXED to "Don't squat so low!",
        KNEE_NOT_FLEXED_ENOUGH to "Squat a bit deeper!",
        BACK_TOO_UPRIGHT to "Lean forward a bit more with your back!",
        BACK_TOO_BENT to "Keep your chest up!"
    )

    if (invalidStates.isEmpty()) {
        return "Your form looks great! Keep it up!"
    }

    val feedbackList = invalidStates.mapNotNull { corrections[it] }
    
    val feedbackStrings = feedbackList.mapIndexed { index, feedback ->
        if (index == feedbackList.lastIndex) {
            feedback
        } else {
            feedback.removeSuffix("!")
        }
    }

    return when (feedbackStrings.size) {
        1 -> feedbackStrings.first()
        2 -> {
            val first = feedbackStrings[0]
            val second = feedbackStrings[1]
            "$first! Also, ${second.replaceFirstChar { it.lowercase() }}"
        }
        else -> {
            val joinedStrings = feedbackStrings.joinToString(", ")
            "Check your form! Let's work on a few things: ${joinedStrings.replaceFirstChar { it.lowercase() }}"
        }
    }
}


    fun reset() {
        skipFrames = 0
        currentExercise = SQUAT
        repCount = 0
        lastRepState = NEUTRAL
    }

    fun getRepCount(): Int = repCount
    
    fun getRemainingSkipFrames(): Int = skipFrames
    
    fun isSkippingFrames(): Boolean = skipFrames > 0
}