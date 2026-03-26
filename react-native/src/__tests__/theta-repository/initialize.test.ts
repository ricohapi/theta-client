import { NativeModules } from 'react-native';
import { ThetaConfig, ThetaTimeout } from '../../theta-repository';
import { initialize } from '../../theta-repository';
import { NativeEventEmitter_addListener } from '../../__mocks__/react-native';
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
    jest.mocked(NativeEventEmitter_addListener).mockImplementation(
      jest.fn(() => {
        return {
          remove: jest.fn(),
        };
      })
    );
  });

  afterEach(() => {
    thetaClient.initialize = jest.fn(() => Promise.resolve());
  });

  test('Call empty parameter', async () => {
    jest.mocked(thetaClient.initialize).mockImplementation(
      jest.fn(async (endpoint, config, timeout) => {
        expect(endpoint).toBe(defaultEndpoint);
        expect(config).toBeNull();
        expect(timeout).toBeNull();
        return true;
      })
    );

    const result = await initialize();
    expect(result).toBeTruthy();
    expect(thetaClient.initialize).toHaveBeenCalledWith(
      defaultEndpoint,
      null,
      null
    );
  });

  test('Call parameter endpoint', async () => {
    const testEndpoint = 'http://test.com';

    jest.mocked(thetaClient.initialize).mockImplementation(
      jest.fn(async (endpoint, config, timeout) => {
        expect(endpoint).toBe(testEndpoint);
        expect(config).toBeNull();
        expect(timeout).toBeNull();
        return true;
      })
    );

    const result = await initialize(testEndpoint);
    expect(result).toBeTruthy();
    expect(thetaClient.initialize).toHaveBeenCalledWith(
      testEndpoint,
      null,
      null
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
        expect(timeout).toBeNull();
        return true;
      })
    );

    const result = await initialize(testEndpoint, testConfig);
    expect(result).toBeTruthy();
    expect(thetaClient.initialize).toHaveBeenCalledWith(
      testEndpoint,
      testConfig,
      null
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
      null,
      null
    );
  });
});
