# 📱 E-Commerce Mobile App (Flutter)

A modern cross-platform mobile e-commerce application built with **Flutter** & **Dart**. Designed for seamless mobile shopping, featuring real-time catalog browsing, category filters, cart management, checkout with order status tracking, and customer address management.

---

## ✨ Features

- **🔐 Customer Authentication**
  - Sign in, registration, and persistent session management (`SharedPreferences`).
  - Automatic token expiration handling & secure API requests.

- **🛍️ Product Discovery & Catalog**
  - Grid-based product browsing with responsive layout.
  - Category filtering and search capabilities.
  - Product detail view with multi-image gallery support and stock status.

- **🛒 Cart Management**
  - Add to cart with custom quantity.
  - Real-time cart synchronization with backend server.
  - Instant total price calculation and item removal.

- **📦 Order Tracking**
  - Checkout flow converting cart to orders.
  - My Orders list with real-time status chips (`PENDING`, `PROCESSING`, `SHIPPED`, `DELIVERED`, `CANCELLED`).
  - Order details view with item breakdown.

- **👤 Profile & Address Book**
  - Customer profile overview.
  - Shipping and billing address management (Add, update, delete).

---

## 🛠 Tech Stack & Dependencies

- **Framework:** Flutter SDK (Dart)
- **Networking:** `http` package with centralized `BaseApiClient`
- **State & Storage:** `shared_preferences` for token and preference persistence
- **UI & Styling:** `google_fonts`, `shimmer` (skeleton loading), `cached_network_image`, `cupertino_icons`

---

## 📂 Project Structure

```text
mobile/
├── lib/
│   ├── main.dart               # App entry point & routes
│   ├── models/                 # Data models (Product, Category, Order, Cart, Address, User)
│   ├── notifiers/              # State listeners
│   ├── screens/                # UI screens
│   │   ├── auth/               # Login & Register screens
│   │   ├── cart/               # Cart screen & checkout widgets
│   │   ├── profile/            # Profile & address management
│   │   ├── home_screen.dart    # Product catalog & home page
│   │   ├── product_detail_screen.dart # Detailed product view
│   │   └── orders_screen.dart  # Order history and status tracking
│   ├── services/               # API clients & service integrations
│   │   ├── api/                # Modular API services (Auth, Product, Cart, Order, etc.)
│   │   └── api_service.dart    # Main API client facade
│   └── widgets/                # Reusable UI components
├── pubspec.yaml                # Dependencies & Flutter configurations
└── android/ & ios/             # Native platform project wrappers
```

---

## 🚀 Getting Started

### Prerequisites

- [Flutter SDK](https://docs.flutter.dev/get-started/install) (3.12+ recommended)
- Android Studio / Xcode (for device emulators)
- Running **E-Commerce Backend** API server

### Configuration

Open [`lib/services/api/base_api_client.dart`](lib/services/api/base_api_client.dart):

```dart
// For Android Emulator, use: http://10.0.2.2:9191/api/v1
// For iOS Simulator, use: http://localhost:9191/api/v1
// For physical device testing, use your machine's local IP (e.g. http://YOUR_LOCAL_IP:9191/api/v1):
static const String baseUrl = 'http://10.0.2.2:9191/api/v1';
```

### Installation & Run

1. Navigate to the mobile directory:
   ```bash
   cd mobile
   ```

2. Fetch Flutter packages:
   ```bash
   flutter pub get
   ```

3. Run on your connected device or emulator:
   ```bash
   flutter run
   ```
