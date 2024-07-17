import { NativeModules } from 'react-native';
import { getPhotoCaptureBuilder, initialize } from '../../theta-repository';
import {
  FilterEnum,
  PhotoFileFormatEnum,
  PresetEnum,
} from '../../theta-repository/options';
import { NativeEventEmitter_addListener } from '../../__mocks__/react-native';
import {
  BaseNotify,
  NotifyController,
} from '../../theta-repository/notify-controller';
import { CapturingStatusEnum } from '../../capture';

describe('photo capture', () => {
  const thetaClient = NativeModules.ThetaClientReactNative;

  beforeEach(() => {
    jest.clearAllMocks();
    NotifyController.instance.release();
  });

  afterEach(() => {
    thetaClient.initialize = jest.fn();
    thetaClient.buildPhotoCapture = jest.fn();
    thetaClient.takePicture = jest.fn();
    NotifyController.instance.release();
  });

  test('getPhotoCaptureBuilder', async () => {
    const builder = getPhotoCaptureBuilder();
    // expect(thetaClient.getPhotoCaptureBuilder).toHaveBeenCalled();
    expect(builder.options).toBeDefined();

    builder.setFileFormat(PhotoFileFormatEnum.IMAGE_11K);
    builder.setFilter(FilterEnum.HDR);
    builder.setPreset(PresetEnum.FACE);

    expect(builder.options.fileFormat).toBe(PhotoFileFormatEnum.IMAGE_11K);
    expect(builder.options.filter).toBe(FilterEnum.HDR);
    expect(builder.options.preset).toBe(PresetEnum.FACE);

    let isCallBuild = false;
    jest.mocked(thetaClient.buildPhotoCapture).mockImplementation(
      jest.fn(async (options) => {
        expect(options.fileFormat).toBe(PhotoFileFormatEnum.IMAGE_11K);
        expect(options.filter).toBe(FilterEnum.HDR);
        expect(options.preset).toBe(PresetEnum.FACE);
        isCallBuild = true;
      })
    );

    const capture = await builder.build();
    expect(capture).toBeDefined();
    expect(isCallBuild).toBeTruthy();
  });

  test('takePicture', async () => {
    const builder = getPhotoCaptureBuilder();
    const testUrl = 'http://192.168.1.1/files/100RICOH/R100.JPG';

    jest
      .mocked(thetaClient.buildPhotoCapture)
      .mockImplementation(jest.fn(async () => {}));
    jest.mocked(thetaClient.takePicture).mockImplementation(
      jest.fn(async () => {
        return testUrl;
      })
    );

    const capture = await builder.build();
    const fileUrl = await capture.takePicture();
    expect(fileUrl).toBe(testUrl);
  });

  test('takePictureWithCapturing', async () => {
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
    const builder = getPhotoCaptureBuilder();
    const testUrl = 'http://192.168.1.1/files/100RICOH/R100.JPG';

    const sendStatus = (status: CapturingStatusEnum) => {
      notifyCallback({
        name: 'PHOTO-CAPTURING',
        params: {
          status: status,
        },
      });
    };

    jest
      .mocked(thetaClient.buildPhotoCapture)
      .mockImplementation(jest.fn(async () => {}));
    jest.mocked(thetaClient.takePicture).mockImplementation(
      jest.fn(async () => {
        sendStatus(CapturingStatusEnum.SELF_TIMER_COUNTDOWN);
        return testUrl;
      })
    );

    const capture = await builder.build();
    let isOnCapturing = false;
    const fileUrl = await capture.takePicture((status) => {
      expect(status).toBe(CapturingStatusEnum.SELF_TIMER_COUNTDOWN);
      isOnCapturing = true;
    });
    expect(fileUrl).toBe(testUrl);
    expect(isOnCapturing).toBeTruthy();
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

  test('exception', (done) => {
    const builder = getPhotoCaptureBuilder();
    jest
      .mocked(thetaClient.buildPhotoCapture)
      .mockImplementation(jest.fn(async () => {}));
    jest.mocked(thetaClient.takePicture).mockImplementation(
      jest.fn(async () => {
        throw 'error';
      })
    );

    builder.build().then((capture) => {
      capture
        .takePicture()
        .then(() => {
          expect(true).toBeFalsy();
        })
        .catch((error) => {
          expect(error).toBe('error');
          done();
        });
    });
  });
});
