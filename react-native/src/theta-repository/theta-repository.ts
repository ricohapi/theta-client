import { AccessPoint, AuthModeEnum } from './access-point';
import type { FileTypeEnum, StorageEnum, ThetaFiles } from './theta-files';
import type { ThetaState } from './theta-state';
import type { ThetaInfo, ThetaModel } from './theta-info';
import type { MetaInfo } from './theta-meta';
import type { PluginInfo } from './theta-plugin';

import { NativeModules } from 'react-native';
import type {
  OptionNameEnum,
  Options,
  CaptureModeEnum,
  Proxy,
  BurstCaptureNumEnum,
  BurstBracketStepEnum,
  BurstCompensationEnum,
  BurstMaxExposureTimeEnum,
  BurstEnableIsoControlEnum,
  BurstOrderEnum,
} from './options';
import {
  PhotoCaptureBuilder,
  VideoCaptureBuilder,
  TimeShiftCaptureBuilder,
  LimitlessIntervalCaptureBuilder,
  ShotCountSpecifiedIntervalCaptureBuilder,
  CompositeIntervalCaptureBuilder,
  BurstCaptureBuilder,
  ContinuousCaptureBuilder,
  MultiBracketCaptureBuilder,
} from '../capture';
import type { ThetaConfig } from './theta-config';
import type { ThetaTimeout } from './theta-timeout';
import { NotifyController } from './notify-controller';
import { EventWebSocket } from './event-websocket';
import { convertOptions, convertVideoFormatsImpl } from './libs';
const ThetaClientReactNative = NativeModules.ThetaClientReactNative;

/**
 * initialize theta client sdk
 *
 * @function initialize
 * @param {string} endPoint optional endpoint of camera
 * @param {ThetaConfig} config Configuration of initialize. If null, get from THETA.
 * @param {ThetaTimeout} timeout Timeout of HTTP call.
 * @return promise of boolean result
 **/
export function initialize(
  endPoint: string = 'http://192.168.1.1',
  config?: ThetaConfig,
  timeout?: ThetaTimeout
): Promise<boolean> {
  NotifyController.instance.init();

  return ThetaClientReactNative.initialize(endPoint, config, timeout);
}

/**
 * Returns whether it is initialized or not.
 *
 * @function isInitialized
 * @return promise of boolean result
 **/
export function isInitialized(): Promise<boolean> {
  return ThetaClientReactNative.isInitialized();
}

/**
 * Reset all device settings and capture settings.
 * After reset, the camera will be restarted.
 *
 * @function reset
 * @return promise of boolean result
 */
export function reset(): Promise<boolean> {
  return ThetaClientReactNative.reset();
}

/**
 * Get PhotoCaptureBuilder for take a picture.
 *
 * @function getPhotoCaptureBuilder
 * @return created PhotoCaptureBuilder
 */
export function getPhotoCaptureBuilder(): PhotoCaptureBuilder {
  return new PhotoCaptureBuilder();
}

/**
 * Get TimeShiftCaptureBuilder for time-shift.
 *
 * @function getTimeShiftCaptureBuilder
 * @return created TimeShiftCaptureBuilder
 */
export function getTimeShiftCaptureBuilder(): TimeShiftCaptureBuilder {
  return new TimeShiftCaptureBuilder();
}

/**
 * Get VideoCapture.Builder for capture video.
 *
 * @function getVideoCaptureBuilder
 * @return created VideoCaptureBuilder instance
 */
export function getVideoCaptureBuilder(): VideoCaptureBuilder {
  return new VideoCaptureBuilder();
}

/**
 * Get LimitlessIntervalCapture.Builder for take limitless interval shooting.
 *
 * @function getLimitlessIntervalCaptureBuilder
 * @return created LimitlessIntervalCaptureBuilder instance
 */
export function getLimitlessIntervalCaptureBuilder(): LimitlessIntervalCaptureBuilder {
  return new LimitlessIntervalCaptureBuilder();
}

/**
 * Get ShotCountSpecifiedIntervalCapture.Builder for take interval shooting with the shot count specified.
 *
 * @function getShotCountSpecifiedIntervalCaptureBuilder
 * @param {shotCount} shot count specified
 * @return created ShotCountSpecifiedIntervalCaptureBuilder instance
 */
export function getShotCountSpecifiedIntervalCaptureBuilder(
  shotCount: number
): ShotCountSpecifiedIntervalCaptureBuilder {
  return new ShotCountSpecifiedIntervalCaptureBuilder(shotCount);
}

/**
 * Get CompositeIntervalCapture.Builder for take interval composite shooting.
 *
 * @function getCompositeIntervalCaptureBuilder
 * @param {shootingTimeSec} Shooting time for interval composite shooting (sec)
 * @return created CompositeIntervalCaptureBuilder instance
 */
export function getCompositeIntervalCaptureBuilder(
  shootingTimeSec: number
): CompositeIntervalCaptureBuilder {
  return new CompositeIntervalCaptureBuilder(shootingTimeSec);
}

/**
 * Get BurstCapture.Builder for take burst shooting.
 *
 * @function getBurstCaptureBuilder
 * @param burstOption Burst shooting setting.
 * @return created BurstCaptureBuilder instance
 */
export function getBurstCaptureBuilder(
  burstCaptureNum: BurstCaptureNumEnum,
  burstBracketStep: BurstBracketStepEnum,
  burstCompensation: BurstCompensationEnum,
  burstMaxExposureTime: BurstMaxExposureTimeEnum,
  burstEnableIsoControl: BurstEnableIsoControlEnum,
  burstOrder: BurstOrderEnum
): BurstCaptureBuilder {
  return new BurstCaptureBuilder(
    burstCaptureNum,
    burstBracketStep,
    burstCompensation,
    burstMaxExposureTime,
    burstEnableIsoControl,
    burstOrder
  );
}

/**
 * Get MultiBracketCapture.Builder for take multi bracket shooting.
 *
 * @function getMultiBracketCaptureBuilder
 * @return created MultiBracketCaptureBuilder instance
 */
export function getMultiBracketCaptureBuilder(): MultiBracketCaptureBuilder {
  return new MultiBracketCaptureBuilder();
}

/**
 * Get ContinuousCapture.Builder for take limitless interval shooting.
 *
 * @function getContinuousCaptureBuilder
 * @return created ContinuousCaptureBuilder instance
 */
export function getContinuousCaptureBuilder(): ContinuousCaptureBuilder {
  return new ContinuousCaptureBuilder();
}

/**
 * Start live preview.
 * preview frame (JPEG DataURL) send THETA_EVENT_NAME event as
 * {
 *   "data": "DataURL of JPEG Frame"
 * }
 *
 * @function StartLivePreview
 * @return promise of boolean result
 */
export function getLivePreview(): Promise<boolean> {
  return ThetaClientReactNative.getLivePreview();
}

/**
 * Stop live preview.
 *
 * @function StopLivePreview
 * @return promise of boolean result
 */
export function stopLivePreview(): Promise<boolean> {
  return ThetaClientReactNative.stopLivePreview();
}

/**
 * Restore setting to THETA
 *
 * @function restoreSettings
 * @return promise of boolean result
 */
export function restoreSettings(): Promise<boolean> {
  return ThetaClientReactNative.restoreSettings();
}

/**
 * Stop running self-timer.
 *
 * @function stopSelfTimer
 * @return promise of boolean result
 */
export function stopSelfTimer(): Promise<boolean> {
  return ThetaClientReactNative.stopSelfTimer();
}

/**
 * Converts the movie format of a saved movie.
 *
 * @function convertVideoFormats
 * @param {string} fileUrl URL of a saved movie file to convert
 * @param {boolean} toLowResolution If true generates lower resolution
 *   video, otherwise same resolution.
 * @param {boolean} applyTopBottomCorrection apply Top/bottom
 *   correction. This parameter is ignored on Theta X.
 * @param onProgress the block for convertVideoFormats progress
 * @return promise of URL of a converted movie file.
 */
export function convertVideoFormats(
  fileUrl: string,
  toLowResolution: boolean,
  applyTopBottomCorrection: boolean,
  onProgress?: (completion: number) => void
): Promise<string> {
  return convertVideoFormatsImpl(
    fileUrl,
    toLowResolution,
    applyTopBottomCorrection,
    onProgress
  );
}

/**
 * Cancels the movie format conversion.
 *
 * @function cancelVideoConvert
 * @return promise of boolean result
 */
export function cancelVideoConvert(): Promise<boolean> {
  return ThetaClientReactNative.cancelVideoConvert();
}

/**
 * Turns the wireless LAN off.
 *
 * @function finishWlan
 * @return promise of boolean result
 */
export function finishWlan(): Promise<boolean> {
  return ThetaClientReactNative.finishWlan();
}

/**
 * Registers identification information (UUID) of a BLE device.
 *
 * @function setBluetoothDevice
 * @param {string} uuid UUID of the BLE device to set.
 * @return promise of String result
 */
export function setBluetoothDevice(uuid: string): Promise<String> {
  return ThetaClientReactNative.setBluetoothDevice(uuid);
}

/**
 * Returns the connected THETA model.
 *
 * @function getThetaModel
 * @return promise of THETA model
 **/
export function getThetaModel(): Promise<ThetaModel | undefined> {
  return new Promise<ThetaModel | undefined>(async (resolve, reject) => {
    await ThetaClientReactNative.getThetaModel()
      .then((result?: string) => {
        resolve((result as ThetaModel) ?? undefined);
      })
      .catch((error: any) => {
        reject(error);
      });
  });
}

/**
 * Get basic information about Theta.
 *
 * @function getThetaInfo
 * @return promise of ThetaInfo
 **/
export function getThetaInfo(): Promise<ThetaInfo> {
  return ThetaClientReactNative.getThetaInfo();
}

/**
 * Acquires open source license information related to the camera.
 *
 * @function getThetaLicense
 * @return Promise of string result, HTML string of the license.
 */
export function getThetaLicense(): Promise<string> {
  return ThetaClientReactNative.getThetaLicense();
}

/**
 * Get current state of Theta.
 *
 * @function getThetaState
 * @return promise of ThetaState
 **/
export function getThetaState(): Promise<ThetaState> {
  return ThetaClientReactNative.getThetaState();
}

/**
 * Lists information of images and videos in Theta.
 *
 * @function listFiles
 * @param {FileTypeEnum} fileType Type of the files to be listed.
 * @param {number} startPosition The position of the first file to be
 *   returned in the list. 0 represents the first file.  If
 *   startPosition is larger than the position of the last file, an
 *   empty list is returned.
 * @param {number} entryCount Desired number of entries to return.  If
 *   entryCount is more than the number of remaining files, just
 *   return entries of actual remaining files.
 * @param {StorageEnum} storage Desired storage. If omitted, return current storage. (RICOH THETA X Version 2.00.0 or later)
 * @return promise with a list of file information and number of totalEntries.
 *   see [camera.listFiles](https://github.com/ricohapi/theta-api-specs/blob/main/theta-web-api-v2.1/commands/camera.list_files.md).
 */
export function listFiles(
  fileTypeEnum: FileTypeEnum,
  startPosition: number = 0,
  entryCount: number,
  storage?: StorageEnum
): Promise<ThetaFiles> {
  return ThetaClientReactNative.listFiles(
    fileTypeEnum,
    startPosition,
    entryCount,
    storage
  );
}

/**
 * Delete files in Theta.
 *
 * @function deleteFiles
 * @param {string[]} fileUrls URLs of the file to be deleted.
 * @return promise of boolean result
 */
export function deleteFiles(fileUrls: string[]): Promise<boolean> {
  return ThetaClientReactNative.deleteFiles(fileUrls);
}

/**
 * Delete all files in Theta.
 *
 * @function deleteAllFiles
 * @return promise of boolean result
 */
export function deleteAllFiles(): Promise<boolean> {
  return ThetaClientReactNative.deleteAllFiles();
}

/**
 * Delete all image files in Theta.
 *
 * @function deleteAllImageFiles
 * @return promise of boolean result
 */
export function deleteAllImageFiles(): Promise<boolean> {
  return ThetaClientReactNative.deleteAllImageFiles();
}

/**
 * Delete all video files in Theta.
 *
 * @function deleteAllVideoFiles
 * @return promise of boolean result
 */
export function deleteAllVideoFiles(): Promise<boolean> {
  return ThetaClientReactNative.deleteAllVideoFiles();
}

/**
 * Acquires the properties and property support specifications for
 * shooting, the camera, etc.
 *
 * @function getOptions
 * @param {OptionNameEnum[]} optionNames List of OptionNameEnum.
 * @return promise of Options acquired
 */
export async function getOptions(
  optionNames: OptionNameEnum[]
): Promise<Options> {
  const response = await ThetaClientReactNative.getOptions(optionNames);
  const { options, json } = response;
  const result = convertOptions(options, json);
  return result;
}

/**
 * Property settings for shooting, the camera, etc.
 *
 * @function setOptions
 * @param {Options} options Camera setting options.
 * @return promise of boolean result
 */
export function setOptions(options: Options): Promise<boolean> {
  return ThetaClientReactNative.setOptions(options);
}

/**
 * Get metadata of a still image
 *
 * @function getMetadata
 * @param {string} fileUrl URL of a still image file to get metadata
 * @return promise of MetaInfo
 */
export function getMetadata(fileUrl: string): Promise<MetaInfo> {
  return ThetaClientReactNative.getMetadata(fileUrl);
}

/**
 * Acquires the access point list used in client mode.
 *
 * @function listAccessPoints
 * @return promise of AccessPoint list
 */
export function listAccessPoints(): Promise<AccessPoint[]> {
  return ThetaClientReactNative.listAccessPoints();
}

/**
 * Set access point. IP address is set dynamically.
 *
 * @function setAccessPointDynamically
 * @param {string} ssid SSID of the access point.
 * @param {} params - Optional parameters for additional configuration.
 * @param {boolean} params.ssidStealth true if SSID stealth is enabled.
 * @param {AuthModeEnum} params.authMode Authentication mode.
 * @param {string} params.password Password. Not set if authMode is “NONE”.
 * @param {number} params.connectionPriority Connection priority 1 to 5.
 * @param {Proxy} params.proxy Proxy information to be used for the access point.
 * @return promise of boolean result
 */
export function setAccessPointDynamically(
  ssid: string,
  params?: {
    ssidStealth?: boolean;
    authMode?: AuthModeEnum;
    password?: string;
    connectionPriority?: number;
    proxy?: Proxy;
  }
): Promise<boolean> {
  const {
    ssidStealth,
    authMode = AuthModeEnum.NONE,
    password,
    connectionPriority,
    proxy,
  } = params ?? {};
  return ThetaClientReactNative.setAccessPointDynamically({
    ssid,
    ssidStealth,
    authMode,
    password,
    connectionPriority,
    proxy,
  });
}

/**
 * Set access point. IP address is set statically.
 *
 * @function setAccessPointStatically
 * @param {string} ssid SSID of the access point.
 * @param {string} ipAddress IP address assigns to Theta.
 * @param {string} subnetMask Subnet mask.
 * @param {string} defaultGateway Default gateway.
 * @param {} params - Optional parameters for additional configuration.
 * @param {boolean} params.ssidStealth True if SSID stealth is enabled.
 * @param {AuthModeEnum} params.authMode Authentication mode.
 * @param {string} params.password Password. Not set if authMode is “NONE”.
 * @param {number} params.connectionPriority Connection priority 1 to 5.
 * @param {Proxy} params.proxy Proxy information to be used for the access point.
 * @return promise of boolean result
 */
export function setAccessPointStatically(
  ssid: string,
  ipAddress: string,
  subnetMask: string,
  defaultGateway: string,
  params?: {
    ssidStealth?: boolean;
    authMode: AuthModeEnum;
    password?: string;
    connectionPriority?: number;
    proxy?: Proxy;
  }
): Promise<boolean> {
  const {
    ssidStealth,
    authMode = AuthModeEnum.NONE,
    password,
    connectionPriority,
    proxy,
  } = params ?? {};
  return ThetaClientReactNative.setAccessPointStatically({
    ssid,
    ssidStealth,
    authMode,
    password,
    connectionPriority,
    ipAddress,
    subnetMask,
    defaultGateway,
    proxy,
  });
}

/**
 * Deletes access point information used in client mode.
 *
 * @function deleteAccessPoint
 * @param {string} ssid SSID of the access point to delete.
 * @return promise of boolean result
 */
export function deleteAccessPoint(ssid: string): Promise<boolean> {
  return ThetaClientReactNative.deleteAccessPoint(ssid);
}

/**
 * Acquires the shooting properties set by the camera._setMySetting command.
 * Just for Theta V and later.
 *
 * @function getMySetting
 * @param captureMode The target shooting mode
 * @returns Options of my setting
 */
export function getMySetting(captureMode: CaptureModeEnum): Promise<Options> {
  return ThetaClientReactNative.getMySetting(captureMode);
}

/**
 * Acquires the shooting properties set by the camera._setMySetting command.
 * Just for Theta S and SC.
 *
 * @function getMySetting
 * @param optionNames List of option names to acquire
 * @returns Options of my setting
 */
export function getMySettingFromOldModel(
  optionNames: OptionNameEnum[]
): Promise<Options> {
  return ThetaClientReactNative.getMySettingFromOldModel(optionNames);
}

/**
 * Registers shooting conditions in My Settings
 * @function setMySetting
 * @param captureMode The target shooting mode.  RICOH THETA S and SC do not support My Settings in video capture mode.
 * @param options registered to My Settings
 * @returns Promise of boolean result, always true
 */
export function setMySetting(
  captureMode: CaptureModeEnum,
  options: Options
): Promise<boolean> {
  return ThetaClientReactNative.setMySetting(captureMode, options);
}

/**
 * Delete shooting conditions in My Settings. Supported just by Theta X and Z1.
 * @param captureMode The target shooting mode
 * @returns Promise of boolean result, always true
 */
export function deleteMySetting(
  captureMode: CaptureModeEnum
): Promise<boolean> {
  return ThetaClientReactNative.deleteMySetting(captureMode);
}

/**
 * Acquires a list of installed plugins
 * @function listPlugins
 * @return A list of the plugins installed in Theta.
 */
export function listPlugins(): Promise<PluginInfo[]> {
  return ThetaClientReactNative.listPlugins();
}

/**
 * Sets the installed plugin for boot. Supported just by Theta V.
 * @function setPlugin
 * @param packageName Package name of the target plugin.
 * @return Promise of boolean result, always true.
 */
export function setPlugin(packageName: string): Promise<boolean> {
  return ThetaClientReactNative.setPlugin(packageName);
}

/**
 * Start the plugin specified by the [packageName].
 * @function startPlugin
 * @param packageName Package name of the target plugin.
 * @return Promise of boolean result, always true.
 */
export function startPlugin(packageName: string): Promise<boolean> {
  return ThetaClientReactNative.startPlugin(packageName);
}

/**
 * Stop the running plugin.
 * @function stopPlugin
 * @return Promise of boolean result, always true.
 */
export function stopPlugin(): Promise<boolean> {
  return ThetaClientReactNative.stopPlugin();
}

/**
 * Acquires the license for the installed plugin.
 * @function getPluginLicense
 * @param packageName Package name of the target plugin.
 * @return Promise of string result, HTML string of the license.
 */
export function getPluginLicense(packageName: string): Promise<string> {
  return ThetaClientReactNative.getPluginLicense(packageName);
}

/**
 * Return the plugin orders.  Supported just by Theta X and Z1.
 * @function getPluginOrders
 * @return Promise of string[] result, list of package names of plugins.
 */
export function getPluginOrders(): Promise<string[]> {
  return ThetaClientReactNative.getPluginOrders();
}

/**
 * Sets the plugin orders.  Supported just by Theta X and Z1.
 * @function setPluginOrders
 * @param plugins list of package names of plugins.
 * For Z1, list size must be three. No restrictions for the size for X.
 * When not specifying, set an empty string.
 * If an empty string is placed mid-way, it will be moved to the front.
 * Specifying zero package name will result in an error
 * @return Promise of boolean result, always true.
 */
export function setPluginOrders(plugins: string[]): Promise<boolean> {
  return ThetaClientReactNative.setPluginOrders(plugins);
}

export function getEventWebSocket(): Promise<EventWebSocket> {
  return new Promise<EventWebSocket>(async (resolve, reject) => {
    ThetaClientReactNative.getEventWebSocket()
      .then(() => {
        resolve(new EventWebSocket(NotifyController.instance));
      })
      .catch((error: any) => {
        reject(error);
      });
  });
}
