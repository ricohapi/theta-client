import { AiAutoThumbnailEnum } from '../../theta-repository/options/option-ai-auto-thumbnail';

describe('AiAutoThumbnailEnum', () => {
  const data: [AiAutoThumbnailEnum, string][] = [
    [AiAutoThumbnailEnum.ON, 'ON'],
    [AiAutoThumbnailEnum.OFF, 'OFF'],
    [AiAutoThumbnailEnum.UNKNOWN, 'UNKNOWN'],
  ];

  test('length', () => {
    expect(data.length).toBe(Object.keys(AiAutoThumbnailEnum).length);
  });

  test('data', () => {
    data.forEach((item) => {
      expect(item[0]).toBe(item[1]);
    });
  });
});
