import * as React from 'react';
import type { OptionEditProps } from '..';
import { Item, ItemSelectorView } from '../../ui/item-list';

interface Props extends OptionEditProps {
  enumList: Item[];
  propName: string;
}

export const EnumEdit: React.FC<Props> = ({
  propName,
  enumList,
  onChange,
  options,
}) => {
  return (
    <ItemSelectorView
      itemList={enumList}
      title={propName}
      onSelected={(item) => {
        let option = { [propName]: item.value };
        onChange(option);
      }}
      selectedItem={enumList.find((item) => {
        if (options != null) {
          const option = Object.entries(options).find(
            (element) => element[0] === propName
          );
          if (option != null) {
            return item.value === option[1];
          }
        }
        return false;
      })}
    />
  );
};

EnumEdit.displayName = 'EnumEdit';

export default EnumEdit;
