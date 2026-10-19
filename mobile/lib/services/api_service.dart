import 'package:flutter/material.dart';
import '../models/category.dart';
import '../models/product.dart';
import '../models/address.dart';
import '../models/cart.dart';
import '../models/order.dart';
import '../models/user_profile.dart';
import 'api/base_api_client.dart';
import 'api/auth_service.dart';
import 'api/category_service.dart';
import 'api/product_service.dart';
import 'api/customer_service.dart';
import 'api/address_service.dart';
import 'api/cart_service.dart';
import 'api/order_service.dart';

class ApiService {
  static const String baseUrl = BaseApiClient.baseUrl;

  static GlobalKey<NavigatorState> get navigatorKey =>
      BaseApiClient.navigatorKey;

  static String? get token => BaseApiClient.token;
  static set token(String? value) => BaseApiClient.token = value;

  static Future<void> saveToken(String? tokenValue) =>
      BaseApiClient.saveToken(tokenValue);

  static bool isTokenExpired(String token) =>
      BaseApiClient.isTokenExpired(token);

  static String getFullImageUrl(String relativeUrl) =>
      BaseApiClient.getFullImageUrl(relativeUrl);

  final CategoryService _categoryService = CategoryService();
  final ProductService _productService = ProductService();
  final AuthService _authService = AuthService();
  final CustomerService _customerService = CustomerService();
  final AddressService _addressService = AddressService();
  final CartService _cartService = CartService();
  final OrderService _orderService = OrderService();

  Future<List<Category>> fetchCategories() =>
      _categoryService.fetchCategories();

  Future<List<Category>> fetchMainCategories() =>
      _categoryService.fetchMainCategories();

  Future<List<Category>> fetchSubCategories(int parentId) =>
      _categoryService.fetchSubCategories(parentId);

  Future<List<Product>> fetchProducts() => _productService.fetchProducts();

  Future<Product> fetchProductById(int id) =>
      _productService.fetchProductById(id);

  Future<Map<String, dynamic>> login(String email, String password) =>
      _authService.login(email, password);

  Future<Map<String, dynamic>> register(
    String email,
    String phone,
    String password,
  ) => _authService.register(email, phone, password);

  Future<Map<String, dynamic>> fetchProfile() async {
    final result = await _customerService.fetchProfile();
    if (result['success'] == true) {
      final userProfile = result['data'] as UserProfile;
      return {'success': true, 'data': userProfile.toJson()};
    }
    return result;
  }

  Future<Map<String, dynamic>> updateProfile(
    int customerId,
    String email,
    String phone, {
    String? password,
    String? oldPassword,
  }) async {
    final result = await _customerService.updateProfile(
      customerId,
      email,
      phone,
      password: password,
      oldPassword: oldPassword,
    );
    if (result['success'] == true) {
      final userProfile = result['data'] as UserProfile;
      return {
        'success': true,
        'message': result['message'],
        'data': userProfile.toJson(),
      };
    }
    return result;
  }

  Future<Map<String, dynamic>> deleteAccount(int customerId) =>
      _customerService.deleteAccount(customerId);

  Future<List<dynamic>> fetchMyAddresses() async {
    final list = await _addressService.fetchMyAddresses();
    return list.map((addr) => addr.toJson()).toList();
  }

  Future<Map<String, dynamic>> addAddress(
    String title,
    String addressLine,
  ) async {
    final result = await _addressService.addAddress(title, addressLine);
    if (result['success'] == true) {
      final address = result['data'] as Address;
      return {
        'success': true,
        'message': result['message'],
        'data': address.toJson(),
      };
    }
    return result;
  }

  Future<Map<String, dynamic>> updateAddress(
    int addressId,
    String title,
    String addressLine,
  ) async {
    final result = await _addressService.updateAddress(
      addressId,
      title,
      addressLine,
    );
    if (result['success'] == true) {
      final address = result['data'] as Address;
      return {
        'success': true,
        'message': result['message'],
        'data': address.toJson(),
      };
    }
    return result;
  }

  Future<Map<String, dynamic>> deleteAddress(int addressId) =>
      _addressService.deleteAddress(addressId);

  Future<Map<String, dynamic>> fetchMyOrders() async {
    final result = await _orderService.fetchMyOrders();
    if (result['success'] == true) {
      final List<Order> orders = result['data'] as List<Order>;
      return {'success': true, 'data': orders.map((o) => o.toJson()).toList()};
    }
    return result;
  }

  Future<Map<String, dynamic>> fetchCart() async {
    final result = await _cartService.fetchCart();
    if (result['success'] == true) {
      final cart = result['data'] as Cart;
      return {'success': true, 'data': cart.toJson()};
    }
    return result;
  }

  Future<Map<String, dynamic>> addToCart(int productId, int quantity) async {
    final result = await _cartService.addToCart(productId, quantity);
    if (result['success'] == true) {
      final cart = result['data'] as Cart;
      return {'success': true, 'data': cart.toJson()};
    }
    return result;
  }

  Future<Map<String, dynamic>> updateCartItemQuantity(
    int productId,
    int quantity,
  ) async {
    final result = await _cartService.updateCartItemQuantity(
      productId,
      quantity,
    );
    if (result['success'] == true) {
      final cart = result['data'] as Cart;
      return {'success': true, 'data': cart.toJson()};
    }
    return result;
  }

  Future<Map<String, dynamic>> removeFromCart(int productId) async {
    final result = await _cartService.removeFromCart(productId);
    if (result['success'] == true) {
      final cart = result['data'] as Cart;
      return {'success': true, 'data': cart.toJson()};
    }
    return result;
  }

  Future<Map<String, dynamic>> clearCart() async {
    final result = await _cartService.clearCart();
    if (result['success'] == true) {
      final cart = result['data'] as Cart;
      return {'success': true, 'data': cart.toJson()};
    }
    return result;
  }

  Future<Map<String, dynamic>> placeOrder(List<Map<String, dynamic>> items) =>
      _orderService.placeOrder(items);

  Future<Map<String, dynamic>> cancelOrder(int orderId) =>
      _orderService.cancelOrder(orderId);

  Future<Map<String, dynamic>> logout() => _authService.logout();
}
