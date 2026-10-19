import 'dart:convert';
import '../../models/user_profile.dart';
import 'base_api_client.dart';

class CustomerService {
  Future<Map<String, dynamic>> fetchProfile() async {
    try {
      final response = await BaseApiClient.get('/customers/customer/me');
      final body = jsonDecode(response.body);
      if (response.statusCode == 200) {
        final profile = UserProfile.fromJson(body['data']);
        return {'success': true, 'data': profile};
      } else {
        return {
          'success': false,
          'message': body['message'] ?? 'Failed to load profile',
        };
      }
    } catch (e) {
      return {'success': false, 'message': 'An error occurred: $e'};
    }
  }

  Future<Map<String, dynamic>> updateProfile(
    int customerId,
    String email,
    String phone, {
    String? password,
    String? oldPassword,
  }) async {
    try {
      final response = await BaseApiClient.put(
        '/customers/customer/$customerId/update',
        body: {
          'email': email,
          'phone': phone,
          if (password != null && password.isNotEmpty) 'password': password,
          if (oldPassword != null && oldPassword.isNotEmpty)
            'oldPassword': oldPassword,
        },
      );

      final body = jsonDecode(response.body);
      if (response.statusCode == 200) {
        final profile = UserProfile.fromJson(body['data']);
        return {
          'success': true,
          'message': body['message'] ?? 'Profile updated successfully!',
          'data': profile,
        };
      } else {
        return {
          'success': false,
          'message': body['message'] ?? 'Update failed',
        };
      }
    } catch (e) {
      return {'success': false, 'message': 'An error occurred: $e'};
    }
  }

  Future<Map<String, dynamic>> deleteAccount(int customerId) async {
    try {
      final response = await BaseApiClient.delete(
        '/customers/customer/$customerId/delete',
      );

      final body = jsonDecode(response.body);
      if (response.statusCode == 200) {
        await BaseApiClient.saveToken(null);
        return {
          'success': true,
          'message': body['message'] ?? 'Account deleted successfully!',
        };
      } else {
        return {
          'success': false,
          'message': body['message'] ?? 'Deletion failed',
        };
      }
    } catch (e) {
      return {'success': false, 'message': 'An error occurred: $e'};
    }
  }
}
