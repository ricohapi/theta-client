import { EmitterSubscription, NativeEventEmitter, NativeModules } from "react-native";

export interface BaseNotify {
    name: string,
    /* eslint-disable-next-line @typescript-eslint/no-explicit-any */
    params?: any,
}

export interface CaptureProgressNotify {
    name: string,
    params?: {
        completion: number,
    },
}

export function addNotifyListener(callback: (event: BaseNotify) => void): EmitterSubscription {
    const eventEmitter = new NativeEventEmitter(NativeModules.ThetaClientReactNative);
    return eventEmitter.addListener('ThetaNotify', callback);
}