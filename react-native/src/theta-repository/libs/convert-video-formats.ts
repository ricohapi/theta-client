import { NativeModules } from 'react-native';
import { BaseNotify, NotifyController } from '../notify-controller';
const ThetaClientReactNative = NativeModules.ThetaClientReactNative;

const NOTIFY_PROGRESS = 'CONVERT-VIDEO-FORMATS-PROGRESS';
const MESSAGE_ERROR_RUNNING = 'convertVideoFormats is running.';

interface ProgressNotify extends BaseNotify {
  params?: {
    completion: number;
  };
}

export async function convertVideoFormatsImpl(
  fileUrl: string,
  toLowResolution: boolean,
  applyTopBottomCorrection: boolean,
  onProgress?: (completion: number) => void
): Promise<string> {
  const notify = NotifyController.instance;
  if (notify.existsNotify(NOTIFY_PROGRESS)) {
    throw new Error(MESSAGE_ERROR_RUNNING);
  }
  notify.addNotify(NOTIFY_PROGRESS, (event: ProgressNotify) => {
    if (onProgress != null && event.params?.completion != null) {
      onProgress(event.params.completion);
    }
  });
  try {
    const result: string = await ThetaClientReactNative.convertVideoFormats(
      fileUrl,
      toLowResolution,
      applyTopBottomCorrection
    );
    return result;
  } catch (error: any) {
    throw error;
  } finally {
    notify.removeNotify(NOTIFY_PROGRESS);
  }
}
