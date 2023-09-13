import type { Options } from 'theta-client-react-native';

export interface OptionEditProps {
  onChange: (options: Options) => void;
  options?: Options;
}

export * from './aperture';
export * from './capture-mode';
export * from './exposure-compensation';
export * from './exposure-delay';
export * from './exposure-program';
export * from './filter';
export * from './gps-info';
export * from './gps-tag-recording';
export * from './iso';
export * from './iso-auto-high-limit';
export * from './max-recordable-time';
export * from './photo-file-format';
export * from './preset';
export * from './time-shift';
export * from './video-file-format';
export * from './white-balance';
