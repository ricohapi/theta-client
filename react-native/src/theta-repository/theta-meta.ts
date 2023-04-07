/**
 * exif information
 */
export type Exif = {
  /** EXIF Support version */
  exifVersion: string;
  /** File created or updated date and time */
  dateTime: string;
  /** Image width (pixel). Theta X returns null. */
  imageWidth?: number;
  /** Image height (pixel). Theta X returns null. */
  imageLength?: number;
  /** GPS latitude if exists. */
  gpsLatitude?: number;
  /** GPS longitude if exists. */
  gpsLongitude?: number;
};

/**
 * Photo sphere XMP metadata of a still image.
 */
export type Xmp = {
  /** Compass heading, for the center the image. Theta X returns null. */
  poseHeadingDegrees?: number;
  /** Image width (pixel). */
  fullPanoWidthPixels: number;
  /** Image height (pixel). */
  fullPanoHeightPixels: number;
};

/**
 * metadata of a still image
 */
export type MetaInfo = {
  /** exif information */
  exif?: Exif;
  /** xmp information */
  xmp?: Xmp;
};
