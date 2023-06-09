
#ifdef RCT_NEW_ARCH_ENABLED
#import "RNThetaClientReactNativeSpec.h"
#import <React/RCTEventEmitter.h>

@interface ThetaClientReactNative : RCTEventEmitter <NativeThetaClientReactNativeSpec>
#else
#import <React/RCTBridgeModule.h>
#import <React/RCTEventEmitter.h>

@interface ThetaClientReactNative : RCTEventEmitter <RCTBridgeModule>
#endif

/** theta repository instance */
@property(nonatomic, strong) THETACThetaRepository *theta;
/** photoCapture builder instance */
@property(nonatomic, strong) THETACPhotoCaptureBuilder *photoCaptureBuilder;
/** photoCapture instance */
@property(nonatomic, strong) THETACPhotoCapture *photoCapture;
/** timeShiftCapture builder instance */
@property(nonatomic, strong) THETACTimeShiftCaptureBuilder *timeShiftCaptureBuilder;
/** timeShiftCapture instance */
@property(nonatomic, strong) THETACTimeShiftCapture *timeShiftCapture;
/** timeShiftCapturing instance */
@property(nonatomic, strong) THETACTimeShiftCapturing *timeShiftCapturing;
/** videoCapture builder instance */
@property(nonatomic, strong) THETACVideoCaptureBuilder *videoCaptureBuilder;
/** videoCapture instance */
@property(nonatomic, strong) THETACVideoCapture *videoCapture;
/** videoCapturing instance */
@property(nonatomic, strong) THETACVideoCapturing *videoCapturing;
/** previewing in progress flag */
@property(nonatomic) BOOL previewing;
@end
