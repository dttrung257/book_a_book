export interface OrderInfo {
  id: string;
  userId: string;
  fullName: string;
  email: string;
  orderDate: string;
  address: string;
  quantity: number;
  total: number;
  status: string;
}
export interface PersonalOrder {
  id: string;
  orderDate: Date;
  address: string;
  quantity: number;
  total?: number;
  status: string;
}
