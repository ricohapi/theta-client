import React from 'react';
import { View, Alert, ScrollView, Text } from 'react-native';
import Button from '../../components/ui/button';
import { CaptureCommonOptionsEdit } from '../../components/capture/capture-common-options';
import { EnumEdit } from '../../components/options';
import { InputNumber } from '../../components/ui/input-number';
import {
    CaptureModeEnum,
    FilterEnum,
    Options,
    PhotoFileFormatEnum,
    PresetEnum,
    getPhotoCaptureBuilder,
    setOptions,
    stopSelfTimer,
} from '../../modules/theta-client';
import styles from './styles.tsx';

interface PhotoCaptureScreenProps {
    navigation: {
        setOptions: (options: { title: string }) => void;
        navigate: (screen: string, params?: any) => void;
    };
}

const PhotoCaptureScreen: React.FC<PhotoCaptureScreenProps> = ({ navigation }) => {
    const [interval, setInterval] = React.useState<number>();
    const [message, setMessage] = React.useState('');
    const [captureOptions, setCaptureOptions] = React.useState<Options>({});
    const [isTaking, setIsTaking] = React.useState(false);

    const onTakePhoto = async () => {
        setIsTaking(true);

        const builder = getPhotoCaptureBuilder();
        if (interval != null) {
            builder.setCheckStatusCommandInterval(interval);
        }
        captureOptions?.filter && builder.setFilter(captureOptions.filter);
        captureOptions?.fileFormat &&
            builder.setFileFormat(captureOptions.fileFormat as PhotoFileFormatEnum);
        captureOptions?.preset && builder.setPreset(captureOptions.preset);

        captureOptions?.aperture && builder.setAperture(captureOptions.aperture);
        if (captureOptions?.colorTemperature != null) {
            builder.setColorTemperature(captureOptions.colorTemperature);
        }
        captureOptions?.exposureCompensation &&
            builder.setExposureCompensation(captureOptions.exposureCompensation);
        captureOptions?.exposureDelay &&
            builder.setExposureDelay(captureOptions.exposureDelay);
        captureOptions?.exposureProgram &&
            builder.setExposureProgram(captureOptions.exposureProgram);
        captureOptions?.gpsInfo && builder.setGpsInfo(captureOptions.gpsInfo);
        captureOptions?._gpsTagRecording &&
            builder.setGpsTagRecording(captureOptions._gpsTagRecording);
        captureOptions?.iso && builder.setIso(captureOptions.iso);
        captureOptions?.isoAutoHighLimit &&
            builder.setIsoAutoHighLimit(captureOptions.isoAutoHighLimit);
        captureOptions?.whiteBalance &&
            builder.setWhiteBalance(captureOptions.whiteBalance);

        console.log('takePicture options: ' + JSON.stringify(captureOptions));
        console.log('takePicture builder: ' + JSON.stringify(builder));

        try {
            setMessage('');
            const photoCapture = await builder.build();
            const url = await photoCapture.takePicture((status) => {
                setMessage(`onCapturing: ${status}`);
            });

            setIsTaking(false);

            if (url) {
                const fileName = url.split('/').pop() || 'photo.jpg';
                Alert.alert(
                    'takePicture Success',
                    `File: ${fileName}`,
                    [
                        { text: 'OK' },
                        {
                            text: 'Preview',
                            onPress: () => {
                                navigation.navigate('filePreview', {
                                    fileInfo: {
                                        name: fileName,
                                        fileUrl: url,
                                    },
                                });
                            },
                        },
                    ]
                );
            } else {
                Alert.alert('takePicture canceled.');
            }
        } catch (error) {
            setIsTaking(false);
            Alert.alert('takePicture error', JSON.stringify(error), [{ text: 'OK' }]);
        }
    };

    const onStopSelfTimer = async () => {
        try {
            await stopSelfTimer();
        } catch (error) {
            Alert.alert('stopSelfTimer error', JSON.stringify(error), [
                { text: 'OK' },
            ]);
        }
    };

    React.useEffect(() => {
        setOptions({ captureMode: CaptureModeEnum.IMAGE }).catch();
    }, []);

    React.useEffect(() => {
        navigation.setOptions({ title: 'Photo Capture' });
    }, [navigation]);

    return (
        <View style={styles.safeAreaContainer}>
            <View style={styles.topViewContainer}>
                <Text style={styles.itemText}>{message}</Text>
                <View style={styles.bottomViewContainerLayout}>
                    <Button
                        style={styles.button}
                        title="take photo"
                        onPress={onTakePhoto}
                        disabled={isTaking}
                    />
                    <Button
                        style={styles.button}
                        title="stop self timer"
                        onPress={onStopSelfTimer}
                        disabled={!isTaking}
                    />
                </View>
            </View>
            <ScrollView style={styles.contentContainer}>
                <InputNumber
                    title="CheckStatusCommandInterval"
                    placeHolder="Input value"
                    value={interval}
                    onChange={(value) => {
                        setInterval(value);
                    }}
                />
                <EnumEdit
                    title={'filter'}
                    option={captureOptions?.filter}
                    onChange={(filter) => {
                        setCaptureOptions((prevState) => ({
                            ...prevState,
                            filter,
                        }));
                    }}
                    optionEnum={FilterEnum}
                />
                <EnumEdit
                    title={'fileFormat'}
                    option={captureOptions?.fileFormat}
                    onChange={(fileFormat) => {
                        setCaptureOptions((prevState) => ({
                            ...prevState,
                            fileFormat,
                        }));
                    }}
                    optionEnum={PhotoFileFormatEnum}
                />
                <EnumEdit
                    title={'preset'}
                    option={captureOptions?.preset}
                    onChange={(preset) => {
                        setCaptureOptions((prevState) => ({
                            ...prevState,
                            preset,
                        }));
                    }}
                    optionEnum={PresetEnum}
                />
                <CaptureCommonOptionsEdit
                    onChange={(option) => {
                        setCaptureOptions(option);
                    }}
                    options={captureOptions}
                />
            </ScrollView>
        </View>
    );
};

export default PhotoCaptureScreen;
