/**
 * Registers the ThetaClientReactNative module with the React Native bridge.
 * Required so the TurboModule system can obtain the native instance when
 * TurboModuleRegistry.get('ThetaClientReactNative') is called.
 * Method dispatch is handled by the New Architecture JSI layer (codegen Spec);
 * RCT_EXTERN_METHOD is not used.
 */
#import <React/RCTBridgeModule.h>

@interface RCT_EXTERN_MODULE(ThetaClientReactNative, NSObject)
@end

#import <ReactCodegen/ThetaClientReactNativeSpec/ThetaClientReactNativeSpec.h>
#import <ReactCommon/RCTTurboModule.h>
#import <objc/runtime.h>
__attribute__((constructor)) static void
ThetaClientReactNativeRegisterTurboModule(void) {
  Class cls = NSClassFromString(@"ThetaClientReactNative");
  if (cls) {
    class_addProtocol(cls, @protocol(RCTTurboModule));
  }
}

@interface ThetaClientReactNative () <NativeThetaClientReactNativeSpec>
@end

@implementation ThetaClientReactNative (RCTTurboModule)

#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Wobjc-protocol-method-implementation"
- (std::shared_ptr<facebook::react::TurboModule>)getTurboModule:
    (const facebook::react::ObjCTurboModule::InitParams &)params {
  return std::make_shared<facebook::react::NativeThetaClientReactNativeSpecJSI>(params);
}
#pragma clang diagnostic pop

@end
