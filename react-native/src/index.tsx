import type { Spec } from './NativeThetaClientReactNative';
import NativeThetaClientReactNative from './NativeThetaClientReactNative';

const ThetaClientReactNative: Spec = NativeThetaClientReactNative;
// Ensure TurboModule is loaded on first access (triggers Proxy getter)
// eslint-disable-next-line no-void -- intentional: evaluate for side effect only
void ThetaClientReactNative;

/** theta frame event name
 * Note: TurboModule interfaces cannot contain constant properties (Codegen limitation).
 * Therefore, this constant is defined here instead of in the Spec interface,
 * even though Swift's constantsToExport() defines it as DEFAULT_EVENT_NAME.
 */
export const THETA_EVENT_NAME = 'ThetaFrameEvent';

export * from './theta-repository';
export * from './theta-repository/options';
export * from './capture';
