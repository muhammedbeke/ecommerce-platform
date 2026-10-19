class ImageDto {
  final int? id;
  final String fileName;
  final String downloadUrl;

  ImageDto({
    this.id,
    required this.fileName,
    required this.downloadUrl,
  });

  factory ImageDto.fromJson(Map<String, dynamic> json) {
    return ImageDto(
      id: json['id'] as int?,
      fileName: json['fileName'] as String? ?? '',
      downloadUrl: json['downloadUrl'] as String? ?? '',
    );
  }

  Map<String, dynamic> toJson() {
    return {
      if (id != null) 'id': id,
      'fileName': fileName,
      'downloadUrl': downloadUrl,
    };
  }
}
