import React, { useState } from 'react';
import { ScrollView, Text, View } from 'react-native';
import { getOptions, OptionNameEnum, setOptions } from '../../modules/theta-client';
import Button from '../../components/ui/button';
import { ItemListView, type Item } from '../../components/ui/item-list';
import { InputString } from '../../components/ui/input-string';
import { clientModeSession, connectClientMode } from './client-mode-session';
import styles from './sub-screen-styles';

interface Input {
    username: string;
    password: string;
}

interface CommandItem extends Item {
    value: {
        commandFunction: (input: Input) => Promise<unknown>;
    };
}

const getOption = async (name: OptionNameEnum) => await getOptions([name]);

/**
 * Set digest authentication of client mode, then reconnect with it.
 * The current connection keeps the old one and fails after the change.
 */
const setAuthentication = async (values: { username?: string; password?: string }) => {
    await setOptions(values);
    Object.assign(clientModeSession, values);
    await connectClientMode();
    return `Reconnected as ${clientModeSession.username}`;
};

// Options used in client mode. NetworkType is only gotten: the "Client Mode" menu switches it when entering and leaving.
const commandList: CommandItem[] = [
    { name: 'get username', value: { commandFunction: () => getOption(OptionNameEnum.Username) } },
    { name: 'get networkType', value: { commandFunction: () => getOption(OptionNameEnum.NetworkType) } },
    {
        name: 'get wlanFrequencyClMode',
        value: { commandFunction: () => getOption(OptionNameEnum.WlanFrequencyClMode) },
    },
    { name: 'get accessInfo', value: { commandFunction: () => getOption(OptionNameEnum.AccessInfo) } },
    {
        name: 'set username',
        value: { commandFunction: (input) => setAuthentication({ username: input.username }) },
    },
    {
        name: 'set password',
        value: { commandFunction: (input) => setAuthentication({ password: input.password }) },
    },
];

const ClientModeOptionsScreen: React.FC = () => {
    const [selectedCommand, setSelectedCommand] = useState<CommandItem>();
    const [message, setMessage] = useState('');
    const [input, setInput] = useState<Input>({
        username: clientModeSession.username,
        password: clientModeSession.password ?? '',
    });

    const onExecute = async () => {
        if (selectedCommand == null) {
            return;
        }
        setMessage('');
        try {
            const result = await selectedCommand.value.commandFunction(input);
            setMessage(`OK ${selectedCommand.name}\n${JSON.stringify(result, null, 2)}`);
        } catch (error) {
            setMessage(`${selectedCommand.name} error\n${error}`);
        }
    };

    return (
        <View style={styles.safeAreaContainer}>
            <ItemListView
                itemList={commandList}
                onSelected={(item) => setSelectedCommand(item as CommandItem)}
                selectedItem={selectedCommand}
            />
            <ScrollView style={styles.inputArea}>
                <InputString
                    style={styles.input}
                    title="username"
                    value={input.username}
                    onChange={(username) => setInput((prev) => ({ ...prev, username }))}
                />
                <InputString
                    style={styles.input}
                    title="password"
                    value={input.password}
                    onChange={(password) => setInput((prev) => ({ ...prev, password }))}
                />
            </ScrollView>
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
        </View>
    );
};

export default ClientModeOptionsScreen;
