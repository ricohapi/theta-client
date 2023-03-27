#import "THETAClient/THETAClient.h"
#import "ThetaClientReactNative.h"
#import "RCTConvert.h"

/**
 * converter for int32_t
 */
@implementation RCTConvert (int32_t)
RCT_NUMBER_CONVERTER(int32_t, intValue)
@end

/**
 * takepicture callback holder
 */
@interface PhotoCallback: NSObject <THETACPhotoCaptureTakePictureCallback>
@end
@implementation PhotoCallback {
  RCTPromiseResolveBlock _resolve; ///< takepicture callback resolver
  RCTPromiseRejectBlock _reject;   ///< takepicture callback rejecter
  ThetaClientReactNative *_sdk;            ///< theta client sdk instance
}
/**
 * initialize PhotoCallback
 * @param sdk theta client sdk instance
 * @param resolve resolver for promise
 * @param reject rejecter for promise
 * @return initialized callback instance
 */
-(id)initWith:(ThetaClientReactNative *)sdk
 withResolver:(RCTPromiseResolveBlock)resolve
 withRejecter:(RCTPromiseRejectBlock)reject
{
  self = [super init];
  if (self) {
    _sdk = sdk;
    _resolve = resolve;
    _reject = reject;
  }
  return self;
}

/**
 * call when error detect
 * @param exception exception object
 */
-(void)onErrorException:(THETACThetaRepositoryThetaRepositoryException *)exception
{
  _reject(@"error", exception.message, nil);
  _sdk.photoCapture = nil;
}

/**
 * call when successfully taken picture
 * @param fileUrl taken picture file url
 */
-(void)onSuccessFileUrl:(NSString *)fileUrl
{
  _resolve(fileUrl);
  _sdk.photoCapture = nil;
}
@end

/**
 * startCapture callback holder
 */
@interface VideoCallback: NSObject <THETACVideoCaptureStartCaptureCallback>
@end
@implementation VideoCallback {
  RCTPromiseResolveBlock _resolve; ///< start capture resolver
  RCTPromiseRejectBlock _reject;   ///< start capture rejecter
  ThetaClientReactNative *_sdk;            ///< theta client sdk instance
}

/**
 * initialize VideoCallback
 * @param sdk theta client sdk instance
 * @param resolve resolver for promise
 * @param reject rejecter for promise
 * @return initialized callback instance
 */
-(id)initWith:(ThetaClientReactNative *)sdk
 withResolver:(RCTPromiseResolveBlock)resolve
 withRejecter:(RCTPromiseRejectBlock)reject
{
  self = [super init];
  if (self) {
    _sdk = sdk;
    _resolve = resolve;
    _reject = reject;
  }
  return self;
}

/**
 * call when error detect
 * @param exception exception object
 */
-(void)onErrorException:(THETACThetaRepositoryThetaRepositoryException *)exception
{
  _reject(@"error", exception.message, nil);
  _sdk.videoCapture = nil;
  _sdk.videoCapturing = nil;
}

/**
 * call when successfully video captured
 * @param fileUrl captured video file url
 */
-(void)onSuccessFileUrl:(NSString *)fileUrl
{
  _resolve(fileUrl);
  _sdk.videoCapture = nil;
  _sdk.videoCapturing = nil;
}
@end

/** frame event handler with NSData */
typedef void (^FrameEventHandler)(NSData *);
/** frame interval time */
static CFTimeInterval FRAME_INTERVAL = (CFTimeInterval) (1.0/10.0);
/**
 * live preview frame handler holder
 */
@interface FrameHandler: NSObject <THETACKotlinSuspendFunction1>
@end
@implementation FrameHandler {
  FrameEventHandler _handler;   ///< frame event handler
  ThetaClientReactNative *_sdk;         ///< theta client sdk instance
  CFTimeInterval _last;         ///< last event time
}

/**
 * initialize frame handler
 * @param sdk theta client sdk instance
 * @param handler frame event handler
 */
-(id)initWith:(ThetaClientReactNative *)sdk
 withEvent:(FrameEventHandler)handler
{
  self = [super init];
  if (self) {
    _sdk = sdk;
    _handler = handler;
    _last = 0;
  }
  return self;
}

/**
 * call when frame data arrived
 * @param p1 ByteReadPacket instance
 * @param completionHandler set result
 */
- (void)invokeP1:(id)p1 completionHandler:(void (^)(id _Nullable_result, NSError * _Nullable))completionHandler __attribute__((swift_name("invoke(p1:completionHandler:)")));
{
  CFTimeInterval now = CACurrentMediaTime();
  if (now - _last > FRAME_INTERVAL) {
    NSData *data =
      [THETACPlatformKt frameFromPacket:(THETACKotlinPair<THETACKotlinByteArray *, THETACInt *> *)p1];
    _handler(data);
    completionHandler(@(_sdk.previewing), nil);
    _last = now;
  } else {
    completionHandler(@(_sdk.previewing), nil);
  }
}
@end

/** opiton converter from theta to react */
typedef void (^SetFromTheta)(NSMutableDictionary *, THETACThetaRepositoryOptions *);
/** option converter from react to theta */
typedef void (^SetToTheta)(NSDictionary *, THETACThetaRepositoryOptions *);
/** option converter to set photo option */
typedef void (^SetPhotoOption)(NSDictionary *, THETACPhotoCaptureBuilder *);
/** option converter to set video option */
typedef void (^SetVideoOption)(NSDictionary *, THETACVideoCaptureBuilder *);

typedef struct _convert_t {
  NSDictionary* toTheta;        ///< dictionary react to theta
  NSDictionary* fromTheta;      ///< dictionary theta to react
  NSDictionary* photoOption;    ///< dictionary photoOption
  NSDictionary* videoOption;    ///< dictionary videoOption
  SetToTheta setToTheta;        ///< option setter react to theta
  SetFromTheta setFromTheta;    ///< option setter theta to react
  SetPhotoOption setPhotoOption; ///< photo option setter
  SetVideoOption setVideoOption; ///< video option setter
} convert_t;

/**
 * ChargingStateEnum converter
 */
static convert_t ChargingStateEnum = {
  .fromTheta = @{
    THETACThetaRepositoryChargingStateEnum.charging: @"CHARGING",
    THETACThetaRepositoryChargingStateEnum.completed: @"COMPLETED",
    THETACThetaRepositoryChargingStateEnum.notCharging: @"NOT_CHARGING"
  }
};

/**
 * FileTypeEnum converter
 */
static convert_t FileTypeEnum = {
  .toTheta = @{
    @"IMAGE": THETACThetaRepositoryFileTypeEnum.image,
    @"VIDEO": THETACThetaRepositoryFileTypeEnum.video,
    @"ALL": THETACThetaRepositoryFileTypeEnum.all
  }
};

/**
 * AuthModeEnum converter
 */
static convert_t AuthModeEnum = {
  .toTheta = @{
    @"NONE": THETACThetaRepositoryAuthModeEnum.none,
    @"WEP": THETACThetaRepositoryAuthModeEnum.wep,
    @"WPA": THETACThetaRepositoryAuthModeEnum.wpa
  },
  .fromTheta = @{
    THETACThetaRepositoryAuthModeEnum.none: @"NONE",
    THETACThetaRepositoryAuthModeEnum.wep: @"WEP",
    THETACThetaRepositoryAuthModeEnum.wpa: @"WPA"
  }
};

/**
 * ApertureEnum converter
 */
static convert_t ApertureEnum = {
  .toTheta = @{
    @"APERTURE_AUTO": THETACThetaRepositoryApertureEnum.apertureAuto,
    @"APERTURE_2_0": THETACThetaRepositoryApertureEnum.aperture20,
    @"APERTURE_2_1": THETACThetaRepositoryApertureEnum.aperture21,
    @"APERTURE_2_4": THETACThetaRepositoryApertureEnum.aperture24,
    @"APERTURE_3_5": THETACThetaRepositoryApertureEnum.aperture35,
    @"APERTURE_5_6": THETACThetaRepositoryApertureEnum.aperture56
  },
  .fromTheta = @{
    THETACThetaRepositoryApertureEnum.apertureAuto: @"APERTURE_AUTO",
    THETACThetaRepositoryApertureEnum.aperture20: @"APERTURE_2_0",
    THETACThetaRepositoryApertureEnum.aperture21: @"APERTURE_2_1",
    THETACThetaRepositoryApertureEnum.aperture24: @"APERTURE_2_4",
    THETACThetaRepositoryApertureEnum.aperture35: @"APERTURE_3_5",
    THETACThetaRepositoryApertureEnum.aperture56: @"APERTURE_5_6"
  },
  .setToTheta = ^(NSDictionary* rct, THETACThetaRepositoryOptions *opt) {
    id val = [ApertureEnum.toTheta objectForKey:[rct objectForKey:@"aperture"]];
    if (val) {
      opt.aperture = val;
    }
  },
  .setFromTheta = ^(NSMutableDictionary* rct, THETACThetaRepositoryOptions *opt) {
    id val = [ApertureEnum.fromTheta objectForKey:opt.aperture];
    if (val) {
      [rct setObject:val forKey:@"aperture"];
    }
  },
  .setPhotoOption = ^(NSDictionary* rct, THETACPhotoCaptureBuilder *builder) {
    id val = [ApertureEnum.toTheta objectForKey:[rct objectForKey:@"aperture"]];
    if (val) {
      [builder setApertureAperture:val];
    }
  },
  .setVideoOption = ^(NSDictionary* rct, THETACVideoCaptureBuilder *builder) {
    id val = [ApertureEnum.toTheta objectForKey:[rct objectForKey:@"aperture"]];
    if (val) {
      [builder setApertureAperture:val];
    }
  }
};

/**
 * CaptureModeEnum converter
 */
static convert_t CaptureModeEnum = {
  .toTheta = @{
    @"IMAGE": THETACThetaRepositoryCaptureModeEnum.image,
    @"VIDEO": THETACThetaRepositoryCaptureModeEnum.video
  },
  .fromTheta = @{
    THETACThetaRepositoryCaptureModeEnum.image: @"IMAGE",
    THETACThetaRepositoryCaptureModeEnum.video: @"VIDEO"
  },
  .setToTheta = ^(NSDictionary* rct, THETACThetaRepositoryOptions *opt) {
    id val = [CaptureModeEnum.toTheta objectForKey:[rct objectForKey:@"captureMode"]];
    if (val) {
      opt.captureMode = val;
    }
  },
  .setFromTheta = ^(NSMutableDictionary* rct, THETACThetaRepositoryOptions *opt) {
    id val = [CaptureModeEnum.fromTheta objectForKey:opt.captureMode];
    if (val) {
      [rct setObject:val forKey:@"captureMode"];
    }
  }
};

/**
 * ExposureCompensationEnum converter
 */
static convert_t ExposureCompensationEnum = {
  .toTheta = @{
    @"M2_0": THETACThetaRepositoryExposureCompensationEnum.m20,
    @"M1_7": THETACThetaRepositoryExposureCompensationEnum.m17,
    @"M1_3": THETACThetaRepositoryExposureCompensationEnum.m13,
    @"M1_0": THETACThetaRepositoryExposureCompensationEnum.m10,
    @"M0_7": THETACThetaRepositoryExposureCompensationEnum.m07,
    @"M0_3": THETACThetaRepositoryExposureCompensationEnum.m03,
    @"ZERO": THETACThetaRepositoryExposureCompensationEnum.zero,
    @"P0_3": THETACThetaRepositoryExposureCompensationEnum.p03,
    @"P0_7": THETACThetaRepositoryExposureCompensationEnum.p07,
    @"P1_0": THETACThetaRepositoryExposureCompensationEnum.p10,
    @"P1_3": THETACThetaRepositoryExposureCompensationEnum.p13,
    @"P1_7": THETACThetaRepositoryExposureCompensationEnum.p17,
    @"P2_0": THETACThetaRepositoryExposureCompensationEnum.p20
  },
  .fromTheta = @{
    THETACThetaRepositoryExposureCompensationEnum.m20: @"M2_0",
    THETACThetaRepositoryExposureCompensationEnum.m17: @"M1_7",
    THETACThetaRepositoryExposureCompensationEnum.m13: @"M1_3",
    THETACThetaRepositoryExposureCompensationEnum.m10: @"M1_0",
    THETACThetaRepositoryExposureCompensationEnum.m07: @"M0_7",
    THETACThetaRepositoryExposureCompensationEnum.m03: @"M0_3",
    THETACThetaRepositoryExposureCompensationEnum.zero: @"ZERO",
    THETACThetaRepositoryExposureCompensationEnum.p03: @"P0_3",
    THETACThetaRepositoryExposureCompensationEnum.p07: @"P0_7",
    THETACThetaRepositoryExposureCompensationEnum.p10: @"P1_0",
    THETACThetaRepositoryExposureCompensationEnum.p13: @"P1_3",
    THETACThetaRepositoryExposureCompensationEnum.p17: @"P1_7",
    THETACThetaRepositoryExposureCompensationEnum.p20: @"P2_0",
  },
  .setToTheta = ^(NSDictionary* rct, THETACThetaRepositoryOptions *opt) {
    id val = [ExposureCompensationEnum.toTheta
                 objectForKey:[rct objectForKey:@"exposureCompensation"]];
    if (val) {
      opt.exposureCompensation = val;
    }
  },
  .setFromTheta = ^(NSMutableDictionary* rct, THETACThetaRepositoryOptions *opt) {
    id val = [ExposureCompensationEnum.fromTheta objectForKey:opt.exposureCompensation];
    if (val) {
      [rct setObject:val forKey:@"exposureCompensation"];
    }
  },
  .setPhotoOption = ^(NSDictionary* rct, THETACPhotoCaptureBuilder *builder) {
    id val = [ExposureCompensationEnum.toTheta
                 objectForKey:[rct objectForKey:@"exposureCompensation"]];
    if (val) {
      [builder setExposureCompensationValue:val];
    }
  },
  .setVideoOption = ^(NSDictionary* rct, THETACVideoCaptureBuilder *builder) {
    id val = [ExposureCompensationEnum.toTheta
                 objectForKey:[rct objectForKey:@"exposureCompensation"]];
    if (val) {
      [builder setExposureCompensationValue:val];
    }
  }
};

/**
 * ExposureDelayEnum converter
 */
static convert_t ExposureDelayEnum = {
  .toTheta = @{
    @"DELAY_OFF": THETACThetaRepositoryExposureDelayEnum.delayOff,
    @"DELAY_1": THETACThetaRepositoryExposureDelayEnum.delay1,
    @"DELAY_2": THETACThetaRepositoryExposureDelayEnum.delay2,
    @"DELAY_3": THETACThetaRepositoryExposureDelayEnum.delay3,
    @"DELAY_4": THETACThetaRepositoryExposureDelayEnum.delay4,
    @"DELAY_5": THETACThetaRepositoryExposureDelayEnum.delay5,
    @"DELAY_6": THETACThetaRepositoryExposureDelayEnum.delay6,
    @"DELAY_7": THETACThetaRepositoryExposureDelayEnum.delay7,
    @"DELAY_8": THETACThetaRepositoryExposureDelayEnum.delay8,
    @"DELAY_9": THETACThetaRepositoryExposureDelayEnum.delay9,
    @"DELAY_10": THETACThetaRepositoryExposureDelayEnum.delay10
  },
  .fromTheta = @{
    THETACThetaRepositoryExposureDelayEnum.delayOff: @"DELAY_OFF",
    THETACThetaRepositoryExposureDelayEnum.delay1: @"DELAY_1",
    THETACThetaRepositoryExposureDelayEnum.delay2: @"DELAY_2",
    THETACThetaRepositoryExposureDelayEnum.delay3: @"DELAY_3",
    THETACThetaRepositoryExposureDelayEnum.delay4: @"DELAY_4",
    THETACThetaRepositoryExposureDelayEnum.delay5: @"DELAY_5",
    THETACThetaRepositoryExposureDelayEnum.delay6: @"DELAY_6",
    THETACThetaRepositoryExposureDelayEnum.delay7: @"DELAY_7",
    THETACThetaRepositoryExposureDelayEnum.delay8: @"DELAY_8",
    THETACThetaRepositoryExposureDelayEnum.delay9: @"DELAY_9",
    THETACThetaRepositoryExposureDelayEnum.delay10: @"DELAY_10"
  },
  .setToTheta = ^(NSDictionary* rct, THETACThetaRepositoryOptions *opt) {
    id val = [ExposureDelayEnum.toTheta objectForKey:[rct objectForKey:@"exposureDelay"]];
    if (val) {
      opt.exposureDelay = val;
    }
  },
  .setFromTheta = ^(NSMutableDictionary* rct, THETACThetaRepositoryOptions *opt) {
    id val = [ExposureDelayEnum.fromTheta objectForKey:opt.exposureDelay];
    if (val) {
      [rct setObject:val forKey:@"exposureDelay"];
    }
  },
  .setPhotoOption = ^(NSDictionary* rct, THETACPhotoCaptureBuilder *builder) {
    id val = [ExposureDelayEnum.toTheta objectForKey:[rct objectForKey:@"exposureDelay"]];
    if (val) {
      [builder setExposureDelayDelay:val];
    }
  },
  .setVideoOption = ^(NSDictionary* rct, THETACVideoCaptureBuilder *builder) {
    id val = [ExposureDelayEnum.toTheta objectForKey:[rct objectForKey:@"exposureDelay"]];
    if (val) {
      [builder setExposureDelayDelay:val];
    }
  }
};

/**
 * ExposureProgramEnum converter
 */
static convert_t ExposureProgramEnum = {
  .toTheta = @{
    @"MANUAL": THETACThetaRepositoryExposureProgramEnum.manual,
    @"NORMAL_PROGRAM": THETACThetaRepositoryExposureProgramEnum.normalProgram,
    @"APERTURE_PRIORITY": THETACThetaRepositoryExposureProgramEnum.aperturePriority,
    @"SHUTTER_PRIORITY": THETACThetaRepositoryExposureProgramEnum.shutterPriority,
    @"ISO_PRIORITY": THETACThetaRepositoryExposureProgramEnum.isoPriority
  },
  .fromTheta = @{
    THETACThetaRepositoryExposureProgramEnum.manual: @"MANUAL",
    THETACThetaRepositoryExposureProgramEnum.normalProgram: @"NORMAL_PROGRAM",
    THETACThetaRepositoryExposureProgramEnum.aperturePriority: @"APERTURE_PRIORITY",
    THETACThetaRepositoryExposureProgramEnum.shutterPriority: @"SHUTTER_PRIORITY",
    THETACThetaRepositoryExposureProgramEnum.isoPriority: @"ISO_PRIORITY"
  },
  .setToTheta = ^(NSDictionary* rct, THETACThetaRepositoryOptions *opt) {
    id val = [ExposureProgramEnum.toTheta
                 objectForKey:[rct objectForKey:@"exposureProgram"]];
    if (val) {
      opt.exposureProgram = val;
    }
  },
  .setFromTheta = ^(NSMutableDictionary* rct, THETACThetaRepositoryOptions *opt) {
    id val = [ExposureProgramEnum.fromTheta objectForKey:opt.exposureProgram];
    if (val) {
      [rct setObject:val forKey:@"exposureProgram"];
    }
  },
  .setPhotoOption = ^(NSDictionary* rct, THETACPhotoCaptureBuilder *builder) {
    id val = [ExposureProgramEnum.toTheta
                 objectForKey:[rct objectForKey:@"exposureProgram"]];
    if (val) {
      [builder setExposureProgramProgram:val];
    }
  },
  .setVideoOption = ^(NSDictionary* rct, THETACVideoCaptureBuilder *builder) {
    id val = [ExposureProgramEnum.toTheta
                 objectForKey:[rct objectForKey:@"exposureProgram"]];
    if (val) {
      [builder setExposureProgramProgram:val];
    }
  }
};

/**
 * FileFormatTypeEnum converter
 */
static convert_t FileFormatTypeEnum = {
  .toTheta = @{
    @"JPEG": THETACThetaRepositoryFileFormatTypeEnum.jpeg,
    @"MP4": THETACThetaRepositoryFileFormatTypeEnum.mp4,
    @"RAW": THETACThetaRepositoryFileFormatTypeEnum.raw
  },
  .fromTheta = @{
    THETACThetaRepositoryFileFormatTypeEnum.jpeg: @"JPEG",
    THETACThetaRepositoryFileFormatTypeEnum.mp4: @"MP4",
    THETACThetaRepositoryFileFormatTypeEnum.raw: @"RAW"
  }
};

/**
 * FileFormatEnum converter
 */
static convert_t FileFormatEnum = {
  .toTheta = @{
    @"IMAGE_2K": THETACThetaRepositoryFileFormatEnum.image2k,
    @"IMAGE_5K": THETACThetaRepositoryFileFormatEnum.image5k,
    @"IMAGE_6_7K": THETACThetaRepositoryFileFormatEnum.image67k,
    @"RAW_P_6_7K": THETACThetaRepositoryFileFormatEnum.rawP67k,
    @"IMAGE_5_5K": THETACThetaRepositoryFileFormatEnum.image55k,
    @"IMAGE_11K": THETACThetaRepositoryFileFormatEnum.image11k,
    @"VIDEO_HD": THETACThetaRepositoryFileFormatEnum.videoHd,
    @"VIDEO_FULL_HD": THETACThetaRepositoryFileFormatEnum.videoFullHd,
    @"VIDEO_2K": THETACThetaRepositoryFileFormatEnum.video2k,
    @"VIDEO_4K": THETACThetaRepositoryFileFormatEnum.video4k,
    @"VIDEO_2K_30F": THETACThetaRepositoryFileFormatEnum.video2k30f,
    @"VIDEO_2K_60F": THETACThetaRepositoryFileFormatEnum.video2k60f,
    @"VIDEO_4K_30F": THETACThetaRepositoryFileFormatEnum.video4k30f,
    @"VIDEO_4K_60F": THETACThetaRepositoryFileFormatEnum.video4k60f,
    @"VIDEO_5_7K_2F": THETACThetaRepositoryFileFormatEnum.video57k2f,
    @"VIDEO_5_7K_5F": THETACThetaRepositoryFileFormatEnum.video57k5f,
    @"VIDEO_5_7K_30F": THETACThetaRepositoryFileFormatEnum.video57k30f,
    @"VIDEO_7K_2F": THETACThetaRepositoryFileFormatEnum.video7k2f,
    @"VIDEO_7K_5F": THETACThetaRepositoryFileFormatEnum.video7k5f,
    @"VIDEO_7K_10F": THETACThetaRepositoryFileFormatEnum.video7k10f
  },
  .fromTheta = @{
    THETACThetaRepositoryFileFormatEnum.image2k: @"IMAGE_2K",
    THETACThetaRepositoryFileFormatEnum.image5k: @"IMAGE_5K",
    THETACThetaRepositoryFileFormatEnum.image67k: @"IMAGE_6_7K",
    THETACThetaRepositoryFileFormatEnum.rawP67k: @"RAW_P_6_7K",
    THETACThetaRepositoryFileFormatEnum.image55k: @"IMAGE_5_5K",
    THETACThetaRepositoryFileFormatEnum.image11k: @"IMAGE_11K",
    THETACThetaRepositoryFileFormatEnum.videoHd: @"VIDEO_HD",
    THETACThetaRepositoryFileFormatEnum.videoFullHd: @"VIDEO_FULL_HD",
    THETACThetaRepositoryFileFormatEnum.video2k: @"VIDEO_2K",
    THETACThetaRepositoryFileFormatEnum.video4k: @"VIDEO_4K",
    THETACThetaRepositoryFileFormatEnum.video2k30f: @"VIDEO_2K_30F",
    THETACThetaRepositoryFileFormatEnum.video2k60f: @"VIDEO_2K_60F",
    THETACThetaRepositoryFileFormatEnum.video4k30f: @"VIDEO_4K_30F",
    THETACThetaRepositoryFileFormatEnum.video4k60f: @"VIDEO_4K_60F",
    THETACThetaRepositoryFileFormatEnum.video57k2f: @"VIDEO_5_7K_2F",
    THETACThetaRepositoryFileFormatEnum.video57k5f: @"VIDEO_5_7K_5F",
    THETACThetaRepositoryFileFormatEnum.video57k30f: @"VIDEO_5_7K_30F",
    THETACThetaRepositoryFileFormatEnum.video7k2f: @"VIDEO_7K_2F",
    THETACThetaRepositoryFileFormatEnum.video7k5f: @"VIDEO_7K_5F",
    THETACThetaRepositoryFileFormatEnum.video7k10f: @"VIDEO_7K_10F"
  },
  .photoOption = @{
    @"IMAGE_2K": THETACThetaRepositoryPhotoFileFormatEnum.image2k,
    @"IMAGE_5K": THETACThetaRepositoryPhotoFileFormatEnum.image5k,
    @"IMAGE_6_7K": THETACThetaRepositoryPhotoFileFormatEnum.image67k,
    @"RAW_P_6_7K": THETACThetaRepositoryPhotoFileFormatEnum.rawP67k,
    @"IMAGE_5_5K": THETACThetaRepositoryPhotoFileFormatEnum.image55k,
    @"IMAGE_11K": THETACThetaRepositoryPhotoFileFormatEnum.image11k,
  },
  .videoOption = @{
    @"VIDEO_HD": THETACThetaRepositoryVideoFileFormatEnum.videoHd,
    @"VIDEO_FULL_HD": THETACThetaRepositoryVideoFileFormatEnum.videoFullHd,
    @"VIDEO_2K": THETACThetaRepositoryVideoFileFormatEnum.video2k,
    @"VIDEO_4K": THETACThetaRepositoryVideoFileFormatEnum.video4k,
    @"VIDEO_2K_30F": THETACThetaRepositoryVideoFileFormatEnum.video2k30f,
    @"VIDEO_2K_60F": THETACThetaRepositoryVideoFileFormatEnum.video2k60f,
    @"VIDEO_4K_30F": THETACThetaRepositoryVideoFileFormatEnum.video4k30f,
    @"VIDEO_4K_60F": THETACThetaRepositoryVideoFileFormatEnum.video4k60f,
    @"VIDEO_5_7K_2F": THETACThetaRepositoryVideoFileFormatEnum.video57k2f,
    @"VIDEO_5_7K_5F": THETACThetaRepositoryVideoFileFormatEnum.video57k5f,
    @"VIDEO_5_7K_30F": THETACThetaRepositoryVideoFileFormatEnum.video57k30f,
    @"VIDEO_7K_2F": THETACThetaRepositoryVideoFileFormatEnum.video7k2f,
    @"VIDEO_7K_5F": THETACThetaRepositoryVideoFileFormatEnum.video7k5f,
    @"VIDEO_7K_10F": THETACThetaRepositoryVideoFileFormatEnum.video7k10f
  },
  .setToTheta = ^(NSDictionary* rct, THETACThetaRepositoryOptions *opt) {
    id val = [FileFormatEnum.toTheta objectForKey:[rct objectForKey:@"fileFormat"]];
    if (val) {
      opt.fileFormat = val;
    }
  },
  .setFromTheta = ^(NSMutableDictionary* rct, THETACThetaRepositoryOptions *opt) {
    id val = [FileFormatEnum.fromTheta objectForKey:opt.fileFormat];
    if (val) {
      [rct setObject:val forKey:@"fileFormat"];
    }
  },
  .setPhotoOption = ^(NSDictionary* rct, THETACPhotoCaptureBuilder *builder) {
    id val = [FileFormatEnum.photoOption objectForKey:[rct objectForKey:@"fileFormat"]];
    if (val) {
      [builder setFileFormatFileFormat:val];
    }
  },
  .setVideoOption = ^(NSDictionary* rct, THETACVideoCaptureBuilder *builder) {
    id val = [FileFormatEnum.videoOption objectForKey:[rct objectForKey:@"fileFormat"]];
    if (val) {
      [builder setFileFormatFileFormat:val];
    }
  }
};

/**
 * FilterEnum converter
 */
static convert_t FilterEnum = {
  .toTheta = @{
    @"OFF": THETACThetaRepositoryFilterEnum.off,
    @"NOISE_REDUCTION": THETACThetaRepositoryFilterEnum.noiseReduction,
    @"HDR": THETACThetaRepositoryFilterEnum.hdr
  },
  .fromTheta = @{
    THETACThetaRepositoryFilterEnum.off: @"OFF",
    THETACThetaRepositoryFilterEnum.noiseReduction: @"NOISE_REDUCTION",
    THETACThetaRepositoryFilterEnum.hdr: @"HDR"
  },
  .setToTheta = ^(NSDictionary* rct, THETACThetaRepositoryOptions *opt) {
    id val = [FilterEnum.toTheta objectForKey:[rct objectForKey:@"filter"]];
    if (val) {
      opt.filter = val;
    }
  },
  .setFromTheta = ^(NSMutableDictionary* rct, THETACThetaRepositoryOptions *opt) {
    id val = [FilterEnum.fromTheta objectForKey:opt.filter];
    if (val) {
      [rct setObject:val forKey:@"filter"];
    }
  },
  .setPhotoOption = ^(NSDictionary* rct, THETACPhotoCaptureBuilder *builder) {
    id val = [FilterEnum.toTheta objectForKey:[rct objectForKey:@"filter"]];
    if (val) {
      [builder setFilterFilter:val];
    }
  }
};

/**
 * GpsTagRecordingEnum converter
 */
static convert_t GpsTagRecordingEnum = {
  .toTheta = @{
    @"ON": THETACThetaRepositoryGpsTagRecordingEnum.on,
    @"OFF": THETACThetaRepositoryGpsTagRecordingEnum.off
  },
  .fromTheta = @{
    THETACThetaRepositoryGpsTagRecordingEnum.on: @"ON",
    THETACThetaRepositoryGpsTagRecordingEnum.off: @"OFF"
  },
  .setPhotoOption = ^(NSDictionary* rct, THETACPhotoCaptureBuilder *builder) {
    id val = [GpsTagRecordingEnum.toTheta
                 objectForKey:[rct objectForKey:@"_gpsTagRecording"]];
    if (val) {
      [builder setGpsTagRecordingValue:val];
    }
  },
  .setVideoOption = ^(NSDictionary* rct, THETACVideoCaptureBuilder *builder) {
    id val = [GpsTagRecordingEnum.toTheta
                 objectForKey:[rct objectForKey:@"_gpsTagRecording"]];
    if (val) {
      [builder setGpsTagRecordingValue:val];
    }
  }
};

/**
 * IsoEnum converter
 */
static convert_t IsoEnum = {
  .toTheta = @{
    @"ISO_AUTO": THETACThetaRepositoryIsoEnum.isoAuto,
    @"ISO_50": THETACThetaRepositoryIsoEnum.iso50,
    @"ISO_64": THETACThetaRepositoryIsoEnum.iso64,
    @"ISO_80": THETACThetaRepositoryIsoEnum.iso80,
    @"ISO_100": THETACThetaRepositoryIsoEnum.iso100,
    @"ISO_125": THETACThetaRepositoryIsoEnum.iso125,
    @"ISO_160": THETACThetaRepositoryIsoEnum.iso160,
    @"ISO_200": THETACThetaRepositoryIsoEnum.iso200,
    @"ISO_250": THETACThetaRepositoryIsoEnum.iso250,
    @"ISO_320": THETACThetaRepositoryIsoEnum.iso320,
    @"ISO_400": THETACThetaRepositoryIsoEnum.iso400,
    @"ISO_500": THETACThetaRepositoryIsoEnum.iso500,
    @"ISO_640": THETACThetaRepositoryIsoEnum.iso640,
    @"ISO_800": THETACThetaRepositoryIsoEnum.iso800,
    @"ISO_1000": THETACThetaRepositoryIsoEnum.iso1000,
    @"ISO_1250": THETACThetaRepositoryIsoEnum.iso1250,
    @"ISO_1600": THETACThetaRepositoryIsoEnum.iso1600,
    @"ISO_2000": THETACThetaRepositoryIsoEnum.iso2000,
    @"ISO_2500": THETACThetaRepositoryIsoEnum.iso2500,
    @"ISO_3200": THETACThetaRepositoryIsoEnum.iso3200,
    @"ISO_4000": THETACThetaRepositoryIsoEnum.iso4000,
    @"ISO_5000": THETACThetaRepositoryIsoEnum.iso5000,
    @"ISO_6400": THETACThetaRepositoryIsoEnum.iso6400
  },
  .fromTheta = @{
    THETACThetaRepositoryIsoEnum.isoAuto: @"ISO_AUTO",
    THETACThetaRepositoryIsoEnum.iso50: @"ISO_50",
    THETACThetaRepositoryIsoEnum.iso64: @"ISO_64",
    THETACThetaRepositoryIsoEnum.iso80: @"ISO_80",
    THETACThetaRepositoryIsoEnum.iso100: @"ISO_100",
    THETACThetaRepositoryIsoEnum.iso125: @"ISO_125",
    THETACThetaRepositoryIsoEnum.iso160: @"ISO_160",
    THETACThetaRepositoryIsoEnum.iso200: @"ISO_200",
    THETACThetaRepositoryIsoEnum.iso250: @"ISO_250",
    THETACThetaRepositoryIsoEnum.iso320: @"ISO_320",
    THETACThetaRepositoryIsoEnum.iso400: @"ISO_400",
    THETACThetaRepositoryIsoEnum.iso500: @"ISO_500",
    THETACThetaRepositoryIsoEnum.iso640: @"ISO_640",
    THETACThetaRepositoryIsoEnum.iso800: @"ISO_800",
    THETACThetaRepositoryIsoEnum.iso1000: @"ISO_1000",
    THETACThetaRepositoryIsoEnum.iso1250: @"ISO_1250",
    THETACThetaRepositoryIsoEnum.iso1600: @"ISO_1600",
    THETACThetaRepositoryIsoEnum.iso2000: @"ISO_2000",
    THETACThetaRepositoryIsoEnum.iso2500: @"ISO_2500",
    THETACThetaRepositoryIsoEnum.iso3200: @"ISO_3200",
    THETACThetaRepositoryIsoEnum.iso4000: @"ISO_4000",
    THETACThetaRepositoryIsoEnum.iso5000: @"ISO_5000",
    THETACThetaRepositoryIsoEnum.iso6400: @"ISO_6400"
  },
  .setToTheta = ^(NSDictionary* rct, THETACThetaRepositoryOptions *opt) {
    id val = [IsoEnum.toTheta objectForKey:[rct objectForKey:@"iso"]];
    if (val) {
      opt.iso = val;
    }
  },
  .setFromTheta = ^(NSMutableDictionary* rct, THETACThetaRepositoryOptions *opt) {
    id val = [IsoEnum.fromTheta objectForKey:opt.iso];
    if (val) {
      [rct setObject:val forKey:@"iso"];
    }
  },
  .setPhotoOption = ^(NSDictionary* rct, THETACPhotoCaptureBuilder *builder) {
    id val = [IsoEnum.toTheta objectForKey:[rct objectForKey:@"iso"]];
    if (val) {
      [builder setIsoIso:val];
    }
  },
  .setVideoOption = ^(NSDictionary* rct, THETACVideoCaptureBuilder *builder) {
    id val = [IsoEnum.toTheta objectForKey:[rct objectForKey:@"iso"]];
    if (val) {
      [builder setIsoIso:val];
    }
  }
};

/**
 * IsoAutoHighLimitEnum converter
 */
static convert_t IsoAutoHighLimitEnum = {
  .toTheta = @{
    @"ISO_100": THETACThetaRepositoryIsoAutoHighLimitEnum.iso100,
    @"ISO_125": THETACThetaRepositoryIsoAutoHighLimitEnum.iso125,
    @"ISO_160": THETACThetaRepositoryIsoAutoHighLimitEnum.iso160,
    @"ISO_200": THETACThetaRepositoryIsoAutoHighLimitEnum.iso200,
    @"ISO_250": THETACThetaRepositoryIsoAutoHighLimitEnum.iso250,
    @"ISO_320": THETACThetaRepositoryIsoAutoHighLimitEnum.iso320,
    @"ISO_400": THETACThetaRepositoryIsoAutoHighLimitEnum.iso400,
    @"ISO_500": THETACThetaRepositoryIsoAutoHighLimitEnum.iso500,
    @"ISO_640": THETACThetaRepositoryIsoAutoHighLimitEnum.iso640,
    @"ISO_800": THETACThetaRepositoryIsoAutoHighLimitEnum.iso800,
    @"ISO_1000": THETACThetaRepositoryIsoAutoHighLimitEnum.iso1000,
    @"ISO_1250": THETACThetaRepositoryIsoAutoHighLimitEnum.iso1250,
    @"ISO_1600": THETACThetaRepositoryIsoAutoHighLimitEnum.iso1600,
    @"ISO_2000": THETACThetaRepositoryIsoAutoHighLimitEnum.iso2000,
    @"ISO_2500": THETACThetaRepositoryIsoAutoHighLimitEnum.iso2500,
    @"ISO_3200": THETACThetaRepositoryIsoAutoHighLimitEnum.iso3200,
    @"ISO_4000": THETACThetaRepositoryIsoAutoHighLimitEnum.iso4000,
    @"ISO_5000": THETACThetaRepositoryIsoAutoHighLimitEnum.iso5000,
    @"ISO_6400": THETACThetaRepositoryIsoAutoHighLimitEnum.iso6400
  },
  .fromTheta = @{
    THETACThetaRepositoryIsoAutoHighLimitEnum.iso100: @"ISO_100",
    THETACThetaRepositoryIsoAutoHighLimitEnum.iso125: @"ISO_125",
    THETACThetaRepositoryIsoAutoHighLimitEnum.iso160: @"ISO_160",
    THETACThetaRepositoryIsoAutoHighLimitEnum.iso200: @"ISO_200",
    THETACThetaRepositoryIsoAutoHighLimitEnum.iso250: @"ISO_250",
    THETACThetaRepositoryIsoAutoHighLimitEnum.iso320: @"ISO_320",
    THETACThetaRepositoryIsoAutoHighLimitEnum.iso400: @"ISO_400",
    THETACThetaRepositoryIsoAutoHighLimitEnum.iso500: @"ISO_500",
    THETACThetaRepositoryIsoAutoHighLimitEnum.iso640: @"ISO_640",
    THETACThetaRepositoryIsoAutoHighLimitEnum.iso800: @"ISO_800",
    THETACThetaRepositoryIsoAutoHighLimitEnum.iso1000: @"ISO_1000",
    THETACThetaRepositoryIsoAutoHighLimitEnum.iso1250: @"ISO_1250",
    THETACThetaRepositoryIsoAutoHighLimitEnum.iso1600: @"ISO_1600",
    THETACThetaRepositoryIsoAutoHighLimitEnum.iso2000: @"ISO_2000",
    THETACThetaRepositoryIsoAutoHighLimitEnum.iso2500: @"ISO_2500",
    THETACThetaRepositoryIsoAutoHighLimitEnum.iso3200: @"ISO_3200",
    THETACThetaRepositoryIsoAutoHighLimitEnum.iso4000: @"ISO_4000",
    THETACThetaRepositoryIsoAutoHighLimitEnum.iso5000: @"ISO_5000",
    THETACThetaRepositoryIsoAutoHighLimitEnum.iso6400: @"ISO_6400"
  },
  .setToTheta = ^(NSDictionary* rct, THETACThetaRepositoryOptions *opt) {
    id val = [IsoAutoHighLimitEnum.toTheta
                 objectForKey:[rct objectForKey:@"isoAutoHighLimit"]];
    if (val) {
      opt.isoAutoHighLimit = val;
    }
  },
  .setFromTheta = ^(NSMutableDictionary* rct, THETACThetaRepositoryOptions *opt) {
    id val = [IsoAutoHighLimitEnum.fromTheta objectForKey:opt.isoAutoHighLimit];
    if (val) {
      [rct setObject:val forKey:@"isoAutoHighLimit"];
    }
  },
  .setPhotoOption = ^(NSDictionary* rct, THETACPhotoCaptureBuilder *builder) {
    id val = [IsoAutoHighLimitEnum.toTheta
                 objectForKey:[rct objectForKey:@"isoAutoHighLimit"]];
    if (val) {
      [builder setIsoAutoHighLimitIso:val];
    }
  },
  .setVideoOption = ^(NSDictionary* rct, THETACVideoCaptureBuilder *builder) {
    id val = [IsoAutoHighLimitEnum.toTheta
                 objectForKey:[rct objectForKey:@"isoAutoHighLimit"]];
    if (val) {
      [builder setIsoAutoHighLimitIso:val];
    }
  }
};

/**
 * LanguageEnum converter
 */
static convert_t LanguageEnum = {
  .toTheta = @{
    @"DE": THETACThetaRepositoryLanguageEnum.de,
    @"EN_GB": THETACThetaRepositoryLanguageEnum.enGb,
    @"EN_US": THETACThetaRepositoryLanguageEnum.enUs,
    @"FR": THETACThetaRepositoryLanguageEnum.fr,
    @"IT": THETACThetaRepositoryLanguageEnum.it,
    @"JA": THETACThetaRepositoryLanguageEnum.ja,
    @"KO": THETACThetaRepositoryLanguageEnum.ko,
    @"ZH_CN": THETACThetaRepositoryLanguageEnum.zhCn,
    @"ZH_TW": THETACThetaRepositoryLanguageEnum.zhTw
  },
  .fromTheta = @{
    THETACThetaRepositoryLanguageEnum.de: @"DE",
    THETACThetaRepositoryLanguageEnum.enGb: @"EN_GB",
    THETACThetaRepositoryLanguageEnum.enUs: @"EN_US",
    THETACThetaRepositoryLanguageEnum.fr: @"FR",
    THETACThetaRepositoryLanguageEnum.it: @"IT",
    THETACThetaRepositoryLanguageEnum.ja: @"JA",
    THETACThetaRepositoryLanguageEnum.ko: @"KO",
    THETACThetaRepositoryLanguageEnum.zhCn: @"ZH_CN",
    THETACThetaRepositoryLanguageEnum.zhTw: @"ZH_TW"
  },
  .setToTheta = ^(NSDictionary* rct, THETACThetaRepositoryOptions *opt) {
    id val = [LanguageEnum.toTheta objectForKey:[rct objectForKey:@"language"]];
    if (val) {
      opt.language = val;
    }
  },
  .setFromTheta = ^(NSMutableDictionary* rct, THETACThetaRepositoryOptions *opt) {
    id val = [LanguageEnum.fromTheta objectForKey:opt.language];
    if (val) {
      [rct setObject:val forKey:@"language"];
    }
  }
};

/**
 * MaxRecordableTimeEnum converter
 */
static convert_t MaxRecordableTimeEnum = {
  .toTheta = @{
    @"RECORDABLE_TIME_180": THETACThetaRepositoryMaxRecordableTimeEnum.recordableTime180,
    @"RECORDABLE_TIME_300": THETACThetaRepositoryMaxRecordableTimeEnum.recordableTime300,
    @"RECORDABLE_TIME_1500": THETACThetaRepositoryMaxRecordableTimeEnum.recordableTime1500
  },
  .fromTheta = @{
    THETACThetaRepositoryMaxRecordableTimeEnum.recordableTime180: @"RECORDABLE_TIME_180",
    THETACThetaRepositoryMaxRecordableTimeEnum.recordableTime300: @"RECORDABLE_TIME_300",
    THETACThetaRepositoryMaxRecordableTimeEnum.recordableTime1500: @"RECORDABLE_TIME_1500"
  },
  .setToTheta = ^(NSDictionary* rct, THETACThetaRepositoryOptions *opt) {
    id val = [MaxRecordableTimeEnum.toTheta
                 objectForKey:[rct objectForKey:@"maxRecordableTime"]];
    if (val) {
      opt.maxRecordableTime = val;
    }
  },
  .setFromTheta = ^(NSMutableDictionary* rct, THETACThetaRepositoryOptions *opt) {
    id val = [MaxRecordableTimeEnum.fromTheta objectForKey:opt.maxRecordableTime];
    if (val) {
      [rct setObject:val forKey:@"maxRecordableTime"];
    }
  },
  .setVideoOption = ^(NSDictionary* rct, THETACVideoCaptureBuilder *builder) {
    id val = [MaxRecordableTimeEnum.toTheta
                 objectForKey:[rct objectForKey:@"maxRecordableTime"]];
    if (val) {
      [builder setMaxRecordableTimeTime:val];
    }
  }
};

/**
 * OffDelayEnum converter
 */
static convert_t OffDelayEnum = {
  .toTheta = @{
    @"DISABLE": THETACThetaRepositoryOffDelayEnum.disable,
    @"OFF_DELAY_5M": THETACThetaRepositoryOffDelayEnum.offDelay5m,
    @"OFF_DELAY_10M": THETACThetaRepositoryOffDelayEnum.offDelay10m,
    @"OFF_DELAY_15M": THETACThetaRepositoryOffDelayEnum.offDelay15m,
    @"OFF_DELAY_30M": THETACThetaRepositoryOffDelayEnum.offDelay30m
  },
  .fromTheta = @{
    @(THETACThetaRepositoryOffDelayEnum.disable.sec): @"DISABLE",
    @(THETACThetaRepositoryOffDelayEnum.offDelay5m.sec): @"OFF_DELAY_5M",
    @(THETACThetaRepositoryOffDelayEnum.offDelay10m.sec): @"OFF_DELAY_10M",
    @(THETACThetaRepositoryOffDelayEnum.offDelay15m.sec): @"OFF_DELAY_15M",
    @(THETACThetaRepositoryOffDelayEnum.offDelay30m.sec): @"OFF_DELAY_30M"
  },
  .setToTheta = ^(NSDictionary* rct, THETACThetaRepositoryOptions *opt) {
    id rv = [rct objectForKey:@"offDelay"];
    if (rv) {
      if ([rv isKindOfClass:NSString.class]) {
        id val = [OffDelayEnum.toTheta objectForKey:rv];
        opt.offDelay = val;
      } else if ([rv isKindOfClass:NSNumber.class]) {
        id val = [[THETACThetaRepositoryOffDelaySec alloc] initWithSec:[rv intValue]];
        opt.offDelay = val;
      }
    }
  },
  .setFromTheta = ^(NSMutableDictionary* rct, THETACThetaRepositoryOptions *opt) {
    if (opt.offDelay) {
      id val = [OffDelayEnum.fromTheta objectForKey:@(opt.offDelay.sec)];
      if (val) {
        [rct setObject:val forKey:@"offDelay"];
      } else {
        [rct setObject:@(opt.offDelay.sec) forKey:@"offDelay"];
      }
    }
  }
};

/**
 * WhiteBalanceEnum converter
 */
static convert_t WhiteBalanceEnum = {
  .toTheta = @{
    @"AUTO": THETACThetaRepositoryWhiteBalanceEnum.auto_,
    @"DAYLIGHT": THETACThetaRepositoryWhiteBalanceEnum.daylight,
    @"SHADE": THETACThetaRepositoryWhiteBalanceEnum.shade,
    @"CLOUDY_DAYLIGHT": THETACThetaRepositoryWhiteBalanceEnum.cloudyDaylight,
    @"INCANDESCENT": THETACThetaRepositoryWhiteBalanceEnum.incandescent,
    @"WARM_WHITE_FLUORESCENT": THETACThetaRepositoryWhiteBalanceEnum.warmWhiteFluorescent,
    @"DAYLIGHT_FLUORESCENT": THETACThetaRepositoryWhiteBalanceEnum.daylightFluorescent,
    @"DAYWHITE_FLUORESCENT": THETACThetaRepositoryWhiteBalanceEnum.daywhiteFluorescent,
    @"FLUORESCENT": THETACThetaRepositoryWhiteBalanceEnum.fluorescent,
    @"BULB_FLUORESCENT": THETACThetaRepositoryWhiteBalanceEnum.bulbFluorescent,
    @"COLOR_TEMPERATURE": THETACThetaRepositoryWhiteBalanceEnum.colorTemperature,
    @"UNDERWATER": THETACThetaRepositoryWhiteBalanceEnum.underwater
  },
  .fromTheta = @{
    THETACThetaRepositoryWhiteBalanceEnum.auto_: @"AUTO",
    THETACThetaRepositoryWhiteBalanceEnum.daylight: @"DAYLIGHT",
    THETACThetaRepositoryWhiteBalanceEnum.shade: @"SHADE",
    THETACThetaRepositoryWhiteBalanceEnum.cloudyDaylight: @"CLOUDY_DAYLIGHT",
    THETACThetaRepositoryWhiteBalanceEnum.incandescent: @"INCANDESCENT",
    THETACThetaRepositoryWhiteBalanceEnum.warmWhiteFluorescent: @"WARM_WHITE_FLUORESCENT",
    THETACThetaRepositoryWhiteBalanceEnum.daylightFluorescent: @"DAYLIGHT_FLUORESCENT",
    THETACThetaRepositoryWhiteBalanceEnum.daywhiteFluorescent: @"DAYWHITE_FLUORESCENT",
    THETACThetaRepositoryWhiteBalanceEnum.fluorescent: @"FLUORESCENT",
    THETACThetaRepositoryWhiteBalanceEnum.bulbFluorescent: @"BULB_FLUORESCENT",
    THETACThetaRepositoryWhiteBalanceEnum.colorTemperature: @"COLOR_TEMPERATURE",
    THETACThetaRepositoryWhiteBalanceEnum.underwater: @"UNDERWATER"
  },
  .setToTheta = ^(NSDictionary* rct, THETACThetaRepositoryOptions *opt) {
    id val = [WhiteBalanceEnum.toTheta objectForKey:[rct objectForKey:@"whiteBalance"]];
    if (val) {
      opt.whiteBalance = val;
    }
  },
  .setFromTheta = ^(NSMutableDictionary* rct, THETACThetaRepositoryOptions *opt) {
    id val = [WhiteBalanceEnum.fromTheta objectForKey:opt.whiteBalance];
    if (val) {
      [rct setObject:val forKey:@"whiteBalance"];
    }
  },
  .setPhotoOption = ^(NSDictionary* rct, THETACPhotoCaptureBuilder *builder) {
    id val = [WhiteBalanceEnum.toTheta
                 objectForKey:[rct objectForKey:@"whiteBalance"]];
    if (val) {
      [builder setWhiteBalanceWhiteBalance:val];
    }
  },
  .setVideoOption = ^(NSDictionary* rct, THETACVideoCaptureBuilder *builder) {
    id val = [WhiteBalanceEnum.toTheta
                 objectForKey:[rct objectForKey:@"whiteBalance"]];
    if (val) {
      [builder setWhiteBalanceWhiteBalance:val];
    }
  }
};

/**
 * ColorTemperature converter
 */
static convert_t ColorTemperatureCvt = {
  .setToTheta = ^(NSDictionary* rct, THETACThetaRepositoryOptions *opt) {
    NSNumber* val = [rct objectForKey:@"colorTemperature"];
    if (val) {
      opt.colorTemperature = (THETACInt *) val;
    }
  },
  .setFromTheta = ^(NSMutableDictionary* rct, THETACThetaRepositoryOptions *opt) {
    [rct setObject:opt.colorTemperature forKey:@"colorTemperature"];
  },
  .setPhotoOption = ^(NSDictionary* rct, THETACPhotoCaptureBuilder *builder) {
    NSNumber *val = (NSNumber*) [rct objectForKey:@"colorTemperature"];
    if (val) {
      [builder setColorTemperatureKelvin:val.intValue];
    }
  },
  .setVideoOption = ^(NSDictionary* rct, THETACVideoCaptureBuilder *builder) {
    NSNumber *val = (NSNumber*) [rct objectForKey:@"colorTemperature"];
    if (val) {
      [builder setColorTemperatureKelvin:val.intValue];
    }
  }
};

/**
 * DateTimeZone converter
 */
static convert_t DateTimeZoneCvt = {
  .setToTheta = ^(NSDictionary* rct, THETACThetaRepositoryOptions *opt) {
    NSString* val = [rct objectForKey:@"dateTimeZone"];
    if (val) {
      opt.dateTimeZone = val;
    }
  },
  .setFromTheta = ^(NSMutableDictionary* rct, THETACThetaRepositoryOptions *opt) {
    if (opt.dateTimeZone) {
      [rct setObject:opt.dateTimeZone forKey:@"dateTimeZone"];
    }
  }
};

/**
 * IsGpsOn converter
 */
static convert_t IsGpsOnCvt = {
  .setToTheta = ^(NSDictionary* rct, THETACThetaRepositoryOptions *opt) {
    NSNumber* val = [rct objectForKey:@"isGpsOn"];
    if (val) {
      opt.isGpsOn = (THETACBoolean *) val;
    }
  },
  .setFromTheta = ^(NSMutableDictionary* rct, THETACThetaRepositoryOptions *opt) {
    if (opt.isGpsOn) {
      [rct setObject:@(((NSNumber *) opt.isGpsOn).boolValue) forKey:@"isGpsOn"];
    }
  }
};

/**
 * SleepDelay converter
 */
static convert_t SleepDelayEnum = {
  .toTheta = @{
    @"DISABLE": THETACThetaRepositorySleepDelayEnum.disable,
    @"SLEEP_DELAY_3M": THETACThetaRepositorySleepDelayEnum.sleepDelay3m,
    @"SLEEP_DELAY_5M": THETACThetaRepositorySleepDelayEnum.sleepDelay5m,
    @"SLEEP_DELAY_7M": THETACThetaRepositorySleepDelayEnum.sleepDelay7m,
    @"SLEEP_DELAY_10M": THETACThetaRepositorySleepDelayEnum.sleepDelay10m
  },
  .fromTheta = @{
    @(THETACThetaRepositorySleepDelayEnum.disable.sec): @"DISABLE",
    @(THETACThetaRepositorySleepDelayEnum.sleepDelay3m.sec): @"SLEEP_DELAY_3M",
    @(THETACThetaRepositorySleepDelayEnum.sleepDelay5m.sec): @"SLEEP_DELAY_5M",
    @(THETACThetaRepositorySleepDelayEnum.sleepDelay7m.sec): @"SLEEP_DELAY_7M",
    @(THETACThetaRepositorySleepDelayEnum.sleepDelay10m.sec): @"SLEEP_DELAY_10M"
  },
  .setToTheta = ^(NSDictionary* rct, THETACThetaRepositoryOptions *opt) {
    id rv = [rct objectForKey:@"sleepDelay"];
    if (rv) {
      if ([rv isKindOfClass:NSString.class]) {
        id val = [SleepDelayEnum.toTheta objectForKey:rv];
        opt.sleepDelay = val;
      } else if ([rv isKindOfClass:NSNumber.class]) {
        id val = [[THETACThetaRepositorySleepDelaySec alloc] initWithSec:[rv intValue]];
        opt.sleepDelay = val;
      }
    }
  },
  .setFromTheta = ^(NSMutableDictionary* rct, THETACThetaRepositoryOptions *opt) {
    if (opt.sleepDelay) {
      id val = [SleepDelayEnum.fromTheta objectForKey:@(opt.sleepDelay.sec)];
      if (val) {
        [rct setObject:val forKey:@"sleepDelay"];
      } else {
        [rct setObject:@(opt.sleepDelay.sec) forKey:@"sleepDelay"];
      }
    }
  }
};

/**
 * RemainingPictures converter
 */
static convert_t RemainingPicturesCvt = {
  .setToTheta = ^(NSDictionary* rct, THETACThetaRepositoryOptions *opt) {
    NSNumber* val = [rct objectForKey:@"remainingPictures"];
    if (val) {
      opt.remainingPictures = (THETACInt *) val;
    }
  },
  .setFromTheta = ^(NSMutableDictionary* rct, THETACThetaRepositoryOptions *opt) {
    if (opt.remainingPictures) {
      [rct setObject:opt.remainingPictures forKey:@"remainingPictures"];
    }
  }
};

/**
 * RemainingVideoSeconds converter
 */
static convert_t RemainingVideoSecondsCvt = {
  .setToTheta = ^(NSDictionary* rct, THETACThetaRepositoryOptions *opt) {
    NSNumber* val = [rct objectForKey:@"remainingVideoSeconds"];
    if (val) {
      opt.remainingVideoSeconds = (THETACInt *) val;
    }
  },
  .setFromTheta = ^(NSMutableDictionary* rct, THETACThetaRepositoryOptions *opt) {
    if (opt.remainingVideoSeconds) {
      [rct setObject:opt.remainingVideoSeconds forKey:@"remainingVideoSeconds"];
    }
  }
};

/**
 * RemainingSpace converter
 */
static convert_t RemainingSpaceCvt = {
  .setToTheta = ^(NSDictionary* rct, THETACThetaRepositoryOptions *opt) {
    NSNumber* val = [rct objectForKey:@"remainingSpace"];
    if (val) {
      opt.remainingSpace = (THETACLong *) val;
    }
  },
  .setFromTheta = ^(NSMutableDictionary* rct, THETACThetaRepositoryOptions *opt) {
    if (opt.remainingSpace) {
      [rct setObject:opt.remainingSpace forKey:@"remainingSpace"];
    }
  }
};

/**
 * TotalSpace converter
 */
static convert_t TotalSpaceCvt = {
  .setToTheta = ^(NSDictionary* rct, THETACThetaRepositoryOptions *opt) {
    NSNumber* val = [rct objectForKey:@"totalSpace"];
    if (val) {
      opt.totalSpace = (THETACLong *) val;
    }
  },
  .setFromTheta = ^(NSMutableDictionary* rct, THETACThetaRepositoryOptions *opt) {
    if (opt.totalSpace) {
      [rct setObject:opt.totalSpace forKey:@"totalSpace"];
    }
  }
};

/**
 * ShutterVolume converter
 */
static convert_t ShutterVolumeCvt = {
  .setToTheta = ^(NSDictionary* rct, THETACThetaRepositoryOptions *opt) {
    NSNumber* val = [rct objectForKey:@"shutterVolume"];
    if (val) {
      opt.shutterVolume = (THETACInt *) val;
    }
  },
  .setFromTheta = ^(NSMutableDictionary* rct, THETACThetaRepositoryOptions *opt) {
    if (opt.shutterVolume) {
      [rct setObject:opt.shutterVolume forKey:@"shutterVolume"];
    }
  }
};

/**
 * GpsInfo converter
 */
static convert_t GpsInfoCvt = {
  .setToTheta = ^(NSDictionary* rct, THETACThetaRepositoryOptions *opt) {
    NSDictionary *gpsInfoDict = [rct objectForKey:@"gpsInfo"];
    if (gpsInfoDict) {
      opt.gpsInfo =
        [[THETACThetaRepositoryGpsInfo alloc]
          initWithLatitude:((NSNumber*) [gpsInfoDict objectForKey:@"latitude"]).floatValue
                 longitude:((NSNumber*) [gpsInfoDict objectForKey:@"longitude"]).floatValue
                  altitude:((NSNumber*) [gpsInfoDict objectForKey:@"altitude"]).floatValue
              dateTimeZone:[gpsInfoDict objectForKey:@"dateTimeZone"]];
    }
  },

  .setFromTheta = ^(NSMutableDictionary* rct, THETACThetaRepositoryOptions *opt) {
    if (opt.gpsInfo) {
      NSDictionary *gpsInfo = @{
        @"latitude": @(opt.gpsInfo.latitude),
        @"longitude": @(opt.gpsInfo.longitude),
        @"altitude": @(opt.gpsInfo.altitude),
        @"dateTimeZone": opt.gpsInfo.dateTimeZone
      };
      [rct setObject:gpsInfo forKey:@"gpsInfo"];
    }
  }
};

/**
 * OptionNames converter
 */
static NSDictionary *NameToOptionEnum = @{
  @"Aperture": THETACThetaRepositoryOptionNameEnum.aperture,
  @"CaptureMode": THETACThetaRepositoryOptionNameEnum.capturemode,
  @"ColorTemperature": THETACThetaRepositoryOptionNameEnum.colortemperature,
  @"DateTimeZone": THETACThetaRepositoryOptionNameEnum.datetimezone,
  @"ExposureCompensation": THETACThetaRepositoryOptionNameEnum.exposurecompensation,
  @"ExposureDelay": THETACThetaRepositoryOptionNameEnum.exposuredelay,
  @"ExposureProgram": THETACThetaRepositoryOptionNameEnum.exposureprogram,
  @"FileFormat": THETACThetaRepositoryOptionNameEnum.fileformat,
  @"Filter": THETACThetaRepositoryOptionNameEnum.filter,
  @"GpsInfo": THETACThetaRepositoryOptionNameEnum.gpsinfo,
  @"IsGpsOn": THETACThetaRepositoryOptionNameEnum.isgpson,
  @"Iso": THETACThetaRepositoryOptionNameEnum.iso,
  @"IsoAutoHighLimit": THETACThetaRepositoryOptionNameEnum.isoautohighlimit,
  @"Language": THETACThetaRepositoryOptionNameEnum.language,
  @"MaxRecordableTime": THETACThetaRepositoryOptionNameEnum.maxrecordabletime,
  @"OffDelay": THETACThetaRepositoryOptionNameEnum.offdelay,
  @"SleepDelay": THETACThetaRepositoryOptionNameEnum.sleepdelay,
  @"RemainingPictures": THETACThetaRepositoryOptionNameEnum.remainingpictures,
  @"RemainingVideoSeconds": THETACThetaRepositoryOptionNameEnum.remainingvideoseconds,
  @"RemainingSpace": THETACThetaRepositoryOptionNameEnum.remainingspace,
  @"TotalSpace": THETACThetaRepositoryOptionNameEnum.totalspace,
  @"ShutterVolume": THETACThetaRepositoryOptionNameEnum.shuttervolume,
  @"WhiteBalance": THETACThetaRepositoryOptionNameEnum.whitebalance
};

/**
 * OptionNameEnum to OptionName
 */
static NSDictionary *OptionEnumToOption = @{
  @"Aperture": @"aperture",
  @"CaptureMode": @"captureMode",
  @"ColorTemperature": @"colorTemperature",
  @"DateTimeZone": @"dateTimeZone",
  @"ExposureCompensation": @"exposureCompensation",
  @"ExposureDelay": @"exposureDelay",
  @"ExposureProgram": @"exposureProgram",
  @"FileFormat": @"fileFormat",
  @"Filter": @"filter",
  @"GpsInfo": @"gpsInfo",
  @"IsGpsOn": @"isGpsOn",
  @"Iso": @"iso",
  @"IsoAutoHighLimit": @"isoAutoHighLimit",
  @"Language": @"language",
  @"MaxRecordableTime": @"maxRecordableTime",
  @"OffDelay": @"offDelay",
  @"SleepDelay": @"sleepDelay",
  @"RemainingPictures": @"remainingPictures",
  @"RemainingVideoSeconds": @"remainingVideoSeconds",
  @"RemainingSpace": @"remainingSpace",
  @"TotalSpace": @"totalSpace",
  @"ShutterVolume": @"shutterVolume",
  @"WhiteBalance": @"whiteBalance"
};

/** Option converter builder */
typedef convert_t * (^OptionConverter)();

/**
 * option converter tables
 */
static NSDictionary<NSString*, OptionConverter> *NameToConverter = @{
  @"aperture": ^{return &ApertureEnum;},
  @"captureMode": ^{return &CaptureModeEnum;},
  @"colorTemperature": ^{return &ColorTemperatureCvt;},
  @"dateTimeZone": ^{return &DateTimeZoneCvt;},
  @"exposureCompensation": ^{return &ExposureCompensationEnum;},
  @"exposureDelay": ^{return &ExposureDelayEnum;},
  @"exposureProgram": ^{return &ExposureProgramEnum;},
  @"fileFormat": ^{return &FileFormatEnum;},
  @"filter": ^{return &FilterEnum;},
  @"gpsInfo": ^{return &GpsInfoCvt;},
  @"isGpsOn": ^{return &IsGpsOnCvt;},
  @"iso": ^{return &IsoEnum;},
  @"isoAutoHighLimit": ^{return &IsoAutoHighLimitEnum;},
  @"language": ^{return &LanguageEnum;},
  @"maxRecordableTime": ^{return &MaxRecordableTimeEnum;},
  @"offDelay": ^{return &OffDelayEnum;},
  @"sleepDelay": ^{return &SleepDelayEnum;},
  @"remainingPictures": ^{return &RemainingPicturesCvt;},
  @"remainingVideoSeconds": ^{return &RemainingVideoSecondsCvt;},
  @"remainingSpace": ^{return &RemainingSpaceCvt;},
  @"totalSpace": ^{return &TotalSpaceCvt;},
  @"shutterVolume": ^{return &ShutterVolumeCvt;},
  @"whiteBalance": ^{return &WhiteBalanceEnum;},
  @"_gpsTagRecording": ^{return &GpsTagRecordingEnum;}
};

static NSString *EVENT_NAME = @"ThetaFrameEvent";

/**
 * ThetaClientReactNative implementation
 */
@implementation ThetaClientReactNative {
  BOOL hasListeners;
}
RCT_EXPORT_MODULE(ThetaClientReactNative)

/**
 * Will be called when this module's first listener is added.
 */
-(void)startObserving {
  hasListeners = YES;
  // Set up any upstream listeners or background tasks as necessary
}

/**
 * Will be called when this module's last listener is removed, or on dealloc.
 */
-(void)stopObserving {
  hasListeners = NO;
  // Remove upstream listeners, stop unnecessary background tasks
}

/**
 * export constants
 */
- (NSDictionary *)constantsToExport
{
  return @{ @"DEFAULT_EVENT_NAME": EVENT_NAME};
}

/**
 * supported event name list
 */
-(NSArray *)supportedEvents
{
  return @[EVENT_NAME];
}

/**
 * dispatch queue to send events
 */
- (dispatch_queue_t)methodQueue
{
  return dispatch_queue_create("com.ricoh360.event.queue", DISPATCH_QUEUE_SERIAL);
}

/**
 * require main queue setup
 */
+ (BOOL)requiresMainQueueSetup
{
  return YES;
}

/**
 * initialize ThetaRepository
 * @param endPoint endpoint to connect theta
 * @param resolve resolver for initilization
 * @param rejecter rejecter for initialization
 */
RCT_REMAP_METHOD(initialize,
                 initializeWithEndpoint:(NSString *)endPoint
                 withResolver:(RCTPromiseResolveBlock)resolve
                 withRejecter:(RCTPromiseRejectBlock)reject)
{
  if (!endPoint) {
    endPoint = @"http://192.168.1.1";
  }
  NSError *error = nil;
  THETACThetaRepositoryCompanion *companion = THETACThetaRepository.companion;
  [companion doNewInstanceEndpoint:endPoint
                            config:nil
                           timeout:nil
                 completionHandler:^(THETACThetaRepository *repo, NSError *error) {
      if (error) {
        reject(@"error", [error localizedDescription], error);
      } else if (repo) {
        self.theta = repo;
        resolve(@(YES));
      } else {
        reject(@"error", @"can not create repository", nil);
      }
    }];
}

/**
 * getThetaInfo  -  retrieve ThetaInfo from THETA via repository
 * @param resolve resolver for getThetaInfo
 * @param rejecter rejecter for getThetaInfo
 */
RCT_REMAP_METHOD(getThetaInfo,
                 getThetaInfoWithResolver:(RCTPromiseResolveBlock)resolve
                 withRejecter:(RCTPromiseRejectBlock)reject)
{
  [_theta getThetaInfoWithCompletionHandler:^(THETACThetaRepositoryThetaInfo *info,
                                              NSError *error) {
      if (error) {
        reject(@"error", [error localizedDescription], error);
      } else if (info) {
        resolve(@{@"manufacturer": info.manufacturer,
              @"model": info.model,
              @"serialNumber": info.serialNumber,
              @"wlanMacAddress": info.wlanMacAddress != nil? info.wlanMacAddress : [NSNull null],
              @"bluetoothMacAddress": info.bluetoothMacAddress != nil? info.bluetoothMacAddress : [NSNull null],
              @"firmwareVersion": info.firmwareVersion,
              @"supportUrl": info.supportUrl,
              @"hasGps": @(info.hasGps),
              @"hasGyro": @(info.hasGyro),
              @"uptime": @(info.uptime),
              @"api": info.api,
              @"endpoints": @{
                  @"httpPort": @(info.endpoints.httpPort),
                  @"httpUpdatesPort": @(info.endpoints.httpUpdatesPort),
              },
              @"apiLevel": info.apiLevel});
      } else {
        reject(@"error", @"no info", nil);
      }
    }];
}

/**
 * getThetaState  -  retrieve ThetaState from THETA via repository
 * @param resolve resolver for getThetaState
 * @param rejecter rejecter for getThetaState
 */
RCT_REMAP_METHOD(getThetaState,
                 getThetaStateWithResolver:(RCTPromiseResolveBlock)resolve
                 withRejecter:(RCTPromiseRejectBlock)reject)
{
  [_theta getThetaStateWithCompletionHandler:^(THETACThetaRepositoryThetaState *state,
                                               NSError *error) {
      if (error) {
        reject(@"error", [error localizedDescription], error);
      } else if (state) {
        resolve(@{@"fingerprint": state.fingerprint,
              @"batteryLevel":@(state.batteryLevel),
              @"chargingState":[ChargingStateEnum.fromTheta
                                   objectForKey:state.chargingState],
              @"isSdCard":@(state.isSdCard),
              @"recordedTime":@(state.recordedTime),
              @"recordableTime":@(state.recordableTime),
              @"latestFileUrl":state.latestFileUrl});
      } else {
        reject(@"error", @"no state", nil);
      }
    }];
}

/**
 * listFiles  -  retrieve File list from THETA via repository
 * @param fileType file type to retrieve
 * @param startPosition start position to retrieve
 * @param entryCount count to retrieve
 * @param resolve resolver for listFiles
 * @param rejecter rejecter for listFiles
 */
RCT_REMAP_METHOD(listFiles,
                 listFilesWithFileTypeEnum:(NSString*)fileType
                 withStartPosition:(int32_t)startPosition
                 withEntryCount:(int32_t)entryCount
                 withResolver:(RCTPromiseResolveBlock)resolve
                 withRejecter:(RCTPromiseRejectBlock)reject)
{
  [_theta listFilesFileType:[FileTypeEnum.toTheta objectForKey:fileType]
              startPosition:startPosition
                 entryCount:entryCount
          completionHandler:^(NSArray<THETACThetaRepositoryFileInfo *> *items,
                              NSError *error) {
      if (error) {
        reject(@"error", [error localizedDescription], error);
      } else if (items) {
        NSMutableArray *ary = [[NSMutableArray alloc] init];
        for (int i = 0; i < items.count; i++) {
          THETACThetaRepositoryFileInfo *finfo = items[i];
          [ary addObject: @{
              @"name":finfo.name,
                @"size":@(finfo.size),
                @"dateTime":finfo.dateTime,
                @"thumbnailUrl":finfo.thumbnailUrl,
                @"fileUrl":finfo.fileUrl
                }];
        }
        resolve(ary);
      } else {
        reject(@"error", @"no items", nil);
      }
    }];
}

/**
 * deleteFiles  -  delete specified files
 * @param fileUrls file url list to delete
 * @param resolve resolver for deleteFiles
 * @param rejecter rejecter for deleteFiles
 */
RCT_REMAP_METHOD(deleteFiles,
                 deleteFilesWithFileUrls:(NSArray*)fileUrls
                 withResolver:(RCTPromiseResolveBlock)resolve
                 withRejecter:(RCTPromiseRejectBlock)reject)
{
  [_theta deleteFilesFileUrls:fileUrls completionHandler:^(NSError *error) {
      if (error) {
        reject(@"error", [error localizedDescription], error);
      } else {
        resolve(@(YES));
      }
    }];
}

/**
 * deleteAllFiles  -  delete all files
 * @param resolve resolver for deleteAllFiles
 * @param rejecter rejecter for deleteAllFiles
 */
RCT_REMAP_METHOD(deleteAllFiles,
                 deleteAllFilesWithResolver:(RCTPromiseResolveBlock)resolve
                 withRejecter:(RCTPromiseRejectBlock)reject)
{
  [_theta deleteAllFilesWithCompletionHandler:^(NSError *error) {
      if (error) {
        reject(@"error", [error localizedDescription], error);
      } else {
        resolve(@(YES));
      }
    }];
}

/**
 * deleteAllImageFiles  -  delete all image files
 * @param resolve resolver for deleteAllImageFiles
 * @param rejecter rejecter for deleteAllImageFiles
 */
RCT_REMAP_METHOD(deleteAllImageFiles,
                 deleteAllImageFilesWithResolver:(RCTPromiseResolveBlock)resolve
                 withRejecter:(RCTPromiseRejectBlock)reject)
{
  [_theta deleteAllImageFilesWithCompletionHandler:^(NSError *error) {
      if (error) {
        reject(@"error", [error localizedDescription], error);
        return;
      }
      resolve(@(YES));
    }];
}

/**
 * deleteAllVideoFiles  -  delete all video files
 * @param resolve resolver for deleteAllVideoFiles
 * @param rejecter rejecter for deleteAllVideoFiles
 */
RCT_REMAP_METHOD(deleteAllVideoFiles,
                 deleteAllVideoFilesWithResolver:(RCTPromiseResolveBlock)resolve
                 withRejecter:(RCTPromiseRejectBlock)reject)
{
  [_theta deleteAllVideoFilesWithCompletionHandler:^(NSError *error) {
      if (error) {
        reject(@"error", [error localizedDescription], error);
      } else {
        resolve(@(YES));
      }
    }];
}

/**
 * getOptions  -  get options from THETA via repository
 * @param optionNames option name list to get
 * @param resolve resolver for getOptions
 * @param rejecter rejecter for getOptions
 */
RCT_REMAP_METHOD(getOptions,
                 getOptionsWithOptionNames:(NSArray*)optionNames
                 withResolver:(RCTPromiseResolveBlock)resolve
                 withRejecter:(RCTPromiseRejectBlock)reject)
{
  NSMutableArray *optionNameList = [[NSMutableArray alloc] init];
  for (id name in optionNames) {
    [optionNameList addObject:[NameToOptionEnum objectForKey:name]];
  }
  [_theta getOptionsOptionNames:optionNameList
              completionHandler:^(THETACThetaRepositoryOptions *options, NSError *error) {
      if (error) {
        reject(@"error", [error localizedDescription], error);
      } else if (options) {
        NSMutableDictionary *results = [[NSMutableDictionary alloc] init];
        for (id name in optionNames) {
          id option = [OptionEnumToOption objectForKey:name];
          convert_t *convert = [NameToConverter objectForKey:option]();
          if (convert && convert->setFromTheta) {
            convert->setFromTheta(results, options);
          }
        }
        resolve(results);
      } else {
        reject(@"error", @"no options", nil);
      }
    }];
}

/**
 * setOptions  -  set options to THETA via repository
 * @param options options to set
 * @param resolve resolver for setOptions
 * @param rejecter rejecter for setOptions
 */
RCT_REMAP_METHOD(setOptions,
                 setOptionsWithOptions:(NSDictionary*)options
                 withResolver:(RCTPromiseResolveBlock)resolve
                 withRejecter:(RCTPromiseRejectBlock)reject)
{
  THETACThetaRepositoryOptions *newoptions = [[THETACThetaRepositoryOptions alloc] init];
  for (id option in [options allKeys]) {
    convert_t *convert = [NameToConverter objectForKey:option]();
    if (convert && convert->setToTheta) {
      convert->setToTheta(options, newoptions);
    }
  }
  [_theta setOptionsOptions:newoptions completionHandler:^(NSError *error) {
      if (error) {
        reject(@"error", [error localizedDescription], error);
      } else {
        resolve(@(YES));
      }
    }];
}

/**
 * return created data url from [data]
 */
-(NSString*)createDataURL:(NSData*) data
{
  static NSMutableString *uri = nil;
  static NSUInteger prefix_length = 0;
  if (!uri) {
    uri = [[NSMutableString alloc] init];
    [uri appendString:@"data:image/jpeg;base64,"];
    prefix_length = [uri length];
  }
  NSString *b64 = [data base64EncodedStringWithOptions:0];
  NSRange range = {
    .location = prefix_length,
    .length = [uri length] - prefix_length
  };
  [uri replaceCharactersInRange:range withString:b64];
  b64 = nil;
  return uri;
}

/**
 * getLivePreview  -  retrieve live preview frame from THETA via repository
 * @param resolve resolver for getLivePreview
 * @param rejecter rejecter for getLivePreview
 */
RCT_REMAP_METHOD(getLivePreview,
                 getLivePreviewWithResolver:(RCTPromiseResolveBlock)resolve
                 withRejecter:(RCTPromiseRejectBlock)reject)
{
  FrameHandler *_frameHandler =
    [[FrameHandler alloc] initWith:self withEvent:^(NSData *data) {
        if (self->hasListeners) {
          @autoreleasepool {
            NSString *dataUrl = [self createDataURL:data];
            [self sendEventWithName:EVENT_NAME body:@{@"data":dataUrl}];
          }
        }
      }];
  self.previewing = YES;
  [_theta getLivePreviewFrameHandler:_frameHandler completionHandler:^(NSError *error) {
      if (error) {
        reject(@"error", [error localizedDescription], error);
      } else {
        resolve(@(YES));
      }
    }];
}

/**
 * stopLivePreview  -  stop live previewing
 */
RCT_REMAP_METHOD(stopLivePreview,
                 stopLivePreview)
{
  self.previewing = NO;
}

/**
 * getPhotoCaptureBuilder  -  get photo capture builder from repository
 */
RCT_REMAP_METHOD(getPhotoCaptureBuilder,
                 getPhotoCaptureBuilder)
{
  self.photoCaptureBuilder = [_theta getPhotoCaptureBuilder];
}

/**
 * buildPhotoCapture  -  build photo capture
 * @param options option to take picture
 * @param resolve resolver for buildPhotoCapture
 * @param rejecter rejecter for buildPhotoCapture
 */
RCT_REMAP_METHOD(buildPhotoCapture,
                 buildPhotoCaptureWithOptions:(NSDictionary*)options
                 withResolver:(RCTPromiseResolveBlock)resolve
                 withRejecter:(RCTPromiseRejectBlock)reject)
{
  if (!self.photoCaptureBuilder) {
    reject(@"error", @"no photo capture pbuilder", nil);
    return;
  }
  for (id photoOption in [options allKeys]) {
    convert_t *convert = [NameToConverter objectForKey:photoOption]();
    if (convert && convert->setPhotoOption) {
      convert->setPhotoOption(options, self.photoCaptureBuilder);
    }
  }
  [self.photoCaptureBuilder
      buildWithCompletionHandler:^(THETACPhotoCapture *photoCapture, NSError *error) {
      if (error) {
        reject(@"error", [error localizedDescription], error);
        return;
      }
      if (photoCapture) {
        self.photoCapture = photoCapture;
        self.photoCaptureBuilder = nil;
        resolve(@(YES));
      } else {
        reject(@"error", @"no photoCapture", nil);
      }
    }];
}

/**
 * takePicture  -  take a picture with THETA via repository
 * @param resolve resolver for takePicture
 * @param rejecter rejecter for takePicture
 */
RCT_REMAP_METHOD(takePicture,
                 takePictureWithResolver:(RCTPromiseResolveBlock)resolve
                 withRejecter:(RCTPromiseRejectBlock)reject)
{
  if (!self.photoCapture) {
    reject(@"error", @"no photoCapture", nil);
    return;
  }
  PhotoCallback *callback = [[PhotoCallback alloc] initWith:self
                                               withResolver:resolve
                                               withRejecter:reject];
  [self.photoCapture takePictureCallback:callback];
}

/**
 * getVideoCaptureBuilder  -  get video capture builder
 */
RCT_REMAP_METHOD(getVideoCaptureBuilder,
                 getVideoCaptureBuilder)
{
  self.videoCaptureBuilder = [_theta getVideoCaptureBuilder];
}

/**
 * buildVideoCapture  -  build video capture
 * @param options option to capture video
 * @param resolve resolver for buildVideoCapture
 * @param rejecter rejecter for buildVideoCapture
 */
RCT_REMAP_METHOD(buildVideoCapture,
                 buildVideoCaptureWithOptions:(NSDictionary*)options
                 withResolver:(RCTPromiseResolveBlock)resolve
                 withRejecter:(RCTPromiseRejectBlock)reject)
{
  if (!self.videoCaptureBuilder) {
    reject(@"error", @"no video capture builder", nil);
    return;
  }
  for (id videoOption in [options allKeys]) {
    convert_t *convert = [NameToConverter objectForKey:videoOption]();
    if (convert && convert->setVideoOption) {
      convert->setVideoOption(options, self.videoCaptureBuilder);
    }
  }
  [self.videoCaptureBuilder
      buildWithCompletionHandler:^(THETACVideoCapture *videoCapture, NSError *error) {
      if (error) {
        reject(@"error", @"videoCapture build failed", error);
        return;
      }
      if (videoCapture) {
        self.videoCapture = videoCapture;
        self.videoCaptureBuilder = nil;
        resolve(@(YES));
      } else {
        reject(@"error", @"no videoCapture", nil);
      }
    }];
}

/**
 * startCapture  -  start capture video
 * @param resolve resolver for startCapture
 * @param rejecter rejecter for startCapture
 */
RCT_REMAP_METHOD(startCapture,
                 startCaptureWithResolver:(RCTPromiseResolveBlock)resolve
                 withRejecter:(RCTPromiseRejectBlock)reject)
{
  if (!self.videoCapture) {
    reject(@"error", @"no videoCapture", nil);
    return;
  }
  VideoCallback *callback = [[VideoCallback alloc] initWith:self
                                               withResolver:resolve
                                               withRejecter:reject];
  self.videoCapturing = [self.videoCapture startCaptureCallback:callback];
}

/**
 * stopCapture  -  stop capturing video
 */
RCT_REMAP_METHOD(stopCapture,
                 stopCapture)
{
  if (!self.videoCapturing) {
    // todo warning
    return;
  }
  [self.videoCapturing stopCapture];
}

/**
 * getMetadata  -  retrieve meta data from THETA via repository
 * @param resolve resolver for getMetadata
 * @param rejecter rejecter for getMetadata
 */
RCT_REMAP_METHOD(getMetadata,
                 getMetadataWithFileUrl:(NSString*)fileUrl
                 withResolver:(RCTPromiseResolveBlock)resolve
                 withRejecter:(RCTPromiseRejectBlock)reject)
{
  [_theta getMetadataFileUrl:fileUrl
           completionHandler:^(THETACKotlinPair<THETACThetaRepositoryExif *,
                               THETACThetaRepositoryXmp *> *metaData,
                               NSError *error) {
      if (error) {
        reject(@"error", [error localizedDescription], error);
      } else if (metaData) {
        NSDictionary* exif = nil;
        NSDictionary* xmp = nil;
        if (metaData.first) {
          exif = @{
            @"exifVersion": metaData.first.exifVersion,
            @"dateTime": metaData.first.dateTime,
            @"imageWidth": metaData.first.imageWidth,
            @"imageLength": metaData.first.imageLength,
            @"gpsLatitude": metaData.first.gpsLatitude,
            @"gpsLongitude": metaData.first.gpsLongitude
          };
        }
        if (metaData.second) {
          xmp = @{
            @"poseHeadingDegrees": metaData.second.poseHeadingDegrees,
            @"fullPanoWidthPixels": @(metaData.second.fullPanoWidthPixels),
            @"fullPanoHeightPixels": @(metaData.second.fullPanoHeightPixels)
          };
        }
        resolve(@{@"exif": exif, @"xmp": xmp});
      } else {
        reject(@"error", @"no metaData", nil);
      }
    }];
}

/**
 * reset  -  reset THETA via repository
 * @param resolve resolver for reset
 * @param rejecter rejecter for reset
 */
RCT_REMAP_METHOD(reset,
                 resetWithResolver:(RCTPromiseResolveBlock)resolve
                 withRejecter:(RCTPromiseRejectBlock)reject)
{
  [_theta resetWithCompletionHandler:^(NSError *error) {
      if (error) {
        reject(@"error", [error localizedDescription], error);
      } else {
        resolve(@(YES));
      }
    }];
}

/**
 * restoreSettings  -  restore THETA setting before initialization
 * @param resolve resolver for restoreSettings
 * @param rejecter rejecter for restoreSettings
 */
RCT_REMAP_METHOD(restoreSettings,
                 restoreSettingsWithResolver:(RCTPromiseResolveBlock)resolve
                 withRejecter:(RCTPromiseRejectBlock)reject)
{
  [_theta restoreSettingsWithCompletionHandler:^(NSError *error) {
      if (error) {
        reject(@"error", [error localizedDescription], error);
      } else {
        resolve(@(YES));
      }
    }];
}

/**
 * stopSelfTimer  -  stop self timer of THETA
 * @param resolve resolver for stopSelfTimer
 * @param rejecter rejecter for stopSelfTimer
 */
RCT_REMAP_METHOD(stopSelfTimer,
                 stopSelfTimerWithResolver:(RCTPromiseResolveBlock)resolve
                 withRejecter:(RCTPromiseRejectBlock)reject)
{
  [_theta stopSelfTimerWithCompletionHandler:^(NSError *error) {
      if (error) {
        reject(@"error", [error localizedDescription], error);
      } else {
        resolve(@(YES));
      }
    }];
}

/**
 * convertVideoFormats  -  convert video format in THETA
 * @param fileUrl file url to convert
 * @param toLowResolution to lower resolution
 * @param applyTopBottomCorrection apply top bottom correction
 * @param resolve resolver for convertVideoFormats
 * @param rejecter rejecter for convertVideoFormats
 */
RCT_REMAP_METHOD(convertVideoFormats,
                 convertVideoFormatsWithFileUrl:(NSString *)fileUrl
                 withToLowResolution:(BOOL)toLowResolution
                 withApplyTopBottomCorrection:(BOOL)applyTopBottomCorrection
                 withResolver:(RCTPromiseResolveBlock)resolve
                 withRejecter:(RCTPromiseRejectBlock)reject)
{
  [_theta convertVideoFormatsFileUrl:fileUrl
                   toLowResolution:toLowResolution
            applyTopBottomCorrection:applyTopBottomCorrection
                   completionHandler:^(NSString *convertedUrl, NSError *error) {
      if (error) {
        reject(@"error", [error localizedDescription], error);
      } else if (convertedUrl) {
        resolve(convertedUrl);
      } else {
        reject(@"error", @"no convertVideo", nil);
      }
    }];
}

/**
 * cancelVideoconvert  -  cancel converting video format in THETA
 * @param resolve resolver for cancelVideoconvert
 * @param rejecter rejecter for cancelVideoconvert
 */
RCT_REMAP_METHOD(cancelVideoConvert,
                 cancelVideoConvertWithResolver:(RCTPromiseResolveBlock)resolve
                 withRejecter:(RCTPromiseRejectBlock)reject)
{
  [_theta cancelVideoConvertWithCompletionHandler:^(NSError *error) {
      if (error) {
        reject(@"error", [error localizedDescription], error);
      } else {
        resolve(@(YES));
      }
    }];
}

/**
 * finishWlan  -  finish wireless lan
 * @param resolve resolver for finishWlan
 * @param rejecter rejecter for finishWlan
 */
RCT_REMAP_METHOD(finishWlan,
                 finishWlanWithResolver:(RCTPromiseResolveBlock)resolve
                 withRejecter:(RCTPromiseRejectBlock)reject)
{
  [_theta finishWlanWithCompletionHandler:^(NSError *error) {
      if (error) {
        reject(@"error", [error localizedDescription], error);
      } else {
        resolve(@(YES));
      }
    }];
}

/**
 * listAccessPoints  -  list access points
 * @param resolve resolver for listAccessPoints
 * @param rejecter rejecter for listAccessPoints
 */
RCT_REMAP_METHOD(listAccessPoints,
                 listAccessPointsWithResolver:(RCTPromiseResolveBlock)resolve
                 withRejecter:(RCTPromiseRejectBlock)reject)
{
  [_theta listAccessPointsWithCompletionHandler:^(NSArray *accessPointList, NSError *error) {
      if (error) {
        reject(@"error", [error localizedDescription], error);
      } else if (accessPointList) {
        NSMutableArray *ary = [[NSMutableArray alloc] init];
          for (int i = 0; i < accessPointList.count; i++) {
              THETACThetaRepositoryAccessPoint *apinfo = accessPointList[i];
              [ary addObject: @{
                  @"ssid": apinfo.ssid,
                    @"ssidStealth": @(apinfo.ssidStealth),
                    @"authMode": [AuthModeEnum.fromTheta objectForKey:apinfo.authMode],
                    @"connectionPriority": @(apinfo.connectionPriority),
                    @"usingDhcp": @(apinfo.usingDhcp),
                    @"ipAddress": apinfo.ipAddress,
                    @"subnetMask": apinfo.subnetMask,
                    @"defaultGateway": apinfo.defaultGateway
                    }];
          }
          resolve(ary);
      } else {
        reject(@"error", @"no access point", nil);
      }
    }];
}

/**
 * setAccessPointDynamically  -  set access point with dhcp
 * @param ssid ssid to connect
 * @param ssidStealth ssid is stealth or not
 * @param authMode auth mode to connect
 * @param password password to connect with auth
 * @param connectionPriority connection priority
 * @param resolve resolver for setAccessPointDynamically
 * @param rejecter rejecter for setAccessPointDynamically
 */
RCT_REMAP_METHOD(setAccessPointDynamically,
                 setAccessPointDynamicallyWithSsid:(NSString *)ssid
                 withSsidStealth:(BOOL)ssidStealth
                 withAuthMode:(NSString *)authMode
                 withPassword:(NSString *)password
                 withConnectionPrioryty:(int32_t)connectionPriority
                 withResolver:(RCTPromiseResolveBlock)resolve
                 withRejecter:(RCTPromiseRejectBlock)reject)
{
  [_theta setAccessPointDynamicallySsid:ssid
                            ssidStealth:ssidStealth
                               authMode:[AuthModeEnum.toTheta objectForKey:authMode]
                               password:password
                     connectionPriority:connectionPriority
                      completionHandler:^(NSError *error) {
      if (error) {
        reject(@"error", [error localizedDescription], error);
      } else {
        resolve(@(YES));
      }
    }];
}

/**
 * setAccessPointStatically  -  set access point with static connection info
 * @param ssid ssid to connect
 * @param ssidStealth ssid is stealth or not
 * @param authMode auth mode to connect
 * @param password password to connect with auth
 * @param connectionPriority connection priority
 * @param ipAddress static ipaddress to connect
 * @param subnetMask subnet mask for ip address
 * @param defaultGateway default gateway address
 * @param resolve resolver for setAccessPointStatically
 * @param rejecter rejecter for setAccessPointStatically
 */
RCT_REMAP_METHOD(setAccessPointStatically,
                 setAccessPointStaticallyWithSsid:(NSString *)ssid
                 withSsidStealth:(BOOL)ssidStealth
                 withAuthMode:(NSString *)authMode
                 withPassword:(NSString *)password
                 withConnectionPrioryty:(int32_t)connectionPriority
                 withIpAddress:(NSString *)ipAddress
                 withSubnetMask:(NSString *)subnetMask
                 withDefaultGateway:(NSString *)defaultGateway
                 withResolver:(RCTPromiseResolveBlock)resolve
                 withRejecter:(RCTPromiseRejectBlock)reject)
{
  [_theta setAccessPointStaticallySsid:ssid
                           ssidStealth:ssidStealth
                              authMode:[AuthModeEnum.toTheta objectForKey:authMode]
                              password:password
                    connectionPriority:connectionPriority
                             ipAddress:ipAddress
                            subnetMask:subnetMask
                        defaultGateway:defaultGateway
                     completionHandler:^(NSError *error) {
      if (error) {
        reject(@"error", [error localizedDescription], error);
      } else {
        resolve(@(YES));
      }
    }];
}

/**
 * deleteAccessPoint  -  delete access point related ssid
 * @param ssid ssid to delete
 * @param resolve resolver for setAccessPointStaticcaly
 * @param rejecter rejecter for setAccessPointStaticcaly
 */
RCT_REMAP_METHOD(deleteAccessPoint,
                 deleteAccessPointWithSsid:(NSString *)ssid
                 withResolver:(RCTPromiseResolveBlock)resolve
                 withRejecter:(RCTPromiseRejectBlock)reject)
{
  [_theta deleteAccessPointSsid:ssid
              completionHandler:^(NSError *error) {
      if (error) {
        reject(@"error", [error localizedDescription], error);
      } else {
        resolve(@(YES));
      }
    }];
}

/**
 * setBluetoothDevice  -  register uuid of a BLE device
 * @param uuid uuid to set
 * @param resolve resolver for setAccessPointStaticcaly
 * @param rejecter rejecter for setAccessPointStaticcaly
 */
RCT_REMAP_METHOD(setBluetoothDevice,
                 setBluetoothDeviceWithUuid:(NSString *)uuid
                 withResolver:(RCTPromiseResolveBlock)resolve
                 withRejecter:(RCTPromiseRejectBlock)reject)
{
  [_theta setBluetoothDeviceUuid:uuid
              completionHandler:^(NSString *deviceName, NSError *error) {
      if (error) {
        reject(@"error", [error localizedDescription], error);
      } else {
        resolve(deviceName);
      }
    }];
}

// Don't compile this code when we build for the old architecture.
#ifdef RCT_NEW_ARCH_ENABLED
- (std::shared_ptr<facebook::react::TurboModule>)getTurboModule:
(const facebook::react::ObjCTurboModule::InitParams &)params
{
  return std::make_shared<facebook::react::NativeThetaClientReactNativeSpecJSI>(params);
}
#endif

@end
