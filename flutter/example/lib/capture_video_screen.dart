import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:theta_client_flutter/theta_client_flutter.dart';
import 'package:theta_client_flutter_example/video_screen.dart';

class CaptureVideoScreen extends StatefulWidget {
  const CaptureVideoScreen({Key? key}) : super(key: key);

  @override
  State<StatefulWidget> createState() {
    return _CaptureVideoScreen();
  }
}

class _CaptureVideoScreen extends State<CaptureVideoScreen> with WidgetsBindingObserver {
  final _thetaClientPlugin = ThetaClientFlutter();
  final String endpoint = 'http://192.168.1.1:80/';

  Uint8List frameData = Uint8List(0);
  bool previewing = false;
  bool shooting = false;
  VideoCaptureBuilder? builder;
  VideoCapture? videoCapture;
  VideoCapturing? videoCapturing;

  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addObserver(this);
    initialize();
  }
  @override
  void deactivate() {
    WidgetsBinding.instance.removeObserver(this);
    stopLivePreview();
    super.deactivate();
    debugPrint('close CaptureVideo');
  }

  @override
  void didChangeAppLifecycleState(AppLifecycleState state) {
    switch (state) {
      case AppLifecycleState.resumed:
      onResume();
        break;
      case AppLifecycleState.paused:
      onPause();
        break;
      default:
        break;
    }
  }

  void onResume() {
    debugPrint('onResume');
    _thetaClientPlugin.isInitialized()
      .then((isInit) {
        if (isInit) {
          startLivePreview();
        }
      });
  }

  void onPause() {
    debugPrint('onPause');
    stopLivePreview();
  }

  @override
  Widget build(BuildContext context) {
    return WillPopScope(
        child: Scaffold(
          appBar: AppBar(
            title: const Text('Capture Video')
            ),
          body: Stack(
            fit: StackFit.expand,
            children: [
              Container(
                color: Colors.black,
                child: Center(
                  child: Image.memory(
                    frameData,
                    errorBuilder: (a, b, c) {
                      return Container(
                        color: Colors.black,
                      );
                    },
                    gaplessPlayback: true,
                  ),
                )
              ),
              Container(
                alignment: const Alignment(0, 0.8),
                child:
                MaterialButton(
                  height: 80,
                  shape: const CircleBorder(),
                  color: Colors.white,
                  onPressed: () {
                    if (shooting) {
                      stopVideoCapture();
                      return;
                    }
                    startVideoCapture();
                  },
                )
              ),
            ],
          ),
        ),
        onWillPop: () => backButtonPress(context),
      );
  }

  Future<bool> backButtonPress(BuildContext context) async {
    debugPrint('backButtonPress');
    stopLivePreview();
    return true;
  }

  void initialize() {
    debugPrint('init CaptureVideo');
    _thetaClientPlugin.isInitialized().then((isInit) {
      if (!isInit) {
        _thetaClientPlugin.initialize(endpoint)
          .then((isError) {
            debugPrint('initialized.');
            startLivePreview();
          }
        )
          .onError((error, stackTrace) {
            debugPrint('Error initialize.');
            backScreen();
          });
      } else {
        debugPrint('initialized.');
        startLivePreview();
      }
    });

    // initialize VideoCapture
    builder = _thetaClientPlugin.getVideoCaptureBuilder();
    builder!.build()
      .then((value) {
        videoCapture = value;
        debugPrint('Ready VideoCapture');
      })
      .onError((error, stackTrace) {
        debugPrint('Error. VideoCaptureBuild');
      });

    debugPrint('initializing...');
  }

  bool frameHandler(Uint8List frameData) {
    if (!mounted) return false;
    // debugPrint('frameHandler: ${frameData.length}');
    setState(() {
      this.frameData = frameData;
    });
    return previewing;
  }

  void startLivePreview() {
    previewing = true;
    _thetaClientPlugin.getLivePreview(frameHandler)
      .then((value) {
        debugPrint('LivePreview end.');
      })
      .onError((error, stackTrace) {
        debugPrint('Error getLivePreview.');
        backScreen();
      });
    debugPrint('LivePreview starting..');
  }

  void stopLivePreview() {
    previewing = false;
  }

  void backScreen() {
    stopLivePreview();
    Navigator.pop(context);
  }

  void startVideoCapture() {
    if (shooting) {
        debugPrint('already shooting');
      return;
    }
    setState(() {
      shooting = true;
    });
    videoCapturing = videoCapture!.startCapture((fileUrl) { 
      setState(() {
        shooting = false;
      });
      debugPrint('capture video: $fileUrl');
      if (!mounted) return;

      stopLivePreview();
      Navigator.of(context).push(
        MaterialPageRoute(builder: (_) => VideoScreen(
          name: 'Capture Video',
          fileUrl: fileUrl,
          )
        )
      ).then((value) => startLivePreview());
    }, (exception) {
      setState(() {
        shooting = false;
      });
      debugPrint(exception.toString());
    });
  }

  void stopVideoCapture() {
    if (!shooting || videoCapturing == null) {
        debugPrint('Not start capture.');
      return;
    }
    videoCapturing!.stopCapture();
  }
}
