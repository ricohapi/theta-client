import 'package:flutter_test/flutter_test.dart';
import 'package:theta_client_flutter/theta_client_flutter.dart';

void main() {
  setUp(() {});

  tearDown(() {});

  test('ShutterSpeedEnum', () async {
    List<List<dynamic>> data = [
      [ShutterSpeedEnum.shutterSpeedAuto, 'SHUTTER_SPEED_AUTO'],
      [ShutterSpeedEnum.shutterSpeed_60, 'SHUTTER_SPEED_60'],
      [ShutterSpeedEnum.shutterSpeed_50, 'SHUTTER_SPEED_50'],
      [ShutterSpeedEnum.shutterSpeed_40, 'SHUTTER_SPEED_40'],
      [ShutterSpeedEnum.shutterSpeed_30, 'SHUTTER_SPEED_30'],
      [ShutterSpeedEnum.shutterSpeed_25, 'SHUTTER_SPEED_25'],
      [ShutterSpeedEnum.shutterSpeed_20, 'SHUTTER_SPEED_20'],
      [ShutterSpeedEnum.shutterSpeed_15, 'SHUTTER_SPEED_15'],
      [ShutterSpeedEnum.shutterSpeed_13, 'SHUTTER_SPEED_13'],
      [ShutterSpeedEnum.shutterSpeed_10, 'SHUTTER_SPEED_10'],
      [ShutterSpeedEnum.shutterSpeed_8, 'SHUTTER_SPEED_8'],
      [ShutterSpeedEnum.shutterSpeed_6, 'SHUTTER_SPEED_6'],
      [ShutterSpeedEnum.shutterSpeed_5, 'SHUTTER_SPEED_5'],
      [ShutterSpeedEnum.shutterSpeed_4, 'SHUTTER_SPEED_4'],
      [ShutterSpeedEnum.shutterSpeed_3_2, 'SHUTTER_SPEED_3_2'],
      [ShutterSpeedEnum.shutterSpeed_2_5, 'SHUTTER_SPEED_2_5'],
      [ShutterSpeedEnum.shutterSpeed_2, 'SHUTTER_SPEED_2'],
      [ShutterSpeedEnum.shutterSpeed_1_6, 'SHUTTER_SPEED_1_6'],
      [ShutterSpeedEnum.shutterSpeed_1_3, 'SHUTTER_SPEED_1_3'],
      [ShutterSpeedEnum.shutterSpeed_1, 'SHUTTER_SPEED_1'],
      [ShutterSpeedEnum.shutterSpeedOneOver_1_3, 'SHUTTER_SPEED_ONE_OVER_1_3'],
      [ShutterSpeedEnum.shutterSpeedOneOver_1_6, 'SHUTTER_SPEED_ONE_OVER_1_6'],
      [ShutterSpeedEnum.shutterSpeedOneOver_2, 'SHUTTER_SPEED_ONE_OVER_2'],
      [ShutterSpeedEnum.shutterSpeedOneOver_2_5, 'SHUTTER_SPEED_ONE_OVER_2_5'],
      [ShutterSpeedEnum.shutterSpeedOneOver_3, 'SHUTTER_SPEED_ONE_OVER_3'],
      [ShutterSpeedEnum.shutterSpeedOneOver_4, 'SHUTTER_SPEED_ONE_OVER_4'],
      [ShutterSpeedEnum.shutterSpeedOneOver_5, 'SHUTTER_SPEED_ONE_OVER_5'],
      [ShutterSpeedEnum.shutterSpeedOneOver_6, 'SHUTTER_SPEED_ONE_OVER_6'],
      [ShutterSpeedEnum.shutterSpeedOneOver_8, 'SHUTTER_SPEED_ONE_OVER_8'],
      [ShutterSpeedEnum.shutterSpeedOneOver_10, 'SHUTTER_SPEED_ONE_OVER_10'],
      [ShutterSpeedEnum.shutterSpeedOneOver_13, 'SHUTTER_SPEED_ONE_OVER_13'],
      [ShutterSpeedEnum.shutterSpeedOneOver_15, 'SHUTTER_SPEED_ONE_OVER_15'],
      [ShutterSpeedEnum.shutterSpeedOneOver_20, 'SHUTTER_SPEED_ONE_OVER_20'],
      [ShutterSpeedEnum.shutterSpeedOneOver_25, 'SHUTTER_SPEED_ONE_OVER_25'],
      [ShutterSpeedEnum.shutterSpeedOneOver_30, 'SHUTTER_SPEED_ONE_OVER_30'],
      [ShutterSpeedEnum.shutterSpeedOneOver_40, 'SHUTTER_SPEED_ONE_OVER_40'],
      [ShutterSpeedEnum.shutterSpeedOneOver_50, 'SHUTTER_SPEED_ONE_OVER_50'],
      [ShutterSpeedEnum.shutterSpeedOneOver_60, 'SHUTTER_SPEED_ONE_OVER_60'],
      [ShutterSpeedEnum.shutterSpeedOneOver_80, 'SHUTTER_SPEED_ONE_OVER_80'],
      [ShutterSpeedEnum.shutterSpeedOneOver_100, 'SHUTTER_SPEED_ONE_OVER_100'],
      [ShutterSpeedEnum.shutterSpeedOneOver_125, 'SHUTTER_SPEED_ONE_OVER_125'],
      [ShutterSpeedEnum.shutterSpeedOneOver_160, 'SHUTTER_SPEED_ONE_OVER_160'],
      [ShutterSpeedEnum.shutterSpeedOneOver_200, 'SHUTTER_SPEED_ONE_OVER_200'],
      [ShutterSpeedEnum.shutterSpeedOneOver_250, 'SHUTTER_SPEED_ONE_OVER_250'],
      [ShutterSpeedEnum.shutterSpeedOneOver_320, 'SHUTTER_SPEED_ONE_OVER_320'],
      [ShutterSpeedEnum.shutterSpeedOneOver_400, 'SHUTTER_SPEED_ONE_OVER_400'],
      [ShutterSpeedEnum.shutterSpeedOneOver_500, 'SHUTTER_SPEED_ONE_OVER_500'],
      [ShutterSpeedEnum.shutterSpeedOneOver_640, 'SHUTTER_SPEED_ONE_OVER_640'],
      [ShutterSpeedEnum.shutterSpeedOneOver_800, 'SHUTTER_SPEED_ONE_OVER_800'],
      [
        ShutterSpeedEnum.shutterSpeedOneOver_1000,
        'SHUTTER_SPEED_ONE_OVER_1000'
      ],
      [
        ShutterSpeedEnum.shutterSpeedOneOver_1250,
        'SHUTTER_SPEED_ONE_OVER_1250'
      ],
      [
        ShutterSpeedEnum.shutterSpeedOneOver_1600,
        'SHUTTER_SPEED_ONE_OVER_1600'
      ],
      [
        ShutterSpeedEnum.shutterSpeedOneOver_2000,
        'SHUTTER_SPEED_ONE_OVER_2000'
      ],
      [
        ShutterSpeedEnum.shutterSpeedOneOver_2500,
        'SHUTTER_SPEED_ONE_OVER_2500'
      ],
      [
        ShutterSpeedEnum.shutterSpeedOneOver_3200,
        'SHUTTER_SPEED_ONE_OVER_3200'
      ],
      [
        ShutterSpeedEnum.shutterSpeedOneOver_4000,
        'SHUTTER_SPEED_ONE_OVER_4000'
      ],
      [
        ShutterSpeedEnum.shutterSpeedOneOver_5000,
        'SHUTTER_SPEED_ONE_OVER_5000'
      ],
      [
        ShutterSpeedEnum.shutterSpeedOneOver_6400,
        'SHUTTER_SPEED_ONE_OVER_6400'
      ],
      [
        ShutterSpeedEnum.shutterSpeedOneOver_8000,
        'SHUTTER_SPEED_ONE_OVER_8000'
      ],
      [
        ShutterSpeedEnum.shutterSpeedOneOver_10000,
        'SHUTTER_SPEED_ONE_OVER_10000'
      ],
      [
        ShutterSpeedEnum.shutterSpeedOneOver_12500,
        'SHUTTER_SPEED_ONE_OVER_12500'
      ],
      [
        ShutterSpeedEnum.shutterSpeedOneOver_12800,
        'SHUTTER_SPEED_ONE_OVER_12800'
      ],
      [
        ShutterSpeedEnum.shutterSpeedOneOver_16000,
        'SHUTTER_SPEED_ONE_OVER_16000'
      ],
      [
        ShutterSpeedEnum.shutterSpeedOneOver_20000,
        'SHUTTER_SPEED_ONE_OVER_20000'
      ],
      [
        ShutterSpeedEnum.shutterSpeedOneOver_25000,
        'SHUTTER_SPEED_ONE_OVER_25000'
      ],
    ];
    expect(data.length, ShutterSpeedEnum.values.length, reason: 'enum count');
    for (int i = 0; i < data.length; i++) {
      expect(data[i][0].toString(), data[i][1], reason: data[i][1]);
    }
  });
}
