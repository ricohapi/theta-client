
import React, { useState } from 'react';
import { ScrollView, Text, View } from 'react-native';
import {
    getPluginLicense,
    getPluginOrders,
    getThetaInfo,
    getThetaLicense,
    getThetaState,
    listAccessPoints,
    listPlugins,
} from '../../modules/theta-client';
import Button from '../../components/ui/button';
import { ItemListView, type Item } from '../../components/ui/item-list';
import styles from './styles';

interface CommandItem extends Item {
    value: {
        commandFunction: () => Promise<string>;
    };
}

function getJsonString(object: Object) {
    return JSON.stringify(JSON.parse(JSON.stringify(object)), null, 2);
}

const commandList: CommandItem[] = [
    {
        name: 'getThetaInfo',
        value: {
            commandFunction: async () => {
                try {
                    const result = await getThetaInfo();
                    return `OK getThetaInfo()\n${getJsonString(result)}`;
                } catch (error) {
                    return error instanceof Error
                        ? error.toString()
                        : 'An error occurred.';
                }
            },
        },
    },
    {
        name: 'getThetaLicense',
        value: {
            commandFunction: async () => {
                try {
                    const result = await getThetaLicense();
                    const maxChars = 200;
                    return (
                        `OK getThetaLicense()\nlength:${result.length}\n\n${result.slice(
                            0,
                            maxChars
                        )}\n\n` +
                        (result.length > maxChars
                            ? `(The remaining ${result.length - maxChars} characters were omitted.)`
                            : '')
                    );
                } catch (error) {
                    return error instanceof Error
                        ? error.toString()
                        : 'An error occurred.';
                }
            },
        },
    },
    {
        name: 'getThetaState',
        value: {
            commandFunction: async () => {
                try {
                    const result = await getThetaState();
                    return `OK getThetaState()\n${getJsonString(result)}`;
                } catch (error) {
                    return error instanceof Error
                        ? error.toString()
                        : 'An error occurred.';
                }
            },
        },
    },
    {
        name: 'listAccessPoints',
        value: {
            commandFunction: async () => {
                try {
                    const result = await listAccessPoints();
                    return `OK listAccessPoints()\n${getJsonString(result)}`;
                } catch (error) {
                    return error instanceof Error
                        ? error.toString()
                        : 'An error occurred.';
                }
            },
        },
    },
    {
        name: 'listPlugins',
        value: {
            commandFunction: async () => {
                try {
                    const result = await listPlugins();
                    return `OK listPlugins()\n${getJsonString(result)}`;
                } catch (error) {
                    return error instanceof Error
                        ? error.toString()
                        : 'An error occurred.';
                }
            },
        },
    },
    {
        name: 'getPluginOrders',
        value: {
            commandFunction: async () => {
                try {
                    const result = await getPluginOrders();
                    return `OK getPluginOrders()\n${getJsonString(result)}`;
                } catch (error) {
                    return error instanceof Error
                        ? error.toString()
                        : 'An error occurred.';
                }
            },
        },
    },
    {
        // This screen has no input, so get the licenses of the installed plugins listed by listPlugins().
        name: 'getPluginLicense',
        value: {
            commandFunction: async () => {
                try {
                    const plugins = await listPlugins();
                    if (plugins.length === 0) {
                        return 'No plugins installed.';
                    }
                    const maxChars = 200;
                    let text = '';
                    for (const plugin of plugins) {
                        // Some plugins (e.g. pre-installed ones) may have no license, so show the result of each one.
                        try {
                            const result = await getPluginLicense(plugin.packageName);
                            text +=
                                `OK getPluginLicense(${plugin.packageName})\nlength:${result.length}\n\n` +
                                `${result.slice(0, maxChars)}\n\n` +
                                (result.length > maxChars
                                    ? `(The remaining ${result.length - maxChars} characters were omitted.)\n\n`
                                    : '');
                        } catch (error) {
                            text += `NG getPluginLicense(${plugin.packageName}) isPreInstalled:${plugin.isPreInstalled}\n${error}\n\n`;
                        }
                    }
                    return text;
                } catch (error) {
                    return error instanceof Error
                        ? error.toString()
                        : 'An error occurred.';
                }
            },
        },
    },
];

const GetInfoScreen: React.FC = () => {
    const [selectedCommand, setSelectedCommand] = useState<CommandItem>();
    const [message, setMessage] = useState('');

    const onSelected = (item: Item) => {
        setSelectedCommand(item as CommandItem);
        setMessage('');
    };

    const onExecute = () => {
        if (!selectedCommand) { return; }
        selectedCommand.value.commandFunction().then((result) => {
            setMessage(result);
        });
    };

    return (
        <View style={styles.safeAreaContainer}>
            <View style={styles.commandListContainer}>
                <ItemListView
                    itemList={commandList}
                    onSelected={onSelected}
                    selectedItem={selectedCommand}
                />
            </View>
            <View style={styles.buttonViewContainer}>
                <View style={styles.buttonViewContainerLayout}>
                    <Button
                        style={styles.button}
                        title="Execute"
                        disabled={!selectedCommand}
                        onPress={onExecute}
                    />
                </View>
            </View>
            <ScrollView style={styles.messageArea}>
                <Text style={styles.messageText}>{message}</Text>
            </ScrollView>
        </View>
    );
};

export default GetInfoScreen;
