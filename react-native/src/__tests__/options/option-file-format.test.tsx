import {
  FileFormatEnum,
  FileFormatTypeEnum,
  PhotoFileFormatEnum,
  VideoFileFormatEnum,
} from '../..//theta-repository/options/option-file-format';

describe('FileFormatTypeEnum', () => {
  const data: string[][] = [
    [FileFormatTypeEnum.UNKNOWN, 'UNKNOWN'],
    [FileFormatTypeEnum.JPEG, 'JPEG'],
    [FileFormatTypeEnum.MP4, 'MP4'],
    [FileFormatTypeEnum.RAW, 'RAW'],
  ];

  test('length', () => {
    expect(data.length).toBe(Object.keys(FileFormatTypeEnum).length);
  });

  test('data', () => {
    data.forEach((item) => {
      expect(item[0] ? item[0].toString() : '').toBe(item[1]);
    });
  });
});

describe('PhotoFileFormatEnum', () => {
  const data: string[][] = [
    [PhotoFileFormatEnum.IMAGE_2K, 'IMAGE_2K'],
    [PhotoFileFormatEnum.IMAGE_5K, 'IMAGE_5K'],
    [PhotoFileFormatEnum.IMAGE_5_5K, 'IMAGE_5_5K'],
    [PhotoFileFormatEnum.IMAGE_6_7K, 'IMAGE_6_7K'],
    [PhotoFileFormatEnum.RAW_P_6_7K, 'RAW_P_6_7K'],
    [PhotoFileFormatEnum.IMAGE_11K, 'IMAGE_11K'],
  ];

  test('length', () => {
    expect(data.length).toBe(Object.keys(PhotoFileFormatEnum).length);
  });

  test('data', () => {
    data.forEach((item) => {
      expect(item[0] ? item[0].toString() : '').toBe(item[1]);
    });
  });
});

describe('VideoFileFormatEnum', () => {
  const data: string[][] = [
    [VideoFileFormatEnum.VIDEO_HD, 'VIDEO_HD'],
    [VideoFileFormatEnum.VIDEO_FULL_HD, 'VIDEO_FULL_HD'],
    [VideoFileFormatEnum.VIDEO_2K, 'VIDEO_2K'],
    [VideoFileFormatEnum.VIDEO_2K_NO_CODEC, 'VIDEO_2K_NO_CODEC'],
    [VideoFileFormatEnum.VIDEO_2K_30F, 'VIDEO_2K_30F'],
    [VideoFileFormatEnum.VIDEO_2K_60F, 'VIDEO_2K_60F'],
    [VideoFileFormatEnum.VIDEO_2_7K_2752_2F, 'VIDEO_2_7K_2752_2F'],
    [VideoFileFormatEnum.VIDEO_2_7K_2752_5F, 'VIDEO_2_7K_2752_5F'],
    [VideoFileFormatEnum.VIDEO_2_7K_2752_10F, 'VIDEO_2_7K_2752_10F'],
    [VideoFileFormatEnum.VIDEO_2_7K_2752_30F, 'VIDEO_2_7K_2752_30F'],
    [VideoFileFormatEnum.VIDEO_2_7K_1F, 'VIDEO_2_7K_1F'],
    [VideoFileFormatEnum.VIDEO_2_7K_2F, 'VIDEO_2_7K_2F'],
    [VideoFileFormatEnum.VIDEO_3_6K_1F, 'VIDEO_3_6K_1F'],
    [VideoFileFormatEnum.VIDEO_3_6K_2F, 'VIDEO_3_6K_2F'],
    [VideoFileFormatEnum.VIDEO_4K, 'VIDEO_4K'],
    [VideoFileFormatEnum.VIDEO_4K_NO_CODEC, 'VIDEO_4K_NO_CODEC'],
    [VideoFileFormatEnum.VIDEO_4K_10F, 'VIDEO_4K_10F'],
    [VideoFileFormatEnum.VIDEO_4K_15F, 'VIDEO_4K_15F'],
    [VideoFileFormatEnum.VIDEO_4K_30F, 'VIDEO_4K_30F'],
    [VideoFileFormatEnum.VIDEO_4K_60F, 'VIDEO_4K_60F'],
    [VideoFileFormatEnum.VIDEO_5_7K_2F, 'VIDEO_5_7K_2F'],
    [VideoFileFormatEnum.VIDEO_5_7K_5F, 'VIDEO_5_7K_5F'],
    [VideoFileFormatEnum.VIDEO_5_7K_10F, 'VIDEO_5_7K_10F'],
    [VideoFileFormatEnum.VIDEO_5_7K_15F, 'VIDEO_5_7K_15F'],
    [VideoFileFormatEnum.VIDEO_5_7K_30F, 'VIDEO_5_7K_30F'],
    [VideoFileFormatEnum.VIDEO_7K_2F, 'VIDEO_7K_2F'],
    [VideoFileFormatEnum.VIDEO_7K_5F, 'VIDEO_7K_5F'],
    [VideoFileFormatEnum.VIDEO_7K_10F, 'VIDEO_7K_10F'],
  ];

  test('length', () => {
    expect(data.length).toBe(Object.keys(VideoFileFormatEnum).length);
  });

  test('data', () => {
    data.forEach((item) => {
      expect(item[0] ? item[0].toString() : '').toBe(item[1]);
    });
  });
});

describe('FileFormatEnum', () => {
  const data: string[][] = [
    [FileFormatEnum.UNKNOWN, 'UNKNOWN'],
    [FileFormatEnum.IMAGE_2K, 'IMAGE_2K'],
    [FileFormatEnum.IMAGE_5K, 'IMAGE_5K'],
    [FileFormatEnum.IMAGE_5_5K, 'IMAGE_5_5K'],
    [FileFormatEnum.IMAGE_6_7K, 'IMAGE_6_7K'],
    [FileFormatEnum.RAW_P_6_7K, 'RAW_P_6_7K'],
    [FileFormatEnum.IMAGE_11K, 'IMAGE_11K'],
    [FileFormatEnum.VIDEO_HD, 'VIDEO_HD'],
    [FileFormatEnum.VIDEO_FULL_HD, 'VIDEO_FULL_HD'],
    [FileFormatEnum.VIDEO_2K, 'VIDEO_2K'],
    [FileFormatEnum.VIDEO_2K_NO_CODEC, 'VIDEO_2K_NO_CODEC'],
    [FileFormatEnum.VIDEO_2K_30F, 'VIDEO_2K_30F'],
    [FileFormatEnum.VIDEO_2K_60F, 'VIDEO_2K_60F'],
    [FileFormatEnum.VIDEO_2_7K_2752_2F, 'VIDEO_2_7K_2752_2F'],
    [FileFormatEnum.VIDEO_2_7K_2752_5F, 'VIDEO_2_7K_2752_5F'],
    [FileFormatEnum.VIDEO_2_7K_2752_10F, 'VIDEO_2_7K_2752_10F'],
    [FileFormatEnum.VIDEO_2_7K_2752_30F, 'VIDEO_2_7K_2752_30F'],
    [FileFormatEnum.VIDEO_2_7K_1F, 'VIDEO_2_7K_1F'],
    [FileFormatEnum.VIDEO_2_7K_2F, 'VIDEO_2_7K_2F'],
    [FileFormatEnum.VIDEO_3_6K_1F, 'VIDEO_3_6K_1F'],
    [FileFormatEnum.VIDEO_3_6K_2F, 'VIDEO_3_6K_2F'],
    [FileFormatEnum.VIDEO_4K, 'VIDEO_4K'],
    [FileFormatEnum.VIDEO_4K_NO_CODEC, 'VIDEO_4K_NO_CODEC'],
    [FileFormatEnum.VIDEO_4K_10F, 'VIDEO_4K_10F'],
    [FileFormatEnum.VIDEO_4K_15F, 'VIDEO_4K_15F'],
    [FileFormatEnum.VIDEO_4K_30F, 'VIDEO_4K_30F'],
    [FileFormatEnum.VIDEO_4K_60F, 'VIDEO_4K_60F'],
    [FileFormatEnum.VIDEO_5_7K_2F, 'VIDEO_5_7K_2F'],
    [FileFormatEnum.VIDEO_5_7K_5F, 'VIDEO_5_7K_5F'],
    [FileFormatEnum.VIDEO_5_7K_10F, 'VIDEO_5_7K_10F'],
    [FileFormatEnum.VIDEO_5_7K_15F, 'VIDEO_5_7K_15F'],
    [FileFormatEnum.VIDEO_5_7K_30F, 'VIDEO_5_7K_30F'],
    [FileFormatEnum.VIDEO_7K_2F, 'VIDEO_7K_2F'],
    [FileFormatEnum.VIDEO_7K_5F, 'VIDEO_7K_5F'],
    [FileFormatEnum.VIDEO_7K_10F, 'VIDEO_7K_10F'],
  ];

  test('length', () => {
    expect(data.length).toBe(Object.keys(FileFormatEnum).length);
  });

  test('data', () => {
    data.forEach((item) => {
      expect(item[0] ? item[0].toString() : '').toBe(item[1]);
    });
  });
});
