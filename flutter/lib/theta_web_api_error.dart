import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';
import 'package:theta_client_flutter/utils/convert_utils.dart';

/// Exception for THETA Web API errors.
class ThetaWebApiException implements Exception {
  final String message;
  final int? statusCode;
  final String? errorCode;
  final Exception? moduleError;

  ThetaWebApiException(
    this.message, {
    this.statusCode,
    this.errorCode,
    this.moduleError,
  });

  @override
  String toString() {
    return 'ThetaWebApiException(message: $message, statusCode: $statusCode, errorCode: $errorCode, moduleError: $moduleError)';
  }
}

extension ThetaMethodChannelExtension on MethodChannel {
  Future<T?> invokeMethodWithThetaError<T>(String method,
      [dynamic arguments]) async {
    try {
      return await invokeMethod<T>(method, arguments);
    } catch (e) {
      throw _normalizeThetaError(e);
    }
  }
}

Object _normalizeThetaError(Object error) {
  if (error is! PlatformException) {
    return error;
  }
  final details = error.details;
  final detailsMap = details is Map ? details : null;
  final statusCode = ConvertUtils.toInt(detailsMap?['statusCode']);
  final errorCode = detailsMap?['errorCode']?.toString();
  final message = error.message ?? error.toString();
  if (statusCode != null || errorCode != null) {
    debugPrint(
        '[ThetaWebApiError] statusCode=${statusCode ?? '-'} errorCode=${errorCode ?? '-'} message=$message');
    return ThetaWebApiException(
      message,
      statusCode: statusCode,
      errorCode: errorCode,
      moduleError: error,
    );
  }
  return error;
}
