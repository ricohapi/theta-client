import React, { useState } from 'react';
import { ScrollView, Text, View } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import styles from './styles';
import {
  getPluginOrders,
  getThetaInfo,
  getThetaState,
  listAccessPoints,
  listPlugins,
} from 'theta-client-react-native';
import Button from '../../components/ui/button';
import { ItemListView, type Item } from '../../components/ui/item-list';
import type { NativeStackScreenProps } from '@react-navigation/native-stack';
import type { RootStackParamList } from '../../App';

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
          return JSON.stringify(error);
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
          return JSON.stringify(error);
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
          return JSON.stringify(error);
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
          return JSON.stringify(error);
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
          return JSON.stringify(error);
        }
      },
    },
  },
];

const GetInfoScreen: React.FC<
  NativeStackScreenProps<RootStackParamList, 'getInfo'>
> = ({ navigation }) => {
  const [selectedCommand, setSelectedCommand] = useState<CommandItem>();
  const [message, setMessage] = React.useState('');

  React.useEffect(() => {
    navigation.setOptions({ title: 'Get Info' });
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

export default GetInfoScreen;
