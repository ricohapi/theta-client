/**
 * Movie bit rate.
 *
 * ### Support value
 * The supported value depends on the shooting mode [CaptureMode].
 *
 * | Shooting mode | Supported value |
 * | ------------- | --------------- |
 * |         video | "Fine", "Normal", "Economy"(RICOH THETA X or later) <br/>"2000000"-"120000000" (RICOH THETA X v1.20 or later) |
 * |         image | "Auto", "1048576"-"20971520" (RICOH THETA X v1.20 or later) |
 * | _liveStreaming |         "Auto" |
 *
 * #### RICOH THETA X
 * | Video mode | Fine<br/>[Mbps] | Normal<br/>[Mbps] | Economy<br/>[Mbps] | Remark |
 * |------------| --------------- | ------------------| ------------------ | ------ |
 * |   2K 30fps |              32 |                16 |                  8 |        |
 * |   2K 60fps |              64 |                32 |                 16 |        |
 * |   4K 10fps |              48 |                24 |                 12 |        |
 * |   4K 15fps |              64 |                32 |                 16 |        |
 * |   4K 30fps |             100 |                54 |                 32 |        |
 * |   4K 60fps |             120 |                64 |                 32 |        |
 * | 5.7K  2fps |              16 |                12 |                  8 | firmware v2.00.0 or later   |
 * |            |              64 |                32 |                 16 | firmware v1.40.0 or later   (I-frame only)|
 * |            |              16 |                 8 |                  4 | firmware v1.30.0 or earlier |
 * | 5.7K  5fps |              40 |                30 |                 20 | firmware v2.00.0 or later   |
 * |            |             120 |                80 |                 40 | firmware v1.40.0 or later   (I-frame only)|
 * |            |              32 |                16 |                  8 | firmware v1.30.0 or earlier |
 * | 5.7K 10fps |              80 |                60 |                 40 | firmware v2.00.0 or later   |
 * |            |              64 |                40 |                 20 | firmware v1.40.0 or later   |
 * |            |              48 |                24 |                 12 | firmware v1.30.0 or earlier |
 * | 5.7K 15fps |              64 |                32 |                 16 |        |
 * | 5.7K 30fps |             120 |                64 |                 32 |        |
 * |   8K  2fps |              64 |                32 |                 16 | firmware v1.40.0 or later   (I-frame only)|
 * |            |              32 |                16 |                  8 | firmware v1.30.0 or earlier (I-frame only)|
 * |   8K  5fps |             120 |                96 |                 40 | firmware v1.40.0 or later   (I-frame only)|
 * |            |              64 |                32 |                 16 | firmware v1.30.0 or earlier (I-frame only)|
 * |   8K 10fps |             120 |                96 |                 40 | firmware v1.40.0 or later   (I-frame only)|
 * |            |             120 |                64 |                 32 | firmware v1.30.0 or earlier (I-frame only)|
 *
 * For
 * - RICOH THETA X
 * - RICOH THETA Z1
 * - RICOH THETA V firmware v2.50.1 or later
 */
export const BitrateEnum = {
  /** Auto. */
  AUTO: 'AUTO',
  /** Fine */
  FINE: 'FINE',
  /** Normal */
  NORMAL: 'NORMAL',
  /** Economy */
  ECONOMY: 'ECONOMY',
} as const;

/** type definition of BitrateEnum */
export type BitrateEnum =
  | (typeof BitrateEnum)[keyof typeof BitrateEnum]
  | number;
