/** file format type in theta */
export const FileFormatTypeEnum = {
  /** Undefined value */
  UNKNOWN: 'UNKNOWN',
  /** jpeg image */
  JPEG: 'JPEG',
  /** mp4 video */
  MP4: 'MP4',
  /** raw+ image */
  RAW: 'RAW',
} as const;

/** type definition of FileFormatTypeEnum */
export type FileFormatTypeEnum =
  (typeof FileFormatTypeEnum)[keyof typeof FileFormatTypeEnum];

/** Photo image format */
export const PhotoFileFormatEnum = {
  /** jpeg 2048 x 1024 */
  IMAGE_2K: 'IMAGE_2K',
  /** jpeg 5376 x 2688 */
  IMAGE_5K: 'IMAGE_5K',
  /** jpeg 6720 x 3360 */
  IMAGE_6_7K: 'IMAGE_6_7K',
  /** row+ 6720 x 3360 */
  RAW_P_6_7K: 'RAW_P_6_7K',
  /** jpeg 5504 x 2752 */
  IMAGE_5_5K: 'IMAGE_5_5K',
  /** jpeg 11008 x 5504 */
  IMAGE_11K: 'IMAGE_11K',
} as const;

/** Video image format */
export const VideoFileFormatEnum = {
  /** mp4 1280 x 570 */
  VIDEO_HD: 'VIDEO_HD',
  /** mp4 1920 x 1080 */
  VIDEO_FULL_HD: 'VIDEO_FULL_HD',
  /** mp4 1920 x 960 */
  VIDEO_2K: 'VIDEO_2K',
  /** mp4 1920 x 960 no codec */
  VIDEO_2K_NO_CODEC: 'VIDEO_2K_NO_CODEC',
  /** mp4 3840 x 1920 */
  VIDEO_4K: 'VIDEO_4K',
  /** mp4 3840 x 1920 no codec */
  VIDEO_4K_NO_CODEC: 'VIDEO_4K_NO_CODEC',
  /** mp4 1920 x 960 30fps */
  VIDEO_2K_30F: 'VIDEO_2K_30F',
  /** mp4 1920 x 960 60fps */
  VIDEO_2K_60F: 'VIDEO_2K_60F',
  /** mp4 2752 x 2752 2fps */
  VIDEO_2_7K_2752_2F: 'VIDEO_2_7K_2752_2F',
  /** mp4 2752 x 2752 5fps */
  VIDEO_2_7K_2752_5F: 'VIDEO_2_7K_2752_5F',
  /** mp4 2752 x 2752 10fps */
  VIDEO_2_7K_2752_10F: 'VIDEO_2_7K_2752_10F',
  /** mp4 2752 x 2752 30fps */
  VIDEO_2_7K_2752_30F: 'VIDEO_2_7K_2752_30F',
  /** mp4 2688 x 2688 1fps */
  VIDEO_2_7K_1F: 'VIDEO_2_7K_1F',
  /** mp4 2688 x 2688 2fps */
  VIDEO_2_7K_2F: 'VIDEO_2_7K_2F',
  /** mp4 3648 x 3648 1fps */
  VIDEO_3_6K_1F: 'VIDEO_3_6K_1F',
  /** mp4 3648 x 3648 2fps */
  VIDEO_3_6K_2F: 'VIDEO_3_6K_2F',
  /** mp4 3840 x 1920 10fps */
  VIDEO_4K_10F: 'VIDEO_4K_10F',
  /** mp4 3840 x 1920 15fps */
  VIDEO_4K_15F: 'VIDEO_4K_15F',
  /** mp4 3840 x 1920 30fps */
  VIDEO_4K_30F: 'VIDEO_4K_30F',
  /** mp4 3840 x 1920 60fps */
  VIDEO_4K_60F: 'VIDEO_4K_60F',
  /** mp4 5760 x 2880 2fps */
  VIDEO_5_7K_2F: 'VIDEO_5_7K_2F',
  /** mp4 5760 x 2880 5fps */
  VIDEO_5_7K_5F: 'VIDEO_5_7K_5F',
  /** mp4 5760 x 2880 10fps */
  VIDEO_5_7K_10F: 'VIDEO_5_7K_10F',
  /** mp4 5760 x 2880 15fps */
  VIDEO_5_7K_15F: 'VIDEO_5_7K_15F',
  /** mp4 5760 x 2880 30fps */
  VIDEO_5_7K_30F: 'VIDEO_5_7K_30F',
  /** mp4 7680 x 3840 2fps */
  VIDEO_7K_2F: 'VIDEO_7K_2F',
  /** mp4 7680 x 3840 5fps */
  VIDEO_7K_5F: 'VIDEO_7K_5F',
  /** mp4 7680 x 3840 10fps */
  VIDEO_7K_10F: 'VIDEO_7K_10F',
} as const;

/** type definition of PhotoFileFormatEnum */
export type PhotoFileFormatEnum =
  (typeof PhotoFileFormatEnum)[keyof typeof PhotoFileFormatEnum];

/** type definition of VideoFileFormatEnum */
export type VideoFileFormatEnum =
  (typeof VideoFileFormatEnum)[keyof typeof VideoFileFormatEnum];

export const FileFormatEnum = {
  /** Undefined value */
  UNKNOWN: 'UNKNOWN',
  ...PhotoFileFormatEnum,
  ...VideoFileFormatEnum,
};

/** type definition of VideoFileFormatEnum */
export type FileFormatEnum =
  (typeof FileFormatEnum)[keyof typeof FileFormatEnum];
