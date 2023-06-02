/**
 * Format of live view
 */

export const PreviewFormatEnum = {
    /** width-height-framerate */
    W1024_H512_F30: 'W1024_H512_F30', // For Theta X, Z1, V and SC2
    W512_H512_F30: 'W512_H512_F30', // For Theta X
    W1920_H960_F8: 'W1920_H960_F8', // For Theta Z1 and V
    W1024_H512_F8: 'W1024_H512_F8', // For Theta Z1 and V
    W640_H320_F30: 'W640_H320_F30', // For Theta Z1 and V
    W640_H320_F8: 'W640_H320_F8', // For Theta Z1 and V
    W640_H320_F10: 'W640_H320_F10', // For Theta S and SC
  } as const;
  
  /** Type definition of PreviewFormatEnum */
  export type PreviewFormatEnum =
   (typeof PreviewFormatEnum)[keyof typeof PreviewFormatEnum];
  