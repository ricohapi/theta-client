/**
 * Preset mode
 *
 * For Theta SC2 and SC2 for business
 **/

export const PresetEnum = {
    /** Preset "Face" mode suitable for portrait shooting just for Theta SC2. */
    FACE: 'FACE',
    /** Preset "Night View" mode just for Theta SC2. */
    NIGHT_VIEW: 'NIGHT_VIEW',
    /** Preset "Lens-by-Lens Exposure" mode just for Theta SC2. */
    LENS_BY_LENS_EXPOSURE: 'LENS_BY_LENS_EXPOSURE',
    /** Preset "Room" mode just for SC2 for business. */
    ROOM: 'ROOM',
  } as const;
  
  /** Type definition of PresetEnum */
  export type PresetEnum =
   (typeof PresetEnum)[keyof typeof PresetEnum];
  