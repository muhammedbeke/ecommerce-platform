import 'cart_item.dart';

class Cart {
  final int? id;
  final double totalAmount;
  final List<CartItem> cartItems;

  Cart({
    this.id,
    required this.totalAmount,
    required this.cartItems,
  });

  factory Cart.fromJson(Map<String, dynamic> json) {
    final List<dynamic> itemList = json['cartItems'] as List<dynamic>? ?? [];
    return Cart(
      id: json['id'] as int?,
      totalAmount: (json['totalAmount'] as num?)?.toDouble() ?? 0.0,
      cartItems: itemList
          .map((item) => CartItem.fromJson(item as Map<String, dynamic>))
          .toList(),
    );
  }

  Map<String, dynamic> toJson() {
    return {
      if (id != null) 'id': id,
      'totalAmount': totalAmount,
      'cartItems': cartItems.map((item) => item.toJson()).toList(),
    };
  }
}
