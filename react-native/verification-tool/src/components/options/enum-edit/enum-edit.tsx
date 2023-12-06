import * as React from 'react';
import { ItemSelectorView } from '../../ui/item-list';

interface Props<T> {
  title: string;
  option: T;
  onChange: (option: T) => void;
  optionEnum: Record<string, T>;
}

export const EnumEdit = <T,>({
  title,
  option,
  onChange,
  optionEnum,
}: Props<T>) => {
  const enumList = [
    { name: '[undefined]', value: undefined },
    ...Object.entries(optionEnum).map((item) => {
      return { name: item[0], value: item[1] };
    }),
  ];

  return (
    <ItemSelectorView
      itemList={enumList}
      title={title}
      onSelected={(item) => {
        onChange(item.value);
      }}
      selectedItem={enumList.find((item) => {
        return item.value === option;
      })}
    />
  );
};

EnumEdit.displayName = 'EnumEdit';

export default EnumEdit;
