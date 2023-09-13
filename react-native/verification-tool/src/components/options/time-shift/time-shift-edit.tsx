import * as React from 'react';
import { TimeShift, TimeShiftIntervalEnum } from 'theta-client-react-native';
import type { OptionEditProps } from '..';
import { View } from 'react-native';
import { ItemSelectorView } from '../../ui/item-list';
import { TitledSwitch } from '../../ui/titled-switch';

export const TimeShiftEdit: React.FC<OptionEditProps> = ({
  onChange,
  options,
}) => {
  const [editTimeShift, setEditTimeSHift] = React.useState<TimeShift>({});

  React.useEffect(() => {
    const timeShift = options?.timeShift || {};
    if (timeShift.isFrontFirst == null) {
      timeShift.isFrontFirst = true;
    }
    setEditTimeSHift(timeShift);
  }, [options]);

  const intervalList = [
    { name: '[undefined]', value: undefined },
    { name: 'INTERVAL_0', value: TimeShiftIntervalEnum.INTERVAL_0 },
    { name: 'INTERVAL_1', value: TimeShiftIntervalEnum.INTERVAL_1 },
    { name: 'INTERVAL_2', value: TimeShiftIntervalEnum.INTERVAL_2 },
    { name: 'INTERVAL_3', value: TimeShiftIntervalEnum.INTERVAL_3 },
    { name: 'INTERVAL_4', value: TimeShiftIntervalEnum.INTERVAL_4 },
    { name: 'INTERVAL_5', value: TimeShiftIntervalEnum.INTERVAL_5 },
    { name: 'INTERVAL_6', value: TimeShiftIntervalEnum.INTERVAL_6 },
    { name: 'INTERVAL_7', value: TimeShiftIntervalEnum.INTERVAL_7 },
    { name: 'INTERVAL_8', value: TimeShiftIntervalEnum.INTERVAL_8 },
    { name: 'INTERVAL_9', value: TimeShiftIntervalEnum.INTERVAL_9 },
    { name: 'INTERVAL_10', value: TimeShiftIntervalEnum.INTERVAL_10 },
  ];

  return (
    <View>
      <TitledSwitch
        title="isFrontFirst"
        value={editTimeShift.isFrontFirst}
        onChange={(newValue) => {
          const timeShift = { ...editTimeShift };
          timeShift.isFrontFirst = newValue;
          setEditTimeSHift(timeShift);
          onChange({
            timeShift: timeShift,
          });
        }}
      />
      <ItemSelectorView
        itemList={intervalList}
        title={'firstInterval'}
        onSelected={(item) => {
          const timeShift = { ...editTimeShift };
          timeShift.firstInterval = item.value;
          setEditTimeSHift(timeShift);
          onChange({
            timeShift: timeShift,
          });
        }}
        selectedItem={intervalList.find(
          (item) => item.value === editTimeShift.firstInterval
        )}
      />
      <ItemSelectorView
        itemList={intervalList}
        title={'secondInterval'}
        onSelected={(item) => {
          const timeShift = { ...editTimeShift };
          timeShift.secondInterval = item.value;
          setEditTimeSHift(timeShift);
          onChange({
            timeShift: timeShift,
          });
        }}
        selectedItem={intervalList.find(
          (item) => item.value === editTimeShift.secondInterval
        )}
      />
    </View>
  );
};

TimeShiftEdit.displayName = 'TimeShiftEdit';

export default TimeShiftEdit;
