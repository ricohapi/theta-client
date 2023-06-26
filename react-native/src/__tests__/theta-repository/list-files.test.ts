import { NativeModules } from 'react-native';
import {
  CodecEnum,
  FileInfo,
  FileTypeEnum,
  ProjectionTypeEnum,
  StorageEnum,
  ThetaFiles,
  listFiles,
} from '../../theta-repository';

describe('listFiles', () => {
  const thetaClient = NativeModules.ThetaClientReactNative;

  const storageEnumArray: [StorageEnum, string][] = [
    [StorageEnum.INTERNAL, 'INTERNAL'],
    [StorageEnum.SD, 'SD'],
    [StorageEnum.CURRENT, 'CURRENT'],
  ];

  const fileTypeEnumArray: [FileTypeEnum, string][] = [
    [FileTypeEnum.IMAGE, 'IMAGE'],
    [FileTypeEnum.VIDEO, 'VIDEO'],
    [FileTypeEnum.ALL, 'ALL'],
  ];

  const codecEnumArray: [CodecEnum, string][] = [
    [CodecEnum.H264MP4AVC, 'H264MP4AVC'],
  ];

  const projectionTypeEnumArray: [ProjectionTypeEnum, string][] = [
    [ProjectionTypeEnum.EQUIRECTANGULAR, 'EQUIRECTANGULAR'],
    [ProjectionTypeEnum.DUAL_FISHEYE, 'DUAL_FISHEYE'],
  ];

  beforeEach(() => {
    jest.clearAllMocks();
  });

  afterEach(() => {
    thetaClient.initialize = jest.fn();
  });

  function createFileInfo(no: number): FileInfo {
    return {
      name: `name${no}`,
      size: 10,
      dateTime: '2023:04:05 16:21:17',
      thumbnailUrl: 'http://192.168.1.1/files/100RICOH/R${no}.JPG',
      fileUrl: 'http://192.168.1.1/files/100RICOH/R${no}.JPG',
      storageID: '412176649172527ab3d5edabb50a7d69',
    };
  }

  function createThetaFiles(entryCount: number): ThetaFiles {
    return {
      fileList: [...Array(entryCount).keys()].map((i) => createFileInfo(i)),
      totalEntries: entryCount,
    };
  }

  test('Call listFiles', async () => {
    const fileType = FileTypeEnum.IMAGE;
    const startPosition = 0;
    const entryCount = 1;
    const storage = StorageEnum.INTERNAL;
    jest.mocked(thetaClient.listFiles).mockImplementation(
      jest.fn(async (_fileTypeEnum, _startPosition, _entryCount, _storage) => {
        expect(_fileTypeEnum).toBe(fileType);
        expect(_startPosition).toBe(startPosition);
        expect(_entryCount).toBe(entryCount);
        expect(_storage).toBe(storage);
        return createThetaFiles(_entryCount);
      })
    );

    const result = await listFiles(
      fileType,
      startPosition,
      entryCount,
      storage
    );
    expect(result.totalEntries).toBe(entryCount);
    expect(thetaClient.listFiles).toHaveBeenCalledWith(
      fileType,
      startPosition,
      entryCount,
      storage
    );
  });

  test('Call listFiles StorageEnum', async () => {
    const fileType = FileTypeEnum.IMAGE;
    const startPosition = 0;
    const entryCount = 1;

    storageEnumArray.forEach(async (item) => {
      const result = await listFiles(
        fileType,
        startPosition,
        entryCount,
        item[0]
      );
      expect(result.totalEntries).toBe(entryCount);
      expect(thetaClient.listFiles).toHaveBeenCalledWith(
        fileType,
        startPosition,
        entryCount,
        item[1]
      );
    });
  });

  test('Call listFiles FileTypeEnum', async () => {
    const startPosition = 0;
    const entryCount = 1;
    const storage = StorageEnum.INTERNAL;

    fileTypeEnumArray.forEach(async (item) => {
      const result = await listFiles(
        item[0],
        startPosition,
        entryCount,
        storage
      );
      expect(result.totalEntries).toBe(entryCount);
      expect(thetaClient.listFiles).toHaveBeenCalledWith(
        item[1],
        startPosition,
        entryCount,
        storage
      );
    });
  });

  test('FileTypeEnum length', () => {
    expect(fileTypeEnumArray.length).toBe(Object.keys(FileTypeEnum).length);
  });

  test('FileTypeEnum data', () => {
    fileTypeEnumArray.forEach((item) => {
      expect(item[0] ? item[0].toString() : '').toBe(item[1]);
    });
  });

  test('StorageEnum length', () => {
    expect(storageEnumArray.length).toBe(Object.keys(StorageEnum).length);
  });

  test('StorageEnum data', () => {
    storageEnumArray.forEach((item) => {
      expect(item[0] ? item[0].toString() : '').toBe(item[1]);
    });
  });

  test('CodecEnum data', () => {
    expect(codecEnumArray.length).toBe(Object.keys(CodecEnum).length);
    codecEnumArray.forEach((item) => {
      expect(item[0]).toBe(item[1]);
    });
  });

  test('ProjectionTypeEnum data', () => {
    expect(projectionTypeEnumArray.length).toBe(
      Object.keys(ProjectionTypeEnum).length
    );
    projectionTypeEnumArray.forEach((item) => {
      expect(item[0]).toBe(item[1]);
    });
  });
});
