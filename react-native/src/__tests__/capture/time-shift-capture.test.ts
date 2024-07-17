import { NativeModules } from 'react-native';
import { getTimeShiftCaptureBuilder, initialize } from '../../theta-repository';
import {
  BaseNotify,
  NotifyController,
} from '../../theta-repository/notify-controller';
import { NativeEventEmitter_addListener } from '../../__mocks__/react-native';
import { TimeShiftIntervalEnum } from '../../theta-repository/options';
import { CapturingStatusEnum } from '../../capture';

describe('time shift capture', () => {
  const thetaClient = NativeModules.ThetaClientReactNative;

  beforeEach(() => {
    jest.clearAllMocks();
    NotifyController.instance.release();
  });

  afterEach(() => {
    thetaClient.initialize = jest.fn();
    thetaClient.buildTimeShiftCapture = jest.fn();
    thetaClient.startTimeShiftCapture = jest.fn();
    thetaClient.cancelTimeShiftCapture = jest.fn();
    NotifyController.instance.release();
  });

  test('getTimeShiftCaptureBuilder', async () => {
    jest.mocked(NativeEventEmitter_addListener).mockImplementation(
      jest.fn(() => {
        return {
          remove: jest.fn(),
        };
      })
    );
    const builder = getTimeShiftCaptureBuilder();
    expect(builder.interval).toBeUndefined();
    expect(builder.options.timeShift).toBeUndefined();

    builder.setCheckStatusCommandInterval(1);
    builder.setIsFrontFirst(true);
    builder.setFirstInterval(TimeShiftIntervalEnum.INTERVAL_1);
    builder.setSecondInterval(TimeShiftIntervalEnum.INTERVAL_2);

    expect(builder.interval).toBe(1);
    expect(builder.options.timeShift).toBeDefined();
    expect(builder.options.timeShift?.isFrontFirst).toBeTruthy();
    expect(builder.options.timeShift?.firstInterval).toBe(
      TimeShiftIntervalEnum.INTERVAL_1
    );
    expect(builder.options.timeShift?.secondInterval).toBe(
      TimeShiftIntervalEnum.INTERVAL_2
    );

    let isCallBuild = false;
    jest.mocked(thetaClient.buildTimeShiftCapture).mockImplementation(
      jest.fn(async (options) => {
        expect(options._capture_interval).toBe(1);
        expect(options.timeShift?.isFrontFirst).toBeTruthy();
        expect(options.timeShift?.firstInterval).toBe(
          TimeShiftIntervalEnum.INTERVAL_1
        );
        expect(options.timeShift?.secondInterval).toBe(
          TimeShiftIntervalEnum.INTERVAL_2
        );
        isCallBuild = true;
      })
    );

    const capture = await builder.build();
    expect(capture).toBeDefined();
    expect(capture.notify).toBeDefined();
    expect(isCallBuild).toBeTruthy();
  });

  test('build no interval', async () => {
    jest.mocked(NativeEventEmitter_addListener).mockImplementation(
      jest.fn(() => {
        return {
          remove: jest.fn(),
        };
      })
    );
    const builder = getTimeShiftCaptureBuilder();
    expect(builder.interval).toBeUndefined();

    jest.mocked(thetaClient.buildTimeShiftCapture).mockImplementation(
      jest.fn(async (options) => {
        expect(options._capture_interval).toBe(-1);
        expect(options.timeShift).toBeUndefined();
      })
    );

    const capture = await builder.build();
    expect(capture).toBeDefined();
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
    const builder = getTimeShiftCaptureBuilder();
    jest
      .mocked(thetaClient.buildTimeShiftCapture)
      .mockImplementation(jest.fn(async () => {}));
    const testUrl = 'http://192.168.1.1/files/100RICOH/R100.JPG';
    jest.mocked(thetaClient.startTimeShiftCapture).mockImplementation(
      jest.fn(async () => {
        return testUrl;
      })
    );

    const capture = await builder.build();
    const fileUrl = await capture.startCapture();
    expect(fileUrl).toBe(testUrl);
    expect(NotifyController.instance.notifyList.size).toBe(0);
  });

  test('cancelCapture', (done) => {
    jest.mocked(NativeEventEmitter_addListener).mockImplementation(
      jest.fn(() => {
        return {
          remove: jest.fn(),
        };
      })
    );
    const builder = getTimeShiftCaptureBuilder();
    jest
      .mocked(thetaClient.buildTimeShiftCapture)
      .mockImplementation(jest.fn(async () => {}));
    jest.mocked(thetaClient.startTimeShiftCapture).mockImplementation(
      jest.fn(async () => {
        return null;
      })
    );

    builder.build().then((capture) => {
      capture.startCapture().then((value) => {
        expect(value).toBeUndefined();
        done();
      });
      capture.cancelCapture();
      expect(thetaClient.cancelTimeShiftCapture).toHaveBeenCalled();
    });
  });

  test('exception', (done) => {
    jest.mocked(NativeEventEmitter_addListener).mockImplementation(
      jest.fn(() => {
        return {
          remove: jest.fn(),
        };
      })
    );
    const builder = getTimeShiftCaptureBuilder();
    jest
      .mocked(thetaClient.buildTimeShiftCapture)
      .mockImplementation(jest.fn(async () => {}));
    jest.mocked(thetaClient.startTimeShiftCapture).mockImplementation(
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

  test('progress events', async () => {
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
    const builder = getTimeShiftCaptureBuilder();
    jest
      .mocked(thetaClient.buildTimeShiftCapture)
      .mockImplementation(jest.fn(async () => {}));
    const testUrl = 'http://192.168.1.1/files/100RICOH/R100.JPG';

    const sendProgress = (progress: number) => {
      notifyCallback({
        name: 'TIME-SHIFT-PROGRESS',
        params: {
          completion: progress,
        },
      });
    };

    jest.mocked(thetaClient.startTimeShiftCapture).mockImplementation(
      jest.fn(async () => {
        sendProgress(0.5);
        return testUrl;
      })
    );

    const capture = await builder.build();
    let isOnProgress = false;
    const fileUrl = await capture.startCapture((completion) => {
      expect(completion).toBe(0.5);
      isOnProgress = true;
    });
    expect(fileUrl).toBe(testUrl);

    let done: (value: unknown) => void;
    const promise = new Promise((resolve) => {
      done = resolve;
    });

    setTimeout(() => {
      expect(NotifyController.instance.notifyList.size).toBe(0);
      expect(isOnProgress).toBeTruthy();
      done(0);
    }, 1);

    return promise;
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
    const builder = getTimeShiftCaptureBuilder();
    jest
      .mocked(thetaClient.buildShotCountSpecifiedIntervalCapture)
      .mockImplementation(jest.fn(async () => {}));
    const testUrl = 'http://192.168.1.1/files/100RICOH/R100.JPG';

    const sendStopError = (message: string) => {
      notifyCallback({
        name: 'TIME-SHIFT-STOP-ERROR',
        params: {
          message,
        },
      });
    };

    jest.mocked(thetaClient.startTimeShiftCapture).mockImplementation(
      jest.fn(async () => {
        sendStopError('stop error');
        return testUrl;
      })
    );

    const capture = await builder.build();
    let isOnStopError = false;
    const fileUrl = await capture.startCapture(
      () => {},
      (error) => {
        expect(error.message).toBe('stop error');
        isOnStopError = true;
      }
    );
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

  test('capturing events', async () => {
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
    const builder = getTimeShiftCaptureBuilder();
    jest
      .mocked(thetaClient.buildShotCountSpecifiedIntervalCapture)
      .mockImplementation(jest.fn(async () => {}));
    const testUrl = 'http://192.168.1.1/files/100RICOH/R100.JPG';

    const sendStatus = (status: CapturingStatusEnum) => {
      notifyCallback({
        name: 'TIME-SHIFT-CAPTURING',
        params: {
          status,
        },
      });
    };

    jest.mocked(thetaClient.startTimeShiftCapture).mockImplementation(
      jest.fn(async () => {
        sendStatus(CapturingStatusEnum.SELF_TIMER_COUNTDOWN);
        return testUrl;
      })
    );

    const capture = await builder.build();
    let isOnCapturing = false;
    const fileUrl = await capture.startCapture(
      undefined,
      undefined,
      (status) => {
        expect(status).toBe(CapturingStatusEnum.SELF_TIMER_COUNTDOWN);
        isOnCapturing = true;
      }
    );
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
});
