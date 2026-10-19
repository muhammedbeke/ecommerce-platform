export interface ApiResponse<T = any> {
  message: string;
  data: T;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface JwtResponse {
  token: string;
  email: string;
  role: string;
}

export interface CategoryDto {
  id: number;
  name: string;
  description: string;
  parentCategoryId?: number;
}

export interface CategoryRequest {
  name: string;
  description?: string;
  parentCategoryId?: number | null;
}

export interface ImageDto {
  id: number;
  fileName: string;
  fileType: string;
  downloadUrl: string;
}

export interface ProductDto {
  id: number;
  name: string;
  description: string;
  price: number;
  stock: number;
  brand: string;
  lowStockThreshold?: number;
  category?: CategoryDto;
  images?: ImageDto[];
}

export interface ProductRequest {
  name: string;
  description?: string;
  brand?: string;
  categoryId?: number;
  price?: number;
  lowStockThreshold?: number;
}

export interface AddressDto {
  id: number;
  title: string;
  addressLine: string;
  customerId: number;
}

export interface AddressRequest {
  title: string;
  addressLine: string;
  customerId?: number;
}

export interface CustomerDto {
  id: number;
  email: string;
  phone: string;
  role: string;
  addresses?: AddressDto[];
}

export interface CustomerRequest {
  email: string;
  phone: string;
  password?: string;
  oldPassword?: string;
}

export interface OrderDetailDto {
  id: number;
  productId: number;
  productName: string;
  quantity: number;
  price: number;
}

export interface OrderDto {
  id: number;
  customerId: number;
  orderDate: string;
  orderStatus: string;
  orderDetails: OrderDetailDto[];
}

export interface OrderItemRequest {
  productId: number;
  quantity: number;
  price?: number;
}

export interface OrderRequest {
  customerId?: number;
  items?: OrderItemRequest[];
}

export interface BuyingDetailDto {
  id: number;
  productId: number;
  productName: string;
  quantity: number;
  price: number;
}

export interface BuyingDto {
  id: number;
  purchaseDate: string;
  buyingDetails: BuyingDetailDto[];
}

export interface BuyingItemRequest {
  productId: number;
  quantity: number;
  price: number;
}

export interface BuyingRequest {
  items: BuyingItemRequest[];
}

export interface StockDto {
  id: number;
  productId: number;
  quantity: number;
}

export interface RoleDto {
  id: number;
  name: string;
}

export interface RoleRequest {
  name: string;
}

export interface ListOfValuesDto {
  id: number;
  code: string;
  value: string;
  type: string;
}

export interface ListOfValuesRequest {
  code: string;
  value: string;
  type: string;
}
