/**
 * Format of live view
 */
export const PreviewFormatEnum = {
  /** width-height-framerate */
  /** For Theta X, Z1, V and SC2 */
  W1024_H512_F30: 'W1024_H512_F30',
  /** For Theta X. This value can't set. */
  W1024_H512_F15: 'W1024_H512_F15',
  /** For Theta X */
  W512_H512_F30: 'W512_H512_F30',
  /** For Theta Z1 and V */
  W1920_H960_F8: 'W1920_H960_F8',
  /** For Theta Z1 and V */
  W1024_H512_F8: 'W1024_H512_F8',
  /** For Theta Z1 and V */
  W640_H320_F30: 'W640_H320_F30',
  /** For Theta Z1 and V */
  W640_H320_F8: 'W640_H320_F8',
  /** For Theta S and SC */
  W640_H320_F10: 'W640_H320_F10',
  /** For Theta X */
  W3840_H1920_F30: 'W3840_H1920_F30',
} as const;

/** Type definition of PreviewFormatEnum */
export type PreviewFormatEnum =
  (typeof PreviewFormatEnum)[keyof typeof PreviewFormatEnum];
