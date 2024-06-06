import * as React from 'react';
import type { OptionEditProps } from '..';
import { InputNumber } from '../../ui/input-number';
import type { Options } from '../../../modules/theta-client';

interface Props extends OptionEditProps {
  propName: string;
  placeHolder?: string;
}

export const NumberEdit: React.FC<Props> = ({
  propName,
  onChange,
  options,
  placeHolder,
}) => {
  const getOptionPropNumber = (_options: Options, _propName: string) => {
    if (_options != null) {
      const option = Object.entries(_options).find(
        (element) => element[0] === _propName
      );
      if (option != null) {
        return option[1] as number;
      }
    }
    return undefined;
  };
  return (
    <InputNumber
      title={propName}
      placeHolder={placeHolder}
      value={
        options != null ? getOptionPropNumber(options, propName) : undefined
      }
      onChange={(value) => {
        let option = { ...options, [propName]: value };
        onChange(option);
      }}
    />
  );
};

NumberEdit.displayName = 'NumberEdit';

export default NumberEdit;
