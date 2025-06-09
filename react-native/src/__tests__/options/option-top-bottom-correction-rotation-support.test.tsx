import { convertOptions } from '../../theta-repository/libs';
import type { Options } from '../../theta-repository/options';

describe('convertOptions', () => {
  test('getOption', () => {
    const pitchData = { max: 10.0, min: -15.0, stepSize: 1.0 };
    const rollData = { max: 20.0, min: -5.0, stepSize: 0.5 };
    const yawData = { max: 30.0, min: -10.0, stepSize: 2.0 };

    const jsonOptions = {
      topBottomCorrectionRotationSupport: JSON.stringify({
        pitch: pitchData,
        roll: rollData,
        yaw: yawData,
      }),
    };

    const emptyOptions: Options = {};
    const expectedOptions: Options = {
      topBottomCorrectionRotationSupport: {
        pitch: {
          max: pitchData.max,
          min: pitchData.min,
          stepSize: pitchData.stepSize,
        },
        roll: {
          max: rollData.max,
          min: rollData.min,
          stepSize: rollData.stepSize,
        },
        yaw: {
          max: yawData.max,
          min: yawData.min,
          stepSize: yawData.stepSize,
        },
      },
    };

    const options = convertOptions(emptyOptions, jsonOptions);
    expect(options.topBottomCorrectionRotationSupport).toEqual(
      expectedOptions.topBottomCorrectionRotationSupport
    );
  });
});
