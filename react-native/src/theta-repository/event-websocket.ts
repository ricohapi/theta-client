import { NativeModules } from 'react-native';
import type { Options } from './options';
import type { ThetaState } from './theta-state';
import type { BaseNotify, NotifyController } from './notify-controller';

const ThetaClientReactNative = NativeModules.ThetaClientReactNative;

const NOTIFY_EVENT = 'EVENT-WEBSOCKET-EVENT';
const NOTIFY_CLOSE = 'EVENT-WEBSOCKET-CLOSE';

export type CameraEvent = {
  options?: Options;
  state?: ThetaState;
};

interface EventNotify extends BaseNotify {
  params?: {
    event: CameraEvent;
  };
}

export class EventWebSocket {
  notify: NotifyController;
  constructor(notify: NotifyController) {
    this.notify = notify;
  }

  async start(onReceive: (event: CameraEvent) => void, onClose: () => void) {
    this.notify.addNotify(NOTIFY_EVENT, (event: EventNotify) => {
      if (event.params != null) {
        onReceive(event.params.event);
      }
    });

    this.notify.addNotify(NOTIFY_CLOSE, () => {
      onClose();
      this.notify.removeNotify(NOTIFY_EVENT);
      this.notify.removeNotify(NOTIFY_CLOSE);
    });
    try {
      await ThetaClientReactNative.eventWebSocketStart();
    } catch (error) {
      this.notify.removeNotify(NOTIFY_EVENT);
      this.notify.removeNotify(NOTIFY_CLOSE);
      throw error;
    }
  }

  async stop() {
    await ThetaClientReactNative.eventWebSocketStop();
  }
}
