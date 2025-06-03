import {
  NativeEventEmitter,
  type EmitterSubscription,
  NativeModules,
} from 'react-native';

export interface BaseNotify {
  name: string;
  params?: any;
}

export class NotifyController {
  static _instance = new NotifyController();
  static get instance(): NotifyController {
    return NotifyController._instance;
  }

  eventEmitter?: NativeEventEmitter = undefined;
  eventListener?: EmitterSubscription = undefined;
  notifyList = new Map<string, ((notify: BaseNotify) => void) | undefined>();

  init() {
    this.notifyList.clear();
    if (this.eventListener == null) {
      this.eventListener = this.addNotifyListener((notify: BaseNotify) => {
        this.notifyList.get(notify.name)?.(notify as BaseNotify);
      });
    }
  }

  isInit() {
    return this.eventListener != null;
  }

  release() {
    this.eventListener?.remove();
    this.eventListener = undefined;
    this.eventEmitter = undefined;
    this.notifyList.clear();
  }

  addNotify(name: string, callback: (notify: BaseNotify) => void) {
    this.notifyList.set(name, callback);
  }

  removeNotify(name: string) {
    this.notifyList.delete(name);
  }

  existsNotify(name: string): boolean {
    return this.notifyList.has(name);
  }

  private addNotifyListener(
    callback: (event: BaseNotify) => void
  ): EmitterSubscription {
    if (this.eventEmitter == null) {
      this.eventEmitter = new NativeEventEmitter(
        NativeModules.ThetaClientReactNative
      );
    }
    return this.eventEmitter.addListener('ThetaNotify', callback);
  }
}
