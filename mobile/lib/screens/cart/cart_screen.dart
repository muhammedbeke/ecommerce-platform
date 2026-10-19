import 'package:flutter/material.dart';
import '../../models/cart.dart';
import '../../models/address.dart';
import '../../notifiers/cart_notifier.dart';
import '../../services/api/cart_service.dart';
import '../../services/api/address_service.dart';
import '../../services/api/order_service.dart';
import '../../widgets/address_dialog.dart';
import '../../widgets/error_view.dart';
import '../../widgets/shimmer_widgets.dart';
import '../orders_screen.dart';
import 'widgets/address_selection_sheet.dart';
import 'widgets/cart_item_card.dart';

class CartScreen extends StatefulWidget {
  const CartScreen({super.key});

  @override
  State<CartScreen> createState() => _CartScreenState();
}

class _CartScreenState extends State<CartScreen> {
  final CartService _cartService = CartService();
  final AddressService _addressService = AddressService();
  final OrderService _orderService = OrderService();
  final CartNotifier _cartNotifier = CartNotifier();

  bool _isLoading = true;
  bool _isProcessingCheckout = false;
  String? _errorMessage;
  Cart? _cart;

  @override
  void initState() {
    super.initState();
    _loadCart();
  }

  Future<void> _loadCart() async {
    setState(() {
      _isLoading = true;
      _errorMessage = null;
    });

    final result = await _cartService.fetchCart();

    if (mounted) {
      if (result['success'] == true) {
        setState(() {
          _cart = result['data'] as Cart;
          _isLoading = false;
        });
        // Update global cart badge notifier
        int totalQty = 0;
        for (final item in _cart!.cartItems) {
          totalQty += item.quantity;
        }
        _cartNotifier.setCartCount(totalQty);
      } else {
        setState(() {
          _errorMessage = result['message'];
          _isLoading = false;
        });
      }
    }
  }

  Future<void> _updateQuantity(int productId, int currentQty, int delta) async {
    final newQty = currentQty + delta;
    if (newQty <= 0) {
      _removeItem(productId);
      return;
    }

    setState(() {
      _isLoading = true;
    });

    final result = await _cartService.updateCartItemQuantity(productId, newQty);

    if (mounted) {
      if (result['success'] == true) {
        _loadCart();
      } else {
        setState(() {
          _isLoading = false;
        });
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(
            content: Text(result['message'] ?? 'Failed to update quantity.'),
            backgroundColor: Colors.red,
          ),
        );
      }
    }
  }

  Future<void> _removeItem(int productId) async {
    setState(() {
      _isLoading = true;
    });

    final result = await _cartService.removeFromCart(productId);

    if (mounted) {
      if (result['success'] == true) {
        _loadCart();
      } else {
        setState(() {
          _isLoading = false;
        });
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(
            content: Text(result['message'] ?? 'Failed to remove product.'),
            backgroundColor: Colors.red,
          ),
        );
      }
    }
  }

  Future<void> _selectAddressAndCheckout() async {
    if (_cart == null || _cart!.cartItems.isEmpty) return;

    setState(() {
      _isProcessingCheckout = true;
    });

    final List<Address> addresses = await _addressService.fetchMyAddresses();

    if (!mounted) return;

    if (addresses.isEmpty) {
      setState(() {
        _isProcessingCheckout = false;
      });

      // Prompt to add address
      final addAddressConfirm = await showDialog<bool>(
        context: context,
        builder: (context) => AlertDialog(
          title: const Text('No Delivery Address'),
          content: const Text(
            'You must add a delivery address before placing an order. Would you like to add one now?',
          ),
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(20),
          ),
          actions: [
            TextButton(
              onPressed: () => Navigator.pop(context, false),
              child: const Text('Cancel'),
            ),
            ElevatedButton(
              onPressed: () => Navigator.pop(context, true),
              child: const Text('Add Address'),
            ),
          ],
        ),
      );

      if (addAddressConfirm == true && mounted) {
        final newAddressMap = await showDialog<Map<String, String>>(
          context: context,
          builder: (context) => const AddressDialog(),
        );

        if (newAddressMap != null && mounted) {
          setState(() {
            _isLoading = true;
          });
          final result = await _addressService.addAddress(
            newAddressMap['title']!,
            newAddressMap['addressLine']!,
          );
          if (mounted) {
            setState(() {
              _isLoading = false;
            });
            if (result['success'] == true) {
              // Retry selection and checkout after adding address
              _selectAddressAndCheckout();
            } else {
              ScaffoldMessenger.of(context).showSnackBar(
                SnackBar(
                  content: Text(result['message'] ?? 'Failed to add address.'),
                  backgroundColor: Colors.red,
                ),
              );
            }
          }
        }
      }
      return;
    }

    setState(() {
      _isProcessingCheckout = false;
    });

    final selectedAddress = await showModalBottomSheet<Address>(
      context: context,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(24)),
      ),
      builder: (context) => AddressSelectionSheet(initialAddresses: addresses),
    );

    if (selectedAddress != null) {
      _checkout();
    }
  }

  Future<void> _checkout() async {
    if (_cart == null || _cart!.cartItems.isEmpty) return;

    setState(() {
      _isProcessingCheckout = true;
    });

    final orderItems = _cart!.cartItems.map((item) {
      return {'productId': item.productId, 'quantity': item.quantity};
    }).toList();

    final result = await _orderService.placeOrder(orderItems);

    if (!mounted) return;

    if (result['success'] == true) {
      await _cartService.clearCart();
      _cartNotifier.setCartCount(0);

      if (!mounted) return;

      setState(() {
        _isProcessingCheckout = false;
        _cart = null;
      });

      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
          content: Text('Order placed successfully!'),
          backgroundColor: Colors.green,
          duration: Duration(seconds: 3),
        ),
      );

      Navigator.pushReplacement(
        context,
        MaterialPageRoute(builder: (context) => const OrdersScreen()),
      );
    } else {
      setState(() {
        _isProcessingCheckout = false;
      });
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          content: Text(result['message'] ?? 'Checkout failed.'),
          backgroundColor: Colors.red,
        ),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);

    if (_isLoading && _cart == null) {
      return Scaffold(
        appBar: AppBar(title: const Text('My Cart')),
        body: const CartShimmer(),
      );
    }

    if (_errorMessage != null) {
      return Scaffold(
        appBar: AppBar(title: const Text('My Cart')),
        body: ErrorView(message: _errorMessage!, onRetry: _loadCart),
      );
    }

    final cartItems = _cart?.cartItems ?? [];

    return Scaffold(
      backgroundColor: theme.colorScheme.surface,
      appBar: AppBar(
        title: const Text(
          'My Cart',
          style: TextStyle(fontWeight: FontWeight.bold),
        ),
        actions: [
          if (cartItems.isNotEmpty)
            IconButton(
              icon: const Icon(Icons.delete_sweep_outlined),
              tooltip: 'Clear Cart',
              onPressed: () async {
                final confirm = await showDialog<bool>(
                  context: context,
                  builder: (context) => AlertDialog(
                    title: const Text('Clear Cart'),
                    content: const Text(
                      'Are you sure you want to remove all items from your cart?',
                    ),
                    actions: [
                      TextButton(
                        onPressed: () => Navigator.pop(context, false),
                        child: const Text('Cancel'),
                      ),
                      ElevatedButton(
                        onPressed: () => Navigator.pop(context, true),
                        child: const Text('Clear'),
                      ),
                    ],
                  ),
                );
                if (confirm == true && mounted) {
                  setState(() {
                    _isLoading = true;
                  });
                  final result = await _cartService.clearCart();
                  if (mounted) {
                    if (result['success'] == true) {
                      _loadCart();
                    } else {
                      setState(() {
                        _isLoading = false;
                      });
                    }
                  }
                }
              },
            ),
        ],
      ),
      body: cartItems.isEmpty
          ? Center(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Icon(
                    Icons.shopping_cart_outlined,
                    size: 64,
                    color: Colors.grey.shade400,
                  ),
                  const SizedBox(height: 16),
                  const Text(
                    'Your cart is empty',
                    style: TextStyle(color: Colors.grey, fontSize: 16),
                  ),
                  const SizedBox(height: 24),
                  ElevatedButton(
                    onPressed: () => Navigator.pop(context),
                    style: ElevatedButton.styleFrom(
                      padding: const EdgeInsets.symmetric(
                        horizontal: 24,
                        vertical: 12,
                      ),
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(12),
                      ),
                    ),
                    child: const Text('Start Shopping'),
                  ),
                ],
              ),
            )
          : Column(
              children: [
                Expanded(
                  child: RefreshIndicator(
                    onRefresh: _loadCart,
                    child: ListView.builder(
                      padding: const EdgeInsets.all(16.0),
                      itemCount: cartItems.length,
                      itemBuilder: (context, index) {
                        final item = cartItems[index];
                        return CartItemCard(
                          item: item,
                          onRemove: _removeItem,
                          onUpdateQuantity: _updateQuantity,
                        );
                      },
                    ),
                  ),
                ),
                Container(
                  padding: const EdgeInsets.all(20.0),
                  decoration: BoxDecoration(
                    color: theme.cardColor,
                    boxShadow: [
                      BoxShadow(
                        color: Colors.black.withValues(alpha: 0.05),
                        blurRadius: 10,
                        offset: const Offset(0, -5),
                      ),
                    ],
                  ),
                  child: SafeArea(
                    top: false,
                    child: Column(
                      mainAxisSize: MainAxisSize.min,
                      crossAxisAlignment: CrossAxisAlignment.stretch,
                      children: [
                        Row(
                          mainAxisAlignment: MainAxisAlignment.spaceBetween,
                          children: [
                            const Text(
                              'Order Total:',
                              style: TextStyle(
                                fontSize: 16,
                                fontWeight: FontWeight.bold,
                              ),
                            ),
                            Text(
                              '\$${(_cart?.totalAmount ?? 0.0).toStringAsFixed(2)}',
                              style: TextStyle(
                                fontSize: 20,
                                fontWeight: FontWeight.bold,
                                color: theme.colorScheme.primary,
                              ),
                            ),
                          ],
                        ),
                        const SizedBox(height: 16),
                        ElevatedButton(
                          onPressed: _isProcessingCheckout
                              ? null
                              : _selectAddressAndCheckout,
                          style: ElevatedButton.styleFrom(
                            padding: const EdgeInsets.symmetric(vertical: 16),
                            shape: RoundedRectangleBorder(
                              borderRadius: BorderRadius.circular(12),
                            ),
                          ),
                          child: _isProcessingCheckout
                              ? const SizedBox(
                                  height: 20,
                                  width: 20,
                                  child: CircularProgressIndicator(
                                    strokeWidth: 2,
                                    valueColor: AlwaysStoppedAnimation(
                                      Colors.white,
                                    ),
                                  ),
                                )
                              : const Text(
                                  'PLACE ORDER',
                                  style: TextStyle(
                                    fontWeight: FontWeight.bold,
                                    fontSize: 16,
                                  ),
                                ),
                        ),
                      ],
                    ),
                  ),
                ),
              ],
            ),
    );
  }
}
