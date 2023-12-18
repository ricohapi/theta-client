import { NativeModules } from 'react-native';
import {
  getContinuousCaptureBuilder,
  initialize,
} from '../../theta-repository';
import {
  BaseNotify,
  NotifyController,
} from '../../theta-repository/notify-controller';
import { NativeEventEmitter_addListener } from '../../__mocks__/react-native';
import {
  ContinuousNumberEnum,
  OptionNameEnum,
  PhotoFileFormatEnum,
} from '../../theta-repository/options';
import { ContinuousCapture } from '../../capture';

describe('continuous shooting', () => {
  const thetaClient = NativeModules.ThetaClientReactNative;

  beforeEach(() => {
    jest.clearAllMocks();
    NotifyController.instance.release();
  });

  afterEach(() => {
    thetaClient.initialize = jest.fn();
    thetaClient.buildContinuousCapture = jest.fn();
    thetaClient.startContinuousCapture = jest.fn();
    thetaClient.getOptions = jest.fn();
    NotifyController.instance.release();
  });

  test('getContinuousCaptureBuilder', async () => {
    jest.mocked(NativeEventEmitter_addListener).mockImplementation(
      jest.fn(() => {
        return {
          remove: jest.fn(),
        };
      })
    );

    const builder = getContinuousCaptureBuilder();
    expect(builder.interval).toBeUndefined();

    builder.setCheckStatusCommandInterval(1);
    builder.setFileFormat(PhotoFileFormatEnum.IMAGE_11K);

    expect(builder.interval).toBe(1);
    expect(builder.options.fileFormat).toBe('IMAGE_11K');

    let isCallBuild = false;
    jest.mocked(thetaClient.buildContinuousCapture).mockImplementation(
      jest.fn(async (options) => {
        expect(options._capture_interval).toBe(1);
        expect(options.fileFormat).toBe('IMAGE_11K');
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

    const builder = getContinuousCaptureBuilder();
    expect(builder.interval).toBeUndefined();

    jest.mocked(thetaClient.buildContinuousCapture).mockImplementation(
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

    const builder = getContinuousCaptureBuilder();
    jest
      .mocked(thetaClient.buildContinuousCapture)
      .mockImplementation(jest.fn(async () => {}));
    const testUrls = ['http://192.168.1.1/files/100RICOH/R100.JPG'];
    jest.mocked(thetaClient.startContinuousCapture).mockImplementation(
      jest.fn(async () => {
        return testUrls;
      })
    );

    const capture = await builder.build();
    const fileUrls = await capture.startCapture();
    expect(fileUrls).toBe(testUrls);
    expect(NotifyController.instance.notifyList.size).toBe(0);
  });

  test('exception', (done) => {
    jest.mocked(NativeEventEmitter_addListener).mockImplementation(
      jest.fn(() => {
        return {
          remove: jest.fn(),
        };
      })
    );

    const builder = getContinuousCaptureBuilder();
    jest
      .mocked(thetaClient.buildContinuousCapture)
      .mockImplementation(jest.fn(async () => {}));
    jest.mocked(thetaClient.startContinuousCapture).mockImplementation(
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

    const builder = getContinuousCaptureBuilder();
    jest
      .mocked(thetaClient.buildContinuousCapture)
      .mockImplementation(jest.fn(async () => {}));
    const testUrl = 'http://192.168.1.1/files/100RICOH/R100.JPG';

    const sendProgress = (progress: number) => {
      notifyCallback({
        name: 'CONTINUOUS-PROGRESS',
        params: {
          completion: progress,
        },
      });
    };

    jest.mocked(thetaClient.startContinuousCapture).mockImplementation(
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

  test('getContinuousNumber', async () => {
    jest.mocked(thetaClient.getOptions).mockImplementation(
      jest.fn(async (optionNames: OptionNameEnum[]) => {
        expect(optionNames.length).toBe(1);
        expect(optionNames[0]).toBe(OptionNameEnum.ContinuousNumber);
        const options = {
          continuousNumber: ContinuousNumberEnum.MAX_10,
        };
        return options;
      })
    );

    const capture = new ContinuousCapture(NotifyController.instance);
    const continuousNumber = await capture.getContinuousNumber();
    expect(continuousNumber).toBe(ContinuousNumberEnum.MAX_10);
  });

  test('getContinuousNumber unsupported', async () => {
    jest.mocked(thetaClient.getOptions).mockImplementation(
      jest.fn(async (optionNames: OptionNameEnum[]) => {
        expect(optionNames.length).toBe(1);
        expect(optionNames[0]).toBe(OptionNameEnum.ContinuousNumber);
        const options = {};
        return options;
      })
    );

    const capture = new ContinuousCapture(NotifyController.instance);
    const continuousNumber = await capture.getContinuousNumber();
    expect(continuousNumber).toBe(ContinuousNumberEnum.UNSUPPORTED);
  });

  test('getContinuousNumber exception', async () => {
    jest.mocked(thetaClient.getOptions).mockImplementation(
      jest.fn(async (optionNames: OptionNameEnum[]) => {
        expect(optionNames.length).toBe(1);
        expect(optionNames[0]).toBe(OptionNameEnum.ContinuousNumber);
        throw 'error';
      })
    );

    const capture = new ContinuousCapture(NotifyController.instance);
    try {
      await capture.getContinuousNumber();
      expect(true).toBeFalsy();
    } catch (error) {
      expect(error).toBe('error');
    }
  });
});
