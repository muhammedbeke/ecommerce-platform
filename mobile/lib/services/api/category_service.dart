import 'dart:convert';
import '../../models/category.dart';
import 'base_api_client.dart';

class CategoryService {
  Future<List<Category>> fetchCategories() async {
    try {
      final response = await BaseApiClient.get('/categories/all');
      if (response.statusCode == 200) {
        final body = jsonDecode(response.body);
        final List<dynamic> data = body['data'] ?? [];
        return data.map((json) => Category.fromJson(json)).toList();
      } else {
        throw Exception('Failed to load categories: ${response.statusCode}');
      }
    } catch (e) {
      throw Exception('Error loading categories: $e');
    }
  }

  Future<List<Category>> fetchMainCategories() async {
    try {
      final response = await BaseApiClient.get('/categories/main');
      if (response.statusCode == 200) {
        final body = jsonDecode(response.body);
        final List<dynamic> data = body['data'] ?? [];
        return data.map((json) => Category.fromJson(json)).toList();
      } else {
        throw Exception(
          'Failed to load main categories: ${response.statusCode}',
        );
      }
    } catch (e) {
      throw Exception('Error loading main categories: $e');
    }
  }

  Future<List<Category>> fetchSubCategories(int parentId) async {
    try {
      final response = await BaseApiClient.get('/categories/parent/$parentId');
      if (response.statusCode == 200) {
        final body = jsonDecode(response.body);
        final List<dynamic> data = body['data'] ?? [];
        return data.map((json) => Category.fromJson(json)).toList();
      } else {
        throw Exception('Failed to load subcategories: ${response.statusCode}');
      }
    } catch (e) {
      throw Exception('Error loading subcategories: $e');
    }
  }
}
