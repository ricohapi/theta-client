import { CameraErrorEnum } from '../../../theta-repository';

describe('CameraErrorEnum', () => {
  const data: [CameraErrorEnum, string][] = [
    [CameraErrorEnum.UNKNOWN, 'UNKNOWN'],
    [CameraErrorEnum.NO_MEMORY, 'NO_MEMORY'],
    [CameraErrorEnum.FILE_NUMBER_OVER, 'FILE_NUMBER_OVER'],
    [CameraErrorEnum.NO_DATE_SETTING, 'NO_DATE_SETTING'],
    [CameraErrorEnum.READ_ERROR, 'READ_ERROR'],
    [CameraErrorEnum.NOT_SUPPORTED_MEDIA_TYPE, 'NOT_SUPPORTED_MEDIA_TYPE'],
    [CameraErrorEnum.NOT_SUPPORTED_FILE_SYSTEM, 'NOT_SUPPORTED_FILE_SYSTEM'],
    [CameraErrorEnum.MEDIA_NOT_READY, 'MEDIA_NOT_READY'],
    [CameraErrorEnum.NOT_ENOUGH_BATTERY, 'NOT_ENOUGH_BATTERY'],
    [CameraErrorEnum.INVALID_FILE, 'INVALID_FILE'],
    [CameraErrorEnum.PLUGIN_BOOT_ERROR, 'PLUGIN_BOOT_ERROR'],
    [CameraErrorEnum.IN_PROGRESS_ERROR, 'IN_PROGRESS_ERROR'],
    [CameraErrorEnum.CANNOT_RECORDING, 'CANNOT_RECORDING'],
    [CameraErrorEnum.CANNOT_RECORD_LOWBAT, 'CANNOT_RECORD_LOWBAT'],
    [CameraErrorEnum.CAPTURE_HW_FAILED, 'CAPTURE_HW_FAILED'],
    [CameraErrorEnum.CAPTURE_SW_FAILED, 'CAPTURE_SW_FAILED'],
    [CameraErrorEnum.INTERNAL_MEM_ACCESS_FAIL, 'INTERNAL_MEM_ACCESS_FAIL'],
    [CameraErrorEnum.UNEXPECTED_ERROR, 'UNEXPECTED_ERROR'],
    [CameraErrorEnum.BATTERY_CHARGE_FAIL, 'BATTERY_CHARGE_FAIL'],
    [CameraErrorEnum.HIGH_TEMPERATURE_WARNING, 'HIGH_TEMPERATURE_WARNING'],
    [CameraErrorEnum.HIGH_TEMPERATURE, 'HIGH_TEMPERATURE'],
    [CameraErrorEnum.BATTERY_HIGH_TEMPERATURE, 'BATTERY_HIGH_TEMPERATURE'],
    [CameraErrorEnum.COMPASS_CALIBRATION, 'COMPASS_CALIBRATION'],
  ];

  test('length', () => {
    expect(data.length).toBe(Object.keys(CameraErrorEnum).length);
  });

  test('data', () => {
    data.forEach((item) => {
      expect(item[0]).toBe(item[1]);
    });
  });
});
