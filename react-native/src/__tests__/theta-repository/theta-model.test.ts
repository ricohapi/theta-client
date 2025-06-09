import { NativeModules } from 'react-native';
import { ThetaModel, getThetaModel } from '../../theta-repository';

describe('getThetaModel', () => {
  const thetaClient = NativeModules.ThetaClientReactNative;

  const thetaModelArray: [ThetaModel, string][] = [
    [ThetaModel.THETA_S, 'THETA_S'],
    [ThetaModel.THETA_SC, 'THETA_SC'],
    [ThetaModel.THETA_V, 'THETA_V'],
    [ThetaModel.THETA_Z1, 'THETA_Z1'],
    [ThetaModel.THETA_X, 'THETA_X'],
    [ThetaModel.THETA_SC2, 'THETA_SC2'],
    [ThetaModel.THETA_SC2_B, 'THETA_SC2_B'],
    [ThetaModel.THETA_A1, 'THETA_A1'],
  ];

  beforeEach(() => {
    jest.clearAllMocks();
  });

  afterEach(() => {
    thetaClient.getThetaModel = jest.fn();
    jest.clearAllMocks();
  });

  test('ThetaModel enum', () => {
    expect(thetaModelArray.length).toBe(Object.keys(ThetaModel).length);
    thetaModelArray.forEach((item) => {
      expect(item[0].toString()).toBe(item[1]);
    });
  });

  test('getThetaModel normal', async () => {
    jest.mocked(thetaClient.getThetaModel).mockImplementation(
      jest.fn(async () => {
        return 'THETA_Z1';
      })
    );

    const model = await getThetaModel();
    expect(model).toBe(ThetaModel.THETA_Z1);
  });

  test.each([...thetaModelArray, [undefined, null], [undefined, undefined]])(
    'getThetaModel all model',
    async (thetaModel, modelName) => {
      jest.mocked(thetaClient.getThetaModel).mockImplementation(
        jest.fn(async () => {
          return modelName;
        })
      );

      const model = await getThetaModel();
      expect(model).toBe(thetaModel);
    }
  );
});
