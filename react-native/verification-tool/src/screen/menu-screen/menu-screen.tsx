import React from 'react';
import { StatusBar, View, Alert, ScrollView, Text } from 'react-native';
import { getThetaModel, initialize } from '../../modules/theta-client';
import Button from '../../components/ui/button';
import { ItemListPopupView, type Item } from '../../components/ui/item-list';
import styles from './styles.tsx';

interface CaptureItem extends Item {
    value: {
        selectedFunction: () => void;
    };
}

interface FunctionItem extends Item {
    value: {
        selectedFunction: () => void;
    };
}

interface MenuScreenProps {
    navigation: {
        navigate: (screen: string, params?: any) => void;
    };
    message: string;
    onChangeMessage: (message: string) => void;
}

const MenuScreen: React.FC<MenuScreenProps> = ({
    navigation,
    message,
    onChangeMessage,
}) => {
    const [isShowCaptureList, setShowCaptureList] =
        React.useState<boolean>(false);

    const captureList: CaptureItem[] = [
        {
            name: '・photo capture',
            value: {
                selectedFunction: async () => {
                    navigation.navigate('photoCapture');
                },
            },
        },
        {
            name: '・video capture',
            value: {
                selectedFunction: async () => {
                    navigation.navigate('videoCapture');
                },
            },
        },
        {
            name: '・time-shift capture',
            value: {
                selectedFunction: async () => {
                    navigation.navigate('timeShiftCapture');
                },
            },
        },
        {
            name: '・time-shift manual capture',
            value: {
                selectedFunction: async () => {
                    navigation.navigate('timeShiftManualCapture');
                },
            },
        },
        {
            name: '・limitless interval capture',
            value: {
                selectedFunction: async () => {
                    navigation.navigate('limitlessIntervalCapture');
                },
            },
        },
        {
            name: '・interval shooting with the shot count specified',
            value: {
                selectedFunction: async () => {
                    navigation.navigate('shotCountSpecifiedIntervalCapture');
                },
            },
        },
        {
            name: '・interval composite shooting',
            value: {
                selectedFunction: async () => {
                    navigation.navigate('compositeIntervalCapture');
                },
            },
        },
        {
            name: '・burst shooting',
            value: {
                selectedFunction: async () => {
                    navigation.navigate('burstCapture');
                },
            },
        },
        {
            name: '・multi bracket shooting',
            value: {
                selectedFunction: async () => {
                    navigation.navigate('multiBracketCapture');
                },
            },
        },
        {
            name: '・continuous shooting',
            value: {
                selectedFunction: async () => {
                    navigation.navigate('continuousCapture');
                },
            },
        },
    ];

    const functionList: FunctionItem[] = [
        {
            name: 'Get Info',
            value: {
                selectedFunction: () => navigation.navigate('getInfo'),
            },
        },
        {
            name: 'listFiles',
            value: {
                selectedFunction: () => navigation.navigate('listFiles', { reset: true }),
            },
        },
        {
            name: 'DeleteFiles',
            value: {
                selectedFunction: () => navigation.navigate('deleteFiles'),
            },
        },
        {
            name: 'getMetadata',
            value: {
                selectedFunction: () => navigation.navigate('getMetadata'),
            },
        },
        {
            name: 'Options',
            value: {
                selectedFunction: () => navigation.navigate('options'),
            },
        },
        {
            name: 'Live preview',
            value: {
                selectedFunction: () => navigation.navigate('livePreview'),
            },
        },
        {
            name: 'Video Convert',
            value: {
                selectedFunction: () => navigation.navigate('videoConvert'),
            },
        },
        {
            name: 'Commands',
            value: {
                selectedFunction: () => navigation.navigate('commands'),
            },
        },
        {
            name: 'Capture',
            value: {
                selectedFunction: () => setShowCaptureList(true),
            },
        },
    ];

    const functionItems = functionList.map((item) => (
        <View key={item.name}>
            <View style={styles.spacer} />
            <Button title={item.name} onPress={() => item.value.selectedFunction()} />
        </View>
    ));

    const initTheta = async () => {
        try {
            const endpoint = 'http://192.168.1.1';
            await initialize(endpoint);
            const model = await getThetaModel();
            console.log('Connected.');
            onChangeMessage(`Connected.\nModel: ${model}`);
        } catch (error) {
            console.log('Connect error: ' + error);
            onChangeMessage('Connect error: ' + error);
            Alert.alert('initialize', 'error');
        }
    };

    const onPressConnect = () => {
        initTheta();
    };

    const onSelectedCapture = (captureItem: CaptureItem) => {
        setShowCaptureList(false);
        captureItem.value.selectedFunction();
    };

    return (
        <View style={styles.safeAreaContainer}>
            <StatusBar barStyle="light-content" />
            <ItemListPopupView
                visible={isShowCaptureList}
                title={'Capture'}
                itemList={captureList}
                onSelected={onSelectedCapture}
            />
            <View style={styles.messageLayout}>
                <ScrollView style={styles.messageArea}>
                    <Text style={styles.messageText}>{message}</Text>
                </ScrollView>
            </View>
            <Button title="connect" onPress={onPressConnect} />
            <ScrollView style={styles.container}>{functionItems}</ScrollView>
        </View>
    );
};

export default MenuScreen;
