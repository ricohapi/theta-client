import React, { useState } from 'react';
import { StatusBar, Text, View, ScrollView, Alert, SafeAreaView } from 'react-native';
import styles from './styles.tsx';
import {
    FileTypeEnum,
    FileInfo,
    getMetadata,
} from '../../modules/theta-client';
import Button from '../../components/ui/button';
import { ListFilesView } from '../../components/list-files-view';

interface GetMetadataScreenProps {
    navigation: {
        goBack: () => void;
        setOptions: (options: { title: string }) => void;
    };
}

const GetMetadataScreen: React.FC<GetMetadataScreenProps> = ({ navigation }) => {
    const [selectedFile, setSelectedFile] = useState<FileInfo>();
    const [message, setMessage] = React.useState('');

    React.useEffect(() => {
        navigation.setOptions({ title: 'getMetadata' });
    }, [navigation]);

    const onPress = () => {
        if (selectedFile == null) {
            return;
        }
        execGetMetadata(selectedFile.fileUrl, selectedFile.name);
    };

    const execGetMetadata = async (fileUrl: string, name: string) => {
        try {
            const result = await getMetadata(fileUrl);
            const jsonString = JSON.stringify(
                JSON.parse(JSON.stringify(result)),
                null,
                2
            );
            setMessage(`getMetadata\nname: ${name}\nmetadata:\n${jsonString}`);
        } catch (error) {
            console.log('getMetadata error: ' + JSON.stringify(error));
            setMessage('getMetadata error\n' + JSON.stringify(error));
        }
    };

    return (
        <SafeAreaView style={styles.safeAreaContainer}>
            <StatusBar barStyle="light-content" />
            <View>
                <ScrollView style={styles.messageArea}>
                    <Text style={styles.messageText}>{message}</Text>
                </ScrollView>
            </View>

            <View style={styles.buttonViewContainerLayout}>
                <Button
                    style={styles.button}
                    title="Get metadata"
                    disabled={selectedFile == null}
                    onPress={onPress}
                />
            </View>

            <ListFilesView
                onSelected={(files) => {
                    const fileInfo = files.length > 0 ? files[0] : undefined;
                    setSelectedFile(fileInfo);
                    fileInfo && setMessage('select: ' + fileInfo?.fileUrl);
                }}
                fileType={FileTypeEnum.ALL}
                onError={() => {
                    Alert.alert('listFiles', 'get error', [
                        { text: 'OK', onPress: () => navigation.goBack() },
                    ]);
                }}
            />
        </SafeAreaView>
    );
};

export default GetMetadataScreen;
