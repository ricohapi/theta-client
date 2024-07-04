import { NativeModules } from 'react-native';
import {
  getLimitlessIntervalCaptureBuilder,
  initialize,
} from '../../theta-repository';
import {
  BaseNotify,
  NotifyController,
} from '../../theta-repository/notify-controller';
import { NativeEventEmitter_addListener } from '../../__mocks__/react-native';
import { CapturingStatusEnum } from '../../capture';

describe('limitless interval capture', () => {
  const thetaClient = NativeModules.ThetaClientReactNative;

  beforeEach(() => {
    jest.clearAllMocks();
    NotifyController.instance.release();
  });

  afterEach(() => {
    thetaClient.initialize = jest.fn();
    thetaClient.buildLimitlessIntervalCapture = jest.fn();
    thetaClient.startLimitlessIntervalCapture = jest.fn();
    thetaClient.stopLimitlessIntervalCapture = jest.fn();
    NotifyController.instance.release();
  });

  test('getLimitlessIntervalCaptureBuilder', async () => {
    jest.mocked(NativeEventEmitter_addListener).mockImplementation(
      jest.fn(() => {
        return {
          remove: jest.fn(),
        };
      })
    );
    const builder = getLimitlessIntervalCaptureBuilder();
    expect(builder.options).toBeDefined();
    expect(builder.interval).toBeUndefined();

    builder.setCheckStatusCommandInterval(1);
    builder.setCaptureInterval(10);

    expect(builder.interval).toBe(1);
    expect(builder.options.captureInterval).toBe(10);

    let isCallBuild = false;
    jest.mocked(thetaClient.buildLimitlessIntervalCapture).mockImplementation(
      jest.fn(async (options) => {
        expect(options._capture_interval).toBe(1);
        expect(options.captureInterval).toBe(10);
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
    const builder = getLimitlessIntervalCaptureBuilder();
    jest
      .mocked(thetaClient.buildLimitlessIntervalCapture)
      .mockImplementation(jest.fn(async () => {}));
    const testUrls = ['http://192.168.1.1/files/100RICOH/R100.JPG'];
    jest.mocked(thetaClient.startLimitlessIntervalCapture).mockImplementation(
      jest.fn(async () => {
        return testUrls;
      })
    );

    const capture = await builder.build();
    const fileUrls = await capture.startCapture();
    expect(fileUrls).toBe(testUrls);
    expect(NotifyController.instance.notifyList.size).toBe(0);
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
    const builder = getLimitlessIntervalCaptureBuilder();

    const sendStatus = (status: CapturingStatusEnum) => {
      notifyCallback({
        name: 'LIMITLESS-INTERVAL-CAPTURE-CAPTURING',
        params: {
          status: status,
        },
      });
    };

    jest
      .mocked(thetaClient.buildLimitlessIntervalCapture)
      .mockImplementation(jest.fn(async () => {}));
    const testUrls = ['http://192.168.1.1/files/100RICOH/R100.JPG'];
    jest.mocked(thetaClient.startLimitlessIntervalCapture).mockImplementation(
      jest.fn(async () => {
        sendStatus(CapturingStatusEnum.SELF_TIMER_COUNTDOWN);
        return testUrls;
      })
    );

    const capture = await builder.build();
    let isOnCapturing = false;
    const fileUrls = await capture.startCapture(undefined, (status) => {
      expect(status).toBe(CapturingStatusEnum.SELF_TIMER_COUNTDOWN);
      isOnCapturing = true;
    });
    expect(fileUrls).toBe(testUrls);
    expect(isOnCapturing).toBeTruthy();

    let done: (value: unknown) => void;
    const promise = new Promise((resolve) => {
      done = resolve;
    });

    setTimeout(() => {
      expect(NotifyController.instance.notifyList.size).toBe(0);
      done(0);
    }, 1);

    return promise;
  });

  test('stopCapture', (done) => {
    jest.mocked(NativeEventEmitter_addListener).mockImplementation(
      jest.fn(() => {
        return {
          remove: jest.fn(),
        };
      })
    );
    const builder = getLimitlessIntervalCaptureBuilder();
    jest
      .mocked(thetaClient.buildLimitlessIntervalCapture)
      .mockImplementation(jest.fn(async () => {}));
    jest.mocked(thetaClient.startLimitlessIntervalCapture).mockImplementation(
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
      expect(thetaClient.stopLimitlessIntervalCapture).toHaveBeenCalled();
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
    const builder = getLimitlessIntervalCaptureBuilder();
    jest
      .mocked(thetaClient.buildLimitlessIntervalCapture)
      .mockImplementation(jest.fn(async () => {}));
    jest.mocked(thetaClient.startLimitlessIntervalCapture).mockImplementation(
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
    const builder = getLimitlessIntervalCaptureBuilder();
    jest
      .mocked(thetaClient.buildLimitlessIntervalCapture)
      .mockImplementation(jest.fn(async () => {}));
    const testUrl = 'http://192.168.1.1/files/100RICOH/R100.MP4';

    const sendStopError = (message: string) => {
      notifyCallback({
        name: 'LIMITLESS-INTERVAL-CAPTURE-STOP-ERROR',
        params: {
          message,
        },
      });
    };

    jest.mocked(thetaClient.startLimitlessIntervalCapture).mockImplementation(
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
});
