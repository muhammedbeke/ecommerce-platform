import 'address.dart';

class UserProfile {
  final int id;
  final String email;
  final String phone;
  final List<Address> addresses;

  UserProfile({
    required this.id,
    required this.email,
    required this.phone,
    required this.addresses,
  });

  factory UserProfile.fromJson(Map<String, dynamic> json) {
    final List<dynamic> addressList = json['addresses'] as List<dynamic>? ?? [];
    return UserProfile(
      id: json['id'] as int? ?? 0,
      email: json['email'] as String? ?? '',
      phone: json['phone'] as String? ?? '',
      addresses: addressList
          .map((item) => Address.fromJson(item as Map<String, dynamic>))
          .toList(),
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'email': email,
      'phone': phone,
      'addresses': addresses.map((item) => item.toJson()).toList(),
    };
  }
}
