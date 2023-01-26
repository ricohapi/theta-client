#import "ThetaClientFlutterPlugin.h"
#if __has_include(<theta_client_flutter/theta_client_flutter-Swift.h>)
#import <theta_client_flutter/theta_client_flutter-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "theta_client_flutter-Swift.h"
#endif

@implementation ThetaClientFlutterPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftThetaClientFlutterPlugin registerWithRegistrar:registrar];
}
@end
