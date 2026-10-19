import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { LayoutComponent } from './components/layout/layout.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { ProductsComponent } from './pages/products/products.component';
import { CategoriesComponent } from './pages/categories/categories.component';
import { OrdersComponent } from './pages/orders/orders.component';
import { BuyingsComponent } from './pages/buyings/buyings.component';
import { StocksComponent } from './pages/stocks/stocks.component';
import { CustomersComponent } from './pages/customers/customers.component';
import { AddressesComponent } from './pages/addresses/addresses.component';
import { RolesComponent } from './pages/roles/roles.component';
import { ListOfValuesComponent } from './pages/list-of-values/list-of-values.component';
import { authGuard } from './services/auth.guard';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  {
    path: 'admin',
    component: LayoutComponent,
    canActivate: [authGuard],
    children: [
      { path: 'dashboard', component: DashboardComponent },
      { path: 'products', component: ProductsComponent },
      { path: 'categories', component: CategoriesComponent },
      { path: 'orders', component: OrdersComponent },
      { path: 'buyings', component: BuyingsComponent },
      { path: 'stocks', component: StocksComponent },
      { path: 'customers', component: CustomersComponent },
      { path: 'addresses', component: AddressesComponent },
      { path: 'roles', component: RolesComponent },
      { path: 'list-of-values', component: ListOfValuesComponent },
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' }
    ]
  },
  { path: '', redirectTo: 'admin/dashboard', pathMatch: 'full' },
  { path: '**', redirectTo: 'login' }
];
