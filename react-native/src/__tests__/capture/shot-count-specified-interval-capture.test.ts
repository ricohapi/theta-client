import { NativeModules } from 'react-native';
import {
  getShotCountSpecifiedIntervalCaptureBuilder,
  initialize,
} from '../../theta-repository';
import {
  BaseNotify,
  NotifyController,
} from '../../theta-repository/notify-controller';
import { NativeEventEmitter_addListener } from '../../__mocks__/react-native';
import { CapturingStatusEnum } from '../../capture/capture';

describe('interval shooting with the shot count specified', () => {
  const thetaClient = NativeModules.ThetaClientReactNative;

  beforeEach(() => {
    jest.clearAllMocks();
    NotifyController.instance.release();
  });

  afterEach(() => {
    thetaClient.initialize = jest.fn();
    thetaClient.buildShotCountSpecifiedIntervalCapture = jest.fn();
    thetaClient.startShotCountSpecifiedIntervalCapture = jest.fn();
    thetaClient.cancelShotCountSpecifiedIntervalCapture = jest.fn();
    NotifyController.instance.release();
  });

  test('getShotCountSpecifiedIntervalCaptureBuilder', async () => {
    jest.mocked(NativeEventEmitter_addListener).mockImplementation(
      jest.fn(() => {
        return {
          remove: jest.fn(),
        };
      })
    );
    const builder = getShotCountSpecifiedIntervalCaptureBuilder(2);
    expect(builder.interval).toBeUndefined();

    builder.setCheckStatusCommandInterval(1);
    builder.setCaptureInterval(6);

    expect(builder.shotCount).toBe(2);
    expect(builder.interval).toBe(1);
    expect(builder.options.captureInterval).toBe(6);

    let isCallBuild = false;
    jest
      .mocked(thetaClient.buildShotCountSpecifiedIntervalCapture)
      .mockImplementation(
        jest.fn(async (options) => {
          expect(options._capture_interval).toBe(1);
          expect(options.captureInterval).toBe(6);
          isCallBuild = true;
        })
      );

    const capture = await builder.build();
    expect(capture).toBeDefined();
    expect(capture.notify).toBeDefined();
    expect(isCallBuild).toBeTruthy();

    expect(
      thetaClient.getShotCountSpecifiedIntervalCaptureBuilder
    ).toHaveBeenCalledWith(2);
  });

  test('build no interval', async () => {
    jest.mocked(NativeEventEmitter_addListener).mockImplementation(
      jest.fn(() => {
        return {
          remove: jest.fn(),
        };
      })
    );
    const builder = getShotCountSpecifiedIntervalCaptureBuilder(2);
    expect(builder.interval).toBeUndefined();

    jest
      .mocked(thetaClient.buildShotCountSpecifiedIntervalCapture)
      .mockImplementation(
        jest.fn(async (options) => {
          expect(options._capture_interval).toBe(-1);
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
    const builder = getShotCountSpecifiedIntervalCaptureBuilder(2);
    jest
      .mocked(thetaClient.buildShotCountSpecifiedIntervalCapture)
      .mockImplementation(jest.fn(async () => {}));
    const testUrls = ['http://192.168.1.1/files/100RICOH/R100.JPG'];
    jest
      .mocked(thetaClient.startShotCountSpecifiedIntervalCapture)
      .mockImplementation(
        jest.fn(async () => {
          return testUrls;
        })
      );

    const capture = await builder.build();
    const fileUrls = await capture.startCapture();
    expect(fileUrls).toBe(testUrls);
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
    const builder = getShotCountSpecifiedIntervalCaptureBuilder(2);
    jest
      .mocked(thetaClient.buildShotCountSpecifiedIntervalCapture)
      .mockImplementation(jest.fn(async () => {}));
    jest
      .mocked(thetaClient.startShotCountSpecifiedIntervalCapture)
      .mockImplementation(
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
      expect(
        thetaClient.cancelShotCountSpecifiedIntervalCapture
      ).toHaveBeenCalled();
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
    const builder = getShotCountSpecifiedIntervalCaptureBuilder(2);
    jest
      .mocked(thetaClient.buildShotCountSpecifiedIntervalCapture)
      .mockImplementation(jest.fn(async () => {}));
    jest
      .mocked(thetaClient.startShotCountSpecifiedIntervalCapture)
      .mockImplementation(
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
    const builder = getShotCountSpecifiedIntervalCaptureBuilder(2);
    jest
      .mocked(thetaClient.buildShotCountSpecifiedIntervalCapture)
      .mockImplementation(jest.fn(async () => {}));
    const testUrls = ['http://192.168.1.1/files/100RICOH/R100.JPG'];

    const sendProgress = (progress: number) => {
      notifyCallback({
        name: 'SHOT-COUNT-SPECIFIED-INTERVAL-PROGRESS',
        params: {
          completion: progress,
        },
      });
    };

    jest
      .mocked(thetaClient.startShotCountSpecifiedIntervalCapture)
      .mockImplementation(
        jest.fn(async () => {
          sendProgress(0.5);
          return testUrls;
        })
      );

    const capture = await builder.build();
    let isOnProgress = false;
    const fileUrls = await capture.startCapture((completion) => {
      expect(completion).toBe(0.5);
      isOnProgress = true;
    });
    expect(fileUrls).toBe(testUrls);

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
    const builder = getShotCountSpecifiedIntervalCaptureBuilder(2);
    jest
      .mocked(thetaClient.buildShotCountSpecifiedIntervalCapture)
      .mockImplementation(jest.fn(async () => {}));
    const testUrls = ['http://192.168.1.1/files/100RICOH/R100.JPG'];

    const sendStopError = (message: string) => {
      notifyCallback({
        name: 'SHOT-COUNT-SPECIFIED-INTERVAL-STOP-ERROR',
        params: {
          message,
        },
      });
    };

    jest
      .mocked(thetaClient.startShotCountSpecifiedIntervalCapture)
      .mockImplementation(
        jest.fn(async () => {
          sendStopError('stop error');
          return testUrls;
        })
      );

    const capture = await builder.build();
    let isOnStopError = false;
    const fileUrls = await capture.startCapture(
      () => {},
      (error) => {
        expect(error.message).toBe('stop error');
        isOnStopError = true;
      }
    );
    expect(fileUrls).toBe(testUrls);

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
    const builder = getShotCountSpecifiedIntervalCaptureBuilder(2);
    jest
      .mocked(thetaClient.buildShotCountSpecifiedIntervalCapture)
      .mockImplementation(jest.fn(async () => {}));
    const testUrls = ['http://192.168.1.1/files/100RICOH/R100.JPG'];

    const sendStatus = (status: CapturingStatusEnum) => {
      notifyCallback({
        name: 'SHOT-COUNT-SPECIFIED-INTERVAL-CAPTURING',
        params: {
          status: status,
        },
      });
    };

    jest
      .mocked(thetaClient.startShotCountSpecifiedIntervalCapture)
      .mockImplementation(
        jest.fn(async () => {
          sendStatus(CapturingStatusEnum.SELF_TIMER_COUNTDOWN);
          return testUrls;
        })
      );

    const capture = await builder.build();
    let isOnCapturing = false;
    const fileUrls = await capture.startCapture(
      undefined,
      undefined,
      (status) => {
        expect(status).toBe(CapturingStatusEnum.SELF_TIMER_COUNTDOWN);
        isOnCapturing = true;
      }
    );
    expect(fileUrls).toBe(testUrls);

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
