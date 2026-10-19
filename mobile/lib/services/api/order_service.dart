import 'dart:convert';
import '../../models/order.dart';
import 'base_api_client.dart';

class OrderService {
  Future<Map<String, dynamic>> fetchMyOrders() async {
    try {
      final response = await BaseApiClient.get('/orders/my-orders');

      final body = jsonDecode(response.body);
      if (response.statusCode == 200) {
        final List<dynamic> data = body['data'] ?? [];
        final orders = data.map((json) => Order.fromJson(json)).toList();
        return {'success': true, 'data': orders};
      } else {
        return {
          'success': false,
          'message': body['message'] ?? 'Failed to retrieve orders.',
        };
      }
    } catch (e) {
      return {'success': false, 'message': 'Network error: $e'};
    }
  }

  Future<Map<String, dynamic>> placeOrder(
    List<Map<String, dynamic>> items,
  ) async {
    try {
      final response = await BaseApiClient.post(
        '/orders/add',
        body: {'items': items},
      );

      final body = jsonDecode(response.body);
      if (response.statusCode == 201 || response.statusCode == 200) {
        return {'success': true, 'data': body['data']};
      } else {
        return {
          'success': false,
          'message': body['message'] ?? 'Failed to place order.',
        };
      }
    } catch (e) {
      return {'success': false, 'message': 'Network error: $e'};
    }
  }

  Future<Map<String, dynamic>> cancelOrder(int orderId) async {
    try {
      final response = await BaseApiClient.put('/orders/order/$orderId/cancel');

      final body = jsonDecode(response.body);
      if (response.statusCode == 200) {
        return {
          'success': true,
          'message': body['message'] ?? 'Order cancelled successfully!',
          'data': body['data'],
        };
      } else {
        return {
          'success': false,
          'message': body['message'] ?? 'Failed to cancel order.',
        };
      }
    } catch (e) {
      return {'success': false, 'message': 'Network error: $e'};
    }
  }
}
