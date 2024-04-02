# THETA Client Tutorial for React Native

## Advance preparation

Connect the wireless LAN between the smartphone and THETA that runs on the application using this SDK.

## Initialize THETA Client

```Typescript
import {initialize} from 'theta-client-react-native';

initialize()
  .then(() => {
    // success
  })
  .catch(() => {
    // handle error
  });

OR

initialize('http://<IP address>:<port number>')
  .then(() => {
    // success
  })
  .catch(() => {
    // handle error
  });
```

- THETA IP ADDRESS

| Mode        | Address           |
| ----------- | ----------------- |
| Direct mode | 192.168.1.1       |
| Other       | Camera IP address |

- When downloading images or videos from THETA, the connection is plain, so the settings for each platform are required depending on the destination address (default 192.168.1.1).

- iOS: shows an example of Info.plist by default.
  Xcode `Signing & Capabilities` -> `App Transport Security Exception` can also be added.

```xml
    <?xml version="1.0" encoding="UTF-8"?>
    <!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
    <plist version="1.0">
      <dict>
        <key>NSAppTransportSecurity</key>
        <dict>
          <key>NSAllowsArbitraryLoads</key>
          <false/>
          <key>NSExceptionDomains</key>
          <dict>
        <key>192.168.1.1</key>
        <dict>
          <key>NSIncludesSubdomain</key>
          <false/>
          <key>NSTemporaryExceptionAllowsInsecureHTTPLoads</key>
          <true/>
          <key>NSTemporaryExceptionRequiresForwardSecrecy</key>
          <false/>
        </dict>
          </dict>
        </dict>
      </dict>
    </plist>
```

- Android: Res/xml/network_security_config.xml shows an example of the default case.

```xml
    <?xml version="1.0" encoding="utf-8"?>
    <network-security-config>
      <base-config cleartextTrafficPermitted="false" />
      <domain-config cleartextTrafficPermitted="true">
        <domain includeSubdomains="false">192.168.1.1</domain>
      </domain-config>
    </network-security-config>
```

## Shoot still images

First, shooting settings are performed using `getPhotoCaptureBuilder()` to create `PhotoCapture` objects.

```Typescript
import {
  getPhotoCaptureBuilder,
  IsoAutoHighLimitEnum,
  PhotoFileFormatEnum,
} from 'theta-client-react-native';

getPhotoCaptureBuilder()
    .setIsoAutoHighLimit(IsoAutoHighLimitEnum.ISO_1000)
    .setFileFormat(PhotoFileFormatEnum.IMAGE_5K)
    .build()
    .then((photoCapture) => {
      // success build photoCapture
    })
    .catch((error) => {
      // handle error
    });
```

The above example sets the maximum ISO sensitivity to 1000 and the file format to IMAGE_5K.

See [Display a preview](#preview) for instructions on how to view preview.
Next, we call `PhotoCapture.takePicture()` to shoot still pictures.

```Typescript
photoCapture.takePicture()
  .then(fileUrl => {
    // HTTP GET to fileUrl and receiving a JPEG file
  })
  .catch(error => {
    // catch error while take picture
  });
```

#### Set the bitrate value for still image capture (THETA X)

The bitrate value for still image capture can be set in THETA X(see [api-spec](https://github.com/ricohapi/theta-api-specs/blob/main/theta-web-api-v2.1/options/_bitrate.md)).
To set this value with THETA Client, follow the steps below.

1. Create a `PhotoCapture` object
1. Call `setOptions()` to set the `bitrate` value
1. Call `takePicture()`

The reason this step is necessary is that the `bitrate` value on the camera is reset when the `captureMode` value is set to `image` in `PhotoCaptureBuilder.build()`.

```typescript
const photoCapture = await getPhotoCaptureBuilder().build();
await setOptions({ bitrate: 1048576 });
const url = await photoCapture.takePicture();
```

## Shoot a video

First, you set up shooting using `getVideoCaptureBuilder()` and create `VideoCapture` objects.

```Typescript
import {
  getVideoCaptureBuilder,
  IsoAutoHighLimitEnum,
  VideoFileFormatEnum,
} from 'theta-client-react-native';

getVideoCaptureBuilder()
  .setIsoAutoHighLimit(IsoAutoHighLimitEnum.ISO_1000)
  .setFileFormat(VideoFileFormatEnum.VIDEO_HD)
  .build()
  .then((videoCapture) => {
    // success build videoCapture
  })
  .catch((error) => {
    // handle error
  });
```

In the example above, the maximum ISO sensitivity is set to 800 and the file format is set to `VIDEO_HD`.

See [Display a preview](#preview) for instructions on how to view preview.

Next, we call `VideoCapture.startCapture()` to start recording videos.

```Typescript
VideoCapture
  .startCapture((error) => {
    // handle error of stopCapture
  })
  .then((fileUrl) => {
    // send GET requests and receiving MP4 files
  })
  .catch((error) => {
    // handle error of startCapture
  });
```

Next, call `VideoCapture.stopCapture()` to finish recording the video.
If successful, then the URL of the shot file is called then as shown above.

```Typescript
VideoCapture.stopCapture();
```

## <a id="preview"></a>Display a preview

The preview is an equirectangular form of motion JPEG.

When calling `getLivePreview()`, the event THETA_FRAME_EVENT occurs every time each frame of the preview is received.
The DATA URL of the frame data (JPEG) is passed to the event data.
To exit the preview, call `stopLivePreview()`.

```Typescript
import * as React from 'react';
import {NativeModules, NativeEventEmitter} from 'react-native';
import {
  getLivePreview,
  stopLivePreview,
  THETA_FRAME_EVENT,
} from 'theta-client-react-native';

const LivePreview = () => {
  [dataUrl, setDataUrl] = React.useState<string | undefined>();

  React.useEffect(() => {
    const eventListener = new NaiveEventEmitter(NativeModules.ThetaClientReactNative)
      .addListener(THETA_FRAME_EVENT, event => {
        setDataUrl(event.data);
      });
    getLivePreview()
      .then(() => {
        // preview done
      })
      .catch(() => {
        // handle error while previewing
      });
    return (() => {
      stopLivePreview();
      eventListener.remove();
    });
  }, []);

  return (
    ... Omitted
    <Image source={{uri: dataUrl}} style=... />
    ... Omitted
  );
};
```

## Set the camera

To configure the camera, set the desired settings to `Options` and call up `SetOptions()`.

```Typescript
import {
  Options,
  setOptions,
  ApertureEnum,
} from 'theta-client-react-native';

let options: Options = {};
options.aperture = ApertureEnum.APERTURE_AUTO;
setOptions(options)
  .then(()=>{
    // done
  })
  .catch(error => {
    // handle error
  });
```

## Acquire camera settings

To get the camera settings, specify the list of options you want to obtain and call `getOptions()`.

```Typescript
import {
  OptionNameEnum,
  getOptions,
} from 'theta-client-react-native';


let optionNames: OptionNameEnum[] = [
  OptionNameEnum.Aperture,
  OptionNameEnum.CaptureMode,
  OptionNameEnum.ColorTemperature
];
getOptions(optionNames)
  .then(options => {
    // handle options
  })
  .catch(error => {
    // handle error
  });
```

## List still images and videos in THETA

The list of still pictures (JPEG file) and videos (MP4 file) in THETA can be obtained using `listFiles(fileType:FileType Enum, startPosition:number, entryCount:number)`
The return type of `listFiles()` is `ThetaFiles`, and property `fileList` of `ThetaFiles` is the list of files in THETA.
`fileList` is a list of `FileInfo`.

- FileTypeEnum

  | Value | Content                           |
  | ----- | --------------------------------- |
  | IMAGE | List of still images (JPEG files) |
  | VIDEO | List of videos (MP4 files)        |
  | ALL   | List all files                    |

- ThetaFiles

  | Property name | Type             | Contents                                                                                                                                          |
  | ------------- | ---------------- | ------------------------------------------------------------------------------------------------------------------------------------------------- |
  | fileList      | List\<FileInfo\> | The list of files in THETA                                                                                                                        |
  | totalEntries  | number           | Number of files in THETA (see [api spec](https://github.com/ricohapi/theta-api-specs/blob/main/theta-web-api-v2.1/commands/camera.list_files.md)) |

- FileInfo

  | Property name | Type   | Contents                                     |
  | ------------- | ------ | -------------------------------------------- |
  | name          | string | Represents the file name                     |
  | size          | number | Indicates the file size (in bytes)           |
  | dateTime      | string | Shooting date and time (YYYY:MM:DD HH:MM:SS) |
  | fileUrl       | string | Represents the URL of the file               |
  | thumbnailUrl  | string | Represents a thumbnail URL                   |

```Typescript
import {listFiles, FileTypeEnum} from 'theta-client-react-native';

await listFiles(FileTypeEnum.IMAGE, 0, 1000)
  .then(({fileList, totalEntries}) => {
    // handle file list
  })
  .catch(error => {
    // handle error
  });
```

## Download still images and videos

You can retrieve data from the URL of still pictures (JPEG file) or videos (MP4 file).
You can download using the following functions.
For still images, you can download the image and display it by setting the URL to the source attribute of the Image component.

```Typescript
fetch(url)
  .then((res) => {
    if (!res.ok) {
      // error
      throw Error('...');
    }
    return res.blob();
  })
  .then((blob) => {
    // processing data
    // eg: URL.createObjectURL(blob);
  })
  .catch((error) => {
    // handle error
  });
```

## Obtain thumbnails

Thumbnails of the files in THETA can be downloaded using the `thumbnailUrl` of the `FileInfo` aquired by `listFiles()` as follows:

```Typescript
fetch(fileInfo.thumbnailUrl)
  .then((res) => {
    if (!res.ok) {
      // error
      throw Error('...');
    }
    return res.blob();
  })
  .then((blob) => {
    // processing data
    // eg: URL.createObjectURL(blob);
  })
  .catch((error) => {
    // handle error
  });
```

## Get the THETA information

To obtain the information about the connected THETA, call `getThetaInfo()`.
If the call is successful, you can get `ThetaInfo`.

```Typescript
import {getThetaInfo} from 'theta-client-react-native';

getThetaInfo()
  .then((thetaInfo) => {
    // processing thetaInfo
  })
  .catch((error) => {
    // handle error
  });
```

## Get the state of THETA

To get the state of the connected THETA, call `getThetaState()`.
Successful calling allows you to acquire the `ThetaState`.

```Typescript
import {getThetaState} from 'theta-client-react-native';

getThetaState()
  .then((thetaState) => {
    // processing thetaState
  })
  .catch((error) => {
    // handle error
  })
```

## Reset THETA

Call `reset()` to reset the connected THETA.

```Typescript
import {reset} from 'theta-client-react-native';

reset()
  .then(() => {
    // reset done
  })
  .catch((error) => {
    // handle error
  });
```
