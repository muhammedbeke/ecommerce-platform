# 💻 E-Commerce Admin & Web Frontend (Angular)

A modern, responsive **Store Administration & Management Dashboard** built with **Angular 22** (using modern Standalone Components, Signals, and typed reactive state). It connects directly to the Spring Boot REST API to provide complete back-office operational control.

---

## ✨ Features

- **📊 Dashboard Overview**
  - KPI summary metrics, business performance insights, and quick navigation.

- **📦 Product & Category Administration**
  - Create, view, update, and delete products.
  - Hierarchical category management with parent-child category tree.
  - Product image upload and BLOB storage stream previews.

- **📋 Order Workflow Management**
  - Complete order monitoring across all customer accounts.
  - Real-time status transitions (`PENDING` ➔ `PROCESSING` ➔ `SHIPPED` ➔ `DELIVERED`).
  - Order cancellation and status rollback controls.

- **📈 Inventory & Stock Control**
  - Live inventory tracking by product.
  - Quick stock increment/decrement and absolute count adjustments.

- **🚚 Procurement & Wholesale Buying**
  - Supplier procurement record entry, tracking batch purchase costs and supplier inventory entries.

- **👥 Customers & Addresses**
  - Customer directory view, detailed profile inspect, and address book oversight.

- **⚙️ System Configuration (Roles & LOV)**
  - Dynamic List of Values (LOV) control.
  - Role definitions and permissions oversight.

- **🛡️ Secure JWT Authentication**
  - Angular HTTP interceptor injecting `Bearer` tokens into requests.
  - Session guards (`AuthGuard`) protecting management routes.
  - Graceful token expiration handling and automatic logout redirection.

---

## 🛠 Tech Stack

- **Framework:** Angular 22 (Standalone Components, Signals API)
- **Language:** TypeScript 5+
- **Styling:** Modern CSS3 Design System with responsive layouts
- **HTTP Client:** Angular `@angular/common/http` with functional Interceptors
- **Testing:** Vitest

---

## 📂 Project Structure

```text
frontend/
├── src/
│   ├── app/
│   │   ├── components/         # Shared UI components (Navbar, Sidebar, Modals, Tables)
│   │   ├── models/             # API request/response TypeScript interfaces
│   │   ├── pages/              # Module screens
│   │   │   ├── dashboard/      # Main analytics dashboard
│   │   │   ├── login/          # Admin authentication
│   │   │   ├── products/       # Product catalog management
│   │   │   ├── categories/     # Category hierarchy
│   │   │   ├── orders/         # Order state machine workflow
│   │   │   ├── stocks/         # Live inventory controls
│   │   │   ├── buyings/        # Procurement entries
│   │   │   ├── customers/      # Customer directory
│   │   │   ├── addresses/      # Address book
│   │   │   ├── roles/          # Security roles
│   │   │   └── list-of-values/ # LOV configuration
│   │   ├── services/           # REST API services & auth interceptors
│   │   ├── app.config.ts       # Application providers
│   │   └── app.routes.ts       # Route guards and definitions
│   └── environments/           # Environment configuration (API URL)
├── package.json
└── angular.json
```

---

## 🚀 Getting Started

### Prerequisites

- [Node.js](https://nodejs.org/) (v20+ recommended)
- [npm](https://www.npmjs.com/) (v10+)
- Running **E-Commerce Backend** API server

### Configuration

Check `src/environments/environment.ts` to ensure the backend API URL matches your running server:

```typescript
export const environment = {
  production: false,
  apiUrl: 'http://localhost:9191/api/v1'
};
```

### Installation & Run

1. Navigate to the frontend directory:
   ```bash
   cd frontend
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Start the Angular development server:
   ```bash
   npm start
   # or: npx ng serve
   ```

4. Open your browser at:
   ```text
   http://localhost:4200
   ```

5. Log in with the default admin credentials:
   - **Email:** `admin@email.com`
   - **Password:** `123456`
