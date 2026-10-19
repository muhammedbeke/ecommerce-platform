import 'package:flutter/material.dart';
import '../../../models/address.dart';
import '../../../services/api/address_service.dart';
import '../../../widgets/address_dialog.dart';

class AddressSelectionSheet extends StatefulWidget {
  final List<Address> initialAddresses;

  const AddressSelectionSheet({
    super.key,
    required this.initialAddresses,
  });

  @override
  State<AddressSelectionSheet> createState() => _AddressSelectionSheetState();
}

class _AddressSelectionSheetState extends State<AddressSelectionSheet> {
  final AddressService _addressService = AddressService();
  late final List<Address> _localAddresses;
  Address? _currentSelection;
  bool _isLoading = false;

  @override
  void initState() {
    super.initState();
    _localAddresses = List.from(widget.initialAddresses);
    if (_localAddresses.isNotEmpty) {
      _currentSelection = _localAddresses.first;
    }
  }

  Future<void> _handleAddNewAddress() async {
    final result = await showDialog<Map<String, String>>(
      context: context,
      builder: (context) => const AddressDialog(),
    );

    if (result != null && mounted) {
      setState(() {
        _isLoading = true;
      });

      final apiResult = await _addressService.addAddress(
        result['title']!,
        result['addressLine']!,
      );

      if (mounted) {
        setState(() {
          _isLoading = false;
        });

        if (apiResult['success'] == true) {
          final newAddress = apiResult['data'] as Address;
          setState(() {
            _localAddresses.add(newAddress);
            _currentSelection = newAddress;
          });
        } else {
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(
              content: Text(apiResult['message'] ?? 'Failed to add address.'),
              backgroundColor: Colors.red,
            ),
          );
        }
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    final isDark = theme.brightness == Brightness.dark;

    return Container(
      padding: const EdgeInsets.all(24.0),
      decoration: BoxDecoration(
        color: theme.colorScheme.surface,
        borderRadius: const BorderRadius.vertical(top: Radius.circular(24)),
      ),
      child: _isLoading
          ? const SizedBox(
              height: 200,
              child: Center(
                child: CircularProgressIndicator(),
              ),
            )
          : Column(
              mainAxisSize: MainAxisSize.min,
              crossAxisAlignment: CrossAxisAlignment.stretch,
              children: [
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    const Text(
                      'Select Delivery Address',
                      style: TextStyle(
                        fontSize: 18,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                    IconButton(
                      icon: const Icon(Icons.add_circle_outline),
                      tooltip: 'Add New Address',
                      onPressed: _handleAddNewAddress,
                    ),
                  ],
                ),
                const SizedBox(height: 16),
                if (_localAddresses.isEmpty)
                  const SizedBox(
                    height: 150,
                    child: Center(
                      child: Text(
                        'No addresses saved. Tap + to add one.',
                        style: TextStyle(color: Colors.grey),
                      ),
                    ),
                  )
                else
                  Flexible(
                    child: ListView.builder(
                      shrinkWrap: true,
                      itemCount: _localAddresses.length,
                      itemBuilder: (context, index) {
                        final address = _localAddresses[index];
                        final isSelected = _currentSelection?.id == address.id;
                        return Card(
                          margin: const EdgeInsets.only(bottom: 8),
                          shape: RoundedRectangleBorder(
                            borderRadius: BorderRadius.circular(12),
                            side: BorderSide(
                              color: isSelected
                                  ? theme.colorScheme.primary
                                  : Colors.transparent,
                              width: 1.5,
                            ),
                          ),
                          color: isDark ? const Color(0xFF1E293B) : Colors.white,
                          child: InkWell(
                            borderRadius: BorderRadius.circular(12),
                            onTap: () {
                              setState(() {
                                _currentSelection = address;
                              });
                            },
                            child: Padding(
                              padding: const EdgeInsets.all(16.0),
                              child: Row(
                                children: [
                                  Icon(
                                    isSelected
                                        ? Icons.radio_button_checked
                                        : Icons.radio_button_off,
                                    color: isSelected
                                        ? theme.colorScheme.primary
                                        : theme.colorScheme.onSurface
                                            .withValues(alpha: 0.6),
                                  ),
                                  const SizedBox(width: 16),
                                  Expanded(
                                    child: Column(
                                      crossAxisAlignment:
                                          CrossAxisAlignment.start,
                                      children: [
                                        Text(
                                          address.title,
                                          style: const TextStyle(
                                            fontWeight: FontWeight.bold,
                                            fontSize: 16,
                                          ),
                                        ),
                                        const SizedBox(height: 4),
                                        Text(
                                          address.addressLine,
                                          style: TextStyle(
                                            fontSize: 14,
                                            color: theme.colorScheme.onSurface
                                                .withValues(alpha: 0.8),
                                          ),
                                        ),
                                      ],
                                    ),
                                  ),
                                ],
                              ),
                            ),
                          ),
                        );
                      },
                    ),
                  ),
                const SizedBox(height: 16),
                ElevatedButton(
                  onPressed: _currentSelection == null
                      ? null
                      : () => Navigator.pop(context, _currentSelection),
                  style: ElevatedButton.styleFrom(
                    padding: const EdgeInsets.symmetric(vertical: 16),
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(12),
                    ),
                  ),
                  child: const Text(
                    'DELIVER HERE & PLACE ORDER',
                    style: TextStyle(fontWeight: FontWeight.bold),
                  ),
                ),
              ],
            ),
    );
  }
}
