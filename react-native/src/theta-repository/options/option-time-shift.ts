/**
 * Time shift shooting settings.
 *
 * For Theta X, Z1 and V.
 **/

export type TimeShift = {
    /**
     * Shooting order.
     * if true, first shoot the front side (side with Theta logo) then shoot the rear side (side with monitor).
     * if false, first shoot the rear side then shoot the front side.
     */
    isFrontFirst?: boolean;

    /**
     * Time (sec) before 1st lens shooting.
     * 0 to 10.  For V or Z1, default is 5. For X, default is 2.
    */
    firstInterval?: number;

    /**
     * Time (sec) from 1st lens shooting until start of 2nd lens shooting.
     * 0 to 10.  Default is 5.
     */
    secondInterval?: number;
  };
  