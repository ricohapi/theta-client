import * as React from 'react';
import {
    StyleProp,
    TouchableOpacity,
    View,
    ViewStyle,
    type ButtonProps,
    Text,
    StyleSheet,
} from 'react-native';
import { type Item, ItemListPopupView } from '../../ui/item-list';

interface Props<T> extends Pick<ButtonProps, 'disabled' | 'title' | 'testID'> {
    style?: StyleProp<ViewStyle>;
    option: T;
    onChange: (option: T) => void;
    optionEnum: Record<string, T>;
}

export const EnumEdit = <T,>({
    title,
    option,
    onChange,
    optionEnum,
    disabled = false,
    style,
}: Props<T>) => {
    const [isShowList, setShowList] = React.useState<boolean>(false);

    const enumList: Item[] = [
        { name: '[undefined]', value: undefined },
        ...Object.entries(optionEnum).map((item) => {
            return { name: item[0], value: item[1] } as Item;
        }),
    ];

    const selectedItem = enumList.find((item) => item.value === option);

    return (
        <View style={style}>
            <ItemListPopupView
                visible={isShowList}
                title={title ?? ''}
                itemList={enumList}
                selectedItem={selectedItem}
                onSelected={(item) => {
                    setShowList(false);
                    onChange(item.value as T);
                }}
            />
            <View style={styles.containerLayout}>
                <View style={styles.titleBack}>
                    <Text style={styles.titleText}>{title}</Text>
                </View>
                <TouchableOpacity
                    style={styles.itemBack}
                    onPress={() => setShowList(true)}
                    disabled={disabled}
                >
                    <Text style={styles.itemText} numberOfLines={1} adjustsFontSizeToFit>
                        {selectedItem?.name || 'select value'}
                    </Text>
                </TouchableOpacity>
            </View>
        </View>
    );
};

const styles = StyleSheet.create({
    titleText: {
        color: 'black',
        fontSize: 16,
        paddingRight: 10,
    },
    itemText: {
        color: 'black',
        fontSize: 16,
    },
    containerLayout: {
        flexDirection: 'row',
        padding: 5,
        alignItems: 'center',
        justifyContent: 'flex-start',
    },
    titleBack: {
        alignItems: 'center',
        alignSelf: 'center',
    },
    itemBack: {
        alignItems: 'center',
        padding: 5,
        alignSelf: 'center',
        borderColor: 'gray',
        borderWidth: 1,
    },
});

EnumEdit.displayName = 'EnumEdit';

export default EnumEdit;
