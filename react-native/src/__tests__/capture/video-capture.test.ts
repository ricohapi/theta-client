import { NativeModules } from 'react-native';
import { getVideoCaptureBuilder, initialize } from '../../theta-repository';
import {
  BaseNotify,
  NotifyController,
} from '../../theta-repository/notify-controller';
import { NativeEventEmitter_addListener } from '../../__mocks__/react-native';
import {
  MaxRecordableTimeEnum,
  VideoFileFormatEnum,
} from '../../theta-repository/options';
import { CapturingStatusEnum } from '../../capture';

describe('video capture', () => {
  const thetaClient = NativeModules.ThetaClientReactNative;

  beforeEach(() => {
    jest.clearAllMocks();
    NotifyController.instance.release();
  });

  afterEach(() => {
    thetaClient.initialize = jest.fn();
    thetaClient.buildVideoCapture = jest.fn();
    thetaClient.startVideoCapture = jest.fn();
    thetaClient.stopVideoCapture = jest.fn();
    NotifyController.instance.release();
  });

  test('getVideoCaptureBuilder', async () => {
    jest.mocked(NativeEventEmitter_addListener).mockImplementation(
      jest.fn(() => {
        return {
          remove: jest.fn(),
        };
      })
    );
    const builder = getVideoCaptureBuilder();
    expect(builder.options).toBeDefined();

    builder.setCheckStatusCommandInterval(1);
    builder.setFileFormat(VideoFileFormatEnum.VIDEO_4K);
    builder.setMaxRecordableTime(MaxRecordableTimeEnum.RECORDABLE_TIME_1500);

    expect(builder.interval).toBe(1);
    expect(builder.options.fileFormat).toBe(VideoFileFormatEnum.VIDEO_4K);
    expect(builder.options.maxRecordableTime).toBe(
      MaxRecordableTimeEnum.RECORDABLE_TIME_1500
    );

    let isCallBuild = false;
    jest.mocked(thetaClient.buildVideoCapture).mockImplementation(
      jest.fn(async (options) => {
        expect(options._capture_interval).toBe(1);
        expect(options.fileFormat).toBe(VideoFileFormatEnum.VIDEO_4K);
        expect(options.maxRecordableTime).toBe(
          MaxRecordableTimeEnum.RECORDABLE_TIME_1500
        );
        isCallBuild = true;
      })
    );

    const capture = await builder.build();
    expect(capture).toBeDefined();
    expect(capture.notify).toBeDefined();
    expect(isCallBuild).toBeTruthy();
  });

  test('startCapture', async () => {
    jest.mocked(NativeEventEmitter_addListener).mockImplementation(
      jest.fn(() => {
        return {
          remove: jest.fn(),
        };
      })
    );
    await initialize();
    const builder = getVideoCaptureBuilder();
    jest
      .mocked(thetaClient.buildVideoCapture)
      .mockImplementation(jest.fn(async () => {}));
    const testUrl = 'http://192.168.1.1/files/100RICOH/R100.MP4';
    jest.mocked(thetaClient.startVideoCapture).mockImplementation(
      jest.fn(async () => {
        return testUrl;
      })
    );

    const capture = await builder.build();
    const fileUrl = await capture.startCapture();
    expect(fileUrl).toBe(testUrl);
    expect(NotifyController.instance.notifyList.size).toBe(0);
  });

  test('stopCapture', (done) => {
    jest.mocked(NativeEventEmitter_addListener).mockImplementation(
      jest.fn(() => {
        return {
          remove: jest.fn(),
        };
      })
    );
    const builder = getVideoCaptureBuilder();
    jest
      .mocked(thetaClient.buildVideoCapture)
      .mockImplementation(jest.fn(async () => {}));
    jest.mocked(thetaClient.startVideoCapture).mockImplementation(
      jest.fn(async () => {
        return null;
      })
    );

    builder.build().then((capture) => {
      capture.startCapture().then((value) => {
        expect(value).toBeUndefined();
        done();
      });
      capture.stopCapture();
      expect(thetaClient.stopVideoCapture).toHaveBeenCalled();
    });
  });

  test('exception startCapture', (done) => {
    jest.mocked(NativeEventEmitter_addListener).mockImplementation(
      jest.fn(() => {
        return {
          remove: jest.fn(),
        };
      })
    );
    const builder = getVideoCaptureBuilder();
    jest
      .mocked(thetaClient.buildVideoCapture)
      .mockImplementation(jest.fn(async () => {}));
    jest.mocked(thetaClient.startVideoCapture).mockImplementation(
      jest.fn(async () => {
        throw 'error';
      })
    );

    builder.build().then((capture) => {
      capture
        .startCapture()
        .then(() => {
          expect(true).toBeFalsy();
        })
        .catch((error) => {
          expect(error).toBe('error');
          done();
        });
    });
  });

  test('stop error events', async () => {
    let notifyCallback: (notify: BaseNotify) => void = () => {
      expect(true).toBeFalsy();
    };
    jest.mocked(NativeEventEmitter_addListener).mockImplementation(
      jest.fn((_, callback) => {
        notifyCallback = callback;
        return {
          remove: jest.fn(),
        };
      })
    );

    await initialize();
    const builder = getVideoCaptureBuilder();
    jest
      .mocked(thetaClient.buildVideoCapture)
      .mockImplementation(jest.fn(async () => {}));
    const testUrl = 'http://192.168.1.1/files/100RICOH/R100.MP4';

    const sendStopError = (message: string) => {
      notifyCallback({
        name: 'VIDEO-CAPTURE-STOP-ERROR',
        params: {
          message,
        },
      });
    };

    jest.mocked(thetaClient.startVideoCapture).mockImplementation(
      jest.fn(async () => {
        sendStopError('stop error');
        return testUrl;
      })
    );

    const capture = await builder.build();
    let isOnStopError = false;
    const fileUrl = await capture.startCapture((error) => {
      expect(error.message).toBe('stop error');
      isOnStopError = true;
    });
    expect(fileUrl).toBe(testUrl);

    let done: (value: unknown) => void;
    const promise = new Promise((resolve) => {
      done = resolve;
    });

    setTimeout(() => {
      expect(NotifyController.instance.notifyList.size).toBe(0);
      expect(isOnStopError).toBeTruthy();
      done(0);
    }, 1);

    return promise;
  });

  test('startCaptureWithCapturing', async () => {
    let notifyCallback: (notify: BaseNotify) => void = () => {
      expect(true).toBeFalsy();
    };
    jest.mocked(NativeEventEmitter_addListener).mockImplementation(
      jest.fn((_, callback) => {
        notifyCallback = callback;
        return {
          remove: jest.fn(),
        };
      })
    );

    await initialize();
    const builder = getVideoCaptureBuilder();
    jest
      .mocked(thetaClient.buildVideoCapture)
      .mockImplementation(jest.fn(async () => {}));
    const testUrl = 'http://192.168.1.1/files/100RICOH/R100.MP4';

    const sendStatus = (status: CapturingStatusEnum) => {
      notifyCallback({
        name: 'VIDEO-CAPTURE-CAPTURING',
        params: {
          status: status,
        },
      });
    };

    jest.mocked(thetaClient.startVideoCapture).mockImplementation(
      jest.fn(async () => {
        sendStatus(CapturingStatusEnum.SELF_TIMER_COUNTDOWN);
        return testUrl;
      })
    );

    const capture = await builder.build();
    let isOnCapturing = false;
    const fileUrl = await capture.startCapture(undefined, (status) => {
      expect(status).toBe(CapturingStatusEnum.SELF_TIMER_COUNTDOWN);
      isOnCapturing = true;
    });
    expect(fileUrl).toBe(testUrl);

    let done: (value: unknown) => void;
    const promise = new Promise((resolve) => {
      done = resolve;
    });

    setTimeout(() => {
      expect(NotifyController.instance.notifyList.size).toBe(0);
      expect(isOnCapturing).toBeTruthy();
      done(0);
    }, 1);

    return promise;
  });

  test('onCaptureStarted', async () => {
    let notifyCallback: (notify: BaseNotify) => void = () => {
      expect(true).toBeFalsy();
    };
    jest.mocked(NativeEventEmitter_addListener).mockImplementation(
      jest.fn((_, callback) => {
        notifyCallback = callback;
        return {
          remove: jest.fn(),
        };
      })
    );

    await initialize();
    const builder = getVideoCaptureBuilder();
    jest
      .mocked(thetaClient.buildVideoCapture)
      .mockImplementation(jest.fn(async () => {}));
    const testUrl = 'http://192.168.1.1/files/100RICOH/R100.MP4';

    const sendFileUrl = (fileUrl: String) => {
      notifyCallback({
        name: 'VIDEO-CAPTURE-STARTED',
        params: {
          fileUrl: fileUrl,
        },
      });
    };

    jest.mocked(thetaClient.startVideoCapture).mockImplementation(
      jest.fn(async () => {
        sendFileUrl('xxx.mp4');
        return testUrl;
      })
    );

    const capture = await builder.build();
    let isStarted = false;
    const result = await capture.startCapture(
      undefined,
      undefined,
      (fileUrl) => {
        expect(fileUrl).toBe('xxx.mp4');
        isStarted = true;
      }
    );
    expect(result).toBe(testUrl);

    let done: (value: unknown) => void;
    const promise = new Promise((resolve) => {
      done = resolve;
    });

    setTimeout(() => {
      expect(NotifyController.instance.notifyList.size).toBe(0);
      expect(isStarted).toBeTruthy();
      done(0);
    }, 1);

    return promise;
  });
});
