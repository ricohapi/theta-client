const ThetaClientReactNativeMock = {
  getConstants: jest.fn(() => ({ DEFAULT_EVENT_NAME: 'ThetaFrameEvent' })),
  setApiLogListener: jest.fn(() => Promise.resolve()),
  initialize: jest.fn(() => Promise.resolve()),
  getThetaModel: jest.fn(() => Promise.resolve('THETA_X')),
  listFiles: jest.fn(() => Promise.resolve({ fileList: [], totalEntries: 0 })),
  getPhotoCaptureBuilder: jest.fn(() => Promise.resolve()),
  buildPhotoCapture: jest.fn(() => Promise.resolve()),
  takePicture: jest.fn(() => Promise.resolve('')),
  getTimeShiftCaptureBuilder: jest.fn(() => Promise.resolve()),
  buildTimeShiftCapture: jest.fn(() => Promise.resolve()),
  startTimeShiftCapture: jest.fn(() => Promise.resolve('')),
  cancelTimeShiftCapture: jest.fn(() => Promise.resolve()),
  getTimeShiftManualCaptureBuilder: jest.fn(() => Promise.resolve()),
  buildTimeShiftManualCapture: jest.fn(() => Promise.resolve()),
  startTimeShiftManualCapture: jest.fn(() => Promise.resolve()),
  startTimeShiftManualSecondCapture: jest.fn(() => Promise.resolve('')),
  cancelTimeShiftManualCapture: jest.fn(() => Promise.resolve()),
  getVideoCaptureBuilder: jest.fn(() => Promise.resolve()),
  buildVideoCapture: jest.fn(() => Promise.resolve()),
  startVideoCapture: jest.fn(() => Promise.resolve('')),
  stopVideoCapture: jest.fn(() => Promise.resolve()),
  getLimitlessIntervalCaptureBuilder: jest.fn(() => Promise.resolve()),
  buildLimitlessIntervalCapture: jest.fn(() => Promise.resolve()),
  startLimitlessIntervalCapture: jest.fn(() => Promise.resolve([])),
  stopLimitlessIntervalCapture: jest.fn(() => Promise.resolve()),
  getShotCountSpecifiedIntervalCaptureBuilder: jest.fn(() => Promise.resolve()),
  buildShotCountSpecifiedIntervalCapture: jest.fn(() => Promise.resolve()),
  startShotCountSpecifiedIntervalCapture: jest.fn(() => Promise.resolve([])),
  cancelShotCountSpecifiedIntervalCapture: jest.fn(() => Promise.resolve()),
  getCompositeIntervalCaptureBuilder: jest.fn(() => Promise.resolve()),
  buildCompositeIntervalCapture: jest.fn(() => Promise.resolve()),
  startCompositeIntervalCapture: jest.fn(() => Promise.resolve([])),
  cancelCompositeIntervalCapture: jest.fn(() => Promise.resolve()),
  getBurstCaptureBuilder: jest.fn(() => Promise.resolve()),
  buildBurstCapture: jest.fn(() => Promise.resolve()),
  startBurstCapture: jest.fn(() => Promise.resolve([])),
  cancelBurstCapture: jest.fn(() => Promise.resolve()),
  getMultiBracketCaptureBuilder: jest.fn(() => Promise.resolve()),
  buildMultiBracketCapture: jest.fn(() => Promise.resolve()),
  startMultiBracketCapture: jest.fn(() => Promise.resolve([])),
  cancelMultiBracketCapture: jest.fn(() => Promise.resolve()),
  getContinuousCaptureBuilder: jest.fn(() => Promise.resolve()),
  buildContinuousCapture: jest.fn(() => Promise.resolve()),
  startContinuousCapture: jest.fn(() => Promise.resolve([])),
  getOptions: jest.fn(() => Promise.resolve({})),
  getEventWebSocket: jest.fn(() => Promise.resolve()),
  eventWebSocketStart: jest.fn(() => Promise.resolve()),
  eventWebSocketStop: jest.fn(() => Promise.resolve()),
  convertVideoFormats: jest.fn(() => Promise.resolve([])),
};

export const NativeModules = {
  ThetaClientReactNative: ThetaClientReactNativeMock,
};

export const TurboModuleRegistry = {
  get: jest.fn((name: string) =>
    name === 'ThetaClientReactNative' ? ThetaClientReactNativeMock : null
  ),
};

export const NativeEventEmitter_addListener = jest.fn();

export class NativeEventEmitter {
  constructor(_module?: any) {
    // Ignore the module parameter for testing
  }

  addListener(eventName: string, callback: (event: any) => void) {
    return NativeEventEmitter_addListener(eventName, callback);
  }
}
