import 'package:demo_flutter/message_box.dart';
import 'package:demo_flutter/photo_screen.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:theta_client_flutter/theta_client_flutter.dart';

class TakePictureScreen extends StatefulWidget {
  const TakePictureScreen({Key? key}) : super(key: key);

  @override
  State<StatefulWidget> createState() {
    return _TakePictureScreen();
  }
}

class _TakePictureScreen extends State<TakePictureScreen>
    with WidgetsBindingObserver {
  final _thetaClientFlutter = ThetaClientFlutter();

  Uint8List frameData = Uint8List(0);
  bool previewing = false;
  bool shooting = false;
  PhotoCaptureBuilder? builder;
  PhotoCapture? photoCapture;

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
    debugPrint('close TakePicture');
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
    _thetaClientFlutter.isInitialized().then((isInit) {
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
        appBar: AppBar(title: const Text('Take Picture')),
        body: Stack(
          fit: StackFit.expand,
          children: [
            Container(
                color: Colors.black,
                child: Center(
                  child: shooting
                      ? const Text(
                          'Take Picture...',
                          style: TextStyle(
                            fontSize: 20,
                            color: Colors.white,
                          ),
                        )
                      : Image.memory(
                          frameData,
                          errorBuilder: (a, b, c) {
                            return Container(
                              color: Colors.black,
                            );
                          },
                          gaplessPlayback: true,
                        ),
                )),
            Container(
                alignment: const Alignment(0, 0.8),
                child: MaterialButton(
                  height: 80,
                  shape: const CircleBorder(),
                  color: Colors.white,
                  onPressed: () {
                    if (shooting) {
                      debugPrint('already shooting');
                      return;
                    }
                    takePicture();
                  },
                )),
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

  void initialize() async {
    debugPrint('init TakePicture');
    // initialize PhotoCapture
    builder = _thetaClientFlutter.getPhotoCaptureBuilder();
    builder!.build().then((value) {
      photoCapture = value;
      debugPrint('Ready PhotoCapture');
      Future.delayed(const Duration(milliseconds: 500), () {}).then((value) {
        // Wait because it can fail.
        startLivePreview();
      });
    }).onError((error, stackTrace) {
      MessageBox.show(context, 'Error PhotoCaptureBuilder', () {
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
    _thetaClientFlutter.getLivePreview(frameHandler).then((value) {
      debugPrint('LivePreview end.');
    }).onError((error, stackTrace) {
      debugPrint('Error getLivePreview.$error');
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

  void takePicture() {
    if (shooting) {
      debugPrint('already shooting');
      return;
    }
    setState(() {
      shooting = true;
    });

    // Stops while shooting is in progress
    stopLivePreview();

    photoCapture!.takePicture((fileUrl) {
      setState(() {
        shooting = false;
      });
      debugPrint('take picture: $fileUrl');
      if (!mounted) return;
      if (fileUrl != null) {
        Navigator.of(context)
            .push(MaterialPageRoute(
                builder: (_) => PhotoScreen(
                      name: 'Take Picture',
                      fileUrl: fileUrl,
                    )))
            .then((value) => startLivePreview());
      } else {
        setState(() {
          shooting = true;
        });
        debugPrint('takePicture canceled.');
      }
    }, (exception) {
      setState(() {
        shooting = false;
      });
      debugPrint(exception.toString());
    });
  }
}
