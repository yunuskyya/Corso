import {axiosInstance} from './interceptor'
import { CUSTOMER_URL_LIST_FOR_BROKER, CURRENCY_URL_COST, ACCOUNT_URL_LIST_FOR_BROKER, TRANSACTION_URL_CREATE, TRANSACTION_URL_GET_ALL_BY_BROKER, TRANSACTION_URL_GET_ALL_FOR_CUSTOMER } from '../constants/apiUrl';


export const fetchCustomerList = async (userId) => {
    try {

        const response = await axiosInstance.get(CUSTOMER_URL_LIST_FOR_BROKER.replace('{userId}', parseInt(userId)));
        return response.data;
    } catch (error) {
        console.error('Error fetching customer list:', error);
        throw error;
    }
};

export const fetchTransactionListForBroker = async (userId) => {
    try {
        const response = await axiosInstance.get(TRANSACTION_URL_GET_ALL_BY_BROKER.replace('{userId}', parseInt(userId)));
        return response.data; 
    } catch (error) {
        console.error('Error fetching transaction list:', error);
        throw error;
    }
};

export const fetchTransactionListForManager = async () => {
    try {
        const response = await axiosInstance.get(TRANSACTION_URL_GET_ALL_FOR_CUSTOMER);
        return response.data; 
    } catch (error) {
        console.error('Error fetching transaction list:', error);
        throw error;
    }
}; 


export const fetchAccountsForCustomerBalanceHigherThanZero = async (customerId) => {
    try {
        const response = await axiosInstance.get(ACCOUNT_URL_LIST_FOR_BROKER.replace('{customerId}', customerId));
        return response.data; // Bu, GetCustomerAccountsForTransactionPage sınıfının listesi olacak.
    } catch (error) {
        console.error('Error fetching accounts for customer:', error);
        throw error;
    }
};

export const fetchCurrencyCost = async (purchasedCurrencyCode, soldCurrencyCode, selectedAccountBalance) => {
    try {
        const response = await axiosInstance.post(CURRENCY_URL_COST, {
            soldCurrencyCode,
            purchasedCurrencyCode,
            selectedAccountBalance
        }, {
            headers: {
                'Content-Type': 'application/json'
            }
        });
        return response.data;
    } catch (error) {
        console.error('Error fetching currency cost:', error);
        throw error;
    }
};

export const createTransaction = async (account_id, purchasedCurrency, soldCurrency, amount, user_id) => {
    try {
        const response = await axiosInstance.post(TRANSACTION_URL_CREATE, {
            account_id,
            soldCurrency,
            purchasedCurrency,
            amount,
            user_id
        }, {
            headers: {
                'Content-Type': 'application/json'
            }
        });
        return response.data;
    } catch (error) {
        console.error('Error creating transaction:', error);
        throw error;
    }
};
