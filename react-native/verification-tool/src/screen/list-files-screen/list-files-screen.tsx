import React, { useEffect } from 'react';
import { Text, View, ScrollView, Alert, SafeAreaView, TouchableWithoutFeedback, Keyboard } from 'react-native';
import styles from './styles.tsx';
import { FileTypeEnum, StorageEnum } from '../../modules/theta-client';
import Button from '../../components/ui/button';
import { ListFilesView } from '../../components/list-files-view';
import { ItemSelectorView } from '../../components/ui/item-list';
import { InputNumber } from '../../components/ui/input-number';

interface ListFilesScreenProps {
    navigation: {
        setOptions: (options: { title: string }) => void;
        navigate: (screen: string, params?: any) => void;
    };
    state: {
        fileType: any;
        startPosition: number;
        entryCount: number;
        storage: any;
        message: string;
        selectedFileInfo: any;
        listFilesProps: any;
        refreshCounter: number;
    };
    setState: (state: any) => void;
}

const ListFilesScreen: React.FC<ListFilesScreenProps> = ({ navigation, state, setState }) => {

    const fileTypeList = [
        { name: 'ALL', value: FileTypeEnum.ALL },
        { name: 'IMAGE', value: FileTypeEnum.IMAGE },
        { name: 'VIDEO', value: FileTypeEnum.VIDEO },
    ];

    const storageList = [
        { name: '[undefined]', value: undefined },
        { name: 'INTERNAL', value: StorageEnum.INTERNAL },
        { name: 'SD', value: StorageEnum.SD },
        { name: 'CURRENT', value: StorageEnum.CURRENT },
    ];

    useEffect(() => {
        navigation.setOptions({ title: 'listFiles' });
    }, [navigation]);

    const showList = () => {
        const newListFilesProps = {
            fileType: state.fileType || FileTypeEnum.ALL,
            startPosition: state.startPosition,
            entryCount: state.entryCount,
            storage: state.storage,
        };
        setState({
            ...state,
            listFilesProps: newListFilesProps,
            refreshCounter: state.refreshCounter + 1,
            selectedFileInfo: null,
            message: '',
        });
    };

    const showPhoto = () => {
        if (state.selectedFileInfo) {
            navigation.navigate('filePreview', { fileInfo: state.selectedFileInfo });
        } else {
            Alert.alert('No file selected', 'Please select a file first', [{ text: 'OK' }]);
        }
    };

    return (
        <SafeAreaView style={styles.safeAreaContainer}>
            <TouchableWithoutFeedback onPress={Keyboard.dismiss}>
                <View style={styles.contentContainer}>
                    <View>
                        <ScrollView style={styles.messageArea}>
                            <Text style={styles.messageText}>{state.message}</Text>
                        </ScrollView>
                    </View>

                    <ItemSelectorView
                        itemList={fileTypeList}
                        title={'fileType'}
                        onSelected={(item) => {
                            setState({ ...state, fileType: item.value });
                        }}
                        selectedItem={fileTypeList.find((item) => item.value === (state.fileType || FileTypeEnum.ALL))}
                    />

                    <InputNumber
                        title={'startPosition'}
                        onChange={(newValue) => setState({ ...state, startPosition: newValue ?? 0 })}
                        value={state.startPosition}
                    />

                    <InputNumber
                        title={'entryCount'}
                        onChange={(newValue) => {
                            if (newValue != null) {
                                setState({ ...state, entryCount: newValue });
                            }
                        }}
                        value={state.entryCount}
                    />

                    <ItemSelectorView
                        itemList={storageList}
                        title={'storage'}
                        onSelected={(item) => {
                            setState({ ...state, storage: item.value });
                        }}
                        selectedItem={storageList.find((item) => item.value === state.storage)}
                    />

                    <View style={styles.buttonViewContainerLayout}>
                        <Button style={styles.button} title="SHOW LIST" onPress={showList} />
                        <Button style={styles.button} title="SHOW PHOTO" onPress={showPhoto} disabled={!state.selectedFileInfo} />
                    </View>

                    {state.listFilesProps && (
                        <ListFilesView
                            startPosition={state.listFilesProps.startPosition}
                            entryCount={state.listFilesProps.entryCount}
                            fileType={state.listFilesProps.fileType}
                            storage={state.listFilesProps.storage}
                            selectedFiles={state.selectedFileInfo ? [state.selectedFileInfo] : []}
                            onSelected={(files) => {
                                const fileInfo = files.length > 0 ? files[0] : undefined;
                                console.log('onSelected:' + fileInfo?.fileUrl);
                                if (fileInfo != null) {
                                    const jsonString = JSON.stringify(
                                        JSON.parse(JSON.stringify(fileInfo)),
                                        null,
                                        2
                                    );
                                    setState({ ...state, selectedFileInfo: fileInfo, message: 'select:\n' + jsonString });
                                }
                            }}
                            onError={(error) => {
                                Alert.alert('listFiles', 'get error\n' + JSON.stringify(error), [
                                    {
                                        text: 'OK',
                                        onPress: () => {
                                            setState({ ...state, listFilesProps: undefined });
                                        },
                                    },
                                ]);
                            }}
                            refreshCounter={state.refreshCounter}
                            onRefreshed={(thetaFiles) => {
                                if (thetaFiles != null) {
                                    const strInfo = `listFiles\ntotalEntries: ${thetaFiles.totalEntries} entryCount: ${thetaFiles.fileList.length}`;
                                    if (!state.selectedFileInfo) {
                                        setState({ ...state, message: strInfo });
                                    }
                                } else {
                                    if (!state.selectedFileInfo) {
                                        setState({ ...state, message: '' });
                                    }
                                }
                            }}
                        />
                    )}
                </View>
            </TouchableWithoutFeedback>
        </SafeAreaView>
    );
};

export default ListFilesScreen;
