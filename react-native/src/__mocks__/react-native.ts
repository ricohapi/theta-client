export const NativeModules = {
  ThetaClientReactNative: {
    initialize: jest.fn(),
    getThetaModel: jest.fn(),
    listFiles: jest.fn(),
    getTimeShiftCaptureBuilder: jest.fn(),
    buildTimeShiftCapture: jest.fn(),
    startTimeShiftCapture: jest.fn(),
    cancelTimeShiftCapture: jest.fn(),
  },
};

export const NativeEventEmitter_addListener = jest.fn();

export class NativeEventEmitter {
  addListener = NativeEventEmitter_addListener;
}
