/// File format used in shooting.
enum FileFormatEnum {
  /// Undefined value
  unknown('UNKNOWN'),

  /// Image File format.
  /// type: jpeg
  /// size: 2048 x 1024
  ///
  /// For RICOH THETA S or SC
  image_2K('IMAGE_2K'),

  /// Image File format.
  /// type: jpeg
  /// size: 5376 x 2688
  ///
  /// For RICOH THETA V or S or SC
  image_5K('IMAGE_5K'),

  /// Image File format.
  /// type: jpeg
  /// size: 6720 x 3360
  ///
  /// For RICOH THETA Z1
  image_6_7K('IMAGE_6_7K'),

  /// Image File format.
  /// type: raw+
  /// size: 6720 x 3360
  ///
  /// For RICOH THETA Z1
  rawP_6_7K('RAW_P_6_7K'),

  /// Image File format.
  /// type: jpeg
  /// size: 5504 x 2752
  ///
  /// For RICOH THETA X or later
  image_5_5K('IMAGE_5_5K'),

  /// Image File format.
  /// type: jpeg
  /// size: 11008 x 5504
  ///
  /// For RICOH THETA X or later
  image_11K('IMAGE_11K'),

  /// Video File format.
  /// type: mp4
  /// size: 1280 x 570
  ///
  /// For RICOH THETA S or SC
  videoHD('VIDEO_HD'),

  /// Video File format.
  /// type: mp4
  /// size: 1920 x 1080
  ///
  /// For RICOH THETA S or SC
  videoFullHD('VIDEO_FULL_HD'),

  /// Video File format.
  /// type: mp4
  /// size: 1920 x 960
  /// codec: H.264/MPEG-4 AVC
  ///
  /// For RICOH THETA Z1 or V
  video_2K('VIDEO_2K'),

  /// Video File format.
  /// type: mp4
  /// size: 1920 x 960
  ///
  /// For RICOH THETA SC2 or SC2 for business
  video_2KnoCodec('VIDEO_2K_NO_CODEC'),

  /// Video File format.
  /// type: mp4
  /// size: 3840 x 1920
  /// codec: H.264/MPEG-4 AVC
  ///
  /// For RICOH THETA Z1 or V
  video_4K('VIDEO_4K'),

  /// Video File format.
  /// type: mp4
  /// size: 3840 x 1920
  ///
  /// For RICOH THETA SC2 or SC2 for business
  video_4KnoCodec('VIDEO_4K_NO_CODEC'),

  /// Video File format.
  /// type: mp4
  /// size: 1920 x 960
  /// codec: H.264/MPEG-4 AVC
  /// frame rate: 30
  ///
  /// For RICOH THETA X or later
  video_2K_30F('VIDEO_2K_30F'),

  /// Video File format.
  /// type: mp4
  /// size: 1920 x 960
  /// codec: H.264/MPEG-4 AVC
  /// frame rate: 60
  ///
  /// For RICOH THETA X or later
  video_2K_60F('VIDEO_2K_60F'),

  /// Video File format.
  ///
  /// type: mp4
  /// size: 2752 x 2752
  /// codec: H.264/MPEG-4 AVC
  /// frame rate: 2
  ///
  /// RICOH THETA X firmware v2.50.2 or later.
  /// This mode outputs two fisheye video for each lens.
  /// The MP4 file name ending with _0 is the video file on the front lens, and _1 is back lens.
  video_2_7K_2752_2F('VIDEO_2_7K_2752_2F'),

  /// Video File format.
  ///
  /// type: mp4
  /// size: 2752 x 2752
  /// codec: H.264/MPEG-4 AVC
  /// frame rate: 5
  ///
  /// RICOH THETA X firmware v2.50.2 or later.
  /// This mode outputs two fisheye video for each lens.
  /// The MP4 file name ending with _0 is the video file on the front lens, and _1 is back lens.
  video_2_7K_2752_5F('VIDEO_2_7K_2752_5F'),

  /// Video File format.
  ///
  /// type: mp4
  /// size: 2752 x 2752
  /// codec: H.264/MPEG-4 AVC
  /// frame rate: 10
  ///
  /// RICOH THETA X firmware v2.50.2 or later.
  /// This mode outputs two fisheye video for each lens.
  /// The MP4 file name ending with _0 is the video file on the front lens, and _1 is back lens.
  video_2_7K_2752_10F('VIDEO_2_7K_2752_10F'),

  /// Video File format.
  ///
  /// type: mp4
  /// size: 2752 x 2752
  /// codec: H.264/MPEG-4 AVC
  /// frame rate: 30
  ///
  /// RICOH THETA X firmware v2.50.2 or later.
  /// This mode outputs two fisheye video for each lens.
  /// The MP4 file name ending with _0 is the video file on the front lens, and _1 is back lens.
  video_2_7K_2752_30F('VIDEO_2_7K_2752_30F'),

  /// Video File format.
  /// type: mp4
  /// size: 2688 x 2688
  /// codec: H.264/MPEG-4 AVC
  /// frame rate: 1
  ///
  /// For RICOH THETA Z1 firmware v3.01.1 or later.
  /// This mode outputs two fisheye video for each lens.
  /// The MP4 file name ending with _0 is the video file on the front lens,
  /// and _1 is back lens. This mode does not record audio track to MP4 file.
  video_2_7K_1F('VIDEO_2_7K_1F'),

  /// Video File format.
  /// type: mp4
  /// size: 2688 x 2688
  /// codec: H.264/MPEG-4 AVC
  /// frame rate: 2
  ///
  /// For RICOH THETA Z1 firmware v3.01.1 or later.
  /// This mode outputs two fisheye video for each lens.
  /// The MP4 file name ending with _0 is the video file on the front lens,
  /// and _1 is back lens. This mode does not record audio track to MP4 file.
  video_2_7K_2F('VIDEO_2_7K_2F'),

  /// Video File format.
  /// type: mp4
  /// size: 3648 x 3648
  /// codec: H.264/MPEG-4 AVC
  /// frame rate: 1
  ///
  /// For RICOH THETA Z1 firmware v3.01.1 or later.
  /// This mode outputs two fisheye video for each lens.
  /// The MP4 file name ending with _0 is the video file on the front lens,
  /// and _1 is back lens. This mode does not record audio track to MP4 file.
  video_3_6K_1F('VIDEO_3_6K_1F'),

  /// Video File format.
  /// type: mp4
  /// size: 3648 x 3648
  /// codec: H.264/MPEG-4 AVC
  /// frame rate: 2
  ///
  /// For RICOH THETA Z1 firmware v3.01.1 or later.
  /// This mode outputs two fisheye video for each lens.
  /// The MP4 file name ending with _0 is the video file on the front lens,
  /// and _1 is back lens. This mode does not record audio track to MP4 file.
  video_3_6K_2F('VIDEO_3_6K_2F'),

  /// Video File format.
  /// type: mp4
  /// size: 3840 x 1920
  /// codec: H.264/MPEG-4 AVC
  /// frame rate: 10
  ///
  /// For RICOH THETA X or later
  video_4K_10F('VIDEO_4K_10F'),

  /// Video File format.
  /// type: mp4
  /// size: 3840 x 1920
  /// codec: H.264/MPEG-4 AVC
  /// frame rate: 15
  ///
  /// For RICOH THETA X or later
  video_4K_15F('VIDEO_4K_15F'),

  /// Video File format.
  /// type: mp4
  /// size: 3840 x 1920
  /// codec: H.264/MPEG-4 AVC
  /// frame rate: 30
  ///
  /// For RICOH THETA X or later
  video_4K_30F('VIDEO_4K_30F'),

  /// Video File format.
  /// type: mp4
  /// size: 3840 x 1920
  /// codec: H.264/MPEG-4 AVC
  /// frame rate: 60
  ///
  /// For RICOH THETA X or later
  video_4K_60F('VIDEO_4K_60F'),

  /// Video File format.
  /// type: mp4
  /// size: 5760 x 2880
  /// codec: H.264/MPEG-4 AVC
  /// frame rate: 2
  ///
  /// For RICOH THETA X or later
  video_5_7K_2F('VIDEO_5_7K_2F'),

  /// Video File format.
  /// type: mp4
  /// size: 5760 x 2880
  /// codec: H.264/MPEG-4 AVC
  /// frame rate: 5
  ///
  /// For RICOH THETA X or later
  video_5_7K_5F('VIDEO_5_7K_5F'),

  /// Video File format.
  /// type: mp4
  /// size: 5760 x 2880
  /// codec: H.264/MPEG-4 AVC
  /// frame rate: 10
  ///
  /// For RICOH THETA X or later
  video_5_7K_10F('VIDEO_5_7K_10F'),

  /// Video File format.
  /// type: mp4
  /// size: 5760 x 2880
  /// codec: H.264/MPEG-4 AVC
  /// frame rate: 15
  ///
  /// For RICOH THETA X or later
  video_5_7K_15F('VIDEO_5_7K_15F'),

  /// Video File format.
  /// type: mp4
  /// size: 5760 x 2880
  /// codec: H.264/MPEG-4 AVC
  /// frame rate: 30
  ///
  /// For RICOH THETA X or later
  video_5_7K_30F('VIDEO_5_7K_30F'),

  /// Video File format.
  /// type: mp4
  /// size: 7680 x 3840
  /// codec: H.264/MPEG-4 AVC
  /// frame rate: 2
  ///
  /// For RICOH THETA X or later
  video_7K_2F('VIDEO_7K_2F'),

  /// Video File format.
  /// type: mp4
  /// size: 7680 x 3840
  /// codec: H.264/MPEG-4 AVC
  /// frame rate: 5
  ///
  /// For RICOH THETA X or later
  video_7K_5F('VIDEO_7K_5F'),

  /// Video File format.
  /// type: mp4
  /// size: 7680 x 3840
  /// codec: H.264/MPEG-4 AVC
  /// frame rate: 10
  ///
  /// For RICOH THETA X or later
  video_7K_10F('VIDEO_7K_10F');

  final String rawValue;

  const FileFormatEnum(this.rawValue);

  @override
  String toString() {
    return rawValue;
  }

  static FileFormatEnum? getValue(String rawValue) {
    return FileFormatEnum.values.cast<FileFormatEnum?>().firstWhere(
        (element) => element?.rawValue == rawValue,
        orElse: () => null);
  }
}
