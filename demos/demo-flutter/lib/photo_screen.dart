import 'package:flutter/material.dart';

class PhotoScreen extends StatelessWidget {
  final String name;
  final String fileUrl;

  const PhotoScreen({
    super.key,
    required this.name,
    required this.fileUrl,
  });

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(title: Text('Photo: $name')),
        body: Container(
            alignment: Alignment.center,
            child: Image.network(
              fileUrl,
              loadingBuilder: (context, child, loadingProgress) {
                if (loadingProgress == null) return child;
                return const Text('loading...');
              },
              errorBuilder: (a, b, c) {
                return Container(
                  color: Colors.white,
                );
              },
            )));
  }
}
