import 'dart:convert';
import '../../models/cart.dart';
import 'base_api_client.dart';

class CartService {
  Future<Map<String, dynamic>> fetchCart() async {
    try {
      final response = await BaseApiClient.get('/carts/my-cart');
      final body = jsonDecode(response.body);
      if (response.statusCode == 200) {
        final cart = Cart.fromJson(body['data']);
        return {'success': true, 'data': cart};
      } else {
        return {
          'success': false,
          'message': body['message'] ?? 'Failed to retrieve cart.',
        };
      }
    } catch (e) {
      return {'success': false, 'message': 'Network error: $e'};
    }
  }

  Future<Map<String, dynamic>> addToCart(int productId, int quantity) async {
    try {
      final response = await BaseApiClient.post(
        '/carts/add?productId=$productId&quantity=$quantity',
      );

      final body = jsonDecode(response.body);
      if (response.statusCode == 200) {
        final cart = Cart.fromJson(body['data']);
        return {'success': true, 'data': cart};
      } else {
        return {
          'success': false,
          'message': body['message'] ?? 'Failed to add product to cart.',
        };
      }
    } catch (e) {
      return {'success': false, 'message': 'Network error: $e'};
    }
  }

  Future<Map<String, dynamic>> updateCartItemQuantity(
    int productId,
    int quantity,
  ) async {
    try {
      final response = await BaseApiClient.put(
        '/carts/update?productId=$productId&quantity=$quantity',
      );

      final body = jsonDecode(response.body);
      if (response.statusCode == 200) {
        final cart = Cart.fromJson(body['data']);
        return {'success': true, 'data': cart};
      } else {
        return {
          'success': false,
          'message': body['message'] ?? 'Failed to update cart item.',
        };
      }
    } catch (e) {
      return {'success': false, 'message': 'Network error: $e'};
    }
  }

  Future<Map<String, dynamic>> removeFromCart(int productId) async {
    try {
      final response = await BaseApiClient.delete(
        '/carts/remove/product?productId=$productId',
      );

      final body = jsonDecode(response.body);
      if (response.statusCode == 200) {
        final cart = Cart.fromJson(body['data']);
        return {'success': true, 'data': cart};
      } else {
        return {
          'success': false,
          'message': body['message'] ?? 'Failed to remove product from cart.',
        };
      }
    } catch (e) {
      return {'success': false, 'message': 'Network error: $e'};
    }
  }

  Future<Map<String, dynamic>> clearCart() async {
    try {
      final response = await BaseApiClient.delete('/carts/clear');

      final body = jsonDecode(response.body);
      if (response.statusCode == 200) {
        final cart = Cart.fromJson(body['data']);
        return {'success': true, 'data': cart};
      } else {
        return {
          'success': false,
          'message': body['message'] ?? 'Failed to clear cart.',
        };
      }
    } catch (e) {
      return {'success': false, 'message': 'Network error: $e'};
    }
  }
}
