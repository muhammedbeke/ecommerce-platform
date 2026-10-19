import 'dart:convert';
import '../../models/product.dart';
import 'base_api_client.dart';

class ProductService {
  Future<List<Product>> fetchProducts() async {
    try {
      final response = await BaseApiClient.get('/products/all');
      if (response.statusCode == 200) {
        final body = jsonDecode(response.body);
        final List<dynamic> data = body['data'] ?? [];
        return data.map((json) => Product.fromJson(json)).toList();
      } else {
        throw Exception('Failed to load products: ${response.statusCode}');
      }
    } catch (e) {
      throw Exception('Error loading products: $e');
    }
  }

  Future<Product> fetchProductById(int id) async {
    try {
      final response = await BaseApiClient.get('/products/product/$id');
      if (response.statusCode == 200) {
        final body = jsonDecode(response.body);
        final dynamic data = body['data'];
        if (data == null) {
          throw Exception('Product data was null');
        }
        return Product.fromJson(data);
      } else {
        throw Exception(
          'Failed to load product details: ${response.statusCode}',
        );
      }
    } catch (e) {
      throw Exception('Error loading product details: $e');
    }
  }
}
