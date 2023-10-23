package com.ricoh360.thetaclient.capture

import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.Options
import com.ricoh360.thetaclient.transferred.UnknownResponse
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse

/*
 * Capture
 *
 * @property options option of capture
 */
abstract class Capture internal constructor(internal val options: Options) {

    /**
     * Get aperture value.
     *
     * @return aperture value
     */
    fun getAperture() = options.aperture?.let {
        ThetaRepository.ApertureEnum.get(it)
    }

    /**
     * Get color temperature of the camera (Kelvin).
     *
     * @return Color temperature
     */
    fun getColorTemperature() = options._colorTemperature

    /**
     * Get exposure compensation (EV).
     *
     * @return Exposure compensation (EV)
     */
    fun getExposureCompensation() = options.exposureCompensation?.let {
        ThetaRepository.ExposureCompensationEnum.get(it)
    }

    /**
     * Get operating time (sec.) of the self-timer.
     *
     * @return Operating time (sec.) of the self-timer
     */
    fun getExposureDelay() = options.exposureDelay?.let {
        ThetaRepository.ExposureDelayEnum.get(it)
    }

    /**
     * Get exposure program.
     *
     * @return Exposure program
     */
    fun getExposureProgram() = options.exposureProgram?.let {
        ThetaRepository.ExposureProgramEnum.get(it)
    }

    /**
     * Get GPS information.
     *
     * @return GPS information
     */
    fun getGpsInfo() = options.gpsInfo?.let {
        ThetaRepository.GpsInfo(it)
    }

    /**
     * Get turns position information assigning ON/OFF.
     *
     * @return Turns position information assigning ON/OFF
     */
    fun getGpsTagRecording() = options._gpsTagRecording?.let {
        ThetaRepository.GpsTagRecordingEnum.get(it)
    }

    /**
     * Set ISO sensitivity.
     *
     * It can be set for video shooting mode at RICOH THETA V firmware v3.00.1 or later.
     * Shooting settings are retained separately for both the Still image shooting mode and Video shooting mode.
     *
     * When the exposure program (exposureProgram) is set to Manual or ISO Priority
     *
     * @return ISO sensitivity
     */
    fun getIso() = options.iso?.let {
        ThetaRepository.IsoEnum.get(it)
    }

    /**
     * Get ISO sensitivity upper limit when ISO sensitivity is set to automatic.
     *
     * @return ISO sensitivity upper limit
     */
    fun getIsoAutoHighLimit() = options.isoAutoHighLimit?.let {
        ThetaRepository.IsoAutoHighLimitEnum.get(it)
    }

    /**
     * Get white balance.
     *
     * @return White balance
     */
    fun getWhiteBalance() = options.whiteBalance?.let {
        ThetaRepository.WhiteBalanceEnum.get(it)
    }

    /*
     * Builder
     */
    abstract class Builder<T> {
        internal val options = Options()

        /**
         * Set aperture value.
         *
         * @param aperture aperture value
         * @return Builder
         */
        fun setAperture(aperture: ThetaRepository.ApertureEnum): T {
            options.aperture = aperture.value
            @Suppress("UNCHECKED_CAST")
            return this as T
        }

        /**
         * Set color temperature of the camera (Kelvin).
         *
         * 2500 to 10000. In 100-Kelvin units.
         *
         * @param kelvin Color temperature
         * @return Builder
         */
        fun setColorTemperature(kelvin: Int): T {
            options._colorTemperature = kelvin
            @Suppress("UNCHECKED_CAST")
            return this as T
        }

        /**
         * Set exposure compensation (EV).
         *
         * @param value Exposure compensation
         * @return Builder
         */
        fun setExposureCompensation(value: ThetaRepository.ExposureCompensationEnum): T {
            options.exposureCompensation = value.value
            @Suppress("UNCHECKED_CAST")
            return this as T
        }

        /**
         * Set operating time (sec.) of the self-timer.
         *
         * @param delay Operating time (sec.)
         * @return Builder
         */
        fun setExposureDelay(delay: ThetaRepository.ExposureDelayEnum): T {
            options.exposureDelay = delay.sec
            @Suppress("UNCHECKED_CAST")
            return this as T
        }

        /**
         * Set exposure program. The exposure settings that take priority can be selected.
         *
         * @param program Exposure program
         * @return Builder
         */
        fun setExposureProgram(program: ThetaRepository.ExposureProgramEnum): T {
            options.exposureProgram = program.value
            @Suppress("UNCHECKED_CAST")
            return this as T
        }

        /**
         * Set GPS information.
         *
         * @param gpsInfo GPS information
         * @return Builder
         */
        fun setGpsInfo(gpsInfo: ThetaRepository.GpsInfo): T {
            options.gpsInfo = gpsInfo.toTransferredGpsInfo()
            @Suppress("UNCHECKED_CAST")
            return this as T
        }

        /**
         * Set turns position information assigning ON/OFF.
         *
         * For RICOH THETA X
         *
         * @param value Turns position information assigning ON/OFF
         * @return Builder
         */
        fun setGpsTagRecording(value: ThetaRepository.GpsTagRecordingEnum): T {
            options._gpsTagRecording = value.value
            @Suppress("UNCHECKED_CAST")
            return this as T
        }

        /**
         * Set ISO sensitivity.
         *
         * It can be set for video shooting mode at RICOH THETA V firmware v3.00.1 or later.
         * Shooting settings are retained separately for both the Still image shooting mode and Video shooting mode.
         *
         * When the exposure program (exposureProgram) is set to Manual or ISO Priority
         *
         * @param iso ISO sensitivity
         * @return Builder
         */
        fun setIso(iso: ThetaRepository.IsoEnum): T {
            options.iso = iso.value
            @Suppress("UNCHECKED_CAST")
            return this as T
        }

        /**
         * Set ISO sensitivity upper limit when ISO sensitivity is set to automatic.
         *
         * @param iso ISO sensitivity upper limit
         * @return Builder
         */
        fun setIsoAutoHighLimit(iso: ThetaRepository.IsoAutoHighLimitEnum): T {
            options.isoAutoHighLimit = iso.value
            @Suppress("UNCHECKED_CAST")
            return this as T
        }

        /**
         * Set white balance.
         *
         * It can be set for video shooting mode at RICOH THETA V firmware v3.00.1 or later.
         * Shooting settings are retained separately for both the Still image shooting mode and Video shooting mode.
         *
         * @param whiteBalance White balance
         * @return Builder
         */
        fun setWhiteBalance(whiteBalance: ThetaRepository.WhiteBalanceEnum): T {
            options.whiteBalance = whiteBalance.value
            @Suppress("UNCHECKED_CAST")
            return this as T
        }
    }
}

internal suspend fun isCanceledShootingResponse(httpResponse: HttpResponse): Boolean {
    try {
        val response: UnknownResponse = httpResponse.body()
        if (response.error?.isCanceledShootingCode() == true) {
            return true
        }
    } catch (_: Exception) {
    }
    return false
}
