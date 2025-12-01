export interface User {
  username: string;
  role: 'CUSTOMER' | 'ADMIN';
  cardNumber?: string;
}

export interface Transaction {
  id: number;
  cardNumber: string;
  type: 'withdraw' | 'topup';
  amount: number;
  timestamp: string;
  status: 'SUCCESS' | 'FAILED';
  reason: string;
}

export interface TransactionRequest {
  cardNumber: string;
  pin: string;
  amount: number;
  type: 'withdraw' | 'topup';
}

export interface ApiResponse {
  success: boolean;
  message: string;
  data?: any;
  timestamp?: string;
}

export interface CardBalance {
  cardNumber: string;
  balance: number;
  customerName: string;
}