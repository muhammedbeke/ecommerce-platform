class Category {
  final int? id;
  final String name;
  final String? description;
  final int? parentCategoryId;

  Category({
    this.id,
    required this.name,
    this.description,
    this.parentCategoryId,
  });

  factory Category.fromJson(Map<String, dynamic> json) {
    return Category(
      id: json['id'] as int?,
      name: json['name'] as String? ?? '',
      description: json['description'] as String?,
      parentCategoryId: json['parentCategoryId'] as int?,
    );
  }

  Map<String, dynamic> toJson() {
    return {
      if (id != null) 'id': id,
      'name': name,
      if (description != null) 'description': description,
      if (parentCategoryId != null) 'parentCategoryId': parentCategoryId,
    };
  }
}
