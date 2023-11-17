import * as React from 'react';
import type { OptionEditProps } from '..';
import { View, Text } from 'react-native';
import { InputNumber } from '../../ui/input-number';
import styles from './styles';
import Button from '../../ui/button';
import {
  ApertureEnum,
  BracketSetting,
  ExposureCompensationEnum,
  ExposureProgramEnum,
  IsoEnum,
  ShutterSpeedEnum,
  WhiteBalanceEnum,
} from 'theta-client-react-native';
import { BracketEnumEdit } from '../bracket-enum-edit';

const EditItem = ({
  item,
  onChange,
}: {
  item: BracketSetting;
  onChange: (newItem: BracketSetting) => void;
}) => {
  return (
    <View>
      <BracketEnumEdit
        propName={'aperture'}
        onChange={({ aperture }) => {
          onChange({ ...item, aperture });
        }}
        bracketSetting={item}
        enumValues={ApertureEnum}
      />
      <InputNumber
        title={'colorTemperature'}
        onChange={(value) => {
          onChange({ ...item, colorTemperature: value });
        }}
        value={item.colorTemperature}
      />
      <BracketEnumEdit
        propName={'exposureCompensation'}
        onChange={({ exposureCompensation }) => {
          onChange({ ...item, exposureCompensation });
        }}
        bracketSetting={item}
        enumValues={ExposureCompensationEnum}
      />
      <BracketEnumEdit
        propName={'exposureProgram'}
        onChange={({ exposureProgram }) => {
          onChange({ ...item, exposureProgram });
        }}
        bracketSetting={item}
        enumValues={ExposureProgramEnum}
      />
      <BracketEnumEdit
        propName={'iso'}
        onChange={({ iso }) => {
          onChange({ ...item, iso });
        }}
        bracketSetting={item}
        enumValues={IsoEnum}
      />
      <BracketEnumEdit
        propName={'shutterSpeed'}
        onChange={({ shutterSpeed }) => {
          onChange({ ...item, shutterSpeed });
        }}
        bracketSetting={item}
        enumValues={ShutterSpeedEnum}
      />
      <BracketEnumEdit
        propName={'whiteBalance'}
        onChange={({ whiteBalance }) => {
          onChange({ ...item, whiteBalance });
        }}
        bracketSetting={item}
        enumValues={WhiteBalanceEnum}
      />
    </View>
  );
};

interface BracketSettingMenuProps {
  item: BracketSetting;
  index: number;
  onChange: (index: number, setting: BracketSetting) => void;
  onRemove: (index: number) => void;
}

const BracketSettingMenu: React.FC<BracketSettingMenuProps> = ({
  item,
  index,
  onChange,
  onRemove,
}) => {
  return (
    <View key={index}>
      <Text style={styles.itemText}>
        {'Bracket parameters: ' + (index + 1)}
      </Text>
      <View style={styles.removeButtonContainerLayout}>
        <Button
          style={styles.button}
          title="Remove setting"
          onPress={() => onRemove(index)}
        />
      </View>
      <EditItem item={item} onChange={(setting) => onChange(index, setting)} />
    </View>
  );
};

export const AutoBracketEdit: React.FC<OptionEditProps> = ({
  onChange,
  options,
}) => {
  const onChangeSetting = (index: number, autoBracket: BracketSetting) => {
    const autoBracketList = (options?.autoBracket ?? []).map((item, idx) =>
      idx === index ? autoBracket : item
    );
    onChange({ ...options, autoBracket: autoBracketList });
  };
  const onRemoveSetting = (index: number) => {
    const autoBracketList = [...(options?.autoBracket ?? [])].filter(
      (_, idx) => idx !== index
    );
    const autoBracket =
      autoBracketList.length > 0 ? autoBracketList : undefined;
    onChange({
      ...options,
      autoBracket,
    });
  };
  const onAddSetting = () => {
    const newList = [...(options?.autoBracket ?? []), {}];
    onChange({ ...options, autoBracket: newList });
  };

  return (
    <View style={styles.editorContainerLayout}>
      {options?.autoBracket?.map((item, index) => (
        <BracketSettingMenu
          item={item}
          index={index}
          onChange={onChangeSetting}
          onRemove={onRemoveSetting}
          key={index}
        />
      )) ?? null}
      <View style={styles.addButtonContainerLayout}>
        <Button
          style={styles.button}
          title="Add setting"
          onPress={onAddSetting}
        />
      </View>
    </View>
  );
};

AutoBracketEdit.displayName = 'AutoBracketEdit';

export default AutoBracketEdit;
