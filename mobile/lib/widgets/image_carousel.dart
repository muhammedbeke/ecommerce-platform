import 'package:flutter/material.dart';
import '../models/product.dart';
import '../services/api_service.dart';
import 'custom_network_image.dart';

class ImageCarousel extends StatefulWidget {
  final Product product;

  const ImageCarousel({super.key, required this.product});

  @override
  State<ImageCarousel> createState() => _ImageCarouselState();
}

class _ImageCarouselState extends State<ImageCarousel> {
  final PageController _pageController = PageController();
  int _activeImageIndex = 0;

  @override
  void dispose() {
    _pageController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    final hasImages = widget.product.images.isNotEmpty;

    return SizedBox(
      height: 320,
      child: Stack(
        children: [
          if (hasImages)
            PageView.builder(
              controller: _pageController,
              itemCount: widget.product.images.length,
              onPageChanged: (index) {
                setState(() {
                  _activeImageIndex = index;
                });
              },
              itemBuilder: (context, index) {
                final image = widget.product.images[index];
                final imageUrl = ApiService.getFullImageUrl(image.downloadUrl);

                return Hero(
                  tag: index == 0
                      ? 'product_image_${widget.product.id}'
                      : 'product_image_${widget.product.id}_$index',
                  child: CustomNetworkImage(
                    imageUrl: imageUrl,
                    fit: BoxFit.cover,
                    width: double.infinity,
                  ),
                );
              },
            )
          else
            Hero(
              tag: 'product_image_${widget.product.id}',
              child: Container(
                color: Colors.grey.shade100,
                width: double.infinity,
                child: const Icon(
                  Icons.image_outlined,
                  size: 80,
                  color: Colors.grey,
                ),
              ),
            ),

          // Carousel Indicator dots
          if (hasImages && widget.product.images.length > 1)
            Positioned(
              bottom: 16,
              left: 0,
              right: 0,
              child: Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: List.generate(
                  widget.product.images.length,
                  (index) => Container(
                    margin: const EdgeInsets.symmetric(horizontal: 4.0),
                    width: 8.0,
                    height: 8.0,
                    decoration: BoxDecoration(
                      shape: BoxShape.circle,
                      color: _activeImageIndex == index
                          ? theme.colorScheme.primary
                          : Colors.grey.shade400,
                    ),
                  ),
                ),
              ),
            ),
        ],
      ),
    );
  }
}
