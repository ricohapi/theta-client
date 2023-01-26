import 'package:flutter/material.dart';

class MessageBox extends StatelessWidget {
  final String title;
  final Function? onClose;
  const MessageBox({Key? key,
    required this.title,
    this.onClose
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return AlertDialog(
      title: Text(title),
      actions: [
        ElevatedButton(
          onPressed: () {
            Navigator.pop(context);
            if (onClose != null) {
              onClose!();
            }
          },
          child: const Text('OK')
          ),
      ],
    );
  }

  static void show(BuildContext context, String title, [Function? onClose]) {
    showDialog(
      context: context,
      builder: (context) {
        return MessageBox(title: title, onClose: onClose,);
      }
    );
  }
}
