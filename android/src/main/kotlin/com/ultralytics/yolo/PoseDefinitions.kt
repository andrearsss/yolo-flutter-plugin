package com.ultralytics.yolo.constants

// Body joints
const val RIGHT_ANKLE = 0
const val RIGHT_KNEE = 1
const val RIGHT_HIP = 2
const val LEFT_HIP = 3
const val LEFT_KNEE = 4
const val LEFT_ANKLE = 5
const val RIGHT_WRIST = 6
const val RIGHT_ELBOW = 7
const val RIGHT_SHOULDER = 8
const val LEFT_SHOULDER = 9
const val LEFT_ELBOW = 10
const val LEFT_WRIST = 11
const val NECK = 12
const val HEAD_TOP = 13


// Constants for posePalette indices
const val ORANGE = 0
const val LIGHT_ORANGE = 1
const val PEACH = 2
const val YELLOW = 3
const val PINK = 4
const val SKY_BLUE = 5
const val MAGENTA = 6
const val PURPLE = 7
const val LIGHT_BLUE = 8
const val BLUE_LIGHT = 9
const val LIGHT_RED = 10
const val RED_ORANGE = 11
const val RED_DEEP = 12
const val LIGHT_GREEN = 13
const val MINT = 14
const val BRIGHT_GREEN = 15
const val GREEN = 16
const val BLUE = 17
const val RED = 18
const val WHITE = 19

// Pose palette colors
val posePalette = arrayOf(
    floatArrayOf(255f, 128f,  0f),   // ORANGE
    floatArrayOf(255f, 153f,  51f),  // LIGHT_ORANGE
    floatArrayOf(255f, 178f, 102f),  // PEACH
    floatArrayOf(230f, 230f,   0f),  // YELLOW
    floatArrayOf(255f, 153f, 255f),  // PINK
    floatArrayOf(153f, 204f, 255f),  // SKY_BLUE
    floatArrayOf(255f, 102f, 255f),  // MAGENTA
    floatArrayOf(255f,  51f, 255f),  // PURPLE
    floatArrayOf(102f, 178f, 255f),  // LIGHT_BLUE
    floatArrayOf( 51f, 153f, 255f),  // BLUE_LIGHT
    floatArrayOf(255f, 153f, 153f),  // LIGHT_RED
    floatArrayOf(255f, 102f, 102f),  // RED_ORANGE
    floatArrayOf(255f,  51f,  51f),  // RED_DEEP
    floatArrayOf(153f, 255f, 153f),  // LIGHT_GREEN
    floatArrayOf(102f, 255f, 102f),  // MINT
    floatArrayOf( 51f, 255f,  51f),  // BRIGHT_GREEN
    floatArrayOf(  0f, 255f,   0f),  // GREEN
    floatArrayOf(  0f,   0f, 255f),  // BLUE
    floatArrayOf(255f,   0f,   0f),  // RED
    floatArrayOf(255f, 255f, 255f),  // WHITE
)

// Keypoint color mapping
val kptColorIndices = intArrayOf(
    BLUE_LIGHT, BLUE_LIGHT, BLUE_LIGHT, BLUE_LIGHT, BLUE_LIGHT, BLUE_LIGHT, // hips down
    ORANGE, ORANGE, ORANGE, ORANGE, ORANGE, ORANGE, // arms and shoulders
    GREEN, GREEN // neck and head
)

// Skeleton connections between joints
val skeleton = arrayOf(
    // legs and hips
    Pair(RIGHT_ANKLE, RIGHT_KNEE),
    Pair(RIGHT_KNEE, RIGHT_HIP),
    Pair(RIGHT_HIP, LEFT_HIP),
    Pair(LEFT_HIP, LEFT_KNEE),
    Pair(LEFT_KNEE, LEFT_ANKLE),
    Pair(LEFT_HIP, LEFT_SHOULDER),
    Pair(RIGHT_HIP, RIGHT_SHOULDER),
    // arms
    Pair(LEFT_SHOULDER, LEFT_ELBOW),
    Pair(LEFT_ELBOW, LEFT_WRIST),
    Pair(RIGHT_SHOULDER, RIGHT_ELBOW),
    Pair(RIGHT_ELBOW, RIGHT_WRIST),
    // head
    Pair(RIGHT_SHOULDER, NECK),
    Pair(LEFT_SHOULDER, NECK),
    Pair(NECK, HEAD_TOP)
)

val defaultLimbColors = intArrayOf(
        BLUE_LIGHT, BLUE_LIGHT, BLUE_LIGHT, BLUE_LIGHT, BLUE_LIGHT, BLUE_LIGHT, BLUE_LIGHT, // shoulders down
        ORANGE, ORANGE, ORANGE, ORANGE,                                                     // arms
        GREEN, GREEN, GREEN                                                                 // clavicles and neck
    )

// Exercises
const val SQUAT = 0
const val PULL_UP = 1
const val PUSH_UP = 2
const val DEADLIFT = 3
const val BICEP_CURL = 4

// Angle states
const val NEUTRAL = 0
const val DEEP_BEND = 1
const val EXTENDED = 2

