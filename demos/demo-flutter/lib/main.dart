import 'package:demo_flutter/capture_video_screen.dart';
import 'package:demo_flutter/file_list_screen.dart';
import 'package:demo_flutter/message_box.dart';
import 'package:demo_flutter/take_picture_screen.dart';
import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:theta_client_flutter/theta_client_flutter.dart';

void main() {
  runApp(const MaterialApp(home: MyApp()));
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';
  final _thetaClientFlutter = ThetaClientFlutter();
  bool _isInitTheta = false;
  bool _initializing = false;

  final String endpoint = 'http://192.168.1.1:80/';

  @override
  void initState() {
    super.initState();
    initPlatformState();
    initTheta();
  }

  Future<void> initPlatformState() async {
    String platformVersion;
    try {
      platformVersion =
          await _thetaClientFlutter.getPlatformVersion() ?? 'Unknown platform version';
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }

    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
    });
  }

  Future<void> initTheta() async {
    if (_initializing) {
      return;
    }
    bool isInitTheta;
    try {
      isInitTheta = await _thetaClientFlutter.isInitialized();
      if (!isInitTheta) {
        _initializing = true;
        debugPrint('start initialize');
        await _thetaClientFlutter.initialize(endpoint);
        isInitTheta = true;
      }
    } on PlatformException {
      if (!mounted) return;
      debugPrint('Error. init');
      isInitTheta = false;
      MessageBox.show(context, 'Initialize error.');
    } finally {
      _initializing = false;
    }

    if (!mounted) return;

    setState(() {
      _isInitTheta = isInitTheta;
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'title',
      home: Home(
        platformVersion: _platformVersion,
        isInitialized: _isInitTheta,
        connectTheta: initTheta,
      ),
    );
  }
}

class Home extends StatelessWidget {
  final String platformVersion;
  final bool isInitialized;
  final Function connectTheta;
  const Home({Key? key,
    required this.platformVersion,
    required this.isInitialized,
    required this.connectTheta,
    }) : super(key: key);


  @override
  Widget build(BuildContext context) {
    String camera = isInitialized ? 'connected!': 'disconnected';
    return Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Center(
            child: Column(
              mainAxisSize: MainAxisSize.min,
              children: [
                Text('Running on: $platformVersion\n'),
                Text('Camera: $camera\n'),
                TextButton(
                  onPressed: isInitialized ? null: () {
                    connectTheta();
                  },
                  child: const Text('Connect'),
                  
                ),
                TextButton(
                  onPressed: !isInitialized ? null: () {
                    Navigator.of(context).push(
                      MaterialPageRoute(builder: (_) => const TakePictureScreen())
                    );
                  },
                  child: const Text('Take Picture'),
                  
                ),
                TextButton(
                  onPressed: !isInitialized ? null: () {
                    Navigator.of(context).push(
                      MaterialPageRoute(builder: (_) => const CaptureVideoScreen())
                    );
                  },
                  child: const Text('Capture Video'),
                  
                ),
                TextButton(
                  onPressed: !isInitialized ? null: () {
                    Navigator.of(context).push(
                      MaterialPageRoute(builder: (_) => const FileListScreen())
                    );
                  },
                  child: const Text('File List'),
                  
                ),
              ],
            ),
          ),
        )
    );
  }
}
