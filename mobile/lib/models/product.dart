import 'category.dart';
import 'image_dto.dart';

class Product {
  final int? id;
  final String name;
  final String? description;
  final double price;
  final int stock;
  final String? brand;
  final Category? category;
  final List<ImageDto> images;

  Product({
    this.id,
    required this.name,
    this.description,
    required this.price,
    required this.stock,
    this.brand,
    this.category,
    required this.images,
  });

  factory Product.fromJson(Map<String, dynamic> json) {
    return Product(
      id: json['id'] as int?,
      name: json['name'] as String? ?? '',
      description: json['description'] as String?,
      price: (json['price'] as num?)?.toDouble() ?? 0.0,
      stock: json['stock'] as int? ?? 0,
      brand: json['brand'] as String?,
      category: json['category'] != null
          ? Category.fromJson(json['category'] as Map<String, dynamic>)
          : null,
      images: json['images'] != null
          ? (json['images'] as List<dynamic>)
              .map((img) => ImageDto.fromJson(img as Map<String, dynamic>))
              .toList()
          : [],
    );
  }

  Map<String, dynamic> toJson() {
    return {
      if (id != null) 'id': id,
      'name': name,
      if (description != null) 'description': description,
      'price': price,
      'stock': stock,
      if (brand != null) 'brand': brand,
      if (category != null) 'category': category!.toJson(),
      'images': images.map((img) => img.toJson()).toList(),
    };
  }
}
