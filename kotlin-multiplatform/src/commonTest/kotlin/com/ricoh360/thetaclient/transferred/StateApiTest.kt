package com.ricoh360.thetaclient.transferred

import kotlinx.coroutines.test.runTest
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalSerializationApi::class)
class StateApiTest {
    @BeforeTest
    fun setup() {
    }

    @AfterTest
    fun teardown() {
    }

    val js = Json {
        encodeDefaults = true // Encode properties with default value.
        explicitNulls = false // Don't encode properties with null value.
        ignoreUnknownKeys = true // Ignore unknown keys on decode.
    }

    @Test
    fun serializeCaptureStatus() = runTest {
        val data = listOf(
            Pair(CaptureStatus.UNKNOWN, "unknown value"),
            Pair(CaptureStatus.SHOOTING, "shooting"),
            Pair(CaptureStatus.IDLE, "idle"),
            Pair(CaptureStatus.SELF_TIMER_COUNTDOWN, "self-timer countdown"),
            Pair(CaptureStatus.BRACKET_SHOOTING, "bracket shooting"),
            Pair(CaptureStatus.CONVERTING, "converting"),
            Pair(CaptureStatus.TIME_SHIFT_SHOOTING, "timeShift shooting"),
            Pair(CaptureStatus.CONTINUOUS_SHOOTING, "continuous shooting"),
            Pair(CaptureStatus.RETROSPECTIVE_IMAGE_RECORDING, "retrospective image recording"),
            Pair(CaptureStatus.BURST_SHOOTING, "burst shooting"),
            Pair(CaptureStatus.TIME_SHIFT_SHOOTING_IDLE, "timeShift shooting idle"),
        )

        @Serializable
        data class Dummy(
            val value: CaptureStatus,
        )

        data.forEach {
            val jsonString = """
                {
                    "value": "${it.second}"
                }
            """.trimIndent()
            val dummy = js.decodeFromString<Dummy>(jsonString)
            assertEquals(dummy.value, it.first, "CaptureStatus: ${it.first.name}")

            val encoded = js.encodeToString(dummy)
            val dist = js.decodeFromString<Dummy>(encoded)
            assertEquals(dist.value, it.first, "CaptureStatus: ${it.first.name}")
        }
    }
}