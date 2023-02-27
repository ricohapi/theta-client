import 'package:demo_flutter/video_screen.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:theta_client_flutter/theta_client_flutter.dart';

import 'message_box.dart';

class CaptureVideoScreen extends StatefulWidget {
  const CaptureVideoScreen({Key? key}) : super(key: key);

  @override
  State<StatefulWidget> createState() {
    return _CaptureVideoScreen();
  }
}

class _CaptureVideoScreen extends State<CaptureVideoScreen> with WidgetsBindingObserver {
  final _thetaClientFlutter = ThetaClientFlutter();

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
    _thetaClientFlutter.isInitialized()
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
                  color: shooting ? Colors.white : Colors.red,
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
    // initialize VideoCapture
    builder = _thetaClientFlutter.getVideoCaptureBuilder();
    builder!.build()
      .then((value) {
        videoCapture = value;
        debugPrint('Ready VideoCapture');
        Future.delayed(const Duration(milliseconds: 500), (){}).then((value) {
          // Wait because it can fail.
          startLivePreview();
        });
      })
      .onError((error, stackTrace) {
        MessageBox.show(context, 'Error VideoCaptureBuilder', () {
          backScreen();
        });
      });

    debugPrint('initializing...');
  }

  bool frameHandler(Uint8List frameData) {
    if (!mounted) return false;
    setState(() {
      this.frameData = frameData;
    });
    return previewing;
  }

  void startLivePreview() {
    previewing = true;
    _thetaClientFlutter.getLivePreview(frameHandler)
      .then((value) {
        debugPrint('LivePreview end.');
      })
      .onError((error, stackTrace) {
        debugPrint('Error getLivePreview.');
        MessageBox.show(context, 'Error getLivePreview', () {
          backScreen();
        });
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
      final uri = Uri.parse(fileUrl);
      Navigator.of(context).push(
        MaterialPageRoute(builder: (_) => VideoScreen(
          name: uri.pathSegments.last,
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
