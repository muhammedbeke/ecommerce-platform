import 'package:flutter/material.dart';
import '../models/category.dart';
import '../models/product.dart';
import '../services/api_service.dart';
import '../widgets/category_selector.dart';
import '../widgets/error_view.dart';
import '../widgets/shimmer_widgets.dart';
import '../widgets/product_card.dart';
import '../widgets/product_search_bar.dart';
import 'product_detail_screen.dart';
import 'profile/profile_screen.dart';
import 'cart/cart_screen.dart';
import '../notifiers/cart_notifier.dart';

class HomeScreen extends StatefulWidget {
  const HomeScreen({super.key});

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  final ApiService _apiService = ApiService();

  List<Category> _categories = [];
  List<Product> _products = [];
  List<Product> _filteredProducts = [];

  Category? _selectedParentCategory;
  Category? _selectedSubCategory;
  String _searchQuery = '';

  bool _isLoading = true;
  String? _errorMessage;

  @override
  void initState() {
    super.initState();
    _loadData();
  }

  Future<void> _loadData() async {
    if (_products.isEmpty) {
      setState(() {
        _isLoading = true;
        _errorMessage = null;
      });
    } else {
      setState(() {
        _errorMessage = null;
      });
    }

    try {
      final results = await Future.wait([
        _apiService.fetchCategories(),
        _apiService.fetchProducts(),
      ]);

      if (!mounted) return;

      setState(() {
        _categories = results[0] as List<Category>;
        _products = results[1] as List<Product>;
        _filterProducts();
        _isLoading = false;
      });
      CartNotifier().updateCartCount();
    } catch (e) {
      if (!mounted) return;

      setState(() {
        _errorMessage = e.toString();
        _isLoading = false;
      });
    }
  }

  void _filterProducts() {
    setState(() {
      _filteredProducts = _products.where((product) {
        if (product.stock <= 0) return false;

        bool matchesCategory = true;
        if (_selectedParentCategory != null) {
          if (_selectedSubCategory != null) {
            matchesCategory =
                product.category != null &&
                product.category!.id == _selectedSubCategory!.id;
          } else {
            final parentId = _selectedParentCategory!.id;
            final subCategoryIds = _categories
                .where((c) => c.parentCategoryId == parentId)
                .map((c) => c.id)
                .toList();

            matchesCategory =
                product.category != null &&
                (product.category!.id == parentId ||
                    subCategoryIds.contains(product.category!.id));
          }
        }

        final query = _searchQuery.toLowerCase();
        final matchesSearch =
            product.name.toLowerCase().contains(query) ||
            (product.brand != null &&
                product.brand!.toLowerCase().contains(query)) ||
            (product.category != null &&
                product.category!.name.toLowerCase().contains(query));

        return matchesCategory && matchesSearch;
      }).toList();
    });
  }

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);

    return Scaffold(
      backgroundColor: theme.colorScheme.surface,
      appBar: AppBar(
        title: const Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(
              'SmartShop E-Commerce',
              style: TextStyle(fontWeight: FontWeight.bold, fontSize: 20),
            ),
            Text(
              'Explore quality products',
              style: TextStyle(fontSize: 12, fontWeight: FontWeight.normal),
            ),
          ],
        ),
        actions: [
          IconButton(
            icon: const Icon(Icons.person_outline),
            tooltip: 'Profile',
            onPressed: () {
              Navigator.push(
                context,
                MaterialPageRoute(builder: (context) => const ProfileScreen()),
              );
            },
          ),
          ListenableBuilder(
            listenable: CartNotifier(),
            builder: (context, _) {
              final count = CartNotifier().itemCount;
              return Stack(
                children: [
                  IconButton(
                    icon: const Icon(Icons.shopping_cart_outlined),
                    tooltip: 'Cart',
                    onPressed: () {
                      Navigator.push(
                        context,
                        MaterialPageRoute(
                          builder: (context) => const CartScreen(),
                        ),
                      ).then((_) {
                        CartNotifier().updateCartCount();
                      });
                    },
                  ),
                  if (count > 0)
                    Positioned(
                      right: 8,
                      top: 8,
                      child: Container(
                        padding: const EdgeInsets.all(2),
                        decoration: const BoxDecoration(
                          color: Colors.red,
                          shape: BoxShape.circle,
                        ),
                        constraints: const BoxConstraints(
                          minWidth: 16,
                          minHeight: 16,
                        ),
                        child: Text(
                          '$count',
                          style: const TextStyle(
                            color: Colors.white,
                            fontSize: 9,
                            fontWeight: FontWeight.bold,
                          ),
                          textAlign: TextAlign.center,
                        ),
                      ),
                    ),
                ],
              );
            },
          ),
          const SizedBox(width: 8),
        ],
      ),
      body: _isLoading
          ? const ProductGridShimmer()
          : _errorMessage != null
          ? ErrorView(message: _errorMessage!, onRetry: _loadData)
          : RefreshIndicator(
              onRefresh: _loadData,
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  // Search Bar
                  ProductSearchBar(
                    query: _searchQuery,
                    onChanged: (value) {
                      setState(() {
                        _searchQuery = value;
                      });
                      _filterProducts();
                    },
                    onClear: () {
                      setState(() {
                        _searchQuery = '';
                      });
                      _filterProducts();
                    },
                  ),

                  // Categories Section Title
                  const Padding(
                    padding: EdgeInsets.symmetric(
                      horizontal: 16.0,
                      vertical: 4.0,
                    ),
                    child: Text(
                      'Categories',
                      style: TextStyle(
                        fontSize: 18,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                  ),

                  CategorySelector(
                    categories: _categories
                        .where((c) => c.parentCategoryId == null)
                        .toList(),
                    selectedCategory: _selectedParentCategory,
                    onCategorySelected: (category) {
                      setState(() {
                        _selectedParentCategory = category;
                        _selectedSubCategory =
                            null; // Reset subcategory selection
                      });
                      _filterProducts();
                    },
                  ),

                  // Subcategories Horizontal List (Only if selected parent category has subcategories)
                  if (_selectedParentCategory != null &&
                      _categories.any(
                        (c) =>
                            c.parentCategoryId == _selectedParentCategory!.id,
                      )) ...[
                    CategorySelector(
                      categories: _categories
                          .where(
                            (c) =>
                                c.parentCategoryId ==
                                _selectedParentCategory!.id,
                          )
                          .toList(),
                      selectedCategory: _selectedSubCategory,
                      onCategorySelected: (category) {
                        setState(() {
                          _selectedSubCategory = category;
                        });
                        _filterProducts();
                      },
                    ),
                  ],

                  // Products Section Title
                  Padding(
                    padding: const EdgeInsets.symmetric(
                      horizontal: 16.0,
                      vertical: 8.0,
                    ),
                    child: Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        Text(
                          _selectedSubCategory != null
                              ? _selectedSubCategory!.name
                              : (_selectedParentCategory != null
                                    ? _selectedParentCategory!.name
                                    : 'All Products'),
                          style: const TextStyle(
                            fontSize: 18,
                            fontWeight: FontWeight.bold,
                          ),
                        ),
                        Text(
                          '${_filteredProducts.length} items',
                          style: const TextStyle(
                            color: Colors.grey,
                            fontSize: 13,
                          ),
                        ),
                      ],
                    ),
                  ),

                  // Products Grid
                  Expanded(
                    child: _filteredProducts.isEmpty
                        ? LayoutBuilder(
                            builder: (context, constraints) =>
                                SingleChildScrollView(
                                  physics:
                                      const AlwaysScrollableScrollPhysics(),
                                  child: ConstrainedBox(
                                    constraints: BoxConstraints(
                                      minHeight: constraints.maxHeight,
                                    ),
                                    child: Center(
                                      child: Column(
                                        mainAxisAlignment:
                                            MainAxisAlignment.center,
                                        children: [
                                          Icon(
                                            Icons.search_off_rounded,
                                            size: 64,
                                            color: Colors.grey.shade400,
                                          ),
                                          const SizedBox(height: 16),
                                          const Text(
                                            'No products found matching the criteria.',
                                            style: TextStyle(
                                              color: Colors.grey,
                                              fontSize: 15,
                                            ),
                                          ),
                                        ],
                                      ),
                                    ),
                                  ),
                                ),
                          )
                        : GridView.builder(
                            physics: const AlwaysScrollableScrollPhysics(),
                            padding: const EdgeInsets.symmetric(
                              horizontal: 16.0,
                            ),
                            gridDelegate:
                                const SliverGridDelegateWithFixedCrossAxisCount(
                                  crossAxisCount: 2,
                                  childAspectRatio: 0.70,
                                  crossAxisSpacing: 16,
                                  mainAxisSpacing: 16,
                                ),
                            itemCount: _filteredProducts.length,
                            itemBuilder: (context, index) {
                              final product = _filteredProducts[index];
                              return ProductCard(
                                product: product,
                                onTap: () {
                                  Navigator.push(
                                    context,
                                    MaterialPageRoute(
                                      builder: (context) => ProductDetailScreen(
                                        productId: product.id!,
                                      ),
                                    ),
                                  );
                                },
                              );
                            },
                          ),
                  ),
                ],
              ),
            ),
    );
  }
}
