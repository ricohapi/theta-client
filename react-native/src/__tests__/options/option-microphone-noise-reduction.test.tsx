import { MicrophoneNoiseReductionEnum } from '../../theta-repository/options/option-microphone-noise-reduction';

describe('MicrophoneNoiseReductionEnum', () => {
  const data: string[][] = [
    [MicrophoneNoiseReductionEnum.UNKNOWN, 'UNKNOWN'],
    [MicrophoneNoiseReductionEnum.ON, 'ON'],
    [MicrophoneNoiseReductionEnum.OFF, 'OFF'],
  ];

  test('length', () => {
    expect(data.length).toBe(Object.keys(MicrophoneNoiseReductionEnum).length);
  });

  test('data', () => {
    data.forEach((item) => {
      expect(item[0] ? item[0].toString() : '').toBe(item[1]);
    });
  });
});
