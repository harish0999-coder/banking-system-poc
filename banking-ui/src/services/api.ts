import axios from "axios";
import type { TransactionRequest, ApiResponse } from "../types/types";

const SYSTEM1_URL = "http://localhost:8081/api";
const SYSTEM2_URL = "http://localhost:8082/api";

// Create axios instance with timeout
const axiosInstance = axios.create({
  timeout: 10000,
});

// Add request interceptor
axiosInstance.interceptors.request.use(
  (config) => {
    console.log(`API Request: ${config.method?.toUpperCase()} ${config.url}`);
    return config;
  },
  (error) => Promise.reject(error)
);

// Add response interceptor
axiosInstance.interceptors.response.use(
  (response) => response,
  (error) => {
    console.error("API Error:", error.message);
    return Promise.reject(error);
  }
);

// Helper function to handle errors consistently
function handleError(error: any, defaultMessage: string): ApiResponse {
  const message = error?.response?.data?.message ?? error?.message ?? defaultMessage;
  return {
    success: false,
    message,
    data: null,
  };
}

// ================= Transaction Processor (System 1) =================
export async function processTransaction(request: TransactionRequest): Promise<ApiResponse> {
  try {
    const response = await axiosInstance.post<ApiResponse>(
      `${SYSTEM1_URL}/transaction`,
      request
    );
    return response.data;
  } catch (error: any) {
    return handleError(error, "Transaction failed");
  }
}

// ================= System 2 Balance =================
export async function getBalance(cardNumber: string): Promise<ApiResponse> {
  try {
    const response = await axiosInstance.get<ApiResponse>(
      `${SYSTEM2_URL}/customer/balance/${cardNumber}`
    );
    return response.data;
  } catch (error: any) {
    return handleError(error, "Failed to fetch balance");
  }
}

// ================= Customer Transactions =================
export async function getTransactions(cardNumber: string): Promise<ApiResponse> {
  try {
    const response = await axiosInstance.get<ApiResponse>(
      `${SYSTEM2_URL}/customer/transactions/${cardNumber}`
    );
    return response.data;
  } catch (error: any) {
    return handleError(error, "Failed to fetch transactions");
  }
}

// ================= Admin All Transactions =================
export async function getAllTransactions(): Promise<ApiResponse> {
  try {
    const response = await axiosInstance.get<ApiResponse>(
      `${SYSTEM2_URL}/customer/transactions/all`
    );
    return response.data;
  } catch (error: any) {
    return handleError(error, "Failed to fetch all transactions");
  }
}

// ================= Health Check =================
export async function healthCheck(): Promise<{ system1: boolean; system2: boolean }> {
  try {
    const [sys1, sys2] = await Promise.all([
      axiosInstance.get(`${SYSTEM1_URL}/health`).catch(() => ({ status: 0 })),
      axiosInstance.get(`${SYSTEM2_URL}/health`).catch(() => ({ status: 0 })),
    ]);
    return {
      system1: sys1.status === 200,
      system2: sys2.status === 200,
    };
  } catch {
    return { system1: false, system2: false };
  }
}

// ================= Export Named Functions (for direct imports) =================
export default {
  processTransaction,
  getBalance,
  getTransactions,
  getAllTransactions,
  healthCheck,
};