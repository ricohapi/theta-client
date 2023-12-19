import React, { useState } from 'react';
import { ScrollView, Text, View } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import styles from './styles';
import { reset, restoreSettings, finishWlan } from 'theta-client-react-native';
import Button from '../../components/ui/button';
import { ItemListView, type Item } from '../../components/ui/item-list';
import type { NativeStackScreenProps } from '@react-navigation/native-stack';
import type { RootStackParamList } from '../../App';

interface CommandItem extends Item {
  value: {
    commandFunction: () => Promise<string>;
  };
}

const commandList: CommandItem[] = [
  {
    name: 'reset',
    value: {
      commandFunction: async () => {
        try {
          await reset();
        } catch (error) {
          return JSON.stringify(error);
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
          return JSON.stringify(error);
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
          return JSON.stringify(error);
        }
        return 'OK';
      },
    },
  },
];

const CommandsScreen: React.FC<
  NativeStackScreenProps<RootStackParamList, 'commands'>
> = ({ navigation }) => {
  const [selectedCommand, setSelectedCommand] = useState<CommandItem>();
  const [message, setMessage] = React.useState('');

  React.useEffect(() => {
    navigation.setOptions({ title: 'Commands' });
  }, [navigation]);

  const onSelected = (item: Item) => {
    console.log('selected: ' + item.name);
    setSelectedCommand(item);
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
    <SafeAreaView
      style={styles.safeAreaContainer}
      edges={['left', 'right', 'bottom']}
    >
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
