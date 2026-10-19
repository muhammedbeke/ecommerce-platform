class OrderItem {
  final int? id;
  final int? productId;
  final String? productName;
  final int quantity;
  final double price;

  OrderItem({
    this.id,
    this.productId,
    this.productName,
    required this.quantity,
    required this.price,
  });

  factory OrderItem.fromJson(Map<String, dynamic> json) {
    return OrderItem(
      id: json['id'] as int?,
      productId: json['productId'] as int?,
      productName: json['productName'] as String?,
      quantity: (json['quantity'] as num?)?.toInt() ?? 0,
      price: (json['price'] as num?)?.toDouble() ?? 0.0,
    );
  }

  Map<String, dynamic> toJson() {
    return {
      if (id != null) 'id': id,
      if (productId != null) 'productId': productId,
      if (productName != null) 'productName': productName,
      'quantity': quantity,
      'price': price,
    };
  }
}
