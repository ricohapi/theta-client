import { NativeModules } from 'react-native';
import { initialize } from '../../theta-repository';
import {
  BaseNotify,
  NotifyController,
} from '../../theta-repository/notify-controller';
import { NativeEventEmitter_addListener } from '../../__mocks__/react-native';

describe('NotifyController', () => {
  const thetaClient = NativeModules.ThetaClientReactNative;

  beforeEach(() => {
    jest.clearAllMocks();
    NotifyController.instance.release();
  });

  afterEach(() => {
    thetaClient.initialize = jest.fn();
    NotifyController.instance.release();
  });

  test('init', async () => {
    jest.mocked(NativeEventEmitter_addListener).mockImplementation(
      jest.fn(() => {
        return {
          remove: jest.fn(),
        };
      })
    );

    expect(NotifyController.instance.eventListener).toBeUndefined();

    NotifyController.instance.init();

    expect(NotifyController.instance.notifyList.size).toBe(0);
    expect(NotifyController.instance.eventListener).toBeDefined();
  });

  test('call initialize', async () => {
    jest.mocked(NativeEventEmitter_addListener).mockImplementation(
      jest.fn(() => {
        return {
          remove: jest.fn(),
        };
      })
    );

    expect(NotifyController.instance.eventListener).toBeUndefined();

    await initialize();

    expect(NotifyController.instance.notifyList.size).toBe(0);
    expect(NotifyController.instance.eventListener).toBeDefined();
  });

  test('release', async () => {
    jest.mocked(NativeEventEmitter_addListener).mockImplementation(
      jest.fn(() => {
        return {
          remove: jest.fn(),
        };
      })
    );

    const notifyController = NotifyController.instance;

    notifyController.init();
    notifyController.addNotify('test', () => {});

    expect(notifyController.notifyList.size).toBe(1);
    expect(notifyController.notifyList.get('test')).toBeDefined();

    notifyController.release();

    expect(notifyController.notifyList.size).toBe(0);
    expect(notifyController.eventListener).toBeUndefined();
  });

  test('multiple events', async () => {
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

    const notifyController = NotifyController.instance;

    notifyController.init();

    let isOnEvent1 = false;
    notifyController.addNotify('test1', (event) => {
      expect(event.name).toBe('test1');
      expect(event.params?.value).toBe(2);
      isOnEvent1 = true;
    });

    let isOnEvent2 = false;
    notifyController.addNotify('test2', (event) => {
      expect(event.name).toBe('test2');
      expect(event.params?.value).toBe(3);
      isOnEvent2 = true;
    });

    expect(notifyController.notifyList.size).toBe(2);
    expect(notifyController.notifyList.get('test1')).toBeDefined();
    expect(notifyController.notifyList.get('test2')).toBeDefined();

    notifyCallback({
      name: 'test1',
      params: {
        value: 2,
      },
    });

    expect(isOnEvent1).toBeTruthy();
    expect(isOnEvent2).toBeFalsy();

    isOnEvent1 = false;

    notifyCallback({
      name: 'test2',
      params: {
        value: 3,
      },
    });

    expect(isOnEvent1).toBeFalsy();
    expect(isOnEvent2).toBeTruthy();
  });

  test('remove notify', async () => {
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

    const notifyController = NotifyController.instance;

    notifyController.init();
    let isOnEvent = false;
    notifyController.addNotify('test', () => {
      expect(true).toBeFalsy();
      isOnEvent = true;
    });

    expect(notifyController.notifyList.size).toBe(1);
    expect(notifyController.notifyList.get('test')).toBeDefined();

    notifyController.removeNotify('test');
    expect(notifyController.notifyList.size).toBe(0);

    notifyCallback({
      name: 'test',
      params: {
        value: 3,
      },
    });
    expect(isOnEvent).toBeFalsy();
  });
});
