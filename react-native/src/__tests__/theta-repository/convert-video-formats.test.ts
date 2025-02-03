import { NativeModules } from 'react-native';
import { convertVideoFormats, initialize } from '../../theta-repository';
import { NativeEventEmitter_addListener } from '../../__mocks__/react-native';
import {
  BaseNotify,
  NotifyController,
} from '../../theta-repository/notify-controller';

describe('convertVideoFormats', () => {
  const thetaClient = NativeModules.ThetaClientReactNative;

  beforeEach(() => {
    thetaClient.convertVideoFormats = jest.fn();
    NotifyController.instance.release();
    jest.clearAllMocks();
  });

  afterEach(() => {
    NotifyController.instance.release();
    jest.clearAllMocks();
  });

  test('convertVideoFormats', async () => {
    const inUrl = 'http://192.168.1.1/files/100RICOH/R0013330.MP4';
    const outUrl =
      'http://192.168.1.1/files/100RICOH/R0013330_er_2K_H264_tbc.MP4';
    const toLowResolution = true;
    const applyTopBottomCorrection = true;
    jest.mocked(thetaClient.convertVideoFormats).mockImplementation(
      jest.fn(async () => {
        return outUrl;
      })
    );

    const result = await convertVideoFormats(
      inUrl,
      toLowResolution,
      applyTopBottomCorrection
    );
    expect(result).toBe(outUrl);
    expect(thetaClient.convertVideoFormats).toHaveBeenCalledTimes(1);
    expect(thetaClient.convertVideoFormats).toHaveBeenCalledWith(
      inUrl,
      toLowResolution,
      applyTopBottomCorrection
    );
  });

  test('onProgress', async () => {
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
    const sendProgress = (completion: number) => {
      notifyCallback({
        name: 'CONVERT-VIDEO-FORMATS-PROGRESS',
        params: {
          completion,
        },
      });
    };

    const inUrl = 'http://192.168.1.1/files/100RICOH/R0013330.MP4';
    const outUrl =
      'http://192.168.1.1/files/100RICOH/R0013330_er_2K_H264_tbc.MP4';
    const toLowResolution = true;
    const applyTopBottomCorrection = true;
    jest.mocked(thetaClient.convertVideoFormats).mockImplementation(
      jest.fn(async () => {
        sendProgress(0.8);
        return outUrl;
      })
    );

    await initialize();
    let isOnProgress = false;
    const result = await convertVideoFormats(
      inUrl,
      toLowResolution,
      applyTopBottomCorrection,
      (completion) => {
        expect(completion).toBe(0.8);
        isOnProgress = true;
      }
    );

    expect(result).toBe(outUrl);
    expect(isOnProgress).toBeTruthy();
    expect(thetaClient.convertVideoFormats).toHaveBeenCalledTimes(1);
    expect(thetaClient.convertVideoFormats).toHaveBeenCalledWith(
      inUrl,
      toLowResolution,
      applyTopBottomCorrection
    );
    expect(NotifyController.instance.notifyList.size).toBe(0);
  });

  test('error of running', async () => {
    const inUrl = 'http://192.168.1.1/files/100RICOH/R0013330.MP4';
    const outUrl =
      'http://192.168.1.1/files/100RICOH/R0013330_er_2K_H264_tbc.MP4';
    const toLowResolution = true;
    const applyTopBottomCorrection = true;
    jest.mocked(thetaClient.convertVideoFormats).mockImplementation(
      jest.fn(async () => {
        return outUrl;
      })
    );

    let isOnError = false;
    convertVideoFormats(inUrl, toLowResolution, applyTopBottomCorrection).then(
      (url) => {
        expect(url).toBe(outUrl);
      }
    );
    try {
      await convertVideoFormats(
        inUrl,
        toLowResolution,
        applyTopBottomCorrection
      );
    } catch (error: any) {
      isOnError = true;
      console.log(`${error}`);
      expect(error.message).toBe('convertVideoFormats is running.');
    }
    expect(isOnError).toBeTruthy();
  });

  test('error convertVideoFormats', async () => {
    const inUrl = 'http://192.168.1.1/files/100RICOH/R0013330.MP4';
    const toLowResolution = true;
    const applyTopBottomCorrection = true;
    jest.mocked(thetaClient.convertVideoFormats).mockImplementation(
      jest.fn(async () => {
        throw 'error';
      })
    );

    let isOnError = false;
    try {
      await convertVideoFormats(
        inUrl,
        toLowResolution,
        applyTopBottomCorrection
      );
      expect(false).toBeTruthy();
    } catch (error) {
      isOnError = true;
      expect(error).toBe('error');
    }
    expect(isOnError).toBeTruthy();
  });
});
