export const NativeModules = {
  ThetaClientReactNative: {
    initialize: jest.fn(),
    getThetaModel: jest.fn(),
    listFiles: jest.fn(),
    getPhotoCaptureBuilder: jest.fn(),
    buildPhotoCapture: jest.fn(),
    takePicture: jest.fn(),
    getTimeShiftCaptureBuilder: jest.fn(),
    buildTimeShiftCapture: jest.fn(),
    startTimeShiftCapture: jest.fn(),
    cancelTimeShiftCapture: jest.fn(),
    getVideoCaptureBuilder: jest.fn(),
    buildVideoCapture: jest.fn(),
    startVideoCapture: jest.fn(),
    stopVideoCapture: jest.fn(),
    getLimitlessIntervalCaptureBuilder: jest.fn(),
    buildLimitlessIntervalCapture: jest.fn(),
    startLimitlessIntervalCapture: jest.fn(),
    stopLimitlessIntervalCapture: jest.fn(),
  },
};

export const NativeEventEmitter_addListener = jest.fn();

export class NativeEventEmitter {
  addListener = NativeEventEmitter_addListener;
}
