import { toNumber, toStringValue } from './libs/convert-utils';

export class ThetaWebApiError extends Error {
  statusCode?: number;
  errorCode?: string;
  moduleError?: Error;

  constructor(
    message: Error['message'],
    statusCode?: number,
    errorCode?: string,
    e?: unknown
  ) {
    super(message);
    this.name = 'ThetaWebApiError';
    this.statusCode = statusCode;
    this.errorCode = errorCode;
    this.moduleError = e instanceof Error ? e : undefined;
  }
}

type NativeErrorLike = {
  message?: unknown;
  userInfo?: Record<string, unknown>;
};

/**
 * Extract THETA Web API error details from a native bridge error.
 *
 * Current bridge contract:
 * - message: native error message (fallback to Error/String representation)
 * - statusCode: nativeError.userInfo.statusCode
 * - errorCode: nativeError.userInfo.errorCode
 *
 * Returns undefined when neither statusCode nor errorCode is available.
 */
function _extractThetaWebApiError(
  error: unknown
): ThetaWebApiError | undefined {
  if (error == null || typeof error !== 'object') {
    return undefined;
  }

  const nativeError = error as NativeErrorLike;
  const message =
    toStringValue(nativeError.message) ??
    (error instanceof Error ? error.message : String(error));

  const statusCode = toNumber(nativeError.userInfo?.statusCode);
  const errorCode = toStringValue(nativeError.userInfo?.errorCode);

  if (statusCode == null && errorCode == null) {
    return undefined;
  }

  return new ThetaWebApiError(message, statusCode, errorCode, error);
}

/**
 * Normalize a native bridge error into Error with THETA Web API details.
 *
 * If THETA Web API details are not available, returns the original Error when
 * possible, otherwise creates a generic Error.
 * String errors are returned as-is.
 */
export function normalizeNativeError(error: unknown): unknown {
  if (typeof error === 'string') {
    return error;
  }

  const details = _extractThetaWebApiError(error);
  if (details == null) {
    return error instanceof Error ? error : new Error(String(error));
  }

  console.log(
    `[ThetaWebApiError] statusCode=${details.statusCode ?? '-'} errorCode=${details.errorCode ?? '-'} message=${details.message}`
  );

  return details;
}
