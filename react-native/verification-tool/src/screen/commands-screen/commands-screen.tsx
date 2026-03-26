import React, { useState } from 'react';
import { ScrollView, Text, View, SafeAreaView } from 'react-native';
import styles from './styles.tsx';
import {
    reset,
    restoreSettings,
    finishWlan,
    reboot,
} from '../../modules/theta-client';
import Button from '../../components/ui/button';
import { ItemListView, type Item } from '../../components/ui/item-list';

interface CommandsScreenProps {
    navigation: {
        setOptions: (options: { title: string }) => void;
    };
}

interface CommandItem extends Item {
    value: {
        commandFunction: () => Promise<string>;
    };
}

const commandList: CommandItem[] = [
    {
        name: 'reboot',
        value: {
            commandFunction: async () => {
                try {
                    await reboot();
                } catch (error) {
                    console.log('reboot error', error);
                    return JSON.stringify(error, null, 2);
                }
                return 'OK';
            },
        },
    },
    {
        name: 'reset',
        value: {
            commandFunction: async () => {
                try {
                    await reset();
                } catch (error) {
                    console.log('reset error', error);
                    return JSON.stringify(error, null, 2);
                }
                return 'OK';
            },
        },
    },
    {
        name: 'restoreSettings',
        value: {
            commandFunction: async () => {
                try {
                    await restoreSettings();
                } catch (error) {
                    console.log('restoreSettings error', error);
                    return JSON.stringify(error, null, 2);
                }
                return 'OK';
            },
        },
    },
    {
        name: 'finishWlan',
        value: {
            commandFunction: async () => {
                try {
                    await finishWlan();
                } catch (error) {
                    console.log('finishWlan error', error);
                    return JSON.stringify(error, null, 2);
                }
                return 'OK';
            },
        },
    },
];

const CommandsScreen: React.FC<CommandsScreenProps> = ({ navigation }) => {
    const [selectedCommand, setSelectedCommand] = useState<CommandItem>();
    const [message, setMessage] = React.useState('');

    React.useEffect(() => {
        navigation.setOptions({ title: 'Commands' });
    }, [navigation]);

    const onSelected = (item: Item) => {
        console.log('selected: ' + item.name);
        setSelectedCommand(item as CommandItem);
        setMessage('');
    };

    const onExecute = () => {
        if (selectedCommand == null) {
            return;
        }
        selectedCommand.value.commandFunction().then((result) => {
            setMessage(result);
        });
    };

    return (
        <SafeAreaView style={styles.safeAreaContainer}>
            <ItemListView
                itemList={commandList}
                onSelected={onSelected}
                selectedItem={selectedCommand}
            />
            <View style={styles.bottomViewContainer}>
                <View style={styles.bottomViewContainerLayout}>
                    <Button
                        style={styles.button}
                        title="Execute"
                        disabled={selectedCommand == null}
                        onPress={onExecute}
                    />
                </View>
            </View>
            <ScrollView style={styles.messageArea}>
                <Text style={styles.messageText}>{message}</Text>
            </ScrollView>
        </SafeAreaView>
    );
};

export default CommandsScreen;
