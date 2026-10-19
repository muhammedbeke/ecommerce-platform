class Address {
  final int id;
  final String title;
  final String addressLine;

  Address({
    required this.id,
    required this.title,
    required this.addressLine,
  });

  factory Address.fromJson(Map<String, dynamic> json) {
    return Address(
      id: json['id'] as int? ?? 0,
      title: json['title'] as String? ?? '',
      addressLine: json['addressLine'] as String? ?? '',
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'title': title,
      'addressLine': addressLine,
    };
  }
}
