import type { TurboModule } from 'react-native';
import { TurboModuleRegistry } from 'react-native';

export interface Spec extends TurboModule {
  // Event listeners for Android old architecture only
  addListener(eventName: string): void;
  removeListeners(count: number): void;

  // API Log
  setApiLogListener(enabled: boolean): Promise<void>;

  // Initialize
  initialize(
    endPoint: string,
    config: Object | null,
    timeout: Object | null
  ): Promise<boolean>;

  isInitialized(): Promise<boolean>;

  // Get theta info
  getThetaModel(): Promise<string | undefined>;
  getThetaInfo(): Promise<Object>;
  getThetaLicense(): Promise<string>;
  getThetaState(): Promise<Object>;

  // File operations
  listFiles(
    fileType: string,
    startPosition: number,
    entryCount: number,
    storage: string | null
  ): Promise<Object>;

  deleteFiles(fileUrls: Array<string>): Promise<boolean>;
  deleteAllFiles(): Promise<boolean>;
  deleteAllImageFiles(): Promise<boolean>;
  deleteAllVideoFiles(): Promise<boolean>;

  // Options
  getOptions(optionNames: Array<string>): Promise<Object>;
  setOptions(options: Object): Promise<boolean>;

  // Live preview
  getLivePreview(): Promise<boolean>;
  stopLivePreview(): Promise<boolean>;

  // Photo capture
  getPhotoCaptureBuilder(): Promise<void>;
  buildPhotoCapture(options: Object): Promise<void>;
  takePicture(): Promise<string>;

  // TimeShift capture
  getTimeShiftCaptureBuilder(): Promise<void>;
  buildTimeShiftCapture(options: Object): Promise<void>;
  startTimeShiftCapture(): Promise<string>;
  cancelTimeShiftCapture(): Promise<void>;

  // TimeShift manual capture
  getTimeShiftManualCaptureBuilder(): Promise<void>;
  buildTimeShiftManualCapture(options: Object): Promise<void>;
  startTimeShiftManualCapture(): Promise<string>;
  startTimeShiftManualSecondCapture(): Promise<string>;
  cancelTimeShiftManualCapture(): Promise<void>;

  // Video capture
  getVideoCaptureBuilder(): Promise<void>;
  buildVideoCapture(options: Object): Promise<void>;
  startVideoCapture(): Promise<string>;
  stopVideoCapture(): Promise<string>;

  // Limitless interval capture
  getLimitlessIntervalCaptureBuilder(): Promise<void>;
  buildLimitlessIntervalCapture(options: Object): Promise<void>;
  startLimitlessIntervalCapture(): Promise<Array<string>>;
  stopLimitlessIntervalCapture(): Promise<Array<string>>;

  // Shot count specified interval capture
  getShotCountSpecifiedIntervalCaptureBuilder(shotCount: number): Promise<void>;
  buildShotCountSpecifiedIntervalCapture(options: Object): Promise<void>;
  startShotCountSpecifiedIntervalCapture(): Promise<Array<string>>;
  cancelShotCountSpecifiedIntervalCapture(): Promise<void>;

  // Composite interval capture
  getCompositeIntervalCaptureBuilder(shootingTimeSec: number): Promise<void>;
  buildCompositeIntervalCapture(options: Object): Promise<void>;
  startCompositeIntervalCapture(): Promise<Array<string>>;
  cancelCompositeIntervalCapture(): Promise<void>;

  // Burst capture
  getBurstCaptureBuilder(
    burstCaptureNum: string,
    burstBracketStep: string,
    burstCompensation: string,
    burstMaxExposureTime: string,
    burstEnableIsoControl: string,
    burstOrder: string
  ): Promise<void>;
  buildBurstCapture(options: Object): Promise<void>;
  startBurstCapture(): Promise<Array<string>>;
  cancelBurstCapture(): Promise<void>;

  // Multi bracket capture
  getMultiBracketCaptureBuilder(): Promise<void>;
  buildMultiBracketCapture(options: Object): Promise<void>;
  startMultiBracketCapture(): Promise<Array<string>>;
  cancelMultiBracketCapture(): Promise<void>;

  // Continuous capture
  getContinuousCaptureBuilder(): Promise<void>;
  buildContinuousCapture(options: Object): Promise<void>;
  startContinuousCapture(): Promise<Array<string>>;

  // Metadata
  getMetadata(fileUrl: string): Promise<Object>;

  // Commands
  reboot(): Promise<void>;
  reset(): Promise<boolean>;
  restoreSettings(): Promise<boolean>;
  stopSelfTimer(): Promise<boolean>;

  // Video convert
  convertVideoFormats(
    fileUrl: string,
    toLowResolution: boolean,
    applyTopBottomCorrection: boolean
  ): Promise<string>;
  cancelVideoConvert(): Promise<boolean>;

  // WLAN
  finishWlan(): Promise<boolean>;
  listAccessPoints(): Promise<Array<Object>>;
  setAccessPointDynamically(params: Object): Promise<boolean>;
  setAccessPointStatically(params: Object): Promise<boolean>;
  setAccessPointConnectionPriority(
    ssid: string,
    connectionPriority: number,
    ssidStealth: boolean
  ): Promise<boolean>;
  deleteAccessPoint(ssid: string): Promise<boolean>;

  // My setting
  getMySetting(captureMode: string): Promise<Object>;
  getMySettingFromOldModel(optionNames: Array<string>): Promise<Object>;
  setMySetting(captureMode: string, options: Object): Promise<boolean>;
  deleteMySetting(captureMode: string): Promise<boolean>;

  // Plugin
  listPlugins(): Promise<Array<Object>>;
  setPlugin(packageName: string): Promise<boolean>;
  startPlugin(packageName: string): Promise<boolean>;
  stopPlugin(): Promise<boolean>;
  getPluginLicense(packageName: string): Promise<string>;
  getPluginOrders(): Promise<Array<string>>;
  setPluginOrders(plugins: Array<string>): Promise<boolean>;

  // Bluetooth
  setBluetoothDevice(uuid: string): Promise<string>;

  // Event WebSocket
  getEventWebSocket(): Promise<void>;
  eventWebSocketStart(): Promise<void>;
  eventWebSocketStop(): Promise<void>;
}

// New Architecture only: resolve TurboModule on first access
let cachedModule: Spec | null = null;

function getModule(): Spec {
  if (cachedModule != null) {
    return cachedModule;
  }
  const turbo = TurboModuleRegistry.get<Spec>('ThetaClientReactNative');
  if (turbo == null) {
    // In test environment, return a mock object instead of throwing
    if (
      process.env.NODE_ENV === 'test' ||
      process.env.JEST_WORKER_ID !== undefined
    ) {
      cachedModule = {} as Spec;
      return cachedModule;
    }
    throw new Error(
      'ThetaClientReactNative TurboModule could not be found. Ensure New Architecture is enabled and the module is registered in the native binary.'
    );
  }
  cachedModule = turbo;
  return cachedModule;
}

export default new Proxy({} as Spec, {
  get(_, prop: string | symbol) {
    const module = getModule();
    const value = module[prop as keyof Spec];
    // Return the value directly - if it's a function, it will be callable
    return value;
  },
});
