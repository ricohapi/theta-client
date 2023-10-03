import { VideoStitchingEnum } from '../../theta-repository/options/option-video-stitching';

describe('VideoStitchingEnum', () => {
  const data: [VideoStitchingEnum, string][] = [
    [VideoStitchingEnum.NONE, 'NONE'],
    [VideoStitchingEnum.ONDEVICE, 'ONDEVICE'],
  ];

  test('length', () => {
    expect(data.length).toBe(Object.keys(VideoStitchingEnum).length);
  });

  test('data', () => {
    data.forEach((item) => {
      expect(item[0]).toBe(item[1]);
    });
  });
});
