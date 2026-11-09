import React, { useState } from 'react';
import { ScrollView, Text, View } from 'react-native';
import {
    AuthModeEnum,
    deleteAccessPoint,
    listAccessPoints,
    setAccessPointConnectionPriority,
    setAccessPointDynamically,
    setAccessPointStatically,
} from '../../modules/theta-client';
import Button from '../../components/ui/button';
import { ItemListView, type Item } from '../../components/ui/item-list';
import { InputString } from '../../components/ui/input-string';
import { InputNumber } from '../../components/ui/input-number';
import { TitledSwitch } from '../../components/ui/titled-switch';
import clientModeConfig from '../../client-mode-config';
import { clientModeSession } from './client-mode-session';
import styles from './sub-screen-styles';

interface Input {
    ssid: string;
    password: string;
    authMode: string;
    connectionPriority?: number;
    ssidStealth: boolean;
    ipAddress: string;
    subnetMask: string;
    defaultGateway: string;
}

interface CommandItem extends Item {
    value: {
        commandFunction: (input: Input) => Promise<unknown>;
    };
}

const toAuthMode = (value: string): AuthModeEnum => {
    if (!Object.values(AuthModeEnum).includes(value as AuthModeEnum)) {
        throw new Error(`authMode must be one of ${Object.values(AuthModeEnum).join(', ')}`);
    }
    return value as AuthModeEnum;
};

const commandList: CommandItem[] = [
    {
        name: 'listAccessPoints',
        value: { commandFunction: () => listAccessPoints() },
    },
    {
        name: 'setAccessPointDynamically',
        value: {
            commandFunction: (input) =>
                setAccessPointDynamically(input.ssid, {
                    ssidStealth: input.ssidStealth,
                    authMode: toAuthMode(input.authMode),
                    password: input.password || undefined,
                    connectionPriority: input.connectionPriority,
                }),
        },
    },
    {
        name: 'setAccessPointStatically',
        value: {
            commandFunction: (input) =>
                setAccessPointStatically(
                    input.ssid,
                    input.ipAddress,
                    input.subnetMask,
                    input.defaultGateway,
                    {
                        ssidStealth: input.ssidStealth,
                        authMode: toAuthMode(input.authMode),
                        password: input.password || undefined,
                        connectionPriority: input.connectionPriority,
                    }
                ),
        },
    },
    {
        name: 'setAccessPointConnectionPriority',
        value: {
            commandFunction: (input) =>
                setAccessPointConnectionPriority(
                    input.ssid,
                    input.connectionPriority ?? 1,
                    input.ssidStealth
                ),
        },
    },
    {
        name: 'deleteAccessPoint',
        value: { commandFunction: (input) => deleteAccessPoint(input.ssid) },
    },
];

/**
 * Access points used in client mode, operated on the client mode connection.
 * Changing or deleting the access point THETA is connected to may disconnect THETA.
 */
const ClientModeAccessPointsScreen: React.FC = () => {
    const [selectedCommand, setSelectedCommand] = useState<CommandItem>();
    const [message, setMessage] = useState('');
    const [input, setInput] = useState<Input>({
        ssid: clientModeSession.ssid,
        password: clientModeConfig.password,
        authMode: clientModeConfig.authMode,
        connectionPriority: 1,
        ssidStealth: false,
        ipAddress: '',
        subnetMask: '255.255.255.0',
        defaultGateway: '',
    });
    const update = (values: Partial<Input>) => setInput((prev) => ({ ...prev, ...values }));

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
                <InputString style={styles.input} title="ssid" value={input.ssid} onChange={(ssid) => update({ ssid })} />
                <InputString style={styles.input} title="password" value={input.password} onChange={(password) => update({ password })} />
                <InputString style={styles.input} title="authMode" value={input.authMode} onChange={(authMode) => update({ authMode })} />
                <InputNumber
                    style={styles.input}
                    title="connectionPriority"
                    value={input.connectionPriority}
                    onChange={(connectionPriority) => update({ connectionPriority })}
                />
                <TitledSwitch
                    style={styles.input}
                    title="ssidStealth"
                    value={input.ssidStealth}
                    onChange={(ssidStealth) => update({ ssidStealth })}
                />
                <InputString style={styles.input} title="ipAddress (static)" value={input.ipAddress} onChange={(ipAddress) => update({ ipAddress })} />
                <InputString style={styles.input} title="subnetMask (static)" value={input.subnetMask} onChange={(subnetMask) => update({ subnetMask })} />
                <InputString
                    style={styles.input}
                    title="defaultGateway (static)"
                    value={input.defaultGateway}
                    onChange={(defaultGateway) => update({ defaultGateway })}
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

export default ClientModeAccessPointsScreen;
