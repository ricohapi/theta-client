import React from 'react';
import { ScrollView, Text, View } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import styles from './styles';
import Button from '../../components/ui/button';
import {
  ApertureEnum,
  CaptureModeEnum,
  ExposureCompensationEnum,
  ExposureDelayEnum,
  ExposureProgramEnum,
  IsoAutoHighLimitEnum,
  IsoEnum,
  MaxRecordableTimeEnum,
  OptionNameEnum,
  Options,
  PresetEnum,
  TopBottomCorrectionOptionEnum,
  VideoStitchingEnum,
  VisibilityReductionEnum,
  WhiteBalanceEnum,
  getOptions,
  setOptions,
} from 'theta-client-react-native';
import {
  ApertureEdit,
  CaptureModeEdit,
  ExposureCompensationEdit,
  ExposureDelayEdit,
  ExposureProgramEdit,
  FilterEdit,
  GpsInfoEdit,
  IsoAutoHighLimitEdit,
  IsoEdit,
  MaxRecordableTimeEdit,
  PresetEdit,
  TimeShiftEdit,
  TopBottomCorrectionEdit,
  TopBottomCorrectionRotationEdit,
  VideoStitchingEdit,
  VisibilityReductionEdit,
  WhiteBalanceEdit,
} from '../../components/options';
import { ItemSelectorView, type Item } from '../../components/ui/item-list';
import { NumberEdit } from '../../components/options/number-edit';

interface OptionItem extends Item {
  value: {
    optionName: OptionNameEnum;
    editor?: (
      options: Options,
      onChange: (options: Options) => void
    ) => React.ReactElement;
    defaultValue?: Options;
  };
}

const optionList: OptionItem[] = [
  {
    name: 'aperture',
    value: {
      optionName: OptionNameEnum.Aperture,
      editor: (options, onChange) => (
        <ApertureEdit onChange={onChange} options={options} />
      ),
      defaultValue: { aperture: ApertureEnum.APERTURE_AUTO },
    },
  },
  {
    name: 'captureMode',
    value: {
      optionName: OptionNameEnum.CaptureMode,
      editor: (options, onChange) => (
        <CaptureModeEdit onChange={onChange} options={options} />
      ),
      defaultValue: { captureMode: CaptureModeEnum.IMAGE },
    },
  },
  {
    name: 'colorTemperature',
    value: {
      optionName: OptionNameEnum.ColorTemperature,
      editor: (options, onChange) => (
        <NumberEdit
          propName={'colorTemperature'}
          onChange={onChange}
          options={options}
        />
      ),
    },
  },
  {
    name: 'exposureCompensation',
    value: {
      optionName: OptionNameEnum.ExposureCompensation,
      editor: (options, onChange) => (
        <ExposureCompensationEdit onChange={onChange} options={options} />
      ),
      defaultValue: { exposureCompensation: ExposureCompensationEnum.ZERO },
    },
  },
  {
    name: 'exposureDelay',
    value: {
      optionName: OptionNameEnum.ExposureDelay,
      editor: (options, onChange) => (
        <ExposureDelayEdit onChange={onChange} options={options} />
      ),
      defaultValue: { exposureDelay: ExposureDelayEnum.DELAY_OFF },
    },
  },
  {
    name: 'exposureProgram',
    value: {
      optionName: OptionNameEnum.ExposureProgram,
      editor: (options, onChange) => (
        <ExposureProgramEdit onChange={onChange} options={options} />
      ),
      defaultValue: { exposureProgram: ExposureProgramEnum.NORMAL_PROGRAM },
    },
  },
  {
    name: 'filter',
    value: {
      optionName: OptionNameEnum.Filter,
      editor: (options, onChange) => (
        <FilterEdit onChange={onChange} options={options} />
      ),
    },
  },
  {
    name: 'gpsInfo',
    value: {
      optionName: OptionNameEnum.GpsInfo,
      editor: (options, onChange) => (
        <GpsInfoEdit onChange={onChange} options={options} hideLabel />
      ),
    },
  },
  {
    name: 'iso',
    value: {
      optionName: OptionNameEnum.Iso,
      editor: (options, onChange) => (
        <IsoEdit onChange={onChange} options={options} />
      ),
      defaultValue: { iso: IsoEnum.ISO_AUTO },
    },
  },
  {
    name: 'isoAutoHighLimit',
    value: {
      optionName: OptionNameEnum.IsoAutoHighLimit,
      editor: (options, onChange) => (
        <IsoAutoHighLimitEdit onChange={onChange} options={options} />
      ),
      defaultValue: { isoAutoHighLimit: IsoAutoHighLimitEnum.ISO_100 },
    },
  },
  {
    name: 'latestEnabledExposureDelayTime',
    value: {
      optionName: OptionNameEnum.LatestEnabledExposureDelayTime,
    },
  },
  {
    name: 'maxRecordableTime',
    value: {
      optionName: OptionNameEnum.MaxRecordableTime,
      editor: (options, onChange) => (
        <MaxRecordableTimeEdit onChange={onChange} options={options} />
      ),
      defaultValue: {
        maxRecordableTime: MaxRecordableTimeEnum.RECORDABLE_TIME_1500,
      },
    },
  },
  {
    name: 'preset',
    value: {
      optionName: OptionNameEnum.Preset,
      editor: (options, onChange) => (
        <PresetEdit onChange={onChange} options={options} />
      ),
      defaultValue: { preset: PresetEnum.FACE },
    },
  },
  {
    name: 'timeShift',
    value: {
      optionName: OptionNameEnum.TimeShift,
      editor: (options, onChange) => (
        <TimeShiftEdit onChange={onChange} options={options} />
      ),
    },
  },
  {
    name: 'topBottomCorrection',
    value: {
      optionName: OptionNameEnum.TopBottomCorrection,
      editor: (options, onChange) => (
        <TopBottomCorrectionEdit onChange={onChange} options={options} />
      ),
      defaultValue: {
        topBottomCorrection: TopBottomCorrectionOptionEnum.APPLY_AUTO,
      },
    },
  },
  {
    name: 'videoStitching',
    value: {
      optionName: OptionNameEnum.VideoStitching,
      editor: (options, onChange) => (
        <VideoStitchingEdit onChange={onChange} options={options} />
      ),
      defaultValue: {
        videoStitching: VideoStitchingEnum.NONE,
      },
    },
  },
  {
    name: 'topBottomCorrectionRotation',
    value: {
      optionName: OptionNameEnum.TopBottomCorrectionRotation,
      editor: (options, onChange) => (
        <TopBottomCorrectionRotationEdit
          onChange={onChange}
          options={options}
        />
      ),
    },
  },
  {
    name: 'visibilityReduction',
    value: {
      optionName: OptionNameEnum.VisibilityReduction,
      editor: (options, onChange) => (
        <VisibilityReductionEdit onChange={onChange} options={options} />
      ),
      defaultValue: {
        visibilityReduction: VisibilityReductionEnum.OFF,
      },
    },
  },
  {
    name: 'whiteBalance',
    value: {
      optionName: OptionNameEnum.WhiteBalance,
      editor: (options, onChange) => (
        <WhiteBalanceEdit onChange={onChange} options={options} />
      ),
      defaultValue: { whiteBalance: WhiteBalanceEnum.AUTO },
    },
  },
];

const OptionsScreen: React.FC = ({ navigation }) => {
  const [selectedOption, setSelectedOption] = React.useState<OptionItem>();
  const [message, setMessage] = React.useState('');
  const [editOptions, setEditOptions] = React.useState<Options>();

  React.useEffect(() => {
    navigation.setOptions({ title: 'Options' });
  }, [navigation]);

  const onChangeOption = (item: OptionItem) => {
    setMessage('');
    setSelectedOption(item);
    setEditOptions(item.value.defaultValue);
  };

  const onPressGet = async () => {
    if (selectedOption == null) {
      return;
    }
    try {
      const options = await getOptions([selectedOption.value.optionName]);
      setMessage(JSON.stringify(options));
      setEditOptions(options);
    } catch (error) {
      setMessage(JSON.stringify(error));
    }
  };

  const onPressSet = async () => {
    if (selectedOption == null || editOptions == null) {
      return;
    }
    console.log('call setOptions(): ' + JSON.stringify(editOptions));
    try {
      await setOptions(editOptions);
      setMessage('OK.\n' + JSON.stringify(editOptions));
    } catch (error) {
      setMessage(JSON.stringify(error));
    }
  };

  return (
    <SafeAreaView
      style={styles.safeAreaContainer}
      edges={['left', 'right', 'bottom']}
    >
      <View style={styles.topViewContainer}>
        <View style={styles.rowContainerLayout}>
          <ItemSelectorView
            style={undefined}
            title="Option"
            itemList={optionList}
            onSelected={(item) => onChangeOption(item)}
            selectedItem={selectedOption}
            placeHolder="select option"
          />
          <Button
            title="Get"
            onPress={onPressGet}
            disabled={selectedOption == null}
          />
        </View>
      </View>
      {selectedOption?.value.editor && (
        <View style={styles.editorContainerLayout}>
          <View style={styles.contentContainer}>
            {selectedOption?.value.editor(editOptions || {}, setEditOptions)}
          </View>
          <Button
            title="Set"
            onPress={onPressSet}
            disabled={editOptions == null}
          />
        </View>
      )}
      <View style={styles.bottomViewContainer}>
        <ScrollView style={styles.messageArea}>
          <Text style={styles.messageText}>{message}</Text>
        </ScrollView>
      </View>
    </SafeAreaView>
  );
};

export default OptionsScreen;
