import { NativeModules } from 'react-native';
import { getPhotoCaptureBuilder } from '../../theta-repository';
import {
  FilterEnum,
  PhotoFileFormatEnum,
  PresetEnum,
} from '../../theta-repository/options';

describe('photo capture', () => {
  const thetaClient = NativeModules.ThetaClientReactNative;

  beforeEach(() => {
    jest.clearAllMocks();
  });

  afterEach(() => {
    thetaClient.initialize = jest.fn();
    thetaClient.buildPhotoCapture = jest.fn();
    thetaClient.takePicture = jest.fn();
  });

  test('getPhotoCaptureBuilder', async () => {
    const builder = getPhotoCaptureBuilder();
    expect(thetaClient.getPhotoCaptureBuilder).toHaveBeenCalled();
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
