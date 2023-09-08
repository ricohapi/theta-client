/*
 * [camera._setBluetoothDevice](https://github.com/ricohapi/theta-api-specs/blob/main/theta-web-api-v2.1/commands/camera._set_bluetooth_device.md)
 */
package com.ricoh360.thetaclient.transferred

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
internal data class SetBluetoothDeviceRequest(
    override val name: String = "camera._setBluetoothDevice",
    override val parameters: SetBluetoothDeviceParams,
) : CommandApiRequest

@Serializable
internal data class SetBluetoothDeviceParams(
    /**
     * Format: xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx
     * Alphabetic letters are not case-sensitive.
     */
    val uuid: String,
)

@Serializable
internal data class SetBluetoothDeviceResponse(
    /**
     * Executed command
     */
    override val name: String,

    /**
     * Command execution status
     * @see CommandState
     */
    override val state: CommandState,

    /**
     * Command ID used to check the execution status with
     * Commands/Status
     */
    override val id: String? = null,

    /**
     * Results when each command is successfully executed.  This
     * output occurs in state "done"
     */
    override val results: ResultSetBluetoothDevice? = null,

    /**
     * Error information (See Errors for details).  This output occurs
     * in state "error"
     */
    override val error: CommandError? = null,

    /**
     * Progress information.  This output occurs in state
     * "inProgress"
     */
    override val progress: CommandProgress? = null,
) : CommandApiResponse

@Serializable
internal data class ResultSetBluetoothDevice(
    /**
     * Device name generated from the serial number (S/N) of the Theta.
     * Eg. "00101234" or "THETAXS00101234" when the serial number (S/N) is "XS00101234"
     */
    val deviceName: String,
)


