import { ThetaWebApiError } from '../../theta-repository';
import { normalizeNativeError } from '../../theta-repository/theta-web-api-error';

describe('theta web api error extraction', () => {
  test('extracts statusCode and errorCode from userInfo', () => {
    const error = {
      message: 'Command cannot be executed due to the camera status.',
      userInfo: {
        statusCode: 403,
        errorCode: 'disabledCommand',
      },
    };

    const result = normalizeNativeError(error);

    expect(result).toBeInstanceOf(ThetaWebApiError);
    expect(result?.message).toBe(
      'Command cannot be executed due to the camera status.'
    );
    expect(result?.statusCode).toBe(403);
    expect(result?.errorCode).toBe('disabledCommand');
  });

  test('returns generic Error when only message contains legacy fallback pattern', () => {
    const error = {
      message: 'disabled (statusCode=409, errorCode=invalidParameterValue)',
    };

    const result = normalizeNativeError(error);

    expect(result).toBeInstanceOf(Error);
    expect((result as Error).message).toBe('[object Object]');
  });

  test('returns generic Error when userInfo does not contain statusCode/errorCode', () => {
    const error = {
      message: 'connection error',
      userInfo: {
        otherField: 'ignored',
      },
    };

    const result = normalizeNativeError(error);

    expect(result).toBeInstanceOf(Error);
    expect((result as Error).message).toBe('[object Object]');
  });

  test('returns original Error for non Theta web api error', () => {
    const error = new Error('network timeout');

    const result = normalizeNativeError(error);

    expect(result).toBe(error);
    expect((result as Error).message).toBe('network timeout');
  });
});
