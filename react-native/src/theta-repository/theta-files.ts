/** File information in Theta. */
export type FileInfo = {
  /** File name. */
  name: string;
  /** fileUrl You can get a file using HTTP GET to [fileUrl]. */
  fileUrl: string;
  /** File size in bytes. */
  size: number;
  /** File creation or update time with the time zone in the format "YYYY:MM:DD hh:mm:ss+(-)hh:mm". */
  dateTimeZone: string;
  /** File creation time in the format "YYYY:MM:DD HH:MM:SS". */
  dateTime: string;
  /** Latitude. */
  lat?: number;
  /** Longitude. */
  lng?: number;
  /** Horizontal size of image (pixels). */
  width?: number;
  /** Vertical size of image (pixels). */
  height?: number;
  /** You can get a thumbnail image using HTTP GET to [thumbnailUrl]. */
  thumbnailUrl: string;
  /** Group ID of a still image shot by interval shooting. */
  intervalCaptureGroupId?: string;
  /** Group ID of a still image shot by interval composite shooting. */
  compositeShootingGroupId?: string;
  /** Group ID of a still image shot by multi bracket shooting. */
  autoBracketGroupId?: string;
  /** Video shooting time (sec). */
  recordTime?: number;
  /** Whether or not image processing has been completed. */
  isProcessed?: boolean;
  /** URL of the file being processed. */
  previewUrl?: string;
  /** Codec. (RICOH THETA V or later) */
  codec?: CodecEnum;
  /** Projection type of movie file. (RICOH THETA V or later) */
  projectionType?: ProjectionTypeEnum;
  /** Group ID of continuous shooting.  (RICOH THETA X or later) */
  continuousShootingGroupId?: string;
  /** Frame rate.  (RICOH THETA Z1 Version 3.01.1 or later, RICOH THETA X or later) */
  frameRate?: number;
  /** Favorite.  (RICOH THETA X or later) */
  favorite?: boolean;
  /** Image description.  (RICOH THETA X or later) */
  imageDescription?: string;
  /** Storage ID. (RICOH THETA X Version 2.00.0 or later) */
  storageID?: String;
};

/** Data about files in Theta. */
export type ThetaFiles = {
  /** A list of file information */
  fileList: FileInfo[];
  /** number of totalEntries */
  totalEntries: Number;
};

/** File type in Theta. */
export const FileTypeEnum = {
  /** still image files. */
  IMAGE: 'IMAGE',
  /** video files. */
  VIDEO: 'VIDEO',
  /** all files. */
  ALL: 'ALL',
} as const;

/** type definition of FileTypeEnum */
export type FileTypeEnum = (typeof FileTypeEnum)[keyof typeof FileTypeEnum];

/** Specifies the storage. */
export const StorageEnum = {
  /** internal storage */
  INTERNAL: 'INTERNAL',
  /** external storage (SD card) */
  SD: 'SD',
  /** current storage */
  CURRENT: 'CURRENT',
} as const;

/** type definition of StorageEnum */
export type StorageEnum = (typeof StorageEnum)[keyof typeof StorageEnum];

/** Video codec */
export const CodecEnum = {
  /** Undefined value */
  UNKNOWN: 'UNKNOWN',
  /** codec H.264/MPEG-4 AVC */
  H264MP4AVC: 'H264MP4AVC',
  /** codec H.265/HEVC */
  H265HEVC: 'H265HEVC',
} as const;

/** type definition of CodecEnum */
export type CodecEnum = (typeof CodecEnum)[keyof typeof CodecEnum];

/** THETA projection type */
export const ProjectionTypeEnum = {
  /** Equirectangular type */
  EQUIRECTANGULAR: 'EQUIRECTANGULAR',
  /** Dual Fisheye type */
  DUAL_FISHEYE: 'DUAL_FISHEYE',
  /** Dual Fisheye type */
  FISHEYE: 'FISHEYE',
} as const;

/** type definition of ProjectionTypeEnum */
export type ProjectionTypeEnum =
  (typeof ProjectionTypeEnum)[keyof typeof ProjectionTypeEnum];
