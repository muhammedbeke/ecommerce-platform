import 'package:flutter/material.dart';
import '../../../models/user_profile.dart';

class ProfileDetailsTab extends StatefulWidget {
  final UserProfile profile;
  final bool isSaving;
  final Function(String email, String phone) onSave;
  final VoidCallback onDeleteAccount;
  final VoidCallback onMyOrdersPressed;

  const ProfileDetailsTab({
    super.key,
    required this.profile,
    required this.isSaving,
    required this.onSave,
    required this.onDeleteAccount,
    required this.onMyOrdersPressed,
  });

  @override
  State<ProfileDetailsTab> createState() => _ProfileDetailsTabState();
}

class _ProfileDetailsTabState extends State<ProfileDetailsTab> {
  final _profileFormKey = GlobalKey<FormState>();
  late final TextEditingController _emailController;
  late final TextEditingController _phoneController;

  @override
  void initState() {
    super.initState();
    _emailController = TextEditingController(text: widget.profile.email);
    _phoneController = TextEditingController(text: widget.profile.phone);
  }

  @override
  void didUpdateWidget(covariant ProfileDetailsTab oldWidget) {
    super.didUpdateWidget(oldWidget);
    if (oldWidget.profile.email != widget.profile.email) {
      _emailController.text = widget.profile.email;
    }
    if (oldWidget.profile.phone != widget.profile.phone) {
      _phoneController.text = widget.profile.phone;
    }
  }

  @override
  void dispose() {
    _emailController.dispose();
    _phoneController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);

    return SingleChildScrollView(
      physics: const AlwaysScrollableScrollPhysics(),
      padding: const EdgeInsets.all(24.0),
      child: Form(
        key: _profileFormKey,
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            // Avatar block
            Center(
              child: CircleAvatar(
                radius: 50,
                backgroundColor: theme.colorScheme.primary.withValues(alpha: 0.1),
                child: Icon(
                  Icons.person_rounded,
                  size: 50,
                  color: theme.colorScheme.primary,
                ),
              ),
            ),
            const SizedBox(height: 24),

            // My Orders Button
            OutlinedButton.icon(
              onPressed: widget.onMyOrdersPressed,
              icon: const Icon(Icons.shopping_bag_outlined),
              label: const Text('My Orders'),
              style: OutlinedButton.styleFrom(
                padding: const EdgeInsets.symmetric(vertical: 14),
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(16.0),
                ),
              ),
            ),
            const SizedBox(height: 24),

            // Current email display / input
            TextFormField(
              controller: _emailController,
              keyboardType: TextInputType.emailAddress,
              decoration: InputDecoration(
                labelText: 'Email Address',
                prefixIcon: const Icon(Icons.email_outlined),
                border: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(16.0),
                ),
              ),
              validator: (value) {
                if (value == null || value.trim().isEmpty) {
                  return 'Email is required';
                }
                return null;
              },
            ),
            const SizedBox(height: 20),

            // Current phone input
            TextFormField(
              controller: _phoneController,
              keyboardType: TextInputType.phone,
              decoration: InputDecoration(
                labelText: 'Phone Number',
                prefixIcon: const Icon(Icons.phone_outlined),
                border: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(16.0),
                ),
              ),
              validator: (value) {
                if (value == null || value.trim().isEmpty) {
                  return 'Phone number is required';
                }
                return null;
              },
            ),
            const SizedBox(height: 32),

            // Save profile button
            ElevatedButton(
              onPressed: widget.isSaving
                  ? null
                  : () {
                      if (_profileFormKey.currentState!.validate()) {
                        widget.onSave(
                          _emailController.text.trim(),
                          _phoneController.text.trim(),
                        );
                      }
                    },
              style: ElevatedButton.styleFrom(
                backgroundColor: theme.colorScheme.primary,
                foregroundColor: theme.colorScheme.onPrimary,
                padding: const EdgeInsets.symmetric(vertical: 16),
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(16.0),
                ),
              ),
              child: widget.isSaving
                  ? const SizedBox(
                      height: 20,
                      width: 20,
                      child: CircularProgressIndicator(
                        strokeWidth: 2,
                        valueColor: AlwaysStoppedAnimation(Colors.white),
                      ),
                    )
                  : const Text(
                      'Save Profile Details',
                      style: TextStyle(
                        fontSize: 16,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
            ),
            const SizedBox(height: 48),

            // Danger zone
            const Divider(color: Colors.redAccent),
            const SizedBox(height: 8),
            Text(
              'Danger Zone',
              style: theme.textTheme.titleMedium?.copyWith(
                color: Colors.redAccent,
                fontWeight: FontWeight.bold,
              ),
            ),
            const SizedBox(height: 12),
            OutlinedButton.icon(
              onPressed: widget.isSaving ? null : widget.onDeleteAccount,
              icon: const Icon(
                Icons.delete_forever,
                color: Colors.red,
              ),
              label: const Text(
                'Delete Account',
                style: TextStyle(color: Colors.red),
              ),
              style: OutlinedButton.styleFrom(
                side: const BorderSide(color: Colors.red),
                padding: const EdgeInsets.symmetric(vertical: 16),
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(16.0),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
