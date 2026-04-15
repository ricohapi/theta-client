import React from 'react';
import { View, Image } from 'react-native';
import styles from './styles';
import type { FileInfo } from '../../modules/theta-client';

interface FilePreviewScreenProps {
    fileInfo: FileInfo;
}

const FilePreviewScreen: React.FC<FilePreviewScreenProps> = ({ fileInfo }) => {
    return (
        <View style={styles.takePhotoBack}>
            <Image
                style={styles.takePhoto}
                source={{ uri: fileInfo.fileUrl }}
            />
        </View>
    );
};

export default FilePreviewScreen;
