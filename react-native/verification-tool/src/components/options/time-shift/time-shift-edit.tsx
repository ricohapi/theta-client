import * as React from 'react';
import {
  TimeShift,
  TimeShiftIntervalEnum,
} from '../../../modules/theta-client';
import { EnumEdit, type OptionEditProps } from '..';
import { View } from 'react-native';
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

  return (
    <View>
      <TitledSwitch
        title="isFrontFirst"
        value={editTimeShift.isFrontFirst}
        onChange={(isFrontFirst) => {
          const timeShift = { ...editTimeShift, isFrontFirst };
          setEditTimeSHift(timeShift);
          onChange({
            timeShift,
          });
        }}
      />
      <EnumEdit
        title={'firstInterval'}
        option={editTimeShift.firstInterval}
        onChange={(firstInterval) => {
          const timeShift = { ...editTimeShift, firstInterval };
          setEditTimeSHift(timeShift);
          onChange({
            timeShift,
          });
        }}
        optionEnum={TimeShiftIntervalEnum}
      />
      <EnumEdit
        title={'secondInterval'}
        option={editTimeShift.secondInterval}
        onChange={(secondInterval) => {
          const timeShift = { ...editTimeShift, secondInterval };
          setEditTimeSHift(timeShift);
          onChange({
            timeShift,
          });
        }}
        optionEnum={TimeShiftIntervalEnum}
      />
    </View>
  );
};

TimeShiftEdit.displayName = 'TimeShiftEdit';

export default TimeShiftEdit;
