package com.ricoh360.thetaclient.capture

import com.ricoh360.thetaclient.ThetaApi
import com.ricoh360.thetaclient.transferred.CaptureStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

internal class CaptureStatusMonitor(
    val endpoint: String,
    var onChangeStatus: ((newStatus: CaptureStatus, oldStatus: CaptureStatus?) -> Unit),
    var onError: ((error: Throwable) -> Unit),
    val checkStateInterval: Long = CHECK_STATE_INTERVAL,
    val checkShootingIdleCount: Int = CHECK_SHOOTING_IDLE_COUNT,
) {
    private var isStartMonitor = false
    private val scope = CoroutineScope(Dispatchers.Default)
    var currentStatus: CaptureStatus? = null
    var lastException: Throwable? = null
    var job: Job? = null

    companion object {
        private const val CHECK_STATE_INTERVAL = 1000L
        private const val CHECK_STATE_RETRY = 3
        private const val CHECK_SHOOTING_IDLE_COUNT = 2
    }

    fun start() {
        if (isStartMonitor) {
            return
        }
        isStartMonitor = true
        job = scope.launch {
            var idleCount = checkShootingIdleCount
            while (isStartMonitor) {
                when (val status = getCaptureStatus()) {
                    CaptureStatus.IDLE -> {
                        idleCount -= 1
                        if (idleCount <= 0) {
                            updateStatus(CaptureStatus.IDLE)
                        }
                    }

                    null -> {
                        isStartMonitor = false
                        // Error notification
                    }

                    else -> {
                        idleCount = checkShootingIdleCount
                        updateStatus(status)
                    }
                }
                if (isStartMonitor) {
                    delay(checkStateInterval)
                }
            }
        }
    }

    fun stop() {
        isStartMonitor = false
        job?.let {
            it.cancel()
            job = null
        }
    }

    private fun updateStatus(status: CaptureStatus) {
        if (!isStartMonitor || currentStatus == status) {
            return
        }
        val oldStatus = currentStatus
        currentStatus = status
        onChangeStatus(status, oldStatus)
    }

    private suspend fun getCaptureStatus(): CaptureStatus? {
        var retry = CHECK_STATE_RETRY
        while (retry > 0) {
            try {
                val stateResponse = ThetaApi.callStateApi(endpoint)
                lastException = null
                return stateResponse.state._captureStatus
            } catch (e: Throwable) {
                lastException = e
                delay(checkStateInterval)
            }
            retry -= 1
        }
        lastException?.let {
            onError(it)
        }
        return null
    }
}