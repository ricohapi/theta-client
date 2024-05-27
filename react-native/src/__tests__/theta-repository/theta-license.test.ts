import { NativeModules } from 'react-native';
import { getThetaLicense } from '../../theta-repository';

describe('getThetaLicense', () => {
  const thetaClient = NativeModules.ThetaClientReactNative;

  beforeEach(() => {
    thetaClient.getThetaLicense = jest.fn();
    jest.clearAllMocks();
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  test('getThetaLicense', async () => {
    const license = '<html>\n  <head></head>\n  <body></body>\n</html>';
    jest.mocked(thetaClient.getThetaLicense).mockImplementation(
      jest.fn(async () => {
        return license;
      })
    );

    const html = await getThetaLicense();
    expect(html).toBe(license);
  });
});
