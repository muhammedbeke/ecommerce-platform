import 'package:flutter/material.dart';
import 'package:shimmer/shimmer.dart';

class ShimmerBlock extends StatelessWidget {
  final double width;
  final double height;
  final double borderRadius;
  final bool isCircular;

  const ShimmerBlock.rectangular({
    super.key,
    this.width = double.infinity,
    required this.height,
    this.borderRadius = 8.0,
  }) : isCircular = false;

  const ShimmerBlock.circular({
    super.key,
    required this.width,
    required this.height,
  }) : borderRadius = 0.0,
       isCircular = true;

  @override
  Widget build(BuildContext context) {
    final isDark = Theme.of(context).brightness == Brightness.dark;

    // Smooth modern slate/grey tones for light/dark modes
    final baseColor = isDark ? const Color(0xFF1E293B) : Colors.grey[300]!;
    final highlightColor = isDark ? const Color(0xFF334155) : Colors.grey[100]!;

    return Shimmer.fromColors(
      baseColor: baseColor,
      highlightColor: highlightColor,
      child: Container(
        width: width,
        height: height,
        decoration: BoxDecoration(
          color: isDark ? const Color(0xFF0F172A) : Colors.white,
          shape: isCircular ? BoxShape.circle : BoxShape.rectangle,
          borderRadius: isCircular ? null : BorderRadius.circular(borderRadius),
        ),
      ),
    );
  }
}

class ProductGridShimmer extends StatelessWidget {
  const ProductGridShimmer({super.key});

  @override
  Widget build(BuildContext context) {
    return SingleChildScrollView(
      physics: const NeverScrollableScrollPhysics(),
      child: Padding(
        padding: const EdgeInsets.symmetric(vertical: 16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // Category Title Shimmer
            const Padding(
              padding: EdgeInsets.symmetric(horizontal: 16.0, vertical: 8.0),
              child: ShimmerBlock.rectangular(width: 120, height: 20),
            ),

            SizedBox(
              height: 48,
              child: ListView.builder(
                scrollDirection: Axis.horizontal,
                padding: const EdgeInsets.symmetric(horizontal: 16.0),
                itemCount: 5,
                itemBuilder: (context, index) {
                  return const Padding(
                    padding: EdgeInsets.only(right: 12.0, top: 4, bottom: 4),
                    child: ShimmerBlock.rectangular(
                      width: 90,
                      height: 40,
                      borderRadius: 20,
                    ),
                  );
                },
              ),
            ),
            const SizedBox(height: 16),

            // Products Section Title Shimmer
            const Padding(
              padding: EdgeInsets.symmetric(horizontal: 16.0, vertical: 8.0),
              child: Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  ShimmerBlock.rectangular(width: 140, height: 20),
                  ShimmerBlock.rectangular(width: 50, height: 16),
                ],
              ),
            ),
            const SizedBox(height: 8),

            // Products Grid Shimmer
            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 16.0),
              child: GridView.builder(
                shrinkWrap: true,
                physics: const NeverScrollableScrollPhysics(),
                gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
                  crossAxisCount: 2,
                  childAspectRatio: 0.70,
                  crossAxisSpacing: 16,
                  mainAxisSpacing: 16,
                ),
                itemCount: 4,
                itemBuilder: (context, index) {
                  return const Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      // Product Image Shimmer
                      Expanded(
                        child: ShimmerBlock.rectangular(
                          height: double.infinity,
                          borderRadius: 16,
                        ),
                      ),
                      SizedBox(height: 8),
                      // Product Name Shimmer
                      ShimmerBlock.rectangular(width: 120, height: 16),
                      SizedBox(height: 4),
                      // Product Category Shimmer
                      ShimmerBlock.rectangular(width: 80, height: 12),
                      SizedBox(height: 8),
                      // Product Price Shimmer
                      Row(
                        mainAxisAlignment: MainAxisAlignment.spaceBetween,
                        children: [
                          ShimmerBlock.rectangular(width: 60, height: 18),
                          ShimmerBlock.circular(width: 24, height: 24),
                        ],
                      ),
                    ],
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

class ProductDetailShimmer extends StatelessWidget {
  const ProductDetailShimmer({super.key});

  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        const Expanded(
          child: SingleChildScrollView(
            physics: NeverScrollableScrollPhysics(),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                // Image Box Shimmer
                ShimmerBlock.rectangular(height: 300, borderRadius: 0),
                SizedBox(height: 20),

                Padding(
                  padding: EdgeInsets.all(20.0),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      // Brand & Category Row Shimmer
                      Row(
                        mainAxisAlignment: MainAxisAlignment.spaceBetween,
                        children: [
                          ShimmerBlock.rectangular(
                            width: 80,
                            height: 20,
                            borderRadius: 8,
                          ),
                          ShimmerBlock.rectangular(width: 100, height: 16),
                        ],
                      ),
                      SizedBox(height: 16),

                      // Product Title Shimmer
                      ShimmerBlock.rectangular(width: 240, height: 28),
                      SizedBox(height: 16),

                      // Price and Stock status Shimmer
                      Row(
                        mainAxisAlignment: MainAxisAlignment.spaceBetween,
                        children: [
                          ShimmerBlock.rectangular(width: 100, height: 30),
                          ShimmerBlock.rectangular(width: 120, height: 20),
                        ],
                      ),
                      SizedBox(height: 24),
                      Divider(),
                      SizedBox(height: 16),

                      // Description Title Shimmer
                      ShimmerBlock.rectangular(width: 100, height: 20),
                      SizedBox(height: 12),

                      // Description Lines Shimmer
                      ShimmerBlock.rectangular(height: 14),
                      SizedBox(height: 8),
                      ShimmerBlock.rectangular(height: 14),
                      SizedBox(height: 8),
                      ShimmerBlock.rectangular(width: 250, height: 14),
                    ],
                  ),
                ),
              ],
            ),
          ),
        ),

        // Purchase Bar Shimmer at the bottom
        Container(
          padding: const EdgeInsets.all(20),
          decoration: BoxDecoration(
            color: Theme.of(context).cardColor,
            border: Border(
              top: BorderSide(color: Colors.grey.withValues(alpha: 0.1)),
            ),
          ),
          child: const Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              ShimmerBlock.rectangular(
                width: 120,
                height: 40,
                borderRadius: 12,
              ),
              ShimmerBlock.rectangular(
                width: 160,
                height: 50,
                borderRadius: 16,
              ),
            ],
          ),
        ),
      ],
    );
  }
}

class ProfileShimmer extends StatelessWidget {
  const ProfileShimmer({super.key});

  @override
  Widget build(BuildContext context) {
    return const Padding(
      padding: EdgeInsets.all(24.0),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: [
          // Circular Avatar Placeholder
          Center(child: ShimmerBlock.circular(width: 100, height: 100)),
          SizedBox(height: 24),

          // Orders Button Shimmer
          ShimmerBlock.rectangular(height: 50, borderRadius: 16),
          SizedBox(height: 32),

          // Form fields Shimmer
          ShimmerBlock.rectangular(height: 60, borderRadius: 16),
          SizedBox(height: 20),
          ShimmerBlock.rectangular(height: 60, borderRadius: 16),
          SizedBox(height: 32),

          // Save Details button Shimmer
          ShimmerBlock.rectangular(height: 50, borderRadius: 16),
          SizedBox(height: 48),

          // Danger zone divider & title
          Divider(color: Colors.redAccent),
          SizedBox(height: 8),
          ShimmerBlock.rectangular(width: 100, height: 20),
          SizedBox(height: 12),
          ShimmerBlock.rectangular(height: 50, borderRadius: 16),
        ],
      ),
    );
  }
}

class CartShimmer extends StatelessWidget {
  const CartShimmer({super.key});

  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        // Cart Items list
        Expanded(
          child: ListView.builder(
            padding: const EdgeInsets.all(16.0),
            itemCount: 3,
            itemBuilder: (context, index) {
              return Padding(
                padding: const EdgeInsets.only(bottom: 16.0),
                child: Container(
                  padding: const EdgeInsets.all(16.0),
                  decoration: BoxDecoration(
                    color: Theme.of(context).cardColor,
                    borderRadius: BorderRadius.circular(16.0),
                  ),
                  child: const Row(
                    children: [
                      // Product texts shimmer
                      Expanded(
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            ShimmerBlock.rectangular(width: 180, height: 18),
                            SizedBox(height: 8),
                            ShimmerBlock.rectangular(width: 80, height: 14),
                            SizedBox(height: 6),
                            ShimmerBlock.rectangular(width: 100, height: 16),
                          ],
                        ),
                      ),
                      SizedBox(width: 16),
                      // Quantity selector outline shimmer
                      ShimmerBlock.rectangular(
                        width: 90,
                        height: 36,
                        borderRadius: 10,
                      ),
                      SizedBox(width: 8),
                      // Delete icon shimmer
                      ShimmerBlock.circular(width: 32, height: 32),
                    ],
                  ),
                ),
              );
            },
          ),
        ),

        // Checkout bar
        Container(
          padding: const EdgeInsets.all(20.0),
          decoration: BoxDecoration(
            color: Theme.of(context).cardColor,
            boxShadow: [
              BoxShadow(
                color: Colors.black.withValues(alpha: 0.05),
                blurRadius: 10,
                offset: const Offset(0, -5),
              ),
            ],
          ),
          child: const Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  ShimmerBlock.rectangular(width: 100, height: 18),
                  ShimmerBlock.rectangular(width: 80, height: 22),
                ],
              ),
              SizedBox(height: 16),
              ShimmerBlock.rectangular(height: 50, borderRadius: 12),
            ],
          ),
        ),
      ],
    );
  }
}

class OrdersShimmer extends StatelessWidget {
  const OrdersShimmer({super.key});

  @override
  Widget build(BuildContext context) {
    return ListView.builder(
      padding: const EdgeInsets.all(16.0),
      itemCount: 4,
      itemBuilder: (context, index) {
        return Padding(
          padding: const EdgeInsets.only(bottom: 16.0),
          child: Container(
            padding: const EdgeInsets.all(16.0),
            decoration: BoxDecoration(
              color: Theme.of(context).cardColor,
              borderRadius: BorderRadius.circular(16.0),
            ),
            child: const Column(
              children: [
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    ShimmerBlock.rectangular(width: 100, height: 18),
                    ShimmerBlock.rectangular(
                      width: 80,
                      height: 22,
                      borderRadius: 8,
                    ),
                  ],
                ),
                SizedBox(height: 12),
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    ShimmerBlock.rectangular(width: 140, height: 14),
                    ShimmerBlock.rectangular(width: 60, height: 16),
                  ],
                ),
              ],
            ),
          ),
        );
      },
    );
  }
}
