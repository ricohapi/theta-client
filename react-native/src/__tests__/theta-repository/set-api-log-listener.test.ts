import { initialize, setApiLogListener } from '../../theta-repository';
import {
  BaseNotify,
  NotifyController,
} from '../../theta-repository/notify-controller';
import { NativeEventEmitter_addListener } from '../../__mocks__/react-native';

describe('setApiLogListener', () => {
  beforeEach(() => {
    jest.clearAllMocks();
    setApiLogListener();
    NotifyController.instance.release();
  });

  afterEach(() => {
    jest.clearAllMocks();
    setApiLogListener();
    NotifyController.instance.release();
  });

  test('callback set before initialize', async () => {
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

    let isCallCallback = false;
    await setApiLogListener((message) => {
      expect(message).toBe('log-message');
      isCallCallback = true;
    });
    await initialize();
    notifyCallback({
      name: 'API-LOG',
      params: {
        message: 'log-message',
      },
    });

    expect(NotifyController.instance.existsNotify('API-LOG')).toBeTruthy();
    expect(isCallCallback).toBeTruthy();
  });

  test('callback set after initialize', async () => {
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
    expect(NotifyController.instance.existsNotify('API-LOG')).toBeFalsy();
    let isCallCallback = false;
    await setApiLogListener((message) => {
      expect(message).toBe('log-message');
      isCallCallback = true;
    });
    notifyCallback({
      name: 'API-LOG',
      params: {
        message: 'log-message',
      },
    });

    expect(NotifyController.instance.existsNotify('API-LOG')).toBeTruthy();
    expect(isCallCallback).toBeTruthy();
  });

  test('remove callback', async () => {
    await setApiLogListener(() => {});
    await initialize();
    expect(NotifyController.instance.existsNotify('API-LOG')).toBeTruthy();

    await setApiLogListener();
    expect(NotifyController.instance.existsNotify('API-LOG')).toBeFalsy();
  });
});
