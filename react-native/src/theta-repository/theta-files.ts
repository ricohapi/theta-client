/** File information in Theta. */
export type FileInfo = {
  /** File name. */
  name: string;
  /** File size in bytes. */
  size: number;
  /** File creation time in the format "YYYY:MM:DD HH:MM:SS". */
  dateTime: string;
  /** You can get a thumbnail image using HTTP GET to [thumbnailUrl]. */
  thumbnailUrl: string;
  /** fileUrl You can get a file using HTTP GET to [fileUrl]. */
  fileUrl: string;
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
