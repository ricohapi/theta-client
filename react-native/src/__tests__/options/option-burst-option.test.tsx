import {
  BurstBracketStepEnum,
  BurstCaptureNumEnum,
  BurstCompensationEnum,
  BurstMaxExposureTimeEnum,
  BurstEnableIsoControlEnum,
  BurstOrderEnum,
} from '../../theta-repository/options/option-burst-option';

describe('BurstCaptureNumEnum', () => {
  const data: string[][] = [
    [BurstCaptureNumEnum.BURST_CAPTURE_NUM_1, 'BURST_CAPTURE_NUM_1'],
    [BurstCaptureNumEnum.BURST_CAPTURE_NUM_3, 'BURST_CAPTURE_NUM_3'],
    [BurstCaptureNumEnum.BURST_CAPTURE_NUM_5, 'BURST_CAPTURE_NUM_5'],
    [BurstCaptureNumEnum.BURST_CAPTURE_NUM_7, 'BURST_CAPTURE_NUM_7'],
    [BurstCaptureNumEnum.BURST_CAPTURE_NUM_9, 'BURST_CAPTURE_NUM_9'],
  ];

  test('length', () => {
    expect(data.length).toBe(Object.keys(BurstCaptureNumEnum).length);
  });

  test('data', () => {
    data.forEach((item) => {
      expect(item[0] ? item[0].toString() : '').toBe(item[1]);
    });
  });
});

describe('BurstBracketStepEnum', () => {
  const data: string[][] = [
    [BurstBracketStepEnum.BRACKET_STEP_0_0, 'BRACKET_STEP_0_0'],
    [BurstBracketStepEnum.BRACKET_STEP_0_3, 'BRACKET_STEP_0_3'],
    [BurstBracketStepEnum.BRACKET_STEP_0_7, 'BRACKET_STEP_0_7'],
    [BurstBracketStepEnum.BRACKET_STEP_1_0, 'BRACKET_STEP_1_0'],
    [BurstBracketStepEnum.BRACKET_STEP_1_3, 'BRACKET_STEP_1_3'],
    [BurstBracketStepEnum.BRACKET_STEP_1_7, 'BRACKET_STEP_1_7'],
    [BurstBracketStepEnum.BRACKET_STEP_2_0, 'BRACKET_STEP_2_0'],
    [BurstBracketStepEnum.BRACKET_STEP_2_3, 'BRACKET_STEP_2_3'],
    [BurstBracketStepEnum.BRACKET_STEP_2_7, 'BRACKET_STEP_2_7'],
    [BurstBracketStepEnum.BRACKET_STEP_3_0, 'BRACKET_STEP_3_0'],
  ];

  test('length', () => {
    expect(data.length).toBe(Object.keys(BurstBracketStepEnum).length);
  });

  test('data', () => {
    data.forEach((item) => {
      expect(item[0] ? item[0].toString() : '').toBe(item[1]);
    });
  });
});

describe('BurstCompensationEnum', () => {
  const data: string[][] = [
    [
      BurstCompensationEnum.BURST_COMPENSATION_DOWN_5_0,
      'BURST_COMPENSATION_DOWN_5_0',
    ],
    [
      BurstCompensationEnum.BURST_COMPENSATION_DOWN_4_7,
      'BURST_COMPENSATION_DOWN_4_7',
    ],
    [
      BurstCompensationEnum.BURST_COMPENSATION_DOWN_4_3,
      'BURST_COMPENSATION_DOWN_4_3',
    ],
    [
      BurstCompensationEnum.BURST_COMPENSATION_DOWN_4_0,
      'BURST_COMPENSATION_DOWN_4_0',
    ],
    [
      BurstCompensationEnum.BURST_COMPENSATION_DOWN_3_7,
      'BURST_COMPENSATION_DOWN_3_7',
    ],
    [
      BurstCompensationEnum.BURST_COMPENSATION_DOWN_3_3,
      'BURST_COMPENSATION_DOWN_3_3',
    ],
    [
      BurstCompensationEnum.BURST_COMPENSATION_DOWN_3_0,
      'BURST_COMPENSATION_DOWN_3_0',
    ],
    [
      BurstCompensationEnum.BURST_COMPENSATION_DOWN_2_7,
      'BURST_COMPENSATION_DOWN_2_7',
    ],
    [
      BurstCompensationEnum.BURST_COMPENSATION_DOWN_2_3,
      'BURST_COMPENSATION_DOWN_2_3',
    ],
    [
      BurstCompensationEnum.BURST_COMPENSATION_DOWN_2_0,
      'BURST_COMPENSATION_DOWN_2_0',
    ],
    [
      BurstCompensationEnum.BURST_COMPENSATION_DOWN_1_7,
      'BURST_COMPENSATION_DOWN_1_7',
    ],
    [
      BurstCompensationEnum.BURST_COMPENSATION_DOWN_1_3,
      'BURST_COMPENSATION_DOWN_1_3',
    ],
    [
      BurstCompensationEnum.BURST_COMPENSATION_DOWN_1_0,
      'BURST_COMPENSATION_DOWN_1_0',
    ],
    [
      BurstCompensationEnum.BURST_COMPENSATION_DOWN_0_7,
      'BURST_COMPENSATION_DOWN_0_7',
    ],
    [
      BurstCompensationEnum.BURST_COMPENSATION_DOWN_0_3,
      'BURST_COMPENSATION_DOWN_0_3',
    ],
    [BurstCompensationEnum.BURST_COMPENSATION_0_0, 'BURST_COMPENSATION_0_0'],
    [
      BurstCompensationEnum.BURST_COMPENSATION_UP_0_3,
      'BURST_COMPENSATION_UP_0_3',
    ],
    [
      BurstCompensationEnum.BURST_COMPENSATION_UP_0_7,
      'BURST_COMPENSATION_UP_0_7',
    ],
    [
      BurstCompensationEnum.BURST_COMPENSATION_UP_1_0,
      'BURST_COMPENSATION_UP_1_0',
    ],
    [
      BurstCompensationEnum.BURST_COMPENSATION_UP_1_3,
      'BURST_COMPENSATION_UP_1_3',
    ],
    [
      BurstCompensationEnum.BURST_COMPENSATION_UP_1_7,
      'BURST_COMPENSATION_UP_1_7',
    ],
    [
      BurstCompensationEnum.BURST_COMPENSATION_UP_2_0,
      'BURST_COMPENSATION_UP_2_0',
    ],
    [
      BurstCompensationEnum.BURST_COMPENSATION_UP_2_3,
      'BURST_COMPENSATION_UP_2_3',
    ],
    [
      BurstCompensationEnum.BURST_COMPENSATION_UP_2_7,
      'BURST_COMPENSATION_UP_2_7',
    ],
    [
      BurstCompensationEnum.BURST_COMPENSATION_UP_3_0,
      'BURST_COMPENSATION_UP_3_0',
    ],
    [
      BurstCompensationEnum.BURST_COMPENSATION_UP_3_3,
      'BURST_COMPENSATION_UP_3_3',
    ],
    [
      BurstCompensationEnum.BURST_COMPENSATION_UP_3_7,
      'BURST_COMPENSATION_UP_3_7',
    ],
    [
      BurstCompensationEnum.BURST_COMPENSATION_UP_4_0,
      'BURST_COMPENSATION_UP_4_0',
    ],
    [
      BurstCompensationEnum.BURST_COMPENSATION_UP_4_3,
      'BURST_COMPENSATION_UP_4_3',
    ],
    [
      BurstCompensationEnum.BURST_COMPENSATION_UP_4_7,
      'BURST_COMPENSATION_UP_4_7',
    ],
    [
      BurstCompensationEnum.BURST_COMPENSATION_UP_5_0,
      'BURST_COMPENSATION_UP_5_0',
    ],
  ];

  test('length', () => {
    expect(data.length).toBe(Object.keys(BurstCompensationEnum).length);
  });

  test('data', () => {
    data.forEach((item) => {
      expect(item[0] ? item[0].toString() : '').toBe(item[1]);
    });
  });
});

describe('BurstMaxExposureTimeEnum', () => {
  const data: string[][] = [
    [BurstMaxExposureTimeEnum.MAX_EXPOSURE_TIME_0_5, 'MAX_EXPOSURE_TIME_0_5'],
    [
      BurstMaxExposureTimeEnum.MAX_EXPOSURE_TIME_0_625,
      'MAX_EXPOSURE_TIME_0_625',
    ],
    [
      BurstMaxExposureTimeEnum.MAX_EXPOSURE_TIME_0_76923076,
      'MAX_EXPOSURE_TIME_0_76923076',
    ],
    [BurstMaxExposureTimeEnum.MAX_EXPOSURE_TIME_1, 'MAX_EXPOSURE_TIME_1'],
    [BurstMaxExposureTimeEnum.MAX_EXPOSURE_TIME_1_3, 'MAX_EXPOSURE_TIME_1_3'],
    [BurstMaxExposureTimeEnum.MAX_EXPOSURE_TIME_1_6, 'MAX_EXPOSURE_TIME_1_6'],
    [BurstMaxExposureTimeEnum.MAX_EXPOSURE_TIME_2, 'MAX_EXPOSURE_TIME_2'],
    [BurstMaxExposureTimeEnum.MAX_EXPOSURE_TIME_2_5, 'MAX_EXPOSURE_TIME_2_5'],
    [BurstMaxExposureTimeEnum.MAX_EXPOSURE_TIME_3_2, 'MAX_EXPOSURE_TIME_3_2'],
    [BurstMaxExposureTimeEnum.MAX_EXPOSURE_TIME_4, 'MAX_EXPOSURE_TIME_4'],
    [BurstMaxExposureTimeEnum.MAX_EXPOSURE_TIME_5, 'MAX_EXPOSURE_TIME_5'],
    [BurstMaxExposureTimeEnum.MAX_EXPOSURE_TIME_6, 'MAX_EXPOSURE_TIME_6'],
    [BurstMaxExposureTimeEnum.MAX_EXPOSURE_TIME_8, 'MAX_EXPOSURE_TIME_8'],
    [BurstMaxExposureTimeEnum.MAX_EXPOSURE_TIME_10, 'MAX_EXPOSURE_TIME_10'],
    [BurstMaxExposureTimeEnum.MAX_EXPOSURE_TIME_13, 'MAX_EXPOSURE_TIME_13'],
    [BurstMaxExposureTimeEnum.MAX_EXPOSURE_TIME_15, 'MAX_EXPOSURE_TIME_15'],
    [BurstMaxExposureTimeEnum.MAX_EXPOSURE_TIME_20, 'MAX_EXPOSURE_TIME_20'],
    [BurstMaxExposureTimeEnum.MAX_EXPOSURE_TIME_25, 'MAX_EXPOSURE_TIME_25'],
    [BurstMaxExposureTimeEnum.MAX_EXPOSURE_TIME_30, 'MAX_EXPOSURE_TIME_30'],
    [BurstMaxExposureTimeEnum.MAX_EXPOSURE_TIME_40, 'MAX_EXPOSURE_TIME_40'],
    [BurstMaxExposureTimeEnum.MAX_EXPOSURE_TIME_50, 'MAX_EXPOSURE_TIME_50'],
    [BurstMaxExposureTimeEnum.MAX_EXPOSURE_TIME_60, 'MAX_EXPOSURE_TIME_60'],
  ];

  test('length', () => {
    expect(data.length).toBe(Object.keys(BurstMaxExposureTimeEnum).length);
  });

  test('data', () => {
    data.forEach((item) => {
      expect(item[0] ? item[0].toString() : '').toBe(item[1]);
    });
  });
});

describe('BurstEnableIsoControlEnum', () => {
  const data: string[][] = [
    [BurstEnableIsoControlEnum.OFF, 'OFF'],
    [BurstEnableIsoControlEnum.ON, 'ON'],
  ];

  test('length', () => {
    expect(data.length).toBe(Object.keys(BurstEnableIsoControlEnum).length);
  });

  test('data', () => {
    data.forEach((item) => {
      expect(item[0] ? item[0].toString() : '').toBe(item[1]);
    });
  });
});

describe('BurstOrderEnum', () => {
  const data: string[][] = [
    [BurstOrderEnum.BURST_BRACKET_ORDER_0, 'BURST_BRACKET_ORDER_0'],
    [BurstOrderEnum.BURST_BRACKET_ORDER_1, 'BURST_BRACKET_ORDER_1'],
  ];

  test('length', () => {
    expect(data.length).toBe(Object.keys(BurstOrderEnum).length);
  });

  test('data', () => {
    data.forEach((item) => {
      expect(item[0] ? item[0].toString() : '').toBe(item[1]);
    });
  });
});
