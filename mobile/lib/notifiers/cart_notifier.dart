import 'package:flutter/material.dart';
import '../services/api_service.dart';

class CartNotifier extends ChangeNotifier {
  static final CartNotifier _instance = CartNotifier._internal();
  factory CartNotifier() => _instance;
  CartNotifier._internal();

  final ApiService _apiService = ApiService();
  int _itemCount = 0;
  int get itemCount => _itemCount;

  Future<void> updateCartCount() async {
    final result = await _apiService.fetchCart();
    if (result['success'] == true) {
      final cartData = result['data'];
      final items = cartData['cartItems'] as List<dynamic>? ?? [];
      int count = 0;
      for (final item in items) {
        count += (item['quantity'] as num?)?.toInt() ?? 0;
      }
      _itemCount = count;
      notifyListeners();
    }
  }

  void setCartCount(int count) {
    _itemCount = count;
    notifyListeners();
  }
}
