import 'order_item.dart';

class Order {
  final int id;
  final String orderDate;
  final String orderStatus;
  final List<OrderItem> orderDetails;

  Order({
    required this.id,
    required this.orderDate,
    required this.orderStatus,
    required this.orderDetails,
  });

  factory Order.fromJson(Map<String, dynamic> json) {
    final List<dynamic> detailsList = json['orderDetails'] as List<dynamic>? ?? [];
    return Order(
      id: json['id'] as int? ?? 0,
      orderDate: json['orderDate'] as String? ?? '',
      orderStatus: json['orderStatus'] as String? ?? 'PENDING',
      orderDetails: detailsList
          .map((item) => OrderItem.fromJson(item as Map<String, dynamic>))
          .toList(),
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'orderDate': orderDate,
      'orderStatus': orderStatus,
      'orderDetails': orderDetails.map((item) => item.toJson()).toList(),
    };
  }
}
