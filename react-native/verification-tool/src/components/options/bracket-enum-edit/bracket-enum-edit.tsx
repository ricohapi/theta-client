import * as React from 'react';
import { ItemSelectorView } from '../../ui/item-list';
import type { BracketSetting } from 'theta-client-react-native';

interface Props {
  onChange: (bracketSetting: BracketSetting) => void;
  bracketSetting: BracketSetting;
  propName: Extract<keyof BracketSetting, string>;
  enumValues: Record<string, string>;
}

export const BracketEnumEdit: React.FC<Props> = ({
  propName,
  onChange,
  bracketSetting,
  enumValues,
}) => {
  const enumList = [
    { name: '[undefined]', value: undefined },
    ...Object.entries(enumValues).map((item) => {
      return { name: item[0], value: item[1] };
    }),
  ];
  return (
    <ItemSelectorView
      itemList={enumList}
      title={propName}
      onSelected={(item) => {
        onChange({ [propName]: item.value });
      }}
      selectedItem={enumList.find((item) => {
        return item.value === bracketSetting[propName];
      })}
    />
  );
};

BracketEnumEdit.displayName = 'BracketEnumEdit';

export default BracketEnumEdit;
