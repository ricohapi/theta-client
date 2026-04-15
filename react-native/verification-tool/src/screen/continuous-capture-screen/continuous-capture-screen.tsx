import React from 'react';
import { View, Alert, ScrollView, Text, SafeAreaView } from 'react-native';
import styles from './styles.tsx';
import Button from '../../components/ui/button';
import {
    CaptureModeEnum,
    ContinuousCapture,
    ContinuousNumberEnum,
    PhotoFileFormatEnum,
    getContinuousCaptureBuilder,
    setOptions,
} from '../../modules/theta-client';
import { CaptureCommonOptionsEdit } from '../../components/capture/capture-common-options';
import { EnumEdit } from '../../components/options';
import { InputNumber } from '../../components/ui/input-number';

interface ContinuousCaptureScreenProps {
    navigation: {
        setOptions: (options: { title: string }) => void;
    };
}

const ContinuousCaptureScreen: React.FC<ContinuousCaptureScreenProps> = ({ navigation }) => {
    const [interval, setInterval] = React.useState<number>();
    const [captureOptions, setCaptureOptions] = React.useState<any>({});
    const [isTaking, setIsTaking] = React.useState(false);
    const [capture, setCapture] = React.useState<ContinuousCapture>();
    const [continuousNumber, setContinuousNumber] =
        React.useState<ContinuousNumberEnum>();

    const onTake = async () => {
        if (isTaking) {
            return;
        }

        const builder = getContinuousCaptureBuilder();
        if (interval != null) {
            builder.setCheckStatusCommandInterval(interval);
        }
        captureOptions?.fileFormat &&
            builder.setFileFormat(captureOptions.fileFormat);

        console.log('ContinuousCapture interval: ' + interval);
        console.log('ContinuousCapture options: ' + JSON.stringify(captureOptions));
        console.log('ContinuousCapture builder: ' + JSON.stringify(builder));

        try {
            setCapture(await builder.build());
            setIsTaking(false);
        } catch (error) {
            setIsTaking(false);
            if (error instanceof Error) {
                Alert.alert(
                    'ContinuousCaptureBuilder build error',
                    error.name + ': ' + error.message,
                    [{ text: 'OK' }]
                );
            }
        }
    };

    const initCapture = () => {
        setCapture(undefined);
        setIsTaking(false);
    };

    const startCapture = async () => {
        if (capture == null) {
            initCapture();
            return;
        }
        try {
            console.log('ContinuousCapture startCapture');

            const number = await capture.getContinuousNumber();
            setContinuousNumber(number);

            const urls = await capture.startCapture();
            initCapture();
            if (urls) {
                Alert.alert(`file ${urls.length} urls : `, urls.join('\n'), [
                    { text: 'OK' },
                ]);
            } else {
                Alert.alert('Capture', 'Capture cancel', [{ text: 'OK' }]);
            }
        } catch (error) {
            initCapture();
            if (error instanceof Error) {
                Alert.alert('startCapture error', error.name + ': ' + error.message, [
                    { text: 'OK' },
                ]);
            }
        }
    };

    React.useEffect(() => {
        setOptions({ captureMode: CaptureModeEnum.IMAGE }).catch();
    }, []);

    React.useEffect(() => {
        navigation.setOptions({ title: 'Continuous Capture' });
    }, [navigation]);

    React.useEffect(() => {
        if (capture != null && !isTaking) {
            setIsTaking(true);
            startCapture();
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [capture]);

    return (
        <SafeAreaView style={styles.safeAreaContainer}>
            <View style={styles.topViewContainer}>
                <Text style={styles.itemText}>
                    ContinuousNumber: {continuousNumber ?? 'unknown'}
                </Text>
                <View style={styles.bottomViewContainerLayout}>
                    <Button
                        style={styles.button}
                        title="take Continuous"
                        onPress={onTake}
                        disabled={isTaking}
                    />
                </View>
            </View>
            <View style={styles.contentContainer}>
                <ScrollView>
                    <InputNumber
                        title="CheckStatusCommandInterval"
                        placeHolder="Input value"
                        value={interval}
                        onChange={(value) => {
                            setInterval(value);
                        }}
                    />
                    <EnumEdit
                        title={'fileFormat'}
                        option={captureOptions?.fileFormat}
                        onChange={(fileFormat) => {
                            setCaptureOptions((prevState: any) => ({
                                ...prevState,
                                fileFormat,
                            }));
                        }}
                        optionEnum={PhotoFileFormatEnum}
                    />
                    <CaptureCommonOptionsEdit
                        onChange={(options) => {
                            setCaptureOptions((prevState: any) => ({
                                ...prevState,
                                ...options,
                            }));
                        }}
                    />
                </ScrollView>
            </View>
        </SafeAreaView>
    );
};

export default ContinuousCaptureScreen;

