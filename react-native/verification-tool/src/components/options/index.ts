import type { Options } from '../../modules/theta-client';

export interface OptionEditProps {
  onChange: (options: Options) => void;
  options?: Options;
}
export * from './auto-bracket';
export * from './enum-edit';
export * from './camera-lock-config';
export * from './ethernet-config';
export * from './gps-info';
export * from './mobile-network-setting';
export * from './time-shift';
export * from './top-bottom-correction-rotation';
export * from './wlan-frequency-cl-mode';
