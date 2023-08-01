import { NativeModules } from 'react-native';
import { ThetaConfig, ThetaTimeout, initialize } from '../../theta-repository';
import {
  LanguageEnum,
  OffDelayEnum,
  SleepDelayEnum,
} from '../../theta-repository/options';

describe('initialize', () => {
  const defaultEndpoint = 'http://192.168.1.1';
  const thetaClient = NativeModules.ThetaClientReactNative;

  beforeEach(() => {
    jest.clearAllMocks();
  });

  afterEach(() => {
    thetaClient.initialize = jest.fn();
  });

  test('Call empty parameter', async () => {
    jest.mocked(thetaClient.initialize).mockImplementation(
      jest.fn(async (endpoint, config, timeout) => {
        expect(endpoint).toBe(defaultEndpoint);
        expect(config).toBeUndefined();
        expect(timeout).toBeUndefined();
        return true;
      })
    );

    const result = await initialize();
    expect(result).toBeTruthy();
    expect(thetaClient.initialize).toHaveBeenCalledWith(
      defaultEndpoint,
      undefined,
      undefined
    );
  });

  test('Call parameter endpoint', async () => {
    const testEndpoint = 'http://test.com';

    jest.mocked(thetaClient.initialize).mockImplementation(
      jest.fn(async (endpoint, config, timeout) => {
        expect(endpoint).toBe(testEndpoint);
        expect(config).toBeUndefined();
        expect(timeout).toBeUndefined();
        return true;
      })
    );

    const result = await initialize(testEndpoint);
    expect(result).toBeTruthy();
    expect(thetaClient.initialize).toHaveBeenCalledWith(
      testEndpoint,
      undefined,
      undefined
    );
  });

  test('Call parameter config', async () => {
    const testEndpoint = 'http://test.com';
    const testConfig: ThetaConfig = {
      dateTime: '2022:11:28 09:33:53+09:00',
      language: LanguageEnum.JA,
      offDelay: OffDelayEnum.OFF_DELAY_10M,
      sleepDelay: SleepDelayEnum.SLEEP_DELAY_3M,
      shutterVolume: 5,
      clientMode: {
        username: 'THETAXX01234567',
        password: '01234567',
      },
    };

    jest.mocked(thetaClient.initialize).mockImplementation(
      jest.fn(async (endpoint, config, timeout) => {
        expect(endpoint).toBe(testEndpoint);
        expect(config).toBe(testConfig);
        expect(config.clientMode).toBeDefined();
        expect(config.clientMode).toBe(testConfig.clientMode);
        expect(timeout).toBeUndefined();
        return true;
      })
    );

    const result = await initialize(testEndpoint, testConfig);
    expect(result).toBeTruthy();
    expect(thetaClient.initialize).toHaveBeenCalledWith(
      testEndpoint,
      testConfig,
      undefined
    );
  });

  test('Call parameter timeout', async () => {
    const testEndpoint = 'http://test.com';
    const testConfig: ThetaConfig = {};
    const testTimeout: ThetaTimeout = {
      connectTimeout: 10,
      requestTimeout: 20,
      socketTimeout: 30,
    };

    jest.mocked(thetaClient.initialize).mockImplementation(
      jest.fn(async (endpoint, config, timeout) => {
        expect(endpoint).toBe(testEndpoint);
        expect(config).toBe(testConfig);
        expect(timeout).toBe(testTimeout);
        return true;
      })
    );

    const result = await initialize(testEndpoint, testConfig, testTimeout);
    expect(result).toBeTruthy();
    expect(thetaClient.initialize).toHaveBeenCalledWith(
      testEndpoint,
      testConfig,
      testTimeout
    );
  });

  test('Exception', async () => {
    jest.mocked(thetaClient.initialize).mockImplementation(
      jest.fn(async (_endpoint, _config, _timeout) => {
        throw 'error';
      })
    );

    try {
      await initialize();
      throw new Error('failed');
    } catch (error) {
      expect(error).toBe('error');
    }
    expect(thetaClient.initialize).toHaveBeenCalledWith(
      defaultEndpoint,
      undefined,
      undefined
    );
  });
});
