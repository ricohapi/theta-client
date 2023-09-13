import 'package:flutter_test/flutter_test.dart';
import 'package:theta_client_flutter/theta_client_flutter.dart';
import 'package:theta_client_flutter/theta_client_flutter_platform_interface.dart';

import '../theta_client_flutter_test.dart';

void main() {
  test('listFiles', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform =
        MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    const name = 'R0013336.JPG';
    var infoList = List<FileInfo>.empty(growable: true);
    infoList.add(FileInfo(
      name,
      'http://192.168.1.1/files/150100524436344d4201375fda9dc400/100RICOH/R0013336.JPG',
      100,
      '2022:11:15 14:00:15+09:00',
      '2022:11:15 14:00:15',
      1.1,
      2.1,
      1000,
      500,
      'http://192.168.1.1/files/150100524436344d4201375fda9dc400/100RICOH/R0013336.JPG?type=thumb',
      'id_intervalCaptureGroupId',
      'id_compositeShootingGroupId',
      'id_autoBracketGroupId',
      2,
      true,
      'http://192.168.1.1/preview',
      CodecEnum.h264mp4avc,
      ProjectionTypeEnum.dualFisheye,
      'id_continuousShootingGroupId',
      30,
      true,
      'imageDescription_Description',
      '01234567890',
    ));
    var input = ThetaFiles(infoList, 10);
    onCallListFiles = () {
      return Future.value(input);
    };

    var result = await thetaClientPlugin.listFiles(
        FileTypeEnum.image, 10, 10, StorageEnum.current);
    expect(result, input);
  });

  test('CodecEnum', () async {
    List<List<dynamic>> data = [
      [CodecEnum.h264mp4avc, 'H264MP4AVC'],
    ];
    expect(data.length, CodecEnum.values.length, reason: 'enum count');
    for (int i = 0; i < data.length; i++) {
      expect(data[i][0].toString(), data[i][1], reason: data[i][1]);
      expect(CodecEnum.getValue(data[i][1]), data[i][0], reason: data[i][1]);
    }
  });

  test('ProjectionTypeEnum', () async {
    List<List<dynamic>> data = [
      [ProjectionTypeEnum.equirectangular, 'EQUIRECTANGULAR'],
      [ProjectionTypeEnum.dualFisheye, 'DUAL_FISHEYE'],
      [ProjectionTypeEnum.fisheye, 'FISHEYE'],
    ];
    expect(data.length, ProjectionTypeEnum.values.length, reason: 'enum count');
    for (int i = 0; i < data.length; i++) {
      expect(data[i][0].toString(), data[i][1], reason: data[i][1]);
      expect(ProjectionTypeEnum.getValue(data[i][1]), data[i][0],
          reason: data[i][1]);
    }
  });
}
