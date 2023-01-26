import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:theta_client_flutter/theta_client_flutter.dart';
import 'package:theta_client_flutter_example/capture_video_screen.dart';
import 'package:theta_client_flutter_example/file_list_screen.dart';
import 'package:theta_client_flutter_example/take_picture_screen.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';
  final _thetaClientPlugin = ThetaClientFlutter();
  bool _isInitTheta = false;

  final String endpoint = 'http://192.168.1.1:80/';

  @override
  void initState() {
    super.initState();
    initPlatformState();
    initTheta();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String platformVersion;
    // Platform messages may fail, so we use a try/catch PlatformException.
    // We also handle the message potentially returning null.
    try {
      platformVersion =
          await _thetaClientPlugin.getPlatformVersion() ?? 'Unknown platform version';
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
    });
  }

  Future<void> initTheta() async {
    bool isInitTheta;
    try {
      isInitTheta = await _thetaClientPlugin.isInitialized();
      if (!isInitTheta) {
        await _thetaClientPlugin.initialize(endpoint);
        isInitTheta = true;
      }
    } on PlatformException {
      debugPrint('Error. init');
      isInitTheta = false;
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
