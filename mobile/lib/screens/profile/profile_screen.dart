import 'package:flutter/material.dart';
import '../../models/address.dart';
import '../../models/user_profile.dart';
import '../../services/api/customer_service.dart';
import '../../services/api/address_service.dart';
import '../../services/api/auth_service.dart';
import '../../widgets/address_dialog.dart';
import '../../widgets/error_view.dart';
import '../../widgets/shimmer_widgets.dart';
import '../auth/login_screen.dart';
import '../orders_screen.dart';
import 'widgets/profile_details_tab.dart';
import 'widgets/profile_addresses_tab.dart';
import 'widgets/profile_security_tab.dart';

class ProfileScreen extends StatefulWidget {
  const ProfileScreen({super.key});

  @override
  State<ProfileScreen> createState() => _ProfileScreenState();
}

class _ProfileScreenState extends State<ProfileScreen> {
  final CustomerService _customerService = CustomerService();
  final AddressService _addressService = AddressService();
  final AuthService _authService = AuthService();

  UserProfile? _profile;
  bool _isLoading = true;
  bool _isSaving = false;
  String? _errorMessage;

  @override
  void initState() {
    super.initState();
    _loadProfile();
  }

  Future<void> _loadProfile() async {
    setState(() {
      _isLoading = true;
      _errorMessage = null;
    });

    final result = await _customerService.fetchProfile();

    if (mounted) {
      if (result['success'] == true) {
        setState(() {
          _profile = result['data'] as UserProfile;
          _isLoading = false;
        });
      } else {
        setState(() {
          _errorMessage = result['message'];
          _isLoading = false;
        });
      }
    }
  }

  Future<void> _handleUpdateProfile(String email, String phone) async {
    if (_profile == null) return;

    setState(() {
      _isSaving = true;
    });

    final result = await _customerService.updateProfile(_profile!.id, email, phone);

    setState(() {
      _isSaving = false;
    });

    if (mounted) {
      if (result['success'] == true) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(
            content: Text(result['message']),
            backgroundColor: Colors.green,
            behavior: SnackBarBehavior.floating,
          ),
        );
        _loadProfile();
      } else {
        _showErrorDialog(result['message']);
      }
    }
  }

  Future<void> _handleUpdatePassword(String currentPassword, String newPassword) async {
    if (_profile == null) return;

    setState(() {
      _isSaving = true;
    });

    final result = await _customerService.updateProfile(
      _profile!.id,
      _profile!.email,
      _profile!.phone,
      password: newPassword,
      oldPassword: currentPassword,
    );

    setState(() {
      _isSaving = false;
    });

    if (mounted) {
      if (result['success'] == true) {
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(
            content: Text('Password updated successfully!'),
            backgroundColor: Colors.green,
            behavior: SnackBarBehavior.floating,
          ),
        );
        _loadProfile();
      } else {
        _showErrorDialog(result['message']);
      }
    }
  }

  Future<void> _handleAddAddress() async {
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
        if (apiResult['success'] == true) {
          ScaffoldMessenger.of(context).showSnackBar(
            const SnackBar(
              content: Text('Address added successfully!'),
              backgroundColor: Colors.green,
            ),
          );
          _loadProfile();
        } else {
          _showErrorDialog(apiResult['message']);
          setState(() {
            _isLoading = false;
          });
        }
      }
    }
  }

  Future<void> _handleEditAddress(Address address) async {
    final result = await showDialog<Map<String, String>>(
      context: context,
      builder: (context) => AddressDialog(
        initialTitle: address.title,
        initialAddressLine: address.addressLine,
      ),
    );

    if (result != null && mounted) {
      setState(() {
        _isLoading = true;
      });

      final apiResult = await _addressService.updateAddress(
        address.id,
        result['title']!,
        result['addressLine']!,
      );

      if (mounted) {
        if (apiResult['success'] == true) {
          ScaffoldMessenger.of(context).showSnackBar(
            const SnackBar(
              content: Text('Address updated successfully!'),
              backgroundColor: Colors.green,
            ),
          );
          _loadProfile();
        } else {
          _showErrorDialog(apiResult['message']);
          setState(() {
            _isLoading = false;
          });
        }
      }
    }
  }

  Future<void> _handleDeleteAddress(Address address) async {
    final confirm = await showDialog<bool>(
      context: context,
      builder: (context) => AlertDialog(
        title: const Text('Delete Address'),
        content: const Text('Are you sure you want to delete this address?'),
        actions: [
          TextButton(
            onPressed: () => Navigator.pop(context, false),
            child: const Text('Cancel'),
          ),
          ElevatedButton(
            onPressed: () => Navigator.pop(context, true),
            style: ElevatedButton.styleFrom(backgroundColor: Colors.red),
            child: const Text('Delete'),
          ),
        ],
      ),
    );

    if (confirm == true && mounted) {
      setState(() {
        _isLoading = true;
      });

      final apiResult = await _addressService.deleteAddress(address.id);

      if (mounted) {
        if (apiResult['success'] == true) {
          ScaffoldMessenger.of(context).showSnackBar(
            const SnackBar(
              content: Text('Address deleted successfully!'),
              backgroundColor: Colors.green,
            ),
          );
          _loadProfile();
        } else {
          _showErrorDialog(apiResult['message']);
          setState(() {
            _isLoading = false;
          });
        }
      }
    }
  }

  Future<void> _handleLogout() async {
    final confirm = await showDialog<bool>(
      context: context,
      builder: (context) => AlertDialog(
        title: const Text('Log Out'),
        content: const Text('Are you sure you want to log out?'),
        actions: [
          TextButton(
            onPressed: () => Navigator.pop(context, false),
            child: const Text('Cancel'),
          ),
          ElevatedButton(
            onPressed: () => Navigator.pop(context, true),
            child: const Text('Log Out'),
          ),
        ],
      ),
    );

    if (confirm == true && mounted) {
      setState(() {
        _isLoading = true;
      });

      await _authService.logout();

      if (mounted) {
        Navigator.pushAndRemoveUntil(
          context,
          MaterialPageRoute(builder: (context) => const LoginScreen()),
          (route) => false,
        );
      }
    }
  }

  Future<void> _handleDeleteAccount() async {
    if (_profile == null) return;

    final confirm = await showDialog<bool>(
      context: context,
      builder: (context) => AlertDialog(
        title: const Row(
          children: [
            Icon(Icons.warning_amber_rounded, color: Colors.red),
            SizedBox(width: 8),
            Text('Delete Account'),
          ],
        ),
        content: const Text(
          'WARNING: This action is permanent! All your personal details, addresses, and order information will be deleted. Are you sure you want to delete your account?',
        ),
        actions: [
          TextButton(
            onPressed: () => Navigator.pop(context, false),
            child: const Text('Cancel'),
          ),
          ElevatedButton(
            onPressed: () => Navigator.pop(context, true),
            style: ElevatedButton.styleFrom(backgroundColor: Colors.red),
            child: const Text('Delete Permanently'),
          ),
        ],
      ),
    );

    if (confirm == true && mounted) {
      setState(() {
        _isLoading = true;
      });

      final apiResult = await _customerService.deleteAccount(_profile!.id);

      if (mounted) {
        if (apiResult['success'] == true) {
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(
              content: Text(apiResult['message']),
              backgroundColor: Colors.green,
            ),
          );
          Navigator.pushAndRemoveUntil(
            context,
            MaterialPageRoute(builder: (context) => const LoginScreen()),
            (route) => false,
          );
        } else {
          _showErrorDialog(apiResult['message']);
          setState(() {
            _isLoading = false;
          });
        }
      }
    }
  }

  void _showErrorDialog(String message) {
    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: const Row(
          children: [
            Icon(Icons.error_outline, color: Colors.red),
            SizedBox(width: 8),
            Text('Error'),
          ],
        ),
        content: Text(message),
        actions: [
          TextButton(
            onPressed: () => Navigator.pop(context),
            child: const Text('OK'),
          ),
        ],
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);

    if (_isLoading) {
      return Scaffold(
        appBar: AppBar(title: const Text('Profile')),
        body: const ProfileShimmer(),
      );
    }

    if (_errorMessage != null) {
      return Scaffold(
        appBar: AppBar(title: const Text('Profile')),
        body: ErrorView(message: _errorMessage!, onRetry: _loadProfile),
      );
    }

    return DefaultTabController(
      length: 3,
      child: Scaffold(
        backgroundColor: theme.colorScheme.surface,
        appBar: AppBar(
          title: const Text(
            'My Profile',
            style: TextStyle(fontWeight: FontWeight.bold),
          ),
          bottom: const TabBar(
            tabs: [
              Tab(icon: Icon(Icons.person), text: 'Details'),
              Tab(icon: Icon(Icons.location_on), text: 'Addresses'),
              Tab(icon: Icon(Icons.security), text: 'Security'),
            ],
          ),
          actions: [
            IconButton(
              icon: const Icon(Icons.logout),
              tooltip: 'Log Out',
              onPressed: _handleLogout,
            ),
          ],
        ),
        body: TabBarView(
          children: [
            ProfileDetailsTab(
              profile: _profile!,
              isSaving: _isSaving,
              onSave: _handleUpdateProfile,
              onDeleteAccount: _handleDeleteAccount,
              onMyOrdersPressed: () {
                Navigator.push(
                  context,
                  MaterialPageRoute(
                    builder: (context) => const OrdersScreen(),
                  ),
                );
              },
            ),
            ProfileAddressesTab(
              addresses: _profile!.addresses,
              onAddAddress: _handleAddAddress,
              onEditAddress: _handleEditAddress,
              onDeleteAddress: _handleDeleteAddress,
            ),
            ProfileSecurityTab(
              isSaving: _isSaving,
              onUpdatePassword: _handleUpdatePassword,
            ),
          ],
        ),
      ),
    );
  }
}
