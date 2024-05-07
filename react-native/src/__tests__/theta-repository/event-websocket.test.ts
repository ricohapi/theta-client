import { NativeModules } from 'react-native';
import {
  BaseNotify,
  NotifyController,
} from '../../theta-repository/notify-controller';
import {
  CameraEvent,
  EventWebSocket,
  getEventWebSocket,
  initialize,
} from '../../theta-repository';
import { NativeEventEmitter_addListener } from '../../__mocks__/react-native';
import { CaptureModeEnum } from '../../theta-repository/options';

describe('EventWebSocket', () => {
  const thetaClient = NativeModules.ThetaClientReactNative;

  beforeEach(() => {
    jest.clearAllMocks();
    NotifyController.instance.release();
  });

  afterEach(() => {
    thetaClient.initialize = jest.fn();
    thetaClient.getEventWebSocket = jest.fn();
    thetaClient.eventWebSocketStart = jest.fn();
    thetaClient.eventWebSocketStop = jest.fn();
    NotifyController.instance.release();
  });

  test('normal', async () => {
    let notifyCallback: (notify: BaseNotify) => void = () => {
      expect(true).toBeFalsy();
    };
    const sendEvent = (cameraEvent: CameraEvent) => {
      notifyCallback({
        name: 'EVENT-WEBSOCKET-EVENT',
        params: {
          event: cameraEvent,
        },
      });
    };
    const sendClose = () => {
      notifyCallback({
        name: 'EVENT-WEBSOCKET-CLOSE',
      });
    };
    jest.mocked(NativeEventEmitter_addListener).mockImplementation(
      jest.fn((_, callback) => {
        notifyCallback = callback;
        return {
          remove: jest.fn(),
        };
      })
    );
    jest.mocked(thetaClient.getEventWebSocket).mockImplementation(
      jest.fn(async () => {
        return;
      })
    );
    jest.mocked(thetaClient.eventWebSocketStop).mockImplementation(
      jest.fn(async () => {
        sendClose();
        return;
      })
    );

    let done: (value: unknown) => void;
    const promise = new Promise((resolve) => {
      done = resolve;
    });

    let isClose = false;
    let captureMode: CaptureModeEnum | undefined;
    let batteryLevel: number | undefined;

    await initialize();
    const websocket: EventWebSocket = await getEventWebSocket();
    await websocket.start(
      (event) => {
        if (event.options) {
          captureMode = event.options.captureMode;
        }
        if (event.state) {
          batteryLevel = event.state.batteryLevel;
        }
      },
      () => {
        isClose = true;
        done(0);
      }
    );

    sendEvent({ options: { captureMode: CaptureModeEnum.IMAGE } });
    sendEvent({ state: { batteryLevel: 100 } });

    await websocket.stop();

    expect(thetaClient.getEventWebSocket).toHaveBeenCalled();
    expect(thetaClient.eventWebSocketStart).toHaveBeenCalled();
    expect(thetaClient.eventWebSocketStop).toHaveBeenCalled();

    setTimeout(() => {
      expect(NotifyController.instance.notifyList.size).toBe(0);
      expect(captureMode).toBe(CaptureModeEnum.IMAGE);
      expect(batteryLevel).toBe(100);
      expect(isClose).toBeTruthy();
      done(0);
    }, 10);

    return promise;
  });
});
