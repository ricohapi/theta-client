import * as React from 'react';
import type { OptionEditProps } from '..';
import { InputString } from '../../ui/input-string';
import type { Options } from 'theta-client-react-native';

interface Props extends OptionEditProps {
  propName: string;
  placeHolder?: string;
}

export const StringEdit: React.FC<Props> = ({
  propName,
  onChange,
  options,
  placeHolder,
}) => {
  const getOptionPropString = (_options: Options, _propName: string) => {
    if (_options != null) {
      const option = Object.entries(_options).find(
        (element) => element[0] === _propName
      );
      if (option != null) {
        return option[1] as string;
      }
    }
    return undefined;
  };
  return (
    <InputString
      title={propName}
      placeHolder={placeHolder}
      value={
        options != null ? getOptionPropString(options, propName) : undefined
      }
      onChange={(value) => {
        let newValue = value.length === 0 ? undefined : value;
        let option = { ...options, [propName]: newValue };
        onChange(option);
      }}
    />
  );
};

StringEdit.displayName = 'StringEdit';

export default StringEdit;
