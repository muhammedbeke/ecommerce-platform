import 'dart:convert';
import '../../models/address.dart';
import 'base_api_client.dart';

class AddressService {
  Future<List<Address>> fetchMyAddresses() async {
    try {
      final response = await BaseApiClient.get('/addresses/my-addresses');

      if (response.statusCode == 200) {
        final body = jsonDecode(response.body);
        final List<dynamic> data = body['data'] ?? [];
        return data.map((json) => Address.fromJson(json)).toList();
      } else {
        return [];
      }
    } catch (e) {
      return [];
    }
  }

  Future<Map<String, dynamic>> addAddress(
    String title,
    String addressLine,
  ) async {
    try {
      final response = await BaseApiClient.post(
        '/addresses/add',
        body: {'title': title, 'addressLine': addressLine},
      );

      final body = jsonDecode(response.body);
      if (response.statusCode == 200 || response.statusCode == 201) {
        final address = Address.fromJson(body['data']);
        return {
          'success': true,
          'message': body['message'] ?? 'Address added successfully!',
          'data': address,
        };
      } else {
        return {
          'success': false,
          'message': body['message'] ?? 'Failed to add address',
        };
      }
    } catch (e) {
      return {'success': false, 'message': 'An error occurred: $e'};
    }
  }

  Future<Map<String, dynamic>> updateAddress(
    int addressId,
    String title,
    String addressLine,
  ) async {
    try {
      final response = await BaseApiClient.put(
        '/addresses/address/$addressId/update',
        body: {'title': title, 'addressLine': addressLine},
      );

      final body = jsonDecode(response.body);
      if (response.statusCode == 200) {
        final address = Address.fromJson(body['data']);
        return {
          'success': true,
          'message': body['message'] ?? 'Address updated successfully!',
          'data': address,
        };
      } else {
        return {
          'success': false,
          'message': body['message'] ?? 'Failed to update address',
        };
      }
    } catch (e) {
      return {'success': false, 'message': 'An error occurred: $e'};
    }
  }

  Future<Map<String, dynamic>> deleteAddress(int addressId) async {
    try {
      final response = await BaseApiClient.delete(
        '/addresses/address/$addressId/delete',
      );

      final body = jsonDecode(response.body);
      if (response.statusCode == 200) {
        return {
          'success': true,
          'message': body['message'] ?? 'Address deleted successfully!',
        };
      } else {
        return {
          'success': false,
          'message': body['message'] ?? 'Failed to delete address',
        };
      }
    } catch (e) {
      return {'success': false, 'message': 'An error occurred: $e'};
    }
  }
}
