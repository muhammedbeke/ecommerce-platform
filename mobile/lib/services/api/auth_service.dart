import 'dart:convert';
import 'package:http/http.dart' as http;
import 'base_api_client.dart';

class AuthService {
  Future<Map<String, dynamic>> login(String email, String password) async {
    try {
      final response = await http.post(
        Uri.parse('${BaseApiClient.baseUrl}/auth/login'),
        headers: {'Content-Type': 'application/json'},
        body: jsonEncode({'email': email, 'password': password}),
      );

      final body = jsonDecode(response.body);
      if (response.statusCode == 200) {
        final data = body['data'];
        await BaseApiClient.saveToken(data['token']);
        return {
          'success': true,
          'message': body['message'] ?? 'Login successful!',
          'email': data['email'],
          'role': data['role'],
        };
      } else {
        return {'success': false, 'message': body['message'] ?? 'Login failed'};
      }
    } catch (e) {
      return {'success': false, 'message': 'An error occurred: $e'};
    }
  }

  Future<Map<String, dynamic>> register(
    String email,
    String phone,
    String password,
  ) async {
    try {
      final response = await http.post(
        Uri.parse('${BaseApiClient.baseUrl}/customers/add'),
        headers: {'Content-Type': 'application/json'},
        body: jsonEncode({
          'email': email,
          'phone': phone,
          'password': password,
        }),
      );

      final body = jsonDecode(response.body);
      if (response.statusCode == 200 || response.statusCode == 201) {
        return {
          'success': true,
          'message': body['message'] ?? 'Registration successful!',
        };
      } else {
        return {
          'success': false,
          'message': body['message'] ?? 'Registration failed',
        };
      }
    } catch (e) {
      return {'success': false, 'message': 'An error occurred: $e'};
    }
  }

  Future<Map<String, dynamic>> logout() async {
    try {
      final response = await BaseApiClient.post('/auth/logout');
      await BaseApiClient.saveToken(
        null,
      ); // Clear token locally regardless of response
      final body = jsonDecode(response.body);
      return {
        'success': response.statusCode == 200,
        'message': body['message'] ?? 'Logout completed',
      };
    } catch (e) {
      await BaseApiClient.saveToken(null);
      return {'success': true, 'message': 'Logout locally completed: $e'};
    }
  }
}
