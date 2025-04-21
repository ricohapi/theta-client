/** Aperture value. */
export const ApertureEnum = {
  /** AUTO(0) */
  APERTURE_AUTO: 'APERTURE_AUTO',
  /** 2.0F RICOH THETA V or prior */
  APERTURE_2_0: 'APERTURE_2_0',
  /** 2.1F RICOH THETA Z1 and the exposure program (exposureProgram) is set to Manual or Aperture Priority */
  APERTURE_2_1: 'APERTURE_2_1',
  /** 2.4F RICOH THETA X or later */
  APERTURE_2_4: 'APERTURE_2_4',
  /** 3.5F RICOH THETA Z1 and the exposure program (exposureProgram) is set to Manual or Aperture Priority */
  APERTURE_3_5: 'APERTURE_3_5',
  /** 5.6F RICOH THETA Z1 and the exposure program (exposureProgram) is set to Manual or Aperture Priority */
  APERTURE_5_6: 'APERTURE_5_6',
} as const;

/** type definition of ApertureEnum */
export type ApertureEnum = (typeof ApertureEnum)[keyof typeof ApertureEnum];
